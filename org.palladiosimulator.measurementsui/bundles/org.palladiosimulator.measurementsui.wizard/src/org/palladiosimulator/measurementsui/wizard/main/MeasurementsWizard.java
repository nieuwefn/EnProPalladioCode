package org.palladiosimulator.measurementsui.wizard.main;

import org.eclipse.jface.wizard.IWizardPage;
import org.palladiosimulator.measurementsui.wizard.pages.AddMonitorWizardPage;
import org.palladiosimulator.measurementsui.wizard.pages.AdditionalModelsToMeasuringpointWizardPage;
import org.palladiosimulator.measurementsui.wizard.pages.ChooseMeasuringpointWizardPage;
import org.palladiosimulator.measurementsui.wizard.pages.FinalModelsToMeasuringpointWizardPage;
import org.palladiosimulator.measurementsui.wizard.pages.MeasurementSpecificationWizardPage;
import org.palladiosimulator.measurementsui.wizard.pages.SelectMeasurementsWizardPage;
import org.palladiosimulator.measurementsui.wizardmodel.WizardModelManager;
import org.palladiosimulator.measurementsui.wizardmodel.WizardModelType;
import org.palladiosimulator.measurementsui.wizardmodel.pages.MeasuringPointSelectionWizardModel;
import org.palladiosimulator.measurementsui.wizardmodel.pages.MetricDescriptionSelectionWizardModel;
import org.palladiosimulator.measurementsui.wizardmodel.pages.MonitorCreationWizardModel;
import org.palladiosimulator.measurementsui.wizardmodel.pages.ProcessingTypeSelectionWizardModel;
import org.palladiosimulator.monitorrepository.Monitor;
import org.palladiosimulator.monitorrepository.MonitorRepositoryFactory;

/**
 * This class handles the wizard and its wizard pages for creating a new measuring point/monitor.
 * 
 * @author Birasanth Pushpanathan David Schuetz, Lasse Merz Added WizardModel support
 *
 */
public class MeasurementsWizard extends org.eclipse.jface.wizard.Wizard {

    private WizardModelManager wizardManager;

    private WizardModelType startingPage;
    /**
     * Represents the first wizard page, where the new monitor defined (name +
     * activated/deactivated)
     */
    private AddMonitorWizardPage page1;

    /**
     * Represents the 2nd wizard page, where the user selects either an existing measuring point for
     * the new monitor or creates a new one.
     */
    private ChooseMeasuringpointWizardPage page2;

    /**
     * Represents the 3rd wizard page, where the user selects measurements which are then assigned
     * to the monitor
     */
    private SelectMeasurementsWizardPage page3;

    /**
     * Represents the 4th wizard page, where the user can set properties for the selected
     * measurements.
     */
    private MeasurementSpecificationWizardPage page4;

    private AdditionalModelsToMeasuringpointWizardPage page2extra;

    private FinalModelsToMeasuringpointWizardPage page2final;

    public MeasurementsWizard() {
        wizardManager = new WizardModelManager();
        this.startingPage = WizardModelType.MONITOR_CREATION;
        createPages();
    }

    /**
     * The constructor
     */
    public MeasurementsWizard(WizardModelType startingPage) {
        setWindowTitle("Add new Measuring Point");
        wizardManager = new WizardModelManager();
        this.startingPage = startingPage;
        createPages();
    }

    /**
     * The constructor
     */
    public MeasurementsWizard(WizardModelType startingPage, Monitor monitor) {
        setWindowTitle("Add new Measuring Point");
        wizardManager = new WizardModelManager(monitor);
        this.startingPage = startingPage;
        createPages();
    }

    private void createPages() {
        page1 = new AddMonitorWizardPage(
                (MonitorCreationWizardModel) wizardManager.getWizardModel(WizardModelType.MONITOR_CREATION));
        
        page2 = new ChooseMeasuringpointWizardPage((MeasuringPointSelectionWizardModel) wizardManager
                .getWizardModel(WizardModelType.MEASURING_POINT_SELECTION));
        page2extra = new AdditionalModelsToMeasuringpointWizardPage((MeasuringPointSelectionWizardModel) wizardManager
                .getWizardModel(WizardModelType.MEASURING_POINT_SELECTION));
        page2final = new FinalModelsToMeasuringpointWizardPage((MeasuringPointSelectionWizardModel) wizardManager
                .getWizardModel(WizardModelType.MEASURING_POINT_SELECTION));
        
        page3 = new SelectMeasurementsWizardPage((MetricDescriptionSelectionWizardModel) wizardManager
                .getWizardModel(WizardModelType.METRIC_DESCRIPTION_SELECTION));
        
        page4 = new MeasurementSpecificationWizardPage(
                (ProcessingTypeSelectionWizardModel) wizardManager.getWizardModel(WizardModelType.PROCESSING_TYPE));

    }

    @Override
    public IWizardPage getStartingPage() {
        switch (startingPage) {
        case MONITOR_CREATION:
            return page1;
        case MEASURING_POINT_SELECTION:
            return page2;
        case METRIC_DESCRIPTION_SELECTION:
            return page3;
        case PROCESSING_TYPE:
            return page4;
        default:
            return page1;
        }

    }

    @Override
    public void addPages() {
        addPage(page1);
        addPage(page2);
        addPage(page3);
        addPage(page4);
        addPage(page2extra);
        addPage(page2final);

    }

    @Override
    public boolean performFinish() {
        wizardManager.finish();
        return true;
    }

    @Override
    public boolean canFinish() {
        return wizardManager.canFinish();
    }
}