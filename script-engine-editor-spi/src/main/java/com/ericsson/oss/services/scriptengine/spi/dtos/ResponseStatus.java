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
package com.ericsson.oss.services.scriptengine.spi.dtos;

public interface ResponseStatus {

    int SUCCESS = 0;
    int COMMAND_EXECUTION_ERROR = -1;
    int UNEXPECTED_ERROR = -2;
    int COMMAND_SYNTAX_ERROR = -3;

    /**
     * Retrieve the statusCode of the Response
     * 
     * @return the statusCode of the Response<br>
     *         The status code can have the following values:<br>
     * <br>
     *         -2 : Unexpected Error, there is something wrong with the system or a bug has been encountered.<br>
     *         -1 : Execution Error, a business rule has been broken e.g. incorrect value given for an attribute.<br>
     *         0 : Success but no data returned e.g. Command was execute successfully<br>
     *         >0 : Success, the value indicates the number of CmObjects returned in the response<br>
     */
    int getStatusCode();

    /**
     * Set the statusCode of the Response
     * 
     * @param successCode
     *            the statusCode of the Response<br>
     *            -2 : Unexpected Error, there is something wrong with the system or a bug has been encountered.<br>
     *            -1 : Execution Error, a business rule has been broken e.g. incorrect value given for an attribute.<br>
     *            0 : Success but no data returned e.g. Command was execute successfully<br>
     *            >0 : Success, the value indicates the number of CmObjects returned in the response<br>
     */
    void setStatusCode(int successCode);

    /**
     * Retrieve the statusMessage of the Response
     * 
     * @return the statusMessage of the Response
     */
    String getStatusMessage();

    /**
     * Set the statusMessage of the Response
     * 
     * @param successMessage
     *            the statusMessage of the Response
     */
    void setStatusMessage(String successMessage);

    /**
     * Retrieve the errorCode for this response. Each error in business logic has a unique code. The errorCode will only be set when an error occurs
     * 
     * @return the errorCode for this response
     */
    int getErrorCode();

    /**
     * Set the errorCode for this response
     * 
     * @param errorCode
     *            the errorCode for this response
     */
    void setErrorCode(final int errorCode);

    /**
     * Retrieve the solution (message) for the error. The solution will only be set when an error occurs
     * 
     * @return the solution (message) for the error
     */
    String getSolution();

    /**
     * Set the solution (message) for the error
     * 
     * @param solution
     *            the solution (message) for the error
     */
    void setSolution(final String solution);

}