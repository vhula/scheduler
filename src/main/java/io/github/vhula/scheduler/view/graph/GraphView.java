package io.github.vhula.scheduler.view.graph;

import io.github.vhula.scheduler.model.graph.Connection;
import io.github.vhula.scheduler.model.graph.ConnectionMatrix;
import io.github.vhula.scheduler.model.graph.Graph;
import io.github.vhula.scheduler.model.graph.Node;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 18.12.12
 * Time: 14:07
 * Class which represents view of the graph.
 */
public class GraphView extends Observable implements Serializable {

    protected ArrayList<NodeView> nodeViews = new ArrayList<NodeView>();

    protected ArrayList<ConnectionView> connectionViews = new ArrayList<ConnectionView>();

    protected transient ArrayList<Observer> observers = new ArrayList<Observer>();

    protected ConnectionMatrix connectionMatrix = null;

    protected Graph graph = null;

    public GraphView(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null!");
        }
        this.graph = graph;
        connectionMatrix = new ConnectionMatrix(this);
        connectionMatrix.rebuild(this);
    }

    /**
     * Adds node view in the graph view.
     * Throws IllegalArgumentException if node view already exist in the graph view, or node view is null.
     * @param nodeView node view in the graph.
     * @see IllegalArgumentException
     */
    public void addNode(NodeView nodeView) {
        if (nodeView == null) {
            throw new IllegalArgumentException("Node view cannot be null!");
        }
        if (nodeViews.contains(nodeView)) {
            throw new IllegalArgumentException("Graph view already contains this node!");
        }
        nodeViews.add(nodeView);
        graph.addNode(nodeView.getNode());
        connectionMatrix.rebuild(this);
        notifyObservers();
    }

    /**
     * Removes node view from the graph view.
     * Throws IllegalArgumentException if node view doesn't exist in the graph, or node view is null.
     * @param nodeView node view from the graph.
     * @see IllegalArgumentException
     */
    public void removeNode(NodeView nodeView) {
        if (nodeView == null) {
            throw new IllegalArgumentException("Node view cannot be null!");
        }
        if (!nodeViews.contains(nodeView)) {
            throw new IllegalArgumentException("Graph view doesn't contain this node!");
        }
        nodeViews.remove(nodeView);
        graph.removeNode(nodeView.getNode());
        connectionMatrix.rebuild(this);
        notifyObservers();
    }

    /**
     * Adds connection view to the graph view.
     * Throws IllegalArgumentException if the graph view already contains connection view.
     * @param connectionView connection view in the graph view.
     * @see IllegalArgumentException
     */
    public void addConnection(ConnectionView connectionView) {
        if (connectionView == null) {
            throw new IllegalArgumentException("Connection view cannot be null!");
        }
        if (connectionViews.contains(connectionView)) {
            throw new IllegalArgumentException("Graph view already contains this connection!");
        }
        if (isExistConnection(connectionView)) {
            throw new IllegalArgumentException("Graph view already contains this connection, but not this instance!");
        }
        connectionViews.add(connectionView);
        graph.addConnection(connectionView.getConnection());
        connectionMatrix.rebuild(this);
        notifyObservers();
    }

    /**
     * Removes connection view from the graph view.
     * Throws IllegalArgumentException if connection view is null, or the graph view doesn't contain this<br>
     * connection view.
     * @param connectionView connection view from the graph.
     * @see IllegalArgumentException
     */
    public void removeConnection(ConnectionView connectionView) {
        if (connectionView == null) {
            throw new IllegalArgumentException("Connection view cannot be null!");
        }
        if (!connectionViews.contains(connectionView)) {
            throw new IllegalArgumentException("Graph view doesn't contain this connection!");
        }
        connectionViews.remove(connectionView);
        graph.removeConnection(connectionView.getConnection());
        connectionMatrix.rebuild(this);
        notifyObservers();
    }

    public ConnectionView connectedWith(NodeView nodeView) {
        for (ConnectionView cv : connectionViews) {
            if (cv.getStartNode().equals(nodeView)) {
                return cv;
            }
            if (cv.getEndNode().equals(nodeView)) {
                return cv;
            }
        }
        return null;
    }

    public void update() {
        connectionMatrix.rebuild(this);
    }

    /**
     * Method for checking if the graph view already contains this connection view.
     * @param connection connection to check.
     * @return true if the graph view contains this connection view.
     */
    protected boolean isExistConnection(ConnectionView connection) {
        for (ConnectionView c : connectionViews) {
            if (connection.getStartNode().equals(c.getStartNode())) {
                if (connection.getEndNode().equals(c.getEndNode())) {
                    return true;
                }
            }
        }
        return false;
    }

    public int nodesAmount() {
        return nodeViews.size();
    }

    public ArrayList<NodeView> getNodeViews() {
        return nodeViews;
    }

    public ArrayList<ConnectionView> getConnectionViews() {
        return connectionViews;
    }

    public ArrayList<Observer> getObservers() {
        return observers;
    }

    public ConnectionMatrix getConnectionMatrix() {
        return connectionMatrix;
    }

    public ArrayList<Node> getNodes() {
        return graph.getNodes();
    }

    public ArrayList<Connection> getConnections() {
        return graph.getConnections();
    }

    public Graph getGraph() {
        return graph;
    }

    /**
     * Method which checks if node contains point.
     * @param x
     * @param y
     * @return instance of node contains this point, or null.
     */
    public NodeView containsNode(int x, int y) {
        for (NodeView nv : nodeViews) {
            if (nv.contains(x, y)) {
                return nv;
            }
        }
        return null;
    }

    public void newGraph() {
        graph = new Graph();
        connectionViews = new ArrayList<ConnectionView>();
        nodeViews = new ArrayList<NodeView>();
    }

    /**
     * Method which checks if node contains point.
     * @param x
     * @param y
     * @return instance of node contains this point, or null.
     */
    public ConnectionView containsConnection(int x, int y) {
        for (ConnectionView cv : connectionViews) {
            if (cv.containsPoint(x, y)) {
                return cv;
            }
        }
        return null;
    }

    /**
     * Drawing the graph view in the canvas.
     * @param g graphics parameter of the canvas.
     */
    public void draw(Graphics g) {
        for (NodeView n : nodeViews) {
            n.draw((Graphics2D) g);
        }
        for (ConnectionView c : connectionViews) {
            c.draw((Graphics2D) g);
        }
    }

    public void make(GraphView graphView) {
        this.graph = graphView.graph;
        this.nodeViews = graphView.nodeViews;
        this.connectionViews = graphView.connectionViews;
    }

    public void export(String filename) {
        for (Observer observer : observers) {
            observer.update(this, filename);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null!");
        }
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this, null);
        }
    }
}
