package com.ericsson.oss.services.scriptengine.spi.dtos;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CommandDto extends LineDto {

    private static final long serialVersionUID = 1L;

    public CommandDto(@JsonProperty("value")final String command) {
        super(command);
    }
}
