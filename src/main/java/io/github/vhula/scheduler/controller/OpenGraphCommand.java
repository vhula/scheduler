package io.github.vhula.scheduler.controller;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 18.12.12
 * Time: 21:41
 * To change this template use File | Settings | File Templates.
 */
public class OpenGraphCommand extends Command {

    public OpenGraphCommand(Controller controller) {
        super(controller);
        putValue(NAME, "Open");
        putValue(SMALL_ICON, new ImageIcon(this.getClass().getClassLoader().getResource("img/open.png")));
        putValue(SHORT_DESCRIPTION, "Open Graph");
    }

    @Override
    public void doCommand() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return ".graph";
            }

            @Override
            public boolean accept(File file) {
                if (file.getName().endsWith(".graph")) {
                    return true;
                }
                if (file.isDirectory()) {
                    return true;
                }
                return false;
            }
        });
        int choice = jfc.showOpenDialog(null);
        String filename = null;
        if (choice == JFileChooser.APPROVE_OPTION) {
            filename = jfc.getSelectedFile().getName();
            int n = filename.length() - "graph".length();
            String format = filename.substring(n, filename.length());
            if (format.equals("graph")) {
                controller.open(jfc.getSelectedFile().getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(null,
                        "Illegal file format."
                                + "\nPlease, try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                filename = null;
            }
        } else {
            filename = null;
        }
    }
}
