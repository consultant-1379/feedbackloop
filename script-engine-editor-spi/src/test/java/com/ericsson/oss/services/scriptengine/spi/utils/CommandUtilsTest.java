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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ericsson.oss.services.scriptengine.spi.dtos.Command;

/**
 * Test for CommandUtils class
 * 
 * 
 */
public class CommandUtilsTest {

	@Test
	public void canCreateCommandWithContext() {
		final Command command = CommandUtils.createCommand(
				"ap create someparameter", null);
		assertEquals(command.getCommandContext(), "ap");
		assertEquals(command.getCommand(), "create someparameter");
		assertEquals(command.getProperties(), null);
	}

	@Test
	public void canCreateCommandWithProperties() {
		final Map<String, Object> properties = new HashMap<String, Object>();
		Command command = CommandUtils.createCommand("ap", properties);
		assertEquals(command.getCommandContext(), "ap");
		assertEquals(command.getCommand(), "");
		assertEquals(command.getProperties(), properties);
	}

	@Test
	public void createCommandIgnoresAdditionalWhitespace() {
		Command command = CommandUtils.createCommand("   ap  ", null);
		assertEquals(command.getCommandContext(), "ap");
		assertEquals(command.getCommand(), "");
	}

	@Test(expected = InstantiationException.class)
	public void testIsNotInsatiable() throws Exception {
		CommandUtils.class.newInstance();
	}

	@Test
	public void testIsExtensible() throws Exception {
		new CommandUtils() {
		};
	}
}
