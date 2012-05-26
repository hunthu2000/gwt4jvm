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
package com.mind.mc.server;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mind.mc.client.Service;
import com.mind.mc.dto.MovieDTO;

@SuppressWarnings("serial")
public class ServiceImpl extends RemoteServiceServlet implements Service
{
    private static final ArrayList<MovieDTO> movies = new ArrayList<MovieDTO>(Arrays.asList(MovieDTO.createRandom(), MovieDTO.createRandom(), MovieDTO.createRandom()));  

    @Override
    public void login(String username, String password)
    {
        // any username/password will do
    }

    @Override
    public ArrayList<MovieDTO> getMovieList()
    {
        return movies; 
    }

    @Override
    public Float rateMovie(long movieId, byte rate)
    {
        return (float) (Math.random() * 10);
    }

    @Override
    public void logout()
    {
        // TODO Auto-generated method stub
    }

}