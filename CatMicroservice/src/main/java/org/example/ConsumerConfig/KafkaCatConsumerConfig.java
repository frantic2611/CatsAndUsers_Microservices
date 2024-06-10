package org.example.ConsumerConfig;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.Span.CatSpan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaCatConsumerConfig {


    public ConsumerFactory<String, CatSpan> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(CatSpan.class));
    }

    public ConsumerFactory<String, Integer> consumerFactory1(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new IntegerDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CatSpan> catKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CatSpan> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Integer> cat1KafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Integer> factory1 = new ConcurrentKafkaListenerContainerFactory<>();
        factory1.setConsumerFactory(consumerFactory1());
        return factory1;
    }
}