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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.mind.gwt.jclient.test.client.Service;
import com.mind.gwt.jclient.test.client.ServiceAsync;
import com.mind.gwt.jclient.test.dto.Cookie;

public class CookieTest
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
    public void testCookieTransmission() throws InterruptedException
    {
        new TestGwtJavaClient()
        {
            @Override
            public void run()
            {
                final ServiceAsync service = GWT.create(Service.class);
                LinkedList<Cookie> cookies = new LinkedList<Cookie>();
                cookies.add(new Cookie("ServerCookieName1", "ServerCookieValue1", null, null, null, false));
                cookies.add(new Cookie("ServerCookieName2", "ServerCookieValue2", null, null, null, false));
                service.setCookies(cookies, new SimpleAsyncCallback<Void>()
                {
                    @Override
                    public void onSuccess(Void result)
                    {
                        if ("ServerCookieValue1".equals(Cookies.getCookie("ServerCookieName1")) && "ServerCookieValue2".equals(Cookies.getCookie("ServerCookieName2")))
                        {
                            Cookies.setCookie("ClientCookieName", "ClientCookieValue");
                            service.getCookies(new SimpleAsyncCallback<String>()
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
                            });
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