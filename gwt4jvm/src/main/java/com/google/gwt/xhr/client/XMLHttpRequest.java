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
 *     Copyright 2009 Google Inc.
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
package com.google.gwt.xhr.client;

import com.mind.gwt.jclient.xhr.XMLHttpRequestImpl;

/**
 * There is an abstract implementation of XMLHttpRequest.
 * 
 * <p>
 * See <a href="http://www.w3.org/TR/XMLHttpRequest/">http://www.w3.org/TR/XMLHttpRequest/</a>
*/
public abstract class XMLHttpRequest
{
    /**
     * When constructed, the XMLHttpRequest object must be in the UNSENT state.
    */
    public static final int UNSENT = 0;

    /**
     * The OPENED state is the state of the object when the open() method has been successfully invoked. During
     * this state request headers can be set using setRequestHeader() and the request can be made using send().
    */
    public static final int OPENED = 1;

    /**
     * The HEADERS_RECEIVED state is the state of the object when all response headers have been received.
    */
    public static final int HEADERS_RECEIVED = 2;

    /**
     * The LOADING state is the state of the object when the response entity body is being received.
    */
    public static final int LOADING = 3;

    /**
     * The DONE state is the state of the object when either the data transfer has been completed or something went
     * wrong during the transfer (infinite redirects for instance).
    */
    public static final int DONE = 4;

    /**
     * Creates an XMLHttpRequest object.
     * 
     * @return the created object.
    */
    public static XMLHttpRequest create()
    {
        return new XMLHttpRequestImpl();
    }

    /**
     * Aborts the current request.
    */
    public abstract void abort();

    /**
     * Clears the {@link ReadyStateChangeHandler}.
     * 
     * @see #clearOnReadyStateChange()
    */
    public abstract void clearOnReadyStateChange();

    /**
     * Gets all the HTTP response headers, as a single string.
     * 
     * @return the response headers
    */
    public abstract String getAllResponseHeaders();

    /**
     * Get's the current ready-state
     * 
     * @return the ready-state constant
    */
    public abstract int getReadyState();

    /**
     * Gets an HTTP response header.
     * 
     * @param header the response header to be retrieved
     * @return the header value
    */
    public abstract String getResponseHeader(String header);

    /**
     * Gets the response text.
     * 
     * @return the response text
    */
    public abstract String getResponseText();

    /**
     * Gets the status code.
     * 
     * @return the status code
    */
    public abstract int getStatus();

    /**
     * Gets the status text.
     * 
     * @return the status text
    */
    public abstract String getStatusText();

    /**
     * There is an extension to XMLHttpRequest object, that returns a string containing a failure message on case of failure.
     * 
     * @return a failure message or null if there has been no failure.
    */
    public abstract String getFailureMessage();

    /**
     * Opens an asynchronous connection.
     * 
     * @param httpMethod the HTTP method to use
     * @param url the URL to be opened
    */
    public void open(String httpMethod, String url)
    {
        open(httpMethod, url, null, null);
    }

    /**
     * Opens an asynchronous connection.
     * 
     * @param httpMethod the HTTP method to use
     * @param url the URL to be opened
     * @param user user to use in the URL
    */
    public void open(String httpMethod, String url, String user)
    {
        open(httpMethod, url, user, null);
    }

    /**
     * Opens an asynchronous connection.
     * 
     * @param httpMethod the HTTP method to use
     * @param url the URL to be opened
     * @param user user to use in the URL
     * @param password password to use in the URL
    */
    public abstract void open(final String httpMethod, String url, String user, String password);

    /**
     * Initiates a request with no request data. This simply calls {@link #send(String)}
     * with <code>null</code> as an argument.
    */
    public void send()
    {
        send(null);
    }

    /**
     * Initiates a request with data. If there is no data, specify null.
     * 
     * @param requestData the data to be sent with the request
    */
    public abstract void send(String requestData);

    /**
     * Sets the {@link ReadyStateChangeHandler} to be notified when the object's ready-state changes.
     * 
     * @param handler the handler to be called when the ready state changes
     * @see #clearOnReadyStateChange()
    */
    public abstract void setOnReadyStateChange(ReadyStateChangeHandler handler);

    /**
     * Sets single request header.
     * 
     * @param header the header to be set
     * @param value the header's value
    */
    public abstract void setRequestHeader(String header, String value);

}