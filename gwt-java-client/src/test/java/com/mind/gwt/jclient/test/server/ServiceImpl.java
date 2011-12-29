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
package com.mind.gwt.jclient.test.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mind.gwt.jclient.test.client.Service;
import com.mind.gwt.jclient.test.dto.Primitives;
import com.mind.gwt.jclient.test.dto.PrimitiveWrappers;
import com.mind.gwt.jclient.test.dto.WithStaticNestedClass;

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

    @Override
    public WithStaticNestedClass getWithStaticNestedClass()
    {
        return WithStaticNestedClass.createServerToClientObject();
    }

    @Override
    public void putWithStaticNestedClass(WithStaticNestedClass withStaticNestedClass)
    {
        if (!withStaticNestedClass.equals(WithStaticNestedClass.createClientToServerObject()))
        {
            throw new IllegalArgumentException();
        }
    }

}