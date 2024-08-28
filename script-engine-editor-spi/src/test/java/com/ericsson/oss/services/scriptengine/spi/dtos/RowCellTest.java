package com.ericsson.oss.services.scriptengine.spi.dtos;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RowCellTest {

    RowCell objectUnderTest;
    final String testValue = "testVal";
    final int testSize = 44;

    @Before
    public void setUp(){
        objectUnderTest = new RowCell(testValue, testSize);
    }

    @Test
    public void canCreateCell() {
        assertNotNull("RowCell should not be null",objectUnderTest);
    }

    @Test
    public void canCreateCellWithNullValue(){
        objectUnderTest = new RowCell(null, testSize);
        assertNotNull("RowCell should not be null",objectUnderTest);
        assertNull("RowCell value should be null", objectUnderTest.getValue());
    }

    @Test
    public void canGetValue(){
        assertEquals(testValue, objectUnderTest.getValue());
    }

    @Test
    public void canGetSize(){
        assertEquals(testSize, objectUnderTest.getWidth());
    }

    @Test
    public void equalsReturnsTrueWhenComparingEquivalentCells(){
        RowCell otherObject =  new RowCell(testValue, testSize);
        assertTrue("objects should be equivalent",objectUnderTest.equals(otherObject));
        assertTrue("objects should be equivalent", otherObject.equals(objectUnderTest));
    }

    @Test
    public void equalsReturnsFalseWhenComparingValueIsDifferent(){
        RowCell otherObject =  new RowCell("AnotherValue", testSize);
        assertFalse("objects should not be equal", objectUnderTest.equals(otherObject));
        assertFalse("objects should not be equal",otherObject.equals(objectUnderTest));
    }

    @Test
    public void equalsReturnsFalseWhenComparingValueIsDifferentType(){
        RowCell otherObject =  new RowCell(new Float(3.75).toString(), testSize);
        assertFalse("objects should be not equal",objectUnderTest.equals(otherObject));
        assertFalse("objects should be not equal", otherObject.equals(objectUnderTest));
    }
    @Test
    public void equalsReturnsFalseWhenComparingSizeIsDifferent(){
        RowCell otherObject =  new RowCell(testValue, testSize+1);
        assertFalse("objects should be not equal", objectUnderTest.equals(otherObject));
        assertFalse("objects should be not equal", otherObject.equals(objectUnderTest));
    }

    @Test
    public void coverageTestForHashCode(){
        RowCell otherObject =  new RowCell(testValue, testSize);
        assertEquals("Hashcode should be equivalent",objectUnderTest.hashCode(),otherObject.hashCode());

    }

    @Test
    public void canCallToString(){
        assertEquals("value:testVal;width:44",objectUnderTest.toString());
    }

}