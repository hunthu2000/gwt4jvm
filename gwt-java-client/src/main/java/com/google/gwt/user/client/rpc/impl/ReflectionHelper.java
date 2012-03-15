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
     * Loads class using {@link Class#forName(String)}.
    */
    public static Class<?> loadClass(String classToLoad)
    {
        try
        {
            return Class.forName(classToLoad);
        }
        catch (ClassNotFoundException exception)
        {
            throw new RuntimeException("Unable to find class " + classToLoad, exception);
        }
    }

    /**
     * Creates a new instance of class. The class must have default constructor.   
    */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> classToInstantiate) throws Exception
    {
        if (TypeHandler.class.isAssignableFrom(classToInstantiate))
        {
            String type = classToInstantiate.getName().replaceFirst("_FieldSerializer$", "").replace('_', '$').replaceFirst("^com.google.gwt.user.client.rpc.core.java", "java");
            return (T) TypeHandlerFactory.getTypeHandler(Class.forName(type));
        }
        else
        {
            Constructor<T> constructor = classToInstantiate.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        }
    }

}