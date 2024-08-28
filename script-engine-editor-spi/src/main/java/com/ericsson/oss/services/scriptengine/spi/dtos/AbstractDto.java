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
package com.ericsson.oss.services.scriptengine.spi.dtos;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.ericsson.oss.services.scriptengine.spi.dtos.file.FileDownloadRequestDto;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "dtoType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommandDto.class, name = "command"),
        @JsonSubTypes.Type(value = FileDownloadRequestDto.class, name = "fileDownload"),
        @JsonSubTypes.Type(value = HeaderRowDto.class, name = "headerRow"),
        @JsonSubTypes.Type(value = LineDto.class, name = "line"),
        @JsonSubTypes.Type(value = RowDto.class, name = "row")})
public abstract class AbstractDto implements Serializable {
    private static final long serialVersionUID = -7228515539515312608L;

    @JsonSerialize(include=Inclusion.NON_NULL)
    private String dtoName;
    /**
     * @return the name
     */
    public String getDtoName() {
        return dtoName;
    }

    /**
     * @param dtoName
     *        the name to set
     */
    public void setDtoName(final String dtoName) {
        this.dtoName = dtoName;
    }

    /**
     * @return if the DTO is cacheable. By default is true
     */
    @JsonIgnore
    public boolean isCacheable() {
        return true;
    }

}