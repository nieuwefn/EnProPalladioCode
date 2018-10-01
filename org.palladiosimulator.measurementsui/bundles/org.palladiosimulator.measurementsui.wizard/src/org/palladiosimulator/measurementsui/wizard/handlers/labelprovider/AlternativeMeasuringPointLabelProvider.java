package org.palladiosimulator.measurementsui.wizard.handlers.labelprovider;

import java.util.LinkedList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.provider.AssemblyContextItemProvider;
import org.palladiosimulator.pcm.core.entity.NamedElement;
import org.palladiosimulator.pcm.provider.PcmItemProviderAdapterFactory;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.PassiveResource;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.provider.BasicComponentItemProvider;
import org.palladiosimulator.pcm.repository.provider.PassiveResourceItemProvider;
import org.palladiosimulator.pcm.repository.provider.RepositoryItemProvider;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.provider.LinkingResourceItemProvider;
import org.palladiosimulator.pcm.resourceenvironment.provider.ResourceContainerItemProvider;
import org.palladiosimulator.pcm.resourceenvironment.provider.ResourceEnvironmentItemProvider;
import org.palladiosimulator.pcm.resourcetype.provider.ProcessingResourceTypeItemProvider;
import org.palladiosimulator.pcm.seff.ExternalCallAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.provider.ExternalCallActionItemProvider;
import org.palladiosimulator.pcm.seff.provider.ResourceDemandingSEFFItemProvider;
import org.palladiosimulator.pcm.subsystem.SubSystem;
import org.palladiosimulator.pcm.subsystem.provider.SubSystemItemProvider;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.provider.SystemItemProvider;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.provider.BranchItemProvider;
import org.palladiosimulator.pcm.usagemodel.provider.BranchTransitionItemProvider;
import org.palladiosimulator.pcm.usagemodel.provider.EntryLevelSystemCallItemProvider;
import org.palladiosimulator.pcm.usagemodel.provider.LoopItemProvider;
import org.palladiosimulator.pcm.usagemodel.provider.ScenarioBehaviourItemProvider;
import org.palladiosimulator.pcm.usagemodel.provider.UsageModelItemProvider;
import org.palladiosimulator.pcm.usagemodel.provider.UsageScenarioItemProvider;
/**
 * An alternative label provider for the first step of the measuringpoint creation wizard pages
 * 
 * @author Domas Mikalkinas
 *
 */
public class AlternativeMeasuringPointLabelProvider implements ILabelProvider, IFontProvider {

	private static final String TRANSIENT = "[TRANSIENT]";

	@Override
	public void addListener(ILabelProviderListener listener) {
		// not needed
	}

	@Override
	public void dispose() {
		// not needed
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// not needed
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// not needed
	}

	@Override
	public Image getImage(Object element) {
		PcmItemProviderAdapterFactory factory = new PcmItemProviderAdapterFactory();

		if (element instanceof LinkedList) {
			return null;
		} else {
			return getModelImage(element, factory);

		}

	}

	private Image getModelImage(Object element, PcmItemProviderAdapterFactory factory) {
		EObject object = (EObject) element;
		if (object instanceof UsageScenario) {
			UsageScenarioItemProvider mod = new UsageScenarioItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof ResourceEnvironment) {
			ResourceEnvironmentItemProvider mod = new ResourceEnvironmentItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof System) {
			SystemItemProvider mod = new SystemItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof AssemblyContext) {
			AssemblyContextItemProvider mod = new AssemblyContextItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof ResourceContainer) {
			ResourceContainerItemProvider mod = new ResourceContainerItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof ProcessingResourceSpecification) {
			ProcessingResourceTypeItemProvider mod = new ProcessingResourceTypeItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof LinkingResource) {
			LinkingResourceItemProvider mod = new LinkingResourceItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof ExternalCallAction) {
			ExternalCallActionItemProvider mod = new ExternalCallActionItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof EntryLevelSystemCall) {
			EntryLevelSystemCallItemProvider mod = new EntryLevelSystemCallItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof SubSystem) {
			SubSystemItemProvider mod = new SubSystemItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof PassiveResource) {
			PassiveResourceItemProvider mod = new PassiveResourceItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof Repository) {
			RepositoryItemProvider mod = new RepositoryItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof BasicComponent) {
			BasicComponentItemProvider mod = new BasicComponentItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof ResourceDemandingSEFF) {
			ResourceDemandingSEFFItemProvider mod = new ResourceDemandingSEFFItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof ScenarioBehaviour) {
			ScenarioBehaviourItemProvider mod = new ScenarioBehaviourItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof UsageModel) {
			UsageModelItemProvider mod = new UsageModelItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof Branch) {
			BranchItemProvider mod = new BranchItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof BranchTransition) {
			BranchTransitionItemProvider mod = new BranchTransitionItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else if (object instanceof Loop) {
			LoopItemProvider mod = new LoopItemProvider(factory);
			return ExtendedImageRegistry.getInstance().getImage(mod.getImage(object));
		} else {
			return null;
		}
	}

	@Override
	public String getText(Object element) {
		if (element instanceof LinkedList) {
			if (!((LinkedList<?>) element).isEmpty()) {
				return (((LinkedList<?>) element).get(0).getClass().getSimpleName().replaceAll("Impl", "")
						.replaceAll("([A-Z])", " $1"));
			} else {
				return null;
			}

		} else {
			if (element instanceof ProcessingResourceSpecification) {
				return ((ProcessingResourceSpecification) element).getActiveResourceType_ActiveResourceSpecification()
						.getEntityName();

			} else if (element instanceof AssemblyContext) {
				return ((AssemblyContext) element).getEntityName();

			} else if (element instanceof ResourceContainer) {
				return ((ResourceContainer) element).getEntityName();

			} else if (element instanceof LinkingResource) {
				return ((LinkingResource) element).getEntityName();

			} else if (element instanceof ExternalCallAction) {

				return ((ExternalCallAction) element).getEntityName();

			} else if (element instanceof EntryLevelSystemCall) {

				return ((EntryLevelSystemCall) element).getEntityName();

			} else if (element instanceof UsageScenario) {
				return ((UsageScenario) element).getEntityName();

			} else if (element instanceof ResourceDemandingSEFF) {
				return ((ResourceDemandingSEFF) element).toString().replace(TRANSIENT, "");
			} else if (element instanceof UsageModel) {
				return ((UsageModel) element).toString().replace(TRANSIENT, "");

			} else if (element instanceof BranchTransition) {
				return ((BranchTransition) element).toString().replace(TRANSIENT, "");
			}

			return ((NamedElement) element).getEntityName();

		}

	}

	@Override
	public Font getFont(Object element) {
		if (element instanceof ResourceEnvironment || element instanceof ResourceContainer
				|| element instanceof ProcessingResourceSpecification || element instanceof AssemblyContext
				|| element instanceof EntryLevelSystemCall || element instanceof ExternalCallAction
				|| element instanceof LinkingResource || element instanceof SubSystem
				|| element instanceof org.palladiosimulator.pcm.system.System || element instanceof UsageScenario) {

			return JFaceResources.getBannerFont();
		}
		return null;
	}

}
