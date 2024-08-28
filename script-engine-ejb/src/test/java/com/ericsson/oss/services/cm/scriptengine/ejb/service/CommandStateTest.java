package com.ericsson.oss.services.cm.scriptengine.ejb.service;

import com.ericsson.oss.services.scriptengine.api.CommandStatus;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CommandStateTest {
    @Mock
    Future<CommandResponseDto> commandResponseDtoFutureMock;
    Command command;
    String EXAMPLE_CONTEXT = "commandContext";
    String EXAMPLE_COMMAND = "example command";
    final Map<String, Object> EXAMPLE_PROPERTIES = new HashMap<String, Object>();
    CommandState objectUnderTest;

    @Before
    public void setup(){
        command = new Command(EXAMPLE_CONTEXT,EXAMPLE_COMMAND,EXAMPLE_PROPERTIES);
        objectUnderTest = new CommandState(command, commandResponseDtoFutureMock, CommandStatus.RUNNING);
    }

    @Test
    public void canGetCommandStatus(){
        assertEquals(CommandStatus.RUNNING, objectUnderTest.getCommandStatus());

    }
    @Test
    public void canSetCommandStatus(){
        objectUnderTest.setCommandStatus(CommandStatus.COMPLETE);
        assertEquals(CommandStatus.COMPLETE, objectUnderTest.getCommandStatus());
    }
    @Test
    public void canGetFuture(){
        assertEquals(commandResponseDtoFutureMock, objectUnderTest.getCommandResponseDtoFuture());
    }
    @Test
    public void canGetCommand(){
        assertEquals(command, objectUnderTest.getCommand());
    }

}
