package com.ericsson.oss.services.cm.scriptengine.ejb.service;

import com.ericsson.oss.itpf.sdk.core.classic.ServiceFinderBean;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.AliasHandler;
import com.ericsson.oss.services.cm.scriptengine.error.ErrorManager;
import com.ericsson.oss.services.scriptengine.spi.CommandHandler;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AsyncCommandServiceTest {


    private static final String DUMMY_COMMAND = "dummy-command";
    private static final String DUMMY_CONTEXT = "dummy-context";
    private static final String USER_ID = "BOB";

    @Mock
    CommandHandler mockService;

    @Mock
    ErrorManager mockErrorManager;

    @Mock
    RuntimeException mockThrowable;

    @Mock
    AliasHandler aliasHandlerMock;

    @Mock
    AsyncContextPropagator asyncContextPropagatorMock;

    @Mock
    ServiceFinderBean serviceFinderBean;

    @InjectMocks
    AsyncCommandService objUnderTest = new AsyncCommandService();
    private final Map<String, Object> properties = new HashMap<String, Object>();
    private final Command command = new Command(DUMMY_CONTEXT, DUMMY_COMMAND, properties);

    @Test
    public void executeCommandForwardsToServiceWithCommandAndReturnsTheCommandResult() throws ExecutionException, InterruptedException {
        final String commandString = "dummy-command";
        final CommandResponseDto serviceResponse = new CommandResponseDto();
        serviceResponse.setCommand(commandString);
        when(mockService.execute(command)).thenReturn(serviceResponse);
        when(serviceFinderBean.find(CommandHandler.class, DUMMY_CONTEXT)).thenReturn(mockService);
        final Future<CommandResponseDto> result = objUnderTest.executeCommand(command, USER_ID );
        assertEquals(serviceResponse.getCommand(), result.get().getCommand());
        // Verify that the correct method was called with the correct parameters
        verify(mockService).execute(command);
    }

    @Test
    public void aliasHandlerIsReturnedWhenAnAliasCommandIsPassed() throws ExecutionException, InterruptedException {
        Command command = new Command("alias", "command string");
        final CommandResponseDto serviceResponse = new CommandResponseDto();
        when(aliasHandlerMock.execute(command)).thenReturn(serviceResponse);

        final Future<CommandResponseDto> result = objUnderTest.executeCommand(command, USER_ID );
        assertEquals(serviceResponse, result.get());
        verify(aliasHandlerMock).execute(command);
    }

    @Test
    public void executeCommandAndCannotFindCLIServiceAndReturnsErrorResult() throws ExecutionException, InterruptedException {
        when(serviceFinderBean.find(CommandHandler.class, command.getCommandContext())).thenThrow(new IllegalStateException());
        objUnderTest.executeCommand(command,USER_ID );
        verify(mockErrorManager).handleUnrecognisedCommand(command);
    }

    @Test
    public void executeCommandFailsDueToUnexpectedError() {
        when(serviceFinderBean.find(CommandHandler.class, DUMMY_CONTEXT)).thenThrow(mockThrowable);
        objUnderTest.executeCommand(command, USER_ID );
        verify(mockErrorManager).handleUnexpectedException(any(Throwable.class));
    }
}