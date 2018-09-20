package org.palladiosimulator.measurementsui.datamanipulation;

import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;
import org.palladiosimulator.metricspec.MetricDescription;
import org.palladiosimulator.monitorrepository.MeasurementSpecification;

/**
 * Class for editing resources without use of parsley
 * 
 * @author Florian
 *
 */
public class ResourceEditorImpl implements ResourceEditor {

    private final DataEditor editor = new DataEditor();
    
    private static ResourceEditorImpl instance;

    public static ResourceEditorImpl getInstance() {
        if (ResourceEditorImpl.instance == null) {
        	ResourceEditorImpl.instance = new ResourceEditorImpl();
        }
        return ResourceEditorImpl.instance;
    }

    /* (non-Javadoc)
     * @see org.palladiosimulator.measurementsui.datamanipulation.ResourceEditor#setResourceName(org.eclipse.emf.ecore.EObject, java.lang.String)
     */
    @Override
    public void setResourceName(EObject resource, String newName) {
        editor.editResource(resource, "entityName", newName);
    }

    /* (non-Javadoc)
     * @see org.palladiosimulator.measurementsui.datamanipulation.ResourceEditor#changeMonitorActive(org.eclipse.emf.ecore.EObject, boolean)
     */
    @Override
    public void changeMonitorActive(EObject monitor, boolean currentBool) {
        editor.editResource(monitor, "activated", !currentBool);
    }

    /* (non-Javadoc)
     * @see org.palladiosimulator.measurementsui.datamanipulation.ResourceEditor#changeTriggersSelfAdapting(org.palladiosimulator.monitorrepository.MeasurementSpecification, boolean)
     */
    @Override
    public void changeTriggersSelfAdapting(MeasurementSpecification mspec) {
        editor.editResource(mspec, "triggersSelfAdaptations", !mspec.isTriggersSelfAdaptations());
    }

    /* (non-Javadoc)
     * @see org.palladiosimulator.measurementsui.datamanipulation.ResourceEditor#setMeasuringPoint(org.eclipse.emf.ecore.EObject, org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint)
     */
    @Override
    public void setMeasuringPointToMonitor(EObject monitor, MeasuringPoint mp) {
        editor.editResource(monitor, "measuringPoint", mp);
    }

    /* (non-Javadoc)
     * @see org.palladiosimulator.measurementsui.datamanipulation.ResourceEditor#addMeasuringPoint(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
     */
    @Override
    public void addMeasuringPointToRepository(EObject mpRep, EObject mp) {
        editor.addResource(mpRep, "measuringPoints", mp);
    }

	@Override
	public void deleteResource(EObject objToDelete) {
		editor.deleteResource(objToDelete);
		
	}

	@Override
	public void setMetricDescription(EObject aMeasurementSpecification, MetricDescription aMetricDescription) {
		editor.editResource(aMeasurementSpecification, "metricDescription", aMetricDescription);
	}

    @Override
    public void addMeasurementSpecificationToMonitor(EObject monitor, MeasurementSpecification mspec) {
        editor.addResource(monitor, "measurementSpecifications", mspec);
        
    }

	@Override
	public void addMonitorToRepository(EObject monitorRepository, EObject monitor) {
		editor.addResource(monitorRepository, "monitors", monitor);
		
	}


    

}
