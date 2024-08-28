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
package com.ericsson.oss.services.cm.scriptengine.ejb.service.alias;

import static com.ericsson.oss.services.cm.scriptengine.error.ErrorHandlerImpl.ALIAS_ALREADY_EXISTS_ERROR_CODE;
import static com.ericsson.oss.services.cm.scriptengine.error.ErrorHandlerImpl.ALIAS_INVALID_CREATE_ERROR_CODE;
import static com.ericsson.oss.services.cm.scriptengine.error.ErrorHandlerImpl.ALIAS_SYNTAX_ERROR_CODE;
import static com.ericsson.oss.services.scriptengine.spi.dtos.ResponseStatus.COMMAND_SYNTAX_ERROR;
import static com.ericsson.oss.services.scriptengine.spi.dtos.ResponseStatus.SUCCESS;

import javax.inject.Inject;

import com.ericsson.oss.services.cli.alias.model.CliAlias;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.AliasCreateInvalidArgumentsException;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.AliasInvalidSyntaxException;
import com.ericsson.oss.services.cm.scriptengine.error.ErrorHandlerImpl;
import com.ericsson.oss.services.scriptengine.spi.CommandHandler;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

public class AliasHandler implements CommandHandler {

    private static final String RESOURCE = "Invalid command";
    public static final String ALIAS_CREATE_SUCCESS_MESSAGE = "alias created";

    @Inject
    ErrorHandlerImpl errorHandler;

    @Inject
    AliasPersistence aliasPersistance;

    @Inject
    AliasParser aliasParser;

    public boolean isAlias(final Command command) {
        return aliasPersistance.isDefinedAlias(command.getCommandContext());
    }

    public Command resolveAlias(final Command command) {
        final CliAlias cliAlias = aliasPersistance.findAlias(command.getCommandContext());
        return new Command(cliAlias.commandContext, aliasParser.getAliasedCommandAfterArgumentSubstitution(command, cliAlias.command));
    }

    @Override
    public CommandResponseDto execute(final Command command) {
        CliAlias cliAlias;
        try {
            cliAlias = aliasParser.parseAliasCreateCommand(command);
        } catch (final AliasInvalidSyntaxException e) {
            return unsuccessfulResponseForAlias(command, ALIAS_SYNTAX_ERROR_CODE);
        } catch (final AliasCreateInvalidArgumentsException e) {
            return unsuccessfulResponseForAlias(command, ALIAS_INVALID_CREATE_ERROR_CODE);
        }
        if (aliasPersistance.isDefinedAlias(cliAlias.name)) {
            return unsuccessfulResponseForAlias(command, ALIAS_ALREADY_EXISTS_ERROR_CODE, cliAlias.name);
        }
        aliasPersistance.saveAlias(cliAlias);
        return successfulResponseForAlias(command);

    }

    /*
     * P R I V A T E - M E T H O D S
     */
    private CommandResponseDto successfulResponseForAlias(final Command command) {
        final CommandResponseDto response = new CommandResponseDto();
        response.setStatusMessage(ALIAS_CREATE_SUCCESS_MESSAGE);
        response.setCommand(command.toString());
        response.setStatusCode(SUCCESS);

        response.addSuccessLines();

        return response;
    }

    private CommandResponseDto unsuccessfulResponseForAlias(final Command command, final int errorCode, final Object... aliasInfo) {
        final CommandResponseDto response = new CommandResponseDto();
        response.setCommand(command.toString());
        response.setStatusCode(COMMAND_SYNTAX_ERROR);
        response.setStatusMessage(errorHandler.createErrorMessage(errorCode, RESOURCE, aliasInfo));
        response.setErrorCode(errorCode);
        response.setSolution(errorHandler.createSolutionMessage(errorCode, RESOURCE, aliasInfo));

        response.addErrorLines();

        return response;
    }
}