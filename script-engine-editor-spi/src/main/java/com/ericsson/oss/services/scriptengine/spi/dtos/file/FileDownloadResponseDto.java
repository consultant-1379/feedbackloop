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

import java.io.Serializable;

/**
 * The response to a file download request.
 *
 * @since 1.6.2
 */
public abstract class FileDownloadResponseDto implements Serializable {

    private static final long serialVersionUID = 2338585382639399224L;
    
    private final String fileName;
    private final String mimeType;
    
    protected static final String DEFAULT_MIME_TYPE = "application/octet-stream";
    
    /**
     * Construct File response with default mime type, application/octet-stream.
     * @param fileName the name of the file, browser will use this name as a default when saving
     */
    public FileDownloadResponseDto(final String fileName) {
        this(fileName, DEFAULT_MIME_TYPE);
    }
    

    /**
     * Construct <code>FileDownloadResponseDto</code>.
     * @param fileName the name of the file, browser will use this name as a default when saving
     * @param mimeType the mime type e.g. application/xml, used to inform browser the type of file being download
     */
    public FileDownloadResponseDto(final String fileName, final String mimeType) {
        this.fileName = fileName;
        this.mimeType = mimeType;
    }


    /**
     * Gets the default file name for saving
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }
    
    /**
     * Gets the mime type
     * @return the mime type
     */
    public String getMimeType() {
        return mimeType;
    }
}
