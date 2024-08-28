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

import java.util.HashMap;
import java.util.Map;

import com.ericsson.oss.services.cm.log.AbstractErrorHandler;

public class ErrorHandlerImpl extends AbstractErrorHandler {
    static final String COMPONENT_NAME = "Script Engine";
    private static Map<Integer, String[]> errorMessages = new HashMap<>();
    private static final int ERROR_MESSAGE_INDEX = 0;
    private static final int SOLUTION_MESSAGE_INDEX = 1;
    public static final int UNRECOGNISED_CLI_COMMAND_CODE = 6001;
    public static final String UNRECOGNISED_CLI_COMMAND_MSGS[] = { "Unrecognized CLI command {0}", "Please check the online help for valid CLI commands" };
    public static final int MULTIPART_FORM_DATA_HAS_INVALID_KEY_CODE = 6002;
    public static final String MULTIPART_FORM_DATA_HAS_INVALID_KEY_MSGS[] = { "Invalid multipart form request key",
            "The multipart form request key '{0}' must be set in the form data" };
    public static final int ALIAS_INVALID_CREATE_ERROR_CODE = 6003;
    public static final String ALIAS_INVALID_CREATE_ERROR_MSGS[] = {
            "Invalid alias command specification",
            "The number of arguments in the alias must match the number of arguments in the aliased command. Arguments must be unique and sequential in the range of $1 to $9" };
    public static final int ALIAS_ALREADY_EXISTS_ERROR_CODE = 6004;
    public static final String ALIAS_ALREADY_EXISTS_ERROR_MSGS[] = { "Alias with name {0} already exists", "Create an alias with an unique name" };
    public static final int ALIAS_INVALID_EXECUTE_ERROR_CODE = 6005;
    public static final String ALIAS_INVALID_EXECUTE_ERROR_MSGS[] = { "Alias with name {0} expects {1} argument(s)",
            "The number of arguments in the alias must match the number of arguments in the aliased command" };
    public static final int ALIAS_SYNTAX_ERROR_CODE = 6006;
    public static final String ALIAS_SYNTAX_ERROR_MSGS[] = { "Invalid alias command syntax", COMMON_PARSER_SOLUTION };
    static {
        errorMessages.put(UNRECOGNISED_CLI_COMMAND_CODE, UNRECOGNISED_CLI_COMMAND_MSGS);
        errorMessages.put(MULTIPART_FORM_DATA_HAS_INVALID_KEY_CODE, MULTIPART_FORM_DATA_HAS_INVALID_KEY_MSGS);
        errorMessages.put(ALIAS_INVALID_CREATE_ERROR_CODE, ALIAS_INVALID_CREATE_ERROR_MSGS);
        errorMessages.put(ALIAS_ALREADY_EXISTS_ERROR_CODE, ALIAS_ALREADY_EXISTS_ERROR_MSGS);
        errorMessages.put(ALIAS_INVALID_EXECUTE_ERROR_CODE, ALIAS_INVALID_EXECUTE_ERROR_MSGS);
        errorMessages.put(ALIAS_SYNTAX_ERROR_CODE, ALIAS_SYNTAX_ERROR_MSGS);
    }

    @Override
    protected String getComponentName() {
        return COMPONENT_NAME;
    }

    @Override
    protected String getErrorMessageForCode(final int errorCode) {
        return errorMessages.get(errorCode)[ERROR_MESSAGE_INDEX];
    }

    @Override
    public String getSolutionForCode(final int errorCode) {
        return errorMessages.get(errorCode)[SOLUTION_MESSAGE_INDEX];
    }

    public FormValidationException createValidationException(final int errorCode, final String resource, final Object... additionalInfo) {
        final String errorMessage = createErrorMessage(errorCode, resource, additionalInfo);
        return new FormValidationException(errorMessage);
    }
}