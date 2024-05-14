package io.github.vhula.scheduler.controller;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 27.12.12
 * Time: 12:20
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleCommand extends Command {

    public ScheduleCommand(Controller controller) {
        super(controller);
        putValue(NAME, "Schedule...");
        putValue(SHORT_DESCRIPTION, "Makes schedule");
        putValue(SMALL_ICON, new ImageIcon(this.getClass().getClassLoader().getResource("img/schedule.png")));
    }

    @Override
    public void doCommand() {
        controller.schedule();
    }
}
