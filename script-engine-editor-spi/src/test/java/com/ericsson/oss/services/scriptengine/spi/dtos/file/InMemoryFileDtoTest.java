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
 * Tests {@link InMemoryFileDto}.
 *
 */
public class InMemoryFileDtoTest {
    
    private static final String FILENAME = "file1.xml";
    private static final byte[] FILECONTENTS = new byte[0];

 
    /**
     * Tests constructed fileContents are successfully returned.
     */
    @Test
    public void testGetFileContents() {
        final InMemoryFileDto dto = new InMemoryFileDto(FILECONTENTS, FILENAME);
        assertEquals(FILECONTENTS, dto.getFileContents());
    }
}
