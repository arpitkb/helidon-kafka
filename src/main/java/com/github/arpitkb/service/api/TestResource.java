package com.github.arpitkb.service.api;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

@Path("/test")
@ApplicationScoped
public class TestResource {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());



    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getDefaultMessage() {
        return createResponse("World");
    }

    private JsonObject createResponse(String who) {
        String msg = String.format("%s %s!", "Hello", who);

        return JSON.createObjectBuilder()
                .add("message", msg)
                .build();
    }

}


