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
package com.mind.mc.client.activities.mc;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.mind.mc.client.Service;
import com.mind.mc.client.ServiceAsync;
import com.mind.mc.client.activities.mc.MovieChartView.Listener;
import com.mind.mc.client.places.LoginPlace;
import com.mind.mc.dto.MovieDTO;

public class MovieChartActivity extends AbstractActivity implements Listener
{
    public final MovieChartView view = GWT.create(MovieChartView.class); 

    private final ServiceAsync service = GWT.create(Service.class);

    private final PlaceController placeController;

    private List<MovieDTO> movies;

    public MovieChartActivity(PlaceController placeController)
    {
        this.placeController = placeController;
    }

    @Override
    public void start(AcceptsOneWidget display, EventBus eventBus)
    {
        view.setDisplay(display);
        view.setListener(this);
        service.getMovieList(new AsyncCallback<ArrayList<MovieDTO>>()
        {
            @Override
            public void onSuccess(ArrayList<MovieDTO> movies)
            {
                MovieChartActivity.this.movies = movies;
                view.setMovies(movies);
            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Movie list can't be obtained!");
            }

        });
    }

    @Override
    public void onMovieRate(final int index, byte rate)
    {
        service.rateMovie(movies.get(index).getId(), rate, new AsyncCallback<Float>()
        {
            @Override
            public void onSuccess(Float rating)
            {
                view.setMovieRating(index, rating);
            }

            @Override
            public void onFailure(Throwable caught)
            {
                Window.alert("Movie can't be rated!");
            }

        });
    }

    @Override
    public void onLogout()
    {
        placeController.goTo(new LoginPlace());
    }

}