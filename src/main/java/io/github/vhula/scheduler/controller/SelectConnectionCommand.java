package io.github.vhula.scheduler.controller;

import io.github.vhula.scheduler.model.ItemType;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 18.12.12
 * Time: 17:07
 *
 */
public class SelectConnectionCommand extends Command {

    public SelectConnectionCommand(Controller controller) {
        super(controller);
        putValue(NAME, "Connection");
        putValue(SMALL_ICON, new ImageIcon(this.getClass().getClassLoader().getResource("img/connection.png")));
        putValue(SHORT_DESCRIPTION, "Put Connection");
    }

    @Override
    public void doCommand() {
        controller.selectedItem = ItemType.CONNECTION;
    }
}
