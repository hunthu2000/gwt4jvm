/*
 * Copyright 2012 Mind Ltd.
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
package com.mind.gwt.jclient.metrics;

import java.util.concurrent.TimeUnit;

public class TimingMetrics<T> implements Metrics
{
    private final T оbject;
    private final long startTime;
    private final long duration;

    public TimingMetrics(T оbject, long startTime, long duration)
    {
        this.оbject = оbject;
        this.startTime = startTime;
        this.duration = duration;
    }

    public T getObject()
    {
        return оbject;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public long getDuration(TimeUnit timeUnit)
    {
        return timeUnit.convert(duration, TimeUnit.MILLISECONDS);
    }

}