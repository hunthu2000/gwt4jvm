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

import com.mind.gwt.jclient.GwtJavaClient;

//TODO add clients per second (tests per second) metric...
public class GwtLoadTest implements GwtJavaClientListener
{
    private final Class<? extends GwtJavaClient> clientClass;
    private final int maxConcurrentClients;
    private final long rampUpTime;
    private final long testDuration;
    private final CountDownLatch finishLatch = new CountDownLatch(1); 
    private final AtomicLong concurrentClients = new AtomicLong();

    private volatile long testStartTime;

    /**
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
    public GwtLoadTest(Class<? extends GwtJavaClient> clientClass, int maxConcurrentClients, int rampUpTime, int testDuration, TimeUnit timeUnit)
    {
        this.clientClass = clientClass;
        this.maxConcurrentClients = maxConcurrentClients;
        this.rampUpTime = timeUnit.toMillis(rampUpTime);
        this.testDuration = timeUnit.toMillis(testDuration);
    }

    public long getConcurrentClients()
    {
        return concurrentClients.get();
    }

    public void start() throws InstantiationException, IllegalAccessException, InterruptedException
    {
        testStartTime = System.currentTimeMillis();
        while (getTimeElapsedSinceStart() <= rampUpTime)
        {
            while (!isTimeExpired() && concurrentClients.get() < getEstimatedConcurrentClients())
            {
                startClient();
            }
            Thread.sleep(1);
        }
        finishLatch.await();
    }

    @Override
    public void onFinish(GwtJavaClient client)
    {
        concurrentClients.decrementAndGet();
        if (!isTimeExpired())
        {
            startClient();
        }
        if (concurrentClients.get() == 0)
        {
            finishLatch.countDown();
        }
    }

    private void startClient()
    {
        try
        {
            concurrentClients.incrementAndGet();
            GwtJavaClient client = clientClass.newInstance();
            client.addListener(this);
            client.start();
        }
        catch (Exception exception)
        {
            throw new RuntimeException(clientClass.getSimpleName() + " instance can't be created", exception);
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