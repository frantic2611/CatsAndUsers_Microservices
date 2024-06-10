package org.example.kafka;

import org.example.Span.UserSpan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducer {

    private KafkaTemplate<String, UserSpan> kafkaTemplate;

    private KafkaTemplate<String, Integer> kafkaTemplate1;

    @Autowired
    public UserProducer(KafkaTemplate<String, UserSpan> kafkaTemplate,KafkaTemplate<String, Integer> kafkaTemplate1 ) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate1 = kafkaTemplate1;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProducer.class);

    public void push(UserSpan userSpan){

        LOGGER.info(String.format("Message sent %s", userSpan.getUsername()));
        kafkaTemplate.send("saveUserTopic", userSpan);
    }

    public void delete(Integer id){
        kafkaTemplate1.send("deleteUserTopic1", id);
    }

    public void update(UserSpan userSpan){
        kafkaTemplate.send("updateUserTopic", userSpan);
    }
}
