package com.alphaeci.notification.infrastructure.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Base64;

/**
 * Inicializa el SDK de Firebase Admin con la service account (base64 en
 * FIREBASE_SERVICE_ACCOUNT_JSON) para poder mandar push por FCM. Si la
 * variable no está configurada, el servicio sigue arrancando normal —
 * solo el push de eventos queda inactivo (ver FcmPushAdapter) en vez de
 * tumbar todo notification-service por una credencial opcional.
 */
@Slf4j
@Component
public class FirebaseConfig {

    @Value("${firebase.service-account-json-base64:}")
    private String serviceAccountJsonBase64;

    @PostConstruct
    public void init() {
        if (serviceAccountJsonBase64 == null || serviceAccountJsonBase64.isBlank()) {
            log.warn("FIREBASE_SERVICE_ACCOUNT_JSON no configurada: el push de eventos nuevos "
                    + "queda desactivado (el resto de notification-service funciona normal).");
            return;
        }
        try {
            if (!FirebaseApp.getApps().isEmpty()) {
                return;
            }
            byte[] decoded = Base64.getDecoder().decode(serviceAccountJsonBase64);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(decoded)))
                    .build();
            FirebaseApp.initializeApp(options);
            log.info("Firebase Admin SDK inicializado — push de eventos activo.");
        } catch (Exception e) {
            log.error("No se pudo inicializar Firebase Admin SDK, push de eventos desactivado: {}",
                    e.getMessage());
        }
    }
}
