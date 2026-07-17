package com.alphaeci.notification.domain.ports.out;

/** Push a un topic FCM (broadcast a todos los dispositivos suscritos). */
public interface FcmPushPort {
    void sendToTopic(String topic, String title, String body, java.util.Map<String, String> data);
}
