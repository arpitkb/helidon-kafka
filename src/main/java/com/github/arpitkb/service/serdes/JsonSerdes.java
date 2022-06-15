package com.github.arpitkb.service.serdes;

import com.github.arpitkb.service.model.NodeInstance;
import com.github.arpitkb.service.model.Stats;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class JsonSerdes extends Serdes{

    private JsonSerdes(){}

    public static Serde<NodeInstance> NodeInstance(){
        JsonSerializer<NodeInstance> serializer = new JsonSerializer<>();
        JsonDeserializer<NodeInstance> deserializer = new JsonDeserializer<>(NodeInstance.class);

        return Serdes.serdeFrom(serializer,deserializer);
    }

    public static Serde<Stats> Stats(){
        JsonSerializer<Stats> serializer = new JsonSerializer<>();
        JsonDeserializer<Stats> deserializer = new JsonDeserializer<>(Stats.class);

        return Serdes.serdeFrom(serializer,deserializer);
    }
}

