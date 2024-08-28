package com.ericsson.oss.services.scriptengine.spi.dtos;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.oss.services.scriptengine.spi.utils.TableBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SdkExamples {

    @Test
    public void singleLine(){
        List<AbstractDto> elements = new ArrayList<>();
        elements.add(new LineDto("test content"));
        ResponseDto responseDto = new ResponseDto(elements);
        CommandResponseDto commandResponseDto = new CommandResponseDto(responseDto);
        assertEquals(commandResponseDto.getResponseDto().getElements(), elements);
    }

    @Test
    public void addMultipleLinesWithSpacing(){
        List<AbstractDto> elements = new ArrayList<>();
        elements.add(new LineDto("test content line 1"));
        elements.add(new LineDto("test content line 2"));
        elements.add(new LineDto("test content line 3"));
        elements.add(new LineDto());
        elements.add(new LineDto("test content line 4"));
        elements.add(new LineDto("test content line 5"));
        elements.add(new LineDto());

        ResponseDto responseDto = new ResponseDto(elements);
        CommandResponseDto commandResponseDto = new CommandResponseDto(responseDto);
        assertEquals(commandResponseDto.getResponseDto().getElements(), elements);
    }

    @Test
    public void addTable(){
        List<AbstractDto> elements = new ArrayList<>();

        TableBuilder tableBuilder = new TableBuilder("test title");
        tableBuilder.withHeader(0, "test header 1");
        tableBuilder.withHeader(1, "test header 2");
        tableBuilder.withHeader(2, "test header 3");
        tableBuilder.withCell(0, 0, "test cell 1 1");
        tableBuilder.withCell(0, 1, "test cell 1 2");
        tableBuilder.withCell(0, 2, "test cell 1 3");
        tableBuilder.withCell(1, 0, "test cell 2 1");
        tableBuilder.withCell(1, 1, "test long cell 2 2");
        tableBuilder.withCell(1, 2, "test cell 2 3");

        elements.add(new LineDto());
        elements.add(new LineDto("test some line value"));

        elements.addAll(tableBuilder.build());

        ResponseDto responseDto = new ResponseDto(elements);
        CommandResponseDto commandResponseDto = new CommandResponseDto(responseDto);
        assertEquals(commandResponseDto.getResponseDto().getElements(), elements);
    }

    @Test
    public void addErrorCode(){
        List<AbstractDto> elements = new ArrayList<>();
        elements.add(new LineDto("test content"));
        ResponseDto responseDto = new ResponseDto(elements);
        CommandResponseDto commandResponseDto = new CommandResponseDto(responseDto);

        commandResponseDto.setErrorCode(1234);
        commandResponseDto.setStatusMessage("test status");
        commandResponseDto.setSolution("test solution");
        //The error code and solution will only be added to the response if  .addErrorLines() is called
        commandResponseDto.addErrorLines();

        List<AbstractDto> commandResposne = commandResponseDto.getResponseDto().getElements();
        assertEquals(((LineDto) commandResposne.get(commandResposne.size()-1)).getValue(), "Suggested Solution : test solution");
        assertEquals(((LineDto) commandResposne.get(commandResposne.size()-2)).getValue(), "Error 1234 : test status");
    }

}
