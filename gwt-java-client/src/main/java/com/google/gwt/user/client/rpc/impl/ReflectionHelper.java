/*
 * Copyright 2010 Mind Ltd.
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
 * 
 * This file incorporates work covered by the following copyright and permission
 * notice:  
 * 
 *     Copyright 2010 Google Inc.
 * 
 *     Licensed under the Apache License, Version 2.0 (the "License"); you may
 *     not use this file except in compliance with the License. You may obtain a
 *     copy of the License at:
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *     WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *     License for the specific language governing permissions and limitations
 *     under the License.
*/
package com.google.gwt.user.client.rpc.impl;

import java.lang.reflect.Constructor;

/**
 * Provides access to reflection capability, but only when running from bytecode.
*/
public class ReflectionHelper
{
    /**
     * Loads {@code c} using Class.forName.
    */
    public static Class<?> loadClass(String c) throws Exception
    {
        return Class.forName(c);
    }

    /**
     * Creates a new instance of {@code c}. The class must have a no-arg
     * constructor. The constructor may have any access modifier (for example,
     * private).
    */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> c) throws Exception
    {
        if (c.getName().endsWith("_FieldSerializer") && !c.getName().startsWith("com.google.gwt.user.client.rpc.core.java"))
        {
            Class<?> classToCreate = Class.forName(fieldSerializerToClass(c.getName()));
            if (!classToCreate.isEnum())
            {
                return (T) new Reflective_FieldSerializer(classToCreate);
            }
        }
        Constructor<T> constructor = c.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    /**
     * Return name of the class whose objects this field serializer is on duty to serialize
     * or deserialize. GWT's default filed serializers has following naming:    
     * 
     *     SomeClass$SomeInnerClass -> SomeClass_SomeInnerClass_FieldSerializer
     * 
     * @param fieldSerializer - then name of field serializer.
     * @return name of the class which this field serializer is responsible for.
     */
    private static String fieldSerializerToClass(String fieldSerializer)
    {
        return fieldSerializer.replaceFirst("_FieldSerializer$", "").replace("_", "$");
    }

}