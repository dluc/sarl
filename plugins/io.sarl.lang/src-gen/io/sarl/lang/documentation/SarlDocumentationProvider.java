/*
 * $Id$
 *
 * File is automatically generated by the Xtext language generator.
 * Do not change it.
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright 2014-2016 the original authors and authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sarl.lang.documentation;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend.core.documentation.XtendDocumentationProvider;
import org.eclipse.xtext.xbase.compiler.DocumentationAdapter;

/** Provider a documentation string.
 */
public class SarlDocumentationProvider extends XtendDocumentationProvider {

	public String getDocumentation(EObject o) {
		// Get the documentation from the Xtext grammar hidden nodes.
		String text = super.getDocumentation(o);
		if (text == null) {
			// Get the grammar from the Ecore model element.
			if (o instanceof EModelElement) {
				text = EcoreUtil.getDocumentation((EModelElement) o);
			}
			if (text == null) {
				// Get the grammar from the code builder extension.
				DocumentationAdapter adapter = (DocumentationAdapter) EcoreUtil.getAdapter(
						o.eAdapters(), DocumentationAdapter.class);
				if (adapter != null) {
					return adapter.getDocumentation();
				}
			}
		}
		return text;
	}
}

