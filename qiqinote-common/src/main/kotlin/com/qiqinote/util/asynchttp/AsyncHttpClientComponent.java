package com.qiqinote.util.asynchttp;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by vanki on 2018/7/19 18:10.
 */
public class AsyncHttpClientComponent {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncHttpClientComponent.class);
    private static final int eventLoopGroupThreadNum = 1;
    private static final int connectTimeoutMilliseconds = 5000;
    private static final int requestTimeoutMilliseconds = 300000;
    private static final int readTimeoutTimeoutMilliseconds = 300000;

    private static AsyncHttpClientComponent clientComponent;

    private EventLoopGroup eventLoopGroup;
    private AsyncHttpClient asyncHttpClient;

    private AsyncHttpClientComponent() {
        init();
    }

    public static AsyncHttpClientComponent getInstance() {
        if (clientComponent == null) {
            synchronized (AsyncHttpClientComponent.class) {
                if (clientComponent == null) {
                    clientComponent = new AsyncHttpClientComponent();
                }
            }
        }
        return clientComponent;
    }

    public AsyncHttpClient client() {
        return this.asyncHttpClient;
    }

    private synchronized void init() {
        if (clientComponent != null) {
            return;
        }
        this.asyncHttpClient = new DefaultAsyncHttpClient(initConfig());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        LOG.warn("启动asyncHttpClient服务！");
    }

    private AsyncHttpClientConfig initConfig() {
        String osName = System.getProperty("os.name");
        LOG.warn("初始化asyncHttpClient配置，检测到环境为：" + osName);

        if ("Linux".equalsIgnoreCase(osName)) {
            eventLoopGroup = new EpollEventLoopGroup();
        } else {
            eventLoopGroup = new NioEventLoopGroup(eventLoopGroupThreadNum);
        }
        AsyncHttpClientConfig asyncHttpClientConfig = new DefaultAsyncHttpClientConfig
                .Builder()
                .setConnectTimeout(connectTimeoutMilliseconds)
                .setRequestTimeout(requestTimeoutMilliseconds)
                .setReadTimeout(readTimeoutTimeoutMilliseconds)
                .setEventLoopGroup(eventLoopGroup)
                .build();
        return asyncHttpClientConfig;
    }

    public void shutdown() {
        if (this.asyncHttpClient != null) {
            try {
                this.asyncHttpClient.close();
                eventLoopGroup.shutdownGracefully();
                LOG.warn("关闭asyncHttpClient服务！");
            } catch (IOException exception) {
                LOG.error("Ops!", exception);
            }
        }
    }
}
