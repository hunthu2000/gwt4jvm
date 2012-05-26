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
package com.mind.gwt.jclient;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.mind.gwt.jclient.context.Context;
import com.mind.gwt.jclient.metrics.Metrics;

/**
 * <tt>GwtJavaClient</tt> is a browser-like (isolated single-threaded asynchronous) environment that in conjunction
 * with a pure Java implementation of a lot of native components of GWT SDK opens the door to launch GWT-applications
 * inside the Java Virtual Machine.
 * 
 * <p>Must be understood that <tt>GwtJavaClient</tt> doesn't support JavaScript (JSNI) and there is no DOM-emulation.
 * This means that to launch any GWT-application this way it has to contain no GUI and other custom JSNI code. With a
 * help of <a href="http://code.google.com/webtoolkit/doc/latest/DevGuideMvpActivitiesAndPlaces.html">MVP-pattern</a>
 * and wise use of dependency injection that shouldn't be to hard.
 * 
 * <p>There is a special support of GWT-RPC services, and they can be used in exactly the same way as they always were.
 * This feature in conjunction with {@link GwtLoadTest} simplifies the task of performing load testing of GWT-RPC-based
 * applications.
 *
 * @see GwtLoadTest
*/
public abstract class GwtJavaClient implements Runnable
{
    private final Set<GwtJavaClientListener> listeners = new CopyOnWriteArraySet<GwtJavaClientListener>();
    private final ConcurrentMap<Class<? extends Metrics>, Collection<Metrics>> metricsMap = new ConcurrentHashMap<Class<? extends Metrics>, Collection<Metrics>>();
    private final AtomicLong startTime = new AtomicLong(-1);
    private final AtomicLong duration = new AtomicLong(-1);
    private final AtomicBoolean succeed = new AtomicBoolean();
    private final CountDownLatch completeLatch = new CountDownLatch(1);
    private final AtomicReference<Throwable> uncaughtException = new AtomicReference<Throwable>();

    private final String moduleBaseURL;
    private final DeferredBindingFactory deferredBindingFactory;

    public GwtJavaClient(String moduleBaseURL)
    {
        this(moduleBaseURL, DeferredBindingFactory.getDeferredBindingFactory());
    }

    public GwtJavaClient(String moduleBaseURL, DeferredBindingFactory deferredBindingFactory)
    {
        if (moduleBaseURL == null)
        {
            throw new IllegalArgumentException("moduleBaseURL can't be null!");
        }
        if (deferredBindingFactory == null)
        {
            throw new IllegalArgumentException("deferredBindingFactory can't be null!");
        }
        this.moduleBaseURL = moduleBaseURL;
        this.deferredBindingFactory = deferredBindingFactory;
    }

    /**
     * Starts executing specified <tt>run</tt> method in a newly created <tt>Context</tt>. 
    */
    public void start()
    {
        if (!startTime.compareAndSet(-1, System.currentTimeMillis()))
        {
            throw new IllegalStateException(getClass().getSimpleName() + " has been already started!");
        }
        Context context = new Context(this, new UncaughtExceptionHandler()
        {
            @Override
            public void onUncaughtException(Throwable exception)
            {
                uncaughtException.set(exception);
                finish(false);
            }

        });
        context.execute(this);
    }

    public void await() throws InterruptedException
    {
        completeLatch.await();
    }

    public void success()
    {
        finish(true);
    }

    public void failure()
    {
        finish(false);
    }

    public void addListener(GwtJavaClientListener listener)
    {
        listeners.add(listener);
    }

    public long getStartTime()
    {
        return startTime.get();
    }

    public long getDuration(TimeUnit timeUnit)
    {
        return timeUnit.convert(duration.get(), TimeUnit.MILLISECONDS);
    }

    public boolean isFinished()
    {
        return duration.get() != -1;
    }

    public boolean isSucceed()
    {
        return succeed.get();
    }

    public Throwable getUncaughtException()
    {
        return uncaughtException.get();
    }

    public void addMetrics(Metrics metrics)
    {
        metricsMap.putIfAbsent(metrics.getClass(), new ConcurrentLinkedQueue<Metrics>());
        metricsMap.get(metrics.getClass()).add(metrics);
    }

    @SuppressWarnings("unchecked")
    public <T extends Metrics> Collection<T> getMetrics(Class<T> type)
    {
        return (Collection<T>) (metricsMap.containsKey(type) ? Collections.unmodifiableCollection(metricsMap.get(type)) : Collections.emptyList()); 
    }

    // TODO This method should become final...
    public DeferredBindingFactory getDeferredBindingFactory()
    {
        return deferredBindingFactory;
    }

    // TODO This method should become final...
    public String getModuleBaseURL()
    {
        return moduleBaseURL;
    }

    private void finish(boolean succeed)
    {
        // TODO Make it legal to run success and failure methods beyond the context, and convert this check to an assert.
        if (Context.getCurrentContext() == null)
        {
            throw new IllegalStateException("This method can be called within context only!");
        }
        if (!duration.compareAndSet(-1, System.currentTimeMillis() - startTime.get()))
        {
            throw new IllegalStateException(getClass().getSimpleName() + " has been already finished!");
        }
        this.succeed.set(succeed);
        for (GwtJavaClientListener listener : listeners)
        {
            listener.onFinish(this);
        }
        completeLatch.countDown();
    }

}