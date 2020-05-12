package com.greenapp.authservice.kafka;

public interface KafkaConfigConstants {
    String BOOTSTRAP="moped-01.srvs.cloudkafka.com:9094,moped-02.srvs.cloudkafka.com:9094,moped-03.srvs.cloudkafka.com:9094";
    String USERNAME="uryu7l0c";
    String PASSWORD="rh2dpUrTHe7tGIuXZbmX2D4vkyjZZgUN";
    String TOPIC_PREFIX = USERNAME + "-";
}
