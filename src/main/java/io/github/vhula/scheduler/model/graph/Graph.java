package io.github.vhula.scheduler.model.graph;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 16.12.12
 * Time: 20:35
 * Class which represents graph.
 */
public class Graph implements Serializable {

    private ArrayList<Node> nodes = new ArrayList<Node>();

    private ArrayList<Connection> connections = new ArrayList<Connection>();

    public Graph() {
    }

    /**
     * Method to add node to the graph.
     * Throws IllegalArgumentException if node eqaul to null, or graph already contains this node.
     * @param node node to add.
     * @see IllegalArgumentException
     */
    public void addNode(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Cannot add node == null.");
        }
        if (nodes.contains(node)) {
            throw new IllegalArgumentException("Graph already contains this node!");
        }
        nodes.add(node);
    }

    /**
     * Method to remove node from the graph.
     * Throws IllegalArgumentException if node equal to null, or node doesn't consist in the graph.
     * @param node node to remove.
     * @see IllegalArgumentException
     */
    public void removeNode(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Node to remove cannot be null.");
        }
        if (!nodes.contains(node)) {
            throw new IllegalArgumentException("Graph doesn't contain this node!");
        }
        nodes.remove(node);
        for (Node n : nodes) {
            if (n.hasChildern(node)) {
                n.removeChild(node);
            }
            if (n.hasParent(node)) {
                n.removeParent(node);
            }
        }
    }

    /**
     * Method for adding connection to the graph.
     * Throws IllegalArgumentException if the graph contains this connection, or
     * this connection is null.
     * @param connection connection to add.
     * @see IllegalArgumentException
     */
    public void addConnection(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null!");
        }
        if (connections.contains(connection)) {
            throw new IllegalArgumentException("Graph already contains this connection!");
        }
        if (isExistConnection(connection)) {
            throw new IllegalArgumentException("Graph already contains this connection! But not this instance!");
        }
        connections.add(connection);
        Node parent = connection.getStart();
        Node child = connection.getEnd();
        parent.addChild(child);
        child.addParent(parent);
    }

    /**
     * Removes connection from the graph.
     * Throws IllegalArgumentException if connection is null, or the graph doesn't contain connection.
     * @param connection connection to remove.
     * @see IllegalArgumentException
     */
    public void removeConnection(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null!");
        }
        if (!connections.contains(connection)) {
            throw new IllegalArgumentException("Graph doesn't contain this connection!");
        }
        connections.remove(connection);
        Node parent = connection.getStart();
        Node child = connection.getEnd();
        parent.removeChild(child);
        child.removeParent(parent);
    }

    /**
     * Method for checking if the graph already contains this connection.
     * @param connection connection to check.
     * @return true if the graph contains this connection.
     */
    protected boolean isExistConnection(Connection connection) {
        for (Connection c : connections) {
            if (connection.getStart().equals(c.getStart())) {
                if (connection.getEnd().equals(c.getEnd())) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

}
