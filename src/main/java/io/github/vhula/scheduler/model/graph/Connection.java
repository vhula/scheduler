package io.github.vhula.scheduler.model.graph;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 16.12.12
 * Time: 20:26
 * Class which represents connection between nodes in oriented graph.
 */
public class Connection implements Serializable {

    private Node start = null;

    private Node end = null;

    private int weight = 1;

    public Connection(Node start, Node end, int weight) {
        this.start = start;
        this.end = end;
        setWeight(weight);
    }

    private void checkParameters(Node start, Node end, int weight) {
        checkWeight(weight);
        if (start == null) {
            throw new IllegalArgumentException("Start node cannot be null.");
        }
        if (end == null) {
            throw new IllegalArgumentException("End node cannot be null.");
        }
    }

    protected void checkWeight(int weight) {
        if (weight < 1) {
            throw new IllegalArgumentException("Weight must be higher than 0.");
        }
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        checkWeight(weight);
        this.weight = weight;
    }

}
