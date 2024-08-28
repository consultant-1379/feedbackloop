package com.ericsson.oss.services.cm.scriptengine.ejb.service;

import com.ericsson.oss.services.scriptengine.api.CommandStatus;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class CommandStatusContainer {
    public static final int MAX_NUMBER_OF_CONCURRENT_TERMINALS = 200;

    private final Map<String, CommandState> managedCommands = new HashMap<>(MAX_NUMBER_OF_CONCURRENT_TERMINALS);

    @Lock(LockType.READ)
    public boolean isCommandManagedForTerminal(final String terminalId){
        return managedCommands.containsKey(terminalId);
    }

    @Lock(LockType.READ)
    public CommandStatus getCommandStatus(final String terminalId) {
        final CommandState commandState = managedCommands.get(terminalId);
        if(isTerminalStateInCache(commandState)){
            return commandState.getCommandStatus();
        }
        //TODO emcgdan update to return FAIL or ERROR
        return null;
    }

    @Lock(LockType.READ)
    public boolean isCommandRunning(final String terminalId) {
        final Future<CommandResponseDto>  commandResponseDtoFuture = managedCommands.get(terminalId).getCommandResponseDtoFuture();
        return !(commandResponseDtoFuture.isDone() || commandResponseDtoFuture.isCancelled());
    }

    @Lock(LockType.READ)
    public Command getCommand(final String terminalId) {
        return managedCommands.get(terminalId).getCommand();
    }

    @Lock(LockType.READ)
    public Future<CommandResponseDto> getExecutingCommand(final String terminalId) {
        return managedCommands.get(terminalId).getCommandResponseDtoFuture();
    }

    @Lock(LockType.WRITE)
    public CommandStatus addRunningCommandState(final String terminalId, final Command command, final Future<CommandResponseDto> commandResponseDtoFuture) {
        final CommandState commandState = new CommandState(command, commandResponseDtoFuture, CommandStatus.RUNNING);
        managedCommands.put(terminalId, commandState);
        return commandState.getCommandStatus();
    }

    @Lock(LockType.WRITE)
    public void updateCommandStatus(final String terminalId, final CommandStatus commandStatus) {
        managedCommands.get(terminalId).setCommandStatus(commandStatus);
    }

    @Lock(LockType.WRITE)
    public Command removeCommandForTerminal(final String terminalId) {
        final CommandState commandState = managedCommands.remove(terminalId);
        return commandState.getCommand();
    }

    private boolean isTerminalStateInCache(final CommandState commandState) {
        /**
         * Note it is possible to get null at this point if polling occurs
         * in a fail over or if messages are routed to a different SC
         */
        return commandState != null;
    }
}
