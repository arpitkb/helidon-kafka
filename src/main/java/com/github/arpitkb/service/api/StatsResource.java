package com.github.arpitkb.service.api;


import com.github.arpitkb.service.kafka.Consumer;
import com.github.arpitkb.service.kafka.StreamApplication;
import com.github.arpitkb.service.model.Stats;
import com.github.arpitkb.service.utils.StatsRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;

@Path("/stats")
@ApplicationScoped
public class StatsResource {

    private final StatsRepository statsRepository;

    /**
     * Kafka consumer
     */

    private Consumer consumer;


    @Inject
    public StatsResource(Consumer consumer, StatsRepository statsRepository){
        this.consumer = consumer;
        this.statsRepository = statsRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage() throws IOException {

        ArrayList<Stats> res = consumer.fetchMessages();

        this.statsRepository.updateStats(res);

        return Response.ok(this.statsRepository.getAll()).build();

    }

}
