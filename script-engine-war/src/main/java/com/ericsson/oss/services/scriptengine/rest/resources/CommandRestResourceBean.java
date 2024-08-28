package com.ericsson.oss.services.scriptengine.rest.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import static com.ericsson.oss.services.cm.scriptengine.error.ErrorHandlerImpl.MULTIPART_FORM_DATA_HAS_INVALID_KEY_CODE;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.ericsson.oss.services.cm.scriptengine.error.ErrorHandlerImpl;
import com.ericsson.oss.services.scriptengine.api.CommandStatus;
import com.ericsson.oss.services.scriptengine.api.ScriptEngineSessionFacade;
import com.ericsson.oss.services.scriptengine.spi.dtos.AbstractDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;
import com.ericsson.oss.services.scriptengine.spi.dtos.CommandDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.ResponseDto;
import com.ericsson.oss.services.scriptengine.spi.utils.CommandUtils;
import com.google.common.collect.Lists;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@SuppressWarnings("PMD.AvoidCatchingThrowable")
public class CommandRestResourceBean implements CommandRestResource {
    private static final String UNIX_FILE_PATH = "/ericsson/config_mgt/script_engine";
    private static final String COMMAND_RESPONSE_SIZE_HEADER = "ResponseSize";
    private static final String SEC_ADM_COMMAND = "secadm";
    private static final String COMMAND_FORM_KEY = "command";
    private static final String FORM_DATA = "Form Data";
    static final String FILE_PATH_KEY = "filePath";
    private static final String FILE_FORM_KEY = "file:";
    private static final String FILE_NAME_KEY = "fileName";
    private static final String SHARED_FILE_PATH = getFilePath();
    private static final String POLLING_REST_RESOURCE = "/command";
    private static final String SESSION_COOKIE_NAME = "iPlanetDirectoryPro";
    private static final String COMMAND_STATUS_HEADER_NAME = "CommandStatus";

    @Context
    private HttpServletRequest request;

    @Inject
    private CommandResponseCache cache;

    @Inject
    ErrorHandlerImpl errorHandler;

    @Inject
    private ScriptEngineSessionFacade scriptEngineSessionFacade;

    @Inject
    FileHandlerBean fileHandlerBean;

    @Override
    public Response addManagedCommand(final MultipartFormDataInput commandData) {
        Response response;
        try {
            final String key = getCacheKey();
            final Command command = extractCommandFromForm(commandData);
            final CommandStatus commandStatus = scriptEngineSessionFacade.addCommand(key, command);
            addCommandToCache(key, command);
            response = createPostResponse(commandStatus);
        } catch (final SecurityException e) {
            errorHandler.createErrorMessageFromException(e); // For logging purposes
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (final Throwable t) {
            errorHandler.createErrorMessageFromException(t); // For logging purposes
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @Override
    public Response getCommandState() {
        Response response;
        try {
            final String key = getCacheKey();
            final CommandStatus commandStatus = scriptEngineSessionFacade.getCommandStatus(key);
            response = generateResponseForStatusRequest(commandStatus, key);
        } catch (final SecurityException e) {
            errorHandler.createErrorMessageFromException(e); // For logging purposes
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (final Throwable t) {
            errorHandler.createErrorMessageFromException(t); // For logging purposes
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public Response getCommandResponse(final int index, final int offset) {
        Response response;
        String key = null;
        try {
            key = getCacheKey();
            final List<AbstractDto> elements = cache.getMaintainedElements(key, index, offset);
            final List<AbstractDto> nonCachableDtos = cache.getReadOnceElements(key);
            final GetResponseBody getResponseBody = new GetResponseBody(nonCachableDtos, new ResponseDto(elements));
            response = Response.ok(getResponseBody, APPLICATION_JSON).build();
        } catch (final SecurityException e) {
            errorHandler.createErrorMessageFromException(e);
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (final Throwable t) {
            errorHandler.createErrorMessageFromException(t);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        removeCommandAndCommandResources(key);
        return response;
    }

    /*
     * P R I V A T E - M E T H O D S
     */
    private static String getFilePath() {
        final File directory = new File(UNIX_FILE_PATH);

        if (directory.isDirectory()) {
            return UNIX_FILE_PATH;
        } else {
            return System.getProperty("java.io.tmpdir");
        }
    }

    private void removeCommandAndCommandResources(final String terminalKey) {
        // TODO move file handling methods into ScriptEngineSessionFacadeImpl
        if (scriptEngineSessionFacade.doesCommandExistForTerminal(terminalKey)) {
            final Command command = scriptEngineSessionFacade.removeManagedCommand(terminalKey);
            deleteAnyExistingFileFromSystem(command);
        }
    }

    private void deleteAnyExistingFileFromSystem(final Command command) {
        final Map<String, Object> commandProperties = command.getProperties();
        if (commandProperties.containsKey(FILE_PATH_KEY)) {
            final String fileUri = (String) commandProperties.get(FILE_PATH_KEY);
            fileHandlerBean.deleteFile(fileUri);
        }
    }

    private void addCommandToCache(final String terminalKey, final Command command) {
        final List<AbstractDto> elementsToCache = new ArrayList<>(1);
        elementsToCache.add(new CommandDto(command.toString()));
        cache.appendCachedOutput(terminalKey, elementsToCache);
    }

    private Response generateResponseForStatusRequest(final CommandStatus commandStatus, final String terminalKey) {
        // TODO emcgdan: consider using seeOther if CommandStatus.COMPLETE
        final Response.ResponseBuilder responseBuilder = Response.ok().header(COMMAND_STATUS_HEADER_NAME, commandStatus);
        if (CommandStatus.COMPLETE.equals(commandStatus)) {
            final int responseSize = updateCache(terminalKey);
            responseBuilder.header(COMMAND_RESPONSE_SIZE_HEADER, responseSize);
        }
        return responseBuilder.build();
    }

    private int updateCache(final String terminalKey) {
        final ResponseDto responseDto = scriptEngineSessionFacade.getCommandOutput(terminalKey);
        final List<AbstractDto> nonCachableDtos = removeAbstractDtosFromResponse(responseDto);
        final List<AbstractDto> elementsToCache = Lists.reverse(responseDto.getElements());
        cache.appendCachedOutput(terminalKey, elementsToCache, nonCachableDtos);
        return elementsToCache.size() + 1;
    }

    private List<AbstractDto> removeAbstractDtosFromResponse(final ResponseDto responseDto) {
        final List<AbstractDto> nonCachableDtos = getNonCachableDtos(responseDto);
        responseDto.getElements().removeAll(nonCachableDtos);
        return nonCachableDtos;
    }

    private List<AbstractDto> getNonCachableDtos(final ResponseDto responseDto) {
        final List<AbstractDto> dtos = responseDto.getElements();
        final ArrayList<AbstractDto> nonCachable = new ArrayList<>(0);
        for (final AbstractDto dto : dtos) {
            if (!dto.isCacheable()) {
                nonCachable.add(dto);
            }
        }
        return nonCachable;
    }

    private Response createPostResponse(final CommandStatus commandStatus) {
        final Response response = Response.created(URI.create(POLLING_REST_RESOURCE)).header(COMMAND_STATUS_HEADER_NAME, commandStatus).build();
        return response;
    }

    private String getCacheKey() {
        initiateSession();
        String sessionId = null;
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) { // Can be null when no cookies set (during Arq. tests)
            for (final Cookie cookie : cookies) {
                if (SESSION_COOKIE_NAME.equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        if (sessionId == null) {
            throw new SecurityException("No SSO Cookie found (Cookie:" + SESSION_COOKIE_NAME + ")");
        }
        return sessionId;
    }

    private HttpSession initiateSession() {
        /*
         * EEITSIK: Need this to be able to access Cookies (even if httpSession
         * itself not used) getCookies() will throw InvocationTargetException
         * otherwise?!
         */
        final HttpSession httpSession = request.getSession();
        return httpSession;
    }

    private Command extractCommandFromForm(final MultipartFormDataInput input) throws IOException {
        final Map<String, Object> properties = new HashMap<>();
        final Map<String, List<InputPart>> formData = input.getFormDataMap();
        ensureFormContainsCommand(formData);
        String commandString = null;
        // Iterate through the form map and extract into the command object
        Object extractedFormDataObject;
        for (final Map.Entry<String, List<InputPart>> entry : formData.entrySet()) {
            if (entry.getKey().equals(COMMAND_FORM_KEY)) { // Handle command itself - extracted previously
                commandString = extractFormDataForClass(entry.getValue(), String.class);
            } else {
                if (isFileProperty(entry.getKey())) {
                    extractedFormDataObject = extractFormDataForClass(entry.getValue(), byte[].class);
                } else {
                    extractedFormDataObject = extractFormDataForClass(entry.getValue(), String.class);
                }
                properties.put(entry.getKey(), extractedFormDataObject);
            }
        }

        editPropertiesForFileHandling(commandString, properties);

        return CommandUtils.createCommand(commandString, properties);
    }

    private void editPropertiesForFileHandling(final String commandString, final Map<String, Object> properties) throws IOException {
        final Object fileFormData = properties.get(FILE_FORM_KEY);
        final Object fileName = properties.get(FILE_NAME_KEY);
        if (fileFormData != null && fileName != null && !commandString.startsWith(SEC_ADM_COMMAND)) {
            final String filePathUri = generateAbsoluteFileUri(fileName.toString());
            fileHandlerBean.writeFile(filePathUri, (byte[]) fileFormData);
            properties.put(FILE_PATH_KEY, filePathUri);
            properties.remove(FILE_FORM_KEY);
        }
    }

    private void ensureFormContainsCommand(final Map<String, List<InputPart>> formData) {
        if (!formData.containsKey(COMMAND_FORM_KEY)) {
            throw errorHandler.createValidationException(MULTIPART_FORM_DATA_HAS_INVALID_KEY_CODE, FORM_DATA, COMMAND_FORM_KEY);
        }
    }

    private boolean isFileProperty(final String formKey) {
        return formKey.startsWith(FILE_FORM_KEY);
    }

    private <T>T extractFormDataForClass(final List<InputPart> formDataItem, final Class<T> targetClass) throws IOException {
        final InputPart formDataContainer = formDataItem.get(0);
        final T rawData = formDataContainer.getBody(targetClass, null);
        return rawData;
    }

    private String generateAbsoluteFileUri(final String fileName) {
        return (SHARED_FILE_PATH + File.separator + System.currentTimeMillis() + fileName);
    }

}
