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
package com.ericsson.oss.services.scriptengine.spi;

import javax.ejb.Remote;

import com.ericsson.oss.itpf.sdk.core.annotation.EService;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.FileDownloadResponseDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.FileSystemLocatedFileDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.InMemoryFileDto;

/**
 * Inteface to be implemented by an application to support file download through script-engine. 
 *
 * @since 1.6.2
 */
@Remote
@EService
public interface FileDownloadHandler {
    
    /**
     * Provide the data required to download the file identified by the supplied fileId.
     * <p>
     * The <code>FileDownloadResponseDto</code> to be returned may be influenced by both the location 
     * and size of the file being downloaded. For larger files or files that exist on the file system it is recommended
     * to use {@link FileSystemLocatedFileDto}. For smaller files which are read or created in-memory, {@link InMemoryFileDto}
     * can be used.
     * @param fileId the application specific file id
     * @return {@link FileDownloadResponseDto}
     */
    FileDownloadResponseDto execute(final String fileId);

}
