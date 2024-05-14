package io.github.vhula.scheduler.view.graph;

import io.github.vhula.scheduler.model.graph.Node;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 16.12.12
 * Time: 22:40
 * Class which represents view of the node.
 */
public class NodeView extends Ellipse2D.Double implements Serializable {

    private Node node = null;

    protected int x = 0;

    protected int y = 0;

    protected static int radius = 25;

    public Color color = null;

    /**
     * Creates instance of the node view.
     * Throws IllegalArgumentException if node is null.
     * @param node node in the graph.
     * @see IllegalArgumentException
     * @see Node
     * @see io.github.vhula.scheduler.model.graph.Graph
     */
    public NodeView(Node node, int x, int y) {
        super((double)x, (double)y, radius * 2, radius * 2);
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null!");
        }
        setX(x);
        setY(y);
        this.node = node;
    }

    /**
     * Draws node on a canvas.
     * @param g2 graphics object of the canvas.
     */
    public void draw(Graphics2D g2) {
        Color temp = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        if (color == null) {
            g2.setColor(new Color(209, 209, 245));
        } else {
            g2.setColor(color);
        }
        g2.setStroke(new BasicStroke(2.4f));
        g2.fillOval(x, y, radius * 2, radius * 2);
        g2.setColor(Color.BLACK);
        g2.drawOval(x, y, radius * 2, radius * 2);
        g2.drawLine(x, y + radius, x + radius * 2, y + radius);
        g2.drawString("" + node.getNumber(), x + radius - 5, y + radius / 2 + 5);
        g2.drawString("[" + node.getWeight() + "]", x + radius - 13, y + radius * 5 / 3);
        g2.setColor(temp);
        g2.setStroke(oldStroke);
        color = null;
    }

    public void setRadius(int radius) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius cannot be less than 1");
        }
        this.radius = radius;
    }

    public void setX(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("X cannot be less than zero!");
        }
        int dx = x / 50;
        x = 50 * dx;
        this.x = x;
        super.x = x;
    }

    public void setY(int y) {
        if (y < 0) {
            throw new IllegalArgumentException("Y cannot be less than zero!");
        }
        int dy = y / 50;
        y = dy * 50;
        this.y = y;
        super.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public Node getNode() {
        return node;
    }

}
