package io.github.vhula.scheduler.view.graph;

import io.github.vhula.scheduler.model.topology.Channel;
import io.github.vhula.scheduler.model.topology.Processor;
import io.github.vhula.scheduler.model.topology.Scheduler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 27.12.12
 * Time: 12:38
 *
 */
public class SchedulePanel extends JPanel {

    private Scheduler scheduler = null;

    private int stepHeight = 40;

    private int stepWidth = 50;

    private int stepsCount = -1;

    public SchedulePanel(Scheduler scheduler) {
        if (scheduler == null) {
            throw new IllegalArgumentException("Scheduler cannot be null!");
        }
        this.scheduler = scheduler;
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        stepsCount = scheduler.getTopology().getProcessor(0).getLife().size();
        setSize(scheduler.getTopology().getProcessors().size() * stepWidth * 6, stepsCount * stepHeight * 2);
        setPreferredSize(new Dimension(scheduler.getTopology().getProcessors().size() * stepWidth * 6, stepsCount * stepHeight * 2));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.WHITE);
    }

    public int getImageWidth() {
        int w = stepWidth * scheduler.getTopology().getProcessors().size();
        w += stepWidth * 4.5 * scheduler.getTopology().getChannels().size();
        return w;
    }

    public int getImageHeight() {
        int h = stepHeight * (scheduler.getTopology().getProcessor(0).getLife().size() + 3);
        return h;
    }

    public void export() {

    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        int defW = 50;
        int defH = 80;
        int w = defW;
        int h = defH;
        g2.setColor(Color.BLACK);
        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        g2.drawString("", w + 10, h - 20);
        for (int i = 0; i <= stepsCount; i++) {
            g2.setColor(Color.MAGENTA);
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(w, h, getWidth(), h);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawString("" + i, w - 30, h + 5);
            h += stepHeight;
        }
        ArrayList<Processor> processors = scheduler.getTopology().getProcessors();
        w = defW;
        h = defH;
        for (int i = 0; i < processors.size(); i++) {
            Processor p = processors.get(i);
            g2.drawString("P" + p.getNumber(), w + stepWidth * (i + 1) - stepWidth / 2 + stepWidth / 2, h - 20);
            g2.setColor(Color.MAGENTA);
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(w + stepWidth * (i + 1), 0, w + stepWidth * (i + 1), getHeight());
            g2.drawLine(w + stepWidth * (i + 2), 0, w + stepWidth * (i + 2), getHeight());
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            ArrayList<Integer> life = p.getLife();
            for (int j = 0; j < life.size(); j++) {
                int number = -1;
                int size = 0;
                if (life.get(j) != -1) {
                    number = life.get(j);
                    size++;
                    for (int k = j + 1; k < life.size(); k++) {
                        if (life.get(k) == number) {
                            size++;
                        } else {
                            break;
                        }
                    }
                    g2.drawRect(w + stepWidth * (i + 1), h + j * stepHeight, stepWidth, size * stepHeight);
                    g2.drawString(""+number, w + stepWidth * (i + 1) + stepWidth / 4,
                            h + j * stepHeight + stepHeight / 2);
                    j = size + j - 1;
                }
            }
        }
        w += (processors.size()) * stepWidth;
        ArrayList<Channel> channels = scheduler.getTopology().getChannels();
        for (int i = 0; i < channels.size(); i++) {
            Channel c = channels.get(i);
            g2.drawString("P" + c.getSource().getNumber() + "-" + "P" + c.getDestination().getNumber(),
                    w + 2 * stepWidth * (i + 1) + stepWidth / 2, h - 20);
            g2.setColor(Color.MAGENTA);
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(w + 2 * stepWidth * (i + 1) + stepWidth / 4, 0,
                    w + 2 * stepWidth * (i + 1) + stepWidth / 4, getHeight());
            g2.drawLine(w + 2 * stepWidth * (i + 2) + stepWidth / 4, 0,
                    w + 2 * stepWidth * (i + 2) + stepWidth / 4, getHeight());
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            ArrayList<Integer> life = c.getLife();
            for (int j = 0; j < life.size(); j++) {
                if (life.get(j) != -1) {
                    g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
                    g2.drawString(c.log.get(j),
                            w + 2 * stepWidth * (i + 1) + stepWidth / 3,
                            h + j * stepHeight + stepHeight / 2);
                    g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
                }
            }
        }
    }
}
