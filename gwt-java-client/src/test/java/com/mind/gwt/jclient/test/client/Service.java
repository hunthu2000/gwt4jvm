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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mind.gwt.jclient.test.dto.ExtendedCollection;
import com.mind.gwt.jclient.test.dto.ExtendedPrimitives;
import com.mind.gwt.jclient.test.dto.Primitives;
import com.mind.gwt.jclient.test.dto.PrimitiveWrappers;
import com.mind.gwt.jclient.test.dto.AggregatedEnumeration;
import com.mind.gwt.jclient.test.dto.WithStaticNestedClass;

@RemoteServiceRelativePath("service")
public interface Service extends RemoteService
{
    PrimitiveWrappers getMinPrimitiveWrappers();

    void putMaxPrimitiveWrappers(PrimitiveWrappers primitiveWrappers);

    Primitives getMinPrimitives();

    void putMaxPrimitives(Primitives primitives);

    ExtendedPrimitives getExtendedPrimitives();

    void putExtendedPrimitives(ExtendedPrimitives extendedPrimitives);

    WithStaticNestedClass getWithStaticNestedClass();

    void putWithStaticNestedClass(WithStaticNestedClass withStaticNestedClass);

    ExtendedCollection getExtendedCollection();

    void putExtendedCollection(ExtendedCollection extendedCollection);

    AggregatedEnumeration getAggregatedEnumeration();

    void putAggregatedEnumeration(AggregatedEnumeration aggregatedEnumeration);

}