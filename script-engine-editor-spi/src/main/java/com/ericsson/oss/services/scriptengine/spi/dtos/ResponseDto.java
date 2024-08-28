package com.ericsson.oss.services.scriptengine.spi.dtos;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Represents the response for a request
 */
public class ResponseDto extends AbstractDto implements Serializable{

    private static final long serialVersionUID = 2663983954286748101L;
    private final List<AbstractDto> elements;

    /**
     *  Creates an instance of ResponseDto
     * @param entities The weighted DTO's which represent the response
     */
    @JsonCreator
    public ResponseDto(@JsonProperty("elements") @NotNull final List<AbstractDto> entities) {
        this.elements = new ArrayList<>(entities);
    }

    /**
     *
     * @return the elemens which represent the response
     */
    public List<AbstractDto> getElements() {
        return elements;
    }


    @Override
    public String toString(){
        return elements.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ResponseDto that = (ResponseDto) o;

        if (!elements.equals(that.elements)){
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return elements.hashCode();
    }
}
