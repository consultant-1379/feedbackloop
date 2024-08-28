package com.ericsson.oss.services.scriptengine.rest.resources;


import com.ericsson.oss.services.scriptengine.spi.dtos.AbstractDto;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.Before;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CachedOutputTest {
    @Mock
    AbstractDto a,b,c,x,y,z;

    CachedOutput objectUnderTest;
    List<AbstractDto> maintainedElements;
    List<AbstractDto> readOnceElements;

    @Before
    public void setup(){
        maintainedElements = new ArrayList<>();
        maintainedElements.add(a);
        maintainedElements.add(b);
        maintainedElements.add(c);
        readOnceElements = new ArrayList<>();
        readOnceElements.add(x);
        readOnceElements.add(y);
        readOnceElements.add(z);
        objectUnderTest = new CachedOutput(maintainedElements,readOnceElements);
    }

    @Test
    public void canCreateObjectWithMaintainedElementsListOnly(){
        objectUnderTest = new CachedOutput(maintainedElements);
        assertNotNull(objectUnderTest);
        assertEquals(maintainedElements, objectUnderTest.getMaintainedElements());
        assertEquals(new ArrayList<AbstractDto>(0), objectUnderTest.removeReadOnceElements());
    }

    @Test
    public void canReadMaintainedElements(){
        List<AbstractDto> storedMaintainedElements = objectUnderTest.getMaintainedElements();
        assertEquals(maintainedElements, storedMaintainedElements);
    }

    @Test
    public void removeReadOnceElementsReturnsAnEmptyListIfCalledMultipleTimes(){
        List<AbstractDto> storedMaintainedElements = objectUnderTest.removeReadOnceElements();
        assertEquals(readOnceElements, storedMaintainedElements);
        storedMaintainedElements = objectUnderTest.removeReadOnceElements();
        assertEquals(new ArrayList<>(0), storedMaintainedElements);
    }

    @Test
    public void coverageTestForEquas(){
        List<AbstractDto>  maintainedElements = new ArrayList<>();
        maintainedElements.add(a);
        maintainedElements.add(b);
        maintainedElements.add(c);
        List<AbstractDto> readOnceElements = new ArrayList<>();
        readOnceElements.add(x);
        readOnceElements.add(y);
        readOnceElements.add(z);
        CachedOutput cachedOutput = new CachedOutput(maintainedElements, readOnceElements);

        assertTrue(objectUnderTest.equals(objectUnderTest));
        assertTrue(objectUnderTest.equals(cachedOutput));
        assertTrue(cachedOutput.equals(objectUnderTest));

        objectUnderTest.removeReadOnceElements();
        assertFalse(objectUnderTest.equals(cachedOutput));
        assertFalse(objectUnderTest.equals(null));
        assertFalse(objectUnderTest.equals(new Object()));
        cachedOutput = new CachedOutput(new ArrayList<AbstractDto>(0), readOnceElements);
        assertFalse(objectUnderTest.equals(cachedOutput));
    }

    @Test
    public void coverageTestForHashCode(){
        List<AbstractDto>  maintainedElements = new ArrayList<>();
        maintainedElements.add(a);
        maintainedElements.add(b);
        maintainedElements.add(c);
        List<AbstractDto> readOnceElements = new ArrayList<>();
        readOnceElements.add(x);
        readOnceElements.add(y);
        readOnceElements.add(z);
        CachedOutput cachedOutput = new CachedOutput(maintainedElements, readOnceElements);

        assertEquals(cachedOutput.hashCode(),objectUnderTest.hashCode());
    }
}
