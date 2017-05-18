package org.kennyzhu.netflix.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * desc
 * author yanlongzhu
 * date:2017/2/22.
 */
public class HystrixTest extends HystrixCommand<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HystrixTest.class);

    private final String groupName;

    public HystrixTest(String groupName) {
        //指定Command名字
        super(HystrixCommandGroupKey.Factory.asKey(groupName));
        this.groupName = groupName;
    }

    protected String run() throws Exception {
        return "#Current thread is :" + Thread.currentThread().getName();

    }

    public static void main(String[] args) throws Exception {
        HystrixTest test1 = new HystrixTest("Synchronous-Hystrix");
        LOGGER.info(test1.execute());
        test1 = new HystrixTest("Asynchronous-Hystrix");
        Future<String> future = test1.queue();
        LOGGER.info(future.get(100, TimeUnit.MILLISECONDS));
    }

    class HystrixObservableTest extends HystrixObservableCommand {

        public HystrixObservableTest(String groupName) {
            super(HystrixCommandGroupKey.Factory.asKey(groupName));
        }

        @Override
        protected Observable construct() {
            return null;
        }
    }

}
