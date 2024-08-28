package com.ericsson.oss.services.cm.scriptengine.ejb.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import com.ericsson.oss.services.scriptengine.api.CommandStatus;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.Header;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.script.engine.test.dummyhandler.FileDownloadHandlerImpl;

@RunWith(Arquillian.class)
public class ScriptEngineIT extends ScriptEngineTestBase {

    private static final String SCRIPT_ENGINE_BASE_URL = "http://localhost:8080/script-engine/services/command/";
    private static final String SCRIPT_ENGINE_STATUS_URL = SCRIPT_ENGINE_BASE_URL+"status";
    private static final String SCRIPT_ENGINE_OUTPUT_URL = SCRIPT_ENGINE_BASE_URL+"output/%s/%s";
    private static final String FILE_REST_URL = "http://localhost:8080/script-engine/services/files";
    private static final String DUMMY_SUCCESS = "dummy testForSuccess";
    private static final String DUMMY_ERROR = "dummy error";
    private static final String COMMAND_WITHOUT_HANDLER = "fake-handler command";
    private static final String COMMAND_WITHOUT_HANDLER_ERROR = "Unrecognized CLI command fake-handler";
    private static final String COMMAND = "command";
    private static final String DUMMY_SOLUTION = "some solution";
    private static final String ALIAS_CREATE_COMMAND_VALID = "dummy alias \"kaosTestAlias01\" \"cmedit get *\"";
    private static final String ALIAS_CREATE_COMMAND_INVALID = "dummy alias-error \"kaosTestAlias01 $1\" \"cmedit get *\"";
    public static final String COMMAND_STATUS_HEADER = "commandstatus";
    public static final String RESPONSE_SIZE_HEADER = "responsesize";
    public static final int MAX_TRYS = 5;
    public static final int POSITIVE_HTTP_GET = 200;

    final HttpClient httpClient = new HttpClient();

    @Test
    public void postRequestGetsRoutedToDummyCommandHandler() throws Exception {
        final String response = sendPostRequestToScriptEngineRESTinterfaceAndWaitForResponse(DUMMY_SUCCESS);
        assertTrue(response.contains("Command Executed Successfully"));
    }

    @Test
    public void errorPostRequestGetsRoutedToDummyCommandHandler() throws Exception {
        final String response = sendPostRequestToScriptEngineRESTinterfaceAndWaitForResponse(DUMMY_ERROR);
        assertTrue(response.contains("Command Execution Failed"));
        assertTrue(response.contains(DUMMY_SOLUTION));
    }

    @Test
    public void postRequestFailsIfSpecifiedCommandHandlerIsNotPresent() throws Exception {
        final String response = sendPostRequestToScriptEngineRESTinterfaceAndWaitForResponse(COMMAND_WITHOUT_HANDLER);
        assertTrue(response.contains(COMMAND_WITHOUT_HANDLER_ERROR));
    }

    @Test
    public void postRequestAliasCreateGetsRoutedToDummyCommandHandler() throws Exception {
        final String response = sendPostRequestToScriptEngineRESTinterfaceAndWaitForResponse(ALIAS_CREATE_COMMAND_VALID);
        assertTrue(response.contains("alias created"));
    }

    @Test
    public void errorPostRequestAliasCreateGetsRoutedToDummyCommandHandler() throws Exception {
        final String response = sendPostRequestToScriptEngineRESTinterfaceAndWaitForResponse(ALIAS_CREATE_COMMAND_INVALID);
        assertTrue(response.contains("alias not created"));
    }

    @Test
    public void commandStatusEqualsFinishedAfterCommandIsComplete() throws Exception{
    	createResourceScriptEngineRestInterfacePost(DUMMY_SUCCESS);
        String responseSize = waitForCommandToFinish();
        final HeadMethod head = new HeadMethod(SCRIPT_ENGINE_STATUS_URL);
        addSsoHeader(head);
        int httpResponse = httpClient.executeMethod(head);
        assertEquals(200, httpResponse);
        assertEquals(CommandStatus.FINISHED.toString(), head.getResponseHeader(COMMAND_STATUS_HEADER).getValue());
    }
    /**
     * Tests successful download of file stored in-memory. The REST request is routed to the <code>FileDownloadHandlerImpl</code> which returns the
     * required data for downloading the file from in-memory.
     */
    @Test
    public void testDownloadFileFromInMemory() throws IOException {
        testFileDownload("inMemory");
    }

    /**
     * Tests successful download of file stored on the filesystem. The REST request is routed to the <code>FileDownloadHandlerImpl</code> which
     * returns the required data for downloading the file from the file system.
     */
    @Test
    public void testDownloadFileFromFileSystem() throws IOException {
        testFileDownload("fromFileSystem");
    }

    /*
     * P R I V A T E - M E T H O D S
     */

    private String sendPostRequestToScriptEngineRESTinterfaceAndWaitForResponse(final String command) throws Exception {
        createResourceScriptEngineRestInterfacePost(command);
        String responseSize = waitForCommandToFinish();
        return sendGetForResponseOutPut("0",responseSize );
    }

    private int createResourceScriptEngineRestInterfacePost(final String command) throws Exception {
        final PostMethod post = new PostMethod(SCRIPT_ENGINE_BASE_URL);
        post.addRequestHeader("Accept", "application/json");
        addSsoHeader(post);
        final Part[] parts = { new StringPart(COMMAND, command), };
        post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
        return httpClient.executeMethod(post);
    }

    private String waitForCommandToFinish() throws IOException, InterruptedException {
        final HeadMethod head = new HeadMethod(SCRIPT_ENGINE_STATUS_URL);
        addSsoHeader(head);
        String commandStatusHeader;
        int response;
        int timeOut = 0;
        do{
            response = httpClient.executeMethod(head);
            ++timeOut;
            Thread.sleep(1000);
            assertEquals("Head Method Should Return Positive Response", POSITIVE_HTTP_GET, response);
            commandStatusHeader = head.getResponseHeader(COMMAND_STATUS_HEADER).getValue();
        }while(!(commandStatusHeader.equals(CommandStatus.COMPLETE.toString()) || timeOut > MAX_TRYS));
        return head.getResponseHeader(RESPONSE_SIZE_HEADER).getValue();
    }

    private String sendGetForResponseOutPut(String index, String offset) throws IOException {
        final GetMethod get = new GetMethod(String.format(SCRIPT_ENGINE_OUTPUT_URL,index,offset));
        addSsoHeader(get);
        httpClient.executeMethod(get);
        assertEquals(Response.Status.OK.getStatusCode(), get.getStatusCode());
        return get.getResponseBodyAsString();
    }

    private void addSsoHeader(HttpMethodBase httpMethod) {
        final String ssoCookieAsString = "iPlanetDirectoryPro=AQIC5wM2LY4SfcwNPrcXlzFTsoG5t9sdf_DBM-t2ubdkyZc.AAJTSQACMDE";
        httpMethod.addRequestHeader("Cookie", ssoCookieAsString);
    }

    private void testFileDownload(final String fileId) throws IOException {
        final String url = FILE_REST_URL + "?fileId=" + fileId + "&applicationId=" + FileDownloadHandlerImpl.class.getSimpleName();
        final GetMethod getRequest = new GetMethod(url);
        httpClient.executeMethod(getRequest);
        assertEquals(FileDownloadHandlerImpl.FILE_CONTENTS, getRequest.getResponseBodyAsString());
    }

}
