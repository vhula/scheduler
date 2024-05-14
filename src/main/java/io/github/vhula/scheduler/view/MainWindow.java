package io.github.vhula.scheduler.view;


import io.github.vhula.scheduler.controller.Controller;
import io.github.vhula.scheduler.model.graph.Graph;
import io.github.vhula.scheduler.view.graph.GraphPanel;
import io.github.vhula.scheduler.view.graph.GraphView;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 16.12.12
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow extends JFrame implements Observer {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(
                            UIManager.
                                    getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedLookAndFeelException e1) {
                    e1.printStackTrace();
                }
                Graph graph = new Graph();
                GraphView graphView = new GraphView(graph);
                Controller controller = new Controller(graphView);
                new MainWindow(controller, graphView);
            }
        });
    }

    private Controller controller = null;

    private GraphView model = null;

    private ToolBar toolBar = null;

    private GraphPanel graphPanel = null;

    public MainWindow(Controller controller, GraphView model) {
        super("Graph");
        if (controller == null) {
            throw new IllegalArgumentException("Controller cannot be null!");
        }
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null!");
        }
        this.controller = controller;
        this.model = model;
        init();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 480);
        setVisible(true);
    }

    private void init() {
        model.addObserver(this);
        setLayout(new BorderLayout());
        toolBar = new ToolBar(controller);
        add(toolBar, BorderLayout.NORTH);
        graphPanel = new GraphPanel(model, controller);
        graphPanel.setLayout(null);
        graphPanel.setPreferredSize(new Dimension(1500, 1000));
        JScrollPane jsp = new JScrollPane();
        jsp.setViewportView(graphPanel);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(jsp, BorderLayout.CENTER);
    }

    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void update(Observable o, Object arg) {
        invalidate();
        validate();
        repaint();
        if (arg != null) {
            controller.export((String)arg, graphPanel, graphPanel.getImageWidth(), graphPanel.getImageHeight());
        }
    }
}
