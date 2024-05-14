package io.github.vhula.scheduler.controller;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 24.12.12
 * Time: 23:58
 *
 */
public class ShowConnectionMatrixCommand extends Command {

    public ShowConnectionMatrixCommand(Controller controller) {
        super(controller);
        putValue(NAME, "Show connection matrix");
        putValue(SHORT_DESCRIPTION, "Shows connection matrix");
        putValue(SMALL_ICON, new ImageIcon(this.getClass().getClassLoader().getResource("img/show_matrix.png")));
    }

    @Override
    public void doCommand() {
        controller.showConnectionMatrix();
    }
}
