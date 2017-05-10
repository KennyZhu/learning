package org.kennyzhu.netflix.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import rx.Observable;
import rx.Subscriber;

import java.util.concurrent.Future;

/**
 * desc
 * author yanlongzhu
 * date:2017/2/27.
 */
public class HystrixCommandTest {

    @HystrixCommand
    public Observable<Object> test() {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            public void call(Subscriber<? super Object> observer) {
                System.out.println("######");
                try {
                    if (!observer.isUnsubscribed()) {
                        observer.onNext(new Object());
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });

    }

    @HystrixCommand(fallbackMethod = "dealFallBack")
    public void fallbackTest(String id) {
        throw new RuntimeException("#Error");
    }

    @HystrixCommand
    public void dealFallBack(String id) {
        System.out.println(id);

    }

    @HystrixCommand(groupKey = "exceptionTest", commandKey = "exceptionTest",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "false"),
                    @HystrixProperty(name = "fallback.enabled", value = "false"),
                    @HystrixProperty(name = "execution.timeout.enabled", value = "false")
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "10"),
                    @HystrixProperty(name = "maxQueueSize", value = "100"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "100"),
            }
    )
    public Future exceptionTest() {
        return new AsyncResult() {
            @Override
            public Object invoke() {
//                throw new RuntimeException("#Error");
                return "ddd";
            }
        };
    }

    public static void main(String[] args) {
        HystrixCommandTest test = new HystrixCommandTest();
//        test.test();
        Future future = test.exceptionTest();
        try {
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
