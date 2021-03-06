package org.kennyzhu.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Desc:
 * <p/>Date: 2014/5/27
 * <br/>Time: 17:08
 * <br/>User: ylzhu
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String CONNECTION_STR = "47.92.225.122:2181,47.92.225.122:2182,47.92.225.122:2183";

    public void test() {
        try {
            //创建一个Zookeeper实例，第一个参数为目标服务器地址和端口，第二个参数为Session超时时间，第三个为节点变化时的回调方法
            // 监控所有被触发的事件
            ZooKeeper zk = new ZooKeeper(CONNECTION_STR, 500000, event -> LOGGER.info("WatchedEvent event is :{}", event.toString()));
            //创建一个节点root，数据是mydata,不进行ACL权限控制，节点为永久性的(即客户端shutdown了也不会消失)
//            LOGGER.info("Create root return :{}", zk.create("/root", "mydata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));

            //在root下面创建一个childone znode,数据为childone,不进行ACL权限控制，节点为永久性的
//            LOGGER.info("Create childone return :{}", zk.create("/root/childone", "childone".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));

            //取得/root节点下的子节点名称,返回List<String>
            List<String> rootChildren = zk.getChildren("/root", true);

            for (String child : rootChildren) {
                LOGGER.info("Root Child :{}", child);
            }

            //取得/root/childone节点下的数据,返回byte[]
            zk.getData("/root/childone", true, null);

            //修改节点/root/childone下的数据，第三个参数为版本，如果是-1，那会无视被修改的数据版本，直接改掉
            zk.setData("/root/childone", "childonemodify".getBytes(), -1);

            //删除/root/childone这个节点，第二个参数为版本，－1的话直接删除，无视版本
            zk.delete("/root/childone", -1);

            //关闭session
            zk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Main main = new Main();
        main.test();
    }
}
