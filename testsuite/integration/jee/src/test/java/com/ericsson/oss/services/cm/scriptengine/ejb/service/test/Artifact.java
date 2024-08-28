package com.ericsson.oss.services.cm.scriptengine.ejb.service.test;

import java.io.File;
import java.util.ArrayList;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import com.ericsson.oss.services.cm.script.engine.test.dummyhandler.DummyCommandHandler;
import com.ericsson.oss.services.cm.script.engine.test.dummyhandler.DummyReaderWriter;
import com.ericsson.oss.services.cm.script.engine.test.dummyhandler.FileDownloadHandlerImpl;

public class Artifact {
    static final File MANIFEST_MF_FILE = new File("src/test/resources/META-INF/MANIFEST.MF");
    static final File BEANS_XML_FILE = new File("src/test/resources/META-INF/beans.xml");
    private static final String GROUP_ID_CM_SERVICES = "com.ericsson.oss.services.cm";
    private static final String MAVEN_DEPENDENCY_RESOLVER = "org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-api-maven";

    public static final String ORG_JBOSS___RESTEASY = "org.jboss.resteasy:resteasy-jaxrs";

    static Archive<?> createScriptEngineArchive() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "script-engine-test-lib.jar")
                .addAsResource("META-INF/beans.xml", "META-INF/beans.xml").addAsResource(MANIFEST_MF_FILE).addPackage(Artifact.class.getPackage())
                .addClass(DummyCommandHandler.class).addClass(FileDownloadHandlerImpl.class).addClass(DummyReaderWriter.class);
        return archive;
    }

    public static Archive<?> createWARTestArchive() {
        final WebArchive archive = ShrinkWrap.create(WebArchive.class, "ScriptEngineTestWAR").addAsWebInfResource("META-INF/beans.xml")
                .addPackage(ScriptEngineIT.class.getPackage());
        return archive;
    }

    static void addEarRequiredlibraries(final EnterpriseArchive archive) {
        ArrayList<String> dependencies = new ArrayList<>();
        dependencies.add(GROUP_ID_CM_SERVICES + ":" + "cm-common-log");
        dependencies.add(GROUP_ID_CM_SERVICES + ":" + "script-engine-editor-spi");
        dependencies.add(GROUP_ID_CM_SERVICES + ":" + "script-engine-api");
        dependencies.add(GROUP_ID_CM_SERVICES + ":" + "cm-reader-api");
        dependencies.add(GROUP_ID_CM_SERVICES + ":" + "cm-writer-api");
        dependencies.add(ORG_JBOSS___RESTEASY);
        dependencies.add(MAVEN_DEPENDENCY_RESOLVER);
        archive.addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml").resolve(dependencies).withTransitivity().asFile());
    }
}
