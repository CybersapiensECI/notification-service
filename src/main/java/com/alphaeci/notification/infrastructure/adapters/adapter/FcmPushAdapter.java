package com.alphaeci.notification.infrastructure.adapters.adapter;

import com.alphaeci.notification.domain.ports.out.FcmPushPort;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class FcmPushAdapter implements FcmPushPort {

    @Override
    public void sendToTopic(String topic, String title, String body, Map<String, String> data) {
        if (FirebaseApp.getApps().isEmpty()) {
            log.warn("Firebase no inicializado: se omite el push a topic '{}' ({}).", topic, title);
            return;
        }
        try {
            Message.Builder builder = Message.builder()
                    .setTopic(topic)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build());
            if (data != null) {
                builder.putAllData(data);
            }
            String messageId = FirebaseMessaging.getInstance().send(builder.build());
            log.info("Push enviado a topic '{}': {}", topic, messageId);
        } catch (Exception e) {
            log.error("Fallo enviando push a topic '{}': {}", topic, e.getMessage());
        }
    }
}
