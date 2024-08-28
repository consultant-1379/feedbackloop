package com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CannotPersistAliasExceptionTest {

    CannotPersistAliasException objUnderTest;
    final static String message = "Some Message";

    @Test
    public void canCreateCannotPersistAliasExceptionWithMessageNull() {
        objUnderTest = new CannotPersistAliasException(null);
        assertNull(objUnderTest.getMessage());
    }

    @Test
    public void canCreateCmValidationExceptionWithMessage() {
        objUnderTest = new CannotPersistAliasException(message);
        assertEquals(message, objUnderTest.getMessage());
    }
}