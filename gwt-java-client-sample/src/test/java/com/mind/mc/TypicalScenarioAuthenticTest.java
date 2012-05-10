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
package com.mind.mc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Test;

import com.mind.gwt.jclient.DeferredBindingFactory;
import com.mind.gwt.jclient.GwtJavaClient;
import com.mind.gwt.jclient.GwtLoadTest;
import com.mind.mc.client.Layout;
import com.mind.mc.client.MovieChartEntryPoint;
import com.mind.mc.client.activities.login.LoginView;
import com.mind.mc.client.activities.mc.MovieChartView;
import com.mind.mc.mocks.LayoutMock;
import com.mind.mc.mocks.LoginViewMock;
import com.mind.mc.mocks.MovieChartViewMock;

public class TypicalScenarioAuthenticTest
{
    private static final String MODULE_BASE_URL = System.getProperty("moduleBaseURL", "http://localhost:8080/mc/");
    private static final int CONCURRENT_USERS = Integer.getInteger("concurrentUsers", 100);
    private static final int RAMP_UP_SECONDS = Integer.getInteger("rampUpSeconds", 10);
    private static final int TEST_DURATION_SECONDS = Integer.getInteger("testDurationSeconds", 30);

    private static final DeferredBindingFactory DEFERRED_BINDING_FACTORY = new DeferredBindingFactory()
    {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T create(Class<?> c) throws InstantiationException, IllegalAccessException, ClassNotFoundException
        {
            if (c == Layout.class)
            {
                return (T) new LayoutMock();
            }
            if (c == MovieChartView.class)
            {
                return (T) new MovieChartViewMock();
            }
            if (c == LoginView.class)
            {
                return (T) new LoginViewMock();
            }
            return super.create(c);
        }
    };

    @Test
    public void test() throws InstantiationException, IllegalAccessException, InterruptedException
    {
        final AtomicLong succeed = new AtomicLong();
        final AtomicLong failure = new AtomicLong();
        new GwtLoadTest(MovieChartEntryPoint.class, MODULE_BASE_URL, DEFERRED_BINDING_FACTORY)
        {
            @Override
            public void onClientFinished(GwtJavaClient client)
            {
                if (client.isSucceed())
                {
                    succeed.incrementAndGet();
                }
                else
                {
                    failure.incrementAndGet();
                }
                System.out.println(TypicalScenarioAuthenticTest.class.getSimpleName() + ": concurrent users: " + getConcurrentClients() + ", succeed: " + succeed.get() + ", failure: " + failure.get());
            }

        }.start(CONCURRENT_USERS, RAMP_UP_SECONDS, TEST_DURATION_SECONDS, TimeUnit.SECONDS);
        Assert.assertTrue(true);
    }

}