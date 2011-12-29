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

import junit.framework.Assert;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mind.gwt.jclient.GwtJavaClient;
import com.mind.gwt.jclient.test.client.Service;
import com.mind.gwt.jclient.test.client.ServiceAsync;
import com.mind.gwt.jclient.test.dto.Primitives;
import com.mind.gwt.jclient.test.dto.PrimitiveWrappers;
import com.mind.gwt.jclient.test.dto.WithStaticNestedClass;
import com.mind.gwt.jclient.test.server.ServiceImpl;

public class GwtRpcTest
{
    private static final String MODULE_BASE_URL = "http://localhost:8080/test/";

    private static Server jetty;

    @BeforeClass
    public static void setUp() throws Exception
    {
        jetty = new Server(8080);
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.setResourceBase("target/test-war");
        servletContextHandler.addServlet(new ServletHolder(new ServiceImpl()), "/test/service");
        jetty.setHandler(servletContextHandler);
        jetty.start();
    }

    @AfterClass
    public static void cleanUp() throws Exception
    {
        jetty.stop();
        jetty.join();
    }

    @Test
    public void testPrimitiveWrappersTransmission() throws InterruptedException
    {
        GwtJavaClient client = new GwtJavaClient()
        {
            @Override
            public String getModuleBaseURL()
            {
                return MODULE_BASE_URL;
            }

            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                service.putMaxPrimitiveWrappers(PrimitiveWrappers.createMaxValue(), new AsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void result)
                    {
                        service.getMinPrimitiveWrappers(new AsyncCallback<PrimitiveWrappers>()
                        {
                            @Override
                            public void onSuccess(PrimitiveWrappers result)
                            {
                                if (PrimitiveWrappers.createMinValue().equals(result))
                                {
                                    success();
                                }
                            }

                            @Override
                            public void onFailure(Throwable caught)
                            {
                                failure();
                            }
                        });
                    }
                    
                    @Override
                    public void onFailure(Throwable caught)
                    {
                        failure();
                    }
                });
            }
        };
        client.start();
        client.await();
        Assert.assertTrue(client.isSucceed());
    }

    @Test
    public void testPrimitivesTransmission() throws InterruptedException
    {
        GwtJavaClient client = new GwtJavaClient()
        {
            @Override
            public String getModuleBaseURL()
            {
                return MODULE_BASE_URL;
            }

            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                service.putMaxPrimitives(Primitives.createMaxValue(), new AsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void result)
                    {
                        service.getMinPrimitives(new AsyncCallback<Primitives>()
                        {
                            @Override
                            public void onSuccess(Primitives result)
                            {
                                if (Primitives.createMinValue().equals(result))
                                {
                                    success();
                                }
                            }

                            @Override
                            public void onFailure(Throwable caught)
                            {
                                failure();
                            }
                        });
                    }
                    
                    @Override
                    public void onFailure(Throwable caught)
                    {
                        failure();
                    }
                });
            }
        };
        client.start();
        client.await();
        Assert.assertTrue(client.isSucceed());
    }

    @Test
    public void testWithStaticNestedClassTransmission() throws InterruptedException
    {
        GwtJavaClient client = new GwtJavaClient()
        {
            @Override
            public String getModuleBaseURL()
            {
                return MODULE_BASE_URL;
            }

            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                service.putWithStaticNestedClass(WithStaticNestedClass.createClientToServerObject(), new AsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void result)
                    {
                        service.getWithStaticNestedClass(new AsyncCallback<WithStaticNestedClass>()
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
                            
                            @Override
                            public void onFailure(Throwable caught)
                            {
                                failure();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable caught)
                    {
                        failure();
                    }

                });
            }
        };
        client.start();
        client.await();
        Assert.assertTrue(client.isSucceed());
    }

}