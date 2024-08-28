/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.cm.scriptengine.ejb.service;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.cm.event.handling.UpgradeEventObserver;

/**
 * This class responds to <code>UpgradeEvent</code> coming in from Service Framework. We annotate with @Startup so that the bean is loaded when the
 * container starts, ready to observe events
 * 
 * @Singleton ensures that only a single bean instance is loaded
 */
@Singleton
@Startup
public class ScriptEngineUpgradeHandlerBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptEngineUpgradeHandlerBean.class);

    @Inject
    UpgradeEventObserver upgradeEventObserver;

    @SuppressWarnings("unused")
    @PostConstruct
    void startup() {
        LOGGER.info("ScriptEngineUpgradeHandlerBean Singleton initialised");
    }
}
