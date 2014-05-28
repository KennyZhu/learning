package org.kennyzhu.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Desc:
 * <p/>Date: 2014/5/27
 * <br/>Time: 17:45
 * <br/>User: ylzhu
 */
public class ZkClient implements Watcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkClient.class);

    private ZooKeeper zk;

    private ZkConf zkConf;

    private CountDownLatch connectedSignal;


    public void connection() throws Exception {
        if (zk != null && zk.getState().isAlive()) {
            zk.close();
        }
        zk = new ZooKeeper(zkConf.getServer(), zkConf.getTimeout(), this);
        connectedSignal = new CountDownLatch(1);
        connectedSignal.await();
        if (zkConf.getScheme() != null) {
            zk.addAuthInfo(zkConf.getScheme(), zkConf.getAuth().getBytes());
        }

        if ((zkConf.getUseACL() != 0) && (zkConf.getAcls() == ZooDefs.Ids.OPEN_ACL_UNSAFE)) {
            List<ACL> acls = zkConf.getAcls();
            Id authId = new Id("digest", DigestAuthenticationProvider.generateDigest(zkConf.getAuth()));
            Id anyId = new Id("world", "anyone");
            acls.clear();
            acls.add(new ACL(23, anyId));
            acls.add(new ACL(8, authId));
            zkConf.setAcls(acls);
        }
        this.connectedSignal = null;
    }

    public void init() throws Exception {
        connection();
    }

    @PreDestroy
    public void destroy() {
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (connectedSignal != null) {
                connectedSignal.countDown();
            }
        } else if (Event.KeeperState.Expired == event.getState()) {
            try {
                init();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public void setData(String path, String data) throws Exception {
        setData(path, data, CreateMode.PERSISTENT);

    }

    public void setData(String path, String data, CreateMode mode) throws Exception {
        Stat stat = this.zk.exists(path, null);

    }

    public ZooKeeper getZk() {
        return this.zk;
    }

    public ZkConf getZkConf() {
        return zkConf;
    }

    public void setZkConf(ZkConf zkConf) {
        this.zkConf = zkConf;
    }
}
