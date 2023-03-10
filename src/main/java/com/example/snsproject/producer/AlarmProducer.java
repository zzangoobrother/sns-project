package com.example.snsproject.producer;

import com.example.snsproject.model.event.AlarmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmProducer {

    @Value("${spring.kafka.topic.alarm}")
    private String topic;

    private final KafkaTemplate<Long, AlarmEvent> kafkaTemplate;

    public void send(AlarmEvent event) {
        kafkaTemplate.send(topic, event.getReceiveUserId(), event);
        log.info("Send to Kafka finished");
    }
}
