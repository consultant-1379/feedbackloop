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

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ScriptEngineUpgradeHandlerBeanTest {

    ScriptEngineUpgradeHandlerBean objUnderTest;

    @Before
    public void canCreateCmReaderUpgradeHandlerBean() {
        objUnderTest = new ScriptEngineUpgradeHandlerBean();
        assertNotNull(objUnderTest);
    }

    @Test
    public void startupLogsItsOwnInitiation() {
        objUnderTest.startup();
    }

}
