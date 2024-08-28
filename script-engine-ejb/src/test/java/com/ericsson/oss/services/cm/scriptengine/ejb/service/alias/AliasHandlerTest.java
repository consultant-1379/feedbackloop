package com.ericsson.oss.services.cm.scriptengine.ejb.service.alias;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.sdk.core.classic.ServiceFinderBean;
import com.ericsson.oss.services.cli.alias.model.CliAlias;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.AliasCreateInvalidArgumentsException;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.AliasInvalidSyntaxException;
import com.ericsson.oss.services.cm.scriptengine.error.ErrorHandlerImpl;
import com.ericsson.oss.services.scriptengine.spi.CommandHandler;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.ResponseStatus;

@RunWith(MockitoJUnitRunner.class)
public class AliasHandlerTest {
    @Mock
    @Inject
    ErrorHandlerImpl errorHandler;

    @Mock
    @Inject
    AliasPersistence aliasPersistance;

    @Mock
    @Inject
    AliasParser aliasParser;

    @Mock
    @Inject
    ServiceFinderBean serviceFinderBean;

    @InjectMocks
    AliasHandler objUnderTest = new AliasHandler();
    final Map<String, Object> properties = new HashMap<String, Object>();

    final Command command = new Command("dummy-context", "dummy-command", properties);

    CliAlias cliAlias = new CliAlias();

    @Mock
    @Inject
    CommandHandler commandHandlerService;

    @Test
    public void canCreateAliasSuccessfully() throws Exception {
        cliAliasSetup();
        when(aliasParser.isCreateAliasCommand(command)).thenReturn(true);
        when(aliasParser.parseAliasCreateCommand(command)).thenReturn(cliAlias);
        when(aliasPersistance.isDefinedAlias(any(String.class))).thenReturn(false);
        final CommandResponseDto response = objUnderTest.execute(command);
        assertEquals(AliasHandler.ALIAS_CREATE_SUCCESS_MESSAGE, response.getStatusMessage());
        assertEquals(ResponseStatus.SUCCESS, response.getStatusCode());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void canCreateAliasWithInvalidSyntaxReturnsUnsuccessfulResponse() throws Exception {
        when(aliasParser.isCreateAliasCommand(command)).thenReturn(true);
        when(aliasParser.parseAliasCreateCommand(command)).thenThrow(AliasInvalidSyntaxException.class);
        final CommandResponseDto response = objUnderTest.execute(command);
        assertEquals(ResponseStatus.COMMAND_SYNTAX_ERROR, response.getStatusCode());
    }

    @Test
    public void canExecuteAliasAlreadyExistsReturnsUnsuccessfulResponse() throws Exception {
        cliAliasSetup();
        when(aliasParser.isCreateAliasCommand(command)).thenReturn(true);
        when(aliasParser.parseAliasCreateCommand(command)).thenReturn(cliAlias);
        when(aliasPersistance.isDefinedAlias(any(String.class))).thenReturn(true);
        final CommandResponseDto response = objUnderTest.execute(command);
        assertEquals(ErrorHandlerImpl.ALIAS_ALREADY_EXISTS_ERROR_CODE, response.getErrorCode());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void canExecuteInvalidAliasArgumentsCommandAndReturnsUnsuccessfulResponse() throws Exception {
        cliAliasSetup();
        when(aliasParser.isCreateAliasCommand(command)).thenReturn(true);
        when(aliasParser.parseAliasCreateCommand(command)).thenThrow(AliasCreateInvalidArgumentsException.class);
        final CommandResponseDto response = objUnderTest.execute(command);
        assertEquals(ErrorHandlerImpl.ALIAS_INVALID_CREATE_ERROR_CODE, response.getErrorCode());
    }

    private void cliAliasSetup() {
        cliAlias.name = "dummy-name";
        cliAlias.arguments = "dummy-arguments";
        cliAlias.commandContext = "dummy-commandContext";
        cliAlias.command = "dummy-command";
        cliAlias.userId = "dummy-userId";
    }
}