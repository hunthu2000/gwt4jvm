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
package com.google.gwt.user.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.UrlBuilder;
import com.mind.gwt.jclient.GwtJavaClient;
import com.mind.gwt.jclient.GwtJavaClientListener;
import com.mind.gwt.jclient.context.Context;

/**
 * This class should provides access to the browser window's methods, properties, and events.
*/
public class Window
{
    /**
     * Fired just before the browser window closes or navigates to a different site.
    */
    public static class ClosingEvent extends GwtEvent<Window.ClosingHandler>
    {
        /**
         * The event type.
        */
        private static final Type<ClosingHandler> TYPE = new Type<ClosingHandler>();

        static Type<ClosingHandler> getType()
        {
            return TYPE;
        }

        /**
         * The message to display to the user to see whether they really want to leave the page.
        */
        private String message = null;

        @Override
        public final Type<ClosingHandler> getAssociatedType()
        {
            return TYPE;
        }

        /**
         * Get the message that will be presented to the user in a confirmation
         * dialog that asks the user whether or not she wishes to navigate away from
         * the page.
         * 
         * @return the message to display to the user, or null
        */
        public String getMessage()
        {
            return message;
        }

        /**
         * Set the message to a <code>non-null</code> value to present a
         * confirmation dialog that asks the user whether or not she wishes to
         * navigate away from the page. If multiple handlers set the message, the
         * last message will be displayed; all others will be ignored.
         * 
         * @param message the message to display to the user, or null
        */
        public void setMessage(String message)
        {
            this.message = message;
        }

        @Override
        protected void dispatch(ClosingHandler handler)
        {
            handler.onWindowClosing(this);
        }
    }

    /**
     * Handler for {@link Window.ClosingEvent} events.
    */
    public interface ClosingHandler extends EventHandler
    {
        /**
         * Fired just before the browser window closes or navigates to a different
         * site. No user-interface may be displayed during shutdown.
         * 
         * @param event the event
        */
        void onWindowClosing(Window.ClosingEvent event);
    }

    /**
     * This class provides access to the browser's location's object. The location
     * object contains information about the current URL and methods to manipulate
     * it. <code>Location</code> is a very simple wrapper, so not all browser
     * quirks are hidden from the user.
    */
    public static class Location
    {
        public static void assign(String newURL) {}

        public static UrlBuilder createUrlBuilder()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getHash()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getHost()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getHostName()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getHref()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getParameter(String name)
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static Map<String, List<String>> getParameterMap()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getPath()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getPort()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getProtocol()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getQueryString()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static void reload() {}

        public static void replace(String newURL) {}

        private Location() {}
    }

    /**
     * This class provides access to the browser's navigator object. The mimeTypes and plugins properties are not included.
    */
    public static class Navigator
    {
        public static String getAppCodeName()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getAppName()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getAppVersion()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getPlatform()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static String getUserAgent()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static boolean isCookieEnabled()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        public static boolean isJavaEnabled()
        {
            throw new UnsupportedOperationException("Method isn't implemented yet");
        }

        private Navigator() {}

    }

    /**
     * Fired when the browser window is scrolled.
    */
    public static class ScrollEvent extends GwtEvent<Window.ScrollHandler>
    {
        /**
         * The event type.
        */
        static final Type<Window.ScrollHandler> TYPE = new Type<Window.ScrollHandler>();

        static Type<Window.ScrollHandler> getType()
        {
            return TYPE;
        }

        private int scrollLeft;
        private int scrollTop;

        /**
         * Construct a new {@link Window.ScrollEvent}.
         * 
         * @param scrollLeft the left scroll position
         * @param scrollTop the top scroll position
        */
        private ScrollEvent(int scrollLeft, int scrollTop)
        {
            this.scrollLeft = scrollLeft;
            this.scrollTop = scrollTop;
        }

        @Override
        public final Type<ScrollHandler> getAssociatedType()
        {
            return TYPE;
        }

        /**
         * Gets the window's scroll left.
         * 
         * @return window's scroll left
        */
        public int getScrollLeft()
        {
            return scrollLeft;
        }

        /**
         * Get the window's scroll top.
         * 
         * @return the window's scroll top
        */
        public int getScrollTop()
        {
            return scrollTop;
        }

        @Override
        protected void dispatch(ScrollHandler handler)
        {
            handler.onWindowScroll(this);
        }
    }

    /**
     * Handler for {@link Window.ScrollEvent} events.
    */
    public interface ScrollHandler extends EventHandler
    {
        /**
         * Fired when the browser window is scrolled.
         * 
         * @param event the event
         */
        void onWindowScroll(Window.ScrollEvent event);
    }

    /**
     * Adds a {@link CloseEvent} handler.
     * 
     * @param handler the handler
     * @return returns the handler registration
    */
    public static HandlerRegistration addCloseHandler(final CloseHandler<Window> handler)
    {
        Context.getCurrentContext().getClient().addListener(new GwtJavaClientListener()
        {
            @Override
            public void onFinish(GwtJavaClient client)
            {
                handler.onClose(null); // TODO `null` has to be changed to the authentic CloseEvent somehow...
            }

        });
        return null; // TODO useful HandlerRegistration should be returned instead of `null`...
    }

    public static HandlerRegistration addResizeHandler(ResizeHandler handler)
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    /**
     * Adds a {@link Window.ClosingEvent} handler.
     * 
     * @param handler the handler
     * @return returns the handler registration
    */
    public static HandlerRegistration addWindowClosingHandler(final ClosingHandler handler)
    {
        Context.getCurrentContext().getClient().addListener(new GwtJavaClientListener()
        {
            @Override
            public void onFinish(GwtJavaClient client)
            {
                handler.onWindowClosing(new Window.ClosingEvent());
            }

        });
        return null; // TODO useful HandlerRegistration should be returned instead of `null`...
    }

    public static HandlerRegistration addWindowScrollHandler(Window.ScrollHandler handler)
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    public static void alert(String msg)
    {
        System.out.println(msg);
    }

    public static boolean confirm(String msg)
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    public static void enableScrolling(boolean enable) {}

    public static int getClientHeight()
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    public static int getClientWidth()
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    public static int getScrollLeft()
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    public static int getScrollTop()
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    public static String getTitle()
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    public static void moveBy(int dx, int dy) {}

    public static void moveTo(int x, int y) {}

    public static void open(String url, String name, String features) {}

    public static void print() {}

    public static String prompt(String msg, String initialValue)
    {
        throw new UnsupportedOperationException("Method isn't implemented yet");
    }

    public static void resizeBy(int width, int height) {}

    public static void resizeTo(int width, int height) {}

    public static void scrollTo(int left, int top) {}

    public static void setMargin(String size) {}

    public static void setStatus(String status) {}

    public static void setTitle(String title) {}

}