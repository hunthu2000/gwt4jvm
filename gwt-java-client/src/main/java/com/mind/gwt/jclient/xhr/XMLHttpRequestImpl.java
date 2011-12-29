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

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.mind.gwt.jclient.context.Context;
import com.mind.gwt.jclient.xhr.ChannelService.ChannelMessageHandler;

public class XMLHttpRequestImpl extends XMLHttpRequest implements ChannelMessageHandler
{
    private static final ChannelService channelService = new ChannelService();

    private final Context context = Context.getCurrentContext();
    private final ResponseText responseText = new ResponseText();

    private XMLHttpRequest snapshot;
    private Channel channel;
    private HttpRequest request;
    private int status;

    private ReadyStateChangeHandler handler;
    
    public XMLHttpRequestImpl()
    {
        snapshot = new XMLHttpRequestSnapshot(UNSENT, 0, "");
    }

    @Override
    public void abort()
    {
        channelService.releaseChannel(channel, true);  
    }

    @Override
    public void clearOnReadyStateChange()
    {
        handler = null;
    }

    @Override
    public String getAllResponseHeaders()
    {
        return snapshot.getAllResponseHeaders();
    }

    @Override
    public int getReadyState()
    {
        return snapshot.getReadyState();
    }

    @Override
    public String getResponseHeader(String header)
    {
        return snapshot.getResponseHeader(header);
    }

    @Override
    public String getResponseText()
    {
        return snapshot.getResponseText();
    }

    @Override
    public int getStatus()
    {
        return snapshot.getStatus();
    }

    @Override
    public String getStatusText()
    {
        return snapshot.getStatusText();
    }

    @Override
    public String getFailureMessage()
    {
        return snapshot.getFailureMessage();
    }

    @Override
    public void open(final String httpMethod, String url, String user, String password)
    {
        try
        {
            if (user != null || password != null)
            {
                throw new IllegalArgumentException("Authentication is not supported yet");
            }

            if (!(httpMethod.toLowerCase()).matches("get|post"))
            {
                throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
            }

            URI uri = new URI(url == null ? "null" : url); // TODO "null"?! wtf???
            if (uri.getPort() == -1)
            {
                uri = new URI(uri.getScheme(), null, uri.getHost(), 80, uri.getPath(), null, uri.getFragment());
            }

            channel = channelService.acquireChannel(uri, this); // this could be null, but that's all right, we will check it in `send` method 
            request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, httpMethod.charAt(0) == 'p' ||  httpMethod.charAt(0) == 'P' ? HttpMethod.POST : HttpMethod.GET, uri.toASCIIString());
            request.setHeader(HttpHeaders.Names.HOST, uri.getHost() + ':' + uri.getPort());
            request.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            // request.setHeader(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
            fireOnReadyStateChange(new XMLHttpRequestSnapshot(OPENED, 0, ""), false);
        }
        catch (URISyntaxException exception)
        {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
    }

    @Override
    public void send(String requestData)
    {
        if (requestData != null)
        {
            request.setContent(ChannelBuffers.copiedBuffer(requestData, Charset.forName("UTF-8")));
            request.setHeader(HttpHeaders.Names.CONTENT_LENGTH, requestData.length());
        }
        CookieEncoder encoder = new CookieEncoder(false);
        Set<Cookie> cookes = context.getCookies();
        if (!cookes.isEmpty())
        {
            for (Cookie cookie : cookes)
            {
                encoder.addCookie(cookie);
            }
            request.setHeader("Cookie", encoder.encode());
        }
        if (channel == null)
        {
            fireOnReadyStateChange(new XMLHttpRequestSnapshot(DONE, 0, "", "Connection couldn't be established"), false);
        }
        else 
        {
            channel.write(request);
        }
    }

    @Override
    public void setOnReadyStateChange(ReadyStateChangeHandler handler)
    {
        this.handler = handler;
    }

    @Override
    public void setRequestHeader(String header, String value)
    {
        assert request != null : "Request should be opened first!";
        request.setHeader(header, value);
    }

    @Override
    public void onMessageReceived(MessageEvent event) throws Exception
    {
        if (context.getClient().isFinished()) return;

        if (event.getMessage() instanceof HttpResponse)
        {
            HttpResponse response = (HttpResponse) event.getMessage();

            if (response.getHeader("Set-Cookie") != null)
            {
                context.addCookies(new CookieDecoder().decode(response.getHeader("Set-Cookie")));
            }

            status = response.getStatus().getCode();

            fireOnReadyStateChange(new XMLHttpRequestSnapshot(HEADERS_RECEIVED, status, ""), false);

            if (!response.isChunked())
            {
                ChannelBuffer content = response.getContent();
                if (content.readable())
                {
                    responseText.append(content.toString(Charset.forName("UTF-8")));
                }
                fireOnReadyStateChange(new XMLHttpRequestSnapshot(DONE, status, responseText.toString()), false);
            }
        }
        else if (event.getMessage() instanceof HttpChunk)
        {
            HttpChunk chunk = (HttpChunk) event.getMessage();

            if (chunk.isLast())
            {
                fireOnReadyStateChange(new XMLHttpRequestSnapshot(DONE, status, responseText.toString()), false);
            }
            else
            {
                responseText.append(chunk.getContent().toString(Charset.forName("UTF-8")));
                fireOnReadyStateChange(new XMLHttpRequestSnapshot(LOADING, status, responseText.toString()), false);
            }
        }
        else
        {
            throw new IllegalStateException("Unexpected message: " + event.getMessage());
        }
    }

    @Override
    public void onException(ExceptionEvent event, boolean closeChannel) throws Exception
    {
        if (!context.getClient().isFinished())
        {
            fireOnReadyStateChange(new XMLHttpRequestSnapshot(DONE, 0, responseText.toString(), event.getCause().getMessage()), closeChannel);
        }
    }

    private void fireOnReadyStateChange(final XMLHttpRequestSnapshot snapshot, final boolean closeChannel)
    {
        if (this.handler != null)
        {
            context.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    // We are piggybacking on `context` synchronization here. 
                    XMLHttpRequestImpl.this.snapshot = snapshot;
                    handler.onReadyStateChange(snapshot);
                    if (snapshot.getReadyState() == DONE && channel != null)
                    {
                        channelService.releaseChannel(channel, closeChannel);
                    }
                }
            });
        }
    }
}