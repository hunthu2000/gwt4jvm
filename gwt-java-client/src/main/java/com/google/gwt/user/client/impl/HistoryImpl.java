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
package com.google.gwt.user.client.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.mind.gwt.jclient.GwtJavaClient;
import com.mind.gwt.jclient.GwtJavaClientListener;
import com.mind.gwt.jclient.context.Context;

/**
 * Native implementation associated with {@link com.google.gwt.user.client.History}.
*/
public class HistoryImpl implements HasValueChangeHandlers<String>
{
    private static ConcurrentMap<Context, String> tokens = new ConcurrentHashMap<Context, String>();

    private HandlerManager handlers = new HandlerManager(null);

    public static String getToken()
    {
        String token = tokens.get(Context.getCurrentContext());        
        return token == null ? "" : token; 
    }

    protected static void setToken(String token)
    {
        assert token != null;
        if (tokens.putIfAbsent(Context.getCurrentContext(), token) == null)
        {
            Context.getCurrentContext().getClient().addListener(new GwtJavaClientListener()
            {
                @Override
                public void onFinish(GwtJavaClient client)
                {
                    tokens.remove(Context.getCurrentContext());
                }
            });
        }
    }

    /**
     * Adds a {@link ValueChangeEvent} handler to be informed of changes to the browser's history stack.
     * 
     * @param handler - the handler.
    */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler)
    {
        return handlers.addHandler(ValueChangeEvent.getType(), handler);
    }

    public void fireEvent(GwtEvent<?> event)
    {
        handlers.fireEvent(event);
    }

    /**
     * Fires the {@link ValueChangeEvent} to all handlers with the given tokens.
    */
    public void fireHistoryChangedImpl(String newToken)
    {
        ValueChangeEvent.fire(this, newToken);
    }

    public HandlerManager getHandlers()
    {
        return handlers;
    }

    public boolean init()
    {
        return true;
    }

    public final void newItem(String historyToken, boolean issueEvent)
    {
        historyToken = (historyToken == null) ? "" : historyToken;
        if (!historyToken.equals(getToken()))
        {
            setToken(historyToken);
            nativeUpdate(historyToken);
            if (issueEvent)
            {
                fireHistoryChangedImpl(historyToken);
            }
        }
    }

    public final void newItemOnEvent(String historyToken)
    {
        historyToken = (historyToken == null) ? "" : historyToken;
        if (!historyToken.equals(getToken()))
        {
            setToken(historyToken);
            nativeUpdateOnEvent(historyToken);
            fireHistoryChangedImpl(historyToken);
        }
    }

    protected void nativeUpdate(String historyToken)
    {
        // There is nothing to do in GwtJavaClient.
    }

    protected void nativeUpdateOnEvent(String historyToken)
    {
        // There is nothing to do in GwtJavaClient.
    }

}