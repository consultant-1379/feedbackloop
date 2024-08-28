package com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AliasIncorrectNumberOfArgumentsExceptionTest {

    AliasIncorrectNumberOfArgumentsException objUnderTest;
    final static String message = "Some Message";

    @Test
    public void canCreateAliasIncorrectNumberOfArgumentsException() {
        objUnderTest = new AliasIncorrectNumberOfArgumentsException();
        assertNull(objUnderTest.getMessage());
    }
}