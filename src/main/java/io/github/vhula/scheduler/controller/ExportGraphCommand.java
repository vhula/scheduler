package io.github.vhula.scheduler.controller;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 19.12.12
 * Time: 9:51
 * To change this template use File | Settings | File Templates.
 */
public class ExportGraphCommand extends Command {

    public ExportGraphCommand(Controller controller) {
        super(controller);
        putValue(NAME, "Export");
        putValue(SMALL_ICON, new ImageIcon(this.getClass().getClassLoader().getResource("img/export.png")));
        putValue(SHORT_DESCRIPTION, "Export to PNG");
    }

    @Override
    public void doCommand() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return "";
            }

            @Override
            public boolean accept(final File file) {
                return true;
            }
        });
        int choice = jfc.showSaveDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().getPath() != null) {
                controller.export(jfc.getSelectedFile().getPath());
            }
        }
    }
}
