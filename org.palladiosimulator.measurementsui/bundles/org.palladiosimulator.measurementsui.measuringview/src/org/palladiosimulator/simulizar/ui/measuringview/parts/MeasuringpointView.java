package org.palladiosimulator.simulizar.ui.measuringview.parts;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.internal.workbench.handlers.SaveHandler;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;
import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPointRepository;
import org.palladiosimulator.measurementsui.abstractviewer.MpTreeViewer;
import org.palladiosimulator.measurementsui.datamanipulation.ResourceEditor;
import org.palladiosimulator.measurementsui.datamanipulation.ResourceEditorImpl;
import org.palladiosimulator.measurementsui.dataprovider.DataApplication;
import org.palladiosimulator.measurementsui.wizardmain.MeasuringPointsWizard;
import org.palladiosimulator.measurementsui.wizardmodel.WizardModelType;
import org.palladiosimulator.monitorrepository.MeasurementSpecification;
import org.palladiosimulator.monitorrepository.Monitor;
import org.palladiosimulator.monitorrepository.MonitorRepository;
import org.palladiosimulator.monitorrepository.ProcessingType;
import org.palladiosimulator.simulizar.ui.measuringview.viewer.EmptyMpTreeViewer;
import org.palladiosimulator.simulizar.ui.measuringview.viewer.MonitorTreeViewer;

/**
 * Eclipse e4 view in which the user gets an overview of all existing monitors and measuringpoints
 * in a selected monitorrepository.
 * 
 * @author David Schuetz
 * 
 */
public class MeasuringpointView {

    private MpTreeViewer monitorTreeViewer;
    private MpTreeViewer measuringTreeViewer;
    private Combo projectsComboDropDown;
    private DataApplication dataApplication;
    private Button deleteButton;
    private Button editButton;

    @Inject
    private MDirtyable dirty;

    @Inject
    private ECommandService commandService;

    @Inject
    private EHandlerService handlerService;

    @Inject
    private ESelectionService selectionService;

    /**
     * Creates the meu items and controls for the simulizar measuring point view
     * 
     * @param parent
     *            composite of the empty view
     */
    @PostConstruct
    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout(1, true));
        initializeApplication();
        createWorkspaceListener();
        createProjectsSelectionComboBox(parent);
        SashForm outerContainer = new SashForm(parent, SWT.FILL);
        outerContainer.setLayout(new GridLayout(1, true));
        outerContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        SashForm treeContainer = new SashForm(outerContainer, SWT.VERTICAL);
        treeContainer.setLayout(new GridLayout(1, true));

        Composite buttonContainer = new Composite(outerContainer, SWT.BORDER);
        buttonContainer.setLayout(new GridLayout(1, true));

        outerContainer.setWeights(new int[] { 3, 1 });

        Composite monitorContainer = createTreeComposite(treeContainer);
        Composite undefinedMeasuringContainer = createTreeComposite(treeContainer);

        createViewButtons(buttonContainer);

        monitorTreeViewer = createMonitorTreeViewer(monitorContainer);
        measuringTreeViewer = createEmptyMpTreeViewer(undefinedMeasuringContainer);

        handlerService.activateHandler("org.eclipse.ui.file.save", new SaveHandler());
    }

    /**
     * Initializes the connecton to the data management and manipulation packages
     */
    private void initializeApplication() {
        this.dataApplication = DataApplication.getInstance();
        dataApplication.loadData(0);
    }

    /**
     * Adds a changeListener to the eclispe workspace to listen to changes in it and update our GUI
     * accordingly
     */
    private void createWorkspaceListener() {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IResourceChangeListener listener = new IResourceChangeListener() {
            public void resourceChanged(IResourceChangeEvent event) {
                Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                        updateMeasuringPointView();
                    }
                });
            }
        };
        workspace.addResourceChangeListener(listener, 1);

    }

    /**
     * Creates a tree view which shows all existing monitors and their childs in the selected
     * projected
     * 
     * @param parent
     *            composite where the tree view will be placed
     * @return TreeViewer which includes all existing monitors
     */
    private MpTreeViewer createMonitorTreeViewer(Composite parent) {
        MpTreeViewer mpTreeViewer = new MonitorTreeViewer(parent, dirty, commandService, dataApplication);
        mpTreeViewer.addMouseListener();
        addSelectionListener(mpTreeViewer.getViewer());
        return mpTreeViewer;
    }

    /**
     * Creates a tree view which shows all empty measuring points of all projects in the workspace
     * 
     * @param parent
     *            composite where the tree view will be placed
     * @return TreeViewer which includes all measuring points without a monitor
     */
    private MpTreeViewer createEmptyMpTreeViewer(Composite parent) {
        EmptyMpTreeViewer emptyMpTreeViewer = new EmptyMpTreeViewer(parent, dirty, commandService, dataApplication);
        addSelectionListener(emptyMpTreeViewer.getViewer());
        return emptyMpTreeViewer;
    }

    private void addSelectionListener(Viewer treeViewer) {
        treeViewer.addSelectionChangedListener(event -> {
            IStructuredSelection selection = (IStructuredSelection) event.getSelection();
            selectionService.setSelection(selection.size() == 1 ? selection.getFirstElement() : selection.toArray());

            Object selectionObject = selection.getFirstElement();
            if (selectionObject instanceof MonitorRepository || selectionObject instanceof MeasuringPointRepository) {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
            } else {
                editButton.setEnabled(true);
                if (selectionObject instanceof ProcessingType) {
                    deleteButton.setEnabled(false);
                } else {
                    deleteButton.setEnabled(true);
                }
            }
        });
    }

    /**
     * Creates the composite in which the tree view is later embedded
     * 
     * @param parent
     *            composite where the tree composite will be placed
     * @return Composite where the TreeViewers can be placed
     */
    private Composite createTreeComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FillLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        return composite;
    }

    /**
     * Create all buttons of the view which provide different functionalities like editing,
     * deleting, assigning measuringpoints to monitor or creating a standard measuring point set
     * 
     * @param buttonContainer
     *            composite where the buttons will be placed
     */
    private void createViewButtons(Composite buttonContainer) {
        createNewMeasuringpointButton(buttonContainer);
        createDeleteButton(buttonContainer);
        createEditButton(buttonContainer);

        Button assignMonitorButton = new Button(buttonContainer, SWT.PUSH);
        assignMonitorButton.setText("Assign to Monitor");
        Button createStandardButton = new Button(buttonContainer, SWT.PUSH);
        createStandardButton.setText("Create Standard Set");

    }

    /**
     * Creates a Button which opens the Wizard in order to create a measuring point
     * 
     * @param parent
     *            composite where the button will be placed
     */
    private void createNewMeasuringpointButton(Composite parent) {
        Button newMpButton = new Button(parent, SWT.PUSH);
        newMpButton.setText("Add new Measuring Point");

        newMpButton.addListener(SWT.Selection, e -> {
            MeasuringPointsWizard wizard = new MeasuringPointsWizard();
            Shell parentShell = wizard.getShell();
            WizardDialog dialog = new WizardDialog(parentShell, wizard);
            dialog.setPageSize(720, 400);
            dialog.setMinimumPageSize(720, 400);
            dialog.open();
        });
    }

    /**
     * Creates a Button which deletes selected EObjects
     * 
     * @param parent
     *            composite where the button will be placed
     */
    private void createDeleteButton(Composite parent) {
        deleteButton = new Button(parent, SWT.PUSH);
        deleteButton.setText("Delete...");

        deleteButton.addListener(SWT.Selection, e -> {
            ResourceEditor resourceEditor = new ResourceEditorImpl();
            Object selection = selectionService.getSelection();
            if (selection instanceof EObject) {
                resourceEditor.deleteResource((EObject) selection);
            }
        });
    }

    /**
     * Creates a Button which edits selected EObjects
     * 
     * @param parent
     *            composite where the button will be placed
     */
    private void createEditButton(Composite parent) {
        editButton = new Button(parent, SWT.PUSH);
        editButton.setText("Edit...");
        
        editButton.addListener(SWT.Selection, e -> {
        	MeasuringPointsWizard wizard;
            Object selection = selectionService.getSelection();   
            ITreeContentProvider provider = (ITreeContentProvider) monitorTreeViewer.getViewer().getContentProvider();
            if (selection instanceof Monitor) {
                wizard = new MeasuringPointsWizard(WizardModelType.MONITOR_CREATION, (Monitor) selection);    
            } else if (selection instanceof MeasuringPoint) {
            	wizard = new MeasuringPointsWizard(WizardModelType.MEASURING_POINT_SELECTION, (Monitor) provider.getParent(selection));   
            } else if (selection instanceof MeasurementSpecification) { 
            	wizard = new MeasuringPointsWizard(WizardModelType.METRIC_DESCRIPTION_SELECTION, (Monitor) provider.getParent(selection));   
            } else {
            	wizard = new MeasuringPointsWizard(WizardModelType.MONITOR_CREATION);   
            }
            
            Shell parentShell = wizard.getShell();
            WizardDialog dialog = new WizardDialog(parentShell, wizard);
            dialog.setPageSize(720, 400);
            dialog.setMinimumPageSize(720, 400);
            dialog.open();
        });
    }

    /**
     * Creates a combobox at the top of the view where the user can select the project
     * 
     * @param parent
     *            composite where the combobox is placed in
     */
    private void createProjectsSelectionComboBox(Composite parent) {
        projectsComboDropDown = new Combo(parent, SWT.DROP_DOWN);
        projectsComboDropDown.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        updateProjectComboBox();
        projectsComboDropDown.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                int selectionIndex = projectsComboDropDown.getSelectionIndex();
                dataApplication.loadData(selectionIndex);
                updateTreeViewer();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                projectsComboDropDown.select(0);

            }
        });
        projectsComboDropDown.select(0);
    }

    /**
     * Adds every project in the workspace, that has an .aird file to the projectsComboBox
     */
    private void updateProjectComboBox() {

        int selectionIndex = -1;
        projectsComboDropDown.removeAll();
        List<IProject> allProjects = dataApplication.getDataGathering().getAllProjectAirdfiles();
        for (int i = 0; i < allProjects.size(); i++) {
            IProject project = allProjects.get(i);

            if (project.equals(dataApplication.getProject())) {
                selectionIndex = i;
            }

            projectsComboDropDown.add(project.toString());
        }
        projectsComboDropDown.select(selectionIndex);

    }

    /**
     * Reloads the dashboard view and updates it, if something changed
     */
    private void updateMeasuringPointView() {
        updateProjectComboBox();
        updateTreeViewer();
    }

    /**
     * Updates the Monitor and Measuringpoint Tree Viewer
     */
    private void updateTreeViewer() {
        monitorTreeViewer.update();
        measuringTreeViewer.update();
    }

    /**
     * Saves the current data in the tree view
     * 
     * @param dirty
     *            states whether there were changes made
     * @throws IOException
     */
    @Persist
    public void save(MDirtyable dirty) throws IOException {
        monitorTreeViewer.save(dirty);
    }
}
