package com.ericsson.oss.services.cm.scriptengine.ejb.service;

import com.ericsson.oss.services.scriptengine.api.CommandStatus;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.Future;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandStatusContainerTest {
    final String INVALID_TERMINAL_ID_KEY = "badKey";
    final String TERMINAL_ID = "someterminalId";

    @Mock
    Future<CommandResponseDto> commandResponseDtoFutureMock;
    CommandStatusContainer objectUnderTest = new CommandStatusContainer();

    Command command;

    @Before
    public void setup(){
        command = new Command("","");
        objectUnderTest.addRunningCommandState(TERMINAL_ID, command, commandResponseDtoFutureMock);
    }

    @Test
    public void isCommandManagedForTerminalReturnsTrueForValidTerminalId(){
        boolean managed =  objectUnderTest.isCommandManagedForTerminal(TERMINAL_ID);
        assertTrue(managed);
    }

    @Test
    public void isCommandManagedForTerminalReturnsFalseForInvalidTerminalId(){
        boolean managed =  objectUnderTest.isCommandManagedForTerminal(INVALID_TERMINAL_ID_KEY);
        assertFalse(managed);
    }

    @Test
    public void getCommandStatusReturnsNullIfNoCommandExists(){
        assertNull(objectUnderTest.getCommandStatus(INVALID_TERMINAL_ID_KEY));
    }

    @Test
    public void canGetCommandManaged(){
        Assert.assertEquals(command, objectUnderTest.getCommand(TERMINAL_ID));
    }

    @Test
    public void isCommandStillRunningReturnsTrueWhenCommandIsNotDone(){
        when(commandResponseDtoFutureMock.isDone()).thenReturn(false);
        boolean isCommandStillRunning  = objectUnderTest.isCommandRunning(TERMINAL_ID);
        assertTrue(isCommandStillRunning);
    }

    @Test
    public void isCommandStillRunningReturnsTrueWhenCommandIsNotDoneAndNotCancelled(){
        when(commandResponseDtoFutureMock.isDone()).thenReturn(false);
        when(commandResponseDtoFutureMock.isCancelled()).thenReturn(false);
        boolean isCommandStillRunning  = objectUnderTest.isCommandRunning(TERMINAL_ID);
        assertTrue(isCommandStillRunning);
    }

    @Test
    public void isCommandStillRunningReturnsFalseWhenCommandIsDone(){
        when(commandResponseDtoFutureMock.isDone()).thenReturn(true);
        boolean isCommandStillRunning  = objectUnderTest.isCommandRunning(TERMINAL_ID);
        assertFalse(isCommandStillRunning);
    }


    @Test
    public void isCommandStillRunningReturnsFalseWhenCommandIsCancelled(){
        when(commandResponseDtoFutureMock.isDone()).thenReturn(false);
        when(commandResponseDtoFutureMock.isCancelled()).thenReturn(true);
        boolean isCommandStillRunning  = objectUnderTest.isCommandRunning(TERMINAL_ID);
        assertFalse(isCommandStillRunning);
    }

    @Test
    public void canAddRunningCommandStateAndGetRunningCommandAndCommandStatus(){
        CommandStatus commandStatus = objectUnderTest.addRunningCommandState(TERMINAL_ID, command, commandResponseDtoFutureMock);
        assertEquals(CommandStatus.RUNNING, commandStatus);
        assertEquals(CommandStatus.RUNNING, objectUnderTest.getCommandStatus(TERMINAL_ID));
        assertEquals(commandResponseDtoFutureMock, objectUnderTest.getExecutingCommand(TERMINAL_ID));
    }

    @Test
    public void canUpdateCommandStatus(){
        objectUnderTest.updateCommandStatus(TERMINAL_ID, CommandStatus.COMPLETE);
        assertEquals(CommandStatus.COMPLETE, objectUnderTest.getCommandStatus(TERMINAL_ID));
    }

    @Test
    public void whenCommandIsRemovedItIsNoLongerManaged(){
        assertEquals(command, objectUnderTest.removeCommandForTerminal(TERMINAL_ID));
        assertFalse(objectUnderTest.isCommandManagedForTerminal(TERMINAL_ID));
    }

}
