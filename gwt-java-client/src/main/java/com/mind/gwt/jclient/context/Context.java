/*
 * Copyright 2011 Mind Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
*/
package com.mind.gwt.jclient.context;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.handler.codec.http.Cookie;

import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.mind.gwt.jclient.GwtJavaClient;
import com.mind.gwt.jclient.GwtJavaClientListener;

public class Context
{
    private static final ThreadLocal<Context> currentContext = new ThreadLocal<Context>();

    private final GwtJavaClient client;
    private final UncaughtExceptionHandler uncaughtExceptionHandler;
    private final ExecutorService executorService = ExecutorServiceFactory.getExecutorService(this);
    private final ScheduledExecutorService scheduledExecutorService = ExecutorServiceFactory.getScheduledExecutorService();
    private final Set<Cookie> cookies = Collections.newSetFromMap(new ConcurrentHashMap<Cookie, Boolean>());
    private final Set<ScheduledFuture<?>> scheduledFutures = Collections.newSetFromMap(new ConcurrentHashMap<ScheduledFuture<?>, Boolean>());

    public Context(GwtJavaClient client, UncaughtExceptionHandler uncaughtExceptionHandler)
    {
        this.client = client;
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        client.addListener(new GwtJavaClientListener()
        {
            @Override
            public void onFinish(GwtJavaClient client)
            {
                cancelScheduledFutures();
            }
            
        });
    }
    
    public static Context getCurrentContext()
    {
        if (currentContext.get() == null)
        {
            throw new IllegalStateException("There is no context!");
        }
        return currentContext.get();
    }

    public GwtJavaClient getClient()
    {
        return client;
    }

    public void execute(final Runnable runnable)
    {
        if (client.isFinished()) return;

        executorService.submit(new Runnable()
        {
            @Override
            public void run()
            {
                runInContext(runnable);
            }

        });
    }

    public ScheduledFuture<?> schedule(long delayMs, final Runnable runnable)
    {
        if (client.isFinished()) return null;

        ScheduledFuture<?> feature = scheduledExecutorService.schedule(new Runnable()
        {
            @Override
            public void run()
            {
                runInContext(runnable);
            }

        }, delayMs, TimeUnit.MILLISECONDS);
        scheduledFutures.add(feature);
        return feature;
    }

    public ScheduledFuture<?> scheduleRepeating(long periodMs, final Runnable runnable)
    {
        if (client.isFinished()) return null;

        ScheduledFuture<?> feature = scheduledExecutorService.scheduleWithFixedDelay(new Runnable()
        {
            @Override
            public void run()
            {
                runInContext(runnable);
            }

        }, periodMs, periodMs, TimeUnit.MILLISECONDS);
        scheduledFutures.add(feature);
        return feature;
    }

    /**
     * @deprecated Modify <tt>Set</tt> returned by {@link #getCookies() getCookies} method instead.
    */
    @Deprecated
    public void addCookies(Set<Cookie> cookies)
    {
        this.cookies.addAll(cookies);
    }

    /**
     * Returns a mutable but thread-safe <tt>Set</tt> of cookies associated with the current <tt>Context</tt>. The 
     * result <tt>Set</tt> is backed by {@link Map} and both of {@link Set#addAll(java.util.Collection) addAll} and
     * {@link Set#add(Object) add} methods not only add new cookies to the context but replace already contained. 
    */
    public Set<Cookie> getCookies()
    {
        return cookies;
    }

    private synchronized void runInContext(Runnable runnable)
    {
        currentContext.set(this);
        try
        {
            if (!client.isFinished()) runnable.run();
        }
        catch (Throwable exception)
        {
            uncaughtExceptionHandler.onUncaughtException(exception);
        }
    }

    private void cancelScheduledFutures()
    {
        if (getCurrentContext() != this)
        {
            throw new IllegalStateException("This method can be called within context only!");
        }
        for (ScheduledFuture<?> future : scheduledFutures)
        {
            future.cancel(true);
        }
    }

}