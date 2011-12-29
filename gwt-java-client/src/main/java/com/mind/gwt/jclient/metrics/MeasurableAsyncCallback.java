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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mind.gwt.jclient.context.Context;

public class MeasurableAsyncCallback<T> implements AsyncCallback<T>
{
    private final Method method;
    private final AsyncCallback<T> callback;
    private final long startTime;

    public MeasurableAsyncCallback(Method method, AsyncCallback<T> callback)
    {
        startTime = System.currentTimeMillis();
        this.method = method;
        this.callback = callback;
    }

    @Override
    public void onSuccess(T result)
    {
        reportMetrics(true);
        callback.onSuccess(result);
    }

    @Override
    public void onFailure(Throwable caught)
    {
        reportMetrics(false);
        callback.onFailure(caught);
    }

    private void reportMetrics(boolean succeed)
    {
        Context.getCurrentContext().getClient().addMetrics(new GwtRpcMetrics(method, startTime, System.currentTimeMillis() - startTime, succeed));
    }

}