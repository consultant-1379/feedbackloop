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
package com.ericsson.oss.services.cm.scriptengine.ejb.service.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;

public class ScriptEngineTestBase {

    @Deployment(name = "ScritpEngineServiceBeanEar")
    public static Archive<?> createTestArchive() {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class);
        Artifact.addEarRequiredlibraries(ear);
        ear.addAsModule(Artifact.createScriptEngineArchive());
        ear.addAsModule(Artifact.createWARTestArchive());
        ear.setManifest(Artifact.MANIFEST_MF_FILE);
        ear.addAsApplicationResource(Artifact.BEANS_XML_FILE);
        return ear;
    }
}
