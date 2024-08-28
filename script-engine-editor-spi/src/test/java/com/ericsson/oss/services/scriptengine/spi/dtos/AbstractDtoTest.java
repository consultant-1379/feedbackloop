/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.scriptengine.spi.dtos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class AbstractDtoTest {

	AbstractDto objUnderTest;

	@Before
	public void canCreateSomethingThatExtendsAbstractDto() {
		objUnderTest = new TestDTO();
		assertNotNull(objUnderTest);
	}

	@Test
	public void canSetAndGetName() {
		final String myName = "Name of DTO";
		objUnderTest.setDtoName(myName);
		assertEquals(myName, objUnderTest.getDtoName());
	}
	@Test
	public void testIsCacheable() {
		assertEquals(true, objUnderTest.isCacheable());
	}

	class TestDTO extends AbstractDto {

	}

}
