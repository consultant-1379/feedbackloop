package com.ericsson.oss.services.cm.scriptengine.ejb.service;

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import com.ericsson.oss.itpf.sdk.core.classic.ServiceFinderBean;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.AliasHandler;
import com.ericsson.oss.services.cm.scriptengine.error.ErrorManager;
import com.ericsson.oss.services.scriptengine.spi.CommandHandler;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandResponseDto;

@LocalBean
@Asynchronous
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@SuppressWarnings("PMD.AvoidCatchingThrowable")
public class AsyncCommandService {

    private static final String ALIAS = "alias".intern();

    @Inject
    ErrorManager errorManager;

    @Inject
    AliasHandler aliasHandler;

    @Inject
    AsyncContextPropagator asyncContextPropagator;

    // TODO inject
    ServiceFinderBean serviceFinderBean;

    public Future<CommandResponseDto> executeCommand(final Command command, final String userId) {
        final CommandHandler commandHandlerService;
        try {
            asyncContextPropagator.setUserId(userId);
            commandHandlerService = getCommandHandlerService(command.getCommandContext());
            final CommandResponseDto commandResponseDto = commandHandlerService.execute(command);
            return new AsyncResult<>(commandResponseDto);
        } catch (final IllegalStateException illegalStateException) {
            return new AsyncResult<>(errorManager.handleUnrecognisedCommand(command));
        } catch (final Throwable t) {
            return new AsyncResult<>(errorManager.handleUnexpectedException(t));
        }
    }

    private CommandHandler getCommandHandlerService(final String commandName) {
        if (ALIAS.equals(commandName)) {
            return aliasHandler;
        }
        final CommandHandler commandHandler = getServiceFinderBean().find(CommandHandler.class, commandName);
        return commandHandler;
    }

    private ServiceFinderBean getServiceFinderBean() {
        if (serviceFinderBean == null) {
            serviceFinderBean = new ServiceFinderBean();
        }
        return serviceFinderBean;
    }
}
