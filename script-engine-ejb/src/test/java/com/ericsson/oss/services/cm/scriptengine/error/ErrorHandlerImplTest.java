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
package com.ericsson.oss.services.cm.scriptengine.error;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ericsson.oss.services.cm.log.ErrorHandler;

public class ErrorHandlerImplTest {

    ErrorHandlerImpl objUnderTest = new ErrorHandlerImpl();

    @Test
    public void ensureCorrectErrorMessageCanBeCreatedForEachErrorCode() throws Exception {
        /*
         * This test uses reflection to check consistency of names of error code constants and also verifies they are (added) correctly in the
         * errorMessagesMap
         */
        final Map<String, String[]> extractedErrorMessages = new HashMap<>();
        final Field[] fields = ErrorHandlerImpl.class.getDeclaredFields();
        for (final Field field : fields) {
            if (field.getName().endsWith("_MSGS")) {
                System.out.println(field.getName());
                final String matchingErrorCodeFieldname = field.getName().replaceFirst("_MSGS", "_CODE");
                extractedErrorMessages.put(matchingErrorCodeFieldname, (String[]) field.get(ErrorHandler.class));
            }
        }

        for (final Field field : ErrorHandlerImpl.class.getDeclaredFields()) {
            if (field.getName().endsWith("_CODE")) {
                final int errorCode = field.getInt(ErrorHandlerImpl.class);
                final String[] messages = extractedErrorMessages.get(field.getName());
                final String errorMessage = messages[0];
                final String solution = messages[1];
                generateDitaForOnlineHelp(errorCode, errorMessage, solution);
                assertEquals("Cannot create errorMessage for " + field.getName(), errorMessage,
                        objUnderTest.createErrorMessageWithoutLogging(errorCode, "Test"));
            }

        }
    }

    private void generateDitaForOnlineHelp(final int errorCode, final String errorMessage, final String solution) {
        System.out.println("<strow>");
        System.out.println("<stentry>" + errorCode + "</stentry>");
        System.out.println("<stentry>" + errorMessage + "</stentry>");
        System.out.println("<stentry>" + solution + "</stentry>");
        System.out.println("</strow>");
    }

}
