package io.github.vhula.scheduler.view;

import io.github.vhula.scheduler.view.graph.ConnectionTable;
import io.github.vhula.scheduler.view.graph.GraphView;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 25.12.12
 * Time: 0:01
 *
 */
public class ConnectionTableFrame extends JFrame {

    private final GraphView graphView;

    public ConnectionTableFrame(GraphView graphView) {
        super("Transition table");
        if (graphView == null) {
            throw new NullPointerException("Graph View cannot be null!");
        }
        this.graphView = graphView;
        setLayout(new BorderLayout());
        setSize(640, 480);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        init();
        pack();
    }

    private void init() {
        ConnectionTable connectionTable = new ConnectionTable(graphView.getConnectionMatrix());
        JScrollPane jsp = new JScrollPane();
        jsp.setViewportView(connectionTable);
        add(jsp, BorderLayout.CENTER);
        JLabel header = new JLabel("Transition table");
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD, 32));
        add(header, BorderLayout.NORTH);
        JTable table = new JTable(2, graphView.nodesAmount());
        table.setRowHeight(25);
        table.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        for (int i = 0; i < graphView.getConnectionMatrix().getNumbers().length; i++) {
            table.setValueAt(graphView.getConnectionMatrix().getNumbers()[i], 0, i);
            table.setValueAt(graphView.getNodeViews().get(i).getNode().getWeight() + "", 1, i);
        }
        JPanel panel = new JPanel(new BorderLayout());
        header = new JLabel("Weights of nodes");
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD, 32));
        panel.add(header, BorderLayout.NORTH);
        panel.add(table, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }
}
