package io.github.vhula.scheduler.controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 18.12.12
 * Time: 16:01
 *
 */
public abstract class Command extends AbstractAction {

    protected Controller controller;

    public Command(Controller controller) {
        if (controller == null) {
            throw new IllegalArgumentException("Controller cannot be null!");
        }
        this.controller = controller;
    }

    public abstract void doCommand();

    @Override
    public void actionPerformed(ActionEvent e) {
        doCommand();
    }
}
