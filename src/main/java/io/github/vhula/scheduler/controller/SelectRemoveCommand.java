package io.github.vhula.scheduler.controller;

import io.github.vhula.scheduler.model.ItemType;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 18.12.12
 * Time: 17:20
 *
 */
public class SelectRemoveCommand extends Command {

    public SelectRemoveCommand(Controller controller) {
        super(controller);
        putValue(NAME, "Remove");
        putValue(SMALL_ICON, new ImageIcon(this.getClass().getClassLoader().getResource("img/remove.png")));
        putValue(SHORT_DESCRIPTION, "Remove Element");
    }

    @Override
    public void doCommand() {
        controller.selectedItem = ItemType.REMOVE;
    }
}
