package cn.homjie.learn.sse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author jiehong.jh
 * @date 2023/5/23
 */
@RestController
public class HttpController {

    private final ExecutorService executor = new ThreadPoolExecutor(8, 8,
        60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new CallerRunsPolicy());

    @PreDestroy
    public void close() {
        executor.shutdown();
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(); // 创建 SSEEmitter 对象

        executor.execute(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(1000); // 模拟事件推送
                    emitter.send(SseEmitter.event()
                        .id(String.valueOf(i))
                        .name("eventName")
                        .data("事件数据 " + i, MediaType.TEXT_PLAIN));
                }
            } catch (Exception e) {
                emitter.completeWithError(e); // 发送错误信息
            } finally {
                emitter.complete(); // 关闭 SSEEmitter 对象
                executor.shutdown(); // 关闭线程池
            }
        });

        return emitter;
    }
}
