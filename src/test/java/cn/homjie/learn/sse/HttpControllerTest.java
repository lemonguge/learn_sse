package cn.homjie.learn.sse;

import java.util.concurrent.CountDownLatch;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.junit.jupiter.api.Test;

class HttpControllerTest {

    @Test
    void stream() throws InterruptedException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
            .url("http://localhost:8080/stream")
            .build();

        CountDownLatch latch = new CountDownLatch(1);
        EventSources.createFactory(client)
            .newEventSource(request, new EventSourceListener() {
                @Override
                public void onEvent(EventSource eventSource, String id, String type, String data) {
                    System.out.println("SSE received: " + data);
                }

                @Override
                public void onClosed(EventSource eventSource) {
                    System.out.println("SSE completed");
                    latch.countDown();
                }

                @Override
                public void onFailure(EventSource eventSource, Throwable t, Response response) {
                    System.err.println("SSE connection failed: " + t.getMessage());
                    t.printStackTrace();
                    eventSource.cancel();
                    latch.countDown();
                }
            });

        // 阻塞等待 SSE 相应结束
        latch.await();
    }

    @Test
    void stream2() throws InterruptedException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
            .url("http://localhost:8080/stream")
            .build();

        CountDownLatch latch = new CountDownLatch(1);
        EventSources.createFactory(client)
            .newEventSource(request, new EventSourceListener() {
                @Override
                public void onEvent(EventSource eventSource, String id, String type, String data) {
                    System.out.println("SSE received: " + data);
                    if ("3".equals(id)) {
                        eventSource.cancel();
                    }
                }

                @Override
                public void onClosed(EventSource eventSource) {
                    System.out.println("SSE completed");
                    latch.countDown();
                }

                @Override
                public void onFailure(EventSource eventSource, Throwable t, Response response) {
                    System.err.println("SSE connection failed: " + t.getMessage());
                    t.printStackTrace();
                    eventSource.cancel();
                    latch.countDown();
                }
            });

        // 阻塞等待 SSE 相应结束
        latch.await();
    }
}