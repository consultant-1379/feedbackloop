package com.ericsson.oss.services.cm.scriptengine.ejb.service;


import com.ericsson.oss.services.scriptengine.api.CommandStatus;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

import java.util.concurrent.Future;

public class CommandState {
    private final Command command;
    private final Future<CommandResponseDto> commandResponseDtoFuture;
    private CommandStatus commandStatus;

    public CommandState(final Command command, final Future<CommandResponseDto> commandResponseDtoFuture, final CommandStatus commandStatus) {
        this.command = command;
        this.commandResponseDtoFuture = commandResponseDtoFuture;
        this.commandStatus = commandStatus;
    }

    public void setCommandStatus(final CommandStatus commandStatus) {
        this.commandStatus = commandStatus;
    }

    public CommandStatus getCommandStatus() {
        return commandStatus;
    }

    public Future<CommandResponseDto> getCommandResponseDtoFuture() {
        return commandResponseDtoFuture;
    }

    public Command getCommand() {
        return command;
    }
}
