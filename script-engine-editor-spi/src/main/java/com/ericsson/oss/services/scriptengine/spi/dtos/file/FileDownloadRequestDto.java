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

import org.codehaus.jackson.annotate.JsonProperty;

import com.ericsson.oss.services.scriptengine.spi.dtos.AbstractDto;


/**
 * Command result to be returned in order to initiate file download.
 *
 * @since 1.6.2
 */
public class FileDownloadRequestDto extends AbstractDto {

    private static final long serialVersionUID = 1964814194079776848L;
        
    private final String fileId;
    
    private final String applicationId;
    
    /**
     * Construct <code>FileDownloadRequestDto</code>.
     * 
     * @param applicationId identifies application specific <code>FileDownloadHandler</code> implementation to handle the file download request, corresponds to <code>EServiceQualifer</code>
     * @param fileId the application specific file id to identify a file

     * @throws IllegalArgumentException if fileId or applicationId are null or empty
     */
    public FileDownloadRequestDto(@JsonProperty("applicationId")final String applicationId, @JsonProperty("fileId")final String fileId) {
        this.fileId = fileId;
        this.applicationId = applicationId;
    }


    /**
     * Gets the file id.
     * @return the file id
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * Gets the file URI.
     * @return the applciation id
     */
    public String getApplicationId() {
        return applicationId;
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        final FileDownloadRequestDto that = (FileDownloadRequestDto) o;

        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null){
            return false;
        }
        if (fileId != null ? !fileId.equals(that.fileId) : that.fileId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = fileId != null ? fileId.hashCode() : 0;
        result = 31 * result + (applicationId != null ? applicationId.hashCode() : 0);
        return result;
    }
}
