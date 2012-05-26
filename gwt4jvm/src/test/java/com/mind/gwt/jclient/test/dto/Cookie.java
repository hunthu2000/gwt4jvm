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
package com.mind.gwt.jclient.test.dto;

import java.io.Serializable;
import java.util.Date;

public class Cookie implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String name;
    private String value;
    private Date expires;
    private String domain;
    private String path;
    private boolean secure;

    Cookie() {}

    public Cookie(String name, String value, Date expires, String domain, String path, boolean secure)
    {
        this.name = name;
        this.value = value;
        this.expires = expires;
        this.domain = domain;
        this.path = path;
        this.secure = secure;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Date getExpires()
    {
        return expires;
    }

    public void setExpires(Date expires)
    {
        this.expires = expires;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public boolean isSecure()
    {
        return secure;
    }

    public void setSecure(boolean secure)
    {
        this.secure = secure;
    }

}