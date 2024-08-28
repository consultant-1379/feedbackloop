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
package com.ericsson.oss.services.scriptengine.spi.utils;

import java.util.Map;

import com.ericsson.oss.services.scriptengine.spi.dtos.Command;

/**
 * Utility class for creating command object(s)
 * 
 * 
 */
public abstract class CommandUtils {

    /**
     * Create a command where the command string includes context
     * 
     * @param commandTarget
     *            target subsystem/service where command is to be executed
     * @param commandWithContext
     *            command string including the command context
     * @param properties
     *            properties associated with the command
     * @return Command Fully initialized command object
     */
    public final static Command createCommand(final String commandString, final Map<String, Object> properties) {
        final String commandContext;
        final String command;
        final Command commandObj;

        commandContext = getCommandContext(commandString);

        command = getCommand(commandString, commandContext);

        commandObj = new Command(commandContext, command, properties);

        return commandObj;
    }

    /**
     * Get the rest of the command, except the command context.
     * 
     * @param commandString
     *            the complete command
     * @param context
     *            command context
     * @return
     */
    private static String getCommand(final String commandString, final String context) {

        final String parameters;
        final int parameterStartIndex = commandString.indexOf(context) + context.length();
        parameters = commandString.substring(parameterStartIndex).trim();

        return parameters;
    }

    private static String getCommandContext(final String commandString) {

        final String[] commandArray = commandString.trim().split("\\s");

        return commandArray[0];
    }
}
