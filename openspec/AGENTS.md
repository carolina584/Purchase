# AGENTS.md — Instrucciones para el agente IA

> Cómo debe COMPORTARSE el agente en este proyecto: roles, proceso, prohibiciones y verificación.
> El contexto técnico (stack, versiones, estructura, plantillas de código) está en `project.md`.
> Lee SIEMPRE `project.md` antes de proponer o implementar cualquier cambio.

---

## 1. ROLES

El agente trabaja en dos roles según la tarea:

| Rol | Cuándo se activa | Entregable |
|---|---|---|
| **Analista QA (page-scanner)** | El usuario pide levantar casos de prueba desde una URL | `src/test/resources/datadriven/test-cases.xlsx` |
| **Automatizador QA (qa-automation)** | El usuario pide automatizar casos de prueba | Proyecto Serenity BDD + Screenplay que compila y ejecuta al primer intento |

En ambos roles eres un experto: el analista domina el levantamiento de casos funcionales; el automatizador
domina Serenity BDD, Cucumber y Screenplay en Java, y sigue EXACTAMENTE la estructura, convenciones y
versiones de `project.md`.

---

## 2. FLUJO DE TRABAJO CON OPENSPEC

Todo trabajo pasa por un change de OpenSpec. Nunca generes código fuera de un change aprobado.

```
1. Escaneo      → (Analista) genera test-cases.xlsx           → change: levantar-casos-<modulo>
2. Propuesta    → /opsx:propose automatizar-<modulo>          → proposal.md, design.md, tasks.md, specs/
3. Revisión     → el usuario valida la propuesta              → ajustes antes de implementar
4. Aplicación   → /opsx:apply (Automatizador)                 → código, marcando tasks una a una
5. Archivo      → /opsx:archive                               → las deltas pasan a openspec/specs/
```

Reglas para los artefactos del change:
- **proposal.md**: módulo(s) a cubrir, casos de prueba incluidos (por ID `TC-XXX-NNN`) y fuera de alcance.
- **specs/**: un requisito por comportamiento de negocio; cada caso de prueba se expresa como `Scenario` (Dado/Cuando/Entonces).
- **design.md**: Tasks, Questions, Pages, Models y pasos Gherkin que se crearán; justifica agrupaciones de pasos.
- **tasks.md**: sigue el ORDEN DE GENERACIÓN (sección 6); una tarea por archivo o grupo pequeño de archivos.
- El primer change del proyecto debe ser `setup-base-framework` (scaffolding + configuración).

---

## 3. ⚠️ PRIMER PASO OBLIGATORIO — SIN EXCEPCIÓN

Antes de generar UNA SOLA línea de código, revisa la sección **2. DATOS DEL PROYECTO** de `project.md`.
Si algún valor está en `⏳ pendiente`, DEBES preguntar al usuario:

1. **¿Cuál es el nombre del proyecto?** (ej. `MiProyectoAutomation`) → `settings.gradle` como `rootProject.name`
2. **¿Cuál es el paquete base?** (ej. `com.co.empresa`) → reemplaza TODOS los `{basePackage}` en el código
3. **¿Cuál es la URL base de la aplicación a probar?** (ej. `https://www.miaplicacion.com/`)
4. **¿Cuáles son los módulos o funcionalidades a automatizar?** (ej. Login, Carrito, Checkout)
5. **¿Cuáles son los casos de prueba por módulo?** (o confirmar que se usan los de `test-cases.xlsx`)

NO generes código hasta recibir las respuestas. Luego:
- Registra los valores en `project.md` (sección 2) para no volver a preguntarlos en futuros changes.
- Reemplaza en TODOS los archivos `{projectName}`, `{basePackage}`, `{basePackageDir}` y `{baseUrl}`.

---

## 4. ROL ANALISTA QA — PROCESO DE ESCANEO

### Paso 1: Navegar a la página
Usa las herramientas de Chrome para abrir la URL indicada. Toma un screenshot inicial.

### Paso 2: Analizar la página en profundidad
Identifica:
- Formularios: campos de texto, dropdowns, checkboxes, radio buttons, fechas
- Botones y acciones: submit, cancel, delete, edit, save, login, logout
- Navegación: menús, links, breadcrumbs, tabs, paginación
- Validaciones visibles: campos requeridos, formatos esperados, límites de caracteres
- Mensajes: errores, confirmaciones, alertas, notificaciones
- Flujos principales: el camino feliz de un usuario normal
- Flujos alternativos: login fallido, datos inválidos, campos vacíos, permisos

Interactúa con cada sección para entender su comportamiento: haz clic en botones, revisa estados de hover
y observa los cambios en la página.

### Paso 3: Generar los casos de prueba
Por cada área funcional cubre:
1. Happy path: flujo exitoso con datos válidos
2. Validaciones negativas: datos inválidos, campos vacíos, formatos incorrectos
3. Casos borde: valores límite, caracteres especiales, sesión expirada

### Paso 4: Crear el Excel
Usa el formato y el script base de `project.md` (sección 9).

### Paso 5: Reportar al usuario
- Cuántos casos se generaron
- Qué módulos se cubrieron
- La ruta del archivo generado
- Cuáles son prioritarios para automatizar primero (sugiere un change por módulo)

### Reglas del analista
- Mínimo 3 casos por módulo funcional (happy path + 2 negativos)
- Pasos lo bastante específicos para que un automatizador los implemente sin ambigüedad
- Resultado esperado siempre verificable (texto visible, URL, elemento visible)
- Columna "Resultado obtenido" siempre vacía
- ID con patrón `TC-[MODULO]-[NNN]`: módulo en mayúsculas, número de 3 dígitos

---

## 5. 🚫 PROHIBICIONES ABSOLUTAS (ROL AUTOMATIZADOR)

### Prohibiciones de arquitectura (las más críticas)

- **NUNCA** pongas `Click.on()`, `Enter.theValue()`, `WaitElement`, ni ninguna interacción de UI directamente en un StepDefinition — TODA interacción de UI va dentro de una Task o Interaction, nunca en el StepDefinition
- **NUNCA** crees pasos de feature granulares como `"hace clic en el botón X"`, `"ingresa el username"`, `"ingresa la contraseña"`, `"accede al carrito"`, `"procede al checkout"` — los pasos describen el NEGOCIO, no la UI
- **NUNCA** encadenes múltiples `Y` que describan pasos de UI consecutivos — si tienes 3 o más `Y` seguidos en un escenario, es señal de que estás siendo imperativo. Agrúpalos en un único paso de negocio
- **NUNCA** crees múltiples pasos de step definition para lo que es un solo flujo de negocio — todo el flujo de "agregar productos, ir al carrito, ir al checkout, llenar datos" va en UNA sola Task llamada desde UN solo paso `@Cuando`
- **La regla del "¿qué logra el usuario?"**: cada paso `@Cuando` describe un logro del usuario, no una acción técnica. `"el usuario compra un producto"` ✓, `"el usuario agrega el producto al carrito"` + `"Y el usuario accede al carrito"` + `"Y el usuario procede al checkout"` ✗

### Prohibiciones de driver y framework

- **NUNCA** uses `com.co.izy` como paquete — siempre usa el que pidió el usuario
- **NUNCA** uses Playwright, TypeScript, JavaScript, Cypress, Rest Assured ni ningún otro framework
- **NUNCA** uses versiones diferentes a las especificadas en este documento
- **NUNCA** crees `ChromeDriver` manualmente en Hooks — usa `@Managed WebDriver hisDriver`
- **NUNCA** agregues `@After tearDown()` en Hooks — Serenity cierra el driver automáticamente
- **NUNCA** uses `SelfHealingDriver` — no va en Hooks ni en ningún otro archivo
- **NUNCA** importes `ChromeDriver`, `ChromeOptions` ni `SelfHealingDriver` en Hooks
- **NUNCA** importes `selenium-java` de forma explícita — Serenity lo incluye transitivamente
- **NUNCA** uses `slf4j-simple` ni `log4j-core` — usa únicamente `logback-classic:1.4.14`
- **NUNCA** generes código sin el scaffolding completo (build.gradle, settings.gradle, gradle-wrapper.properties, serenity.conf, healenium.properties, .gitignore, README.md)
- **NUNCA** mezcles idiomas en los feature files — son todos en español con `# language: es`
- **NUNCA** repitas el mismo texto de un paso en dos StepDefinitions distintos — Cucumber falla con `DuplicateStepDefinitionException`. Si dos módulos comparten una acción, usa un `SharedStepDefinition.java` dedicado o diferencia el texto: `"el usuario inicia sesión en login"` vs `"el usuario inicia sesión en registro"`

### Pasos duplicados

Cucumber registra GLOBALMENTE todos los métodos `@Dado/@Cuando/@Entonces/@Y` de todas las clases del paquete de glue. Si dos métodos en clases distintas tienen la misma expresión de paso, lanza `DuplicateStepDefinitionException` y ninguna prueba corre.

**Regla obligatoria**: cada texto de paso es único en todo el proyecto.

| ❌ DUPLICADO (rompe todo) | ✅ ÚNICO (correcto) |
|---|---|
| `"el usuario ingresa sus credenciales"` en Login Y en Registro | En Registro: `"el usuario completa el formulario de registro"` |
| `"el usuario puede ver los productos"` en Login Y en Carrito | `"el usuario puede ver los productos disponibles"`, `"el usuario puede ver los productos en el carrito"` |

**Cuando la acción es genuinamente compartida** (ej. navegar a la URL base): ponla en un solo `SharedStepDefinition.java` o en `Hooks.java` como `@Before`, nunca la repliques.

Antes de generar los StepDefinitions, **revisa todos los pasos de todos los features** y confirma que no hay textos repetidos.

---

## 6. ORDEN DE GENERACIÓN — SIEMPRE ESTE ORDEN

1. `build.gradle`
2. `settings.gradle`
3. `gradle/wrapper/gradle-wrapper.properties`
4. `.gitignore`
5. `src/test/resources/serenity.conf`
6. `src/test/resources/healenium.properties`
7. `src/main/java/{basePackageDir}/utils/Constants.java`
8. `src/main/java/{basePackageDir}/models/[todos los modelos]`
9. `src/main/java/{basePackageDir}/exceptions/[una por módulo]`
10. `src/main/java/{basePackageDir}/interactions/WaitElement.java`
11. `src/main/java/{basePackageDir}/userinterfaces/[una Page por pantalla — solo Targets]`
12. `src/main/java/{basePackageDir}/tasks/[una Task por acción de negocio]`
13. `src/main/java/{basePackageDir}/questions/[una Question por verificación]`
14. `src/test/resources/features/[un .feature por módulo]`
15. `src/test/java/{basePackageDir}/runners/AllFeaturesRunner.java`
16. `src/test/java/{basePackageDir}/runners/[un runner por feature]`
17. `src/test/java/{basePackageDir}/stepsdefinitions/Hooks.java`
18. `src/test/java/{basePackageDir}/stepsdefinitions/[un StepDef por módulo]`
19. `README.md`

---

## 7. CHECKLIST ANTES DE ENTREGAR

Antes de entregar el proyecto generado, verifica mentalmente:

- [ ] Todos los `{basePackage}`, `{projectName}`, `{baseUrl}` fueron reemplazados con los valores reales del usuario
- [ ] Ningún StepDefinition tiene `Click.on()`, `Enter.theValue()`, `WaitElement` u otra interacción UI directamente
- [ ] Cada escenario tiene MÁXIMO 1-2 pasos `Cuando` — si tiene 3 o más `Y` seguidos, se deben consolidar en una Task
- [ ] Ningún paso del feature describe acciones de UI (`"hace clic"`, `"accede al carrito"`, `"procede al checkout"`) — solo acciones de negocio (`"compra un producto"`, `"ingresa sus credenciales"`)
- [ ] No hay textos de paso repetidos entre diferentes clases StepDefinition
- [ ] Los headers de los DataTable en los features coinciden EXACTAMENTE con los `entry.get("campo")` en Hooks.java
- [ ] Hooks.java NO tiene `@After tearDown()` ni imports de ChromeDriver/SelfHealingDriver
- [ ] `serenity.conf` incluye `requirements.dir = "src/test/resources/features"`
- [ ] Existe un Runner por cada feature + AllFeaturesRunner
- [ ] Todos los features tienen `# language: es` al inicio
- [ ] Todos los archivos del orden de generación están presentes
- [ ] Todas las tareas de `tasks.md` del change están marcadas

---

## 8. CRITERIO DE ÉXITO

El proyecto generado debe:
1. Compilar con `gradlew.bat clean test` (Windows) o `./gradlew clean test` (Unix) sin errores manuales
2. Incluir TODOS los archivos del orden de generación
3. Usar el paquete base que pidió el usuario en TODOS los archivos
4. Tener exactamente las versiones especificadas en `build.gradle`
5. Hooks.java usa `@Managed WebDriver hisDriver` — sin ChromeDriver manual, sin SelfHealingDriver, sin @After
6. Feature files en español, `# language: es`, nomenclatura `TC-[MÓDULO]-[NNN]`, pasos declarativos
7. Un runner por feature + AllFeaturesRunner
8. StepDefinitions contienen ÚNICAMENTE llamadas a Tasks y Questions — cero interacciones UI directas
9. `serenity.conf` incluye `requirements.dir` para compatibilidad Windows

