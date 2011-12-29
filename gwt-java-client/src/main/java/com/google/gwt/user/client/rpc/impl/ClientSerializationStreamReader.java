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
 *     Copyright 2008 Google Inc.
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

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.Base64Utils;

/**
 * For internal use only. Used for server call serialization.
*/
public final class ClientSerializationStreamReader extends AbstractSerializationStreamReader
{
    int index;

    JSONArray results;

    JSONArray stringTable;

    private Serializer serializer;

    public ClientSerializationStreamReader(Serializer serializer)
    {
        this.serializer = serializer;
    }

    @Override
    public void prepareToRead(String encoded) throws SerializationException
    {
        try
        {
            results = new JSONArray(encoded);
        }
        catch (JSONException exception)
        {
            new SerializationException(exception);
        }

        index = results.length();
        super.prepareToRead(encoded);

        if (getVersion() != SERIALIZATION_STREAM_VERSION)
        {
            throw new IncompatibleRemoteServiceException("Expecting version " + SERIALIZATION_STREAM_VERSION + " from server, got " + getVersion() + ".");
        }

        if (!areFlagsValid())
        {
            throw new IncompatibleRemoteServiceException("Got an unknown flag from server: " + getFlags());
        }

        stringTable = readJavaScriptObject();
    }

    public boolean readBoolean() throws SerializationException
    {
        try
        {
            return results.getInt(--index) != 0;
        }
        catch (JSONException exception)
        {
            throw new SerializationException(exception);
        }
    }

    public byte readByte() throws SerializationException
    {
        try
        {
            return (byte) results.getInt(--index);
        }
        catch (JSONException exception)
        {
            throw new SerializationException(exception);
        }
    }

    public char readChar() throws SerializationException
    {
        try
        {
            return (char) results.getInt(--index);
        }
        catch (JSONException exception)
        {
            throw new SerializationException(exception);
        }
    }

    public double readDouble() throws SerializationException
    {
        try
        {
            return results.getDouble(--index);
        }
        catch (JSONException exception)
        {
            throw new SerializationException(exception);
        }
    }

    public float readFloat() throws SerializationException
    {
        try
        {
            return (float) results.getDouble(--index);
        }
        catch (JSONException exception)
        {
            throw new SerializationException(exception);
        }
    }

    public int readInt() throws SerializationException
    {
        try
        {
            return results.getInt(--index);
        }
        catch (JSONException exception)
        {
            throw new SerializationException(exception);
        }
    }

    public long readLong() throws SerializationException
    {
        try
        {
            String s = results.getString(--index);
//            return LongLib.longFromBase64(s);
            return Base64Utils.longFromBase64(s);
        }
        catch (JSONException exception)
        {
            throw new SerializationException(exception);
        }
    };

    public short readShort() throws SerializationException
    {
        try
        {
            return (short) results.getInt(--index);
        }
        catch (JSONException exception)
        {
            throw new SerializationException(exception);
        }
    }

    public String readString() throws SerializationException
    {
        return getString(readInt());
    }

    @Override
    protected Object deserialize(String typeSignature) throws SerializationException
    {
        int id = reserveDecodedObjectIndex();
        Object instance = serializer.instantiate(this, typeSignature);
        rememberDecodedObject(id, instance);
        serializer.deserialize(this, instance, typeSignature);
        return instance;
    }

    @Override
    public String getString(int index)
    {
        try
        {
            return index > 0 ? stringTable.getString(index - 1)  : null;
        }
        catch (JSONException exception)
        {
            throw new RuntimeException("Serialization exception");
        }
    }

    private JSONArray readJavaScriptObject() throws SerializationException
    {
        try
        {
            return results.getJSONArray(--index);
        }
        catch (JSONException exception)
        {
            throw new SerializationException(exception);
        }
    }
}