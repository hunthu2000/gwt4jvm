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
package com.mind.mc.client.activities.login;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.mind.mc.client.Service;
import com.mind.mc.client.ServiceAsync;
import com.mind.mc.client.activities.login.LoginView.Listener;
import com.mind.mc.client.places.MovieChartPlace;

public class LoginActivity extends AbstractActivity implements Listener
{
    private final LoginView view = GWT.create(LoginViewImpl.class); 

    private final ServiceAsync service = GWT.create(Service.class);

    private final PlaceController placeController;

    public LoginActivity(PlaceController placeController)
    {
        this.placeController = placeController;
    }

    @Override
    public void start(AcceptsOneWidget display, EventBus eventBus)
    {
        view.setDisplay(display);
        view.setListener(this);
    }

    @Override
    public void onLogin(String username, String password)
    {
        service.login(username, password, new AsyncCallback<Void>()
        {
            @Override
            public void onSuccess(Void result)
            {
                placeController.goTo(new MovieChartPlace());
            }
            
            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Authentication failed!");
            }
        });
    }

}