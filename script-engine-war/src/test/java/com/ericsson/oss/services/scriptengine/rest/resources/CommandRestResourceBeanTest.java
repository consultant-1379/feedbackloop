package com.ericsson.oss.services.scriptengine.rest.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import com.ericsson.oss.services.scriptengine.api.CommandStatus;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.file.FileDownloadRequestDto;
import com.google.common.collect.Lists;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.services.cm.scriptengine.error.ErrorHandlerImpl;
import com.ericsson.oss.services.scriptengine.api.ScriptEngineSessionFacade;
import com.ericsson.oss.services.scriptengine.spi.dtos.AbstractDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.LineDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.ResponseDto;

@RunWith(MockitoJUnitRunner.class)
public class CommandRestResourceBeanTest {
    public static final int HTTP_SUCCESS = 200;
    public static final int HTTP_RESOURCE_CREATED_POST = 201;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int UNAUTHORIZED_ERROR = 401;
    @Mock
    ErrorHandlerImpl errorHandler;

    @InjectMocks
    CommandRestResourceBean objectUnderTest;

    @Mock
    ScriptEngineSessionFacade scriptEngineSessionFacade;

    @Mock
    MultipartFormDataInput commandData;

    @Mock
    HttpServletRequest request;

    @Mock
    CommandResponseCache cache;

    @Mock
    HttpSession httpSession;

    @Mock
    Cookie someOtherCookie;

    @Mock
    Cookie iPlanetDirectoryProCookie;

    @Mock
    RuntimeException runTimeExceptionMock;

    @Mock
    FileHandlerBean fileHandlerBeanMock;

    static final String SSO_SESSION_ID = "SSO Session ID";
    static Cookie[] cookies;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUpMocks() {
        when(request.getSession()).thenReturn(httpSession);
    }

    @Test
    public void canAddManagedCommand() throws IOException {
        mockSsoCookie();
        final InputPart commandInputPart = mock(InputPart.class);
        when(commandInputPart.getBody(String.class, null)).thenReturn("test command");
        final ArrayList<InputPart> commandInputParts = new ArrayList<InputPart>();
        commandInputParts.add(commandInputPart);
        final HashMap<String, List<InputPart>> inputParts = new HashMap<String, List<InputPart>>();
        inputParts.put("command", commandInputParts);
        when(commandData.getFormDataMap()).thenReturn(inputParts);
        final Command testCommand = new Command("test", "command", new HashMap<String, Object>());
        when(scriptEngineSessionFacade.addCommand(SSO_SESSION_ID,testCommand)).thenReturn(CommandStatus.RUNNING);
        final List<AbstractDto> elementsToCache = new ArrayList<>(1);
        elementsToCache.add(new CommandDto(testCommand.toString()));

        Response response = objectUnderTest.addManagedCommand(commandData);
        assertEquals(response.getStatus(), HTTP_RESOURCE_CREATED_POST);
        verify(cache).appendCachedOutput(SSO_SESSION_ID, elementsToCache);
    }

    @Test
    public void canGetCommandStateWhenCommandIsRunning(){
        mockSsoCookie();
        when(scriptEngineSessionFacade.getCommandStatus(SSO_SESSION_ID)).thenReturn(CommandStatus.RUNNING);

        Response response = objectUnderTest.getCommandState();
        assertEquals(HTTP_SUCCESS, response.getStatus());
        assertTrue("Contains CommandStatus Header", response.getMetadata().containsKey("CommandStatus"));
        assertEquals(CommandStatus.RUNNING, (response.getMetadata().get("CommandStatus").get(0)));
    }

    @Test
    public void canGetCommandStateAndResponseSizeWhenCommandIsComplete(){
        mockSsoCookie();
        when(scriptEngineSessionFacade.getCommandStatus(SSO_SESSION_ID)).thenReturn(CommandStatus.COMPLETE);
        final ArrayList<AbstractDto> dtos = getAbstractDtos();
        when(scriptEngineSessionFacade.getCommandOutput(SSO_SESSION_ID)).thenReturn(new ResponseDto(dtos));

        Response response = objectUnderTest.getCommandState();
        assertEquals(HTTP_SUCCESS, response.getStatus());
        assertTrue(response.getMetadata().containsKey("CommandStatus"));
        assertTrue(response.getMetadata().containsKey("ResponseSize"));
        assertEquals(CommandStatus.COMPLETE, response.getMetadata().get("CommandStatus").get(0));
        assertEquals(4, response.getMetadata().get("ResponseSize").get(0));
        verify(cache).appendCachedOutput(SSO_SESSION_ID, Lists.reverse(dtos), new ArrayList<AbstractDto>(0));
    }

    @Test
    public void canGetCommandStateAndResponseSizeWhenCommandIsCompleteAndHasNonCacheableDtos(){
        mockSsoCookie();
        when(scriptEngineSessionFacade.getCommandStatus(SSO_SESSION_ID)).thenReturn(CommandStatus.COMPLETE);
        final ArrayList<AbstractDto> dtos = getAbstractDtos();
        final FileDownloadRequestDto nonCachableDto = new FileDownloadRequestDto("someId", "someFieldId");
        dtos.add(nonCachableDto);
        final ArrayList<AbstractDto> nonCacheableDto = new ArrayList<>(1);
        nonCacheableDto.add(nonCachableDto);
        when(scriptEngineSessionFacade.getCommandOutput(SSO_SESSION_ID)).thenReturn(new ResponseDto(dtos));

        Response response = objectUnderTest.getCommandState();
        assertEquals(HTTP_SUCCESS, response.getStatus());
        assertTrue(response.getMetadata().containsKey("CommandStatus"));
        assertTrue(response.getMetadata().containsKey("ResponseSize"));
        assertEquals(CommandStatus.COMPLETE, response.getMetadata().get("CommandStatus").get(0));
        assertEquals(4, response.getMetadata().get("ResponseSize").get(0));
        dtos.remove(nonCachableDto);
        verify(cache).appendCachedOutput(SSO_SESSION_ID, Lists.reverse(dtos), nonCacheableDto);
    }

    @Test
    public void getCommandResponseWithSsoCookieUsesSsoSessionIdAndReturnsCorrectStatusAndDataWhereNoCommandIsNotComplete() {
        mockSsoCookie();
        when(scriptEngineSessionFacade.getCommandStatus(SSO_SESSION_ID)).thenReturn(CommandStatus.FINISHED);
        final ArrayList<AbstractDto> dtos = new ArrayList<AbstractDto>();
        dtos.add(new LineDto("test"));
        when(cache.getMaintainedElements(SSO_SESSION_ID, 0, 1)).thenReturn(dtos);

        final Response response = objectUnderTest.getCommandResponse(0, 1);
        final GetResponseBody getResponseBody = (GetResponseBody) response.getEntity();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(dtos, getResponseBody.getResponseDto().getElements());
        assertEquals(new ArrayList<AbstractDto>(0), getResponseBody.getNonCachableDtos());
        verify(cache).getMaintainedElements(SSO_SESSION_ID, 0, 1);
    }

    @Test
    public void getCommandReturnsCorrectStatusAndDataWhereNoCommandIsCompleteAndHasNonCachableDtosAndNoFiles(){
        mockSsoCookie();
        FileDownloadRequestDto fileDownloadRequestDto = new FileDownloadRequestDto("someId","someFieldId" );
        final ArrayList<AbstractDto> nonCachable = new ArrayList<>();
        nonCachable.add(fileDownloadRequestDto);
        final ArrayList<AbstractDto> dtos = getAbstractDtos();
        when(scriptEngineSessionFacade.doesCommandExistForTerminal(SSO_SESSION_ID)).thenReturn(true);
        final Command testCommand = new Command("test", "command", new HashMap<String, Object>());
        when(scriptEngineSessionFacade.removeManagedCommand(SSO_SESSION_ID)).thenReturn(testCommand);
        when(cache.getMaintainedElements(SSO_SESSION_ID, 0, 3)).thenReturn(dtos);
        when(cache.getReadOnceElements(SSO_SESSION_ID)).thenReturn(nonCachable);

        final Response response = objectUnderTest.getCommandResponse(0, 3);
        final GetResponseBody getResponseBody = (GetResponseBody) response.getEntity();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(dtos, getResponseBody.getResponseDto().getElements());
        assertEquals(nonCachable, getResponseBody.getNonCachableDtos());
    }

    @Test
    public void getCommandReturnCorrectStatusAndDataWhereFileNeedsToBeDeleted(){
        mockSsoCookie();
        final ArrayList<AbstractDto> dtos = getAbstractDtos();
        when(scriptEngineSessionFacade.doesCommandExistForTerminal(SSO_SESSION_ID)).thenReturn(true);
        Map<String, Object> properties = new HashMap<>();
        String filePathUri = "some/path/for/file";
        properties.put(CommandRestResourceBean.FILE_PATH_KEY, filePathUri);
        final Command testCommand = new Command("test", "command", properties);
        when(scriptEngineSessionFacade.removeManagedCommand(SSO_SESSION_ID)).thenReturn(testCommand);
        when(cache.getMaintainedElements(SSO_SESSION_ID, 0, 3)).thenReturn(dtos);

        final Response response = objectUnderTest.getCommandResponse(0, 3);
        final GetResponseBody getResponseBody = (GetResponseBody) response.getEntity();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(dtos, getResponseBody.getResponseDto().getElements());
        assertEquals(new ArrayList<AbstractDto>(0), getResponseBody.getNonCachableDtos());

        verify(fileHandlerBeanMock).deleteFile(filePathUri);
    }

    /*
     * N E G A T I V E - T E S T S
     */

    @Test
    public void canCatchSecurityExceptionDuringAddManagedCommand(){
        Response response = objectUnderTest.addManagedCommand(commandData);
        assertEquals(UNAUTHORIZED_ERROR, response.getStatus());
        verify(errorHandler).createErrorMessageFromException(any(SecurityException.class));
    }

    @Test
    public void canUnexpectedExceptionDuringAddManagedCommand() throws IOException {
        mockSsoCookie();
        Response response = objectUnderTest.addManagedCommand(commandData);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    public void canCatchSecurityExceptionDuringGetCommandState(){
        Response response = objectUnderTest.getCommandState();
        assertEquals(UNAUTHORIZED_ERROR, response.getStatus());
        verify(errorHandler).createErrorMessageFromException(any(SecurityException.class));
    }

    @Test
    public void canUnexpectedExceptionDuringGetCommandState() throws IOException {
        mockSsoCookie();
        when(scriptEngineSessionFacade.getCommandStatus(SSO_SESSION_ID)).thenThrow(runTimeExceptionMock);
        Response response = objectUnderTest.getCommandState();
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatus());
        verify(errorHandler).createErrorMessageFromException(runTimeExceptionMock);
    }

    @Test
    public void getCommandResponseWithoutAnyCookiesResultsInResponseCode401() {
        assertEquals(UNAUTHORIZED_ERROR, objectUnderTest.getCommandResponse(0, 1).getStatus());
        // Verify correct Exception(type) was logged
        verify(errorHandler).createErrorMessageFromException(any(SecurityException.class));
    }

    @Test
    public void getCommandResponseWithoutSsoCookieResultsInResponseCode401() {
        cookies = new Cookie[] { someOtherCookie };
        when(request.getCookies()).thenReturn(cookies);
        assertEquals(UNAUTHORIZED_ERROR, objectUnderTest.getCommandResponse(0, 1).getStatus());
        // Verify correct Exception(type) was logged
        verify(errorHandler).createErrorMessageFromException(any(SecurityException.class));
    }

    @Test
    public void securityExceptionDuringGetCommandResponseResultsInResponseCode401() {
        when(request.getCookies()).thenThrow(new SecurityException());
        assertEquals(UNAUTHORIZED_ERROR, objectUnderTest.getCommandResponse(0, 1).getStatus());
        // Verify correct Exception(type) was logged
        verify(errorHandler).createErrorMessageFromException(any(SecurityException.class));
    }

    @Test
    public void runtimeExceptionDuringGetCommandResponseResultsInResponseCode500() {
        when(request.getCookies()).thenThrow(new RuntimeException());
        assertEquals(INTERNAL_SERVER_ERROR, objectUnderTest.getCommandResponse(0, 1).getStatus());
        // Verify correct Exception(type) was logged
        verify(errorHandler).createErrorMessageFromException(any(RuntimeException.class));
    }

    private void mockSsoCookie() {
        cookies = new Cookie[] { someOtherCookie, iPlanetDirectoryProCookie };
        when(request.getCookies()).thenReturn(cookies);
        when(iPlanetDirectoryProCookie.getName()).thenReturn("iPlanetDirectoryPro");
        when(iPlanetDirectoryProCookie.getValue()).thenReturn(SSO_SESSION_ID);
    }

    private ArrayList<AbstractDto> getAbstractDtos() {
        final ArrayList<AbstractDto> dtos = new ArrayList<>();
        dtos.add(new LineDto("test1"));
        dtos.add(new LineDto("test2"));
        dtos.add(new LineDto("test3"));
        return dtos;
    }

}
