# Design

## Context

Ver `proposal.md` para la motivación. El proyecto requiere migrar de Serenity 2.0.91 y Cucumber 1.x a Serenity BDD 4.0.22, Cucumber 7.14.0, Gradle 8.14 y Java 11, aplicando estrictamente el patrón Screenplay (Separación de capas: Feature, StepDefinition, Task, Question, Interaction, UserInterface, Model).

## Goals / Non-Goals

**Goals:**
- Establecer la estructura completa y obligatoria del arquetipo descrita en `project.md`.
- Implementar el flujo E2E de compra como invitado (Guest Checkout) cumpliendo con las reglas de Screenplay (cero interacciones UI en StepDefinitions, pasos declarativos en español con `# language: es`).
- Configurar correctamente `serenity.conf` con `requirements.dir` para compatibilidad en Windows.

**Non-Goals:**
- Migrar pruebas de usuarios registrados o flujos de cupones en este change.
- Implementar reportes personalizados fuera de Serenity single-page-html.

## Decisions

- **Uso de WebDriverManager**: Se configura `WebDriverManager` y `@Managed WebDriver hisDriver` en `Hooks.java` para evitar acoplamiento a ejecutables de ChromeDriver locales fijos.
- **Estructura Declarativa en Gherkin**: Los escenarios se estructuran de forma declarativa (ej. `Cuando el usuario compra un producto con la siguiente información`) encapsulando las acciones técnicas de UI en Tasks dedicadas.
- **Logback como único logger**: Se reemplaza `slf4j-simple` por `logback-classic:1.4.14` según restricciones del proyecto.

## Risks / Trade-offs

- [Incompatibilidad de dependencias transitivas antiguas] → Mitigación: Forzar versiones exactas especificadas en `project.md` para serenityVersion (4.0.22) y cucumberVersion (7.14.0).
- [Duplicidad de pasos Gherkin] → Mitigación: Validar estrictamente que cada expresión de paso (`@Dado`, `@Cuando`, `@Entonces`) sea única en todo el paquete de glue.
