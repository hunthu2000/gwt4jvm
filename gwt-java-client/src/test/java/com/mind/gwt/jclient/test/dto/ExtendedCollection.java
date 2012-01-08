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