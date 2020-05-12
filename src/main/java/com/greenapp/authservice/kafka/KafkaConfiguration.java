package com.greenapp.authservice.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.greenapp.authservice.dto.TwoFaDTO;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static com.greenapp.authservice.kafka.KafkaConfigConstants.*;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    private final DynamicPropertyFactory propertyFactory;

    public KafkaConfiguration() {
        this.propertyFactory = DynamicPropertyFactory.getInstance();
    }

    public Properties getProps() {
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, USERNAME, PASSWORD);
        var props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP);
        props.put("group.id", USERNAME + "-consumer");
        props.put("enable.auto.commit", propertyFactory
                .getStringProperty("enable.auto.commit", "").get());
        props.put("auto.commit.interval.ms", propertyFactory
                .getStringProperty("auto.commit.interval.ms", "").get());
        props.put("auto.offset.reset", propertyFactory
                .getStringProperty("auto.offset.reset", "").get());
        props.put("session.timeout.ms", propertyFactory
                .getStringProperty("session.timeout.ms", "").get());
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", JsonDeserializer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonObjectSerializer.class.getName());
        props.put("security.protocol", propertyFactory
                .getStringProperty("security.protocol", "").get());
        props.put("sasl.mechanism", propertyFactory
                .getStringProperty("sasl.mechanism", "").get());
        props.put("sasl.jaas.config", jaasCfg);

        return props;
    }

    @Bean
    public KafkaTemplate<String, TwoFaDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule()
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public DefaultKafkaProducerFactory producerFactory() {
        ObjectMapper mapper = objectMapper();
        Serializer<TwoFaDTO> serializer = new KafkaJsonSerializer<>(new TypeReference<TwoFaDTO>() {
        }, mapper);
        return new DefaultKafkaProducerFactory(getProps(), Serdes.String().serializer(), serializer);

    }
}
