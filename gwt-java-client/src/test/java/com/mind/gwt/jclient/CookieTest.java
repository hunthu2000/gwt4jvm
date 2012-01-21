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
package com.mind.gwt.jclient;

import java.util.LinkedList;

import junit.framework.Assert;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mind.gwt.jclient.GwtJavaClient;
import com.mind.gwt.jclient.test.client.Service;
import com.mind.gwt.jclient.test.client.ServiceAsync;
import com.mind.gwt.jclient.test.dto.Cookie;
import com.mind.gwt.jclient.test.server.ServiceImpl;

public class CookieTest
{
    private static final int JETTY_PORT = Integer.getInteger("gwt.java.client.test.port", 8080);
    
    private static final String MODULE_BASE_URL = "http://localhost:" + JETTY_PORT + "/test/";

    private static Server jetty;

    @BeforeClass
    public static void setUp() throws Exception
    {
        jetty = new Server(JETTY_PORT);
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
    public void testCookieTransmission() throws InterruptedException
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
                LinkedList<Cookie> cookies = new LinkedList<Cookie>();
                cookies.add(new Cookie("ServerCookieName1", "ServerCookieValue1", null, null, null, false));
                cookies.add(new Cookie("ServerCookieName2", "ServerCookieValue2", null, null, null, false));
                service.setCookies(cookies, new AsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void result)
                    {
                        if ("ServerCookieValue1".equals(Cookies.getCookie("ServerCookieName1")) && "ServerCookieValue2".equals(Cookies.getCookie("ServerCookieName2")))
                        {
                            Cookies.setCookie("ClientCookieName", "ClientCookieValue");
                            service.getCookies(new AsyncCallback<String>()
                            {
                                @Override
                                public void onSuccess(String result)
                                {
                                    if ("ClientCookieName=ClientCookieValue;ServerCookieName1=ServerCookieValue1;ServerCookieName2=ServerCookieValue2".equals(result))
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
        };
        client.start();
        client.await();
        if (client.getUncaughtException() != null)
        {
            client.getUncaughtException().printStackTrace();
        }
        Assert.assertTrue(client.isSucceed());
    }

}