package io.github.vhula.scheduler.controller;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 19.12.12
 * Time: 9:34
 *
 */
public class NewGraphCommand extends Command {

    public NewGraphCommand(Controller controller) {
        super(controller);
        putValue(NAME, "New");
        putValue(SMALL_ICON, new ImageIcon(this.getClass().getClassLoader().getResource("img/new.png")));
        putValue(SHORT_DESCRIPTION, "Create New Graph");
    }

    @Override
    public void doCommand() {
        controller.newGraph();
    }
}
