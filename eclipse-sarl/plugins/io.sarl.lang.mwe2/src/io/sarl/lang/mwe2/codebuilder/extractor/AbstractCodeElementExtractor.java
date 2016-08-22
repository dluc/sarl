/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2016 the original authors or authors.
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

package io.sarl.lang.mwe2.codebuilder.extractor;

import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.Action;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xtext.generator.XtextGeneratorNaming;
import org.eclipse.xtext.xtext.generator.model.TypeReference;
import org.eclipse.xtext.xtext.generator.util.GenModelUtil2;

import io.sarl.lang.mwe2.codebuilder.config.CodeBuilderConfig;

/** Abstract implementation for extracting elements from the backtracking grammar.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCodeElementExtractor implements CodeElementExtractor {

	@Inject
	private XtextGeneratorNaming naming;

	@Inject
	private CodeBuilderConfig configuration;

	private Grammar grammar;

	/** Replies the code builder condifugration.
	 *
	 * @return the configuration.
	 */
	protected CodeBuilderConfig getCodeBuilderConfig() {
		return this.configuration;
	}

	/** Replies the fully qualified name for the classifier.
	 *
	 * @param classifier the classifier.
	 * @return the fully qualified name for the given classifier.
	 */
	protected static TypeReference newTypeReference(EClassifier classifier) {
		if (classifier == null) {
			return new TypeReference(Object.class);
		}
		final String name = GenModelUtil2.getJavaTypeName(classifier, classifier.eResource().getResourceSet());
		if (Strings.isEmpty(name)) {
			return new TypeReference(Object.class);
		}
		return new TypeReference(name);
	}

	@Override
	public ElementDescription newElementDescription(String name, EObject grammarComponent, EClassifier elementType) {
		final TypeReference interfaceType = getElementBuilderInterface(name);
		final TypeReference implementationType = getElementBuilderImpl(name);
		final TypeReference customImplementationType = getElementBuilderImplCustom(name);
		final TypeReference appenderType = getElementAppenderImpl(name);
		return new ElementDescription(name, grammarComponent, newTypeReference(elementType), interfaceType,
				implementationType, customImplementationType, appenderType);
	}

	/** Replies the first classifier that is defined in the given grammar element.
	 *
	 * @param grammarElement the grammar element.
	 * @return the classifier or <code>null</code> if none.
	 */
	protected static EClassifier getGeneratedTypeFor(EObject grammarElement) {
		if (grammarElement == null) {
			return null;
		}
		if (grammarElement instanceof AbstractRule) {
			return getGeneratedTypeFor((AbstractRule) grammarElement);
		}
		try {
			return Iterables.find(GrammarUtil.containedActions(grammarElement),
				(it) -> !Strings.isEmpty(it.getFeature())).getType().getClassifier();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	/** Replies the type that is generated by a rule.
	 *
	 * @param rule the rule.
	 * @return the generated type.
	 */
	protected static EClassifier getGeneratedTypeFor(AbstractRule rule) {
		final List<Action> actions = GrammarUtil.containedActions(rule);
		final EClassifier classifier;
		if (actions.isEmpty()) {
			classifier = rule.getType().getClassifier();
		} else {
			classifier = actions.get(0).getType().getClassifier();
		}
		return classifier;
	}

	/** Replies the container in the grammar rule for the given content element.
	 *
	 * @param root the biggest enclosing element to consider in the grammar.
	 * @param content the grammar element to search for the container.
	 * @return the container of the content.
	 */
	protected static EObject getContainerInRule(EObject root, EObject content) {
		EObject container = content;
		do {
			final EClassifier classifier = getGeneratedTypeFor(container);
			if (classifier != null) {
				return container;
			}
			container = container.eContainer();
		} while (container != root);
		final EClassifier classifier = getGeneratedTypeFor(root);
		if (classifier != null) {
			return root;
		}
		return null;
	}

	@Override
	public void initialize(Grammar grammar) {
		this.grammar = grammar;
	}

	/** Replies the grammar.
	 *
	 * @return the grammar.
	 */
	protected Grammar getGrammar() {
		return this.grammar;
	}

	/** Replies the naming conventions.
	 *
	 * @return the naming conventions.
	 */
	@Pure
	protected XtextGeneratorNaming getNaming() {
		return this.naming;
	}

	@Pure
	@Override
	public String getLanguageBasePackage() {
		final Grammar grammar = getGrammar();
		final String basePackage = getNaming().getRuntimeBasePackage(grammar);
		final String ecorePackage = basePackage + "." //$NON-NLS-1$
				+ GrammarUtil.getSimpleName(grammar).toLowerCase();
		return ecorePackage;
	}

	@Pure
	@Override
	public TypeReference getLanguageScriptInterface() {
		final AbstractRule rule = GrammarUtil.findRuleForName(getGrammar(), this.configuration.getScriptRuleName());
		final EClassifier type = getGeneratedTypeFor(rule);
		return newTypeReference(type);
	}

	@Pure
	@Override
	public String getBasePackage() {
		final Grammar grammar = getGrammar();
		final String basePackage = getNaming().getRuntimeBasePackage(grammar);
		return basePackage + ".codebuilder"; //$NON-NLS-1$
	}

	@Pure
	@Override
	public String getAppenderPackage() {
		return getBasePackage() + ".appenders"; //$NON-NLS-1$
	}

	@Pure
	@Override
	public String getDocumentationPackage() {
		final Grammar grammar = getGrammar();
		final String basePackage = getNaming().getRuntimeBasePackage(grammar);
		return basePackage + ".documentation"; //$NON-NLS-1$
	}

	@Pure
	@Override
	public TypeReference getInnerBlockDocumentationAdapter() {
		return new TypeReference(getDocumentationPackage()
				+ ".InnerBlockDocumentationAdapter"); //$NON-NLS-1$
	}

	@Pure
	@Override
	public String getSerializerPackage() {
		final Grammar grammar = getGrammar();
		final String basePackage = getNaming().getRuntimeBasePackage(grammar);
		return basePackage + ".serializer"; //$NON-NLS-1$
	}

	@Pure
	@Override
	public TypeReference getElementAppenderImpl(String elementName) {
		return new TypeReference(getAppenderPackage() + "." //$NON-NLS-1$
				+ Strings.toFirstUpper(elementName) + "SourceAppender"); //$NON-NLS-1$
	}

	@Pure
	@Override
	public TypeReference getElementAppenderImplCustom(String elementName) {
		return new TypeReference(getAppenderPackage() + "." //$NON-NLS-1$
				+ Strings.toFirstUpper(elementName) + "SourceAppenderCustom"); //$NON-NLS-1$
	}

	@Pure
	@Override
	public TypeReference getAbstractAppenderImpl() {
		return new TypeReference(getAppenderPackage() + ".AbstractSourceAppender"); //$NON-NLS-1$
	}

	@Pure
	@Override
	public String getBuilderPackage() {
		return getBasePackage() + ".builders"; //$NON-NLS-1$
	}

	@Pure
	@Override
	public TypeReference getElementBuilderInterface(String elementName) {
		return new TypeReference(getBuilderPackage() + ".I" //$NON-NLS-1$
				+ Strings.toFirstUpper(elementName) + "Builder"); //$NON-NLS-1$
	}

	@Pure
	@Override
	public TypeReference getElementBuilderImpl(String elementName) {
		return new TypeReference(getBuilderPackage() + "." //$NON-NLS-1$
				+ Strings.toFirstUpper(elementName) + "BuilderImpl"); //$NON-NLS-1$
	}

	@Pure
	@Override
	public TypeReference getElementBuilderImplCustom(String elementName) {
		return new TypeReference(getBuilderPackage() + "." //$NON-NLS-1$
				+ Strings.toFirstUpper(elementName) + "BuilderImplCustom"); //$NON-NLS-1$
	}

	@Pure
	@Override
	public TypeReference getLanguageTopElementType() {
		final Grammar grammar = getGrammar();
		final AbstractRule rule = GrammarUtil.findRuleForName(grammar, getCodeBuilderConfig().getTopElementRuleName());
		return newTypeReference(rule.getType().getClassifier());
	}

	@Override
	public TypeReference getFormalParameterContainerType() {
		return new TypeReference(
				getCodeBuilderConfig().getFormalParameterContainerType());
	}

	@Override
	public ElementDescription getFormalParameter() {
		final AbstractRule rule = GrammarUtil.findRuleForName(getGrammar(), getCodeBuilderConfig().getFormalParameterRuleName());
		final EClassifier classifier = getGeneratedTypeFor(rule);
		return newElementDescription(classifier.getName(), rule, classifier);
	}

}