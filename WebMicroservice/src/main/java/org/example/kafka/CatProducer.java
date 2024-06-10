package org.example.kafka;

import org.example.Span.CatSpan;
import org.example.Span.UserSpan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CatProducer {

    private KafkaTemplate<String, CatSpan> kafkaTemplate;

    private KafkaTemplate<String, Integer> kafkaTemplate1;

    @Autowired
    public CatProducer(KafkaTemplate<String, CatSpan> kafkaTemplate, KafkaTemplate<String, Integer> kafkaTemplate1 ) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate1 = kafkaTemplate1;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProducer.class);

    public void push(CatSpan catSpan){
        LOGGER.info(String.format("Message sent %s", catSpan.getName()));
        kafkaTemplate.send("saveCatTopic", catSpan);
    }

    public void delete(Integer id){
        kafkaTemplate1.send("deleteCatTopic", id);
    }

    public void update(CatSpan catSpan){
        kafkaTemplate.send("updateCatTopic", catSpan);
    }
}
