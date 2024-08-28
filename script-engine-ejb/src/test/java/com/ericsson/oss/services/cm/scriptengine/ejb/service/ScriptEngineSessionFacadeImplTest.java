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
package com.ericsson.oss.services.cm.scriptengine.ejb.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.inject.Inject;

import com.ericsson.oss.services.cm.scriptengine.error.ErrorManager;
import com.ericsson.oss.services.scriptengine.api.CommandStatus;
import com.ericsson.oss.services.scriptengine.spi.dtos.ResponseDto;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.AliasHandler;
import com.ericsson.oss.services.scriptengine.api.ScriptEngineSessionFacade;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

@RunWith(MockitoJUnitRunner.class)
public class ScriptEngineSessionFacadeImplTest {

    private static final String DUMMY_COMMAND = "dummy-command";
    private static final String DUMMY_CONTEXT = "dummy-context";
    public static final String TERMINAL_KEY = "someKey";
    public static final String USER_ID = "BOB";

    @Mock
    @Inject
    AliasHandler aliasHandlerMock;
    @Mock
    @Inject
    AsyncCommandService asyncCommandServiceMock;
    @Mock
    @Inject
    CommandStatusContainer commandStatusContainerMock;
    @Mock
    @Inject
    ErrorManager errorManagerMock;

    @InjectMocks
    ScriptEngineSessionFacade objUnderTest = new ScriptEngineSessionFacadeImpl();

    @Mock
    Future<CommandResponseDto> commandResponseDtoFutureMock;
    @Mock
    CommandResponseDto commandResponseDtoMock;
    @Mock
    ResponseDto responseDtoMock;
    @Mock
    InterruptedException interruptedExceptionMock;
    @Mock
    ExecutionException executionExceptionMock;
    @Mock
    AsyncContextPropagator asyncContextPropagatorMock;
    @Mock
    Command commandMock;

    private final Map<String, Object> properties = new HashMap<String, Object>();

    @Before
    public void setUp(){
        when(commandResponseDtoMock.getResponseDto()).thenReturn(responseDtoMock);
    }

    @Test
    public void addCommandForwardsCommandAndUserIdToAsyncService(){
        when(asyncContextPropagatorMock.getUserId()).thenReturn(USER_ID);
        Command command = new Command("someContext","someCommand");
        objUnderTest.addCommand(TERMINAL_KEY, command);
        verify(asyncCommandServiceMock).executeCommand(command, USER_ID);
    }

    @Test
    public void canGetCommandStateWhenCommandIsRunning(){
        when(commandStatusContainerMock.getCommandStatus(TERMINAL_KEY)).thenReturn(CommandStatus.RUNNING);
        when(commandStatusContainerMock.isCommandRunning(TERMINAL_KEY)).thenReturn(true);

        CommandStatus commandStatus = objUnderTest.getCommandStatus(TERMINAL_KEY);
        assertEquals(CommandStatus.RUNNING,commandStatus);
    }

    @Test
    public void commandStateWillUpdateFromRunningToCompleteWhenFutureCompletes(){
        when(commandStatusContainerMock.getCommandStatus(TERMINAL_KEY)).thenReturn(CommandStatus.RUNNING);
        when(commandStatusContainerMock.isCommandRunning(TERMINAL_KEY)).thenReturn(false);

        CommandStatus commandStatus = objUnderTest.getCommandStatus(TERMINAL_KEY);
        assertEquals(CommandStatus.COMPLETE,commandStatus);
    }

    @Test
    public void  commandStateWillNotUpdateFromRunningToCompleteWhenFutureHasCompleted(){
        when(commandStatusContainerMock.getCommandStatus(TERMINAL_KEY)).thenReturn(CommandStatus.RUNNING);
        when(commandStatusContainerMock.isCommandRunning(TERMINAL_KEY)).thenReturn(true);

        CommandStatus commandStatus = objUnderTest.getCommandStatus(TERMINAL_KEY);
        assertEquals(CommandStatus.RUNNING, commandStatus);
    }

    @Test
    public void getCommandStatus_finished_correctState(){
        when(commandStatusContainerMock.getCommandStatus(TERMINAL_KEY)).thenReturn(CommandStatus.FINISHED);

        CommandStatus commandStatus = objUnderTest.getCommandStatus(TERMINAL_KEY);
        assertEquals(CommandStatus.FINISHED,commandStatus);
    }

    @Test
    public void getCommandOutputReturnsCorrectResponseDto() throws ExecutionException, InterruptedException {
        when(commandStatusContainerMock.getExecutingCommand(TERMINAL_KEY)).thenReturn(commandResponseDtoFutureMock);
        when(commandResponseDtoFutureMock.get()).thenReturn(commandResponseDtoMock);

        ResponseDto responseDto = objUnderTest.getCommandOutput(TERMINAL_KEY);
        assertEquals(responseDtoMock, responseDto);
    }

    @Test
    public void removeManagedCommandGetTheCommandForAGivenTerminalId(){
        when(commandStatusContainerMock.removeCommandForTerminal(TERMINAL_KEY)).thenReturn(commandMock);
        assertEquals(commandMock, objUnderTest.removeManagedCommand(TERMINAL_KEY));
    }

    @Test
    public void canCheckIfCommandExistsForTerminal(){
        when(commandStatusContainerMock.isCommandManagedForTerminal(TERMINAL_KEY)).thenReturn(true);
        assertTrue(objUnderTest.doesCommandExistForTerminal(TERMINAL_KEY));

    }

    @Test
    public void canExecuteAliasCommand(){
        Command command = new Command("someContext","someCommand");
        Command aliasCommand = new Command("someCommand","it was aliased");
        when(aliasHandlerMock.isAlias(command)).thenReturn(true);
        when(aliasHandlerMock.resolveAlias(command)).thenReturn(aliasCommand);
        when(asyncContextPropagatorMock.getUserId()).thenReturn(USER_ID);
        when(commandStatusContainerMock.getExecutingCommand(TERMINAL_KEY)).thenReturn(commandResponseDtoFutureMock);

        objUnderTest.addCommand(TERMINAL_KEY, command);
        verify(asyncCommandServiceMock).executeCommand(aliasCommand, USER_ID);
    }

    @Test
    public void correctErrorManagerMethodIsCalledWhenInterruptedExceptionIsThrown() throws ExecutionException, InterruptedException {
        when(commandStatusContainerMock.getExecutingCommand(TERMINAL_KEY)).thenReturn(commandResponseDtoFutureMock);
        when(commandResponseDtoFutureMock.get()).thenThrow(interruptedExceptionMock);
        when(errorManagerMock.handleAsyncException(interruptedExceptionMock)).thenReturn(commandResponseDtoMock);
        objUnderTest.getCommandOutput(TERMINAL_KEY);
        verify(errorManagerMock).handleAsyncException(interruptedExceptionMock);
    }

    @Test
    public void correctErrorManagerMethodIsCalledWhenExecutionExceptionIsThrown() throws ExecutionException, InterruptedException {
        when(commandStatusContainerMock.getExecutingCommand(TERMINAL_KEY)).thenReturn(commandResponseDtoFutureMock);
        when(commandResponseDtoFutureMock.get()).thenThrow(executionExceptionMock);
        when(errorManagerMock.handleAsyncException(executionExceptionMock)).thenReturn(commandResponseDtoMock);
        objUnderTest.getCommandOutput(TERMINAL_KEY);
        verify(errorManagerMock).handleAsyncException(executionExceptionMock);
    }
}