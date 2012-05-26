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
package com.mind.gwt.jclient.xhr;

import java.lang.reflect.Constructor;
import java.nio.BufferOverflowException;
import java.util.Arrays;

public class ResponseText
{
    private static Constructor<String> noCopyStringFactory;

    static
    {
        try
        {
            noCopyStringFactory = String.class.getDeclaredConstructor(int.class, int.class, char[].class);
            noCopyStringFactory.setAccessible(true);
        }
        catch (Exception exception)
        {
            noCopyStringFactory = null;
        }
    }

    private char[] value;

    private int count;

    public ResponseText()
    {
        value = new char[128];
    }

    public synchronized void append(String string)
    {
        int newCount = count + string.length();
        if (newCount > value.length)
        {
            expand(newCount);
        }
        string.getChars(0, string.length(), value, count);
        count = newCount;
    }

    @Override
    public synchronized String toString()
    {
        if (noCopyStringFactory == null)
        {
            return new String(value, 0, count);
        }
        try
        {
            return noCopyStringFactory.newInstance(0, count, value);
        }
        catch (Exception exception)
        {
            return new String(value, 0, count);
        }
    }

    private void expand(int minimumValueLength)
    {
        int newValueLength = value.length * 2;
        if (newValueLength < 0)
        {
            throw new BufferOverflowException();
        }
        if (newValueLength < minimumValueLength)
        {
            newValueLength = minimumValueLength;
        }
        value = Arrays.copyOf(value, newValueLength);
    }

}