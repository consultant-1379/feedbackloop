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

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CommandTest {

    private static final String EXAMPLE_CONTEXT = "ap";
    private static final String EXAMPLE_COMMAND = "import p2 p3";

    private static final Map<String, Object> EXAMPLE_PROPERTIES = new HashMap<String, Object>();

    Command command;
    

    @Before
    public void canCreateCommand() {
        command = new Command(EXAMPLE_CONTEXT, EXAMPLE_COMMAND, EXAMPLE_PROPERTIES);
        assertNotNull(command);
    }

    @Test
    public void getContextReturnsCommandContext() {
        assertEquals(command.getCommandContext(), EXAMPLE_CONTEXT);
    }

    @Test
    public void getCommandRetrunsTheCommandStringWithoutContext() {
        assertEquals(command.getCommand(), EXAMPLE_COMMAND);
    }

    @Test
    public void getPropertiesReturnPropertiesMap() {
        assertEquals(command.getProperties(), EXAMPLE_PROPERTIES);
    }

    @Test
    public void getPropertiesReturnsEmptyMapForCommandWithNOProperties() {
        Command commandWithoutProperties = new Command(EXAMPLE_CONTEXT, EXAMPLE_COMMAND);
        assertTrue(commandWithoutProperties.getProperties().isEmpty());
    }

    @Test
    public void toStringMethodReturnsCompleteCommandWithContext() {
        assertEquals(command.toString(), EXAMPLE_CONTEXT + " " + EXAMPLE_COMMAND);
    }
    
    @Test
    public void toStringMethodReturnsContextWhenCommanNull() {
    	Command anotherInstanceOfCommand = new Command(EXAMPLE_CONTEXT, null, EXAMPLE_PROPERTIES);
        assertEquals(anotherInstanceOfCommand.toString(), EXAMPLE_CONTEXT);
    }
    
    @Test
    public void nullIsNotEqualsToACommand() throws Exception {
        assertFalse(command.equals(null));
    }
    
    @Test
    public void commandIsEqualsToItself() throws Exception {
        assertTrue(command.equals(command));
    }
    
    @Test
    public void commandIsNotEqualsToObjectOfOtherType() throws Exception {
        assertFalse(command.equals("someString"));
    }
    
    @Test
    public void commandIsEqualsToAnotherInstanceOfCommandWithSameProprties() throws Exception {
        Command anotherInstanceOfCommand = new Command(EXAMPLE_CONTEXT, EXAMPLE_COMMAND, EXAMPLE_PROPERTIES);
    	assertEquals(command, anotherInstanceOfCommand);
    }

    @Test
    public void commandIsEqualToOtherCommandWithSamePropertiesContent() throws Exception {
         Map<String, Object> otherPropertiesSameContent = new HashMap<String, Object>();
        Command anotherInstanceOfCommand = new Command(EXAMPLE_CONTEXT, EXAMPLE_COMMAND, otherPropertiesSameContent);
    	assertEquals(command, anotherInstanceOfCommand);
    }
    
    @Test
    public void commandIsNotEqualToOtherCommandWithOtherPropertiesContent() throws Exception {
         Map<String, Object> otherProperties = new HashMap<String, Object>();
         otherProperties.put("something", "some value");
        Command anotherInstanceOfCommand = new Command(EXAMPLE_CONTEXT, EXAMPLE_COMMAND, otherProperties);
    	assertFalse(command.equals(anotherInstanceOfCommand));
    }    
    
}
