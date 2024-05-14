package io.github.vhula.scheduler.view.graph;

import io.github.vhula.scheduler.model.graph.Connection;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 16.12.12
 * Time: 22:53
 * Class which represents view of the connection in the graph.
 */
public class ConnectionView extends Line2D.Double implements Serializable {

    private final NodeView startNode;

    private final NodeView endNode;

    private final Connection connection;

    public Color color = null;

    /**
     * Creates instance of ConnectionView class.
     * Throws IllegalArgumentException if connection is null, or startPoint is null, or endPoint is null.
     * @param startNode start node on the canvas.
     * @param endNode end node on the canvas.
     * @see IllegalArgumentException
     * @see Connection
     * @see io.github.vhula.scheduler.model.graph.Graph
     * @see Point
     */
    public ConnectionView(NodeView startNode, NodeView endNode, Connection connection) {
        super();
        if (startNode == null) {
            throw new IllegalArgumentException("Start node cannot be null!");
        }
        if (endNode == null) {
            throw new IllegalArgumentException("End node cannot be null!");
        }
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null!");
        }
        this.startNode = startNode;
        this.endNode = endNode;
        this.connection = connection;
    }

    public NodeView getStartNode() {
        return startNode;
    }

    public NodeView getEndNode() {
        return endNode;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean containsPoint(int x, int y) {
        if (Line2D.ptLineDist(x1, y1, x2, y2, x, y) < 1.3 && y1 < y && y2 > y) {
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g2) {
        Font tempFont = g2.getFont();
        Color temp = g2.getColor();
        Stroke strokeTemp = g2.getStroke();
        AffineTransform tempAffine = g2.getTransform();
        g2.setColor(Color.BLACK);
        double dx = endNode.x - startNode.x;
        double dy = endNode.y - startNode.y;
        double m = Math.sqrt(
                dx * dx + dy * dy
        );
        int flag = -1;
        if (startNode.x > endNode.x || startNode.y > endNode.y) {
            flag = 1;
        }
        int nx1 = (int) (startNode.x + startNode.radius + startNode.radius * (dx / m));
        int ny1 = (int) (startNode.y + startNode.radius + startNode.radius * (dy / m));
        int nx2 = (int) (endNode.x + endNode.radius - endNode.radius * (dx / m));
        int ny2 = (int) (endNode.y + endNode.radius - endNode.radius * (dy / m));
        this.x1 = nx1;
        this.y1 = ny1;
        this.x2 = nx2;
        this.y2 = ny2;
        setLine(nx1, ny1, nx2, ny2);
        double angle = Math.atan2((ny2 - ny1), (nx2 - nx1));
        int len = (int) Math.sqrt(
                (nx2 - nx1) * (nx2 - nx1) + (ny2 - ny1) * (ny2 - ny1)
        );
        if (color != null) {
            g2.setColor(color);
        }
        AffineTransform at = AffineTransform.getTranslateInstance(nx1, ny1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g2.transform(at);
        float thickness = 1.9f;
        g2.setStroke(new BasicStroke((float) (thickness)));
        g2.drawLine(0, 0, len - len / 2 - 15, 0);
        g2.drawLine(len - len / 2 + 10, 0, len, 0);
        int ARR_SIZE = 10;
        g2.fillPolygon(new int[]{len + 2, len + 2 - ARR_SIZE, len + 2 - ARR_SIZE, len + 2},
                new int[]{0, -ARR_SIZE / 2, ARR_SIZE / 2, 0}, 4);
        g2.setColor(new Color(129, 32, 32));
        if (color != null) {
            g2.setColor(color);
        }
        g2.setStroke(new BasicStroke(4));
        g2.setTransform(tempAffine);
        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        if (nx1 > nx2 || ny1 > ny2) {
            flag = -flag;
        }
        double px = (nx1 + nx2) / 2;
        double py = (ny1 + ny2) / 2;
        g2.drawString("" + connection.getWeight(),(float) px - 5,(float) py);
        g2.setColor(temp);
        g2.setStroke(strokeTemp);
        g2.setFont(tempFont);
        color = null;
    }

}
