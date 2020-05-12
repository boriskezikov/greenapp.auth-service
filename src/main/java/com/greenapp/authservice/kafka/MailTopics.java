package com.greenapp.authservice.kafka;


import static com.greenapp.authservice.kafka.KafkaConfigConstants.TOPIC_PREFIX;

public interface MailTopics {
    String MAIL_2FA_TOPIC = TOPIC_PREFIX + "mail-auth2fa-account-created";
}
