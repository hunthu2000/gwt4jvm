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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.mind.gwt.jclient.test.client.Service;
import com.mind.gwt.jclient.test.client.ServiceAsync;
import com.mind.gwt.jclient.test.dto.ExtendedCollection;
import com.mind.gwt.jclient.test.dto.ExtendedPrimitives;
import com.mind.gwt.jclient.test.dto.Primitives;
import com.mind.gwt.jclient.test.dto.PrimitiveWrappers;
import com.mind.gwt.jclient.test.dto.AggregatedEnumeration;
import com.mind.gwt.jclient.test.dto.WithStaticNestedClass;

public class GwtRpcTest
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
                service.putMaxPrimitiveWrappers(PrimitiveWrappers.createMaxValue(), new SimpleAsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void result)
                    {
                        service.getMinPrimitiveWrappers(new SimpleAsyncCallback<PrimitiveWrappers>()
                        {
                            @Override
                            public void onSuccess(PrimitiveWrappers result)
                            {
                                if (PrimitiveWrappers.createMinValue().equals(result))
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
                service.putMaxPrimitives(Primitives.createMaxValue(), new SimpleAsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void result)
                    {
                        service.getMinPrimitives(new SimpleAsyncCallback<Primitives>()
                        {
                            @Override
                            public void onSuccess(Primitives result)
                            {
                                if (Primitives.createMinValue().equals(result))
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
                service.putExtendedPrimitives(ExtendedPrimitives.createClientToServerObject(), new SimpleAsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void result)
                    {
                        service.getExtendedPrimitives(new SimpleAsyncCallback<ExtendedPrimitives>()
                        {
                            @Override
                            public void onSuccess(ExtendedPrimitives result)
                            {
                                if (ExtendedPrimitives.createServerToClientObject().equals(result))
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
                service.putWithStaticNestedClass(WithStaticNestedClass.createClientToServerObject(), new SimpleAsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void result)
                    {
                        service.getWithStaticNestedClass(new SimpleAsyncCallback<WithStaticNestedClass>()
                        {
                            @Override
                            public void onSuccess(WithStaticNestedClass result)
                            {
                                if (WithStaticNestedClass.createServerToClientObject().equals(result))
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
                service.putExtendedCollection(ExtendedCollection.createClientToServerObject(), new SimpleAsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void result)
                    {
                        service.getExtendedCollection(new SimpleAsyncCallback<ExtendedCollection>()
                        {
                            @Override
                            public void onSuccess(ExtendedCollection result)
                            {
                                if (ExtendedCollection.createServerToClientObject().equals(result))
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
                service.putAggregatedEnumeration(AggregatedEnumeration.createClientToServerObject(), new SimpleAsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void result)
                    {
                        service.getAggregatedEnumeration(new SimpleAsyncCallback<AggregatedEnumeration>()
                        {
                            @Override
                            public void onSuccess(AggregatedEnumeration result)
                            {
                                if (AggregatedEnumeration.createServerToClientObject().equals(result))
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
                });
            }
        }.execute();
    }

}