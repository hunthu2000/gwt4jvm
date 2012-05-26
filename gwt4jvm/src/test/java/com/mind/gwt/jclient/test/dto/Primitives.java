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

public class Primitives implements Serializable
{
    private static final long serialVersionUID = 1L;

    private boolean booleanPrimitive;

    private byte bytePrimitive;

    private char charPrimitive;

    private short shortPrimitive;

    private int intPrimitive;

    private long longPrimitive;

    private float floatPrimitive;

    private double doublePrimitive;

    public Primitives() {}

    public Primitives(boolean booleanPrimitive, byte bytePrimitive, char charPrimitive, short shortPrimitive, int intPrimitive, long longPrimitive, float floatPrimitive, double doublePrimitive)
    {
        this.booleanPrimitive = booleanPrimitive;
        this.bytePrimitive = bytePrimitive;
        this.charPrimitive = charPrimitive;
        this.shortPrimitive = shortPrimitive;
        this.intPrimitive = intPrimitive;
        this.longPrimitive = longPrimitive;
        this.floatPrimitive = floatPrimitive;
        this.doublePrimitive = doublePrimitive;
    }
    
    public Primitives(Primitives primitives)
    {
        this.booleanPrimitive = primitives.booleanPrimitive;
        this.bytePrimitive = primitives.bytePrimitive;
        this.charPrimitive = primitives.charPrimitive;
        this.shortPrimitive = primitives.shortPrimitive;
        this.intPrimitive = primitives.intPrimitive;
        this.longPrimitive = primitives.longPrimitive;
        this.floatPrimitive = primitives.floatPrimitive;
        this.doublePrimitive = primitives.doublePrimitive;
    }

    public boolean isBooleanPrimitive()
    {
        return booleanPrimitive;
    }

    public void setBooleanPrimitive(boolean booleanPrimitive)
    {
        this.booleanPrimitive = booleanPrimitive;
    }

    public byte getBytePrimitive()
    {
        return bytePrimitive;
    }

    public void setBytePrimitive(byte bytePrimitive)
    {
        this.bytePrimitive = bytePrimitive;
    }

    public char getCharPrimitive()
    {
        return charPrimitive;
    }

    public void setCharPrimitive(char charPrimitive)
    {
        this.charPrimitive = charPrimitive;
    }

    public short getShortPrimitive()
    {
        return shortPrimitive;
    }

    public void setShortPrimitive(short shortPrimitive)
    {
        this.shortPrimitive = shortPrimitive;
    }

    public int getIntPrimitive()
    {
        return intPrimitive;
    }

    public void setIntPrimitive(int intPrimitive)
    {
        this.intPrimitive = intPrimitive;
    }

    public long getLongPrimitive()
    {
        return longPrimitive;
    }

    public void setLongPrimitive(long longPrimitive)
    {
        this.longPrimitive = longPrimitive;
    }

    public float getFloatPrimitive()
    {
        return floatPrimitive;
    }

    public void setFloatPrimitive(float floatPrimitive)
    {
        this.floatPrimitive = floatPrimitive;
    }

    public double getDoublePrimitive()
    {
        return doublePrimitive;
    }

    public void setDoublePrimitive(double doublePrimitive)
    {
        this.doublePrimitive = doublePrimitive;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (booleanPrimitive ? 0 : 1);
        result = prime * result + bytePrimitive;
        result = prime * result + charPrimitive;
        result = prime * result + (int) doublePrimitive;
        result = prime * result + (int) floatPrimitive;
        result = prime * result + intPrimitive;
        result = prime * result + (int) longPrimitive;
        result = prime * result + shortPrimitive;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Primitives other = (Primitives) obj;
        if (booleanPrimitive != other.booleanPrimitive)
        {
            return false;
        }
        if (bytePrimitive != other.bytePrimitive)
        {
            return false;
        }
        if (charPrimitive != other.charPrimitive)
        {
            return false;
        }
        if (doublePrimitive != other.doublePrimitive)
        {
            return false;
        }
        if (floatPrimitive != other.floatPrimitive)
        {
            return false;
        }
        if (intPrimitive != other.intPrimitive)
        {
            return false;
        }
        if (longPrimitive != other.longPrimitive)
        {
            return false;
        }
        if (shortPrimitive != other.shortPrimitive)
        {
            return false;
        }
        return true;
    }

}