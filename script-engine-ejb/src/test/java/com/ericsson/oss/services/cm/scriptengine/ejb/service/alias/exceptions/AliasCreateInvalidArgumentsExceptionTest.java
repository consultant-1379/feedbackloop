package com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AliasCreateInvalidArgumentsExceptionTest {

    AliasCreateInvalidArgumentsException objUnderTest;
    final static String message = "Some Message";

    @Test
    public void canCreateAliasCreateInvalidArgumentsException() {
        objUnderTest = new AliasCreateInvalidArgumentsException();
        assertNull(objUnderTest.getMessage());
    }
}