/*
 * Copyright 2012 Mind Ltd.
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

public class ExtendedPrimitives extends Primitives
{
    private static final long serialVersionUID = 1L;

    public ExtendedPrimitives() {} // Don't change it to private, otherwise GWT would generate different code and we would get different test case! 

    public ExtendedPrimitives(Primitives primitives)
    {
        super(primitives);
    }

}