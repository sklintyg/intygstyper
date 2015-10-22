package se.inera.certificate.modules.ts_parent.integration.stub;

import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

public class TSCertificateStoreRestApi {
    @Autowired
    private TSCertificateStore certificateStore;

    @Autowired(required = false)
    private RegisterCertificateResponderStub stub;

    @GET
    @Path("/certificates")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Map<String, String>> getAll() {
        return certificateStore.getAll();
    }

    @DELETE
    @Path("/certificates")
    public void clear() {
        certificateStore.clear();
    }

    @POST
    @Path("/certificates")
    public void fakeException(@QueryParam("fakeException") boolean fakeException) {
        stub.setThrowException(fakeException);
    }
}
