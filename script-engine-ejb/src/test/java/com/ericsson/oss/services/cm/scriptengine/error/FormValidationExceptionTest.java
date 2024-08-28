package com.ericsson.oss.services.cm.scriptengine.error;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FormValidationExceptionTest {

    FormValidationException objUnderTest;
    final static String message = "Some Message";

    @Test
    public void canCreateCmValidationExceptionNoConstructorsDefaultStatusCodeWillBeMinusOneDefaultMessageIsNull() {
        objUnderTest = new FormValidationException(null);
        assertNull(objUnderTest.getMessage());
    }

    @Test
    public void canCreateCmValidationExceptionWithMessage() {
        objUnderTest = new FormValidationException(message);
        assertEquals(message, objUnderTest.getMessage());
    }
}