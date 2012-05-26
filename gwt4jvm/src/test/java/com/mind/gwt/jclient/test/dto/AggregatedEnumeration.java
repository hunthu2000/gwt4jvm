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

import java.io.Serializable;

public class AggregatedEnumeration implements Serializable
{
    public enum SimpleEnumeration
    {
        VALUE1,
        VALUE2;
    }

    public enum ParameterizedEnumeration
    {
        VALUE1(1),
        VALUE2(2);

        private int value;

        private ParameterizedEnumeration(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    private static final long serialVersionUID = 1L;

    private PrimitiveWrappers primitiveWrappers;

    private SimpleEnumeration simpleEnumeration;

    private ParameterizedEnumeration parameterizedEnumeration;

    @SuppressWarnings("unused")
    private AggregatedEnumeration() {}

    public AggregatedEnumeration(PrimitiveWrappers primitiveWrappers, SimpleEnumeration simpleEnumeration, ParameterizedEnumeration parameterizedEnumeration)
    {
        this.primitiveWrappers = primitiveWrappers;
        this.simpleEnumeration = simpleEnumeration;
        this.parameterizedEnumeration = parameterizedEnumeration;
    }

    public PrimitiveWrappers getPrimitiveWrappers()
    {
        return primitiveWrappers;
    }

    public void setPrimitiveWrappers(PrimitiveWrappers primitiveWrappers)
    {
        this.primitiveWrappers = primitiveWrappers;
    }

    public SimpleEnumeration getSimpleEnumeration()
    {
        return simpleEnumeration;
    }

    public void setSimpleEnumeration(SimpleEnumeration simpleEnumeration)
    {
        this.simpleEnumeration = simpleEnumeration;
    }

    public ParameterizedEnumeration getParameterizedEnumeration()
    {
        return parameterizedEnumeration;
    }

    public void setParameterizedEnumeration(ParameterizedEnumeration parameterizedEnumeration)
    {
        this.parameterizedEnumeration = parameterizedEnumeration;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parameterizedEnumeration == null) ? 0 : parameterizedEnumeration.hashCode());
        result = prime * result + ((primitiveWrappers == null) ? 0 : primitiveWrappers.hashCode());
        result = prime * result + ((simpleEnumeration == null) ? 0 : simpleEnumeration.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null)
        {
            return false;
        }
        if (getClass() != object.getClass())
        {
            return false;
        }
        AggregatedEnumeration other = (AggregatedEnumeration) object;
        if (parameterizedEnumeration != other.parameterizedEnumeration)
        {
            return false;
        }
        if (primitiveWrappers == null)
        {
            if (other.primitiveWrappers != null)
            {
                return false;
            }
        }
        else if (!primitiveWrappers.equals(other.primitiveWrappers))
        {
            return false;
        }
        if (simpleEnumeration != other.simpleEnumeration)
        {
            return false;
        }
        return true;
    }

}