package com.github.arpitkb.service.api;

import com.github.arpitkb.service.kafka.StreamApplication;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;

@Path("/")
@ApplicationScoped
public class StreamResource {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

    /**
     * Kafka consumer
     */

    private StreamApplication streamApplication;

    @Inject
    public StreamResource( StreamApplication streamApplication){
        this.streamApplication=streamApplication;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage() throws IOException {

        this.streamApplication.run();

        JsonObject entity =  JSON.createObjectBuilder()
                .add("message", "kafka stream started successfully")
                .build();

        return Response.status(Response.Status.ACCEPTED).entity(entity).build();

    }

}

