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
package com.mind.mc.mocks;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.mind.gwt.jclient.context.Context;
import com.mind.gwt.jclient.metrics.GwtRpcMetrics;
import com.mind.mc.client.activities.login.LoginView;

public class LoginViewMock implements LoginView
{
    @Override
    public void setDisplay(AcceptsOneWidget display) {}

    @Override
    public void setListener(final LoginView.Listener listener)
    {
        boolean afterLogout = Context.getCurrentContext().getClient().getMetrics(GwtRpcMetrics.class).size() > 0;
        if (afterLogout)
        {
            Context.getCurrentContext().getClient().success();
        }
        else
        {
            new Timer()
            {
                @Override
                public void run()
                {
                    listener.onLogin("username", "password");
                }

            }.schedule((int) (100 + Math.random() * 3000));
        }
    }

}