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

import org.junit.Test;

/**
 * Tests {@link FileDownloadResponseDto}.
 *
 */
@SuppressWarnings("serial")
public class FileDownloadResponseDtoTest {
    
    private static final String FILENAME = "file1.xml";
    private static final String MIMETYPE = "application/xml";

 
    /**
     * Tests constructed fileName is successfully returned.
     */
    @Test
    public void testGetFileName() {
        final FileDownloadResponseDto dto = new FileDownloadResponseDto(FILENAME) {};
        assertEquals(FILENAME, dto.getFileName());
    }
    
    /**
     * Tests the default mime type is set when not supplied in constructor.
     */
    @Test
    public void testDefaultMimeType() {
        final FileDownloadResponseDto dto = new FileDownloadResponseDto(FILENAME) {};
        assertEquals("application/octet-stream", dto.getMimeType());
    }
    
    /**
     * Tests the specified mime type is successfuly returned.
     */
    @Test
    public void testGetSpecifiedMimeType() {
        final FileDownloadResponseDto dto = new FileDownloadResponseDto(FILENAME, MIMETYPE) {};
        assertEquals(MIMETYPE, dto.getMimeType());
    }
}
