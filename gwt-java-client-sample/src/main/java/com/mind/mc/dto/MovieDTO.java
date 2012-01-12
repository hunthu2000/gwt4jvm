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
package com.mind.mc.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class MovieDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    public static MovieDTO createRandom()
    {
        MovieDTO movie = new MovieDTO();
        movie.setId((long) (Math.random() * Long.MAX_VALUE));
        movie.setTitle("title");
        movie.setDescription("description");
        movie.setStars(null);
        movie.setRating((float) (Math.random() * 10));
        return movie;
    }

    private long id;

    private String title;

    private String description;

    private ArrayList<String> stars;

    private float rating;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public ArrayList<String> getStars()
    {
        return stars;
    }

    public void setStars(ArrayList<String> stars)
    {
        this.stars = stars;
    }

    public float getRating()
    {
        return rating;
    }

    public void setRating(float rating)
    {
        this.rating = rating;
    }

}