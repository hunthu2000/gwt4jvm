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
package com.mind.gwt.jclient.metrics;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class GwtRpcMetrics implements Metrics, Comparable<GwtRpcMetrics>
{
    private final Method method;
    private final long startTime;
    private final long duration;
    private final boolean succeed;

    public GwtRpcMetrics(Method method, long startTime, long duration, boolean succeed)
    {
        this.method = method;
        this.startTime = startTime;
        this.duration = duration;
        this.succeed = succeed;
    }

    @Override
    public int compareTo(GwtRpcMetrics gwtRpcMetrics)
    {
        long delta = startTime - gwtRpcMetrics.startTime;
        return delta < 0 ? -1 : delta > 0 ? 1 : 0;
    }

    public Method getMethod()
    {
        return method;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public long getDuration(TimeUnit timeUnit)
    {
        return timeUnit.convert(duration, TimeUnit.MILLISECONDS);
    }

    public boolean isSucceed()
    {
        return succeed;
    }

}