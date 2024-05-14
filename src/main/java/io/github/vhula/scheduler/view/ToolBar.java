package io.github.vhula.scheduler.view;

import io.github.vhula.scheduler.controller.*;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: vhula
 * Date: 18.12.12
 * Time: 15:39
 * Class which represent tool bar from the main window.
 * Extends JToolBar.
 * @see JToolBar
 */
public class ToolBar extends JToolBar {

    private JButton arrowButton = new JButton();
    private JButton nodeButton = new JButton();
    private JButton connectionButton = new JButton();
    private JButton removeButton = new JButton();
    private JButton newButton = new JButton();
    private JButton saveButton = new JButton();
    private JButton openButton = new JButton();
    private JButton exportButton = new JButton();
    private JButton showTableButton = new JButton();
    private JButton showScheduleButton = new JButton();
    private JButton exportDiagramButton = new JButton();

    public ToolBar(Controller controller) {
        initButtons(controller);
        addButtons();
    }

    protected void initButtons(Controller controller) {
        arrowButton.setAction(new SelectArrowCommand(controller));
        ImageIcon icon = (ImageIcon) arrowButton.getAction().getValue(Action.SMALL_ICON);
        if (!(icon.getIconWidth() < 1)) {
            arrowButton.setText("");
        }

        nodeButton.setAction(new SelectNodeCommand(controller));
        icon = (ImageIcon) nodeButton.getAction().getValue(Action.SMALL_ICON);
        if (!(icon.getIconWidth() < 1)) {
            nodeButton.setText("");
        }

        connectionButton.setAction(new SelectConnectionCommand(controller));
        icon = (ImageIcon) connectionButton.getAction().getValue(Action.SMALL_ICON);
        if (!(icon.getIconWidth() < 1)) {
            connectionButton.setText("");
        }

        removeButton.setAction(new SelectRemoveCommand(controller));
        icon = (ImageIcon) removeButton.getAction().getValue(Action.SMALL_ICON);
        if (!(icon.getIconWidth() < 1)) {
            removeButton.setText("");
        }

        newButton.setAction(new NewGraphCommand(controller));
        icon = (ImageIcon) newButton.getAction().getValue(Action.SMALL_ICON);
        if (!(icon.getIconWidth() < 1)) {
            newButton.setText("");
        }

        saveButton.setAction(new SaveGraphCommand(controller));
        icon = (ImageIcon) saveButton.getAction().getValue(Action.SMALL_ICON);
        if (!(icon.getIconWidth() < 1)) {
            saveButton.setText("");
        }

        openButton.setAction(new OpenGraphCommand(controller));
        icon = (ImageIcon) openButton.getAction().getValue(Action.SMALL_ICON);
        if (!(icon.getIconWidth() < 1)) {
            openButton.setText("");
        }

        exportButton.setAction(new ExportGraphCommand(controller));
        icon = (ImageIcon) exportButton.getAction().getValue(Action.SMALL_ICON);
        if (!(icon.getIconWidth() < 1)) {
            exportButton.setText("");
        }

        showTableButton.setAction(new ShowConnectionMatrixCommand(controller));
        icon = (ImageIcon) showTableButton.getAction().getValue(Action.SMALL_ICON);
        if (!(icon.getIconWidth() < 1)) {
            showTableButton.setText("");
        }

        showScheduleButton.setAction(new ScheduleCommand(controller));
        icon = (ImageIcon) showScheduleButton.getAction().getValue(Action.SMALL_ICON);
        if (!(icon.getIconWidth() < 1)) {
            showScheduleButton.setText("");
        }

        exportDiagramButton.setAction(new ExportDiagramCommand(controller));
        icon = (ImageIcon) exportDiagramButton.getAction().getValue(Action.SMALL_ICON);
        if (!(icon.getIconWidth() < 1)) {
            exportDiagramButton.setText("");
        }
    }

    protected void addButtons() {
        add(arrowButton);
        add(nodeButton);
        add(connectionButton);
        add(removeButton);
        addSeparator();
        add(newButton);
        add(saveButton);
        add(openButton);
        addSeparator();
        add(exportButton);
        addSeparator();
        add(showTableButton);
        addSeparator();
        add(showScheduleButton);
        add(exportDiagramButton);
    }

}
