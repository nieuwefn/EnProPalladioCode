package org.palladiosimulator.measurementsui.abstractviewer.listener;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.palladiosimulator.measurementsui.datamanipulation.ResourceEditor;
import org.palladiosimulator.measurementsui.datamanipulation.ResourceEditorImpl;
import org.palladiosimulator.monitorrepository.MeasurementSpecification;
import org.palladiosimulator.monitorrepository.Monitor;

/**
 * DoubleClickListener which captures double clicks on the symbol of a tree item
 * 
 * @author David Schuetz
 */
public class MeasurementTreeDoubleClickListener implements MouseListener {

    private Tree measurementsTree;

    /**
     * 
     * @param measurementsTreeViewer
     *            the MeasurementTreeViewer where the listener is attached to.
     */
    public MeasurementTreeDoubleClickListener(TreeViewer measurementsTreeViewer) {
        this.measurementsTree = measurementsTreeViewer.getTree();
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        for (TreeItem item : measurementsTree.getSelection()) {
            if (isClickedOnTreeItemSymbol(e, item)) {
                setChecked(item);
            }
        }
    }

    @Override
    public void mouseDown(MouseEvent e) {
        // Do nothing on mouseDown
    }

    @Override
    public void mouseUp(MouseEvent e) {
        // Do nothing on mouseUp
    }

    /**
     * Depending on the previous state of Monitor change the active attribute of a monitor to true
     * or false
     * 
     * @param monitor
     *            the monitor which is set active/ inactive
     */
    private void toggleMonitorActive(Monitor monitor) {
        ResourceEditor edit = ResourceEditorImpl.getInstance();
        edit.changeMonitorActive(monitor, monitor.isActivated());
    }

    /**
     * Depending on the previous state of MeasurementSpecification change the active attribute of a
     * monitor to true or false
     * 
     * @param measurement
     *            the measurement where the attribute triggersSelfAdaptions is set
     */
    private void toggleTriggersSelfAdaption(MeasurementSpecification measurement) {
        ResourceEditor edit = ResourceEditorImpl.getInstance();
        edit.changeTriggersSelfAdapting(measurement);

    }

    /**
     * Depending on the tree item set a Monitor active/inactive or set triggerSelfAdaption of a
     * MeasurementSpecifaction to true or false.
     * 
     * @param item
     *            the tree item on which was clicked
     */
    private void setChecked(TreeItem item) {
        Object data = item.getData();

        if (data instanceof Monitor) {
            toggleMonitorActive((Monitor) data);
        }

        if (data instanceof MeasurementSpecification) {
            toggleTriggersSelfAdaption((MeasurementSpecification) data);
        }
    }

    /**
     * 
     * @param e
     *            the MouseEvent of the click
     * @param item
     *            the TreeItem on which was clicked
     * @return true if the click was inside the bounding box of the tree item symbol
     */
    private boolean isClickedOnTreeItemSymbol(MouseEvent e, TreeItem item) {
        return item.getImage() != null && (e.x > item.getImageBounds(0).x)
                && (e.x < (item.getImageBounds(0).x + item.getImage().getBounds().width))
                && (e.y > item.getImageBounds(0).y)
                && (e.y < (item.getImageBounds(0).y + item.getImage().getBounds().height));
    }

}