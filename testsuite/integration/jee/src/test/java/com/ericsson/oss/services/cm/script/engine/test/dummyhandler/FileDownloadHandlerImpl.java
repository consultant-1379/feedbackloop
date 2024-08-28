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
package com.ericsson.oss.services.cm.script.engine.test.dummyhandler;

import java.io.File;

import javax.ejb.Stateless;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceQualifier;
import com.ericsson.oss.itpf.sdk.resources.Resource;
import com.ericsson.oss.itpf.sdk.resources.Resources;
import com.ericsson.oss.services.scriptengine.spi.FileDownloadHandler;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.FileDownloadResponseDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.FileSystemLocatedFileDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.InMemoryFileDto;

/**
 * {@link FileDownloadHandler} implementation for arquillian tests.
 *
 */
@Stateless
@EServiceQualifier("FileDownloadHandlerImpl")
public class FileDownloadHandlerImpl implements FileDownloadHandler {
    
    private static final String FILE_NAME = "tmpFile.txt";
    private static final String FILE_PATH = System.getProperty("java.io.tmpdir") + File.separator + FILE_NAME;
    public static final String FILE_CONTENTS = "hello";


    /* (non-Javadoc)
     * @see com.ericsson.oss.services.scriptengine.spi.FileDownloadHandler#execute(java.lang.String)
     */
    @Override
    public FileDownloadResponseDto execute(final String fileId) {
        final Resource resource = createTempFile();
        if (fileId.contains("inMemory")) {
            return new InMemoryFileDto(resource.getBytes(), FILE_NAME, "text/plain");
        } else {
            return new FileSystemLocatedFileDto(FILE_PATH, FILE_NAME, "text/plain");
        }
    }


    private Resource createTempFile() {
        final Resource resource = Resources.getFileSystemResource(FILE_PATH);
        if (!resource.exists()) {
            resource.write(FILE_CONTENTS.getBytes(), false);
        }
        return resource;
    }

}
