package io.github.vhula.scheduler.controller;

import io.github.vhula.scheduler.model.ItemType;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 18.12.12
 * Time: 17:09
 *
 */
public class SelectArrowCommand extends Command {

    public SelectArrowCommand(Controller controller) {
        super(controller);
        putValue(NAME, "Arrow");
        putValue(SMALL_ICON, new ImageIcon(this.getClass().getClassLoader().getResource("img/arrow.png")));
        putValue(SHORT_DESCRIPTION, "Arrow Item");
    }

    @Override
    public void doCommand() {
        controller.selectedItem = ItemType.ARROW;
    }
}
