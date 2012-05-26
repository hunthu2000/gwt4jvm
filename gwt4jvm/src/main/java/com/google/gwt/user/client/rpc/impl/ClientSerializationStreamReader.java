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

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.Base64Utils;

/**
 * For internal use only. Used for server call serialization.
*/
public final class ClientSerializationStreamReader extends AbstractSerializationStreamReader
{
    int index;

    JsonArray results;

    JsonArray stringTable;

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
            // Due to a bug in IE6/7, long GWT-RPC response can contain JavaScript,
            // which we have to remove before parsing response as JSON. Details can
            // be found in LengthConstrainedArray class.
            boolean containsInternetExploresWorkaround = encoded.charAt(encoded.lastIndexOf(']', encoded.length() - 3) + 1) == ')' || encoded.charAt(encoded.length() - 1) == ')';
            if (containsInternetExploresWorkaround)
            {
                char[] encodedCharArray = encoded.toCharArray();
                cleanUpInternetExploresWorkaround(encodedCharArray);
                encoded = new String(encodedCharArray);
            }
            results = new JsonParser().parse(encoded).getAsJsonArray();
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }

        index = results.size();
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
            return results.get(--index).getAsInt() != 0;
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }
    }

    public byte readByte() throws SerializationException
    {
        try
        {
            return (byte) results.get(--index).getAsInt();
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }
    }

    public char readChar() throws SerializationException
    {
        try
        {
            return (char) results.get(--index).getAsInt();
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }
    }

    public double readDouble() throws SerializationException
    {
        try
        {
            return results.get(--index).getAsDouble();
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }
    }

    public float readFloat() throws SerializationException
    {
        try
        {
            return (float) results.get(--index).getAsDouble();
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }
    }

    public int readInt() throws SerializationException
    {
        try
        {
            return results.get(--index).getAsInt();
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }
    }

    public long readLong() throws SerializationException
    {
        try
        {
            return Base64Utils.longFromBase64(results.get(--index).getAsString());
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }
    };

    public short readShort() throws SerializationException
    {
        try
        {
            return (short) results.get(--index).getAsInt();
        }
        catch (Exception exception)
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
            return index > 0 ? stringTable.get(index - 1).getAsString() : null;
        }
        catch (Exception exception)
        {
            throw new RuntimeException("Serialization exception");
        }
    }

    private JsonArray readJavaScriptObject() throws SerializationException
    {
        try
        {
            return results.get(--index).getAsJsonArray();
        }
        catch (Exception exception)
        {
            throw new SerializationException(exception);
        }
    }

    private void cleanUpInternetExploresWorkaround(char[] string)
    {
        int lastQuoteIndex = string.length;
        while (string[--lastQuoteIndex] != '"')
        {
            if (string[lastQuoteIndex] == ')') string[lastQuoteIndex] = ' ';
        }
        for (int i = 0, quotes = 0; i < lastQuoteIndex; i++)
        {
            switch (string[i])
            {
                case '"':
                    quotes++;
                    break;
                case ']':
                    if (quotes % 2 == 0)
                    {
                        while (i < lastQuoteIndex && string[i] != '[')
                        {
                            string[i++] = ' ';
                        }
                        if (string[i] == '[')
                        {
                            string[i] = ',';
                        }
                    }
                    break;
                case '\\':
                    i++;
                    break;
            }
        }
    }

}