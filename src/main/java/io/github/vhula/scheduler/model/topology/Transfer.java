package io.github.vhula.scheduler.model.topology;

import io.github.vhula.scheduler.model.graph.Connection;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 26.12.12
 * Time: 23:58
 * To change this template use File | Settings | File Templates.
 */
public class Transfer {

    private HashMap<Channel, Channel> depending = new HashMap<Channel, Channel>();

    private ArrayList<Channel> queue = new ArrayList<Channel>();

    private ArrayList<Connection> transfers = new ArrayList<Connection>();

    private VectorTopology vectorTopology;

    public Transfer(VectorTopology vectorTopology) {
        this.vectorTopology = vectorTopology;
    }

    public void addChannel(Channel channel, Channel preceding) {
        queue.add(channel);
        depending.put(channel, preceding);
    }

    public void addTransfer(Connection c) {
        if (c == null) {
            throw new IllegalArgumentException("Connection cannot be null!");
        }
        transfers.add(c);
    }

    public void removeChannel(Channel channel) {
        queue.remove(channel);
    }

    public boolean finished() {
        ArrayList<Channel> channels = vectorTopology.getChannels();
        for (Connection c : transfers) {
            for (Channel ch : channels) {
                ArrayList<Connection> chCons = ch.getQueue();
                for (Connection chcon : chCons) {
                    if (chcon.equals(c)) {
                        return false;
                    }
                }
                Connection chcon = ch.getConnection();
                if (c.equals(chcon)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int time() {
        int res = 0;
        for (Channel c : queue) {
            res += c.time();
        }
        return res;
    }

    public void update() {
        //TODO
    }

    public ArrayList<Channel> getQueue() {
        return queue;
    }

}
