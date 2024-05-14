package io.github.vhula.scheduler.controller;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 18.12.12
 * Time: 20:41
 *
 */
public class SaveGraphCommand extends Command {

    public SaveGraphCommand(Controller controller) {
        super(controller);
        putValue(NAME, "Save...");
        putValue(SMALL_ICON, new ImageIcon(this.getClass().getClassLoader().getResource("img/save.png")));
        putValue(SHORT_DESCRIPTION, "Save Graph");
    }

    @Override
    public void doCommand() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return "*.graph";
            }

            @Override
            public boolean accept(final File file) {
                if (file.getName().endsWith(".graph")) {
                    return true;
                }
                if (file.isDirectory()) {
                    return true;
                }
                return false;
            }
        });
        int choice = jfc.showSaveDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().getPath() != null) {
                controller.save(jfc.getSelectedFile().getPath());
            }
        }
    }
}
