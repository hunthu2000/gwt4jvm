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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.google.gwt.core.client.EntryPoint;
import com.mind.gwt.jclient.GwtJavaClient;

//TODO add clients per second (tests per second) metric...
public class GwtLoadTest
{
    private final GwtJavaClientListener listener = new GwtJavaClientListener()
    {
        @Override
        public void onFinish(GwtJavaClient client)
        {
            GwtLoadTest.this.onFinish(client);
            if (isTimeExpired())
            {
                if (concurrentClients.decrementAndGet() == 0)
                {
                    finishLatch.countDown();
                }
            }
            else 
            {
                startClient();
            } 
        }
    };

    private final Class<?> clientClass;
    private final String moduleBaseURL;
    private final DeferredBindingFactory deferredBindingFactory;
    private final CountDownLatch finishLatch = new CountDownLatch(1); 
    private final AtomicLong concurrentClients = new AtomicLong();

    // The only write to fields below is guaranteed to happen-before they are read: we are piggybacking on synchronization provided by start method of GwtJavaClient.  
    private int maxConcurrentClients;
    private long rampUpTime;
    private long testDuration;
    private long testStartTime;

    /**
     * Constructs <tt>GwtLoadTest</tt> that will be {@link #start(int, long, long, TimeUnit) operating on} instances of
     * specified {@link GwtJavaClient} implementation.
    */
    @SuppressWarnings("unchecked")
    public GwtLoadTest(Class<? extends GwtJavaClient> clientClass)
    {
        this((Class<? extends EntryPoint>) clientClass, null, null);
    }

    /**
     * Constructs <tt>GwtLoadTest</tt> that will be {@link #start(int, long, long, TimeUnit) operating on} instances of
     * the specified {@link EntryPoint} implementation, each wrapped in separate instance of {@link GwtJavaClient} with
     * the specified module base URL and default {@link DeferredBindingFactory}.
    */
    public GwtLoadTest(Class<? extends EntryPoint> entryPointClass, String moduleBaseURL)
    {
        this(entryPointClass, moduleBaseURL, DeferredBindingFactory.getDeferredBindingFactory());
    }

    /**
     * Constructs <tt>GwtLoadTest</tt> that will be {@link #start(int, long, long, TimeUnit) operating on} instances of
     * the specified {@link EntryPoint} implementation, each wrapped in separate instance of {@link GwtJavaClient} with
     * specified module base URL and deferred binding factory.
    */
    public GwtLoadTest(Class<? extends EntryPoint> entryPointClass, String moduleBaseURL, DeferredBindingFactory deferredBindingFactory)
    {
        this.clientClass = entryPointClass;
        this.moduleBaseURL = moduleBaseURL;
        this.deferredBindingFactory = deferredBindingFactory;
    }

    /**
     * @deprecated Use {@link #GwtLoadTest(Class)} and {@link #start(int, long, long, TimeUnit)} instead.
     * 
     * Constructs a new <code>GwtLoadTest</code> that will execute concurrently up to specified number of the specified
     * implementation of {@link GwtJavaClient}. The maximum load will be reached within specified ramp up time and will
     * be lasting specified time. All times are in specified time units.
     * 
     * <p><b>Use with an implementation of {@link GwtJavaClient} that overrides {@link GwtJavaClient#getModuleBaseURL()}
     * only! Otherwise you will end up with {@link NullPointerException}!</b></p>
     * 
     * @param clientClass - an implementation of {@link GwtJavaClient};
     * @param maxConcurrentClients - maximum number of concurrently executed instances of <code>clientClasses</code>;  
     * @param rampUpTime - ramp up time;
     * @param testDuration - test duration;
     * @param timeUnit - what unit <code>rampUpTime</code> and <code>testDuration</code> should interpreted at. 
    */
    @Deprecated
    public GwtLoadTest(Class<? extends GwtJavaClient> clientClass, int maxConcurrentClients, int rampUpTime, int testDuration, TimeUnit timeUnit)
    {
        this(clientClass);
        this.maxConcurrentClients = maxConcurrentClients;
        this.rampUpTime = timeUnit.toMillis(rampUpTime);
        this.testDuration = timeUnit.toMillis(testDuration);
    }

    public void onFinish(GwtJavaClient client) {}

    public long getConcurrentClients()
    {
        return concurrentClients.get();
    }

    /**
     * @deprecated. Use {@link #start(int, int, int, TimeUnit)} instead.
    */
    @Deprecated
    public void start() throws InstantiationException, IllegalAccessException, InterruptedException
    {
        start(maxConcurrentClients, rampUpTime, testDuration, TimeUnit.MILLISECONDS);
    }

    /**
     * Start load test that will maintain up to specified number of concurrently working clients. The maximum load will
     * be reached within specified ramp up time and will last specified time. All times are in specified time units.
     * 
     * @param maxConcurrentClients - maximum number of concurrently executed instances of <code>clientClasses</code>;
     * @param rampUpTime - ramp up time;
     * @param testDuration - test duration;
     * @param timeUnit - what unit <code>rampUpTime</code> and <code>testDuration</code> should interpreted at. 
    */
    public void start(int maxConcurrentClients, long rampUpTime, long testDuration, TimeUnit timeUnit) throws InstantiationException, IllegalAccessException, InterruptedException
    {
        if (maxConcurrentClients <= 0)
        {
            throw new IllegalArgumentException("The value of maxConcurrentClients has to be > 0!");
        }
        this.maxConcurrentClients = maxConcurrentClients;
        this.rampUpTime = timeUnit.toMillis(rampUpTime);
        this.testDuration = timeUnit.toMillis(testDuration);
        this.testStartTime = System.currentTimeMillis();
        while (concurrentClients.get() < maxConcurrentClients)
        {
            while (concurrentClients.get() < getEstimatedConcurrentClients())
            {
                startClient();
                concurrentClients.incrementAndGet();
            }
            Thread.sleep(1);
        }
        finishLatch.await();
    }

    private void startClient()
    {
        try
        {
            GwtJavaClient client = createClient();
            client.addListener(listener);
            client.start();
        }
        catch (Exception exception)
        {
            throw new RuntimeException(clientClass.getSimpleName() + " instance can't be created", exception);
        }
    }

    private GwtJavaClient createClient() throws InstantiationException, IllegalAccessException
    {
        boolean needWrapping = moduleBaseURL != null;
        if (needWrapping)
        {
            return new GwtJavaClient(moduleBaseURL, deferredBindingFactory)
            {
                @Override
                public void run()
                {
                    try
                    {
                        ((EntryPoint) clientClass.newInstance()).onModuleLoad();
                    }
                    catch (Exception exception)
                    {
                        throw new RuntimeException(clientClass.getSimpleName() + " instance can't be created", exception);
                    }
                }
            };
        }
        else
        {
            return (GwtJavaClient) clientClass.newInstance();
        }
    }

    private boolean isTimeExpired()
    {
        return getTimeElapsedSinceStart() >= testDuration;
    }

    private long getTimeElapsedSinceStart()
    {
        return System.currentTimeMillis() - testStartTime;
    }

    private int getEstimatedConcurrentClients()
    {
        return getTimeElapsedSinceStart() > rampUpTime ? maxConcurrentClients : (int) (maxConcurrentClients * (1.0 * getTimeElapsedSinceStart() / rampUpTime));
    }

}