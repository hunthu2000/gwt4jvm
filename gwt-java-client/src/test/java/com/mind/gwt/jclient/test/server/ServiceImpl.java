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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mind.gwt.jclient.test.client.Service;
import com.mind.gwt.jclient.test.dto.Cookie;
import com.mind.gwt.jclient.test.dto.ExtendedCollection;
import com.mind.gwt.jclient.test.dto.ExtendedPrimitives;
import com.mind.gwt.jclient.test.dto.Primitives;
import com.mind.gwt.jclient.test.dto.PrimitiveWrappers;
import com.mind.gwt.jclient.test.dto.AggregatedEnumeration;
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
    public ExtendedPrimitives getExtendedPrimitives()
    {
        return ExtendedPrimitives.createServerToClientObject();
    }

    @Override
    public void putExtendedPrimitives(ExtendedPrimitives extendedPrimitives)
    {
        if (!ExtendedPrimitives.createClientToServerObject().equals(extendedPrimitives))
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

    @Override
    public ExtendedCollection getExtendedCollection()
    {
        return ExtendedCollection.createServerToClientObject();
    }

    @Override
    public void putExtendedCollection(ExtendedCollection extendedCollection)
    {
        if (!extendedCollection.equals(ExtendedCollection.createClientToServerObject()))
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public AggregatedEnumeration getAggregatedEnumeration()
    {
        return AggregatedEnumeration.createServerToClientObject();
    }

    @Override
    public void putAggregatedEnumeration(AggregatedEnumeration aggregatedEnumeration)
    {
        if (!aggregatedEnumeration.equals(AggregatedEnumeration.createClientToServerObject()))
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<?> putAndGetList(List<?> list, String reference)
    {
        if (!list.toString().equals(reference))
        {
            throw new IllegalArgumentException("Got: " + list + ", but expected: " + reference);
        }
        return list;
    }

    @Override
    public String getCookies()
    {
        return getThreadLocalRequest().getHeader("Cookie");
    }

    @Override
    public void setCookies(LinkedList<Cookie> cookies)
    {
        for (Cookie cookie : cookies)
        {
            javax.servlet.http.Cookie c = new javax.servlet.http.Cookie(cookie.getName(), cookie.getValue());
            if (cookie.getExpires() != null)
            {
                c.setMaxAge((int) TimeUnit.SECONDS.convert(cookie.getExpires().getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
            }
            if (cookie.getDomain() != null)
            {
                c.setDomain(cookie.getDomain());
            }
            c.setPath(cookie.getPath());
            c.setSecure(cookie.isSecure());
            getThreadLocalResponse().addCookie(c);
        }
    }

}