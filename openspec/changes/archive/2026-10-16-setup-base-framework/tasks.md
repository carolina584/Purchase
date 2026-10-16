# Tasks

## 1. Configuración Base y Scaffolding (AGENTS.md sección 6.1-6.6)

- [x] 1.1 Actualizar `build.gradle` con dependencias y versiones de Serenity 4.0.22, Cucumber 7.14.0 y Logback 1.4.14 y verificar que compila correctamente.
- [x] 1.2 Actualizar `settings.gradle` con `rootProject.name = 'Purchase'` y verificar su configuración.
- [x] 1.3 Configurar `gradle/wrapper/gradle-wrapper.properties` con Gradle 8.14.
- [x] 1.4 Actualizar `.gitignore` según los estándares de `project.md`.
- [x] 1.5 Crear `src/test/resources/serenity.conf` con formato HOCON, ambientes y `requirements.dir = "src/test/resources/features"`.
- [x] 1.6 Crear `src/test/resources/healenium.properties` con la configuración base.

## 2. Capa Main, Utils, Models y Excepciones (AGENTS.md sección 6.7-6.11)

- [x] 2.1 Crear `src/main/java/co/purchase/qa/utils/Constants.java` con timeouts, mensajes y URLs de ambiente.
- [x] 2.2 Actualizar o crear modelos en `src/main/java/co/purchase/qa/models/` (ej. `ProductData.java`).
- [x] 2.3 Crear excepciones por módulo en `src/main/java/co/purchase/qa/exceptions/` (ej. `ExceptionProduct.java`).
- [x] 2.4 Crear la interacción base `src/main/java/co/purchase/qa/interactions/WaitElement.java`.
- [x] 2.5 Crear clases Page/UserInterface en `src/main/java/co/purchase/qa/userinterfaces/` conteniendo únicamente Targets estáticos y dinámicos sin lógica.

## 3. Tasks y Questions (AGENTS.md sección 6.12-6.13)

- [x] 3.1 Implementar las Tasks en `src/main/java/co/purchase/qa/tasks/` (ej. `SearchForProduct`, `AddProductToCart`, `CompleteCheckout`, etc.) agrupando las interacciones de UI de negocio.
- [x] 3.2 Implementar las Questions en `src/main/java/co/purchase/qa/questions/` que consulten el estado de la UI y retornen valores verificables.

## 4. Features, Runners y StepDefinitions (AGENTS.md sección 6.14-6.18)

- [x] 4.1 Crear o actualizar `src/test/resources/features/purchase_product.feature` en español con `# language: es` y escenarios declarativos.
- [x] 4.2 Crear `src/test/java/co/purchase/qa/runners/AllFeaturesRunner.java` y los runners específicos por feature.
- [x] 4.3 Crear `src/test/java/co/purchase/qa/stepsdefinitions/Hooks.java` con `@Managed WebDriver hisDriver`, inicialización de `OnStage` y sin `@After` manual.
- [x] 4.4 Crear o refactorizar los StepDefinitions en `src/test/java/co/purchase/qa/stepsdefinitions/` asegurando que contengan ÚNICAMENTE llamadas a Tasks y Questions (cero interacciones UI directas).

## 5. Documentación, Verificación y Pruebas (AGENTS.md sección 6.19 y Criterios de Éxito)

- [x] 5.1 Actualizar `README.md` con las instrucciones de ejecución y tecnologías del proyecto.
- [x] 5.2 Ejecutar `gradlew.bat clean test` y verificar que el build compila y pasa todas las pruebas con éxito junto con el reporte de Serenity BDD.

## Workflow follow-up

- Verificar el checklist completo de `AGENTS.md` antes de finalizar.
