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
package com.google.gwt.user.client.rpc.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class ReflectiveTypeHandler implements TypeHandler
{
    private static final TypeHandler NO_TYPE_HANDLER = new ReflectiveTypeHandler(null);

    private static final ConcurrentMap<Class<?>, TypeHandler> cachedTypeHandlers = new ConcurrentHashMap<Class<?>, TypeHandler>();

    private static final ConcurrentMap<Class<?>, Field[]> cachedClassFields = new ConcurrentHashMap<Class<?>, Field[]>();

    private final Class<?> type; 

    public ReflectiveTypeHandler(Class<?> type)
    {
        this.type = type;
    }

    @Override
    public Object create(SerializationStreamReader reader) throws SerializationException
    {
        try
        {
            TypeHandler typeHandler = getTypeHandler(type);
            if (typeHandler != NO_TYPE_HANDLER)
            {
                return typeHandler.create(reader);
            }
            Constructor<?> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }
    }

    @Override
    public void serial(SerializationStreamWriter writer, Object object) throws SerializationException
    {
        try
        {
            Class<? extends Object> c = object.getClass();
            do
            {
                TypeHandler typeHandler = getTypeHandler(c);
                if (typeHandler != NO_TYPE_HANDLER)
                {
                    typeHandler.serial(writer, object);
                    return;
                }
                for (Field field : getTypeFields(c))
                {
                    if (field.getType() == boolean.class)
                    {
                        writer.writeBoolean(field.getBoolean(object));
                    }
                    else if (field.getType() == byte.class)
                    {
                        writer.writeByte(field.getByte(object));
                    }
                    else if (field.getType() == char.class)
                    {
                        writer.writeChar(field.getChar(object));
                    }
                    else if (field.getType() == short.class)
                    {
                        writer.writeShort(field.getShort(object));
                    }
                    else if (field.getType() == int.class)
                    {
                        writer.writeInt(field.getInt(object));
                    }
                    else if (field.getType() == long.class)
                    {
                        writer.writeLong(field.getLong(object));
                    }
                    else if (field.getType() == float.class)
                    {
                        writer.writeFloat(field.getFloat(object));
                    }
                    else if (field.getType() == double.class)
                    {
                        writer.writeDouble(field.getDouble(object));
                    }
                    else if (field.getType() == String.class)
                    {
                        writer.writeString((String) field.get(object));
                    }
                    else
                    {
                        writer.writeObject(field.get(object));
                    }
                }
            }
            while ((c = c.getSuperclass()) != Object.class);
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }
    }

    @Override
    public void deserial(SerializationStreamReader reader, Object object) throws SerializationException
    {
        try
        {
            Class<? extends Object> c = object.getClass();
            do
            {
                TypeHandler typeHandler = getTypeHandler(c);
                if (typeHandler != NO_TYPE_HANDLER)
                {
                    typeHandler.deserial(reader, object);
                    return;
                }
                for (Field field : getTypeFields(c))
                {
                    if (field.getType() == boolean.class)
                    {
                        field.setBoolean(object, reader.readBoolean());
                    }
                    else if (field.getType() == byte.class)
                    {
                        field.setByte(object, reader.readByte());
                    }
                    else if (field.getType() == char.class)
                    {
                        field.setChar(object, reader.readChar());
                    }
                    else if (field.getType() == short.class)
                    {
                        field.setShort(object, reader.readShort());
                    }
                    else if (field.getType() == int.class)
                    {
                        field.setInt(object, reader.readInt());
                    }
                    else if (field.getType() == long.class)
                    {
                        field.setLong(object, reader.readLong());
                    }
                    else if (field.getType() == float.class)
                    {
                        field.setFloat(object, reader.readFloat());
                    }
                    else if (field.getType() == double.class)
                    {
                        field.setDouble(object, reader.readDouble());
                    }
                    else if (field.getType() == String.class)
                    {
                        field.set(object, reader.readString());
                    }
                    else
                    {
                        field.set(object, reader.readObject());
                    }
                }
            }
            while ((c = c.getSuperclass()) != Object.class);
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }
    }

    private TypeHandler getTypeHandler(Class<?> c) throws SerializationException
    {
        try
        {
            if (!cachedTypeHandlers.containsKey(c))
            {
                List<Class<TypeHandler>> typeHandlers = getTypeHandlerClasses(c);
                cachedTypeHandlers.putIfAbsent(c, typeHandlers.isEmpty() ? NO_TYPE_HANDLER : typeHandlers.get(0).getDeclaredConstructor().newInstance());
            }
            return cachedTypeHandlers.get(c);
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Class<TypeHandler>> getTypeHandlerClasses(Class<?> c)
    {
        LinkedList<Class<TypeHandler>> typeHandlers = new LinkedList<Class<TypeHandler>>();
        do
        {
            try
            {
                Class<TypeHandler> typeHandler = getGeneratedTypeHandlerClass(c);
                if (!c.isEnum() && hasNativeMethods(typeHandler))
                {
                    return Collections.EMPTY_LIST;
                }
                typeHandlers.add(typeHandler);
            }
            catch (ClassNotFoundException exception)
            {
                break;
            }
        }
        while ((c = c.getSuperclass()) != Object.class);
        return typeHandlers;
    }

    @SuppressWarnings("unchecked")
    private static Class<TypeHandler> getGeneratedTypeHandlerClass(Class<?> c) throws ClassNotFoundException
    {
        String generatedTypeHandlerClassName = c.getName().replace('$', '_') + "_FieldSerializer";
        if (generatedTypeHandlerClassName.startsWith("java"))
        {
            generatedTypeHandlerClassName = "com.google.gwt.user.client.rpc.core." + generatedTypeHandlerClassName;
        }
        return (Class<TypeHandler>) Class.forName(generatedTypeHandlerClassName);
    }

    private static boolean hasNativeMethods(Class<?> c)
    {
        for (Method method : c.getDeclaredMethods())
        {
            if (method.toString().contains(" native ")) return true; 
        }
        return false;
    }

    private static Field[] getTypeFields(Class<?> c)
    {
        if (!cachedClassFields.containsKey(c))
        {
            LinkedList<Field> fields = new LinkedList<Field>();  
            for (Field field : c.getDeclaredFields())
            {
                field.setAccessible(true);

                int fieldModifiers = field.getModifiers();
                if (!Modifier.isStatic(fieldModifiers) && !Modifier.isTransient(fieldModifiers))
                {
                    fields.add(field);
                }
            }
            Field[] result = fields.toArray(new Field[0]);
            Arrays.sort(result, 0, result.length, new Comparator<Field>()
            {
                @Override
                public int compare(Field field1, Field field2)
                {
                    return field1.getName().compareTo(field2.getName());
                }
            });
            cachedClassFields.putIfAbsent(c, result);
        }
        return cachedClassFields.get(c);
    }

}