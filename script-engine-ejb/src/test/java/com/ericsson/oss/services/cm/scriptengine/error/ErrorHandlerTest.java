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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.sdk.recording.ErrorSeverity;
import com.ericsson.oss.itpf.sdk.recording.SystemRecorder;

@RunWith(MockitoJUnitRunner.class)
public class ErrorHandlerTest {

    @Mock
    @Inject
    private SystemRecorder systemRecorder;

    @InjectMocks
    // this will do all the magic!! Mockito will inject your mocked session
    // façade into the appropriate field … so no need for a @Before method
    ErrorHandlerImpl objUnderTest = new ErrorHandlerImpl();

    @Test
    public void createErrorMessageFromExceptionReturnsExceptionsMainMessage() {
        final String errorMessage = "my error message";
        final Throwable t = new Throwable(errorMessage);
        final String result = objUnderTest.createErrorMessageFromException(t);
        assertTrue(result.contains(errorMessage));
        verify(systemRecorder).recordError(anyString(), any(ErrorSeverity.class), anyString(), anyString(), anyString());
    }

    @Test
    public void createErrorMessageFromExceptionFromFormValidation() {
        final FormValidationException exp = objUnderTest.createValidationException(ErrorHandlerImpl.MULTIPART_FORM_DATA_HAS_INVALID_KEY_CODE, "",
                "command");
        final String message = exp.getMessage();
        assertTrue(message.contains("Invalid multipart form request key"));
        verify(systemRecorder).recordError(anyString(), any(ErrorSeverity.class), anyString(), anyString(), anyString());
    }

}
