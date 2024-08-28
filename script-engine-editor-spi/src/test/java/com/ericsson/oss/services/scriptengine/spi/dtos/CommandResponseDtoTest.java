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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CommandResponseDtoTest {

    CommandResponseDto objUnderTest;

    @Before
    public void canCreateCommandReponse() {
        objUnderTest = new CommandResponseDto();
        assertNotNull(objUnderTest);
    }

    @Test
    public void canSetCommand() {
        final String command = "Some command";
        objUnderTest.setCommand(command);
        assertEquals(command, objUnderTest.getCommand());
    }

    @Test
    public void testAddErrorLine() {

        List<AbstractDto> list = new ArrayList<AbstractDto>();
        ResponseDto responseDto = new ResponseDto(list);
        CommandResponseDto commandResponseDto = new CommandResponseDto("cmd", responseDto);

        commandResponseDto.addErrorLines();

        assertEquals(3, responseDto.getElements().size());
    }

    @Test
    public void testAddStatusLine() {

        List<AbstractDto> list = new ArrayList<AbstractDto>();
        ResponseDto responseDto = new ResponseDto(list);

        CommandResponseDto commandResponseDto = new CommandResponseDto(responseDto);
        commandResponseDto.addSuccessLines();

        assertEquals(0, list.size());
        assertEquals(2, responseDto.getElements().size());
    }

    @Test
    public void canSetResponseDto() {
    	
    	TestDto testDto1 = new TestDto(new ArrayList<AbstractDto>());
    	CommandResponseDto commandResponseDto = new CommandResponseDto("test cmd");
    	commandResponseDto.setResponseDto(testDto1);
    	
    	assertEquals(testDto1, commandResponseDto.getResponseDto());
    }

    class TestDto extends ResponseDto {

		private static final long serialVersionUID = 1L;

		public TestDto(List<AbstractDto> entities) {
            super(entities);
        }
    }

}