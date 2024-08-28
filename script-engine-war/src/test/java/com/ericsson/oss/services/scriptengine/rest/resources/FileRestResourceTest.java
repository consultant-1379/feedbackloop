package com.ericsson.oss.services.scriptengine.rest.resources;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.sdk.core.classic.ServiceFinderBean;
import com.ericsson.oss.services.cm.scriptengine.error.ErrorHandlerImpl;
import com.ericsson.oss.services.scriptengine.spi.FileDownloadHandler;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.FileDownloadResponseDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.FileSystemLocatedFileDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.InMemoryFileDto;

/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

/**
 * Tests file resource handling.
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class FileRestResourceTest {

    private final FileDownloadResponseDto inMemoryFileDto = new InMemoryFileDto(INMEMORY_FILECONTENTS, FILENAME, MIMETYPE);
    private final FileDownloadResponseDto fileSystemFileDto = new FileSystemLocatedFileDto(TMP_FILE.getAbsolutePath(), TMP_FILE.getName(), MIMETYPE);

    private static final File TMP_FILE = new File(System.getProperty("java.io.tmpdir") + "/file1.txt");
    private static final String TMP_FILE_CONTENT = "hello";
    private static final String APPLICATIONID = "appId";
    private static final String FILEID = "fileId";
    private static final String FILENAME = TMP_FILE.getName();
    private static final String MIMETYPE = "application/xml";
    private static final byte[] INMEMORY_FILECONTENTS = new byte[0];

    @Mock
    private ServiceFinderBean serviceFinder;

    @Mock
    private ErrorHandlerImpl errorHandler;

    @Mock
    private FileDownloadHandler fileDownloadHandler;

    @InjectMocks
    private final FileRestResourceBean fileResource = new FileRestResourceBean();

    @BeforeClass
    public static void before() throws IOException {
        writeContentToFile(TMP_FILE, TMP_FILE_CONTENT);
    }

    @AfterClass
    public static void after() {
        TMP_FILE.delete();
    }

    /**
     * Tests file is successfully downloaded from memory. Checks file contents in the response are as expected.
     */
    @Test
    public void testDownloadFileFromMemory() {
        when(serviceFinder.find(FileDownloadHandler.class, APPLICATIONID)).thenReturn(fileDownloadHandler);
        doReturn(inMemoryFileDto).when(fileDownloadHandler).execute(FILEID);
        final Response response = fileResource.downloadFile(APPLICATIONID, FILEID);
        assertEquals(INMEMORY_FILECONTENTS, response.getEntity());
    }

    /**
     * Tests file is successfully downloaded from the file system. Checks the file contents in the response are as expected.
     * 
     * @throws IOException
     * @throws WebApplicationException
     */
    @Test
    public void testDownloadFileFromFileSystem() throws WebApplicationException, IOException {
        when(serviceFinder.find(FileDownloadHandler.class, APPLICATIONID)).thenReturn(fileDownloadHandler);
        doReturn(fileSystemFileDto).when(fileDownloadHandler).execute(FILEID);
        final Response response = fileResource.downloadFile(APPLICATIONID, FILEID);
        final StreamingOutput streamOutput = (StreamingOutput) response.getEntity();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        streamOutput.write(bos);
        assertEquals(TMP_FILE_CONTENT, bos.toString());
    }

    /**
     * Tests response 404 is returned when file cannot be found at the provided path.
     */
    @Test
    public void testResponse404WhenFileNotFound() {
        final FileDownloadResponseDto fileSystemFileDto = new FileSystemLocatedFileDto("/nonexistingfile.txt", "nonexistingfile.txt");
        when(serviceFinder.find(FileDownloadHandler.class, APPLICATIONID)).thenReturn(fileDownloadHandler);
        doReturn(fileSystemFileDto).when(fileDownloadHandler).execute(FILEID);
        final Response response = fileResource.downloadFile(APPLICATIONID, FILEID);
        assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    /**
     * Tests successful in-memory file download response contains expected Content-Disposition which contains a default file name and specifies that
     * the file is an attachment.
     */
    @Test
    public void testReponseForInMemoryFileDownloadContainsContentDispositionAttachment() {
        when(serviceFinder.find(FileDownloadHandler.class, APPLICATIONID)).thenReturn(fileDownloadHandler);
        doReturn(inMemoryFileDto).when(fileDownloadHandler).execute(FILEID);
        final Response response = fileResource.downloadFile(APPLICATIONID, FILEID);
        assertEquals("attachment; filename=" + FILENAME, response.getMetadata().get("Content-Disposition").iterator().next());
    }

    /**
     * Tests successful in-memory file download response contains the expected content-type.
     */
    @Test
    public void testReponseForInMemoryFileDownloadContainsFileContentType() {
        when(serviceFinder.find(FileDownloadHandler.class, APPLICATIONID)).thenReturn(fileDownloadHandler);
        doReturn(inMemoryFileDto).when(fileDownloadHandler).execute(FILEID);
        final Response response = fileResource.downloadFile(APPLICATIONID, FILEID);
        assertEquals(MIMETYPE, response.getMetadata().get("Content-Type").iterator().next());
    }

    /**
     * Tests successful filesystem download response contains the expected content-type.
     */
    @Test
    public void testReponseForFilesystemDownloadContainsFileContentType() {
        when(serviceFinder.find(FileDownloadHandler.class, APPLICATIONID)).thenReturn(fileDownloadHandler);
        doReturn(fileSystemFileDto).when(fileDownloadHandler).execute(FILEID);
        final Response response = fileResource.downloadFile(APPLICATIONID, FILEID);
        assertEquals(MIMETYPE, response.getMetadata().get("Content-Type").iterator().next());
    }

    /**
     * Tests successful filesystem download response contains expected Content-Disposition which contains a default file name and specifies that the
     * file is an attachment.
     */
    @Test
    public void testReponseForFilesystemDownloadContainsContentDispositionAttachment() {
        when(serviceFinder.find(FileDownloadHandler.class, APPLICATIONID)).thenReturn(fileDownloadHandler);
        doReturn(fileSystemFileDto).when(fileDownloadHandler).execute(FILEID);
        final Response response = fileResource.downloadFile(APPLICATIONID, FILEID);
        assertEquals("attachment; filename=" + FILENAME, response.getMetadata().get("Content-Disposition").iterator().next());
    }

    /**
     * Tests Server response error returned when unexpected error occurs during execution.
     */
    @Test
    public void testServerErrorResponseOnException() {
        doThrow(new IllegalStateException()).when(serviceFinder).find(FileDownloadHandler.class, APPLICATIONID);
        final Response response = fileResource.downloadFile(APPLICATIONID, FILEID);
        assertEquals(500, response.getStatus());
        verify(errorHandler).createErrorMessageFromException(any(Throwable.class));
    }

    private static void writeContentToFile(final File file, final String fileContent) throws IOException {
        if (file.exists()) {
            return;
        }
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(fileContent);
        }
    }

}
