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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.mind.gwt.jclient.context.Context;
import com.mind.gwt.jclient.metrics.Metrics;

/**
 * <tt>GwtJavaClient</tt> is an implementation of {@link EntryPoint} whose {@link #onModuleLoad() onModuleLoad} method
 * can be {@link #start() executed} inside the JVM in an environment that provides a pure Java implementation of a few
 * native objects of GWT SDK. Here is the complete list of them:
 *
 * <ul>
 *   <li>{@link com.google.gwt.xhr.client.XMLHttpRequest}
 *   <li>{@link com.google.gwt.core.client.GWT}
 *   <li>{@link com.google.gwt.user.client.Window}
 *   <li>{@link com.google.gwt.user.client.Cookies}
 *   <li>{@link com.google.gwt.user.client.Timer}
 * </ul>
 *
 * <p>There is a special support of GWT-RPC services. They can be used in exactly the same way as they always were. But
 * that's all. There is no support of GUI or any other custom JSNI. This means that <tt>GwtJavaClient</tt> doesn't open
 * the door to launch the entire GWT-applications inside the JVM.
 *
 * <p>Any instance of <tt>GwtJavaClient</tt> is disposable (i.e. it could be {@link #start() executed} only once), but
 * it is possible to create as many instances as needed and make them all working concurrently. Each instance will have
 * its own isolated environment. This feature in conjunction with {@link GwtLoadTest} simplifies the task of performing
 * load testing of GWT-RPC-based applications.
 * 
 * @see GwtLoadTest
*/
public abstract class GwtJavaClient implements EntryPoint
{
    private final Set<GwtJavaClientListener> listeners = new CopyOnWriteArraySet<GwtJavaClientListener>();
    private final ConcurrentMap<Class<? extends Metrics>, Collection<Metrics>> metricsMap = new ConcurrentHashMap<Class<? extends Metrics>, Collection<Metrics>>();
    private final AtomicLong startTime = new AtomicLong(-1);
    private final AtomicLong duration = new AtomicLong(-1);
    private final AtomicBoolean succeed = new AtomicBoolean();
    private final CountDownLatch completeLatch = new CountDownLatch(1);
    private final AtomicReference<Throwable> uncaughtException = new AtomicReference<Throwable>(); 

    public abstract String getModuleBaseURL();

    /**
     * @deprecated Use {@link #onModuleLoad() onModuleLoad} method instead.  
    */
    @Deprecated
    public void run() {}

    public void onModuleLoad()
    {
        run();
    }

    /**
     * Starts executing {@link #onModuleLoad() onModuleLoad} method in a newly created <tt>Context</tt>. 
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
        context.execute(new Runnable()
        {
            @Override
            public void run()
            {
                onModuleLoad();
            }

        });
    }

    public void await() throws InterruptedException
    {
        completeLatch.await();
    }

    protected void success()
    {
        finish(true);
    }

    protected void failure()
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
        return timeUnit.convert(duration.get(),TimeUnit.MILLISECONDS);
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
        return Collections.unmodifiableCollection((Collection<T>) metricsMap.get(type));
    }

    public DeferredBindingFactory getDeferredBindingFactory()
    {
        return DeferredBindingFactory.getDeferredBindingFactory();
    }

    private void finish(boolean succeed)
    {
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