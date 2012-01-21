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
 *     Copyright 2007 Google Inc.
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

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.DefaultCookie;

import com.google.gwt.dev.util.collect.HashSet;
import com.mind.gwt.jclient.context.Context;

/**
 * Provides access to browser cookies stored on the client. Because of browser restrictions, you will only be able to
 * access cookies associated with the current page's domain.
*/
public class Cookies
{
    /**
     * Gets the cookie associated with the given name.
     * 
     * @param name the name of the cookie to be retrieved.
     * @return the cookie's value, or <tt>null</tt> if the cookie doesn't exist.
    */
    public static String getCookie(String name)
    {
        for (Cookie cookie : Context.getCurrentContext().getCookies())
        {
            if (cookie.getName().equals(name))
            {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * Gets the names of all cookies in this page's domain.
     * 
     * @return the names of all cookies.
    */
    public static Collection<String> getCookieNames()
    {
        Collection<String> result = new HashSet<String>(); 
        for (Cookie cookie : Context.getCurrentContext().getCookies())
        {
            result.add(cookie.getName());
        }
        return result;
    }

    /**
     * Checks whether or not cookies are enabled or disabled.
     * 
     * @return true if a cookie can be set, false if not.
    */
    public static boolean isCookieEnabled()
    {
        return true;
    }

    /**
     * Removes the cookie associated with the given name.
     * 
     * @param name the name of the cookie to be removed.
    */
    public static void removeCookie(String name)
    {
        removeCookie(name, null);
    }

    /**
     * Removes the cookie associated with the given name.
     * 
     * @param name the name of the cookie to be removed;
     * @param path the path to be associated with this cookie (which should match the path given in {@link #setCookie}).
    */
    public static void removeCookie(String name, String path)
    {
        for (Cookie cookie : Context.getCurrentContext().getCookies())
        {
            if (name.equals(cookie.getName()) && (path == null || path.equals(cookie.getPath())))
            {
                Context.getCurrentContext().getCookies().remove(cookie);
            }
        }
    }

    /**
     * Sets a cookie. The cookie will expire when the current browser session is ended.
     * 
     * @param name the cookie's name.
     * @param value the cookie's value.
    */
    public static void setCookie(String name, String value)
    {
        setCookie(name, value, null, null, null, false);
    }

    /**
     * Sets a cookie.
     * 
     * @param name the cookie's name;
     * @param value the cookie's value;
     * @param expires when the cookie expires.
    */
    public static void setCookie(String name, String value, Date expires)
    {
        setCookie(name, value, expires, null, null, false);
    }

    /**
     * Sets a cookie. If uriEncoding is false, it checks the validity of name and value. Name: Must conform to RFC
     * 2965. Not allowed: = , ; white space. Also can't begin with $. Value: No = or ;
     * 
     * @param name - the cookie's name;
     * @param value - the cookie's value;
     * @param expires - when the cookie expires;
     * @param domain - the domain to be associated with this cookie;
     * @param path - the path to be associated with this cookie;
     * @param secure - <tt>true</tt> to make this a secure cookie (that is, only accessible over an SSL connection).
    */
    public static void setCookie(String name, String value, Date expires, String domain, String path, boolean secure)
    {
        Cookie cookie = new DefaultCookie(name, value);
        if (expires != null)
        {
            cookie.setMaxAge((int) TimeUnit.SECONDS.convert(expires.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
        }
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setSecure(secure);
        Context.getCurrentContext().getCookies().add(cookie);
    }

    private Cookies() {}

}