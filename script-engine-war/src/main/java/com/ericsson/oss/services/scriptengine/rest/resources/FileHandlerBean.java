/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.scriptengine.rest.resources;

import java.io.IOException;

import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.ericsson.oss.itpf.sdk.recording.ErrorSeverity;
import com.ericsson.oss.itpf.sdk.recording.SystemRecorder;
import com.ericsson.oss.itpf.sdk.resources.Resource;
import com.ericsson.oss.itpf.sdk.resources.Resources;

@Stateless
public class FileHandlerBean {

    @Inject
    private SystemRecorder systemRecorder;

    @javax.annotation.Resource
    private EJBContext context;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void writeFile(final String absoluteFileUri, final byte[] fileData) throws IOException {
        final Resource resource = Resources.getFileSystemResource(absoluteFileUri);
        try {
            final int bytesWritten = resource.write(fileData, true);
            if (bytesWritten == 0) {
                throw new IOException("0 bytes written to file");
            }
        } catch (final IOException ioException) {
            context.setRollbackOnly();
            throw ioException;
        }

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteFile(final String filePath) {
        final Resource resource = Resources.getFileSystemResource(filePath);
        if (resource.exists()) {
            resource.delete();
        } else {
            systemRecorder.recordError("SCRIPT_ENGINE.FILE_NOT_DELETED", ErrorSeverity.ERROR, null, "script-engine",
                    "The following file: " + filePath
                            + " was not deleted from the file system as the file does not exist in the specified location.");
        }
    }
}
