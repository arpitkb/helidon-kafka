package com.github.arpitkb.service.serdes;

import com.github.arpitkb.service.model.NodeInstance;
import com.github.arpitkb.service.model.Stats;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for Serdes
 *
 * @author prashant
 * @author www.learningjournal.guru
 */

public class JsonSerdes extends Serdes {


    static final class NodeInstanceSerde extends Serdes.WrapperSerde<NodeInstance> {
        NodeInstanceSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }

    public static Serde<NodeInstance> NodeInstance() {
        NodeInstanceSerde serde = new NodeInstanceSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, NodeInstance.class);
        serde.configure(serdeConfigs, false);

        return serde;
    }

    static final class StatsSerde extends Serdes.WrapperSerde<Stats> {
        StatsSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }

    public static Serde<Stats> Stats() {
        StatsSerde serde = new StatsSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, Stats.class);
        serde.configure(serdeConfigs, false);

        return serde;
    }

}

