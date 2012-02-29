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
*/
package com.mind.gwt.jclient.context;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.InitializeEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Event.NativePreviewEvent;

class EventRepository
{
    /**
     * A few {@link GwtEvent} subclasses coming along with GWT-SDK initialize their {@link GwtEvent.Type TYPE} fields
     * lazily and in non thread-safe way. This method forces initialization immediately. To avoid a race condition,
     * memory writes that are made by this method, have to become visible to all threads before the first instance of
     * {@link Context} is created.
    */
    static void instantiateLazyEventTypes()
    {
        AttachEvent.getType();
        BeforeSelectionEvent.getType();
        CloseEvent.getType();
        HighlightEvent.getType();
        InitializeEvent.getType();
        OpenEvent.getType();
        ResizeEvent.getType();
        SelectionEvent.getType();
        ShowRangeEvent.getType();
        ValueChangeEvent.getType();
        NativePreviewEvent.getType();
        ColumnSortEvent.getType();

        // LoadingStateChangeEvent is thread-safe
        // PlaceChangeEvent is thread-safe
        // PlaceChangeRequestEvent is thread-safe
        // ClosingEvent is thread-safe
        // ScrollEvent is thread-safe

        // SubmitEvent isn't publicly available but thread-safe by accident
        // SubmitCompleteEvent isn't publicly available and isn't thread-safe... 
    };

    private EventRepository() {}

}