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

import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.mind.gwt.jclient.context.Context;
import com.mind.mc.client.activities.mc.MovieChartView;
import com.mind.mc.dto.MovieDTO;

public class MovieChartViewMock implements MovieChartView
{
    private Listener listener;

    @Override
    public void setDisplay(AcceptsOneWidget display) {}

    @Override
    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    @Override
    public void setMovies(final List<MovieDTO> movies)
    {
        new Timer()
        {
            private int repeated = 0;

            @Override
            public void run()
            {
                if (repeated++ < 3)
                {
                    listener.onMovieRate((int) (Math.random() * movies.size()), (byte) (Math.random() * 10));
                }
                else
                {
                    Context.getCurrentContext().getClient().success();
                }
            }

        }.scheduleRepeating((int) (100 + Math.random() * 3000));
    }

}