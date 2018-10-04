package org.palladiosimulator.measurementsui.extension.contributions;

import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;
import org.palladiosimulator.measurementsui.extensionpoint.definition.IMeasuringPointMetricsWorkingCombinations;
import org.palladiosimulator.metricspec.MetricDescription;
import org.palladiosimulator.metricspec.constants.MetricDescriptionConstants;
import org.palladiosimulator.pcmmeasuringpoint.PcmmeasuringpointFactory;

public class StateOfActiveResourceWithActiveResource implements IMeasuringPointMetricsWorkingCombinations {

    public StateOfActiveResourceWithActiveResource() {
    }

    @Override
    public MeasuringPoint getMeasuringPoint() {
        return PcmmeasuringpointFactory.eINSTANCE.createActiveResourceMeasuringPoint();
    }

    @Override
    public MetricDescription getMetricDescription() {
        return MetricDescriptionConstants.STATE_OF_ACTIVE_RESOURCE_METRIC;
    }

    @Override
    public boolean addtoSuggestedList() {
       return true;
    }

}