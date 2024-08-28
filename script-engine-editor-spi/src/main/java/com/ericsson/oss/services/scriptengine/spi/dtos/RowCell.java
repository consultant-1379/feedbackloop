package com.ericsson.oss.services.scriptengine.spi.dtos;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class RowCell implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String value;
    private final Number width;
    private static final String TO_STRING_VALUE_FIELD ="value:";
    private static final String TO_STRING_FIELD_SEPERATOR =";";
    private static final String TO_STRING_WIDTH_FIELD ="width:";

    /**
     * Create an instance of a RowCell using the value of the cell and the width of the cell
     * @param value
     * @param width
     */
    public RowCell(@JsonProperty("value") final String value, @JsonProperty("width") final Number width) {
        this.value = value;
        this.width = width;
    }

    /**
     *
     * @return the value of the cell
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @return return the width the cell should occupy
     */
    public int getWidth() {
        return width.intValue();
    }

    @Override
    public String toString(){
        return TO_STRING_VALUE_FIELD +value+ TO_STRING_FIELD_SEPERATOR + TO_STRING_WIDTH_FIELD +width;
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

        final RowCell rowCell = (RowCell) o;

        if (!width.equals(rowCell.width)){
            return false;
        }
        if (value != null ? !value.equals(rowCell.value) : rowCell.value != null){
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + width.intValue();
        return result;
    }
}
