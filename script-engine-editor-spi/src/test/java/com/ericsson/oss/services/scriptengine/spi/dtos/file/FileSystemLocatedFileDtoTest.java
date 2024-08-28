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
 * Tests {@link FileSystemLocatedFileDto}.
 *
 */
public class FileSystemLocatedFileDtoTest {
    
    private static final String FILENAME = "file1.xml";
    private static final String FILEPATH = "/tmp/" + FILENAME;

 
    /**
     * Tests constructed filePath is successfully returned.
     */
    @Test
    public void testGetFilePath() {
        final FileSystemLocatedFileDto dto = new FileSystemLocatedFileDto(FILEPATH, FILENAME);
        assertEquals(FILEPATH, dto.getFilePath());
    }
}
