package org.palladiosimulator.measurementsui.extension.contributions;

import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;
import org.palladiosimulator.measurementsui.extensionpoint.definition.IMeasuringPointMetricsWorkingCombinations;
import org.palladiosimulator.metricspec.MetricDescription;
import org.palladiosimulator.metricspec.constants.MetricDescriptionConstants;
import org.palladiosimulator.pcmmeasuringpoint.PcmmeasuringpointFactory;

public class ResponseTimeWithExternalCallAction implements IMeasuringPointMetricsWorkingCombinations {

    public ResponseTimeWithExternalCallAction() {
    }

    @Override
    public MeasuringPoint getMeasuringPoint() {
        return PcmmeasuringpointFactory.eINSTANCE.createExternalCallActionMeasuringPoint();
    }

    @Override
    public MetricDescription getMetricDescription() {
        return MetricDescriptionConstants.RESPONSE_TIME_METRIC;
    }

    @Override
    public boolean addtoSuggestedList() {
        return true;
    }

}