package com.ericsson.oss.services.scriptengine.api;

import javax.ejb.Local;

import com.ericsson.oss.itpf.sdk.core.annotation.EService;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.ResponseDto;

@Local
@EService
public interface ScriptEngineSessionFacade {

    CommandStatus addCommand(String terminalKey, final Command command);
    CommandStatus getCommandStatus(String terminalKey);
    ResponseDto getCommandOutput(String terminalKey);
    Command removeManagedCommand(String terminalKey);
    boolean doesCommandExistForTerminal(String terminalKey);
}