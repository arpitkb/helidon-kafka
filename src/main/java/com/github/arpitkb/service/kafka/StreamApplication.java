package com.github.arpitkb.service.kafka;



import com.github.arpitkb.service.model.NodeInstance;
import com.github.arpitkb.service.model.Stats;
import com.github.arpitkb.service.serdes.JsonSerdes;
import io.helidon.config.Config;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@ApplicationScoped
public class StreamApplication{

    static KafkaStreams kafkaStreams;
    static CountDownLatch latch;
    @Inject @ConfigProperty(name="kafka.broker")
    String broker;
    @Inject @ConfigProperty(name="kafka.input-topic")
    String inputTopic;
    @Inject @ConfigProperty(name="kafka.output-topic")
    String outputTopic;
    @Inject @ConfigProperty(name = "kafka.stream.application-id")
    String id;


    StreamApplication(){}

    @PostConstruct
    private void init() {

        Logger logger = LoggerFactory.getLogger(StreamApplication.class);

        Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,broker);
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG,id);

        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, NodeInstance> input = builder.stream(inputTopic, Consumed.with(JsonSerdes.String(), JsonSerdes.NodeInstance()));


        // build topology
        KStream<String,NodeInstance> in2 = input.filter((k, v)->v.getParentId() == null);
        KStream<String,NodeInstance> in3 = in2.merge(in2.flatMapValues(v->v.getNodes()));

        KGroupedStream<String,NodeInstance> in4 =  in3.selectKey((k,v)->v.getName()).groupByKey(Grouped.with(Serdes.String() ,JsonSerdes.NodeInstance()));

        KTable<String, Stats> output = in4.aggregate(
                ()-> new Stats(0L,"temp",0L,0L,"","","",""),
                (k,v,agg)-> {
                    if(v.getStatus().get().equals("Success")) agg.setSuccess(agg.getSuccess()+1);
                    else agg.setFailure(agg.getFailure()+1);
                    agg.setCount(agg.getCount()+1);
                    agg.setName(v.getName());
                    agg.setDescription(v.getDescription());
                    agg.setId(v.getId());
                    agg.setParentId(v.getParentId());
                    agg.setRootId(v.getRootId());
                    return agg;
                },

                Materialized.with(JsonSerdes.String(),JsonSerdes.Stats())
        );
        output.toStream().to(outputTopic,Produced.with(Serdes.String(),JsonSerdes.Stats()));


        kafkaStreams = new KafkaStreams(builder.build(),props);
        latch = new CountDownLatch(1);

    }

        public void startStream(){
            try{
                kafkaStreams.start();
            }catch (final Throwable e){
                System.exit(1);
            }
        }

    @PreDestroy
    public void cleanUp(){
        kafkaStreams.close();
    }
}



//public class StreamApplication{
//
//
//    @Inject @ConfigProperty(name="kafka.input-topic")
//    static String inputTopic;
//    @Inject @ConfigProperty(name="kafka.output-topic")
//    static String outputTopic;
//    @Inject @ConfigProperty(name = "kafka.broker")
//    static String broker;
//    @Inject @ConfigProperty(name = "kafka.stream.application-id")
//    static String app_id;
//
//
//    public static void main(String[] args) {
//
//        Logger logger = LoggerFactory.getLogger(StreamApplication.class);
//
//        Properties props = new Properties();
//        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
//        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG,"helidon-stream01");
//
//        final StreamsBuilder builder = new StreamsBuilder();
//        KStream<String, NodeInstance> input = builder.stream("helidon-input01", Consumed.with(Serdes.String(),JsonSerdes.NodeInstance()));
//
//
//        // build topology
//        KStream<String,NodeInstance> in2 = input.filter((k, v)->v.getParentId() == null);
//        KStream<String,NodeInstance> in3 = in2.merge(in2.flatMapValues(v->v.getNodes()));
//
//        KGroupedStream<String,NodeInstance> in4 =  in3.selectKey((k,v)->v.getName()).groupByKey(Grouped.with(Serdes.String() ,JsonSerdes.NodeInstance()));
//
//        KTable<String,Stats> output = in4.aggregate(
//                ()-> new Stats(0L,"temp",0L,0L,"","","",""),
//                (k,v,agg)-> {
//                    if(v.getStatus().get().equals("Success")) agg.setSuccess(agg.getSuccess()+1);
//                    else agg.setFailure(agg.getFailure()+1);
//                    agg.setCount(agg.getCount()+1);
//                    agg.setName(v.getName());
//                    agg.setDescription(v.getDescription());
//                    agg.setId(v.getId());
//                    agg.setParentId(v.getParentId());
//                    agg.setRootId(v.getRootId());
//                    return agg;
//                },
//
//                Materialized.with(Serdes.String(),JsonSerdes.Stats())
//        );
//        output.toStream().to("helidon-output01",Produced.with(Serdes.String(),JsonSerdes.Stats()));
//
//
//        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(),props);
//
//        //start stream application
//        final CountDownLatch latch = new CountDownLatch(1);
//        try{
//            kafkaStreams.start();
//            latch.await();
//        }catch (final Throwable e){
//            System.exit(1);
//        }
//
//        Runtime.getRuntime().addShutdownHook(new Thread("input-stream01"){
//            @Override
//            public void run(){
//                kafkaStreams.close();
//                latch.countDown();
//            }
//        });
//
//        System.exit(0);
//    }
//}


