package com.github.arpitkb.service.kafka;

import com.github.arpitkb.service.model.NodeInstance;
import com.github.arpitkb.service.model.NodeStatus;
import com.github.arpitkb.service.serdes.JsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {

    public static void main(String[] args) {
        String topic = "helidon-input01";
        String broker = "localhost:9092";
        Logger logger = LoggerFactory.getLogger(Producer.class);


        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG , broker);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        KafkaProducer<String, NodeInstance> producer=new KafkaProducer<>(properties);


        for(int i=0;i<100000;i++){
            int rand = 1+(int)Math.floor(Math.random()*10);
            int rand2 = (int)Math.floor(Math.random()*2);

            String name = "workflow "+rand;
            String status = rand2 == 0 ? "Success":"Failure";
            String id = "w"+rand+"-"+rand+".0";
            String description = name;

            NodeInstance nodeInstance = new NodeInstance();
            nodeInstance.setName(name);
            nodeInstance.setId(id);
            nodeInstance.setRootId(id);
            nodeInstance.setDescription(description);
            nodeInstance.setStatus(new NodeStatus(status));

            int rand3 = (int)Math.floor(Math.random()*2);

            for(int j=0;j<rand3;j++){
                NodeInstance nodeInstance2 = new NodeInstance();
                nodeInstance2.setName("workflow "+rand+"_"+j);
                nodeInstance2.setId("child_"+rand+"_"+j);
                nodeInstance2.setStatus(new NodeStatus("Failure"));
                nodeInstance2.setDescription("I am a child");
                nodeInstance.addNode(nodeInstance2);
            }


            ProducerRecord<String,NodeInstance> record = new ProducerRecord<>(topic,nodeInstance.getName(),nodeInstance);

            producer.send(record,(recordMetadata, e) -> {
                if(e==null){
                    logger.info("topic : "+recordMetadata.topic()+" | Partition : "+recordMetadata.partition()+" | Offset : "+recordMetadata.offset());
                }else{
                    logger.error("Error while producing",e);
                }
            });
        }

        producer.flush();
        producer.close();
    }


}



