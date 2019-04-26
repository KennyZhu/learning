package org.kennyzhu.zookeeper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

/**
 * desc:
 * author: yanlongzhu
 * date:2019-04-26.
 */
@Data
@Slf4j
public class Master implements Watcher {
    private static final String CONNECTION_STR = "47.92.225.122:2181,47.92.225.122:2182,47.92.225.122:2183";
    ZooKeeper zk;
    String hostPort;

    Master(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZK() {
        try {
            zk = new ZooKeeper(hostPort, 15000, this);
        } catch (Exception e) {
            log.error("#New zk error.Cause:", e);
        }
    }

    public void process(WatchedEvent e) {
        log.info("WatchedEvent event is :{}", e.toString());
    }

    public static void main(String[] args) throws Exception {
        Master m = new Master(CONNECTION_STR);
        m.startZK();
//        log.info("Create childone return :{}", m.zk.create("/root/childone", "childone".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));

        // wait for a bit
        Thread.sleep(60000);
    }
}
