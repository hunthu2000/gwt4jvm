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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mind.gwt.jclient.test.client.Service;
import com.mind.gwt.jclient.test.client.ServiceAsync;
import com.mind.gwt.jclient.test.dto.ExtendedCollection;
import com.mind.gwt.jclient.test.dto.ExtendedPrimitives;
import com.mind.gwt.jclient.test.dto.Primitives;
import com.mind.gwt.jclient.test.dto.PrimitiveWrappers;
import com.mind.gwt.jclient.test.dto.AggregatedEnumeration;
import com.mind.gwt.jclient.test.dto.WithStaticNestedClass;
import com.mind.gwt.jclient.test.dto.AggregatedEnumeration.ParameterizedEnumeration;
import com.mind.gwt.jclient.test.dto.AggregatedEnumeration.SimpleEnumeration;
import com.mind.gwt.jclient.test.dto.WithStaticNestedClass.StaticNestedClass;

public class GwtRpcSerializationTest
{
    @BeforeClass
    public static void setUp() throws Exception
    {
        TestGwtJavaClient.startJetty();
    }

    @AfterClass
    public static void cleanUp() throws Exception
    {
        TestGwtJavaClient.stopJetty();
    }

    @Test
    public void testPrimitiveWrappersTransmission() throws InterruptedException
    {
        new TestGwtJavaClient()
        {
            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                final PrimitiveWrappers primitiveWrappers = new PrimitiveWrappers(true, Byte.MIN_VALUE, Character.MIN_VALUE, Short.MIN_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE, Float.MIN_VALUE, Double.MIN_VALUE);
                service.putAndGetPrimitiveWrappers(primitiveWrappers, primitiveWrappers.toString(), new SimpleAsyncCallback<PrimitiveWrappers>()
                {
                    @Override
                    public void onSuccess(PrimitiveWrappers result)
                    {
                        if (result.equals(primitiveWrappers))
                        {
                            success();
                        }
                        else
                        {
                            failure();
                        }
                    }
                });
            }
        }.execute();
    }

    @Test
    public void testPrimitivesTransmission() throws InterruptedException
    {
        new TestGwtJavaClient()
        {
            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                final Primitives primitives = new Primitives(true, Byte.MAX_VALUE, Character.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE);
                service.putAndGetPrimitives(primitives, primitives.toString(), new SimpleAsyncCallback<Primitives>()
                {
                    @Override
                    public void onSuccess(Primitives result)
                    {
                        if (result.equals(primitives))
                        {
                            success();
                        }
                        else
                        {
                            failure();
                        }
                    }
                });
            }
        }.execute();
    }

    @Test
    public void testExtendedPrimitivesTransmission() throws InterruptedException
    {
        new TestGwtJavaClient()
        {
            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                final Primitives primitives = new Primitives(true, Byte.MIN_VALUE, Character.MIN_VALUE, Short.MIN_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE, Float.MIN_VALUE, Double.MIN_VALUE);
                final ExtendedPrimitives extendedPrimitives = new ExtendedPrimitives(primitives);
                service.putAndGetExtendedPrimitives(extendedPrimitives, extendedPrimitives.toString(), new SimpleAsyncCallback<ExtendedPrimitives>()
                {
                    @Override
                    public void onSuccess(ExtendedPrimitives result)
                    {
                        if (result.equals(extendedPrimitives))
                        {
                            success();
                        }
                        else
                        {
                            failure();
                        }
                    }
                });
            }
        }.execute();
    }

    @Test
    public void testWithStaticNestedClassTransmission() throws InterruptedException
    {
        new TestGwtJavaClient()
        {
            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                final Primitives primitives = new Primitives(true, Byte.MAX_VALUE, Character.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE);
                final PrimitiveWrappers primitiveWrappers = new PrimitiveWrappers(true, Byte.MIN_VALUE, Character.MIN_VALUE, Short.MIN_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE, Float.MIN_VALUE, Double.MIN_VALUE);
                final WithStaticNestedClass withStaticNestedClass = new WithStaticNestedClass(new StaticNestedClass(primitiveWrappers), primitives);
                service.putAndGetWithStaticNestedClass(withStaticNestedClass, withStaticNestedClass.toString(), new SimpleAsyncCallback<WithStaticNestedClass>()
                {
                    @Override
                    public void onSuccess(WithStaticNestedClass result)
                    {
                        if (result.equals(withStaticNestedClass))
                        {
                            success();
                        }
                        else
                        {
                            failure();
                        }
                    }
                });
            }
        }.execute();
    }

    @Test
    public void testExtendedCollectionTransmission() throws InterruptedException
    {
        new TestGwtJavaClient()
        {
            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                final ExtendedCollection extendedCollection = new ExtendedCollection(Arrays.asList("item0", "item1", "item2", "item3", "item4"), Integer.MIN_VALUE);
                service.putAndGetExtendedCollection(extendedCollection, extendedCollection.toString(), new SimpleAsyncCallback<ExtendedCollection>()
                {
                    @Override
                    public void onSuccess(ExtendedCollection result)
                    {
                        if (result.equals(extendedCollection))
                        {
                            success();
                        }
                        else
                        {
                            failure();
                        }
                    }
                });
            }
        }.execute();
    }

    @Test
    public void testAggregatedEnumerationTransmission() throws InterruptedException
    {
        new TestGwtJavaClient()
        {
            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                final PrimitiveWrappers primitiveWrappers = new PrimitiveWrappers(true, Byte.MAX_VALUE, Character.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE);
                final AggregatedEnumeration aggregatedEnumeration = new AggregatedEnumeration(primitiveWrappers, SimpleEnumeration.VALUE1, ParameterizedEnumeration.VALUE2);
                service.putAndGetAggregatedEnumeration(aggregatedEnumeration, aggregatedEnumeration.toString(), new SimpleAsyncCallback<AggregatedEnumeration>()
                {
                    @Override
                    public void onSuccess(AggregatedEnumeration result)
                    {
                        if (result.equals(aggregatedEnumeration))
                        {
                            success();
                        }
                        else
                        {
                            failure();
                        }
                    }
                });
            }
        }.execute();
    }

    @Test
    public void testEmptyListTransmission() throws InterruptedException
    {
        new TestGwtJavaClient()
        {
            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                service.putAndGetList(Collections.emptyList(), "[]", new SimpleAsyncCallback<List<?>>()
                {
                    @Override
                    public void onSuccess(List<?> result)
                    {
                        if (result.isEmpty())
                        {
                            success();
                        }
                        else
                        {
                            failure();
                        }
                    }

                });
            }
        }.execute();
    }

    @Test
    public void testVeryLongListTransmission() throws InterruptedException
    {
        new TestGwtJavaClient()
        {
            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                final LinkedList<String> list = new LinkedList<String>();
                for (int i = 0; i < 18000; i++)
                {
                    list.add(String.valueOf(i));
                }
                final String reference = list.toString();
                service.putAndGetList(list, reference, new SimpleAsyncCallback<List<?>>()
                {
                    @Override
                    public void onSuccess(List<?> result)
                    {
                        if (result.toString().equals(reference))
                        {
                            success();
                        }
                        else
                        {
                            failure();
                        }
                    }

                });
            }
        }.execute();
    }

    @Test
    public void testArrayTransmission() throws InterruptedException
    {
        new TestGwtJavaClient()
        {
            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                String[] array = new String[10];
                for (int i = 0; i < array.length; i++)
                {
                    array[i] = String.valueOf(i);
                }
                final String reference = Arrays.toString(array);
                service.putAndGetArray(array, reference, new SimpleAsyncCallback<String[]>()
                {
                    @Override
                    public void onSuccess(String[] result)
                    {
                        if (Arrays.toString(result).equals(reference))
                        {
                            success();
                        }
                        else
                        {
                            failure();
                        }
                    }

                });
            }
        }.execute();
    }

    @Test
    public void testExceptionTransmission() throws InterruptedException
    {
        new TestGwtJavaClient()
        {
            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                final String message = "Test Exception Message";
                service.throwCheckedException(message, new AsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void result)
                    {
                        failure();
                    }

                    @Override
                    public void onFailure(Throwable caught)
                    {
                        if (caught instanceof Exception && message.equals(caught.getMessage()))
                        {
                            success();
                        }
                        else
                        {
                            failure();
                        }
                    }

                });
            }
        }.execute();
    }

}