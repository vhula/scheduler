package io.github.vhula.scheduler.model.graph;

import io.github.vhula.scheduler.view.graph.ConnectionView;
import io.github.vhula.scheduler.view.graph.GraphView;
import io.github.vhula.scheduler.view.graph.NodeView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 24.12.12
 * Time: 23:07
 *
 */
public class ConnectionMatrix implements Serializable {

    private int[][] matrix = new int[0][0];

    private int width = 0;

    private int height = 0;

    private int[] numbers = new int[0];

    private GraphView graphView = null;

    public ConnectionMatrix(GraphView graphView) {
        if (graphView == null) {
            throw new IllegalArgumentException("Graph view cannot be null!");
        }
        this.graphView = graphView;
    }

    private void setWidth(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("Width cannot be less than zero!");
        }
        this.width = width;
    }

    private void setHeight(int height) {
        if (height < 0) {
            throw new IllegalArgumentException("Width cannot be less than zero!");
        }
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getWeight(int src, int dst) {
        int i = indexOf(src);
        int j = indexOf(dst);
        return matrix[i][j];
    }

    public Integer[] getNumbers() {
        Integer[] nums = new Integer[numbers.length];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = new Integer(numbers[i]);
        }
        return nums;
    }

    public void rebuild(GraphView graphView) {
        if (graphView == null) {
            throw new IllegalArgumentException("Graph view cannot be null!");
        }
        this.graphView = graphView;
        int nodesAmount = graphView.nodesAmount();
        matrix = new int[nodesAmount][nodesAmount];
        numbers = new int[nodesAmount];
        setWidth(nodesAmount);
        setHeight(nodesAmount);
        buildNumbers();
        buildMatrix();
//        sortByNumber();
        printMatrix();
    }

    private void buildNumbers() {
        ArrayList<NodeView> nodeViews = graphView.getNodeViews();
        for (int i = 0; i < nodeViews.size(); i++) {
            Node n = nodeViews.get(i).getNode();
            numbers[i] = n.getNumber();
        }
    }

    public void buildMatrix() {
        ArrayList<ConnectionView> connectionViews = graphView.getConnectionViews();
        for (ConnectionView cv : connectionViews) {
            Connection c = cv.getConnection();
            int i = indexOf(c.getStart().getNumber());
            int j = indexOf(c.getEnd().getNumber());
            int w = c.getWeight();
            matrix[i][j] = w;
        }
    }

    public int indexOf(int number) {
        for (int i = 0; i < numbers.length; i++) {
            if (number == numbers[i]) {
                return i;
            }
        }
        return -1;
    }

    public void sortByNumber() {
        //TODO works bad
        //doesn't change columns
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < numbers.length - 1; i++) {
                if (numbers[i] > numbers[i + 1]) {
                    int buf = numbers[i];
                    numbers[i] = numbers[i + 1];
                    numbers[i + 1] = buf;
                    int temp[] = matrix[i];
                    matrix[i] = matrix[i + 1];
                    matrix[i + 1] = temp;
                    flag = true;
                }
            }
        }
    }

    public void printMatrix() {
        System.out.printf("%4d\t", -1);
        for (int i = 0; i < numbers.length; i++) {
            System.out.printf("%4d\t", numbers[i]);
        }
        System.out.println();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (j == 0) {
                    System.out.printf("%4d\t", numbers[i]);
                }
                System.out.printf("%4d\t", matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("----------------------------------------------------------------");
        System.out.println();
    }

}

