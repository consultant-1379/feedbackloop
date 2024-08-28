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


/**
 * The response to a file download request which provides the file to be downloaded in-memory.
 *
 * @since 1.6.2
 */
public class InMemoryFileDto extends FileDownloadResponseDto {
    

    private static final long serialVersionUID = -6548597771128142852L;
    
    private final byte[] fileContents;


    /**
     * Construct File response with default mime type, application/octet-stream.
     * @param fileContents the file contents to be sent to the client
     * @param fileName the name of the file, browser will use this name as a default when saving
     */
    public InMemoryFileDto(final byte[] fileContents, final String fileName) {
        this(fileContents, fileName, DEFAULT_MIME_TYPE);
    }
    

    /**
     * Construct File response
     * @param fileContents the file contents to be sent to the client
     * @param fileName the name of the file, browser will use this name as a default when saving
     * @param mimeType the mime type e.g. application/xml, used to inform browser the type of file being download
     */
    public InMemoryFileDto(final byte[] fileContents, final String fileName, final String mimeType) {
       super(fileName, mimeType);
       this.fileContents = fileContents;
    }


    /**
     * Gets the file contents
     * @return the file contents
     */
    public byte[] getFileContents() {
        return fileContents;
    }
}
