package io.github.vhula.scheduler.model.topology;

import io.github.vhula.scheduler.model.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 25.12.12
 * Time: 20:54
 * Class which represents processor abstraction.
 */
public class Processor {

    private boolean free = true;

    private ArrayList<Integer> life = new ArrayList<Integer>();

    private ArrayList<Node> tasks = new ArrayList<Node>();

    private ArrayList<Node> queue = new ArrayList<Node>();

    private HashMap<Node, Transfer> transfers = new HashMap<Node, Transfer>();

    public HashMap<Node, Boolean> ready = new HashMap<Node, Boolean>();

    private int number = -1;

    public boolean finished = false;

    public Node last = null;

    /**
     * Current task.
     */
    private Node task = null;

    private int taskSteps = 0;

    private VectorTopology vectorTopology;

    public Processor(int number, VectorTopology vectorTopology) {
        if (number < 0) {
            throw new IllegalArgumentException("Are you serious!?");
        }
        this.number = number;
        this.vectorTopology = vectorTopology;
    }

    public void execute(Node task, Transfer transfer) {
        if (task == null) {
            throw new IllegalStateException("Task cannot be null!");
        }
        if (!tasks.contains(task))
            tasks.add(task);
        if (!queue.contains(task))
            queue.add(task);
        transfers.remove(task);
        transfers.put(task, transfer);
        if (this.task == null) {
            this.task = task;
            queue.remove(task);
            taskSteps = this.task.getWeight();
            last = task;
            occupy();
        }
    }

    public boolean finished(Node node, boolean flag) {
        if (node == null) {
            return true;
        }
        int number = last == null ? Integer.MAX_VALUE : last.getNumber();
        if (node != null && life.size() > 0 && number == node.getNumber() && !flag
                || queue.contains(node) || node.equals(task)) {
            if (number == node.getNumber()) {
                if (taskSteps == 0) {
                    last = null;
                    return false;
                }
            }
            return false;
        }
        if (flag) {
            if (node != null && life.size() > 0 && life.contains(node.getNumber())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean hasTask(Node task) {
        if (tasks.contains(task)) {
            return true;
        }
//        for (Node t : tasks) {
//            if (t.equals(task)) {
//                return true;
//            }
//        }
        return false;
    }

    public void step() {
        if (task != null && !transfers.get(task).finished()) {
            for (int i = 0; i < queue.size(); i++) {
                if (ready.get(queue.get(i)) && transfers.get(queue.get(i)).finished() && vectorTopology.isReady(queue.get(i))) {
                    Node temp = task;
                    task = queue.get(i);
                    queue.remove(task);
                    occupy();
                    taskSteps = task.getWeight();
                    queue.add(0, temp);
                    last = task;
                }
            }
        }
        finished = true;
        if (task != null && task.getNumber() == 4) {
            System.out.println();
        }
        for (Node n : queue) {
            if (ready.get(n))
                transfers.get(n).update();
        }
        if (task != null) {
            if (ready.get(task))
                transfers.get(task).update();
        }
        if (task == null || !transfers.get(task).finished()) {
            life.add(-1);
        } else {
            life.add(task.getNumber());
            taskSteps--;
            if (taskSteps == 0) {
                task = null;
                release();
                finished = false;
                if (queue.size() > 0) {
                    task = queue.remove(0);
                    taskSteps = task.getWeight();
                    last = task;
                    occupy();
                }
            }
        }
    }

    public Node getTask() {
        return task;
    }

    public int whenFree() {
        if (task != null) {
            int res = life.size() + taskSteps;
            for (Node n : queue) {
                res += n.getWeight();
                Transfer t = transfers.get(n);
                res += t != null ? t.time() : 0;
            }
            Transfer t = transfers.get(task);
            res += t != null ? t.time() : 0;
            return res;
        } else {
            return life.size();
        }
    }

    public boolean available() {
        return free;
    }

    public void occupy() {
        free = false;
    }

    public void release() {
        free = true;
    }

    public int getNumber() {
        return number;
    }

    public ArrayList<Integer> getLife() {
        return life;
    }

    public boolean hasTaskInQueue(Node node) {
        boolean flag = false;
        if (task != null) {
            flag = task.equals(node);
        }
        return queue.contains(node) || flag;
    }

    public boolean hasFinishedTask(Node node) {
        if (!queue.contains(node)) {
            if (!node.equals(task)) {
                if (tasks.contains(node)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasTask() {
        return tasks.contains(queue);
    }

    public int getStepsCount() {
        return taskSteps;
    }
}
