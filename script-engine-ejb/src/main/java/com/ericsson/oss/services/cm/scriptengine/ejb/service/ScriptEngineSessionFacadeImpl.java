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

import com.ericsson.oss.itpf.sdk.instrument.annotation.Profiled;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.AliasHandler;
import com.ericsson.oss.services.cm.scriptengine.error.ErrorManager;
import com.ericsson.oss.services.scriptengine.api.CommandStatus;
import com.ericsson.oss.services.scriptengine.api.ScriptEngineSessionFacade;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.ResponseDto;

import javax.ejb.EJB;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Profiled
@Default
public class ScriptEngineSessionFacadeImpl implements ScriptEngineSessionFacade {

    @EJB
    CommandStatusContainer commandStatusContainer;

    @EJB
    AsyncCommandService asyncCommandService;

    @Inject
    AliasHandler aliasHandler;

    @Inject
    ErrorManager errorManager;

    @Inject
    AsyncContextPropagator asyncContextPropagator;

    @Override
    public CommandStatus addCommand(final String terminalId, final Command command) {
        Command commandToExecute = command;
        if(isAlias(command)) {
            commandToExecute = resolveAlias(command);
        }
        final Future<CommandResponseDto> commandResponseDtoFuture = asyncCommandService.executeCommand(commandToExecute, asyncContextPropagator.getUserId());
        return commandStatusContainer.addRunningCommandState(terminalId, command, commandResponseDtoFuture);
    }

    @Override
    public CommandStatus getCommandStatus(final String terminalId) {
        final CommandStatus commandStatus = getCommandStatusFromCommandContainer(terminalId);
        return commandStatus;
    }

    @Override
    public ResponseDto getCommandOutput(final String terminalId) {
        CommandResponseDto commandResponseDto;
        try {
            commandResponseDto = commandStatusContainer.getExecutingCommand(terminalId).get();
            commandStatusContainer.updateCommandStatus(terminalId, CommandStatus.FINISHED);
        } catch (InterruptedException e) {
            commandResponseDto = errorManager.handleAsyncException(e);
        } catch (ExecutionException e) {
            commandResponseDto = errorManager.handleAsyncException(e);
        }

        final ResponseDto responseDto = commandResponseDto.getResponseDto();

        return responseDto;
    }

    @Override
    public Command removeManagedCommand(final String terminalId) {
        return commandStatusContainer.removeCommandForTerminal(terminalId);
    }

    @Override
    public boolean doesCommandExistForTerminal(final String terminalId) {
        return commandStatusContainer.isCommandManagedForTerminal(terminalId);
    }

    private CommandStatus getCommandStatusFromCommandContainer(final String terminalId) {
        CommandStatus commandStatus = commandStatusContainer.getCommandStatus(terminalId);
        //TODO emcgdan: add command status failed.
        commandStatus = commandStatus == null ? CommandStatus.FINISHED : commandStatus;
        if(commandStatus.equals(CommandStatus.RUNNING)){
            if(!commandStatusContainer.isCommandRunning(terminalId)){
                commandStatus = CommandStatus.COMPLETE;
                commandStatusContainer.updateCommandStatus(terminalId, commandStatus);
            }
        }
        return commandStatus;
    }

    private Command resolveAlias(final Command command) {
        return aliasHandler.resolveAlias(command);
    }


    private boolean isAlias(final Command command) {
        return aliasHandler.isAlias(command);
    }
}
