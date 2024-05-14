package io.github.vhula.scheduler.model.graph;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 16.12.12
 * Time: 20:24
 * Class which represents node in the graph.
 */
public class Node implements Serializable {

    private int weight = 1;

    private int number = 0;

    private ArrayList<Node> parents = new ArrayList<Node>();

    private ArrayList<Node> children = new ArrayList<Node>();

    /**
     * Creates instance of Node class.
     * Throws IllegalArgumentException if weight less than 1, or number less than zero.
     * @param weight weight of node.
     * @param number number of the node.
     * @see IllegalArgumentException
     */
    public Node(int weight, int number) {
        setWeight(weight);
        setNumber(number);
    }

    /**
     * Creates instance of Node class.
     * Throws IllegalArgumentException if weight less than 1.
     * @param weight weight of node.
     * @see IllegalArgumentException
     */
    public Node(int weight) {
        setWeight(weight);
    }

    public int getWeight() {
        return weight;
    }

    public int getNumber() {
        return number;
    }

    public void setWeight(int weight) {
        checkWeight(weight);
        this.weight = weight;
    }

    protected void checkWeight(int weight) {
        if (weight < 1) {
            throw new IllegalArgumentException("Weight must be higher than 0.");
        }
    }

    public void setNumber(int number) {
        checkNumber(number);
        this.number = number;
    }

    protected void checkNumber(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Number cannot be less than zero.");
        }
    }

    public void addParent(Node parent) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent cannot be null!");
        }
        parents.add(parent);
    }

    public void removeParent(Node parent) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent cannot be null!");
        }
        parents.remove(parent);
    }

    public void addChild(Node child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null!");
        }
        children.add(child);
    }

    public void removeChild(Node child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null!");
        }
        children.remove(child);
    }

    public boolean hasParent(Node node) {
        return parents.contains(node);
    }

    public boolean hasChildern(Node node) {
        return children.contains(node);
    }

    public ArrayList<Node> getParents() {
        return parents;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }


}
