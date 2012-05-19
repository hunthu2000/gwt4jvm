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

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Test;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mind.gwt.jclient.GwtJavaClient;
import com.mind.gwt.jclient.GwtLoadTest;
import com.mind.gwt.jclient.context.Context;
import com.mind.mc.client.Service;
import com.mind.mc.client.ServiceAsync;
import com.mind.mc.dto.MovieDTO;

public class TypicalScenarioSyntheticTest implements EntryPoint
{
    @Override
    public void onModuleLoad()
    {
        final ServiceAsync service = GWT.create(Service.class);
        service.login("username", "password", new SimpleAsyncCallback<Void>()
        {            
            @Override
            public void onSuccess(Void result)
            {
                service.getMovieList(new SimpleAsyncCallback<ArrayList<MovieDTO>>()
                {
                    @Override
                    public void onSuccess(ArrayList<MovieDTO> movies)
                    {
                        final MovieDTO randomMovie = movies.get((int) (Math.random() * movies.size()));
                        new Timer()
                        {
                            @Override
                            public void run()
                            {
                                service.rateMovie(randomMovie.getId(), (byte) (Math.random() * 10), new SimpleAsyncCallback<Float>()
                                {
                                    @Override
                                    public void onSuccess(Float rating)
                                    {
                                        service.logout(new SimpleAsyncCallback<Void>()
                                        {
                                            @Override
                                            public void onSuccess(Void result)
                                            {
                                                Context.getCurrentContext().getClient().success();
                                            }
                                        });
                                    }
                                });
                            }
                        }.schedule((int) (100 + Math.random() * 3000));
                    }
                });
            }
        });
    }

    private abstract class SimpleAsyncCallback<T> implements AsyncCallback<T>
    {
        @Override
        public void onFailure(Throwable caught)
        {
            Context.getCurrentContext().getClient().failure();
        }
    }

    @Test
    public void test() throws InstantiationException, IllegalAccessException, InterruptedException
    {
        final AtomicLong succeed = new AtomicLong();
        final AtomicLong failure = new AtomicLong();
        new GwtLoadTest(getClass(), System.getProperty("moduleBaseURL", "http://localhost:8080/mc/"))
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
                System.out.println(TypicalScenarioSyntheticTest.class.getSimpleName() + ": concurrent users: " + getConcurrentClients() + ", succeed: " + succeed.get() + ", failure: " + failure.get());
            }

        }.start(Integer.getInteger("concurrentUsers", 100), Integer.getInteger("rampUpSeconds", 10), Integer.getInteger("testDurationSeconds", 30), TimeUnit.SECONDS);
        Assert.assertTrue(true);
    }

}