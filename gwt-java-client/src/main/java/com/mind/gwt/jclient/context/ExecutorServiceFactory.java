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
package com.mind.gwt.jclient.context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

class ExecutorServiceFactory
{
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    private static final ExecutorService[] executors = new ExecutorService[Runtime.getRuntime().availableProcessors() * 2];

    static
    {
        for (int i = 0; i < executors.length; i++)
        {
            executors[i] = Executors.newSingleThreadExecutor();
        }
    }

    private ExecutorServiceFactory() {}

    public static ExecutorService getExecutorService(Object dedicatedTo)
    {
        return executors[dedicatedTo.hashCode() % executors.length];
    }

    public static ScheduledExecutorService getScheduledExecutorService()
    {
        return scheduledExecutorService;
    }

}