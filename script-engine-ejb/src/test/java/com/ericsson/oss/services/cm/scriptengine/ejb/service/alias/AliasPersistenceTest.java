package com.ericsson.oss.services.cm.scriptengine.ejb.service.alias;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.services.cli.alias.model.CliAlias;
import com.ericsson.oss.services.cm.cmreader.api.CmReaderService;
import com.ericsson.oss.services.cm.cmshared.dto.CmObject;
import com.ericsson.oss.services.cm.cmshared.dto.CmResponse;
import com.ericsson.oss.services.cm.cmshared.dto.search.CmSearchCriteria;
import com.ericsson.oss.services.cm.cmwriter.api.CmWriterService;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.CannotFindAliasException;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.CannotPersistAliasException;

@RunWith(MockitoJUnitRunner.class)
public class AliasPersistenceTest {
    @Mock
    @Inject
    CmWriterService cmWriterService;

    @Mock
    @Inject
    CmReaderService cmReaderService;

    private final static String DUMMY_ALIAS_NAME = "dummy alias";

    @InjectMocks
    AliasPersistence objectUnderTest = new AliasPersistence();

    @SuppressWarnings("unchecked")
    @Test(expected = CannotPersistAliasException.class)
    public void canExecuteSaveCliAliasFailForCmWriterException() {
        final CmResponse cmResponse = createTestCmResponse(-1);
        when(cmWriterService.createPersistenceObject(anyString(), anyString(), anyString(), any(Map.class), anyString())).thenReturn(cmResponse);
        objectUnderTest.saveAlias(new CliAlias());
    }

    @Test(expected = CannotFindAliasException.class)
    public void canExecuteFindCliAliasFailForCmWriterException() {
        final CmResponse cmResponse = createTestCmResponse(-1);
        when(cmReaderService.search(any(CmSearchCriteria.class), anyString())).thenReturn(cmResponse);
        objectUnderTest.findAlias(DUMMY_ALIAS_NAME);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void canExecuteSaveCliAliasSuccess() throws IllegalArgumentException, IllegalAccessException {
        final CmResponse cmResponse = createTestCmResponse(0);
        when(cmWriterService.createPersistenceObject(anyString(), anyString(), anyString(), any(Map.class), anyString())).thenReturn(cmResponse);
        objectUnderTest.saveAlias(new CliAlias());
        verify(cmWriterService).createPersistenceObject(anyString(), anyString(), anyString(), any(Map.class), anyString());
    }

    @Test
    public void canExecuteFindCliAliasSuccess() throws IllegalArgumentException, IllegalAccessException {
        final CmResponse cmResponse = createTestCmResponse(0);
        cmResponse.setCmObjects(new CmObject());
        when(cmReaderService.search(any(CmSearchCriteria.class), anyString())).thenReturn(cmResponse);
        assertNotNull(objectUnderTest.findAlias(DUMMY_ALIAS_NAME));
    }

    @Test
    public void canExecuteIsDefinedAliasSuccess() throws IllegalArgumentException, IllegalAccessException {
        final CmResponse cmResponse = createTestCmResponse(0);
        cmResponse.setCmObjects(new CmObject());
        when(cmReaderService.search(any(CmSearchCriteria.class), anyString())).thenReturn(cmResponse);
        assertTrue(objectUnderTest.isDefinedAlias(DUMMY_ALIAS_NAME));
    }

    @Test
    public void canExecuteIsDefinedAliasFailNoCmObject() throws IllegalArgumentException, IllegalAccessException {
        final CmResponse cmResponse = createTestCmResponse(0);
        when(cmReaderService.search(any(CmSearchCriteria.class), anyString())).thenReturn(cmResponse);
        assertFalse(objectUnderTest.isDefinedAlias(DUMMY_ALIAS_NAME));
    }

    /*
     * P R I V A T E - M E T H O D S
     */
    private CmResponse createTestCmResponse(final int statusCode) {
        final CmResponse cmResponse = new CmResponse();
        cmResponse.setStatusCode(statusCode);
        return cmResponse;
    }
}
