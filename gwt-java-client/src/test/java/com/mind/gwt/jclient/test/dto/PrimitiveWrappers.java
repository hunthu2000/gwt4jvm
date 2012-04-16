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

public class PrimitiveWrappers implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private Boolean booleanObject;

    private Byte byteObject;

    private Character characterObject;

    private Short shortObject;

    private Integer integerObject;

    private Long longObject;

    private Float floatObject;

    private Double doubleObject;

    public PrimitiveWrappers() {}

    public PrimitiveWrappers(Boolean booleanObject, Byte byteObject, Character characterObject, Short shortObject, Integer integerObject, Long longObject, Float floatObject, Double doubleObject)
    {
        this.booleanObject = booleanObject;
        this.byteObject = byteObject;
        this.characterObject = characterObject;
        this.shortObject = shortObject;
        this.integerObject = integerObject;
        this.longObject = longObject;
        this.floatObject = floatObject;
        this.doubleObject = doubleObject;
    }

    public Boolean getBooleanObject()
    {
        return booleanObject;
    }

    public void setBooleanObject(Boolean booleanObject)
    {
        this.booleanObject = booleanObject;
    }

    public Byte getByteObject()
    {
        return byteObject;
    }

    public void setByteObject(Byte byteObject)
    {
        this.byteObject = byteObject;
    }

    public Character getCharacterObject()
    {
        return characterObject;
    }

    public void setCharacterObject(Character characterObject)
    {
        this.characterObject = characterObject;
    }

    public Short getShortObject()
    {
        return shortObject;
    }

    public void setShortObject(Short shortObject)
    {
        this.shortObject = shortObject;
    }

    public Integer getIntegerObject()
    {
        return integerObject;
    }

    public void setIntegerObject(Integer integerObject)
    {
        this.integerObject = integerObject;
    }

    public Long getLongObject()
    {
        return longObject;
    }

    public void setLongObject(Long longObject)
    {
        this.longObject = longObject;
    }

    public Float getFloatObject()
    {
        return floatObject;
    }

    public void setFloatObject(Float floatObject)
    {
        this.floatObject = floatObject;
    }

    public Double getDoubleObject()
    {
        return doubleObject;
    }

    public void setDoubleObject(Double doubleObject)
    {
        this.doubleObject = doubleObject;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((booleanObject == null) ? 0 : booleanObject.hashCode());
        result = prime * result + ((byteObject == null) ? 0 : byteObject.hashCode());
        result = prime * result + ((characterObject == null) ? 0 : characterObject.hashCode());
        result = prime * result + ((doubleObject == null) ? 0 : doubleObject.hashCode());
        result = prime * result + ((floatObject == null) ? 0 : floatObject.hashCode());
        result = prime * result + ((integerObject == null) ? 0 : integerObject.hashCode());
        result = prime * result + ((longObject == null) ? 0 : longObject.hashCode());
        result = prime * result + ((shortObject == null) ? 0 : shortObject.hashCode());
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
        PrimitiveWrappers other = (PrimitiveWrappers) object;
        if (booleanObject == null)
        {
            if (other.booleanObject != null)
            {
                return false;
            }
        }
        else if (!booleanObject.equals(other.booleanObject))
        {
            return false;
        }
        if (byteObject == null)
        {
            if (other.byteObject != null)
            {
                return false;
            }
        }
        else if (!byteObject.equals(other.byteObject))
        {
            return false;
        }
        if (characterObject == null)
        {
            if (other.characterObject != null)
            {
                return false;
            }
        }
        else if (!characterObject.equals(other.characterObject))
        {
            return false;
        }
        if (doubleObject == null)
        {
            if (other.doubleObject != null)
            {
                return false;
            }
        }
        else if (!doubleObject.equals(other.doubleObject))
        {
            return false;
        }
        if (floatObject == null)
        {
            if (other.floatObject != null)
            {
                return false;
            }
        }
        else if (!floatObject.equals(other.floatObject))
        {
            return false;
        }
        if (integerObject == null)
        {
            if (other.integerObject != null)
            {
                return false;
            }
        }
        else if (!integerObject.equals(other.integerObject))
        {
            return false;
        }
        if (longObject == null)
        {
            if (other.longObject != null)
            {
                return false;
            }
        }
        else if (!longObject.equals(other.longObject))
        {
            return false;
        }
        if (shortObject == null)
        {
            if (other.shortObject != null)
            {
                return false;
            }
        }
        else if (!shortObject.equals(other.shortObject))
        {
            return false;
        }
        return true;
    }

}