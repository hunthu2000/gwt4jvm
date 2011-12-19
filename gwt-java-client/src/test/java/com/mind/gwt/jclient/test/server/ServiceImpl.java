package com.mind.gwt.jclient.test.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mind.gwt.jclient.test.client.Service;
import com.mind.gwt.jclient.test.dto.Primitives;
import com.mind.gwt.jclient.test.dto.PrimitiveWrappers;

@SuppressWarnings("serial")
public class ServiceImpl extends RemoteServiceServlet implements Service
{
    @Override
    public PrimitiveWrappers getMinPrimitiveWrappers()
    {
        return PrimitiveWrappers.createMinValue();
    }

    @Override
    public void putMaxPrimitiveWrappers(PrimitiveWrappers primitiveWrappers)
    {
        if (!PrimitiveWrappers.createMaxValue().equals(primitiveWrappers))
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Primitives getMinPrimitives()
    {
        return Primitives.createMinValue();
    }

    @Override
    public void putMaxPrimitives(Primitives primitives)
    {
        if (!Primitives.createMaxValue().equals(primitives))
        {
            throw new IllegalArgumentException();
        }
    }

}