/*
 * Copyright 2010 Mind Ltd.
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.mind.gwt.jclient.context.Context;
import com.mind.gwt.jclient.metrics.Metrics;

public abstract class GwtJavaClient implements Runnable
{
    private final Set<GwtJavaClientListener> listeners = new CopyOnWriteArraySet<GwtJavaClientListener>();
    private final ConcurrentMap<Class<? extends Metrics>, Collection<Metrics>> metricsMap = new ConcurrentHashMap<Class<? extends Metrics>, Collection<Metrics>>();
    private final AtomicLong startTime = new AtomicLong(-1);
    private final AtomicLong duration = new AtomicLong(-1);
    private final AtomicBoolean succeed = new AtomicBoolean();
    private final CountDownLatch completeLatch = new CountDownLatch(1);
    private final AtomicReference<Throwable> uncaughtException = new AtomicReference<Throwable>(); 

    // TODO Should become an abstract method after removing {@link GWT#getGlobalModuleBaseURL()}.
    public String getModuleBaseURL()
    {
        return GWT.getGlobalModuleBaseURL();
    }

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