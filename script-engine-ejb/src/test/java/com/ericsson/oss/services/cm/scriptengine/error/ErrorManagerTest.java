package com.ericsson.oss.services.cm.scriptengine.error;


import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ExecutionException;

import static com.ericsson.oss.services.cm.log.ErrorHandler.EXECUTION_ERROR;
import static com.ericsson.oss.services.cm.log.ErrorHandler.UNEXPECTED_ERROR;
import static com.ericsson.oss.services.cm.log.ErrorHandler.ERROR_CODE_UNEXPECTED_ERROR;
import static com.ericsson.oss.services.cm.log.ErrorHandler.SOLUTION_UNEXPECTED_ERROR;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ErrorManagerTest {
    private static final String DUMMY_COMMAND_RESOURCE = "Invalid command";
    private static final String DUMMY_ERROR_MESSAGE = "some error message";

    @Mock
    ErrorHandlerImpl errorHandlerMock;

    @Mock
    RuntimeException throwableMock;

    @Mock
    ExecutionException executionExceptionMock;

    @Mock
    InterruptedException interruptedExceptionMock;

    @InjectMocks
    ErrorManager objectUnderTest;

    @Test
    public void canHandleUnexpectedException(){
        when(errorHandlerMock.createErrorMessageFromException(throwableMock)).thenReturn(DUMMY_ERROR_MESSAGE);

        final CommandResponseDto result = objectUnderTest.handleUnexpectedException(throwableMock);

        assertUnexpectedError(result);
    }

    @Test
    public void canHandleUnrecognisedCommand(){
        final String context = "dummy-context";
        final String commandString = context + " dummy-command";
        final CommandResponseDto serviceResponse = new CommandResponseDto();
        serviceResponse.setCommand(commandString);
        final int expectedErrorCode = 6001;
        final String errorMessage = DUMMY_ERROR_MESSAGE;
        final String solution = "some solution";
        final Command command = new Command(context, commandString, null);

        when(errorHandlerMock.createErrorMessage(6001, DUMMY_COMMAND_RESOURCE, context)).thenReturn(errorMessage);
        when(errorHandlerMock.createSolutionMessage(6001, DUMMY_COMMAND_RESOURCE, context)).thenReturn(solution);
        final CommandResponseDto result = objectUnderTest.handleUnrecognisedCommand(command);

        assertEquals(context+" "+commandString, result.getCommand());
        assertEquals(EXECUTION_ERROR, result.getStatusCode());
        assertEquals(expectedErrorCode, result.getErrorCode());
        assertEquals(errorMessage, result.getStatusMessage());
        assertEquals(solution, result.getSolution());
    }

    @Test
    public void canHandleExecutionException(){
        when(errorHandlerMock.createErrorMessageFromException(executionExceptionMock)).thenReturn(DUMMY_ERROR_MESSAGE);
        final CommandResponseDto result = objectUnderTest.handleAsyncException(executionExceptionMock);
        assertUnexpectedError(result);
    }

    @Test
    public void canHandleInterruptedException(){
        when(errorHandlerMock.createErrorMessageFromException(interruptedExceptionMock)).thenReturn(DUMMY_ERROR_MESSAGE);
        final CommandResponseDto result = objectUnderTest.handleAsyncException(interruptedExceptionMock);
        assertUnexpectedError(result);
    }

    private void assertUnexpectedError(CommandResponseDto result) {
        assertEquals(UNEXPECTED_ERROR, result.getStatusCode());
        assertEquals(ERROR_CODE_UNEXPECTED_ERROR, result.getErrorCode());
        assertEquals(DUMMY_ERROR_MESSAGE, result.getStatusMessage());
        assertEquals(SOLUTION_UNEXPECTED_ERROR, result.getSolution());
    }



}
