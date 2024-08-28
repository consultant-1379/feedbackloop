package com.ericsson.oss.services.scriptengine.spi.dtos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.oss.services.scriptengine.spi.utils.TableBuilder;
public class ResponseDtoTest {

    ResponseDto objectUnderTest;
    LineDto testLine;
    CommandDto testCommand;
    List<AbstractDto> dtos;

    ObjectMapper objectMapper = new ObjectMapper();

    private final String JSON_TEST_STRING = "{\"dtoType\":\"ResponseDto\",\"elements\":[{\"dtoType\":\"headerRow\",\"elements\":[{\"value\":\"colKey\",\"width\":8}],\"title\":null},{\"dtoType\":\"row\",\"elements\":[{\"value\":\"someVal1\",\"width\":8}]},{\"dtoType\":\"line\",\"value\":\"SomeValue\"},{\"dtoType\":\"command\",\"value\":\"commandValue\"}]}";

    @Before
    public void setUp(){
        List<RowDto> rows = new TableBuilder().withHeader(0, "colKey").withCell(0, 0, "someVal1").build();
        testCommand = new CommandDto("commandValue");
        testLine = new LineDto("SomeValue");

        dtos = new ArrayList<>();
        dtos.addAll(rows);
        dtos.add(testLine);
        dtos.add(testCommand);

        objectUnderTest = new ResponseDto(dtos);
    }

    @Test
    public void canGetElements() throws Exception {
        assertFalse(dtos == objectUnderTest.getElements());
        assertEquals(dtos, objectUnderTest.getElements());
    }

    @Test
    public void canSerializeToJsonCorrectly() throws IOException {
        String objectUnderTestJson = objectMapper.writeValueAsString(objectUnderTest);
        assertEquals(JSON_TEST_STRING, objectUnderTestJson);
    }

    @Test
    public void canDeserializeFromJsonCorrectly() throws IOException {
        ResponseDto responseDto = objectMapper.readValue(JSON_TEST_STRING, ResponseDto.class);
        List<AbstractDto> orgElements = objectUnderTest.getElements();
        List<AbstractDto> deserializedElements = responseDto.getElements();
        for(int i = 0; i < orgElements.size(); i++){
            assertEquals(orgElements.get(i),deserializedElements.get(i));
        }
    }

    @Test
    public void willEqualsIfEquivalent(){
        ResponseDto secondDto = new ResponseDto(dtos);
        assertTrue(objectUnderTest.equals(secondDto));
        assertTrue(secondDto.equals(objectUnderTest));
    }

    @Test
    public void willHaveSameHashCodeIfEquivalent(){
        ResponseDto secondDto = new ResponseDto(dtos);
        assertTrue(objectUnderTest.hashCode() == secondDto.hashCode());
    }

    @Test
    public void wllNotEqualIfNotEquivalent(){
        List<AbstractDto> secondList = new ArrayList<>(1);
        secondList.add(new LineDto("text"));
        ResponseDto responseDto = new ResponseDto(secondList);
        assertFalse(objectUnderTest.equals(responseDto));
        assertFalse(responseDto.equals(objectUnderTest));
    }
    
    @Test
    public void willBeEqualIfObjectComparedToItself (){
    	assertTrue(objectUnderTest.equals(objectUnderTest));
    }
    
    @Test
    public void willNotEqualIfObjectIsNull (){
    	assertFalse(objectUnderTest.equals(null));
    }
    
    @Test
    public void willNotEqualIfObjectIsOfDifferentClassType (){
    	assertFalse(objectUnderTest.equals("someString".getClass()));
    }

    @Test
    public void canCallToString(){
        assertEquals("[title:null [value:colKey;width:8], [value:someVal1;width:8], SomeValue, commandValue]",objectUnderTest.toString());
    }

}
