package com.qiqinote.util.asynchttp;

import javafx.util.Pair;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Http 请求工具类
 *
 * Created by vanki on 2018/7/19 18:40.
 */
public class AsyncHttpClientUtil {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncHttpClientUtil.class);

    //    private static final ExecutorService resultHandlerExecutor = Executors.newCachedThreadPool();
    private static final ExecutorService resultHandlerExecutor = new ThreadPoolExecutor(0, 64,
            60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public static Pair<Request, Response> doGet(String url) {
        return doGet(url, null);
    }

    /**
     * GET阻塞请求，直到响应数据
     *
     * @param url        请求地址
     * @param preRequest 预请求信息，如：参数、头、请求体等
     *
     * @return 响应数据
     */
    public static Pair<Request, Response> doGet(String url, Consumer<BoundRequestBuilder> preRequest) {
        Pair<Request, ListenableFuture<Response>> result = doGetInFuture(url, preRequest);
        return new Pair(result.getKey(), futureGet(result.getValue()));
    }

    /**
     * GET异步请求，有响应数据时会自动调用resultHandler进入处理
     *
     * @param url           请求地址
     * @param resultHandler 响应数据回调处理函数
     */
    public static void doGetWithResultHandler(String url, BiConsumer<Request, Response> resultHandler) {
        doGetWithResultHandler(url, null, resultHandler);
    }

    /**
     * GET异步请求，有响应数据时会自动调用resultHandler进入处理
     *
     * @param url           请求地址
     * @param preRequest    预请求信息，如：参数、头、请求体等
     * @param resultHandler 响应数据回调处理函数
     */
    public static void doGetWithResultHandler(String url, Consumer<BoundRequestBuilder> preRequest, BiConsumer<Request, Response> resultHandler) {
        Pair<Request, ListenableFuture<Response>> result = doGetInFuture(url, preRequest);
        addListener(resultHandler, result.getKey(), result.getValue());
    }

    /**
     * GET异步请求，可批量发送请求，然后对Future集中处理
     *
     * @param url 请求地址
     *
     * @return
     */
    public static ListenableFuture<Response> doGetInFuture(String url) {
        return doGetInFuture(url, null).getValue();
    }

    /**
     * GET异步请求，可批量发送请求，然后对Future集中处理
     *
     * @param url        请求地址
     * @param preRequest 预请求信息，如：参数、头、请求体等
     *
     * @return key: 请求参数，val：响应数据引用，可通过get()方法获取响应数据（抛异常时为null）
     */
    public static Pair<Request, ListenableFuture<Response>> doGetInFuture(String url, Consumer<BoundRequestBuilder> preRequest) {
        if (url == null) {
            return null;
        }

        BoundRequestBuilder builder = AsyncHttpClientComponent.getInstance().client().prepareGet(url);
        if (preRequest != null) {
            preRequest.accept(builder);
        }
        return new Pair(builder.build(), builder.execute());
    }

    /**
     * POST阻塞请求，直到响应数据
     *
     * @param url        请求地址
     * @param preRequest 预请求信息，如：参数、头、请求体等
     *
     * @return 响应数据
     */
    public static Pair<Request, Response> doPost(String url, Consumer<BoundRequestBuilder> preRequest) {
        Pair<Request, ListenableFuture<Response>> result = doPostInFuture(url, preRequest);
        return new Pair(result.getKey(), futureGet(result.getValue()));
    }

    /**
     * POST异步请求，有响应数据时会自动调用resultHandler进入处理
     *
     * @param url           请求地址
     * @param preRequest    预请求信息，如：参数、头、请求体等
     * @param resultHandler 响应数据回调处理函数
     */
    public static void doPostWithResultHandler(String url, Consumer<BoundRequestBuilder> preRequest, BiConsumer<Request, Response> resultHandler) {
        Pair<Request, ListenableFuture<Response>> result = doPostInFuture(url, preRequest);
        addListener(resultHandler, result.getKey(), result.getValue());
    }

    /**
     * POST异步请求，可批量发送请求，然后对Future集中处理
     *
     * @param url        请求地址
     * @param preRequest 预请求信息，如：参数、头、请求体等
     *
     * @return key: 请求参数，val：响应数据引用，可通过get()方法获取响应数据（抛异常时为null）
     */
    public static Pair<Request, ListenableFuture<Response>> doPostInFuture(String url, Consumer<BoundRequestBuilder> preRequest) {
        if (url == null) {
            return null;
        }

        BoundRequestBuilder builder = AsyncHttpClientComponent.getInstance().client().preparePost(url);
        if (preRequest != null) {
            preRequest.accept(builder);
        }
        return new Pair(builder.build(), builder.execute());
    }

    /**
     * POST阻塞请求，直到响应数据
     *
     * @param url        请求地址
     * @param preRequest 预请求信息，如：参数、头、请求体等
     *
     * @return 响应数据
     */
    public static Pair<Request, Response> doPut(String url, Consumer<BoundRequestBuilder> preRequest) {
        Pair<Request, ListenableFuture<Response>> result = doPutInFuture(url, preRequest);
        return new Pair(result.getKey(), futureGet(result.getValue()));
    }

    /**
     * POST异步请求，可批量发送请求，然后对Future集中处理
     *
     * @param url        请求地址
     * @param preRequest 预请求信息，如：参数、头、请求体等
     *
     * @return key: 请求参数，val：响应数据引用，可通过get()方法获取响应数据（抛异常时为null）
     */
    public static Pair<Request, ListenableFuture<Response>> doPutInFuture(String url, Consumer<BoundRequestBuilder> preRequest) {
        if (url == null) {
            return null;
        }

        BoundRequestBuilder builder = AsyncHttpClientComponent.getInstance().client().preparePut(url);
        if (preRequest != null) {
            preRequest.accept(builder);
        }
        return new Pair(builder.build(), builder.execute());
    }

    private static Response futureGet(ListenableFuture<Response> future) {
        if (future == null) {
            return null;
        }
        try {
            return future.get();
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    private static void addListener(BiConsumer<Request, Response> resultHandler, Request preRequest, ListenableFuture<Response> future) {
        if (future == null || resultHandler == null) {
            return;
        }
        future.addListener(() -> {
            try {
                resultHandler.accept(preRequest, future.get());
            } catch (Exception e) {
                resultHandler.accept(preRequest, null);
                LOG.error("", e);
            }
        }, resultHandlerExecutor);
    }

    public static void main(String[] args) {
        /* 阻塞 */
        Pair<Request, Response> res1 = doGet("https://localhost:8080/test?a=aaa&b=bbb");
        System.out.println("响应数据：" + res1.getValue().getResponseBody());

        Pair<Request, Response> res2 = doGet("https://localhost:8080/test", builder -> {
            builder.setHeader("headInfo", "val");
            builder.addQueryParam("param", "paramVal");
        });
        System.out.println("响应数据：" + res2.getValue());

        /* 异步回调处理 */
        doGetWithResultHandler("https://localhost:8080/test", builder -> {
            builder.setHeader("headInfo", "val");
            builder.addQueryParam("param", "paramVal");
        }, (req, res) -> {
            System.out.println("请求数据：" + req);
            System.out.println("响应数据：" + res);
        });


        /* 异步Future */
        List<ListenableFuture<Response>> futureList = new ArrayList<>(128);
        for (int i = 0; i < 100; i++) {
            futureList.add(doGetInFuture("https://localhost:8080/test?a=aaa&b=bbb"));
        }

        // TODO 这里可以做其它事情

        futureList.forEach(future -> {
            // 批量处理响应数据
            try {
                System.out.println("响应数据：" + future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
