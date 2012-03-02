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
package com.mind.mc.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class LayoutImpl extends Composite implements Layout
{
    interface Ui extends UiBinder<Widget, LayoutImpl> {}

    private static Ui ui = GWT.create(Ui.class);

    @UiField SimplePanel display;

    public LayoutImpl()
    {
        initWidget(ui.createAndBindUi(this));
    }

    @Override
    public void attach()
    {
        RootPanel.get().add(this);
    }

    @Override
    public AcceptsOneWidget getDisplay()
    {
        return display;
    }

}