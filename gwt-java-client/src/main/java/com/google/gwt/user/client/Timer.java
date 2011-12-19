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
 * 
 * This file incorporates work covered by the following copyright and permission
 * notice:  
 * 
 *     Copyright 2007 Google Inc.
 * 
 *     Licensed under the Apache License, Version 2.0 (the "License"); you may
 *     not use this file except in compliance with the License. You may obtain a
 *     copy of the License at:
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *     WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *     License for the specific language governing permissions and limitations
 *     under the License.
*/
package com.google.gwt.user.client;

import java.util.concurrent.ScheduledFuture;

import com.mind.gwt.jclient.context.Context;

/**
 * A simplified, browser-safe timer class. This class serves the same purpose as
 * java.util.Timer, but is simplified because of the single-threaded
 * environment.
 * 
 * To schedule a timer, simply create a subclass of it (overriding {@link #run})
 * and call {@link #schedule} or {@link #scheduleRepeating}.
 * 
 * <p>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.TimerExample}
 * </p>
 */
public abstract class Timer
{
    private ScheduledFuture<?> feature;

    /**
     * Cancels this timer.
    */
    public synchronized void cancel()
    {
        if (feature != null)
        {
            feature.cancel(false);
        }
        feature = null;
    }

    /**
     * This method will be called when a timer fires. Override it to implement the timer's logic.
    */
    public abstract void run();

    /**
     * Schedules a timer to elapse in the future.
     * 
     * @param delayMs how long to wait before the timer elapses, in milliseconds
    */
    public synchronized void schedule(int delayMs)
    {
        if (delayMs <= 0)
        {
            throw new IllegalArgumentException("Period must be positive");
        }
        feature = Context.getCurrentContext().schedule(delayMs, new Runnable()
        {
            @Override
            public void run()
            {
                Timer.this.run();
            }

        });
    }

    /**
     * Schedules a timer that elapses repeatedly.
     * 
     * @param periodMs how long to wait before the timer elapses, in milliseconds, between each repetition
    */
    public synchronized void scheduleRepeating(final int periodMs)
    {
        if (periodMs <= 0)
        {
            throw new IllegalArgumentException("Period must be positive");
        }
        feature = Context.getCurrentContext().scheduleRepeating(periodMs, new Runnable()
        {
            @Override
            public void run()
            {
                Timer.this.run();
            }
        });
    }

}