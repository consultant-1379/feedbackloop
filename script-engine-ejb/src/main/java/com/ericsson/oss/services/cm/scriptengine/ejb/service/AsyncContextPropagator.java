package com.ericsson.oss.services.cm.scriptengine.ejb.service;

import com.ericsson.oss.itpf.sdk.context.ContextService;

import javax.inject.Inject;

/**
 * Propagate context value across asynchronous EJB call.
 */
public class AsyncContextPropagator {
    @Inject
    private ContextService ctxService;
    static final String USER_ID = "X-Tor-UserID";


    /**
     * Get the userId value .
     */
    public String getUserId() {
        return (String) ctxService.getContextValue(USER_ID);
    }


    /**
     * Set the userId
     *
     * @param userId
     */
    public void setUserId(final String userId) {
    	ctxService.setContextValue(USER_ID, userId);
    }


}