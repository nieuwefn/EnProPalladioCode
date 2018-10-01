package org.palladiosimulator.measurementsui.abstractviewer;

import org.eclipse.emf.parsley.viewers.ViewerFactory;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.palladiosimulator.measurementsui.wizardmodel.WizardModel;
import org.palladiosimulator.monitorrepository.MonitorRepositoryPackage;

/**
 * Generates a Eclipse SWT TableViewer based on a TableViewer generated by Eclipse Parsley
 * 
 * @author David Schuetz
 *
 */
public abstract class WizardTableViewer extends ComponentViewer {
    protected TableViewer tableViewer;
    protected ViewerFactory tableFactory;
    protected WizardModel wizardModel;

    /**
     * 
     * @param parent
     *            the container where the tree viewer is placed in
     * @param wizardModel
     *            the connection to temporary data created in the wizard.
     */
    protected WizardTableViewer(Composite parent, WizardModel wizardModel) {
        super(parent);
        this.wizardModel = wizardModel;
        initEditingDomain();
        initParsley(parent);
    }

    @Override
    protected void initParsley(Composite parent) {
        tableFactory = injector.getInstance(ViewerFactory.class);
        tableViewer = tableFactory.createTableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI,
                MonitorRepositoryPackage.eINSTANCE.getMeasurementSpecification());
        tableViewer.setInput(getModelRepository());
    }

    @Override
    public void update() {
        
    }

    @Override
    public StructuredViewer getViewer() {
        return tableViewer;
    }

    @Override
    protected void initDragAndDrop() {
        // Don't use drag and drop in this viewer
    }

}