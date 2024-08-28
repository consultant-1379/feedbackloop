package com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CannotFindAliasExceptionTest {

    CannotFindAliasException objUnderTest;
    final static String message = "Some Message";

    @Test
    public void canCreateCannotFindAliasExceptionWithMessageNull() {
        objUnderTest = new CannotFindAliasException(null);
        assertNull(objUnderTest.getMessage());
    }

    @Test
    public void canCreateCannotFindAliasExceptionWithMessage() {
        objUnderTest = new CannotFindAliasException(message);
        assertEquals(message, objUnderTest.getMessage());
    }
}