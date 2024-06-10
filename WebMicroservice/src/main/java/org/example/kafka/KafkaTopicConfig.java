package org.example.kafka;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic saveUserTopic(){
        return TopicBuilder.name("saveUserTopic")
                .build();
    }

    @Bean
    public NewTopic deleteUserTopic(){
        return TopicBuilder.name("deleteUserTopic1")
                .build();
    }

    @Bean
    public NewTopic updateUserTopic(){
        return TopicBuilder.name("updateUserTopic")
                .build();
    }


    @Bean
    public NewTopic saveCatTopic(){
        return TopicBuilder.name("saveCatTopic")
                .build();
    }

    @Bean
    public NewTopic deleteCatTopic(){
        return TopicBuilder.name("deleteCatTopic")
                .build();
    }

    @Bean
    public NewTopic updateCatTopic(){
        return TopicBuilder.name("updateCatTopic")
                .build();
    }
}
