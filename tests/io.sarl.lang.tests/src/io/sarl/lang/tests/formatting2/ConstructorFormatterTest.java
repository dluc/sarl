/*
 * Copyright (C) 2014-2015 the original authors or authors.
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
package io.sarl.lang.tests.formatting2;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import com.google.inject.Inject;
import junit.framework.TestSuite;
import org.eclipse.xtext.junit4.formatter.FormatterTestRequest;
import org.eclipse.xtext.junit4.formatter.FormatterTester;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.lib.Procedures;
import org.junit.Test;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import io.sarl.tests.api.AbstractSarlTest;

/** Tests for formatting constructors.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ConstructorFormatterTest extends AbstractMemberFormatterTest {

	@Test
	public void noParam() throws Exception {
		String source = unformattedCode("new {System.out.println(\"abc\")}");
		String expected = formattedCode(
				"	new {",
				"		System.out.println(\"abc\")",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void param() throws Exception {
		String source = unformattedCode("new(a:int){System.out.println(a)}");
		String expected = formattedCode(
				"	new(a : int) {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void params() throws Exception {
		String source = unformattedCode("new(a:int,b:boolean){System.out.println(a)}");
		String expected = formattedCode(
				"	new(a : int, b : boolean) {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void defaultValuedParam() throws Exception {
		String source = unformattedCode("new(a:int=5){System.out.println(a)}");
		String expected = formattedCode(
				"	new(a : int = 5) {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void defaultValuedParams() throws Exception {
		String source = unformattedCode("new(a:int=5,b:boolean=false){System.out.println(a)}");
		String expected = formattedCode(
				"	new(a : int = 5, b : boolean = false) {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void varArg() throws Exception {
		String source = unformattedCode("new(a:int  * ){System.out.println(a)}");
		String expected = formattedCode(
				"	new(a : int*) {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void javaGeneric() throws Exception {
		String source = unformattedCode("new<T>(a:T){System.out.println(a)}");
		String expected = formattedCode(
				"	new <T> (a : T) {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void javaGenerics() throws Exception {
		String source = unformattedCode("new<T,TT extends T>(a:T,b:TT){System.out.println(a)}");
		String expected = formattedCode(
				"	new <T, TT extends T> (a : T, b : TT) {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void sarlGeneric() throws Exception {
		String source = unformattedCode("new(a:T)with T{System.out.println(a)}");
		String expected = formattedCode(
				"	new(a : T) with T {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void sarlGenerics() throws Exception {
		String source = unformattedCode("new(a:T,b:TT)with T,TT extends T{System.out.println(a)}");
		String expected = formattedCode(
				"	new(a : T, b : TT) with T, TT extends T {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void exceptionNoParam() throws Exception {
		String source = unformattedCode("new throws Exception{System.out.println(a)}");
		String expected = formattedCode(
				"	new throws Exception {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void exceptionsNoParam() throws Exception {
		String source = unformattedCode("new throws Exception,Exception{System.out.println(a)}");
		String expected = formattedCode(
				"	new throws Exception, Exception {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void exceptionEmptyParam() throws Exception {
		String source = unformattedCode("new()throws Exception{System.out.println(a)}");
		String expected = formattedCode(
				"	new() throws Exception {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void exceptionsEmptyParam() throws Exception {
		String source = unformattedCode("new()throws Exception,Exception{System.out.println(a)}");
		String expected = formattedCode(
				"	new() throws Exception, Exception {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void modifiers() throws Exception {
		String source = unformattedCode("public    new {System.out.println(a)}");
		String expected = formattedCode(
				"	public new {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void annotation() throws Exception {
		String source = unformattedCode("@Annotation new{System.out.println(a)}");
		String expected = formattedCode(
				"	@Annotation new {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void annotations() throws Exception {
		String source = unformattedCode("@Annotation@Beta new{System.out.println(a)}");
		String expected = formattedCode(
				"	@Annotation @Beta new {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

	@Test
	public void annotationValue() throws Exception {
		String source = unformattedCode("@SuppressWarnings(\"name\")new{System.out.println(a)}");
		String expected = formattedCode(
				"	@SuppressWarnings(\"name\") new {",
				"		System.out.println(a)",
				"	}");
		assertFormatted(source, expected);
	}

}