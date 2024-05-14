package io.github.vhula.scheduler.view.graph;

import io.github.vhula.scheduler.model.graph.ConnectionMatrix;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 25.12.12
 * Time: 0:16
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionTable extends JTable {

    private ConnectionMatrix connectionMatrix = null;

    public ConnectionTable(ConnectionMatrix connectionMatrix) {
        super(connectionMatrix.getHeight() + 1, connectionMatrix.getWidth() + 1);
//        super(new Object[connectionMatrix.getHeight() + 1][connectionMatrix.getWidth() + 1], connectionMatrix.getNumbers());
        if (connectionMatrix == null) {
            throw new IllegalArgumentException("Connection matrix cannot be null!");
        }
        setTableHeader(new JTableHeader());
        this.connectionMatrix = connectionMatrix;
        setRowHeight(25);
        ListSelectionModel selectionModel = getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        fillTable();
    }

    private void fillTable() {
        TableModel model = getModel();
        for (int i = 0; i < connectionMatrix.getNumbers().length; i++) {
            model.setValueAt(connectionMatrix.getNumbers()[i], i + 1, 0);
        }
        for (int i = 0; i < connectionMatrix.getNumbers().length; i++) {
            model.setValueAt(connectionMatrix.getNumbers()[i], 0, i + 1);
        }
        int[][] matrix = connectionMatrix.getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                model.setValueAt("" + matrix[i][j], i + 1, j + 1);
                if (matrix[i][j] == 0) {
                    model.setValueAt("", i + 1, j + 1);
                }
            }
        }
    }

}
