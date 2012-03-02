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
package com.google.gwt.user.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.impl.HistoryImpl;
import com.mind.gwt.jclient.GwtJavaClient;
import com.mind.gwt.jclient.GwtJavaClientListener;
import com.mind.gwt.jclient.context.Context;

/**
 * This class allows you to interact with the browser's history stack. Each "item" on the stack is represented by a
 * single string, referred to as a "token". You can create new history items (which have a token associated with them
 * when they are created), and you can programmatically force the current history to move back or forward.
 * 
 * <p>In order to receive notification of user-directed changes to the current history item, implement the {@link
 * ValueChangeHandler} interface and attach it via {@link #addValueChangeHandler(ValueChangeHandler)}.
 * 
 * <p><h3>URL Encoding</h3>
 * Any valid characters may be used in the history token and will survive round-trips through {@link #newItem(String)}
 * to {@link #getToken()}/{@link ValueChangeHandler#onValueChange(ValueChangeEvent)}, but most will be encoded in the
 * user-visible URL. The following US-ASCII characters are not encoded on any currently supported browser (but may be
 * in the future due to future browser changes):
 * 
 * <ul>
 *     <li>a-z
 *     <li>A-Z
 *     <li>0-9
 *     <li>;,/?:@&=+$-_.!~*()
 * </ul>
*/
public class History
{

    private static final ConcurrentMap<Context, HistoryImpl> impls = new ConcurrentHashMap<Context, HistoryImpl>();

    /**
     * Adds a {@link ValueChangeEvent} handler to be informed of changes to the browser's history stack.
     * 
     * @param handler - the handler;
     * @return the registration used to remove this value change handler.
    */
    public static HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler)
    {
        return getImpl() != null ? getImpl().addValueChangeHandler(handler) : null;
    }

    /**
     * Programmatic equivalent to the user pressing the browser's 'back' button.
    */
    public static void back()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Fire {@link ValueChangeHandler#onValueChange(ValueChangeEvent)} events with the current history state. This is
     * most often called at the end of an application's {@link EntryPoint#onModuleLoad()} to inform history handlers of
     * the initial application state.
    */
    public static void fireCurrentHistoryState()
    {
        if (getImpl() != null)
        {
            String token = getToken();
            getImpl().fireHistoryChangedImpl(token);
        }
    }

    /**
     * Programmatic equivalent to the user pressing the browser's 'forward' button.
    */
    public static void forward()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the current history token. The handler will not receive a {@link ValueChangeEvent} for the initial token;
     * requiring that an application request the token explicitly on startup gives it an opportunity to run different
     * initialization code in the presence or absence of an initial token.
     * 
     * @return the initial token, or the empty string if none is present.
    */
    public static String getToken()
    {
        return getImpl() != null ? HistoryImpl.getToken() : "";
    }

    /**
     * Adds a new browser history entry. Calling this method will cause {@link ValueChangeHandler#onValueChange
     * (ValueChangeEvent)} to be called as well.
     * 
     * @param historyToken - the token to associate with the new history item.
    */
    public static void newItem(String historyToken)
    {
        newItem(historyToken, true);
    }

    /**
     * Adds a new browser history entry. Calling this method will cause {@link ValueChangeHandler#onValueChange
     * (ValueChangeEvent)} to be called as well if and only if issueEvent is true.
     * 
     * @param historyToken - the token to associate with the new history item;
     * @param issueEvent - true if a {@link ValueChangeHandler#onValueChange(ValueChangeEvent)} event should be issued.
    */
    public static void newItem(String historyToken, boolean issueEvent)
    {
        if (getImpl() != null)
        {
            getImpl().newItem(historyToken, issueEvent);
        }
    }

    private static HistoryImpl getImpl()
    {
        // There is no race condition here, since we guarded by the context.
        HistoryImpl impl = impls.get(Context.getCurrentContext());
        if (impl == null)
        {
            impls.put(Context.getCurrentContext(), impl = new HistoryImpl());
            Context.getCurrentContext().getClient().addListener(new GwtJavaClientListener()
            {
                @Override
                public void onFinish(GwtJavaClient client)
                {
                    impls.remove(Context.getCurrentContext());
                }

            });
        }
        return impl;
    }

}