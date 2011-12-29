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
package com.mind.gwt.jclient.xhr;

import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;

class XMLHttpRequestSnapshot extends XMLHttpRequest
{
    private final int readyState;
    private final int status;
    private final String responseText;
    private final String failureMessage;

    public XMLHttpRequestSnapshot(int readyState, int status, String responseText)
    {
        this(readyState, status, responseText, null);
    }

    public XMLHttpRequestSnapshot(int readyState, int status, String responseText, String failureMessage)
    {
        this.readyState = readyState;
        this.status = status;
        this.responseText = responseText;
        this.failureMessage = failureMessage;
    }

    @Override
    public void abort()
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    @Override
    public void clearOnReadyStateChange()
    {
        // There is nothing to do here, see the body of {@link XMLHttpRequestImpl#clearOnReadyStateChange()} for an explanation. 
    }

    @Override
    public String getAllResponseHeaders()
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    @Override
    public int getReadyState()
    {
        return readyState;
    }

    @Override
    public String getResponseHeader(String header)
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    @Override
    public String getResponseText()
    {
        return responseText;
    }

    @Override
    public int getStatus()
    {
        return status;
    }

    @Override
    public String getStatusText()
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    @Override
    public String getFailureMessage()
    {
        return failureMessage;
    }

    @Override
    public void open(String httpMethod, String url)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void open(String httpMethod, String url, String user)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void open(String httpMethod, String url, String user, String password)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void send()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void send(String requestData)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOnReadyStateChange(ReadyStateChangeHandler handler)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRequestHeader(String header, String value)
    {
        throw new UnsupportedOperationException();
    }

}