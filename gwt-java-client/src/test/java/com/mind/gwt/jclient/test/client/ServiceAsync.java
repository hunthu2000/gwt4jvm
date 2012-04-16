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
package com.mind.gwt.jclient.test.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mind.gwt.jclient.test.dto.Cookie;
import com.mind.gwt.jclient.test.dto.ExtendedCollection;
import com.mind.gwt.jclient.test.dto.ExtendedPrimitives;
import com.mind.gwt.jclient.test.dto.Primitives;
import com.mind.gwt.jclient.test.dto.PrimitiveWrappers;
import com.mind.gwt.jclient.test.dto.AggregatedEnumeration;
import com.mind.gwt.jclient.test.dto.WithStaticNestedClass;

public interface ServiceAsync
{
    void putAndGetPrimitiveWrappers(PrimitiveWrappers primitiveWrappers, String reference, AsyncCallback<PrimitiveWrappers> callback);

    void putAndGetPrimitives(Primitives primitives, String reference, AsyncCallback<Primitives> callback);

    void putAndGetExtendedPrimitives(ExtendedPrimitives extendedPrimitives, String reference, AsyncCallback<ExtendedPrimitives> callback);

    void putAndGetWithStaticNestedClass(WithStaticNestedClass withStaticNestedClass, String reference, AsyncCallback<WithStaticNestedClass> callback);

    void putAndGetExtendedCollection(ExtendedCollection extendedCollection, String reference, AsyncCallback<ExtendedCollection> callback);

    void putAndGetAggregatedEnumeration(AggregatedEnumeration aggregatedEnumeration, String reference, AsyncCallback<AggregatedEnumeration> callback);

    void putAndGetList(List<?> list, String reference, AsyncCallback<List<?>> callback);

    void getCookies(AsyncCallback<String> callback);

    void setCookies(LinkedList<Cookie> cookies, AsyncCallback<Void> callback);

}