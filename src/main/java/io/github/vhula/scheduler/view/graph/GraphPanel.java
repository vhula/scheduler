package io.github.vhula.scheduler.view.graph;

import io.github.vhula.scheduler.controller.Controller;
import io.github.vhula.scheduler.model.ItemType;
import io.github.vhula.scheduler.model.graph.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 18.12.12
 * Time: 14:24
 * Class which contains graph view and draws it.
 * Extends JPanel.
 * @see JPanel
 */
public class GraphPanel extends JPanel {

    /**
     * Instance of the graph view for drawing.
     * @see GraphView
     */
    private GraphView graphView = null;

    private Controller controller = null;

    public MouseAdapter mouseAdapter = new MouseHandler();

    /**
     * Creates instance of the graph panel.
     * Throws IllegalArgumentException if graph view is null.
     * @param graphView instance of the GraphView class.
     * @see GraphView
     * @see IllegalArgumentException
     */
    public GraphPanel(GraphView graphView, Controller controller) {
        if (graphView == null) {
            throw new IllegalArgumentException("Graph view cannot be null!");
        }
        if (controller == null) {
            throw new IllegalArgumentException("Controller cannot be null!");
        }
        this.graphView = graphView;
        this.controller = controller;
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public int getImageWidth() {
        int max = 0;
        for (NodeView nv : graphView.getNodeViews()) {
            if ((nv.getX() + nv.getRadius() * 4) > max) {
                max = (int) (nv.getX() + nv.getRadius() * 4);
            }
        }
        return max;
    }

    public int getImageHeight() {
        int max = 0;
        for (NodeView nv : graphView.getNodeViews()) {
            if (nv.getY() > max) {
                max = (int) (nv.getY() + nv.getRadius() * 3);
            }
        }
        return max;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        drawField(g2);
        graphView.draw(g2);
        if (controller.selectedItem == ItemType.NODE &&
                ((MouseHandler) mouseAdapter).x > 0 &&
                ((MouseHandler) mouseAdapter).x < getWidth() &&
                ((MouseHandler) mouseAdapter).y > 0 &&
                ((MouseHandler) mouseAdapter).y < getHeight()) {
            Node n = new Node(1);
            NodeView nv = new NodeView(n, ((MouseHandler) mouseAdapter).x, ((MouseHandler) mouseAdapter).y);
            nv.color = new Color(167, 82, 241);
            nv.draw(g2);
            graphView.notifyObservers();
        }
        if (controller.selectedItem == ItemType.CONNECTION && ((MouseHandler) mouseAdapter).startNode != null) {
            drawArrow(g2);
        }
    }

    private void drawField(Graphics2D g2) {
        int w = getWidth();
        int h = getHeight();
        int step = 50;
        for (int i = step; i < w; i += step) {
            for (int j = step; j < h; j += step) {
                g2.fillOval(i, j, 3, 3);
            }
        }
    }

    private void drawArrow(Graphics2D g2) {
        AffineTransform tempAffine = g2.getTransform();
        NodeView startNode = ((MouseHandler) mouseAdapter).startNode;
        int x1 = startNode.x;
        int y1 = startNode.y;
        int x2 = ((MouseHandler) mouseAdapter).x;
        int y2 = ((MouseHandler) mouseAdapter).y;
        double dx = x2 - x1;
        double dy = y2 - y1;
        double m = Math.sqrt(
                dx * dx + dy * dy
        );
        int nx1 = (int) (startNode.x + startNode.getRadius() + startNode.getRadius() * (dx / m));
        int ny1 = (int) (startNode.y + startNode.getRadius() + startNode.getRadius() * (dy / m));
        double angle = Math.atan2((y2 - ny1), (x2 - nx1));
        int len = (int) Math.sqrt(
                (x2 - nx1) * (x2 - nx1) + (y2 - ny1) * (y2 - ny1)
        );
        AffineTransform at = AffineTransform.getTranslateInstance(nx1, ny1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g2.transform(at);
        g2.drawLine(0, 0, len, 0);
        g2.fillPolygon(new int[]{len + 2, len + 2 - 10, len + 2 - 10, len + 2},
                new int[]{0, -5, 5, 0}, 4);
        g2.setTransform(tempAffine);
    }

    public class MouseHandler extends MouseAdapter {

        public int x = 0;

        public int y = 0;

        public NodeView startNode = null;

        public NodeView selectedNode = null;

        @Override
        public void mouseClicked(MouseEvent event) {
            System.out.println("Clicked : " + event.getX() + " " + event.getY());
            switch (controller.selectedItem) {
                case NODE:
                    nodeClicked(event);
                    break;
                case CONNECTION:
                    connectionClicked(event);
                    arrowClicked(event);
                    break;
                case REMOVE:
                    removeClicked(event);
                    break;
                case ARROW:
                    arrowClicked(event);
                    break;
            }

        }

        class PopupMenu extends JPopupMenu {

            private NodeView nodeView;

            private JMenuItem setNumber = new JMenuItem("Change Number");
            private JMenuItem setWeight = new JMenuItem("Change Weight");

            public PopupMenu(final NodeView nodeView) {
                this.nodeView = nodeView;
                setWeight.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String input = JOptionPane.showInputDialog(null, "Input weight:", "Input", JOptionPane.QUESTION_MESSAGE);
                        int w = Integer.parseInt(input);
                        nodeView.getNode().setWeight(w);
                        graphView.notifyObservers();
                    }
                });
                setNumber.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String input = JOptionPane.showInputDialog(null, "Input number:", "Input", JOptionPane.QUESTION_MESSAGE);
                        int n = Integer.parseInt(input);
                        nodeView.getNode().setNumber(n);
                        graphView.notifyObservers();
                    }
                });
                add(setWeight);
                add(setNumber);
            }
        }

        protected void removeClicked(MouseEvent event) {
            startNode = null;
            NodeView nv = graphView.containsNode(event.getX(), event.getY());
            ConnectionView cv = graphView.containsConnection(event.getX(), event.getY());
            ConnectionView cv2 = graphView.connectedWith(nv);
            while (cv2 != null) {
                graphView.removeConnection(cv2);
                cv2 = graphView.connectedWith(nv);
            }
            if (nv != null) {
                graphView.removeNode(nv);
            }
            if (cv != null) {
                graphView.removeConnection(cv);
            }
        }

        protected void arrowClicked(MouseEvent event) {
            if (event.isMetaDown() || event.getClickCount() == 2) {
                NodeView nv = graphView.containsNode(event.getX(), event.getY());
                if (nv != null) {
                    if (event.isMetaDown()) {
                        PopupMenu popupMenu = new PopupMenu(nv);
                        popupMenu.show(event.getComponent(), event.getX(), event.getY());
                    } else {
                        String input = JOptionPane.showInputDialog(null, "Input weight:", "Input", JOptionPane.QUESTION_MESSAGE);
                        int w = Integer.parseInt(input);
                        nv.getNode().setWeight(w);
                        graphView.notifyObservers();
                    }
                } else {
                    ConnectionView cv = graphView.containsConnection(event.getX(), event.getY());
                    if (cv != null) {
                        String input = JOptionPane.showInputDialog(null, "Input weight:", "Input", JOptionPane.QUESTION_MESSAGE);
                        int w = Integer.parseInt(input);
                        cv.getConnection().setWeight(w);
                        graphView.notifyObservers();
                    }
                }
            }
        }

        protected void nodeClicked(MouseEvent event) {
            startNode = null;
            controller.addNode(event.getX(), event.getY());
        }

        protected void connectionClicked(MouseEvent event) {
            if (!event.isMetaDown()) {
                if (startNode == null) {
                    startNode = graphView.containsNode(event.getX(), event.getY());
                } else {
                    NodeView endNode = graphView.containsNode(event.getX(), event.getY());
                    if (endNode != null && !endNode.equals(startNode)) {
                        controller.addConnection(startNode, endNode);
                        startNode = null;
                    }
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent event) {
            x = event.getX();
            y = event.getY();
            NodeView nv = graphView.containsNode(event.getX(), event.getY());
            if (nv != null) {
                nv.color = new Color(167, 82, 241);
            } else {
                ConnectionView cv = graphView.containsConnection(event.getX(), event.getY());
                if (cv != null) {
                    cv.color = new Color(167, 82, 241);
                }
            }
            makeCursor();
            graphView.notifyObservers();
        }

        private void makeCursor() {
            switch (controller.selectedItem) {
                case NODE:
                    startNode = null;
                case CONNECTION:
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    break;
                case REMOVE:
                    startNode = null;
                    setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                    break;
                case ARROW:
                    startNode = null;
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    break;
            }
        }

        @Override
        public void mousePressed(MouseEvent event) {
            if (controller.selectedItem == ItemType.ARROW) {
                selectedNode = graphView.containsNode(event.getX(), event.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            if (controller.selectedItem == ItemType.ARROW) {
                selectedNode = null;
            }
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            if (selectedNode != null) {
                selectedNode.color = new Color(167, 82, 241);
                selectedNode.setX(event.getX());
                selectedNode.setY(event.getY());
                graphView.notifyObservers();
            }
        }
    }
}
