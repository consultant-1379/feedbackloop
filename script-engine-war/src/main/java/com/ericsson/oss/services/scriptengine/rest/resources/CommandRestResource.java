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
package com.ericsson.oss.services.scriptengine.rest.resources;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/")
public interface CommandRestResource {

    /**
     * Send a CLI command
     *
     * @param commandData
     * @return Response containing the result of the executeCommand
    */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/command")
    Response addManagedCommand(final MultipartFormDataInput commandData) throws IOException;

    /**
     * Check the state of command
     *
     * @return
     * @throws IOException
     */
    @HEAD
    @Path("/command/status")
    Response getCommandState() throws IOException;

    /**
     * Get the output for CLI
     *
     * @param index
     * @param offset
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/command/output/{index}/{offset}")
    Response getCommandResponse(final @PathParam("index") int index, final @PathParam("offset") int offset);
}
