/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014 Sebastian RODRIGUEZ, Nicolas GAUD, Stéphane GALLAND.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sarl.eclipse.wizards.elements.newevent;

import io.sarl.eclipse.SARLConfig;
import io.sarl.eclipse.SARLEclipsePlugin;
import io.sarl.eclipse.wizards.elements.AbstractNewSarlElementWizardPage;
import io.sarl.eclipse.wizards.elements.SarlTypeCreatorUtil;
import io.sarl.lang.core.Event;

import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Composite;

/**
 * Wizard page for creating a new SARL event.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class NewSarlEventWizardPage extends AbstractNewSarlElementWizardPage {

	/**
	 */
	public NewSarlEventWizardPage() {
		super(CLASS_TYPE, Messages.NewSarlEvent_0);
		setTitle(Messages.NewSarlEvent_0);
		setDescription(Messages.NewSarlEventWizardPage_0);
		setImageDescriptor(SARLEclipsePlugin.getDefault().getImageDescriptor(SARLConfig.NEW_EVENT_WIZARD_DIALOG_IMAGE));
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = createCommonControls(parent);
		createSuperClassControls(composite, COLUMNS);
		setControl(composite);
	}

	@Override
	protected void doStatusUpdate() {
		IStatus[] status = new IStatus[] {
				this.fContainerStatus,
				this.fPackageStatus,
				this.fTypeNameStatus,
				this.fSuperClassStatus,
		};
		updateStatus(status);
	}

	@Override
	protected void getTypeContent(IPackageFragment packageFragment,
			StringBuilder typeContent, Set<String> imports,
			String indentation, String lineSeparator) {
		String content = SarlTypeCreatorUtil.createEventContent(
				packageFragment.getElementName(),
				getTypeName(),
				getSuperClass(),
				imports,
				indentation, lineSeparator);
		typeContent.append(content);
	}

	@Override
	protected String getExistingElementErrorMessage() {
		return Messages.NewSarlEventWizardPage_1;
	}

	@Override
	protected String getInvalidSubtypeErrorMessage() {
		return Messages.NewSarlEventWizardPage_2;
	}

	@Override
	protected IType getRootSuperType() throws JavaModelException {
		return findType(getJavaProject(), Event.class.getName());
	}

}