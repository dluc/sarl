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

import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.xbase.lib.Pure;

/** Build a documentation string for specific objects.
 */
public interface IEcoreDocumentationBuilder {

	/** Replies the grammar rule that corresponds to multiline comments.
	 * @return the ML grammar rule.
	 */
	@Pure
	AbstractRule getMLCommentRule();

	/** Replies the grammar rule that corresponds to singleline comments.
	 * @return the SL grammar rule.
	 */
	@Pure
	AbstractRule getSLCommentRule();

	/** Replies the documentation formatter used by this builder.
	 * @return documentation formatter.
	 */
	@Pure
	IDocumentationFormatter getDocumentationFormatter();

	/** Replies the formatted string that corresponds to the given documentation.
	 * @param doc the documentation text. It may be on multiple lines.
	 * @param objectType the type of the object for which the document must be built.
	 * @return the formatted comment.
	 */
	@Pure
	String build(String doc, Class<?> objectType);

}

