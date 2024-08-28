package com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AliasInvalidSyntaxTest {

    AliasInvalidSyntaxException objUnderTest;
    final static String message = "Some Message";

    @Test
    public void canCreateAliasInvalidSyntaxException() {
        objUnderTest = new AliasInvalidSyntaxException();
        assertNull(objUnderTest.getMessage());
    }
}