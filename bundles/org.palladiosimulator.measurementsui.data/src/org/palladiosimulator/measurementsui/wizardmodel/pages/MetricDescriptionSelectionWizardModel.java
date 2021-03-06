package org.palladiosimulator.measurementsui.wizardmodel.pages;

import org.palladiosimulator.measurementsui.datamanipulation.ResourceEditorImpl;
import org.palladiosimulator.measurementsui.dataprovider.UnselectedMetricSpecificationsProvider;
import org.palladiosimulator.measurementsui.wizardmodel.WizardModel;
import org.palladiosimulator.monitorrepository.MeasurementSpecification;
import org.palladiosimulator.monitorrepository.Monitor;
import org.palladiosimulator.monitorrepository.MonitorRepositoryFactory;

/**
 * Provides all methods to edit the MeasurementSpecifications of a monitor in the wizard
 * 
 * @author David Schuetz
 *
 */
public class MetricDescriptionSelectionWizardModel implements WizardModel {
    private static final String STANDARD_INFORMATION_MESSAGE = "Please select all Metrics which should be measured.";
    private static final String NO_METRIC_SELECTED_MEASSAGE = "There is currently no Metric selected. "
            + "In order to get Simulation results you have to select at least one Metric.";

    private static final String METRIC_SELECTION_TITEL = "Select Metrics";

    private Monitor usedMetricsMonitor;
    private Monitor unusedMetricsMonitor;
    private UnselectedMetricSpecificationsProvider provider;
    private boolean isEditing;

    /**
     * 
     * @param monitor
     *            the monitor where metricDescriptions will be added or removed
     * @param isEditing
     *            states whether the model edits an existing monitor or creates a new one.
     */
    public MetricDescriptionSelectionWizardModel(Monitor monitor, boolean isEditing) {
        this.provider = new UnselectedMetricSpecificationsProvider();
        this.usedMetricsMonitor = monitor;
        this.isEditing = isEditing;
        this.unusedMetricsMonitor = MonitorRepositoryFactory.eINSTANCE.createMonitor();

    }

    /**
     * unusedMetrics initialized when page is visible, since information from second page is
     * required to correctly initialize the unusedMetrics Monitor.
     * 
     * @param usedMonitor
     */
    public void initUnusedMetrics(Monitor usedMonitor, boolean expertMode) {
        provider.createMonitorWithMissingMetricDescriptions(usedMonitor, unusedMetricsMonitor, expertMode);
    }

    @Override
    public boolean canFinish() {
        return !metricListIsEmpty();
    }

    @Override
    public String getInfoText() {
        if (metricListIsEmpty()) {
            return NO_METRIC_SELECTED_MEASSAGE;
        }
        return STANDARD_INFORMATION_MESSAGE;
    }

    /**
     * 
     * @return a monitor with all unused metric descriptions
     */
    public Monitor getUnusedMetricsMonitor() {
        return unusedMetricsMonitor;
    }

    /**
     * 
     * @return a monitor with all used metric descriptions
     */
    public Monitor getUsedMetricsMonitor() {
        return usedMetricsMonitor;
    }

    /**
     * Adds a MeasurementSpecification with a MetricDescription to the monitor
     * 
     * @param selectedMeasurementSpecification
     *            the specification which will be added to the monitor
     */
    public void addMeasurementSpecification(MeasurementSpecification selectedMeasurementSpecification) {
        if (selectedMeasurementSpecification != null) {
            provider.moveMeasurementSpecificationToMonitor(selectedMeasurementSpecification, usedMetricsMonitor,
                    isEditing);
        }

    }

    /**
     * Removes a MeasurementSpecifcation with a MetricDescription from the monitor
     * 
     * @param selectedMeasurementSpecification
     *            the specification which will be added to the monitor
     */
    public void removeMeasurementSpecification(MeasurementSpecification selectedMeasurementSpecification) {
        if (selectedMeasurementSpecification != null) {
            provider.removeMeasurementSpecificationFromMonitor(selectedMeasurementSpecification, unusedMetricsMonitor,
                    isEditing);
        }

    }

    /**
     * Add all unused MetricDescriptions to the monitor
     */
    public void addAllMetricDescriptions() {
        provider.moveAllMeasurementSpecificationsToMonitor(unusedMetricsMonitor, usedMetricsMonitor, isEditing);
    }

    /**
     * Remove all MetricDescriptions from the monitor
     */
    public void removeAllMetricDescriptions() {
        provider.removeAllMeasurementSpecificationsFromMonitor(usedMetricsMonitor, unusedMetricsMonitor, isEditing);
    }

    /**
     * Moves only the suggested Metric Specs to the monitor.
     */
    public void moveAllSuggested() {
        provider.moveSuggestedMeasurementSpecificationsToMonitor(unusedMetricsMonitor, usedMetricsMonitor, isEditing);
    }

    /**
     * Switches the attribute triggerSelfAdaption of a specific measurementSpecification
     * 
     * @param currentValue
     * @param mspec
     *            the MeasurementSpecification where the triggerSelfAdaptiong attribute will be
     *            changed
     */
    public void switchTriggerSelfAdapting(MeasurementSpecification mspec) {
        if (isEditing) {
            ResourceEditorImpl.getInstance().changeTriggersSelfAdapting(mspec);
        } else {
            mspec.setTriggersSelfAdaptations(!mspec.isTriggersSelfAdaptations());
        }
    }

    /**
     * 
     * @return true if the list of used MeasurementSpecifications is empty
     */
    private boolean metricListIsEmpty() {
        return usedMetricsMonitor.getMeasurementSpecifications().isEmpty();
    }

    @Override
    public String getTitleText() {
        return METRIC_SELECTION_TITEL;

    }

    /**
     * Provides the textual description for the metric description of a measurement specification.
     * Used for showing the description of the metric description in the title of the third page.
     * 
     * @param aMeasurementSpecification
     * @return The textual Description in String form
     */
    public String getTextualDescriptionForMetricDescription(MeasurementSpecification aMeasurementSpecification) {
        return provider.provideTextualDescriptionForMetricDescription(aMeasurementSpecification.getMetricDescription());
    }

}