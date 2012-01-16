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
package com.mind.gwt.jclient.test.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ExtendedCollection extends ArrayList<String>
{
    private static final long serialVersionUID = 1L;

    public static ExtendedCollection createClientToServerObject()
    {
        return new ExtendedCollection(Arrays.asList("ClientToServer0", "ClientToServer1", "ClientToServer2"), Integer.MAX_VALUE);
    }

    public static ExtendedCollection createServerToClientObject()
    {
        return new ExtendedCollection(Arrays.asList("ServerToClient0", "ServerToClient1", "ServerToClient2"), Integer.MIN_VALUE);
    }

    private int intPrimitive;

    @SuppressWarnings("unused")
    private ExtendedCollection() {}

    public ExtendedCollection(Collection<String> collection, int intPrimitive)
    {
        addAll(collection);
        this.intPrimitive = intPrimitive;
    }

    public int getIntPrimitive()
    {
        return intPrimitive;
    }

    public void setIntPrimitive(int intPrimitive)
    {
        this.intPrimitive = intPrimitive;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + intPrimitive;
        return result;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!super.equals(object))
        {
            return false;
        }
        if (getClass() != object.getClass())
        {
            return false;
        }
        ExtendedCollection other = (ExtendedCollection) object;
        if (intPrimitive != other.intPrimitive)
        {
            return false;
        }
        return true;
    }

}