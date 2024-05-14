package io.github.vhula.scheduler.model.topology;

import io.github.vhula.scheduler.model.graph.Connection;
import io.github.vhula.scheduler.model.graph.Node;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 25.12.12
 * Time: 21:06
 * To change this template use File | Settings | File Templates.
 */
public class Channel {

    private boolean free = true;

    private ArrayList<Integer> life = new ArrayList<Integer>();

    private ArrayList<Connection> transfers = new ArrayList<Connection>();

    private ArrayList<Connection> queue = new ArrayList<Connection>();

    private Connection transfer = null;

    private Processor source = null;

    private Processor destination = null;

    private int transferSteps = 0;

    public boolean stepping = false;

    private VectorTopology vectorTopology;

    public ArrayList<String> log = new ArrayList<String>();

    public Channel(Processor from, Processor to, VectorTopology vectorTopology) {
        if (from == null) {
            throw new IllegalArgumentException("Source cannot be null!");
        }
        if (to == null) {
            throw new IllegalArgumentException("Destination cannot be null!");
        }
        this.source = from;
        this.destination = to;
        this.vectorTopology = vectorTopology;
    }

    public void send(Connection transfer) {
        if (transfer == null) {
            throw new IllegalArgumentException("Transfer cannot be null!");
        }
        if (!transfers.contains(transfer) && !queue.contains(transfer)) {
            transfers.add(transfer);
            queue.add(transfer);
        }
//        if (this.transfer == null) {
//            this.transfer = transfer;
//            queue.remove(transfer);
//            transferSteps = transfer.getWeight();
//            occupy();
//        }
    }

    public void step2() {
        if (getSource().getNumber() == 2 && queue.size() > 0) {
            System.out.println();
        }
        ArrayList<Channel> channels = vectorTopology.getChannels();
        Node n = null;
        for (Connection c : queue) {
            boolean flag = true;
            for (Channel ch : channels) {
                ArrayList<Connection> chCons = ch.getQueue();
                Node s = c.getStart();
                Node e = c.getEnd();
                Processor sp = vectorTopology.getProcessor2(s);
                Processor ep = vectorTopology.getProcessor2(e);
//                for (Connection chcon : chCons) {
//                    if (chcon.equals(c) && ch.getSource().getNumber() < this.getSource().getNumber()) {
//                        flag = false;
//                        break;
//                    }
//                }
                if (c.getStart().getNumber() == 11 && getSource().getNumber() == 1) {
                    System.out.println();

                }
                if (sp != null && ep != null) {
                    if (sp.getNumber() >= ep.getNumber()) {
                        for (Connection chcon : chCons) {
                            if (chcon.equals(c) && ch.getSource().getNumber() > this.getSource().getNumber()) {
                                flag = false;
                                break;
                            }
                        }
                        if (!ch.canWeGo(c)) {
                            flag = false;
                            break;
                        }
                    } else {
                        for (Connection chcon : chCons) {
                            if (chcon.equals(c) && ch.getSource().getNumber() < this.getSource().getNumber()) {
                                flag = false;
                                break;
                            }
                        }
                    }
                } else {
                    boolean red = true;
                    for (Connection chcon : chCons) {
                        if (chcon.equals(c) && ch.getSource().getNumber() < this.getSource().getNumber()) {
                            flag = false;
                            red = false;
                            break;
                        }
                    }
//                    if (!red) {
//                        for (Connection chcon : chCons) {
//                            if (chcon.equals(c) && ch.getSource().getNumber() > this.getSource().getNumber()) {
//                                flag = false;
//                                break;
//                            }
//                        }
//                    }
                }
//                if (sp != null && ep != null && sp.getNumber() > ep.getNumber()) {
//                    for (Connection chcon : chCons) {
//                        if (chcon.equals(c) && ch.getSource().getNumber() < this.getSource().getNumber()) {
//                            flag = false;
//                            break;
//                        }
//                    }
//                } else {
//                    if (ch.hasConnectionInQueue(c) && ch.getSource().getNumber() > getSource().getNumber()) {
//                        flag = false;
//                        break;
//                    }
//                }
                Connection chcon = ch.getConnection();
                n = chcon == null ? null : chcon.getStart();
                if (!getSource().finished(n, false)) {
                    flag = false;
                    break;
                }
                if (c.equals(chcon) && ch.getSource().getNumber() < this.getSource().getNumber()) {
                    flag = false;
                    break;
                }
            }
            if (flag && getSource().finished(n, false)) {
                if (transfer == null) {
                    transfer = c;
                    transferSteps = transfer.getWeight();
                    queue.remove(transfer);
                    occupy();
                }
                stepping = true;
                break;
            }
        }
        if (transfer == null){// || !stepping) {
            life.add(-1);
            log.add("");
        } else {
//            queue.remove(transfer);
            log.add("T" + transfer.getStart().getNumber() + "->T" + transfer.getEnd().getNumber());
            life.add(transfers.indexOf(transfer));
            transferSteps--;
            if (transferSteps == 0) {
                release();
                transfer = null;
//                if (queue.size() > 0) {
//                    transfer = queue.remove(0);
//                    transferSteps = transfer.getWeight();
//                    occupy();
//                }
            }
        }
    }


    public void step() {
        if (transfer == null) {
            chooseTransfer();
        }
        if (transfer == null) {
            life.add(-1);
            log.add("");
        } else {
            log.add("T" + transfer.getStart().getNumber() + "->T" + transfer.getEnd().getNumber());
            life.add(transfers.indexOf(transfer));
            transferSteps--;
            if (transferSteps == 0) {
                release();
                transfer = null;
            }
        }
    }

    public void chooseTransfer() {
        for (Connection c : queue) {
            if (c.getStart().getNumber() == 5 && getSource().getNumber() == 0) {
                System.out.println();
            }
            boolean flag = true;
            Node sn = c.getStart();
            Node en = c.getEnd();
            Processor sp = vectorTopology.getProcessor2(sn);
            Processor ep = vectorTopology.getProcessor2(en);
            if (sp == null || ep == null)
                return;
            ArrayList<Channel> channels = vectorTopology.getChannels();
            if (sp.getNumber() > ep.getNumber()) {
                for (Channel channel : channels) {
                    if (channel.hasConnectionInQueue(c) || c.equals(channel.getConnection())) {
                        if (getSource().getNumber() < channel.getSource().getNumber()) {
                            flag = false;
                            break;
                        }
                    }
                }
            } else {
                for (Channel channel : channels) {
                    if (channel.hasConnectionInQueue(c) || c.equals(channel.getConnection())) {
                        if ((getSource().getNumber() > channel.getSource().getNumber())) {
                            flag = false;
                            break;
                        }
                    }
                }
            }
            if (flag) {
                transfer = c;
                transferSteps = c.getWeight();
                queue.remove(transfer);
                return;
            }
        }
    }

    public boolean canWeGo(Connection c) {
        if (c == null) {
            return true;
        }
        if (transfers.contains(c)) {
            if (life.get(life.size() - 1) == transfers.indexOf(c)) {
                return false;
            }
        }
        return true;
    }

    public Connection getConnection(int index) {
        return transfers.get(index);
    }

    public boolean hasConnectionInQueue(Connection connection) {
        for (Connection c : queue) {
            if (c.equals(connection)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Connection> getQueue() {
        return queue;
    }

    public Connection getConnection() {
        if (transfer == null) {
            if (queue.size() > 0) {
                return queue.get(queue.size() - 1);
            }
        }
        return transfer;
    }

    public boolean available() {
        return free;
    }

    public void occupy() {
        free = false;
    }

    public void release() {
        free = true;
    }

    public Processor getSource() {
        return source;
    }

    public Processor getDestination() {
        return destination;
    }

    public int time() {
        int res = transferSteps;
        for (Connection c : queue) {
            res += c.getWeight();
        }
        return res;
    }

    public ArrayList<Integer> getLife() {
        return life;
    }

}
