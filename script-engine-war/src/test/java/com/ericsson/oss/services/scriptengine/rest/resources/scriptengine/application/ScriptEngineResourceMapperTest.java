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
package com.ericsson.oss.services.scriptengine.rest.resources.scriptengine.application;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.*;

import org.junit.Test;

import com.ericsson.oss.services.scriptengine.rest.resources.CommandRestResourceBean;
import com.ericsson.oss.services.scriptengine.rest.resources.FileRestResourceBean;

public class ScriptEngineResourceMapperTest {
    
    private final ScriptEngineResourceMapper objUnderTest = new ScriptEngineResourceMapper();

    @Test
    public void getSingletonsReturnsEmptyCollection() {
        assertEquals(0, objUnderTest.getSingletons().size());
    }

    @Test
    public void getClassesReturnsCollectionOfAllResourceClasses() {
        final List<Class<?>> expectedClassNames = new ArrayList<>();
        expectedClassNames.add(CommandRestResourceBean.class);
        expectedClassNames.add(FileRestResourceBean.class);

        final Set<Class<?>> classes = objUnderTest.getClasses();
        assertEquals(expectedClassNames.size(), classes.size());
        assertThat(classes, containsInAnyOrder(expectedClassNames.toArray()));
    }

}
