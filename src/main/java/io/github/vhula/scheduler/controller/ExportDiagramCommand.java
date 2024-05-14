package io.github.vhula.scheduler.controller;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 28.12.12
 * Time: 4:54
 *
 */
public class ExportDiagramCommand extends Command {

    public ExportDiagramCommand(Controller controller) {
        super(controller);
        putValue(NAME, "Export");
        putValue(SMALL_ICON, new ImageIcon(this.getClass().getClassLoader().getResource("img/export_diagram.png")));
        putValue(SHORT_DESCRIPTION, "Export diagram to PNG");
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
                controller.exportDiagram(jfc.getSelectedFile().getPath());
            }
        }
    }
}
