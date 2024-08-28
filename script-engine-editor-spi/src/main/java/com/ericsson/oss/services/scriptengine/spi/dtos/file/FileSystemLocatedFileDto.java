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
 * The response to a file download request which provides the location of the file to be download
 * on the shared file system. 
 * <p>
 * Providing the location of the file allows the file to be streamed to the client. The whole file does 
 * not need to be read into memory and as such is recommended for downloading large files.
 *
 * @since 1.6.2
 */
public class FileSystemLocatedFileDto extends FileDownloadResponseDto {
    
    private static final long serialVersionUID = -3024137294029400878L;
    
    private final String filePath;


    /**
     * Construct File response with default mime type, application/octet-stream.
     * @param filePath the absolute path to the file on the shared file system
     * @param fileName the name of the file, browser will use this name as a default when saving
     */
    public FileSystemLocatedFileDto(final String filePath, final String fileName) {
        this(filePath, fileName, DEFAULT_MIME_TYPE);
    }
    

    /**
     * Construct File response
     * @param filePath the absolute path to the file on the shared file system
     * @param fileName the name of the file, browser will use this name as a default when saving
     * @param mimeType the mime type e.g. application/xml, used to inform browser the type of file being download
     */
    public FileSystemLocatedFileDto(final String filePath, final String fileName, final String mimeType) {
        super(fileName, mimeType);
        this.filePath = filePath;
    }


    public String getFilePath() {
        return filePath;
    } 
}
