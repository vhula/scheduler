package io.github.vhula.scheduler.controller;

import io.github.vhula.scheduler.model.ItemType;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 18.12.12
 * Time: 17:04
 *
 */
public class SelectNodeCommand extends Command {

    public SelectNodeCommand(Controller controller) {
        super(controller);
        putValue(NAME, "Node");
        putValue(SMALL_ICON, new ImageIcon(this.getClass().getClassLoader().getResource("img/node.png")));
        putValue(SHORT_DESCRIPTION, "Put Node");
    }

    @Override
    public void doCommand() {
        controller.selectedItem = ItemType.NODE;
    }
}
