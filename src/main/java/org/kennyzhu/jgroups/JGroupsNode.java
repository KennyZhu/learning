package org.kennyzhu.jgroups;

import org.jgroups.*;
import org.jgroups.blocks.MethodCall;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Desc:
 * Date: 14-1-6
 * Time: 下午3:51
 * User: ylzhu
 */
public class JGroupsNode implements Receiver {

    private String msg;

    private JChannel channel;
    private RpcDispatcher dispatcher;
    private String clusterName;
    private final String props = "props.xml";

    //当节点只有自己时，不发送
    private boolean sendMessage;

    private boolean readOnly;

    private static final short GET_STATUS = 1;
    private static final long DEFAULT_TIMEOUT = 15000L;
    private final Set<Notification> notifs = new CopyOnWriteArraySet<Notification>();
    private static final RequestOptions CALL_OPTIONS = new RequestOptions(ResponseMode.GET_ALL, DEFAULT_TIMEOUT);
    private static final Logger LOGGER = LoggerFactory.getLogger(JGroupsNode.class);

    public JGroupsNode(String clusterName, boolean readOnly) {
        this.clusterName = clusterName;
        this.readOnly = readOnly;
    }

    public void start() throws Exception {
        this.channel = new JChannel(props);
        this.channel.setReceiver(this);
        this.channel.connect(clusterName);
        this.dispatcher = new RpcDispatcher(channel, this, this, this);
        channel.setDiscardOwnMessages(true);
        sendMessage = channel.getView().size() > 1;
        channel.getView().toString();
        channel.getState(null, DEFAULT_TIMEOUT);
        LOGGER.info("*****Channel started :{}", clusterName);
    }

    @Override
    public void viewAccepted(View addresses) {
        LOGGER.info("******View Accepted :{} ", addresses.getMembers());
    }

    @Override
    public void suspect(Address address) {
        LOGGER.warn("******Suspect address is :{}", address);
    }

    @Override
    public void block() {

    }

    @Override
    public void unblock() {

    }

    @Override
    public void receive(Message message) {
        LOGGER.info("*****Receive message :{}", message);
    }

    @Override
    public void getState(OutputStream outputStream) throws Exception {
        LOGGER.info("***** Get state");
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(outputStream, 1024));
            oos.writeObject(msg);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            oos.close();
        }
    }

    @Override
    public void setState(InputStream inputStream) throws Exception {
        ObjectInputStream ois = null;
        String message = "";
        try {
            ois = new ObjectInputStream(new BufferedInputStream(inputStream, 1024));
            msg = message = (String) ois.readObject();
            LOGGER.info("******Get Message :{}", message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ois.close();
        }
    }

    public String getMsg(String msg) {
        try {
            LOGGER.info("******Get Msg :{}", msg);
            this.msg = msg;
            MethodCall call = new MethodCall("getMsg", new Object[]{msg}, new Class[]{JGroupsNode.class});
            dispatcher.callRemoteMethods(null, call, CALL_OPTIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }


    private void sendViewChangeNotifications(View view, List<Address> new_mbrs, List<Address> old_mbrs) {
        List<Address> joined, left;

        if ((notifs.isEmpty()) || (old_mbrs == null) || (new_mbrs == null))
            return;

        // 1. Compute set of members that joined: all that are in new_mbrs, but
        // not in old_mbrs
        joined = new ArrayList<Address>();
        for (Address mbr : new_mbrs) {
            if (!old_mbrs.contains(mbr))
                joined.add(mbr);
        }

        // 2. Compute set of members that left: all that were in old_mbrs, but
        // not in new_mbrs
        left = new ArrayList<Address>();
        for (Address mbr : old_mbrs) {
            if (!new_mbrs.contains(mbr)) {
                left.add(mbr);
            }
        }

        for (Notification notif : notifs) {
            notif.viewChange(view, joined, left);
        }
    }

    public interface Notification {

        void viewChange(View view, java.util.List<Address> mbrs_joined, java.util.List<Address> mbrs_left);
    }

    public static void main(String[] args) {
        JGroupsNode node = new JGroupsNode("jgroups_test", false);
        try {
            node.start();
            for (int i = 0; i < 10; i++) {
                Thread.sleep(30 * 1000);
                node.getMsg("============================Test====" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
