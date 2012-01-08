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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mind.gwt.jclient.test.dto.ExtendedCollection;
import com.mind.gwt.jclient.test.dto.Primitives;
import com.mind.gwt.jclient.test.dto.PrimitiveWrappers;
import com.mind.gwt.jclient.test.dto.WithStaticNestedClass;

public interface ServiceAsync
{
    void getMinPrimitiveWrappers(AsyncCallback<PrimitiveWrappers> callback);

    void putMaxPrimitiveWrappers(PrimitiveWrappers primitiveWrappers, AsyncCallback<Void> callback);

    void getMinPrimitives(AsyncCallback<Primitives> callback);

    void putMaxPrimitives(Primitives primitives, AsyncCallback<Void> callback);

    void getWithStaticNestedClass(AsyncCallback<WithStaticNestedClass> callback);

    void putWithStaticNestedClass(WithStaticNestedClass withStaticNestedClass, AsyncCallback<Void> callback);

    void getExtendedCollection(AsyncCallback<ExtendedCollection> callback);

    void putExtendedCollection(ExtendedCollection extendedCollection, AsyncCallback<Void> callback);

}