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
package com.ericsson.oss.services.scriptengine.spi.dtos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Command Object used to pass commands to CommandHandler services for
 * processing. For example, if an operator types the following on the command
 * line : >> ap import file:Dallas.zip a command object would be passed to
 * CommandHandler ejb implementation where : <br>
 * commandContext = "ap"<br>
 * command = "import file:project.zip"<br>
 * commandProperties = Map of of command properties that can be interpreted by
 * the target service.<br>
 * 
 */
public class Command implements Serializable {

    private static final long serialVersionUID = -6483416130337286366L;
    private static final Map<String,Object> EMPTY_PROPERTIES = new HashMap<String, Object>(0);
    private final String commandContext;
    private final String command;
    private final Map<String, Object> properties;

    /**
     * Constructor to initialize a command.
     * 
     * @param commandTarget
     *            Target subsystem/service/component to execute the command
     *            (e.g. "pico")
     * @param commandContext
     *            Context of the command (e.g. "ap")
     * @param command
     *            Parameters passed as part of the command (e.g. import
     *            file:Dallas.zip)
     * @param properties
     *            Properties passed to support the command.
     */
    public Command(final String commandContext, final String command, final Map<String, Object> properties) {

        this.commandContext = commandContext;
        this.command = command;

        this.properties = properties;
    }

    /**
     * Constructor to initialize a command.
     * 
     * @param commandTarget
     *            Target subsystem/service/component to execute the command
     *            (e.g. "pico")
     * @param operation
     *            Operation part of a command (e.g. "import")
     * @param parameters
     *            Parameters passed as part of the command (e.g.
     *            file:Dallas.zip)
     */
    public Command(final String commandContext, final String command) {
        this(commandContext, command, EMPTY_PROPERTIES);
    }

    /**
     * Get the Map of properties so operator can iterate through the Map if they
     * wish.
     * 
     * @return
     */
    public Map<String, Object> getProperties() {

        return this.properties;
    }

    /**
     * Return the command context, e.g. "cmedit" or "ap", etc.
     * 
     * @return
     */
    public String getCommandContext() {

        return this.commandContext;
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {

        String printString = this.getCommandContext();

        if (this.getCommand() != null) {
            printString += " " + this.getCommand();
        }

        return printString;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof Command)) {
            return false;
        }
        final Command other = (Command) object;
        return this.command.equals(other.getCommand()) && this.commandContext.equals(other.getCommandContext())  && this.properties.equals(other.getProperties());
    }

}
