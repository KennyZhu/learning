package org.kennyzhu.zookeeper;

import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;

import java.util.List;

/**
 * Desc:
 * <p/>Date: 2014/5/27
 * <br/>Time: 17:49
 * <br/>User: ylzhu
 */
public class ZkConf {
    private String server;
    private final int timeout = 10000;

    private final String scheme = "digest";

    protected String auth = "admin:admin";

    private int useACL;

    protected List<ACL> acls = ZooDefs.Ids.OPEN_ACL_UNSAFE;

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getAuth() {
        return this.auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public int getUseACL() {
        return this.useACL;
    }

    public void setUseACL(int useACL) {
        this.useACL = useACL;
    }

    public List<ACL> getAcls() {
        return this.acls;
    }

    public void setAcls(List<ACL> acls) {
        this.acls = acls;
    }

    public int getTimeout() {
        return 10000;
    }

    public String getScheme() {
        return "digest";
    }
}
