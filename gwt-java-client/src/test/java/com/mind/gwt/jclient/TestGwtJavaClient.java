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

import junit.framework.Assert;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mind.gwt.jclient.GwtJavaClient;
import com.mind.gwt.jclient.test.server.ServiceImpl;

public abstract class TestGwtJavaClient extends GwtJavaClient
{
    private static final int JETTY_PORT = Integer.getInteger("gwtJavaClient.jettyPort", 8080);

    private static final String MODULE_BASE_URL = "http://localhost:" + JETTY_PORT + "/test/";

    private static Server jetty;

    public static void startJetty() throws Exception
    {
        jetty = new Server(JETTY_PORT);
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.setResourceBase("target/test-war");
        servletContextHandler.addServlet(new ServletHolder(new ServiceImpl()), "/test/service");
        jetty.setHandler(servletContextHandler);
        jetty.start();
    }

    public static void stopJetty() throws Exception
    {
        jetty.stop();
        jetty.join();
    }

    @Override
    public String getModuleBaseURL()
    {
        return MODULE_BASE_URL;
    }

    public void execute() throws InterruptedException
    {
        start();
        await();
        if (getUncaughtException() != null)
        {
            getUncaughtException().printStackTrace();
        }
        Assert.assertTrue(isSucceed());
    }

    protected abstract class SimpleAsyncCallback<T> implements AsyncCallback<T>
    {
        @Override
        public void onFailure(Throwable caught)
        {
            failure();
        }
    }

}