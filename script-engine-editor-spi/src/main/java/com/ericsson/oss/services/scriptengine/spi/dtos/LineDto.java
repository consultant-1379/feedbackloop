package com.ericsson.oss.services.scriptengine.spi.dtos;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown=true)
public class LineDto extends AbstractDto implements Serializable{

    private static final long serialVersionUID = -6095563213160791835L;
    final String value;

    /**
     * Creates a new LineDto with a specified String value (can be null)
     * @param value
     */
    public LineDto(@JsonProperty("value")final String value) {
        this.value = value;
    }

    /**
     * Creates a new LineDto with value of null
     */
    public LineDto() {
        value = null;
    }

    /**
     * Returns the String value of the LineDto can be null
     * @return
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString(){
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        final LineDto lineDto = (LineDto) o;

        if (value != null ? !value.equals(lineDto.value) : lineDto.value != null){
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
