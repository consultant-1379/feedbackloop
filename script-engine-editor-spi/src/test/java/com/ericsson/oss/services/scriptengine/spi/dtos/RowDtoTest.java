package com.ericsson.oss.services.scriptengine.spi.dtos;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class RowDtoTest {

    private final String JSON_TEST_STRING = "{\"dtoType\":\"row\",\"elements\":[{\"value\":\"val_0\",\"width\":5},{\"value\":\"val_1\",\"width\":10},{\"value\":\"val_2\",\"width\":15},{\"value\":\"val_3\",\"width\":1},{\"value\":null,\"width\":1}]}";
    private final RowCell[] elements = {new RowCell("val_0",5),new RowCell("val_1",10),new RowCell("val_2",15),new RowCell("val_3",1),new RowCell(null,1)};
    private RowDto objectUnderTest;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp(){
        RowCell[] testElements = Arrays.copyOf(elements, elements.length);
        objectUnderTest = new RowDto(Arrays.asList(testElements));
    }

    @Test
    public  void canCreateRowDto(){
        assertNotNull("RowDto can not be created", objectUnderTest);
    }

    @Test
    public void canGetRowElements(){
        List<RowCell> rowElements = objectUnderTest.getElements();
        assertTestElements(rowElements);
    }

    @Test
    public void canSerializeToJson() throws IOException {
        String objectUnderTestJson = objectMapper.writeValueAsString(objectUnderTest);

        assertEquals(JSON_TEST_STRING,objectUnderTestJson);
    }

    @Test
    public void canDeserializeFromJson() throws IOException {
        RowDto deserializedRowDto = objectMapper.readValue(JSON_TEST_STRING, RowDto.class);
        assertTestElements(deserializedRowDto.getElements());
    }

    @Test
     public void equalsReturnsTrueWhenComparingRow(){
        RowCell[] testElements = Arrays.copyOf(elements, elements.length);
        RowDto otherRow = new RowDto(Arrays.asList(testElements));
        assertTrue("rows should be equivalent", objectUnderTest.equals(otherRow));
        assertTrue("rows should be equivalent", otherRow.equals(objectUnderTest));
     }

    @Test
    public void equalsReturnsTrueWhenComparingDeserializedRow() throws IOException {
        RowDto otherRow = objectMapper.readValue(JSON_TEST_STRING, RowDto.class);
        assertTrue("rows should be equivalent", objectUnderTest.equals(otherRow));
        assertTrue("rows should be equivalent", otherRow.equals(objectUnderTest));
    }

    @Test
    public void equalsReturnsFalseWhenComparingRowWithIncorrectOrderOfElements(){
        List<RowCell> testElements = Arrays.asList(Arrays.copyOf(elements, elements.length));
        Collections.reverse(testElements);
        RowDto otherRow = new RowDto(testElements);
        assertFalse("rows should be equivalent", objectUnderTest.equals(otherRow));
        assertFalse("rows should be equivalent", otherRow.equals(objectUnderTest));
    }

    @Test
    public void canCallToString(){
        Assert.assertEquals("[value:val_0;width:5, value:val_1;width:10, value:val_2;width:15, value:val_3;width:1, value:null;width:1]",objectUnderTest.toString());
    }

    @Test
    public void coverageTestForHash(){
        RowCell[] testElements = Arrays.copyOf(elements, elements.length);
        RowDto otherRow = new RowDto(Arrays.asList(testElements));
        assertEquals(otherRow.hashCode(),objectUnderTest.hashCode());
    }
    private void assertTestElements(List<RowCell> rowElements) {
        assertEquals(elements.length, rowElements.size());
        for(int i=0; i<elements.length; i++){
            assertEquals(elements[i].getWidth(), rowElements.get(i).getWidth());
            assertEquals(elements[i].getValue(), rowElements.get(i).getValue());
        }
    }
}
