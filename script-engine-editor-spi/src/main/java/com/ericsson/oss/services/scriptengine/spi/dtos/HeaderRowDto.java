package com.ericsson.oss.services.scriptengine.spi.dtos;


import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class HeaderRowDto extends RowDto{
    private static final String TO_STRING_TITLE_FIELD = "title:";
    final private String title;

    /**
     * Creates a new header row for a table. Includes the header of the table
     * @param elements
     * @param title
     */
    public HeaderRowDto(@JsonProperty("elements")@NotNull final List<RowCell> elements, @JsonProperty("title") final String title) {
        super(elements);
        this.title = title;
    }

    /**
     * @return title of the table
     */

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        if (!super.equals(o)){
            return false;
        }

        final HeaderRowDto that = (HeaderRowDto) o;

        if (title != null ? !title.equals(that.title) : that.title != null) {
            return false;
        }

        if(!super.equals(o)){
            return false;
        }

        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.title);
        return hash;
    }
    
    @Override
    public String toString(){
        return TO_STRING_TITLE_FIELD +title+" "+super.toString();
    }
}
