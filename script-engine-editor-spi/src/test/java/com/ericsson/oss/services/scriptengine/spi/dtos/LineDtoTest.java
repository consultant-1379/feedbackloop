package com.ericsson.oss.services.scriptengine.spi.dtos;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
@JsonIgnoreProperties(ignoreUnknown=true)
public class LineDtoTest {

    private static final String JSON_TEST_STRING = "{\"dtoType\":\"line\",\"value\":\"some value\"}";
    ObjectMapper objectMapper = new ObjectMapper();
    public static final String TEST_VALUE = "some value";
    LineDto objectUnderTest;



    @Before
    public void setUp(){
        objectUnderTest = new LineDto(TEST_VALUE);
    }

    @Test
    public void canGetValue() throws Exception {
        assertEquals(TEST_VALUE, objectUnderTest.getValue());

    }

    @Test
    public void canEquals() throws Exception {
        LineDto secondLine = new LineDto(TEST_VALUE);
        assertTrue(secondLine.equals(objectUnderTest));
        assertTrue(objectUnderTest.equals(secondLine));
    }

    @Test
    public void willNotEquals() throws Exception {
        LineDto secondLine = new LineDto("some other value");
        assertFalse(secondLine.equals(objectUnderTest));
        assertFalse(objectUnderTest.equals(secondLine));
    }

    @Test
    public void willNotEqualsWhereValueIsNull() throws Exception {
        LineDto secondLine = new LineDto(null);
        assertFalse(secondLine.equals(objectUnderTest));
        assertFalse(objectUnderTest.equals(secondLine));
    }

    @Test
    public void canHashCode() throws Exception {
        LineDto secondLine = new LineDto(TEST_VALUE);
        assertTrue(secondLine.equals(objectUnderTest));
        assertTrue(objectUnderTest.equals(secondLine));
    }

    @Test
    public void canSerializeToJsonAsExpected() throws IOException {
        String objectUnderTestJson = objectMapper.writeValueAsString(objectUnderTest);
        assertEquals(JSON_TEST_STRING, objectUnderTestJson);
    }

    @Test
    public void canDeserialize() throws IOException {
        LineDto deserializeLineDto = objectMapper.readValue(JSON_TEST_STRING,LineDto.class);
        assertEquals(objectUnderTest,deserializeLineDto);
    }

    @Test
    public void canCallToString(){
        assertEquals(TEST_VALUE, objectUnderTest.toString());
    }

}
