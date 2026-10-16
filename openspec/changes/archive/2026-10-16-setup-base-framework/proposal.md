# Proposal: Setup Base Framework

## Why

El repositorio actual cuenta con una suite inicial basada en dependencias obsoletas (Serenity 2.0.91, Cucumber 1.9.51, Gradle 5.2.1 y configuración plana en `serenity.properties` acoplada a un binario `chromedriver.exe` local). 

Para garantizar la estabilidad, mantenibilidad y escalabilidad de las pruebas automatizadas de la aplicación OpenCart bajo los lineamientos del proyecto, es necesario establecer el arquetipo base oficial con **Serenity BDD 4.0.22**, **Cucumber 7.14.0**, **Java 11**, **Gradle 8.14** y el patrón **Screenplay** estricto, configurando los metadatos definitivos del proyecto en `project.md`.

## What Changes

- **Scaffolding de Construcción y Runtime**:
  - Actualización de `build.gradle` con plugins y versiones oficiales (Serenity 4.0.22, Cucumber 7.14.0, Logback 1.4.14, JUnit 4.13.2, AssertJ 3.24.2, POI 5.2.5, Healenium 3.4.3).
  - Configuración de `settings.gradle` con `rootProject.name = 'Purchase'`.
  - Configuración de `gradle/wrapper/gradle-wrapper.properties` apuntando a Gradle 8.14.
  - Actualización de `.gitignore` para ignorar artefactos temporales y de construcción.

- **Configuración de Entorno de Pruebas**:
  - Creación de `src/test/resources/serenity.conf` con formato HOCON, soporte de ambientes (`default`, `qa`, `dev`), switches de Chrome optimizados y `requirements.dir = "src/test/resources/features"` obligatorio para Windows.
  - Creación de `src/test/resources/healenium.properties`.
  - Retiro gradual de la dependencia directa de `chromedriver.exe` local mediante `WebDriverManager`.

- **Capa Base de Arquitectura Screenplay**:
  - Implementación de `Constants.java` en `co.purchase.qa.utils` para timeouts, mensajes y URLs de ambiente.
  - Implementación de la interacción base obligatoria `WaitElement.java` en `co.purchase.qa.interactions`.
  - Implementación de `Hooks.java` en `co.purchase.qa.stepsdefinitions` con `@Managed WebDriver hisDriver`, ciclo de vida `OnStage` y sin `@After` manual.

- **Alcance de Casos de Prueba**:
  - **Casos Incluidos**:
    - `TC-COMPRA-001`: Compra exitosa de un producto como usuario invitado (Guest Checkout) validando la confirmación de la orden en OpenCart.
  - **Casos Fuera de Alcance**:
    - Flujo de compra con usuario registrado autenticado (se abordará en un change futuro `purchase-registered-user`).
    - Validación de cupones de descuento y certificados de regalo en checkout.
    - Flujos de compra fallida por stock insuficiente o datos de pago inválidos.
    - Escaneo masivo del catálogo y generación de `test-cases.xlsx` para otros módulos (Login, Registro, Mi Cuenta).

- **Actualización de Metadatos del Proyecto**:
  - Registro de los placeholders en `openspec/project.md`:
    - `{projectName}` = `Purchase`
    - `{basePackage}` = `co.purchase.qa`
    - `{basePackageDir}` = `co/purchase/qa`
    - `{baseUrl}` = `http://opencart.abstracta.us/index.php?route=common/home`
    - Módulos = `Compra`

## Capabilities

### New Capabilities

- `e2e-purchase`: Flujo de compra E2E en OpenCart como usuario invitado, asegurando la selección del producto, el proceso de checkout y la verificación de orden confirmada mediante Serenity BDD y Screenplay.

### Modified Capabilities

*(Ninguna; no existen especificaciones durables previas en el proyecto).*

## Impact

- **Dependencias**: Se eliminan librerías legacy (`net.serenity-bdd:serenity-cucumber:1.9.51`, `org.slf4j:slf4j-simple`) y se adoptan dependencias estándar modernas de Serenity 4.x.
- **Estructura de Paquetes**: Toda la base se consolida bajo `co.purchase.qa.*` respetando la separación estricta de capas de Screenplay.
- **Compatibilidad**: La ejecución se estandariza para `gradlew.bat clean test` en entornos Windows y CI/CD sin depender de binarios de drivers estáticos.
