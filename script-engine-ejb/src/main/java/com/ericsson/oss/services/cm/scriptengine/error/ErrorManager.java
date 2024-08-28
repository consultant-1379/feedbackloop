package com.ericsson.oss.services.cm.scriptengine.error;


import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

import javax.inject.Inject;

import java.util.concurrent.ExecutionException;

import static com.ericsson.oss.services.cm.log.ErrorHandler.ERROR_CODE_UNEXPECTED_ERROR;
import static com.ericsson.oss.services.cm.log.ErrorHandler.SOLUTION_UNEXPECTED_ERROR;
import static com.ericsson.oss.services.cm.scriptengine.error.ErrorHandlerImpl.UNRECOGNISED_CLI_COMMAND_CODE;
import static com.ericsson.oss.services.scriptengine.spi.dtos.ResponseStatus.COMMAND_EXECUTION_ERROR;
import static com.ericsson.oss.services.scriptengine.spi.dtos.ResponseStatus.UNEXPECTED_ERROR;

public class ErrorManager {
    @Inject
    ErrorHandlerImpl errorHandler;

    private static final String RESOURCE = "Invalid command";

    public CommandResponseDto handleUnexpectedException(final Throwable t) {
        final String errorMessage = errorHandler.createErrorMessageFromException(t);
        final CommandResponseDto commandResponseDto = new CommandResponseDto();
        commandResponseDto.setStatusCode(UNEXPECTED_ERROR);
        commandResponseDto.setStatusMessage(errorMessage);
        commandResponseDto.setErrorCode(ERROR_CODE_UNEXPECTED_ERROR);
        commandResponseDto.setSolution(SOLUTION_UNEXPECTED_ERROR);
        commandResponseDto.addErrorLines();
        return commandResponseDto;
    }

    public CommandResponseDto handleUnrecognisedCommand(final Command command) {
        CommandResponseDto response;
        response = new CommandResponseDto();
        final String commandString = getCommandString(command);
        response.setCommand(commandString);
        response.setStatusCode(COMMAND_EXECUTION_ERROR);
        response.setErrorCode(UNRECOGNISED_CLI_COMMAND_CODE);
        final String errorMessage = errorHandler.createErrorMessage(UNRECOGNISED_CLI_COMMAND_CODE, RESOURCE, command.getCommandContext());
        response.setStatusMessage(errorMessage);
        final String solution = errorHandler.createSolutionMessage(UNRECOGNISED_CLI_COMMAND_CODE, RESOURCE, command.getCommandContext());
        response.setSolution(solution);
        response.addErrorLines();
        return response;
    }

    public CommandResponseDto handleAsyncException(final ExecutionException e) {
        return handleUnexpectedException(e);
    }

    public CommandResponseDto handleAsyncException(final InterruptedException e) {
        return handleUnexpectedException(e);
    }

    private String getCommandString(final Command command) {
        final String commandContext = command.getCommandContext();
        final String commandString = command.getCommand();
        if (commandString != null && !commandString.equals("")) {
            return String.format("%1$s %2$s", commandContext, command.getCommand());
        }
        return commandContext;
    }
}
