<div align="center">

# 🔔 PATRICI.A — Microservicio de Notificaciones

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-CloudAMQP-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Container-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Azure](https://img.shields.io/badge/Azure-Container_Apps-0078D4?style=for-the-badge&logo=microsoftazure&logoColor=white)
![Hexagonal](https://img.shields.io/badge/Architecture-Hexagonal-blueviolet?style=for-the-badge)

> 💡 **PATRICI.A** es un proyecto académico de la Escuela Colombiana de Ingeniería Julio Garavito, construido con arquitectura de microservicios orientada a producción.

</div>

---

## 📑 Tabla de Contenidos

1. [👤 Integrantes](#1--integrantes)
2. [⚙️ Tecnologías Utilizadas](#2-️-tecnologías-utilizadas)
3. [🎯 Descripción del Módulo](#3--descripción-del-módulo)
4. [🏗️ Cómo Funciona el Módulo](#4-️-cómo-funciona-el-módulo)
5. [📊 Diagramas](#5--diagramas)
6. [🧩 Funcionalidades](#6--funcionalidades)
7. [🧪 Evidencia de Pruebas Unitarias](#7--evidencia-de-pruebas-unitarias)
8. [📈 Evidencia de Cobertura](#8--evidencia-del-análisis-de-cobertura)
9. [🚀 Cómo Ejecutar el Proyecto](#9--cómo-ejecutar-el-proyecto)
10. [🔄 Evidencia CI/CD](#10--evidencia-del-despliegue-cicd)
11. [🌐 Link Expuesto en Azure con Swagger](#11--link-expuesto-en-azure-con-swagger)
12. [🗂️ Organización del Código](#12-️-organización-del-código)
13. [📝 Código Documentado](#13--código-documentado)
14. [🔗 Conexiones con Servicios Externos](#14--conexiones-con-servicios-externos)
15. [⚙️ Pipeline de Desarrollo](#15-️-pipeline-de-desarrollo)
16. [🚢 Pipeline de PROD](#16--pipeline-de-prod)

---

## 1. 👤 Integrantes

| Nombre | Correo |
|---|---|
| _(pendiente)_ | _(pendiente)_ |

El equipo aplicó la metodología **Scrum** con sprints semanales, usando **Jira** para seguimiento de tareas y **GitHub Projects** como tablero de trabajo.

---

## 2. ⚙️ Tecnologías Utilizadas

| Tecnología | Versión | Justificación |
|---|---|---|
| **Java** | 21 | Soporte para Virtual Threads: alto número de listeners y conexiones concurrentes (RabbitMQ, WebSocket) con bajo consumo de recursos. Tipado estático y ecosistema maduro con Spring Boot 3. |
| **Spring Boot** | 3.3.0 | Autoconfiguración e integración nativa de todos los componentes requeridos (Web, WebSocket, AMQP, MongoDB, Mail, Security). |
| **Spring Security** | Incluido en Boot 3.3 | Esquema stateless: la autenticación se valida en el API Gateway y se propaga con el header `X-User-Id`, evitando duplicar validación de tokens en cada servicio. |
| **Spring WebSocket + STOMP** | Incluido en Boot 3.3 | Comunicación bidireccional persistente para entrega en tiempo real, con SockJS como respaldo. |
| **Spring AMQP** | Incluido en Boot 3.3 | Abstracción sobre RabbitMQ: listeners declarativos, DLQ y serialización de mensajes. |
| **Spring Data MongoDB** | Incluido en Boot 3.3 | Repositorios simplificados para el carácter semi-estructurado de las notificaciones, sin migraciones rígidas. |
| **Spring Mail (JavaMailSender)** | Incluido en Boot 3.3 | Correos transaccionales (OTP, recuperación de contraseña) con control total sobre contenido y formato. |
| **Jakarta Bean Validation** | Incluido en Boot 3.3 | Validación declarativa sobre DTOs de solicitudes REST y eventos consumidos de RabbitMQ. |
| **Lombok** | Última estable | Reducción de boilerplate con `@Builder`, `@Getter`, `@RequiredArgsConstructor`. |
| **MapStruct** | 1.5.5.Final | Mapeo entre dominio, DTOs y documentos de persistencia generado en tiempo de compilación. |
| **SpringDoc OpenAPI** | 2.5.0 | Documentación Swagger UI automática de los 8 endpoints REST. |
| **JaCoCo** | 0.8.10 | Reportes de cobertura, con verificación mínima integrada al pipeline CI. |
| **JUnit 5 + Mockito** | Incluido en Boot 3.3 | Pruebas unitarias mediante mocking de puertos, sin dependencias externas reales. |
| **Apache Maven** | 3.9 | Gestión de dependencias y construcción del proyecto. |
| **MongoDB Atlas** | Cloud | Base de datos NoSQL administrada: colecciones `notifications`, `notificationPreferences`, `eventReminders`. |
| **RabbitMQ (CloudAMQP)** | Cloud | Broker de mensajería asíncrona gestionado, con conexión cifrada SSL (puerto 5671). |
| **Docker** | Última estable | Build multi-stage e imagen consistente entre desarrollo, pruebas y producción. |
| **GitHub Actions** | — | Pipelines de CI, CD QA y CD Prod. |
| **GHCR** | — | Registro de imágenes de contenedor (`ghcr.io/cybersapienseci/notification-service`). |
| **Azure Container Apps** | — | Plataforma de despliegue en la nube del microservicio. |
| **IntelliJ IDEA** | — | IDE principal con soporte avanzado para Spring Boot. |
| **Draw.io** | — | Elaboración de los diagramas de arquitectura. |
| **Git / GitHub** | — | Control de versiones bajo estrategia Git Flow. |

---

## 3. 🎯 Descripción del Módulo

El microservicio de Notificaciones gestiona el **ciclo de vida completo de alertas y recordatorios** dentro de PATRICI.A, operando bajo un modelo orientado a eventos. A través de **RabbitMQ (CloudAMQP)** con topología de **Topic Exchange**, consume las señales publicadas por los demás módulos del sistema manteniendo desacoplamiento total respecto a los productores. En total consume **9 colas** a través de **9 consumers** (uno por servicio productor).

Cada cola cuenta con su **Dead Letter Queue (DLQ)**, evitando ciclos de reencolamiento ante mensajes malformados. La entrega se realiza principalmente por canal **IN_APP**: si el usuario destinatario está conectado recibe la notificación en tiempo real vía **WebSocket (STOMP sobre SockJS)**; si no, queda persistida en **MongoDB** para consulta posterior. Como excepción, las notificaciones de tipo **OTP_VERIFICATION** y **PASSWORD_RESET** se envían adicionalmente por **correo electrónico** mediante JavaMailSender.

El módulo expone **8 endpoints REST** para envío, consulta paginada, conteo de no leídas, marcado como leídas, gestión de preferencias y creación de recordatorios. Un **scheduler** evalúa cada **10 minutos** los recordatorios programados y genera alertas automáticas **24 horas** y **1 hora** antes del inicio de cada evento guardado.

### Funcionalidades Principales

| Funcionalidad | Descripción |
|---|---|
| **Entrega en Tiempo Real** | Notificaciones instantáneas al usuario conectado vía WebSocket STOMP sobre el destino de usuario `/user/{userId}/queue/notifications`. |
| **Persistencia In-App** | Toda notificación se persiste en MongoDB, esté o no conectado el usuario. |
| **Canal Email para tipos críticos** | `OTP_VERIFICATION` y `PASSWORD_RESET` se envían además por SMTP y **omiten** el filtro de preferencias. |
| **Gestión de Preferencias** | Cada usuario habilita o deshabilita tipos de notificación de forma individual. |
| **Recordatorios Automáticos** | Scheduler cada 10 minutos; avisos a 24h y 1h antes del evento, con banderas `reminded24h` / `reminded1h` contra duplicados. |
| **Consumo de Eventos Asíncrono** | 9 colas RabbitMQ enlazadas a un Topic Exchange, sin acoplamiento sincrónico con los productores. |
| **Dead Letter Queues** | Mensajes malformados se redirigen al exchange `notification.dlx` en vez de reencolarse indefinidamente. |

---

## 4. 🏗️ Cómo Funciona el Módulo

### Modelo Orientado a Eventos

El servicio actúa **exclusivamente como consumidor**. Los módulos productores publican sobre el Topic Exchange `notification.exchange`; cada consumer está enlazado por un patrón de routing key de dominio (`auth.#`, `chat.#`, …).

| Módulo Productor | Cola | Routing key (binding) | Resultado |
|---|---|---|---|
| **Auth** | `notification.auth.queue` | `auth.#` | `OTP_VERIFICATION` / `PASSWORD_RESET` |
| **Chat** | `notification.chat.queue` | `chat.#` | `PARCHE_MESSAGE` |
| **Event** | `notification.event.queue` | `event.#` | Crea un `EventReminder` |
| **Matching** | `notification.matching.queue` | `matching.#` | `CONNECTION_REQUEST` |
| **Hangout** | `notification.hangout.queue` | `hangout.#` | `PARCHE_INVITATION` |
| **Achievement** | `notification.achievement.queue` | `achievement.#` | `ACHIEVEMENT_UNLOCKED` |
| **Geolocation** | `notification.geolocation.queue` | `geolocation.#` | `NEARBY_PARCHE` |
| **Profile** | `notification.profile.queue` | `profile.#` | `REPORT_READY` |
| **Member** | `notification.member.queue` | `member.#` | `MEMBER_LEFT` |

### Patrones Utilizados

| Patrón | Descripción |
|---|---|
| **Ports & Adapters (Hexagonal)** | El dominio define interfaces (puertos); la infraestructura provee implementaciones (adaptadores). |
| **Event-Driven** | El servicio reacciona a eventos de RabbitMQ sin acoplamiento con los productores. |
| **Dead Letter Queue** | Cada cola declara `x-dead-letter-exchange: notification.dlx` y routing key `{cola}.dlq`. |
| **Repository Pattern** | Persistencia abstraída tras interfaces de repositorio del dominio. |
| **DTO Pattern** | DTOs de request/response desacoplan la API del modelo de dominio. |
| **Scheduler Pattern** | `@Scheduled(fixedDelay = 600000)` evalúa y despacha recordatorios de eventos. |

### Estilo de Arquitectura

Arquitectura Hexagonal (Ports & Adapters) según la propuesta de Alistair Cockburn, en cuatro capas:

```
┌──────────────────────────────────────────────────────────────┐
│  ENTRYPOINTS (Adaptadores de Entrada)                        │
│  REST Controllers · 9 RabbitMQ Consumers · Exception Handler │
├──────────────────────────────────────────────────────────────┤
│  APPLICATION (Casos de Uso)                                  │
│  SendNotification · GetNotifications · GetUnreadCount        │
│  MarkAsRead · Get/UpdatePreferences · CreateEventReminder    │
│  EventReminderService (scheduler cada 10 min)                │
├──────────────────────────────────────────────────────────────┤
│  DOMAIN (Núcleo — sin dependencias externas)                 │
│  Entities · Enums · Ports/In (7) · Ports/Out (4) · Exceptions│
├──────────────────────────────────────────────────────────────┤
│  INFRASTRUCTURE (Adaptadores de Salida)                      │
│  MongoDB Repos · NotificationDeliveryAdapter (WS + SMTP)     │
│  RabbitMQConfig · WebSocketConfig · SecurityConfig · ...     │
└──────────────────────────────────────────────────────────────┘
```

**Beneficios obtenidos:**

| Beneficio | Implementación |
|---|---|
| **Testabilidad** | Los casos de uso se prueban con mocks de los puertos, sin MongoDB, broker ni SMTP reales. |
| **Intercambiabilidad** | Cambiar canal de entrega o motor de persistencia solo toca el adaptador correspondiente. |
| **Claridad de responsabilidades** | Cada clase tiene una única razón de cambio (SRP). |
| **Independencia de frameworks** | El dominio no importa clases de Spring, RabbitMQ ni JavaMail. |
| **Desacoplamiento por eventos** | Los módulos productores no conocen al notification-service. |

---

## 5. 📊 Diagramas

### 5.1 Diagrama de Datos — Modelo MongoDB

Modelo desnormalizado en tres colecciones. `notifications` es el núcleo (historial por usuario, tipo, canal y estado de lectura). Antes de crear un registro se consulta `notificationPreferences` para omitir tipos deshabilitados. `eventReminders` es el registro de control del scheduler, con banderas contra duplicados.

> ⚠️ **Pendiente:** agregar `docs/imagenes/NotificationDB.drawio.png`.

#### Colección `notifications`

| Campo | Tipo | Descripción |
|---|---|---|
| _id | String (UUID) | Identificador de la notificación |
| userId | String (UUID) | Usuario destino — indexado |
| type | Enum | Tipo de notificación (`NotificationType`) |
| channel | Enum | `IN_APP` / `EMAIL` |
| title | String | Título visible (máx. 80 caracteres) |
| body | String | Cuerpo del mensaje (máx. 200 caracteres) |
| read | Boolean | Estado de lectura |
| referenceId | String (UUID) | Recurso relacionado (opcional) |
| createdAt | DateTime | Fecha de creación |

#### Colección `notificationPreferences`

| Campo | Tipo | Descripción |
|---|---|---|
| _id | String (UUID) | Identificador del registro |
| userId | String (UUID) | Usuario propietario — índice único |
| connectionRequest | Boolean | Solicitudes de conexión |
| parcheMessage | Boolean | Mensajes en parches |
| eventReminder | Boolean | Recordatorios de eventos |
| nearbyParche | Boolean | Parches cercanos |
| achievementUnlocked | Boolean | Logros desbloqueados |
| parcheInvitation | Boolean | Invitaciones a parches |
| updatedAt | DateTime | Última actualización |

#### Colección `eventReminders`

| Campo | Tipo | Descripción |
|---|---|---|
| _id | String (UUID) | Identificador del recordatorio |
| userId | String (UUID) | Usuario que guardó el evento — indexado |
| eventId | String (UUID) | Identificador del evento |
| eventDate | DateTime | Fecha del evento (debe ser futura) |
| reminded24h | Boolean | Si ya se envió el recordatorio de 24h |
| reminded1h | Boolean | Si ya se envió el recordatorio de 1h |

### 5.2 Diagrama de Clases

Tres entidades de dominio (`Notification`, `NotificationPreferences`, `EventReminder`) y dos enumeraciones de soporte (`NotificationType`, `NotificationChannel`).

> ⚠️ **Pendiente:** agregar `docs/imagenes/DiagramaClasesNotification.drawio.png`.

### 5.3 Diagrama de Componentes

- **Adaptadores de entrada:** `SendNotificationController`, `NotificationController`, `PreferencesController`, `EventReminderController` (REST); 9 consumers RabbitMQ; `EventReminderService` (scheduler).
- **Puertos de entrada (`ports/in`):** `SendNotificationUseCase`, `GetNotificationsUseCase`, `GetUnreadCountUseCase`, `MarkAsReadUseCase`, `GetPreferencesUseCase`, `UpdatePreferencesUseCase`, `CreateEventReminderUseCase`.
- **Puertos de salida (`ports/out`):** `NotificationRepository`, `PreferencesRepository`, `EventReminderRepository`, `NotificationDeliveryPort`.
- **Adaptadores de salida:** `NotificationRepositoryAdapter`, `PreferencesRepositoryAdapter`, `EventReminderRepositoryAdapter` (MongoDB) y `NotificationDeliveryAdapter` (WebSocket/STOMP + SMTP).

> ⚠️ **Pendiente:** agregar `docs/imagenes/PDCE.drawio.png`.

---

## 6. 🧩 Funcionalidades

### Endpoints REST

Base: `/api/notifications`

| # | Método | Ruta | Descripción |
|---|---|---|---|
| 1 | POST | `/api/notifications/send` | Enviar notificación |
| 2 | GET | `/api/notifications` | Listar notificaciones (paginado) |
| 3 | GET | `/api/notifications/unread/count` | Contar no leídas |
| 4 | PATCH | `/api/notifications/{id}/read` | Marcar una como leída |
| 5 | PATCH | `/api/notifications/read-all` | Marcar todas como leídas |
| 6 | GET | `/api/notifications/preferences` | Consultar preferencias |
| 7 | PUT | `/api/notifications/preferences` | Actualizar preferencias |
| 8 | POST | `/api/notifications/reminders` | Crear recordatorio de evento |

---

#### 1️⃣ Enviar Notificación

**Endpoint:** `POST /api/notifications/send`

**Request Body:**

| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| userId | UUID | Obligatorio | Usuario destino |
| type | Enum | Obligatorio | Tipo (`NotificationType`) |
| channel | Enum | Obligatorio | `IN_APP` / `EMAIL` |
| title | String | Obligatorio, máx. 80 | Título visible |
| body | String | Obligatorio, máx. 200 | Cuerpo del mensaje |
| referenceId | UUID | Opcional | Recurso que originó la notificación |

```json
// POST /api/notifications/send
{
  "userId": "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03",
  "type": "PARCHE_INVITATION",
  "channel": "IN_APP",
  "title": "Invitación a parche",
  "body": "Te han invitado al parche: Parche de estudio",
  "referenceId": "8c5b2d10-4e6f-41a2-9d7c-6b3f0a1e8d24"
}
// Response 201 CREATED (sin cuerpo)
```

**Flujo:**
1. Jakarta Bean Validation valida el payload.
2. Si el tipo **no** es crítico (`OTP_VERIFICATION`, `PASSWORD_RESET`), se consultan las preferencias del usuario; si el tipo está deshabilitado se lanza `NotificationTypeDisabledException`.
3. La notificación se persiste en MongoDB.
4. Se entrega en tiempo real por WebSocket al destino del usuario.
5. Si el tipo es crítico, se envía además por correo.

**Casos de error:**

| Código HTTP | Escenario |
|---|---|
| 400 | Campos obligatorios ausentes, `title` > 80 o `body` > 200 caracteres |
| 204 | Tipo deshabilitado en preferencias — la notificación se descarta silenciosamente |

---

#### 2️⃣ Listar Notificaciones del Usuario

**Endpoint:** `GET /api/notifications` · **Headers:** `X-User-Id: {uuid}`

**Query Params:** `page` (default `0`), `size` (default `20`), `sort` — paginación estándar de Spring `Pageable`.

**Response:** `200 OK` — `Page<NotificationResponse>`

---

#### 3️⃣ Contar Notificaciones No Leídas

**Endpoint:** `GET /api/notifications/unread/count` · **Headers:** `X-User-Id: {uuid}`

```json
{ "userId": "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03", "unreadCount": 5 }
```

---

#### 4️⃣ Marcar Notificación como Leída

**Endpoint:** `PATCH /api/notifications/{id}/read` · **Headers:** `X-User-Id: {uuid}`

**Response:** `204 No Content` · **Error:** `404` si la notificación no existe o no pertenece al usuario.

---

#### 5️⃣ Marcar Todas como Leídas

**Endpoint:** `PATCH /api/notifications/read-all` · **Headers:** `X-User-Id: {uuid}`

**Response:** `204 No Content`

---

#### 6️⃣ Consultar Preferencias

**Endpoint:** `GET /api/notifications/preferences` · **Headers:** `X-User-Id: {uuid}`

```json
{
  "userId": "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03",
  "connectionRequest": true,
  "parcheMessage": true,
  "eventReminder": true,
  "nearbyParche": false,
  "achievementUnlocked": true,
  "parcheInvitation": true,
  "updatedAt": "2026-04-15T10:30:00"
}
```

---

#### 7️⃣ Actualizar Preferencias

**Endpoint:** `PUT /api/notifications/preferences` · **Headers:** `X-User-Id: {uuid}`

El cuerpo envía el conjunto completo de banderas, no un solo tipo:

```json
{
  "connectionRequest": true,
  "parcheMessage": true,
  "eventReminder": true,
  "nearbyParche": true,
  "achievementUnlocked": false,
  "parcheInvitation": true
}
```

**Response:** `200 OK` con el `PreferencesResponse` actualizado.

---

#### 8️⃣ Crear Recordatorio de Evento

**Endpoint:** `POST /api/notifications/reminders` · **Headers:** `X-User-Id: {uuid}`

```json
// Request
{
  "eventId": "a1b2c3d4-e5f6-47a8-9b0c-1d2e3f4a5b6c",
  "eventDate": "2026-05-10T14:00:00"
}

// Response 201 CREATED
{
  "id": "9e8d7c6b-5a4f-43e2-b1c0-9d8e7f6a5b4c",
  "userId": "3f2a7c1e-9b4d-4c8a-b0f6-2d1e5a7c9b03",
  "eventId": "a1b2c3d4-e5f6-47a8-9b0c-1d2e3f4a5b6c",
  "eventDate": "2026-05-10T14:00:00",
  "reminded24h": false,
  "reminded1h": false
}
```

> ⚠️ `eventDate` debe ser futura (`@Future`). **Error 400** si está en el pasado o faltan campos.

---

### Colas de Mensajería (RabbitMQ)

Exchange principal: **`notification.exchange`** (Topic). Dead Letter Exchange: **`notification.dlx`** (Direct). Cada cola declara `x-dead-letter-exchange: notification.dlx` y `x-dead-letter-routing-key: {cola}.dlq`.

| Consumer | Cola | Binding | Payload (DTO) |
|---|---|---|---|
| `AuthNotificationConsumer` | `notification.auth.queue` | `auth.#` | `AuthNotificationEvent` — `targetUserId`, `referenceId`, `type`, `otp` |
| `ChatNotificationConsumer` | `notification.chat.queue` | `chat.#` | `ChatNotificationEvent` — `targetUserId`, `chatId`, `senderName` |
| `EventNotificationConsumer` | `notification.event.queue` | `event.#` | `EventNotificationEvent` — `targetUserId`, `eventId`, `eventName`, `eventDate` |
| `MatchingNotificationConsumer` | `notification.matching.queue` | `matching.#` | `MatchingNotificationEvent` — `targetUserId`, `requesterId`, `requesterName` |
| `HangoutNotificationConsumer` | `notification.hangout.queue` | `hangout.#` | `HangoutNotificationEvent` — `targetUserId`, `parcheId`, `parcheName` |
| `AchievementNotificationConsumer` | `notification.achievement.queue` | `achievement.#` | `AchievementNotificationEvent` — `targetUserId`, `achievementId`, `achievementName` |
| `GeolocationNotificationConsumer` | `notification.geolocation.queue` | `geolocation.#` | `GeolocationNotificationEvent` — `targetUserId`, `parcheId`, `parcheName`, `location` |
| `ProfileNotificationConsumer` | `notification.profile.queue` | `profile.#` | `ProfileNotificationEvent` — `targetUserId`, `reportId`, `reportName` |
| `MemberNotificationConsumer` | `notification.member.queue` | `member.#` | `MemberNotificationEvent` — `targetUserId`, `parcheId`, `memberName`, `parcheName` |

Los mensajes se serializan con `Jackson2JsonMessageConverter`. `EventNotificationConsumer` es el único que no genera una notificación directa: crea un `EventReminder` que el scheduler despacha después.

---

### 🔌 WebSocket — Notificaciones en Tiempo Real

STOMP sobre SockJS, con broker simple en memoria.

| Campo | Valor |
|---|---|
| **Endpoint de conexión** | `/ws-notifications` (SockJS) |
| **Destino de usuario** | `/user/{userId}/queue/notifications` |
| **Prefijos del broker** | `/topic`, `/user` |
| **Prefijo de aplicación** | `/app` |

```javascript
const socket = new SockJS('/ws-notifications');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
  stompClient.subscribe(`/user/${userId}/queue/notifications`, (message) => {
    const notification = JSON.parse(message.body);
    console.log('Nueva notificación:', notification);
  });
});
```

---

### ⚠️ Manejo de Errores

Handler centralizado con `@RestControllerAdvice` en `entrypoints/advice/NotificationExceptionHandler`.

| Código HTTP | Excepción | Escenario |
|---|---|---|
| 400 | `InvalidNotificationException` | Payload inválido |
| 400 | `MethodArgumentNotValidException` | Validaciones `@Valid` fallidas en `@RequestBody` |
| 404 | `NotificationNotFoundException` | Notificación no encontrada o ajena al usuario |
| 204 | `NotificationTypeDisabledException` | Tipo deshabilitado en preferencias: se descarta sin error |

```json
{
  "timestamp": "2026-04-15T10:30:00",
  "message": "Notification not found for id: 9e8d7c6b-5a4f-43e2-b1c0-9d8e7f6a5b4c"
}
```

### 📋 Tipos de Notificación (`NotificationType`)

| Valor | Origen | Descripción |
|---|---|---|
| `CONNECTION_REQUEST` | Matching | Solicitud de conexión recibida |
| `PARCHE_MESSAGE` | Chat | Nuevo mensaje en un parche |
| `EVENT_REMINDER` | Scheduler | Recordatorio 24h / 1h antes del evento |
| `NEARBY_PARCHE` | Geolocation | Parche cercano a la ubicación del usuario |
| `ACHIEVEMENT_UNLOCKED` | Achievement | Logro desbloqueado |
| `PARCHE_INVITATION` | Hangout | Invitación a unirse a un parche |
| `OTP_VERIFICATION` | Auth | Código OTP de verificación (canal EMAIL) |
| `PASSWORD_RESET` | Auth | Recuperación de contraseña (canal EMAIL) |
| `REPORT_READY` | Profile | Reporte del usuario disponible |
| `MEMBER_LEFT` | Member | Un miembro salió del parche |

---

## 7. 🧪 Evidencia de Pruebas Unitarias

| Tipo | Descripción | Herramientas |
|---|---|---|
| **Pruebas Unitarias** | Validan cada caso de uso de forma aislada, mockeando puertos y dependencias | JUnit 5 + Mockito |

### Clases de prueba

| Clase | Casos cubiertos |
|---|---|
| `SendNotificationUseCaseImplTest` | Envío exitoso, tipo deshabilitado, tipos críticos por email |
| `GetNotificationsUseCaseImplTest` | Paginación y consulta por usuario |
| `GetUnreadCountUseCaseImplTest` | Conteo de no leídas |
| `MarkAsReadUseCaseImplTest` | Marcar una y todas como leídas |
| `GetPreferencesUseCaseImplTest` | Preferencias existentes y valores por defecto |
| `UpdatePreferencesUseCaseImplTest` | Actualización de banderas de preferencias |
| `CreateEventReminderUseCaseImplTest` | Creación de recordatorio de evento |

### Cómo ejecutar las pruebas

```bash
# Todas las pruebas
mvn clean test

# Una prueba específica
mvn test -Dtest=SendNotificationUseCaseImplTest

# Pruebas + verificación de cobertura JaCoCo
mvn clean verify
```

### Criterios de aceptación

- ✅ Cobertura mínima verificada por JaCoCo sobre `application.usecase`
- ✅ Todas las pruebas en estado **PASSED**
- ✅ Casos felices **y** casos de error cubiertos

> ⚠️ **Pendiente:** agregar captura del reporte de pruebas en `docs/`.

---

## 8. 📈 Evidencia del Análisis de Cobertura

Cobertura generada con **JaCoCo 0.8.10** (`prepare-agent` en `test`, `report` en `test`, `check` en `verify`).

```bash
mvn clean verify
```

Reporte HTML: `target/site/jacoco/index.html`. El pipeline de CI lo publica como artefacto `jacoco-report-notification`.

> ⚠️ **Pendiente:** agregar captura del reporte en `docs/Jacoco.png`.

---

## 9. 🚀 Cómo Ejecutar el Proyecto

### Prerrequisitos

- **Java 21**
- **Maven 3.9+** (o el wrapper `./mvnw` incluido)
- **Docker** (opcional, para ejecutar la imagen del servicio)

### Opción 1: Ejecución Local (Maven)

```bash
# 1. Clonar repositorio
git clone https://github.com/CybersapiensECI/notification-service.git
cd notification-service

# 2. Ejecutar la aplicación
./mvnw spring-boot:run
```

📍 **URL Local:** `http://localhost:8080`
📚 **Swagger UI:** `http://localhost:8080/swagger-ui.html`
📄 **OpenAPI JSON:** `http://localhost:8080/api-docs`
📡 **WebSocket:** `ws://localhost:8080/ws-notifications`

### Opción 2: Docker

```bash
docker build -t notification-service .
docker run -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI="..." \
  -e SPRING_RABBITMQ_HOST="..." \
  -e SPRING_RABBITMQ_USERNAME="..." \
  -e SPRING_RABBITMQ_PASSWORD="..." \
  -e SPRING_RABBITMQ_VIRTUAL_HOST="..." \
  -e SPRING_MAIL_USERNAME="..." \
  -e SPRING_MAIL_PASSWORD="..." \
  notification-service
```

El `Dockerfile` usa build multi-stage (`maven:3.9.6-eclipse-temurin-21-alpine` → `eclipse-temurin:21-jre-alpine`) y expone el puerto **8080**.

### Variables de Entorno

Sobrescriben los valores de `application.properties` mediante el relaxed binding de Spring Boot:

| Variable | Descripción | Default |
|---|---|---|
| `SERVER_PORT` | Puerto del servicio | `8080` |
| `SPRING_DATA_MONGODB_URI` | URI de conexión a MongoDB Atlas | Requerido |
| `SPRING_RABBITMQ_HOST` | Host del broker CloudAMQP | Requerido |
| `SPRING_RABBITMQ_PORT` | Puerto RabbitMQ | `5671` |
| `SPRING_RABBITMQ_USERNAME` | Usuario RabbitMQ | Requerido |
| `SPRING_RABBITMQ_PASSWORD` | Contraseña RabbitMQ | Requerido |
| `SPRING_RABBITMQ_VIRTUAL_HOST` | Virtual host RabbitMQ | Requerido |
| `SPRING_RABBITMQ_SSL_ENABLED` | SSL en RabbitMQ | `true` |
| `SPRING_MAIL_USERNAME` | Usuario SMTP (Gmail) | Requerido |
| `SPRING_MAIL_PASSWORD` | App password SMTP | Requerido |

---

## 10. 🔄 Evidencia del Despliegue CI/CD

Tres workflows de **GitHub Actions**:

| Workflow | Archivo | Disparador |
|---|---|---|
| **CI** | `.github/workflows/ci.yml` | push a `main`, `develop`, `feature/**`; PR a `main` / `develop` |
| **CD QA** | `.github/workflows/cd-qa.yml` | push a `develop` |
| **CD Prod** | `.github/workflows/cd-prod.yml` | push a `main` |

> ⚠️ **Pendiente:** agregar capturas de las ejecuciones en `docs/CI.png` y `docs/CD.png`.

---

## 11. 🌐 Link Expuesto en Azure con Swagger

Despliegue en **Azure Container Apps**, resource group `gr-alphaeci-prod`:

| Entorno | Container App |
|---|---|
| **QA** | `notification-service-qa` |
| **PROD** | `notification-service-prod` |

Rutas expuestas sobre el FQDN asignado por Azure (ingress externo, target port 8080):

- **Swagger UI:** `https://{fqdn}/swagger-ui.html`
- **OpenAPI JSON:** `https://{fqdn}/api-docs`

> ⚠️ **Pendiente:** registrar aquí el FQDN definitivo de cada Container App.

---

## 12. 🗂️ Organización del Código

```
notification-service/
│
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/alphaeci/notification/
│   │   │   │
│   │   │   ├── 📁 domain/                          # 🟢 DOMINIO (sin dependencias externas)
│   │   │   │   ├── 📁 model/                       # Notification, NotificationPreferences, EventReminder
│   │   │   │   │   └── 📁 enums/                   # NotificationType, NotificationChannel
│   │   │   │   ├── 📁 ports/
│   │   │   │   │   ├── 📁 in/                      # 7 puertos de entrada (casos de uso)
│   │   │   │   │   └── 📁 out/                     # 4 puertos de salida (repos + entrega)
│   │   │   │   ├── 📁 exceptions/                  # InvalidNotification, NotFound, TypeDisabled
│   │   │   │   └── 📁 validation/
│   │   │   │
│   │   │   ├── 📁 application/                     # 🔵 APLICACIÓN
│   │   │   │   ├── 📁 usecase/                     # 7 implementaciones de casos de uso
│   │   │   │   ├── 📁 service/                     # EventReminderService (scheduler cada 10 min)
│   │   │   │   ├── 📁 dto/
│   │   │   │   │   ├── 📁 request/                 # SendNotification, CreateEventReminder, UpdatePreferences
│   │   │   │   │   └── 📁 response/                # Notification, Preferences, EventReminder, UnreadCount
│   │   │   │   └── 📁 mapper/                      # NotificationMapper
│   │   │   │
│   │   │   ├── 📁 entrypoints/                     # 🟠 ADAPTADORES DE ENTRADA
│   │   │   │   ├── 📁 rest/controller/             # SendNotification, Notification, Preferences, EventReminder
│   │   │   │   ├── 📁 messaging/
│   │   │   │   │   ├── 📁 consumer/                # 9 consumers RabbitMQ
│   │   │   │   │   └── 📁 dto/                     # 9 DTOs de eventos
│   │   │   │   └── 📁 advice/                      # NotificationExceptionHandler
│   │   │   │
│   │   │   └── 📁 infrastructure/                  # 🔴 INFRAESTRUCTURA
│   │   │       ├── 📁 adapters/
│   │   │       │   ├── 📁 adapter/                 # NotificationDeliveryAdapter (WS + SMTP),
│   │   │       │   │                               # Notification/Preferences/EventReminder RepositoryAdapter
│   │   │       │   └── 📁 persistence/             # Documentos, mappers y repositorios Spring Data
│   │   │       └── 📁 config/                      # RabbitMQConfig, RabbitMQListenerConfig, WebSocketConfig,
│   │   │                                           # SecurityConfig, OpenApiConfig, SchedulerConfig
│   │   │
│   │   └── 📁 resources/
│   │       └── application.properties
│   │
│   └── 📁 test/                                    # 🧪 PRUEBAS UNITARIAS
│
├── 📁 .github/workflows/                           # ci.yml, cd-qa.yml, cd-prod.yml
├── 📄 Dockerfile
├── 📄 pom.xml
└── 📄 README.md
```

---

## 13. 📝 Código Documentado

La documentación viva del API se genera con **SpringDoc OpenAPI 2.5.0** y se publica en Swagger UI (`/swagger-ui.html`), configurada en `infrastructure/config/OpenApiConfig` bajo el título *Notification Service API*.

> ⚠️ **Pendiente:** completar JavaDoc en dominio, casos de uso, consumers y adaptadores, y enriquecer los controladores con `@Tag`, `@Operation`, `@ApiResponse` y los DTOs con `@Schema`.

---

## 14. 🔗 Conexiones con Servicios Externos

| Servicio Externo | Tipo de Conexión | Propósito |
|---|---|---|
| **MongoDB Atlas** | Spring Data MongoDB | Persistencia de notificaciones, preferencias y recordatorios |
| **RabbitMQ (CloudAMQP)** | Spring AMQP — SSL, puerto 5671 | Consumo de eventos de los 9 módulos productores |
| **SMTP (Gmail)** | JavaMailSender — puerto 587, STARTTLS | Envío de OTP y recuperación de contraseña |
| **GHCR** | Docker registry | Publicación de la imagen del servicio |
| **Azure Container Apps** | Despliegue | Ejecución del microservicio en QA y PROD |

### Configuración requerida

En los despliegues, todas las credenciales se inyectan como variables de entorno desde **GitHub Secrets** (`MONGODB_URI_NOTIFICATION`, `RABBITMQ_*`, `MAIL_*`, `AZURE_CREDENTIALS_Noti`).

> 🔐 **Pendiente de seguridad:** `src/main/resources/application.properties` aún contiene credenciales reales de MongoDB, CloudAMQP y Gmail en texto plano y versionadas en Git. Deben reemplazarse por placeholders (`${SPRING_DATA_MONGODB_URI:}`, etc.) y **rotarse**, ya que quedaron expuestas en el historial del repositorio.

---

## 15. ⚙️ Pipeline de Desarrollo

### Estrategia de Ramas (Git Flow)

| Rama | Propósito | Reglas |
|---|---|---|
| `main` | Versión estable en producción | El merge dispara **CD Prod**. PR obligatorio con CI en verde. |
| `develop` | Integración continua | El merge dispara **CD QA**. Rama protegida. |
| `feature/*` | Desarrollo de una funcionalidad | Base: `develop`. Cierre: PR hacia `develop`. |

### Convenciones

```
Ramas:    feature/[NombreFuncionalidad]     → feature/NotificationService
Commits:  Feat: [Descripción]               → Feat: Configuracion Rabbit
          Fix: [Descripción]                → Fix: Correccion Replicas Azure
```

### Etapas del Pipeline de CI (`.github/workflows/ci.yml`)

```yaml
on:
  push:
    branches: [ main, develop, feature/** ]
  pull_request:
    branches: [ main, develop ]

jobs:
  build-and-test:
    - Checkout del código
    - Setup JDK 21 (temurin, cache maven)
    - mvn verify          # compila, prueba y valida cobertura JaCoCo
    - Upload artefacto jacoco-report-notification
```

Las credenciales se inyectan como variables de entorno desde GitHub Secrets.

---

## 16. 🚢 Pipeline de PROD

### CD QA (`.github/workflows/cd-qa.yml`) — push a `develop`

```yaml
jobs:
  deploy-qa:
    - Checkout del código
    - Login en ghcr.io
    - docker build & push  → ghcr.io/cybersapienseci/notification-service:qa-{sha}, :qa-latest
    - Login en Azure (AZURE_CREDENTIALS_Noti)
    - az containerapp up      # notification-service-qa, ingress external, target-port 8080
    - az containerapp update  # min-replicas 0, max-replicas 1, env vars desde secrets
```

### CD Prod (`.github/workflows/cd-prod.yml`) — push a `main`

```yaml
jobs:
  deploy-prod:
    - Checkout del código
    - Login en ghcr.io
    - docker build & push  → ghcr.io/cybersapienseci/notification-service:{ref_name}, :latest
    - Login en Azure (AZURE_CREDENTIALS_Noti)
    - az containerapp up      # notification-service-prod, ingress external, target-port 8080
    - az containerapp update  # min-replicas 0, max-replicas 1, env vars desde secrets
```

Ambos despliegan sobre el resource group y environment `gr-alphaeci-prod` con escalado a cero (`min-replicas 0`) para optimizar costo.

---

<div align="center">

### 🐢 Equipo **AlphaECI**

![Course](https://img.shields.io/badge/Course-DOSW-orange?style=for-the-badge)
![Year](https://img.shields.io/badge/Year-2026--1-blue?style=for-the-badge)

**🎓 Escuela Colombiana de Ingeniería Julio Garavito**

</div>
