package com.ericsson.oss.services.scriptengine.spi.dtos;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RowDto extends AbstractDto{

    private static final long serialVersionUID = 1L;
    private final List<RowCell> elements;

    /**
     * Create a new instance of a RowDto given a list of RowCell's
     * @param elements
     */
    public RowDto(@JsonProperty("elements") @NotNull final List<RowCell> elements) {
        this.elements = new ArrayList<>(elements);
    }

    /**
     *
     * @return the RowCells which make up the row
     */
    public List<RowCell> getElements() {
        return Collections.unmodifiableList(elements);
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
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        final RowDto rowDto = (RowDto) o;

        if (!elements.equals(rowDto.elements)){
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
