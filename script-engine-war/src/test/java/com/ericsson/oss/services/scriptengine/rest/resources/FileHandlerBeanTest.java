package com.ericsson.oss.services.scriptengine.rest.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.ejb.EJBContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.sdk.recording.ErrorSeverity;
import com.ericsson.oss.itpf.sdk.recording.SystemRecorder;

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

@RunWith(MockitoJUnitRunner.class)
public class FileHandlerBeanTest {

    @Mock
    private EJBContext context;
    @Mock
    private SystemRecorder systemRecorder;

    @InjectMocks
    private FileHandlerBean objUnderTest = new FileHandlerBean();

    byte[] fileData = "some sample data".getBytes();
    byte[] emptyFileData = "".getBytes();
    String absoluteFileUri = "./test_file.txt";
    String nonExistentFileUri = "/some/unavailable/filesystem/test.txt";

    @Test
    public void canWriteNewFileAndDeleteAfterwards() throws IOException {
        objUnderTest.writeFile(absoluteFileUri, fileData);
        objUnderTest.deleteFile(absoluteFileUri);
    }

    @Test(expected = IOException.class)
    public void writingEmptyFile() throws IOException {
        doNothing().when(context).setRollbackOnly();
        objUnderTest.writeFile(absoluteFileUri, emptyFileData);
    }

    @Test
    public void errorIsRecordedWhenAttemptingToDeleteNonExistentFile() {
        objUnderTest.deleteFile(nonExistentFileUri);
        verify(systemRecorder).recordError(any(String.class), any(ErrorSeverity.class), any(String.class), any(String.class),
                any(String.class));
    }

}
