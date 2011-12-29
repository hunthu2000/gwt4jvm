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
 *     Copyright 2010 Google Inc.
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

import com.google.gwt.core.client.JavaScriptObject;

public class RpcStatsContext
{
    static int getNextRequestId()
    {
        return 0;
    }

    static int getLastRequestId()
    {
        return 0;
    }

    public RpcStatsContext()
    {
    }

    public RpcStatsContext(int requestId)
    {
    }

    public int getRequestId()
    {
        return 0;
    }

    public boolean stats(JavaScriptObject data)
    {
        return false;
    }

    public JavaScriptObject timeStat(String method, String eventType)
    {
        return null;
    }

    public boolean isStatsAvailable()
    {
        return false;
    }

}