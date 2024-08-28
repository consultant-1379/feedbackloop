package com.ericsson.oss.services.scriptengine.rest.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import com.ericsson.oss.itpf.sdk.core.classic.ServiceFinderBean;
import com.ericsson.oss.itpf.sdk.resources.Resource;
import com.ericsson.oss.itpf.sdk.resources.Resources;
import com.ericsson.oss.services.cm.scriptengine.error.ErrorHandlerImpl;
import com.ericsson.oss.services.scriptengine.spi.FileDownloadHandler;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.FileDownloadResponseDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.FileSystemLocatedFileDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.InMemoryFileDto;

/**
 * {@link FileRestResource} implementation.
 *
 * @since 1.6.2
 */
public class FileRestResourceBean implements FileRestResource {

    @Inject
    private ErrorHandlerImpl errorHandler;

    private ServiceFinderBean serviceFinder;

    private static final int BUFFER_SIZE = 1024;

    @PostConstruct
    public void init() {
        serviceFinder = new ServiceFinderBean();
    }


    @Override
    public Response downloadFile(final String applicationId, final String fileId) {
        Response response = null;
        try {
            final FileDownloadHandler fileDownloadHandler = serviceFinder.find(FileDownloadHandler.class, applicationId);
            final FileDownloadResponseDto fileResponseDto =  fileDownloadHandler.execute(fileId);
            if (fileResponseDto instanceof InMemoryFileDto) {
                response = downloadFromMemory((InMemoryFileDto) fileResponseDto);
            } else if (fileResponseDto instanceof FileSystemLocatedFileDto) {
                response = downloadFromFilesystem((FileSystemLocatedFileDto) fileResponseDto);
            }
        } catch (final Exception e) {
            errorHandler.createErrorMessageFromException(e);
            response = Response.serverError().build(); 
        }
        return response;
    }

    private Response downloadFromMemory(final InMemoryFileDto fileDto) {
        return buildDownloadFileResponse(fileDto.getFileContents(), fileDto.getFileName(), fileDto.getMimeType());
    }

    private Response downloadFromFilesystem(final FileSystemLocatedFileDto fileDto) {
        Response response = null;
        final Resource resource = Resources.getFileSystemResource(fileDto.getFilePath());
        if (resource.exists()) {
            final StreamingOutput outputStream = new ResourceStreamingOutput(resource);
            response = buildDownloadFileResponse(outputStream, fileDto.getFileName(), fileDto.getMimeType());
        } else {
            response = Response.status(Status.NOT_FOUND).build();
        }
        return response;
    }


    private Response buildDownloadFileResponse(final Object entity, final String fileName, final String mimeType) {
        return Response.ok(entity).type(mimeType)
                .header("Content-Disposition", "attachment; filename=" + fileName).build(); 
    }

    /**
     * Response entity to stream <code>Resource</code> output.
     *
     */
    class ResourceStreamingOutput implements StreamingOutput {

        private final Resource resource;

        public ResourceStreamingOutput(final Resource resource) {
            this.resource = resource;
        }

        @Override
        public void write(final OutputStream output) throws IOException {
            try (final InputStream inputStream = resource.getInputStream()) {
                final byte [] buffer = new byte[BUFFER_SIZE];
                int bytesRead = 0;
                while((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                output.flush();            
            }
        }
    }
}
