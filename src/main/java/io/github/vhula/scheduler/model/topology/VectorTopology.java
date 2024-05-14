package io.github.vhula.scheduler.model.topology;

import io.github.vhula.scheduler.model.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 25.12.12
 * Time: 21:31
 *
 */
public class VectorTopology {

    private ArrayList<Processor> processors = new ArrayList<Processor>();

    private ArrayList<Channel> channels = new ArrayList<Channel>();

    public HashMap<Node, Processor> pairs = new HashMap<Node, Processor>();

    public VectorTopology(int amountOfProcessors) {
        for (int i = 0; i < amountOfProcessors; i++) {
            Processor p = new Processor(i, this);
            processors.add(p);
            if (i != 0) {
                Channel c = new Channel(processors.get(i - 1), p, this);
                channels.add(c);
            }
        }
    }

    public void addPair(Node node, Processor processor) {
        pairs.remove(node);
        pairs.put(node, processor);
    }

    public boolean hasChannel(Processor from, Processor to) {
        for (Channel c : channels) {
            if (c.getSource().equals(from)) {
                if (c.getDestination().equals(to)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Processor getDestination(Processor src) {
        for (Channel c : channels) {
            if (c.getSource().equals(src)) {
                return c.getDestination();
            }
        }
        return null;
    }

    public Processor getSource(Processor dst) {
        for (Channel c : channels) {
            if (c.getDestination().equals(dst)) {
                return c.getSource();
            }
        }
        return null;
    }

    public Processor getProcessor(int number) {
        for (Processor p : processors) {
            if (p.getNumber() == number) {
                return p;
            }
        }
        return null;
    }

    public Processor getProcessor(Node node) {
        for (Processor p : processors) {
            if (p.hasTask(node)) {
                return p;
            }
        }
        return null;
    }

    public Processor getProcessor2(Node node) {
        return pairs.get(node);
    }

    public ArrayList<Processor> requiredProcessors(Node node) {
        ArrayList<Processor> res = new ArrayList<Processor>();
        for (Processor p : processors) {
            for (Node n : node.getParents()) {
                if (p.hasTask(n)) {
                    res.add(p);
                }
            }
        }
        return res;
    }

    public Processor getRightmost(Node node) {
        ArrayList<Processor> ps = requiredProcessors(node);
        Processor p = null;
        int maxNum = -1;
        for (Processor pr : ps) {
            if (pr.getNumber() > maxNum) {
                maxNum = pr.getNumber();
                p = pr;
            }
        }
        return p;
    }

    public boolean isReady(Node node) {
        ArrayList<Node> parents = new ArrayList<Node>(node.getParents());
        ArrayList<Processor> ps = requiredProcessors(node);
        for (Processor pr :ps) {
            ArrayList<Node> forRemove = new ArrayList<Node>();
            for (int i = 0; i < parents.size(); i++) {
                Node n = parents.get(i);
//                if (pr.hasTask(n) && (pr.getTask() == null || !pr.getTask().equals(n))) {
//                    forRemove.add(n);
//                }
                if (pr.hasTask(n) && !pr.hasTaskInQueue(n)) {
                    forRemove.add(n);
                }
            }
            parents.removeAll(forRemove);
        }
        return parents.size() == 0;
    }

    public void step() {
        for (Processor p : processors) {
            p.step();
        }
        for (int i = channels.size() - 1; i >= 0; i--) {
            Channel c = channels.get(i);
            c.step();
        }
    }

    public ArrayList<Processor> getProcessors() {
        return processors;
    }

    public Channel getChannel(Processor start) {
        for (Channel c : channels) {
            if (c.getSource().equals(start)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public int size() {
        return processors.size();
    }

    public boolean finished() {
        for (Processor p : processors) {
            if (!p.available()) {
                return false;
            }
        }
        for (Channel c : channels) {
            if (!c.available()) {
                return false;
            }
        }
        return true;
    }

    public void trim() {
        ArrayList<Object> forRemove = new ArrayList<Object>();
        for (Processor p : processors) {
            boolean flag = true;
            for (int i = 0; i < p.getLife().size(); i++) {
                if (p.getLife().get(i) != -1) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                for (Channel c : channels) {
                    if (c.getSource().equals(p) || c.getDestination().equals(p)) {
                        forRemove.add(c);
                    }
                }
                forRemove.add(p);
            }
        }
        processors.removeAll(forRemove);
        channels.removeAll(forRemove);
    }

}
