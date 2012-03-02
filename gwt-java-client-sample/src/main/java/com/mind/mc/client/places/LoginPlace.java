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
package com.mind.mc.client.places;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.mind.mc.client.activities.login.LoginActivity;

public class LoginPlace extends Place implements HasActivity
{
    @Prefix("login")
    public static class Tokenizer implements PlaceTokenizer<LoginPlace>
    {
        @Override
        public LoginPlace getPlace(String token)
        {
            return new LoginPlace();
        }

        @Override
        public String getToken(LoginPlace place)
        {
            return "";
        }
    }

    @Override
    public Activity getActivity(Place place, PlaceController placeController)
    {
        return new LoginActivity(placeController);
    }
}