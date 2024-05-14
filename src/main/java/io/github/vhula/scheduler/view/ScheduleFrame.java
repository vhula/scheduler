package io.github.vhula.scheduler.view;

import io.github.vhula.scheduler.model.topology.Scheduler;
import io.github.vhula.scheduler.view.graph.SchedulePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 27.12.12
 * Time: 12:35
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleFrame extends JFrame {

    private Scheduler scheduler;

    private SchedulePanel schedulePanel = null;

    public ScheduleFrame(Scheduler scheduler) {
        super("Schedule");
        if (scheduler == null) {
            throw new IllegalArgumentException("Scheduler cannot be null!");
        }
        this.scheduler = scheduler;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(640 , 480);
        init();

    }

    private void init() {
        schedulePanel = new SchedulePanel(scheduler);
        JScrollPane jsp = new JScrollPane();
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setViewportView(schedulePanel);
        add(jsp, BorderLayout.CENTER);
    }

    public void exportDiagram(String filename) {
        try {
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            BufferedImage image = new BufferedImage(schedulePanel.getImageWidth(),
                    schedulePanel.getImageHeight(),
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            schedulePanel.paint(g);
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(null, "File is not exported!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
