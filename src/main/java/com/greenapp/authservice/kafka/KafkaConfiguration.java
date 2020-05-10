package com.greenapp.authservice.kafka;

import com.greenapp.authservice.dto.AuthAccessToken;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static com.greenapp.authservice.KafkaConfigConstants.*;
import static com.greenapp.authservice.kafka.MailTopics.MAIL_2FA_TOPIC;

//@Configuration
//@EnableKafka
//public class KafkaConfiguration {
//
//    private final DynamicPropertyFactory propertyFactory;
//
//    public KafkaConfiguration() {
//        this.propertyFactory = DynamicPropertyFactory.getInstance();
//    }
////    public void consume() {
////        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getProps());
////        consumer.subscribe(Collections.singletonList(MAIL_2FA_TOPIC));
////        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1000));
////    }
//
//    public Properties getProps() {
//        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
//        String jaasCfg = String.format(jaasTemplate, USERNAME, PASSWORD);
//        String serializer = StringSerializer.class.getName();
//        String deserializer = StringDeserializer.class.getName();
//
//        var props = new Properties();
//        props.put("bootstrap.servers", BOOTSTRAP);
//        props.put("group.id", USERNAME + "-consumer");
//        props.put("enable.auto.commit", propertyFactory
//                .getStringProperty("enable.auto.commit", "").get());
//        props.put("auto.commit.interval.ms", propertyFactory
//                .getStringProperty("auto.commit.interval.ms", "").get());
//        props.put("auto.offset.reset", propertyFactory
//                .getStringProperty("auto.offset.reset", "").get());
//        props.put("session.timeout.ms", propertyFactory
//                .getStringProperty("session.timeout.ms", "").get());
//        props.put("key.deserializer", deserializer);
//        props.put("value.deserializer", deserializer);
//        props.put("key.serializer", serializer);
//        props.put("value.serializer", serializer);
//        props.put("security.protocol", propertyFactory
//                .getStringProperty("security.protocol", "").get());
//        props.put("sasl.mechanism", propertyFactory
//                .getStringProperty("sasl.mechanism", "").get());
//        props.put("sasl.jaas.config", jaasCfg);
//        return props;
//    }
//
//    @Bean
//    public KafkaProducer<String, AuthAccessToken> kafkaProducer() {
//        return new KafkaProducer<>(getProps());
//    }
//
////    @Bean
////    public NewTopic topic1() {
////        return new NewTopic(MAIL_2FA_TOPIC, 1, (short) 1);
////    }
//
////    public void produce() {
////        Producer<String, String> producer = new KafkaProducer<>(getProps());
////        producer.send(new ProducerRecord<>(topic, "Hello world"));
////    }
//
//}
