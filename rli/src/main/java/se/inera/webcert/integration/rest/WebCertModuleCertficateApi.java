/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.webcert.integration.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * API exposing services for use in modules extending WebCert.
 * 
 * @author nikpet
 * 
 */
@Path("certificate")
public interface WebCertModuleCertficateApi {

    /**
     * Returns the draft certificate as JSON identified by the certificateId.
     * 
     * @param certificateId
     * @return a JSON object
     */
    @Path("{certId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Object getCertificate(@PathParam("certId") String certificateId);

    // TODO: Add method for copying a certificate

    /**
     * Revokes the certificate identified by certificateId
     * 
     * @param certificateId
     */
    @Path("{certId}/revoke")
    @POST
    void revokeCertificate(@PathParam("certId") String certificateId);

}
