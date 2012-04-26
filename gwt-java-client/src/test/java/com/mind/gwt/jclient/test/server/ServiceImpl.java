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

import java.util.Arrays;
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
    public PrimitiveWrappers putAndGetPrimitiveWrappers(PrimitiveWrappers primitiveWrappers, String reference)
    {
        if (!primitiveWrappers.toString().equals(reference))
        {
            throw new IllegalArgumentException("Got: " + primitiveWrappers + ", but expected: " + reference);
        }
        return primitiveWrappers;
    }

    @Override
    public Primitives putAndGetPrimitives(Primitives primitives, String reference)
    {
        if (!primitives.toString().equals(reference))
        {
            throw new IllegalArgumentException("Got: " + primitives + ", but expected: " + reference);
        }
        return primitives;
    }

    @Override
    public ExtendedPrimitives putAndGetExtendedPrimitives(ExtendedPrimitives extendedPrimitives, String reference)
    {
        if (!extendedPrimitives.toString().equals(reference))
        {
            throw new IllegalArgumentException("Got: " + extendedPrimitives + ", but expected: " + reference);
        }
        return extendedPrimitives;
    }

    @Override
    public WithStaticNestedClass putAndGetWithStaticNestedClass(WithStaticNestedClass withStaticNestedClass, String reference)
    {
        if (!withStaticNestedClass.toString().equals(reference))
        {
            throw new IllegalArgumentException("Got: " + withStaticNestedClass + ", but expected: " + reference);
        }
        return withStaticNestedClass;
    }

    @Override
    public ExtendedCollection putAndGetExtendedCollection(ExtendedCollection extendedCollection, String reference)
    {
        if (!extendedCollection.toString().equals(reference))
        {
            throw new IllegalArgumentException("Got: " + extendedCollection + ", but expected: " + reference);
        }
        return extendedCollection;
    }

    @Override
    public AggregatedEnumeration putAndGetAggregatedEnumeration(AggregatedEnumeration aggregatedEnumeration, String reference)
    {
        if (!aggregatedEnumeration.toString().equals(reference))
        {
            throw new IllegalArgumentException("Got: " + aggregatedEnumeration + ", but expected: " + reference);
        }
        return aggregatedEnumeration;
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
    public String[] putAndGetArray(String[] array, String reference)
    {
        String arrayAsString = Arrays.toString(array); 
        if (!arrayAsString.equals(reference))
        {
            throw new IllegalArgumentException("Got: " + arrayAsString + ", but expected: " + reference);
        }
        return array;
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