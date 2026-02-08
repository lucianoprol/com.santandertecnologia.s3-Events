# S3 Events Reactive Service

Microservicio reactivo desarrollado con **Spring Boot + WebFlux**, persistencia en **MongoDB**, mensajería con **AWS SQS** y arquitectura **hexagonal**.

---

## 1. Ejecución y configuración del proyecto

### Requisitos

* Java 17+
* Maven 3.9+
* Docker y Docker Compose
* Cuenta de AWS (o LocalStack para desarrollo local)

---

### Variables de entorno

El proyecto utiliza configuración por variables de entorno (o `application.yml`).

```bash
AWS_REGION=us-east-1
AWS_SQS_QUEUE_URL=https://sqs.us-east-1.amazonaws.com/123456789012/s3-events-queue
MONGODB_URI=mongodb://localhost:27017/s3events
SPRING_PROFILES_ACTIVE=local
```

Las credenciales de AWS **no se definen explícitamente en el código**. Se resuelven mediante la **Default Credentials Provider Chain**:

* Variables de entorno (`AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`)
* Credenciales de perfil (`~/.aws/credentials`)
* IAM Role (EKS / ECS / EC2)

---

### Ejecución local con Docker

```bash
docker-compose up -d mongo
mvn spring-boot:run
```

---

### Endpoints

#### Crear evento S3

```http
POST /api/v1/s3-events
```

#### Obtener eventos por bucket (paginado)

```http
GET /api/v1/s3-events/{bucketName}?page=0&size=20
```

---

## 2. Decisiones técnicas

### Arquitectura

Se adoptó una **arquitectura hexagonal (Ports & Adapters)** para:

* Desacoplar dominio de frameworks
* Facilitar testing unitario
* Permitir cambios de infraestructura sin afectar la lógica de negocio


---

### Programación reactiva

* **Spring WebFlux** + **Project Reactor** (`Mono` / `Flux`)

Beneficios:

* Uso eficiente de recursos
* Mejor escalabilidad bajo alta concurrencia
* Integración natural con I/O no bloqueante (MongoDB, SQS)

---

### Persistencia

La capa de persistencia sigue un enfoque de **separación de responsabilidades
inspirado en CQRS** (Command Query Responsibility Segregation), donde:

- Las operaciones de escritura (commands) se manejan mediante puertos y
  adaptadores específicos de persistencia.
- Las operaciones de lectura (queries) utilizan adaptadores optimizados
  para consultas, basados en `ReactiveMongoTemplate`.

Este enfoque no implementa CQRS completo (con modelos o bases de datos
separadas), sino una variante pragmática que mejora la claridad del diseño,
facilita la evolución del sistema y permite optimizar lecturas y escrituras
de forma independiente.

---

### Logging y observabilidad

* `traceId` propagado vía **Reactor Context**
* Logs por capa (API / Application / Infrastructure)
* Medición de tiempo de ejecución por request
* Body logging seguro mediante el uso de decorator (no implementado en prototipo)

---

## 3. Procesamiento idempotente de mensajes SQS

Cuando la aplicación se ejecuta en **Kubernetes con múltiples pods**, AWS SQS puede entregar mensajes **más de una vez**. Para evitar procesamiento duplicado se puede aplicar la siguiente estrategia:

---

### 3.1 Identificación de mensajes duplicados

Cada evento posee un **identificador único** (`eventId`), generado al crear el evento S3.

Este `eventId` se incluye en:

* Base de datos
* Mensaje enviado a SQS

Ejemplo:

```json
{
  "eventId": "65b33d9d2e8f4f1e1a8b4567",
  "bucketName": "zonda-data-bucket",
  "objectKey": "reports/daily/report_2024-01-25.csv",
  "eventTime": "2024-01-25T14:00:00Z"
}
```

---

### 3.2 Restricciones de base de datos

Se define un **índice único** sobre `eventId`:

```java
@Indexed(unique = true)
private String eventId;
```

Esto garantiza que:

* Un mismo evento no pueda persistirse dos veces
* El segundo intento falle de manera controlada

---

### 3.3 Patrón "At-Least-Once + Idempotency"

AWS SQS garantiza entrega **al menos una vez**.

El sistema asegura idempotencia de la siguiente forma:

1. El consumidor recibe el mensaje
2. Verifica si el `eventId` ya fue procesado
3. Si existe → se descarta el procesamiento
4. Si no existe → se procesa y persiste

Este patrón es simple, confiable y ampliamente utilizado.

---

### 3.4 Manejo de concurrencia entre pods

Cuando varios pods intentan procesar el mismo mensaje simultáneamente:

* La base de datos actúa como **punto de sincronización**
* La restricción única evita condiciones de carrera
* El error por duplicado se maneja como caso esperado

No se requieren locks distribuidos adicionales.

---

### 3.5 Consideraciones adicionales

* **SQS FIFO** puede utilizarse si se requiere orden estricto
* `MessageDeduplicationId` puede reforzar la idempotencia
* La eliminación del mensaje de la cola ocurre solo tras éxito

---

## Conclusión

La combinación de:

* Identificadores únicos
* Restricciones de base de datos
* Manejo reactivo de errores
* Arquitectura desacoplada

permite procesar eventos S3 de forma **segura, escalable e idempotente**, incluso en entornos distribuidos con múltiples instancias.
