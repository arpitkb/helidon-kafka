package com.github.arpitkb.service.kafka;

import com.github.arpitkb.service.model.Stats;
import com.github.arpitkb.service.serdes.JsonDeserializer;
import io.helidon.config.Config;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

@ApplicationScoped
public class Consumer {

    @Inject @ConfigProperty(name="kafka.broker")
    String broker;
    @Inject @ConfigProperty(name="kafka.topic")
    String topic;
    @Inject @ConfigProperty(name = "kafka.group")
    String group;

    Config config;

    KafkaConsumer<String, Stats> consumer;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Consumer(){
    }

    @PostConstruct
    private void init(){

        // set the properties
        Properties properties = new Properties();
        properties.putIfAbsent(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,config.get("kafka.broker").asString().get());
        properties.putIfAbsent(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG , StringDeserializer.class.getName());
        properties.putIfAbsent(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG , JsonDeserializer.class.getName() );
        properties.putIfAbsent(ConsumerConfig.GROUP_ID_CONFIG,config.get("kafka.consumer.group").asString().get());
        properties.putIfAbsent(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new KafkaConsumer<>(properties);


        // subscribe
        consumer.subscribe(Arrays.asList(config.get("kafka.input-topic").asString().get()));

    }

    public ArrayList<Stats> fetchMessages(){

        ArrayList<Stats> arr = new ArrayList<>();
//            final KafkaConsumer<String,WorkFlowStat> consumer = createConsumer();

        final int limit = 3;
        int current = 0;

        while(true){
            final ConsumerRecords<String,Stats> records = consumer.poll(Duration.ofMillis(300));
            if(records.count()==0){
                current++;
                if(current>limit) return arr;
                else continue;
            }
            records.forEach(record -> {
                arr.add(record.value());
            });

            consumer.commitAsync();
            return arr;
        }

    }

    @PreDestroy
    public void cleanUp(){
        consumer.close();
    }

}
