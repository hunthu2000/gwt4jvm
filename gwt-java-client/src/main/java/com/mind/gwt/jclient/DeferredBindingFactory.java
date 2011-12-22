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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mind.gwt.jclient.metrics.MeasurableAsyncCallback;

public class DeferredBindingFactory
{
    private static final DeferredBindingFactory DEFERRED_BINDING_FACTORY = new DeferredBindingFactory();

    public static DeferredBindingFactory getDeferredBindingFactory()
    {
        return DEFERRED_BINDING_FACTORY; 
    }

    protected DeferredBindingFactory() {}

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> c) throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        Class<?> asyncServiceClass = (Class<?>) Class.forName(c.getName() + "Async");
        return (T) Proxy.newProxyInstance(asyncServiceClass.getClassLoader(), new Class[] {asyncServiceClass}, new InvocationHandler()
        {
            private final T asyncServiceProxy  = (T) Class.forName(c.getName() + "_Proxy").newInstance();

            @Override
            @SuppressWarnings("rawtypes")
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
            {
                args[args.length - 1] = new MeasurableAsyncCallback(method, (AsyncCallback<?>) args[args.length - 1]);
                return method.invoke(asyncServiceProxy, args);
            }
        });
    }

}