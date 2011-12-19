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
*/
package com.google.gwt.user.client.rpc.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class Reflective_FieldSerializer implements TypeHandler
{
    private final Class<?> classToCreate; 

    public Reflective_FieldSerializer(Class<?> classToCreate)
    {
        this.classToCreate = classToCreate;
    }

    @Override
    public Object create(SerializationStreamReader reader) throws SerializationException
    {
        try
        {
            Constructor<?> constructor = classToCreate.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
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
                Field[] fields = c.getDeclaredFields();
                Arrays.sort(fields, 0, fields.length, new Comparator<Field>()
                {
                    @Override
                    public int compare(Field field1, Field field2)
                    {
                        return field1.getName().compareTo(field2.getName());
                    }
                });
                for (Field field : fields)
                {
                    field.setAccessible(true);
                    
                    int fieldModifiers = field.getModifiers();
                    if (Modifier.isStatic(fieldModifiers) || Modifier.isTransient(fieldModifiers))
                    {
                        continue;
                    }
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
            throw new RuntimeException(exception);
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
                Field[] fields = c.getDeclaredFields();
                Arrays.sort(fields, 0, fields.length, new Comparator<Field>()
                {
                    @Override
                    public int compare(Field field1, Field field2)
                    {
                        return field1.getName().compareTo(field2.getName());
                    }
                });
                for (Field field : fields)
                {
                    field.setAccessible(true);

                    int fieldModifiers = field.getModifiers();
                    if (Modifier.isStatic(fieldModifiers) || Modifier.isTransient(fieldModifiers))
                    {
                        continue;
                    }

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
            throw new RuntimeException(exception);
        }
    }
}