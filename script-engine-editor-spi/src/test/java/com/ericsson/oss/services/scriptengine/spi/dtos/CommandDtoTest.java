package com.ericsson.oss.services.scriptengine.spi.dtos;


import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CommandDtoTest {
    private static final String JSON_TEST_STRING = "{\"dtoType\":\"command\",\"value\":\"Some Command\"}";
    CommandDto objectUnderTest;
    String testCommand = "Some Command";
    ObjectMapper objectMapper = new ObjectMapper();
    
    @Before
    public void setup(){
        objectUnderTest = new CommandDto(testCommand);
    }

    @Test
    public void canGetCommand(){
        assertEquals(testCommand, objectUnderTest.getValue());
    }

    @Test
    public void canSerializeToJsonAsExpected() throws IOException {
        String objectUnderTestJson = objectMapper.writeValueAsString(objectUnderTest);
        assertEquals(JSON_TEST_STRING, objectUnderTestJson);
    }

    @Test
    public void canDeserialize() throws IOException {
        LineDto deserializeLineDto = objectMapper.readValue(JSON_TEST_STRING,CommandDto.class);
        assertEquals(objectUnderTest,deserializeLineDto);
    }

}
