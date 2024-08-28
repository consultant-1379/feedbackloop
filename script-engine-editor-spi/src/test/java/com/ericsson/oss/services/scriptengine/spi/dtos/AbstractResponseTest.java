package com.ericsson.oss.services.scriptengine.spi.dtos;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AbstractResponseTest {

    TestableAbstractResponse objectUnderTest;

    @Before
    public void canCreateTestableAbstractResponse() {
        objectUnderTest = new TestableAbstractResponse();
        assertNotNull(objectUnderTest);
    }

    @Test
    public void canSetAndGetSuccessCode() {
        assertEquals(0, objectUnderTest.getStatusCode());
        final int successCode = -1;
        objectUnderTest.setStatusCode(successCode);
        assertEquals(successCode, objectUnderTest.getStatusCode());
    }

    @Test
    public void canSetAndGetSuccessMessage() {
        assertNull(objectUnderTest.getStatusMessage());
        final String messageString = "someMessageString";
        objectUnderTest.setStatusMessage(messageString);
        assertEquals(objectUnderTest.getStatusMessage(), messageString);
    }

    @Test
    public void defaultErrorCodeIs0() {
        assertEquals(0, objectUnderTest.getErrorCode());
    }

    @Test
    public void canGetAndSetErrorCode() {
        final int errorCode = 123;
        objectUnderTest.setErrorCode(errorCode);
        assertEquals(errorCode, objectUnderTest.getErrorCode());
    }

    @Test
    public void canGetAndSetSolution() {
        final String solution = "some solution";
        objectUnderTest.setSolution(solution);
        assertEquals(solution, objectUnderTest.getSolution());
    }

    /*
     * D E F I N E - T E S T A B L E - I M P L - F O R - A B S T R A C T - C L A S S
     */

    class TestableAbstractResponse extends AbstractResponse {

    }

}
