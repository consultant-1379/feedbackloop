package com.ericsson.oss.services.scriptengine.rest.resources;


import com.ericsson.oss.services.scriptengine.spi.dtos.AbstractDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.LineDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.ResponseDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.FileDownloadRequestDto;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GetResponseBodyTest {
    GetResponseBody objectUnderTest;

    private static final String JSON_TEST_STRING = "{\"nonCachableDtos\":[{\"dtoType\":\"fileDownload\",\"applicationId\":\"someApplicaionId\",\"fileId\":\"someField\"}],\"responseDto\":{\"dtoType\":\"ResponseDto\",\"elements\":[{\"dtoType\":\"line\",\"value\":\"line1\"},{\"dtoType\":\"line\",\"value\":\"line2\"},{\"dtoType\":\"line\",\"value\":\"line3\"}]}}";
    ObjectMapper objectMapper = new ObjectMapper();
    List<AbstractDto> nonCachableDtoList;
    List<AbstractDto> responseDtoList;

    @Before
    public void setup(){
        nonCachableDtoList = new ArrayList();
        nonCachableDtoList.add(new FileDownloadRequestDto("someApplicaionId", "someField"));
        responseDtoList = new ArrayList();
        responseDtoList.add(new LineDto("line1"));
        responseDtoList.add(new LineDto("line2"));
        responseDtoList.add(new LineDto("line3"));
        ResponseDto responseDto = new ResponseDto(responseDtoList);

        objectUnderTest = new GetResponseBody(nonCachableDtoList, responseDto);
    }

    @Test
    public void canGetNonCachableDto(){
        assertEquals(nonCachableDtoList, objectUnderTest.getNonCachableDtos());
    }

    @Test
    public void  canGetResponseDto(){
        assertEquals(new ResponseDto(responseDtoList), objectUnderTest.getResponseDto());

    }

    @Test
    public void canSerializeToJsonAsExpected() throws IOException {
        String objectUnderTestJson = objectMapper.writeValueAsString(objectUnderTest);
        assertEquals(JSON_TEST_STRING, objectUnderTestJson);
    }

    @Test
    public void canDeserializeFromJsonAsExpected() throws IOException {
        GetResponseBody getResponseBody = objectMapper.readValue(JSON_TEST_STRING, GetResponseBody.class);
        assertEquals(getResponseBody, objectUnderTest);
    }

    @Test
    public void coverageTestForEqualsMethod(){

        assertFalse(objectUnderTest.equals(null));

        GetResponseBody getResponseBodyWithEmptyLists = new GetResponseBody(new ArrayList<AbstractDto>(), new ResponseDto(new ArrayList<AbstractDto>()));
        assertFalse(objectUnderTest.equals(getResponseBodyWithEmptyLists));
        assertFalse(getResponseBodyWithEmptyLists.equals(objectUnderTest));

        GetResponseBody getResponseBodyWithDifferentNonCachable = new GetResponseBody(new ArrayList<AbstractDto>(), new ResponseDto(responseDtoList));
        assertFalse(objectUnderTest.equals(getResponseBodyWithDifferentNonCachable));
        assertFalse(getResponseBodyWithDifferentNonCachable.equals(objectUnderTest));

        GetResponseBody getResponseBodyWithDifferentResponse = new GetResponseBody(nonCachableDtoList,new ResponseDto(new ArrayList<AbstractDto>()));
        assertFalse(objectUnderTest.equals(getResponseBodyWithDifferentResponse));
        assertFalse(getResponseBodyWithDifferentResponse.equals(objectUnderTest));

        GetResponseBody getResponseBodyEqualValue = new GetResponseBody(nonCachableDtoList, new ResponseDto(responseDtoList));
        assertTrue(objectUnderTest.equals(getResponseBodyEqualValue));
        assertTrue(getResponseBodyEqualValue.equals(objectUnderTest));

        assertTrue(objectUnderTest.equals(objectUnderTest));

    }

    @Test
    public void coverageTestForHashCode(){
        GetResponseBody getResponseBodyEqualValue = new GetResponseBody(nonCachableDtoList, new ResponseDto(responseDtoList));
        assertEquals(objectUnderTest.hashCode(), getResponseBodyEqualValue.hashCode());
    }
}
