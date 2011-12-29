/*
 * Copyright 2011 Mind Ltd.
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

public class WithStaticNestedClass implements Serializable
{
    public static final long serialVersionUID = 1L;

    public static WithStaticNestedClass createClientToServerObject()
    {
        return new WithStaticNestedClass(new StaticNestedClass(PrimitiveWrappers.createMaxValue()), Primitives.createMinValue());
    }

    public static WithStaticNestedClass createServerToClientObject()
    {
        return new WithStaticNestedClass(new StaticNestedClass(PrimitiveWrappers.createMinValue()), Primitives.createMaxValue());
    }

    public static class StaticNestedClass implements Serializable
    {
        public static final long serialVersionUID = 1L;

        private PrimitiveWrappers primitiveWrappers;

        @SuppressWarnings("unused")
        private StaticNestedClass() {}

        public StaticNestedClass(PrimitiveWrappers primitiveWrappers)
        {
            this.primitiveWrappers = primitiveWrappers;
        }

        public PrimitiveWrappers getPrimitiveWrappers()
        {
            return primitiveWrappers;
        }

        public void setPrimitiveWrappers(PrimitiveWrappers primitiveWrappers)
        {
            this.primitiveWrappers = primitiveWrappers;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((primitiveWrappers == null) ? 0 : primitiveWrappers.hashCode());
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
            StaticNestedClass other = (StaticNestedClass) object;
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
            return true;
        }

    }

    private StaticNestedClass staticNestedClass;

    private Primitives primitives;

    private WithStaticNestedClass() {}
    
    private WithStaticNestedClass(StaticNestedClass staticNestedClass, Primitives primitives)
    {
        this.staticNestedClass = staticNestedClass;
        this.primitives = primitives;
    }

    public StaticNestedClass getStaticNestedClass()
    {
        return staticNestedClass;
    }

    public Primitives getPrimitives()
    {
        return primitives;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((primitives == null) ? 0 : primitives.hashCode());
        result = prime * result + ((staticNestedClass == null) ? 0 : staticNestedClass.hashCode());
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
        WithStaticNestedClass other = (WithStaticNestedClass) object;
        if (primitives == null)
        {
            if (other.primitives != null)
            {
                return false;
            }
        }
        else if (!primitives.equals(other.primitives))
        {
            return false;
        }
        if (staticNestedClass == null)
        {
            if (other.staticNestedClass != null)
            {
                return false;
            }
        }
        else if (!staticNestedClass.equals(other.staticNestedClass))
        {
            return false;
        }
        return true;
    }

}