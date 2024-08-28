package com.ericsson.oss.services.scriptengine.spi.dtos;


import junit.framework.Assert;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HeaderRowDtoTest {
    HeaderRowDto objectUnderTest;
    private final RowCell[] elements = {new RowCell("val_0",5),new RowCell("val_1",10),new RowCell("val_2",15),new RowCell("val_3",1),new RowCell(null,1)};
    private final String title = "some title";

    @Before
    public void setUp(){
        RowCell[] testElements = Arrays.copyOf(elements, elements.length);
        objectUnderTest = new HeaderRowDto(Arrays.asList(testElements),title);
    }

    @Test
    public void canCreateHeaderRow(){
        assertNotNull("HeaderRowDto should not be null", objectUnderTest);
    }

    @Test
    public void canGetTitle(){
        assertEquals(title, objectUnderTest.getTitle());
    }

    @Test
    public void titleCanBeNull(){
        RowCell[] testElements = Arrays.copyOf(elements, elements.length);
        objectUnderTest = new HeaderRowDto(Arrays.asList(testElements),null);
        assertNull("title should be null", objectUnderTest.getTitle());
    }

    @Test
    public void equalsValidForEquivalentObject(){
        RowCell[] testElements = Arrays.copyOf(elements, elements.length);
        HeaderRowDto otherObject = new HeaderRowDto(Arrays.asList(testElements),title);

        assertTrue("objects should be equivalent", objectUnderTest.equals(otherObject));
        assertTrue("objects should be equivalent", otherObject.equals(objectUnderTest));
    }

    @Test
    public void equalsNotTrueForDiffernetTitle(){
        RowCell[] testElements = Arrays.copyOf(elements, elements.length);
        HeaderRowDto otherObject = new HeaderRowDto(Arrays.asList(testElements),"some other title");

        assertFalse("objects should not be equivalent", objectUnderTest.equals(otherObject));
        assertFalse("objects should not be equivalent", otherObject.equals(objectUnderTest));
    }

    @Test
    public void equalsNotTrueIfTitleIsNull(){
        RowCell[] testElements = Arrays.copyOf(elements, elements.length);
        HeaderRowDto otherObject = new HeaderRowDto(Arrays.asList(testElements),null);

        assertFalse("objects should not be equivalent", objectUnderTest.equals(otherObject));
        assertFalse("objects should not be equivalent", otherObject.equals(objectUnderTest));
    }

    @Test
    public void equalsReturnsFalseWhenComparingRowWithIncorrectOrderOfElements(){
        List<RowCell> testElements = Arrays.asList(Arrays.copyOf(elements, elements.length));
        Collections.reverse(testElements);
        RowDto otherRow = new RowDto(testElements);
        Assert.assertFalse("rows should be equilivant", objectUnderTest.equals(otherRow));
        Assert.assertFalse("rows should be equilivant", otherRow.equals(objectUnderTest));
    }

    @Test
    public void canCallToString(){
        assertEquals("title:some title [value:val_0;width:5, value:val_1;width:10, value:val_2;width:15, value:val_3;width:1, value:null;width:1]",objectUnderTest.toString());
    }
}
