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

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.handler.timeout.TimeoutException;
import org.jboss.netty.util.HashedWheelTimer;

// TODO 'channels' should became a Map<HOST+PORT, Queue<Channel>>
public class ChannelService
{
    public interface ChannelMessageHandler
    {
        public void onMessageReceived(MessageEvent event) throws Exception;
        public void onException(ExceptionEvent event, boolean closeChannel) throws Exception;
    }

    private static final int NIO_TIMEOUT_SECONDS = Integer.getInteger("gwtJavaClient.nioTimeoutSeconds", 30);

    private final ClientBootstrap bootstrap;

    private final Queue<Channel> channels = new ConcurrentLinkedQueue<Channel>();
    private final AtomicLong opennedChannels = new AtomicLong();
    private final Map<Channel, ChannelMessageHandler> handlers = new ConcurrentHashMap<Channel, ChannelMessageHandler>();
    private final ChannelGroup channelGroup = new DefaultChannelGroup();

    // TODO There are a lot of resources allocated here that have to be released manually, but currently there is no way to do so...
    public ChannelService()
    {
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootstrap.setOption("connectTimeoutMillis", TimeUnit.MILLISECONDS.convert(NIO_TIMEOUT_SECONDS, TimeUnit.SECONDS));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory()
        {
            @Override
            public ChannelPipeline getPipeline() throws Exception
            {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("0", new IdleStateHandler(new HashedWheelTimer(), 0, 0, NIO_TIMEOUT_SECONDS)); 
                pipeline.addLast("1", new HttpClientCodec(4096, 8192, 1024));
                pipeline.addLast("2", new HttpContentDecompressor());
                pipeline.addLast("3", new SimpleChannelUpstreamHandler()
                {
                    @Override
                    public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws Exception
                    {
                        ChannelMessageHandler handler = handlers.get(ctx.getChannel());
                        if (handler != null)
                        {
                            handler.onMessageReceived(event);
                        }
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent event) throws Exception
                    {
                        ChannelMessageHandler handler = handlers.get(ctx.getChannel());
                        if (handler != null)
                        {
                            handler.onException(event, event.getCause() instanceof TimeoutException);
                        }
                    }

                });
                return pipeline;
            }
        });
    }

    public Channel acquireChannel(URI uri, ChannelMessageHandler handler)
    {
        Channel channel = null;
        while ((channel = channels.poll()) != null && !channel.isConnected())
        {
            channel.close(); // TODO check if we do really need it or there is no need to close unconnected channel...
            opennedChannels.decrementAndGet();
        }

        if (channel == null)
        {
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(uri.getHost(), uri.getPort())).awaitUninterruptibly();
            if (!future.isSuccess())
            {
                return null;
            }

            channel = future.getChannel();
            channelGroup.add(channel);
            opennedChannels.incrementAndGet();
        }

        handlers.put(channel, handler);
        return channel;
    }

    public void releaseChannel(Channel channel, boolean close)
    {
        handlers.remove(channel);
        if (close)
        {
            channel.close();
            opennedChannels.decrementAndGet();
        }
        else
        {
            channels.add(channel);
        }
    }

    public long getOpennedChannelsCount()
    {
        return opennedChannels.get();
    }

}