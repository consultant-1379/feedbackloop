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

import java.io.Serializable;

public abstract class AbstractResponse implements Serializable, ResponseStatus {

    private static final long serialVersionUID = -8584023780718645115L;
    private int statusCode;
    private String statusMessage;
    private int errorCode;
    private String solution;

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.services.cm.scriptengine.spi.dtos.AbstractResponse.java#getStatusCode ()
     */
    @Override
    public int getStatusCode() {
        return statusCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.services.cm.scriptengine.spi.dtos.AbstractResponse.java#setStatusCode (int)
     */
    @Override
    public void setStatusCode(final int successCode) {
        statusCode = successCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.services.cm.scriptengine.spi.dtos.AbstractResponse.java#getStatusMessage ()
     */
    @Override
    public String getStatusMessage() {
        return statusMessage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.services.cm.scriptengine.spi.dtos.AbstractResponse.java#setStatusMessage (java.lang.String)
     */
    @Override
    public void setStatusMessage(final String successMessage) {
        statusMessage = successMessage;
    }

    /**
     * Retrieve the errorCode for this response. Each error in business logic has a unique code. The errorCode will only be set when an error occurs
     * 
     * @return the errorCode for this response
     */
    @Override
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Set the errorCode for this response
     * 
     * @param errorCode
     *            the errorCode for this response
     */
    @Override
    public void setErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Retrieve the solution (message) for the error. The solution will only be set when an error occurs
     * 
     * @return the solution (message) for the error
     */
    @Override
    public String getSolution() {
        return solution;
    }

    /**
     * Set the solution (message) for the error
     * 
     * @param solution
     *            the solution (message) for the error
     */
    @Override
    public void setSolution(final String solution) {
        this.solution = solution;
    }

}