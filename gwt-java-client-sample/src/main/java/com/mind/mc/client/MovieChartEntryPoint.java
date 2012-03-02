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

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.mind.mc.client.places.LoginPlace;
import com.mind.mc.client.places.MovieChartActivityMapper;
import com.mind.mc.client.places.MovieChartHistoryMapper;

public class MovieChartEntryPoint implements EntryPoint
{
    private final EventBus eventBus = new SimpleEventBus();

    private final PlaceHistoryMapper placeHistoryMapper = GWT.create(MovieChartHistoryMapper.class);

    private final PlaceHistoryHandler placeHistoryHandler = new PlaceHistoryHandler(placeHistoryMapper);

    private final PlaceController placeController = new PlaceController(eventBus);

    private final ActivityManager activityManager = new ActivityManager(new MovieChartActivityMapper(placeController), eventBus);

    public void onModuleLoad()
    {
        Layout layout = GWT.create(LayoutImpl.class);
        layout.attach();
        activityManager.setDisplay(layout.getDisplay());
        placeHistoryHandler.register(placeController, eventBus, new LoginPlace());
        placeHistoryHandler.handleCurrentHistory();
    }

}