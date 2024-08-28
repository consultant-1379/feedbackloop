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
package com.ericsson.oss.services.scriptengine.spi.dtos.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link FileDownloadRequestDto}.
 *
 */
public class FileDownloadRequestDtoTest {
    
    private static final String APPLICATIONID = "appId";
    private static final String FILEID = "fileId";
    public static final String POSITIVE_FILEDOWNLOAD_REQUEST_DTO_JSON_OBJECT = "{\"dtoType\":\"fileDownload\",\"applicationId\":\"ap\",\"fileId\":\"1412872400560_node2A_InitialArtifacts.zip\",\"dtoName\":\"null\"}";
    private final FileDownloadRequestDto dto = new FileDownloadRequestDto(APPLICATIONID,FILEID);

    ObjectMapper objectMapper =  new ObjectMapper();;

    /**
     * Tests constructed application id is successfully returned.
     */
    @Test
    public void testGetApplicationId() {
        assertEquals(APPLICATIONID, dto.getApplicationId());
    }
    
    /**
     * Tests constructed file id is successfully returned.
     */
    @Test
    public void testGetFileId() {
        assertEquals(FILEID, dto.getFileId());
    } 
    
    /**
     * Tests that the FileDownloadRequest can be UnMarshalled using JSON objectMapper
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @Test
    public void testUnMarshallingJsonMapping() throws JsonParseException, JsonMappingException, IOException{
        final String fileId = "1412872400560_node2A_InitialArtifacts.zip";
        final String applicationId = "ap";
        final FileDownloadRequestDto fileDownloadRequestDto = (FileDownloadRequestDto)objectMapper.readValue(POSITIVE_FILEDOWNLOAD_REQUEST_DTO_JSON_OBJECT, FileDownloadRequestDto.class);
        assertEquals(applicationId,fileDownloadRequestDto.getApplicationId());
        assertEquals(fileId,fileDownloadRequestDto.getFileId());
    }
    
    /**
     * Tests that the FileDownloadRequest when Marshalled to JSON Object that its in the correct format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @Test 
    public void testMarshallingJsonMapping() throws JsonGenerationException, JsonMappingException, IOException{
        final String fileId = "1412872400560_node2A_InitialArtifacts.zip";
        final String applicationId = "ap";
        final FileDownloadRequestDto fileDownloadRequestDto = new FileDownloadRequestDto(applicationId, fileId);
        fileDownloadRequestDto.setDtoName("null");
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String JsonString = objectWriter.writeValueAsString(fileDownloadRequestDto);
        assertEquals(JsonString,POSITIVE_FILEDOWNLOAD_REQUEST_DTO_JSON_OBJECT);
    }
    
    @Test 
    public void testIsNotCacheable() {
    	Assert.assertFalse(dto.isCacheable());
    }

    @Test
    public void testEquals(){
        //Not equals
        final FileDownloadRequestDto fileDownloadRequestDtpDiffApplicationId = new FileDownloadRequestDto("someOtherValue",FILEID);
        assertFalse(dto.equals(fileDownloadRequestDtpDiffApplicationId));
        assertFalse(fileDownloadRequestDtpDiffApplicationId.equals(dto));

        final FileDownloadRequestDto fileDownloadRequestDtoDiffFiledId = new FileDownloadRequestDto(APPLICATIONID,"someOtherValue");
        assertFalse(dto.equals(fileDownloadRequestDtoDiffFiledId));
        assertFalse(fileDownloadRequestDtoDiffFiledId.equals(dto));

        //Equasl
        final FileDownloadRequestDto fileDownloadRequestDto = new FileDownloadRequestDto(APPLICATIONID,FILEID);
        assertTrue(fileDownloadRequestDto.equals(dto));
        assertTrue(dto.equals(fileDownloadRequestDto));
        assertTrue(dto.equals(dto));

    }

    @Test
    public void testHashCode(){
        final FileDownloadRequestDto fileDownloadRequestDto = new FileDownloadRequestDto(APPLICATIONID,FILEID);
        assertEquals(fileDownloadRequestDto.hashCode(), dto.hashCode());
    }
}
