package io.github.vhula.scheduler.controller;

import io.github.vhula.scheduler.model.ItemType;
import io.github.vhula.scheduler.model.graph.Connection;
import io.github.vhula.scheduler.model.graph.Node;
import io.github.vhula.scheduler.model.topology.Scheduler;
import io.github.vhula.scheduler.view.ConnectionTableFrame;
import io.github.vhula.scheduler.view.ScheduleFrame;
import io.github.vhula.scheduler.view.graph.ConnectionView;
import io.github.vhula.scheduler.view.graph.GraphView;
import io.github.vhula.scheduler.view.graph.NodeView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Observable;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 18.12.12
 * Time: 16:02
 * Class which represents controller of the graph.
 */
public class Controller {

    private GraphView model = null;

    public ItemType selectedItem = ItemType.NONE;

    public ConnectionTableFrame connectionTableFrame = null;

    public ScheduleFrame scheduleFrame = null;

    /**
     * Creates new instance of controller.
     * Throws IllegalArgumentException if model is null.
     * @param model model of the graph.
     * @see IllegalArgumentException
     * @see Observable
     */
    public Controller(GraphView model) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null!");
        }
        this.model = model;
    }

    public void addNode(int x, int y) {
        Node node = new Node(1);
        node.setNumber(model.getNodeViews().size() + 1);
        NodeView nodeView = new NodeView(node, x, y);
        model.addNode(nodeView);
    }

    public void addConnection(NodeView start, NodeView end) {
        Connection c = new Connection(start.getNode(), end.getNode(), 1);
        ConnectionView cv = new ConnectionView(start, end, c);
        model.addConnection(cv);
    }

    public void newGraph() {
        model.newGraph();
    }

    public void showConnectionMatrix() {
        model.update();
        if (connectionTableFrame == null) {
            connectionTableFrame = new ConnectionTableFrame(model);
            connectionTableFrame.setVisible(true);
        } else {
            connectionTableFrame.dispose();
            connectionTableFrame = null;
            connectionTableFrame = new ConnectionTableFrame(model);
            connectionTableFrame.setVisible(true);
        }
    }

    public void save(String filename) {
        try {
            int n = filename.length() - ".graph".length();
            String format = filename.substring(n, filename.length());
            if (!format.equals(".graph")) {
                filename += ".graph";
            }
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(model);
            oos.flush();
            oos.close();
            System.out.println("Saved");
        } catch (IOException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "File is not saved!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void export(String filename, JPanel panel, int w, int h) {
        try {
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            BufferedImage image = new BufferedImage(w, h,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            panel.paint(g);
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "File is not exported!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void export(String filename) {
        model.export(filename);
    }

    public void exportDiagram(String filename) {
        if (scheduleFrame != null) {
            scheduleFrame.exportDiagram(filename);
        }
    }

    public void open(String filename) {
        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            GraphView temp = (GraphView) ois.readObject();
            model.make(temp);
            model.update();
            ois.close();
            model.notifyObservers();
        } catch (IOException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "File is corrupted!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "File is corrupted!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void schedule() {
        Scheduler s = new Scheduler(model);
        s.schedule();
        model.update();
        if (scheduleFrame == null) {
            scheduleFrame = new ScheduleFrame(s);
            scheduleFrame.setVisible(true);
        } else {
            scheduleFrame.dispose();
            scheduleFrame = new ScheduleFrame(s);
            scheduleFrame.setVisible(true);
        }
    }
}
