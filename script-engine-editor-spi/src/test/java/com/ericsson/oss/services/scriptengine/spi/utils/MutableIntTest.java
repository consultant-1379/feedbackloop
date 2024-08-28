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
package com.ericsson.oss.services.scriptengine.spi.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MutableIntTest {
	MutableInt objUnderTest;

	@Before
	public void canCreateCommand() {
		objUnderTest = new MutableInt(5);
	}

	@Test
	public void willEqualIfMutableIntIsComparedWithItself() {
		assertTrue(objUnderTest.equals(objUnderTest));
	}

	@Test
	public void equalsReturnFalseIfOtherObjectIsNull() {
		Object other = null;
		assertFalse(objUnderTest.equals(other));
	}

	@Test
	public void equalsReturnFalseIfOtherObjectIsOfOtherType() {
		Object other = "some string";
		assertFalse(objUnderTest.equals(other));
	}

	@Test
	public void equalsReturnFalseIfOtherObjectIsOfSameTypeDifferentValue() {
		Object other = new MutableInt(1);
		assertFalse(objUnderTest.equals(other));
	}

	@Test
	public void equalsReturnTrueIfOtherObjectIsOfSameTypeSameValue() {
		Object other = new MutableInt(objUnderTest.value);
		assertEquals(objUnderTest, other);
	}

	@Test
	public void equalsReturnTrueIfOtherObjectIsSubTypeNumberAndSameValue() {
		Object other = new Integer(objUnderTest.value);
		assertEquals(objUnderTest, other);
	}

	@Test
	public void equalsReturnFalseIfOtherObjectIsSubTypeNumberButDifferentValue() {
		Object other = new Integer(1);
		assertFalse(objUnderTest.equals(other));
	}

	@Test
	public void getValueReturnsValueOfNumberGivenAtInstantiation() {
		assertEquals(objUnderTest.getValue(), 5);
	}

	@Test
	public void setValueSetsValueToTheRequiredNumber() {
		objUnderTest.setValue(8);
		assertEquals(objUnderTest.getValue(), 8);
	}

	@Test
	public void intValueReturnsIntegerValueOfNumber() {
		assertEquals(objUnderTest.intValue(), 5);
	}

	@Test
	public void longValueReturnsLongValueOfNumber() {
		assertEquals(objUnderTest.longValue(), 5);
	}

	@Test
	public void doubleValueReturnsDoubleValueOfNumber() {
		assertEquals(objUnderTest.doubleValue(), 5.0, 0.01);
	}

	@Test
	public void floatValueReturnsFloatValueOfNumber() {
		assertEquals(objUnderTest.floatValue(), 5, 0.01);
	}

	@Test
	public void testHashCode() {
		assertEquals(objUnderTest.hashCode(), 5);
	}

	@Test
	public void testToString() {
		assertEquals(objUnderTest.toString(), "5");
	}
}
