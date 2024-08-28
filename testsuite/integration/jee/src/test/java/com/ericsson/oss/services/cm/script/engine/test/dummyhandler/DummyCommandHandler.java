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
package com.ericsson.oss.services.cm.script.engine.test.dummyhandler;

import javax.ejb.Stateless;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceQualifier;
import com.ericsson.oss.services.scriptengine.spi.CommandHandler;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

@Stateless
@EServiceQualifier("dummy")
public class DummyCommandHandler implements CommandHandler {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ericsson.oss.services.scriptengine.spi.CommandHandler#execute(com
     * .ericsson.oss.services.scriptengine.spi.dtos.Command)
     */
    @Override
    public CommandResponseDto execute(final Command command) {
        final CommandResponseDto dummyResponseDto = new CommandResponseDto();

        if (command.getCommand().contains("alias-error")) {
            dummyResponseDto.setStatusCode(CommandResponseDto.COMMAND_EXECUTION_ERROR);
            dummyResponseDto.setStatusMessage("alias not created");
            dummyResponseDto.addErrorLines();
        } else if (command.getCommand().contains("error")) {
            dummyResponseDto.setStatusCode(CommandResponseDto.COMMAND_EXECUTION_ERROR);
            dummyResponseDto.setStatusMessage("Command Execution Failed");
            dummyResponseDto.setErrorCode(9999);
            dummyResponseDto.setSolution("some solution");
            dummyResponseDto.addErrorLines();
        } else if (command.getCommand().contains("alias")) {
            dummyResponseDto.setStatusCode(CommandResponseDto.SUCCESS);
            dummyResponseDto.setStatusMessage("alias created");
            dummyResponseDto.addSuccessLines();
        } else {
            dummyResponseDto.setStatusCode(CommandResponseDto.SUCCESS);
            dummyResponseDto.setStatusMessage("Command Executed Successfully");
            dummyResponseDto.addSuccessLines();
        }
        return dummyResponseDto;
    }

}
