package io.github.vhula.scheduler.model.topology;

import io.github.vhula.scheduler.model.graph.Connection;
import io.github.vhula.scheduler.model.graph.Node;
import io.github.vhula.scheduler.view.graph.GraphView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 25.12.12
 * Time: 22:12
 *
 */
public class Scheduler {

    private GraphView graph = null;

    private VectorTopology vectorTopology = null;

    private HashMap<Node, Integer> priorities = new HashMap<Node, Integer>();

    private HashMap<Node, Integer> layers = new HashMap<Node, Integer>();

    private ArrayList<Node> queue = new ArrayList<Node>();

    public Scheduler(GraphView graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null!");
        }
        this.graph = graph;
    }

    public void schedule() {
        createQueue();
        createTopology();
        makeSchedule();
    }

    private void makeSchedule() {
        int k = 1;
        int wMin = 0;
        boolean flag;
        boolean stepping = false;
        ArrayList<Node> layerNodes = getLayer(k);
        while(layerNodes.size() > 0) {
            flag = true;
            for (int i = layerNodes.size() - 1; i >= 0; i--) {
                Node n = layerNodes.get(i);
                Processor p = getBest(n);
                if (n.getNumber() == 15) {
                    p = vectorTopology.getProcessor(2);
                }
                if (p != null && vectorTopology.getProcessor(n) == null && vectorTopology.isReady(n)) {
                    Transfer t = new Transfer(vectorTopology);
                    ArrayList<Node> parents = n.getParents();
                    ArrayList<Processor> processors = new ArrayList<Processor>();
                    for (int j = 0; j < parents.size(); j++) {
                        processors.add(vectorTopology.getProcessor(parents.get(j)));
                    }
                    ArrayList<Connection> allCon = graph.getConnections();
                    int cpn = p.getNumber();
                    ArrayList<Node> used = new ArrayList<Node>();
                    while (processors.size() > 0) {
                        Processor bestp = null;
                        int bpn = Integer.MAX_VALUE;
                        for (Processor pp : processors) {
                            if (pp.getNumber() - cpn < bpn) {
                                bpn = pp.getNumber();
                                bestp = pp;
                            }
                        }
                        Node par = null;
                        for (Node temp : parents) {
                            if (vectorTopology.getProcessor(temp).equals(bestp)) {
                                if (used.contains(temp)) {
                                    continue;
                                }
                                par = temp;
                                used.add(par);
                                break;
                            }
                        }
                        Connection conn = null;
                        for (Connection temp : allCon) {
                            if (temp.getEnd().equals(n)) {
                                if (temp.getStart().equals(par)) {
                                    conn = temp;
                                    break;
                                }
                            }
                        }
                        Channel prec = null;
                        if (p.getNumber() > bestp.getNumber()) {
                            for (int m = bpn; m < cpn; m++) {
                                Processor sp = vectorTopology.getProcessor(m);
                                Channel c = vectorTopology.getChannel(sp);
                                if (prec == null) {
                                    c.stepping = true;
                                }
                                c.send(conn);
                                t.addTransfer(conn);
                                prec = c;
                            }
                        } else {
                            for (int m = bpn - 1; m >= cpn; m--) {
                                Processor sp = vectorTopology.getProcessor(m);
                                Channel c = vectorTopology.getChannel(sp);
                                if (prec == null) {
                                    c.stepping = true;
                                }
                                c.send(conn);
                                t.addTransfer(conn);
                                prec = c;
                            }
                        }
                        processors.remove(bestp);
                    }
                    p.execute(n, t);
                    vectorTopology.addPair(n, p);
                    p.ready.put(n, Boolean.TRUE);
                    flag = true & flag;
                } else {
                    if (vectorTopology.getProcessor(n) != null) {
                        continue;
                    }
                    if (layerNodes.get(i).getNumber() == 8) {
                        System.out.println();
                    }
                    ArrayList<Node> parents = layerNodes.get(i).getParents();
                    ArrayList<Processor> processors = new ArrayList<Processor>();
                    for (int j = 0; j < parents.size(); j++) {
                        Processor pp = vectorTopology.getProcessor(parents.get(j));
                        if (pp.available() ||
                                pp.hasFinishedTask(parents.get(j)) &&
                                !pp.equals(vectorTopology.getProcessor(layerNodes.get(i)))) {
                            processors.add(vectorTopology.getProcessor(parents.get(j)));
                        }
                    }
                    ArrayList<Connection> allCon = graph.getConnections();
                    Node par = null;
                    ArrayList<Node> used = new ArrayList<Node>();
                    for (Processor prr : processors) {
                        int cpn = prr.getNumber();
                        int bpn = p.getNumber();
                        for (Node temp : parents) {
                            if (vectorTopology.getProcessor(temp).equals(prr)) {
                                if (used.contains(temp)) {
                                    continue;
                                }
                                par = temp;
                                used.add(par);
                                break;
                            }
                        }
                        Connection conn = null;
                        for (Connection temp : allCon) {
                            if (temp.getEnd().equals(n)) {
                                if (temp.getStart().equals(par)) {
                                    conn = temp;
                                    break;
                                }
                            }
                        }
                        vectorTopology.addPair(n, p);
                        Channel prec = null;
                        if (bpn < cpn) {
                            for (int m = bpn; m < cpn; m++) {
                                Processor sp = vectorTopology.getProcessor(m);
                                Channel c = vectorTopology.getChannel(sp);
                                if (prec == null) {
                                    c.stepping = true;
                                }
                                c.send(conn);
                                prec = c;
                            }
                        } else {
                            for (int m = bpn - 1; m >= cpn; m--) {
                                Processor sp = vectorTopology.getProcessor(m);
                                Channel c = vectorTopology.getChannel(sp);
                                if (prec == null) {
                                    c.stepping = true;
                                }
                                c.send(conn);
                                prec = c;
                            }
                        }
                    }
                    if (vectorTopology.getProcessor(n) == null) {
                        flag = false;
                    }
                    continue;
                }
            }
            if (stepping) {
                vectorTopology.step();
            }
            if (flag) {
                stepping = true;
                k++;
            }
            layerNodes = getLayer(k);
        }
        System.out.println();
        while (!vectorTopology.finished()) {
            vectorTopology.step();
        }
        System.out.println();
        vectorTopology.trim();
    }

    protected Processor getBest(Node node) {
        int minCost = Integer.MAX_VALUE;
        ArrayList<Processor> processors = vectorTopology.getProcessors();
        Processor best = processors.get(0);
        ArrayList<Node> parents = node.getParents();
        if (node.getNumber() == 15) {
            System.out.println();
        }
        for (Processor p : processors) {
            if (best != null && p.getNumber() < best.getNumber()) {
                continue;
            } else {
                int cost = 0;
                cost += node.getWeight();
                cost += p.whenFree();
                for (int i = 0; i < parents.size(); i++) {
                    Processor sp = vectorTopology.getProcessor(parents.get(i));
                    cost += Math.abs(p.getNumber() - sp.getNumber()) * graph.getConnectionMatrix().getWeight(parents.get(i).getNumber(),
                            node.getNumber());
                    if (!sp.finished(parents.get(i), true) && !sp.equals(best)) {
                        if (sp.getTask().equals(parents.get(i))) {
                            cost += sp.getStepsCount();
                        } else {
                            cost += parents.get(i).getWeight();
                        }
                    }
                }
                if (cost < minCost) {
                    minCost = cost;
                    best = p;
                }
            }
        }
        if (minCost == node.getWeight()) {
            for (Processor p : processors) {
                if (p.available()) {
                    return p;
                }
            }
        }
        return best;
    }

    protected ArrayList<Node> getLayer(int layer) {
        ArrayList<Node> res = new ArrayList<Node>();
        for (int i = 0; i < queue.size(); i++) {
            Node n = queue.get(i);
            if (layers.get(n) == layer) {
                res.add(n);
            }
        }
        return res;
    }

    private void createQueue() {
        Node[] nodes = new Node[graph.getNodes().size()];
        int[] prs = new int[graph.getNodes().size()];
        int i = 0;
        for (Node n : graph.getNodes()) {
            int pr = countPriority(n);
            countLayer(n);
            paths = new ArrayList<ArrayList<Node>>();
            priorities.put(n, pr);
            nodes[i] = n;
            prs[i] = pr;
            i++;
        }
        sortNodes(nodes, prs);
        for (Node n : nodes) {
            queue.add(n);
        }
    }

    private void sortNodes(Node[] nodes, int[] prs) {
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int j = 0; j < prs.length - 1; j++) {
                if (prs[j] > prs[j + 1]) {
                    Node temp = nodes[j];
                    nodes[j] = nodes[j + 1];
                    nodes[j + 1] = temp;
                    int buf = prs[j];
                    prs[j] = prs[j + 1];
                    prs[j + 1] = buf;
                    flag = true;
                }
            }
        }
    }

    private void countLayer(Node n) {
        if (paths.size() > 0) {
            int max = paths.get(0).size();
            for (int j = 1; j < paths.size(); j++) {
                if (max < paths.get(j).size()) {
                    max = paths.get(j).size();
                }
            }
            layers.put(n, max);
        }
    }

    protected int countPriority(Node node) {
        pathsToRoot(node, new ArrayList<Node>());
        ArrayList<Connection> connections = graph.getConnections();
        int maxWeight = 0;
        for (int i = 0; i < paths.size(); i++) {
            int w = 0;
            for (int j = 0; j < paths.get(i).size() - 1; j++) {
                for (int k = 0; k < connections.size(); k++) {
                    if (connections.get(k).getEnd().equals(paths.get(i).get(j))) {
                        if (connections.get(k).getStart().equals(paths.get(i).get(j + 1))) {
                            w += connections.get(k).getWeight();
                            w += paths.get(i).get(j).getWeight();
                            break;
                        }
                    }
                }
            }
            w += paths.get(i).get(paths.get(i).size() - 1).getWeight();
//            w -= paths.get(i).get(0).getWeight();

            if (w > maxWeight) {
                maxWeight = w;
            }
        }
        return maxWeight;
    }

    ArrayList<ArrayList<Node>> paths = new ArrayList<ArrayList<Node>>();

    private void pathsToRoot(Node node, ArrayList<Node> res) {
        res.add(node);
        if (node.getParents().size() > 0) {
            for (int i = 0; i < node.getParents().size(); i++) {
                pathsToRoot(node.getParents().get(i), new ArrayList<Node>(res));
            }
        } else {
            paths.add(res);
        }
    }

    protected int max(int[] array) {
        int m = array[0];
        for (int i = 0; i < array.length; i++) {
            if (m < array[i]) {
                m = array[i];
            }
        }
        return m;
    }

    public VectorTopology getTopology() {
        return vectorTopology;
    }

    protected void createTopology() {
        vectorTopology = new VectorTopology(countGraphWidth());
    }

    protected int countGraphWidth() {
        int res = 0;
        int k = 1;
        ArrayList<Node> layer = getLayer(k);
        while (layer.size() > 0) {
            int temp = 0;
            for (Node n : queue) {
                if (layers.get(n) == k) {
                    temp++;
                }
            }
            if (temp > res) {
                res = temp;
            }
            k++;
            layer = getLayer(k);
        }
        return res;
    }

}
