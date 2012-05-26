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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LoginViewImpl extends Composite implements LoginView
{
    interface Ui extends UiBinder<Widget, LoginViewImpl> {}

    private static Ui ui = GWT.create(Ui.class);

    private Listener listener;

    @UiField InputElement username;

    @UiField InputElement password;

    public LoginViewImpl()
    {
        initWidget(ui.createAndBindUi(this));
    }

    @Override
    public void setDisplay(AcceptsOneWidget display)
    {
        display.setWidget(this);
    }

    @Override
    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    @UiHandler("loginButton")
    public void onLoginButtonClick(ClickEvent event)
    {
        listener.onLogin(username.getValue(), password.getValue());
    }

}