package com.ericsson.oss.services.cm.scriptengine.ejb.service;

import com.ericsson.oss.itpf.sdk.context.ContextService;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AsyncContextPropagatorTest {
    @Mock
    ContextService contextServiceMock;
    @InjectMocks
    AsyncContextPropagator objectUnderTest;
    String someUserName = "bob";

    @Test
    public void canGetUserIdFromContext(){
        when(contextServiceMock.getContextValue(AsyncContextPropagator.USER_ID)).thenReturn(someUserName);
        assertEquals(someUserName, objectUnderTest.getUserId());
    }
    @Test
    public void canSetUserIdToContext(){
        objectUnderTest.setUserId(someUserName);
        verify(contextServiceMock).setContextValue(AsyncContextPropagator.USER_ID, someUserName);
    }


}
