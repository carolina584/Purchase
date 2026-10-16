# project.md — AgenteQaSauceDemo

> Contexto global del proyecto: QUÉ es, con QUÉ se construye y CÓMO luce el código.
> Las reglas de comportamiento del agente (proceso, prohibiciones, checklist) están en `AGENTS.md`.

---

## 1. PROPÓSITO

Proyecto de automatización de pruebas funcionales E2E con dos capacidades:

1. **Levantamiento de casos de prueba**: escanear una aplicación web y documentar sus casos de prueba en Excel.
2. **Automatización**: convertir esos casos en pruebas ejecutables con Serenity BDD + Cucumber + Screenplay Pattern en Java.

---

## 2. DATOS DEL PROYECTO

Estos valores reemplazan los placeholders en TODOS los archivos. Si alguno está en `⏳ pendiente`,
el agente DEBE preguntarlo al usuario antes de generar código y luego registrarlo aquí (ver `AGENTS.md`).

| Placeholder        | Valor         | Uso                                          |
|--------------------|---------------|----------------------------------------------|
| `{projectName}`    | ⏳ pendiente   | `settings.gradle` → `rootProject.name`       |
| `{basePackage}`    | ⏳ pendiente   | Paquete Java base (ej. `com.co.empresa`)     |
| `{basePackageDir}` | ⏳ pendiente   | Ruta del paquete (ej. `com/co/empresa`)      |
| `{baseUrl}`        | ⏳ pendiente   | URL base de la aplicación bajo prueba        |

**Módulos a automatizar:** ⏳ pendiente (ej. Login, Inventario, Carrito, Checkout)

**Fuente de casos de prueba:** `src/test/resources/datadriven/test-cases.xlsx` (generado por el flujo de escaneo) o los que indique el usuario.

---

## 3. PRINCIPIOS DE ARQUITECTURA

### PRINCIPIO FUNDAMENTAL: SEPARACIÓN DE CAPAS

Este es el principio más importante de toda la arquitectura. Cada capa tiene UNA responsabilidad y no puede hacer el trabajo de otra:

```
Feature (.feature)         → QUÉ hace el negocio   (declarativo, en español, nivel de negocio)
StepDefinition (.java)     → CONECTA el feature con el código (delega a Tasks/Questions ÚNICAMENTE)
Task (.java)               → CÓMO se ejecuta la acción (contiene Click, Enter, Wait, etc.)
Question (.java)           → QUÉ estado se verifica (lee el DOM y retorna un valor)
Interaction (.java)        → Acciones técnicas reutilizables (esperas, excel, acciones complejas)
UserInterface (.java)      → DÓNDE están los elementos (solo Targets, sin lógica)
```

#### Regla de oro del StepDefinition

Un método de StepDefinition tiene exactamente esta estructura:
```java
@Cuando("el usuario realiza la acción X")
public void elUsuarioRealizaLaAccionX(/* parámetros si aplica */) {
    OnStage.theActorInTheSpotlight().attemptsTo(
        TareaX.con(parametros)      // ← SIEMPRE delega a una Task
    );
}

@Entonces("se verifica la condición Y")
public void seVerificaLaCondicionY() {
    OnStage.theActorInTheSpotlight().should(
        seeThat(PreguntaY.displayed(), equalTo(valorEsperado))  // ← SIEMPRE delega a una Question
            .orComplainWith(MiException.class, Constants.MSG_ERROR)
    );
}
```

**NUNCA así:**
```java
// ❌ MAL — interacciones UI en StepDefinition
@Cuando("hace clic en el botón {string}")
public void haceClicEnElBoton(String nombre) {
    OnStage.theActorInTheSpotlight().attemptsTo(
        Click.on(CartPage.BTN_CHECKOUT)   // ← NUNCA aquí
    );
}
```

**SIEMPRE así:**
```java
// ✅ BIEN — el StepDefinition solo delega
@Cuando("el usuario procede al pago")
public void elUsuarioProcedePago() {
    OnStage.theActorInTheSpotlight().attemptsTo(
        ProcederAlPago.enElCarrito()     // ← Task que internamente hace el Click
    );
}
```

---

### PRINCIPIO FUNDAMENTAL: ESCENARIOS DECLARATIVOS

Los feature files describen el COMPORTAMIENTO del negocio, no los pasos técnicos de UI.

#### Regla de los "Y" encadenados

Si un escenario tiene 3 o más pasos `Y` seguidos dentro del mismo bloque `Cuando`, estás siendo imperativo. Agrupa esos pasos en un único `@Cuando` declarativo que llame a una sola Task.

**❌ MAL — imperativo, demasiados "Y", describe cada acción de UI:**
```gherkin
Escenario: TC-VAL-001 – Verificar cálculo de subtotal en checkout
  Cuando el usuario agrega el producto "Sauce Labs Backpack" al carrito
  Y el usuario agrega el producto "Sauce Labs Bike Light" al carrito
  Y el usuario accede al carrito de compras
  Y el usuario procede al checkout desde el carrito
  Y el usuario completa la información del checkout
    | firstName | lastName | postalCode |
    | John      | Doe      | 12345      |
  Entonces el subtotal del checkout es correcto
```

**✅ BIEN — declarativo, un solo paso de negocio:**
```gherkin
Escenario: TC-VAL-001 – Verificar cálculo de subtotal en checkout
  Cuando el usuario compra un producto con la siguiente información
    | firstName | lastName | postalCode |
    | John      | Doe      | 12345      |
  Entonces el subtotal del checkout es correcto
```

El paso `"el usuario compra un producto con la siguiente información"` encapsula TODO el flujo: agregar productos al carrito, navegar al carrito, proceder al checkout, llenar los datos. Todo eso vive dentro de la Task `ComprarProducto.con(datos)`.

**Más ejemplos de conversión:**

| ❌ IMPERATIVO (muchos Y) | ✅ DECLARATIVO (un paso) |
|---|---|
| "agrega al carrito" + "Y va al carrito" + "Y hace checkout" | `"el usuario compra un producto"` |
| "ingresa usuario" + "Y ingresa contraseña" + "Y hace clic en login" | `"el usuario ingresa sus credenciales"` |
| "hace clic en producto" + "Y selecciona talla" + "Y agrega al carrito" | `"el usuario selecciona y agrega el producto al carrito"` |
| "llena nombre" + "Y llena apellido" + "Y llena código postal" | `"el usuario completa la información de envío"` |

#### ¿Cuántos pasos debe tener un escenario?

Un escenario BDD bien escrito tiene EXACTAMENTE esta estructura:
```
Antecedentes (en Característica): precondición común a todos los escenarios
  Dado  → estado inicial (navegar a URL, estar autenticado)
  Y     → máximo 1 condición adicional de contexto

Escenario:
  Cuando → 1 única acción de negocio (a veces 2 si son muy distintas)
  Entonces → 1 verificación de resultado (máximo 2)
```

Si necesitas más de 2 pasos `Cuando` o `Entonces`, es señal de que el escenario está mezclando responsabilidades o siendo imperativo.

---

## 4. STACK TECNOLÓGICO OBLIGATORIO — VERSIONES EXACTAS

```
Gradle wrapper : 8.14
Java           : 11 (sourceCompatibility / targetCompatibility)
Gradle plugin  : net.serenity-bdd.serenity-gradle-plugin 4.0.22

serenityVersion        = 4.0.22
cucumberVersion        = 7.14.0
junitVersion           = 4.13.2   (junit:junit — para CucumberWithSerenity)
assertjVersion         = 3.24.2
poiVersion             = 5.2.5
webdrivermanagerVersion= 5.6.3
healeniumVersion       = 3.4.3
logbackVersion         = 1.4.14
```

---

## 5. ESTRUCTURA DE CARPETAS — OBLIGATORIA Y COMPLETA

```
{projectName}/
├── .gitignore
├── README.md
├── build.gradle
├── settings.gradle
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── src/
│   ├── main/
│   │   └── java/
│   │       └── {basePackageDir}/
│   │           ├── exceptions/
│   │           │   └── [una excepción por módulo, ej. LoginException.java]
│   │           ├── interactions/
│   │           │   ├── WaitElement.java
│   │           │   └── [otras interacciones técnicas reutilizables]
│   │           ├── models/
│   │           │   └── [un modelo por entidad de datos, ej. DataCredentials.java]
│   │           ├── questions/
│   │           │   └── [una Question por verificación de negocio]
│   │           ├── tasks/
│   │           │   └── [una Task por acción de negocio]
│   │           ├── userinterfaces/
│   │           │   └── [una clase Page por pantalla, solo Targets]
│   │           └── utils/
│   │               ├── Constants.java
│   │               └── readers/
│   │                   └── ExcelUtils.java   (si el proyecto usa Excel)
│   └── test/
│       ├── java/
│       │   └── {basePackageDir}/
│       │       ├── runners/
│       │       │   ├── AllFeaturesRunner.java
│       │       │   └── [un Runner por feature]
│       │       └── stepsdefinitions/
│       │           ├── Hooks.java
│       │           └── [un StepDefinition por módulo]
│       └── resources/
│           ├── features/
│           │   └── [un .feature por módulo]
│           ├── healenium.properties
│           └── serenity.conf
```

---

## 6. ARCHIVOS DE CONFIGURACIÓN — CONTENIDO EXACTO

### build.gradle
```groovy
buildscript {
    repositories {
        maven { url "https://plugins.gradle.org/m2/" }
    }
    ext {
        serenityVersion        = '4.0.22'
        cucumberVersion        = '7.14.0'
        poiVersion             = '5.2.5'
        webdrivermanagerVersion = '5.6.3'
        healeniumVersion       = '3.4.3'
    }
    dependencies {
        classpath("net.serenity-bdd:serenity-gradle-plugin:$rootProject.ext.serenityVersion")
        classpath("net.serenity-bdd:serenity-single-page-report:$rootProject.ext.serenityVersion")
    }
}

plugins {
    id 'java'
}

apply plugin: 'net.serenity-bdd.serenity-gradle-plugin'

group = '{basePackage}'
version = '1.0-SNAPSHOT'

sourceCompatibility = 11
targetCompatibility = 11

repositories {
    mavenCentral()
}

serenity {
    reports = ["single-page-html"]
}

dependencies {
    // Serenity BDD
    implementation "net.serenity-bdd:serenity-core:${serenityVersion}"
    implementation "net.serenity-bdd:serenity-cucumber:${serenityVersion}"
    implementation "net.serenity-bdd:serenity-screenplay:${serenityVersion}"
    implementation "net.serenity-bdd:serenity-screenplay-webdriver:${serenityVersion}"
    implementation "net.serenity-bdd:serenity-ensure:${serenityVersion}"

    // Cucumber
    implementation "io.cucumber:cucumber-java:${cucumberVersion}"

    // JUnit (requerido por CucumberWithSerenity)
    testImplementation 'junit:junit:4.13.2'

    // Assertions
    testImplementation 'org.assertj:assertj-core:3.24.2'

    // WebDriver Manager
    implementation "io.github.bonigarcia:webdrivermanager:${webdrivermanagerVersion}"

    // Healenium
    implementation "com.epam.healenium:healenium-web:${healeniumVersion}"

    // Apache POI (Excel)
    implementation "org.apache.poi:poi:${poiVersion}"
    implementation "org.apache.poi:poi-ooxml:${poiVersion}"

    // Logback (único logger permitido)
    implementation 'ch.qos.logback:logback-classic:1.4.14'
}

tasks.withType(Test).configureEach {
    gradle.startParameter.continueOnFailure = true
    test.finalizedBy(aggregate)
}
```

### settings.gradle
```groovy
rootProject.name = '{projectName}'
```

### gradle/wrapper/gradle-wrapper.properties
```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.14-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

### src/test/resources/serenity.conf
```hocon
webdriver {
  driver = chrome
  timeouts {
    implicitlywait = 5000
    fluentwait = 10000
  }
}

headless.mode = false

chrome.switches = """--start-maximized;--no-sandbox;--ignore-certificate-errors;--disable-popup-blocking;--incognito;--disable-gpu;--disable-dev-shm-usage"""

serenity {
  project.name = "{projectName} - Automation Tests"
  take.screenshots = FOR_FAILURES
  # OBLIGATORIO en Windows — evita InvalidPathException al generar reporte aggregate
  requirements.dir = "src/test/resources/features"
}

environments {
  default {
    webdriver.base.url = "{baseUrl}"
  }
  qa {
    webdriver.base.url = "{baseUrl}"
  }
  dev {
    webdriver.base.url = "http://localhost:8080/"
  }
}
```

### src/test/resources/healenium.properties
```properties
recovery-tries = 1
score-cap = 0.5
heal-enabled = true
hlm.server.url = http://localhost:7878
hlm.imitator.url = http://localhost:8000
```

### .gitignore
```
.gradle/
build/
out/
!gradle-wrapper.jar
.idea/
*.iml
.vscode/
*.log
target/
test-output/
.DS_Store
Thumbs.db
*.class
*.jar
!gradle-wrapper.jar
```

> ⚠️ **NOTA SOBRE gradlew / gradlew.bat**: Son archivos binarios que no se pueden generar como texto. Instrúyele al usuario que ejecute `gradle wrapper` una sola vez en la raíz del proyecto (requiere Gradle instalado localmente). Esto crea gradlew, gradlew.bat y el .jar del wrapper.

---

## 7. CÓDIGO JAVA — PATRONES EXACTOS

### Hooks.java — PATRÓN OBLIGATORIO

```java
package {basePackage}.stepsdefinitions;

import {basePackage}.models.DataCredentials;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public class Hooks {

    @Managed
    WebDriver hisDriver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().clearDriverCache().setup();
        OnStage.setTheStage(Cast.ofStandardActors());
        OnStage.theActorCalled("user");
        OnStage.theActorInTheSpotlight().can(BrowseTheWeb.with(hisDriver));
    }

    @DataTableType
    public DataCredentials credentialsEntry(Map<String, String> entry) {
        return new DataCredentials(
                entry.get("username"),
                entry.get("password")
        );
    }

    // Agrega @DataTableType adicionales según los modelos del proyecto
    // Los campos del Map deben coincidir EXACTAMENTE con los headers del DataTable en el feature
}
```

**Claves:**
- `@Managed WebDriver hisDriver` → Serenity inyecta, abre y cierra el driver
- Sin `@After` → Serenity cierra el driver automáticamente
- Sin imports de ChromeDriver, ChromeOptions ni SelfHealingDriver
- Un `@DataTableType` por cada modelo que use DataTable en los features

---

### Runner — PATRÓN OBLIGATORIO

```java
package {basePackage}.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/{nombre}.feature",
        glue = "{basePackage}.stepsdefinitions",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class {Modulo}Runner {
}
```

**AllFeaturesRunner.java** apunta a `features = "src/test/resources/features"`.
Genera SIEMPRE un runner por feature + AllFeaturesRunner.

---

### StepDefinition — PATRÓN OBLIGATORIO

El StepDefinition SOLO contiene llamadas a `OnStage.theActorInTheSpotlight().attemptsTo(Task)` o `.should(seeThat(Question))`. Nunca interacciones UI directas.

```java
package {basePackage}.stepsdefinitions;

import {basePackage}.exceptions.{Modulo}Exception;
import {basePackage}.models.DataCredentials;
import {basePackage}.questions.{PreguntaModulo};
import {basePackage}.tasks.{TareaModulo};
import {basePackage}.utils.Constants;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actors.OnStage;
import net.thucydides.model.util.EnvironmentVariables;
import org.hamcrest.Matchers;

import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class {Modulo}StepDefinition {

    private EnvironmentVariables environmentVariables;

    // ── Dado ──────────────────────────────────────────────────────────────────
    @Dado("que el usuario está en la página web")
    public void queElUsuarioEstaEnLaPaginaWeb() {
        String url = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("environments.default.webdriver.base.url");
        OnStage.theActorInTheSpotlight().wasAbleTo(Open.url(url));
    }

    // ── Cuando ────────────────────────────────────────────────────────────────
    @Cuando("el usuario ingresa sus credenciales")
    public void elUsuarioIngresaSusCredenciales(List<DataCredentials> credenciales) {
        DataCredentials datos = credenciales.get(0);
        OnStage.theActorInTheSpotlight().attemptsTo(
                {TareaModulo}.con(datos)            // delega a la Task — nunca hace Click aquí
        );
    }

    // ── Entonces ──────────────────────────────────────────────────────────────
    @Entonces("el usuario puede ver {palabra}")
    public void elUsuarioPuedeVer(String elemento) {
        OnStage.theActorInTheSpotlight().should(
                seeThat({PreguntaModulo}.displayed(), Matchers.is(true))
                        .orComplainWith({Modulo}Exception.class, Constants.MSG_ERROR)
        );
    }
}
```

---

### Task — PATRÓN OBLIGATORIO

La Task encapsula TODAS las interacciones de UI que corresponden a una acción de negocio.

```java
package {basePackage}.tasks;

import {basePackage}.interactions.WaitElement;
import {basePackage}.models.DataCredentials;
import {basePackage}.userinterfaces.{Pantalla}Page;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

public class {NombreAccion} implements Task {

    private final DataCredentials dataCredentials;

    public {NombreAccion}(DataCredentials dataCredentials) {
        this.dataCredentials = dataCredentials;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        // Aquí van TODOS los pasos de UI de esta acción de negocio
        actor.attemptsTo(Enter.theValue(dataCredentials.getUsername()).into({Pantalla}Page.TXT_USUARIO));
        actor.attemptsTo(Enter.theValue(dataCredentials.getPassword()).into({Pantalla}Page.TXT_PASSWORD));
        actor.attemptsTo(Click.on({Pantalla}Page.BTN_INGRESAR));
        actor.attemptsTo(WaitElement.untilAppears({Pantalla}Page.LBL_BIENVENIDA));
    }

    public static {NombreAccion} con(DataCredentials dataCredentials) {
        return Tasks.instrumented({NombreAccion}.class, dataCredentials);
    }
}
```

---

### Question — PATRÓN OBLIGATORIO

```java
package {basePackage}.questions;

import {basePackage}.userinterfaces.{Pantalla}Page;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.questions.Visibility;

// Ejemplo Question<Boolean> — verifica si un elemento está visible
public class {NombrePregunta} implements Question<Boolean> {

    @Override
    public Boolean answeredBy(Actor actor) {
        return Visibility.of({Pantalla}Page.LBL_ELEMENTO).answeredBy(actor);
    }

    public static {NombrePregunta} displayed() {
        return new {NombrePregunta}();
    }
}

// Ejemplo Question<String> — obtiene el texto de un elemento
// public class {NombrePregunta} implements Question<String> {
//     @Override
//     public String answeredBy(Actor actor) {
//         return Text.of({Pantalla}Page.LBL_ELEMENTO).answeredBy(actor);
//     }
//     public static {NombrePregunta} displayed() { return new {NombrePregunta}(); }
// }
```

---

### UserInterface (Page) — PATRÓN OBLIGATORIO

Solo contiene `Target` estáticos. Sin lógica. Sin constructor público.

```java
package {basePackage}.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class {Pantalla}Page {

    // Targets estáticos — uno por cada elemento de la página
    public static final Target TXT_USUARIO =
            Target.the("campo usuario").locatedBy("//input[@id='user-name']");

    public static final Target TXT_PASSWORD =
            Target.the("campo contraseña").locatedBy("//input[@id='password']");

    public static final Target BTN_INGRESAR =
            Target.the("botón ingresar").locatedBy("//input[@id='login-button']");

    public static final Target LBL_BIENVENIDA =
            Target.the("label bienvenida").locatedBy("//*[text()='Products']");

    // Targets dinámicos — para elementos parametrizados
    public static Target filaPorNombre(String nombre) {
        return Target.the("fila " + nombre)
                .located(By.xpath("//div[normalize-space(text())='" + nombre + "']"));
    }

    private {Pantalla}Page() {
        throw new IllegalStateException("Utility class");
    }
}
```

---

### WaitElement — INTERACCIÓN BASE OBLIGATORIA

```java
package {basePackage}.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitElement implements Interaction {

    private final Target target;
    private final String state;

    public WaitElement(Target target, String state) {
        this.target = target;
        this.state = state;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriverWait wait = new WebDriverWait(
                BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(10));
        switch (state) {
            case "Visible":
                wait.until(ExpectedConditions.visibilityOf(target.resolveFor(actor)));
                break;
            case "Invisible":
                wait.until(ExpectedConditions.invisibilityOf(target.resolveFor(actor)));
                break;
            case "Enabled":
                wait.until(ExpectedConditions.elementToBeClickable(target.resolveFor(actor)));
                break;
            default:
                throw new IllegalArgumentException("Estado no soportado: " + state);
        }
    }

    public static WaitElement untilAppears(Target target) {
        return Tasks.instrumented(WaitElement.class, target, "Visible");
    }

    public static WaitElement untilDisappears(Target target) {
        return Tasks.instrumented(WaitElement.class, target, "Invisible");
    }

    public static WaitElement untilBeEnabled(Target target) {
        return Tasks.instrumented(WaitElement.class, target, "Enabled");
    }
}
```

---

### Model — PATRÓN OBLIGATORIO

```java
package {basePackage}.models;

public class DataCredentials {

    private String username;
    private String password;

    public DataCredentials() {}

    public DataCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
```

Crea un modelo por cada tipo de entidad de datos que se use en DataTables o que se pase entre capas.

---

### Exception — PATRÓN OBLIGATORIO

```java
package {basePackage}.exceptions;

public class {Modulo}Exception extends Error {

    public {Modulo}Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
```

Una excepción por módulo. Se usa en `.orComplainWith({Modulo}Exception.class, Constants.MSG_ERROR)`.

---

### Constants — PATRÓN OBLIGATORIO

```java
package {basePackage}.utils;

public class Constants {

    // Ambientes (claves para EnvironmentSpecificConfiguration)
    public static final String ENV_DEFAULT = "environments.default.webdriver.base.url";
    public static final String ENV_QA      = "environments.qa.webdriver.base.url";
    public static final String ENV_DEV     = "environments.dev.webdriver.base.url";

    // Mensajes de error (usados en orComplainWith)
    public static final String MSG_ERROR_LOGIN    = "No se pudo verificar el login exitoso";
    public static final String MSG_ERROR_PRODUCTO = "El producto no fue encontrado en la página";

    // Timeouts
    public static final int WAIT_SHORT  = 5;
    public static final int WAIT_MEDIUM = 10;
    public static final int WAIT_LONG   = 30;

    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}
```


---

## 8. FEATURE FILES — CONVENCIONES OBLIGATORIAS

```gherkin
# language: es
Característica: [Nombre del módulo funcional]
  Como [rol del usuario]
  Quiero [acción que desea realizar]
  Para [beneficio esperado]

  Antecedentes:
    Dado que el usuario está en la página web
    Y el usuario realiza login exitoso

  Escenario: TC-VAL-001 – Verificar cálculo de subtotal en checkout
    Cuando el usuario compra un producto con la siguiente información
      | firstName | lastName | postalCode |
      | John      | Doe      | 12345      |
    Entonces el subtotal del checkout es correcto

  Escenario: TC-VAL-002 – Verificar cálculo de impuestos en checkout
    Cuando el usuario compra un producto con la siguiente información
      | firstName | lastName | postalCode |
      | John      | Doe      | 12345      |
    Entonces el tax del checkout es mayor que cero
```

El `Task ComprarProducto.con(datos)` internamente hace: agregar productos, ir al carrito, proceder al checkout, llenar los datos. El feature solo describe el QUÉ, no el CÓMO.

**Reglas de feature files:**
- Siempre inicia con `# language: es`
- Nomenclatura: `TC-[MÓDULO]-[NNN]` (ej. TC-LOGIN-001)
- Usa `Antecedentes:` para precondiciones comunes (navegar a URL, hacer login)
- Un feature file por módulo funcional
- Pasos en español: `Dado`, `Cuando`, `Entonces`, `Y`
- Máximo 1-2 pasos `Cuando` por escenario — si tienes más, consolídalos en una sola Task
- **NUNCA encadenes 3 o más `Y` describiendo acciones de UI consecutivas** — eso va en una Task
- Los DataTables llevan los headers exactamente como aparecen en `entry.get("campo")` del `@DataTableType` en Hooks.java


---

## 9. FORMATO DE CASOS DE PRUEBA (EXCEL)

**Ubicación:** `src/test/resources/datadriven/test-cases.xlsx`, hoja `Casos de Prueba`.

| Columna            | Contenido                                                                        |
|--------------------|----------------------------------------------------------------------------------|
| ID                 | `TC-[MÓDULO]-[NNN]`: módulo en mayúsculas, número de 3 dígitos (TC-LOGIN-001)    |
| Módulo             | Nombre del módulo funcional (Login, Productos, Carrito, Checkout…)               |
| Descripción        | Qué valida el caso, en una línea                                                 |
| Paso a paso        | Pasos numerados, específicos y reproducibles                                     |
| Resultado esperado | Resultado verificable (texto visible, URL, elemento visible)                     |
| Resultado obtenido | Siempre vacío al generarse (se llena tras la ejecución)                          |

**Estilo:** encabezado `1F4E79` con texto blanco, filas alternas `D6E4F0`/`FFFFFF`, color por módulo,
panel congelado en `A2` y autofiltro.

### Script base de generación (Python + openpyxl)

Guardar como `generate_cases.py`, reemplazar la lista `casos` con los casos reales y ejecutar
`python generate_cases.py` (requiere `pip install openpyxl`).

```python
import openpyxl
from openpyxl.styles import Font, PatternFill, Alignment, Border, Side
from openpyxl.utils import get_column_letter
import os

wb = openpyxl.Workbook()
ws = wb.active
ws.title = "Casos de Prueba"

# Colores
COLOR_HEADER = "1F4E79"
COLOR_HEADER_FONT = "FFFFFF"
COLOR_ROW_PAR = "D6E4F0"
COLOR_ROW_IMPAR = "FFFFFF"
COLOR_MODULO = "2E75B6"

# Headers
headers = ["ID", "Módulo", "Descripción", "Paso a paso", "Resultado esperado", "Resultado obtenido"]
col_widths = [14, 16, 40, 60, 45, 45]

header_fill = PatternFill(start_color=COLOR_HEADER, end_color=COLOR_HEADER, fill_type="solid")
header_font = Font(name="Calibri", bold=True, color=COLOR_HEADER_FONT, size=11)
thin = Side(style="thin", color="BFBFBF")
border = Border(left=thin, right=thin, top=thin, bottom=thin)
wrap = Alignment(wrap_text=True, vertical="top")

for col, (header, width) in enumerate(zip(headers, col_widths), 1):
    cell = ws.cell(row=1, column=col, value=header)
    cell.font = header_font
    cell.fill = header_fill
    cell.alignment = Alignment(horizontal="center", vertical="center", wrap_text=True)
    cell.border = border
    ws.column_dimensions[get_column_letter(col)].width = width

ws.row_dimensions[1].height = 30

# ── CASOS DE PRUEBA ── (reemplaza con los casos reales identificados)
casos = [
    # [ID, Módulo, Descripción, Paso a paso, Resultado esperado, Resultado obtenido]
    # Ejemplo:
    ["TC-LOGIN-001", "Login",
     "Login exitoso con credenciales válidas",
     "1. Abrir la URL de la aplicación\n2. Ingresar usuario válido\n3. Ingresar contraseña válida\n4. Hacer clic en el botón Login",
     "El usuario es redirigido al dashboard principal y ve su nombre en la barra superior.",
     ""],
    ["TC-LOGIN-002", "Login",
     "Login fallido con contraseña incorrecta",
     "1. Abrir la URL de la aplicación\n2. Ingresar usuario válido\n3. Ingresar contraseña incorrecta\n4. Hacer clic en el botón Login",
     "Se muestra el mensaje de error: 'Epic saddle day! Epic saddle day!' y el usuario permanece en el login.",
     ""],
]

modulo_fills = {}
colors_modulo = ["2E75B6", "5B9BD5", "1F4E79", "70AD47", "ED7D31", "A9D18E"]
color_idx = 0

for row_idx, caso in enumerate(casos, 2):
    modulo = caso[1]
    if modulo not in modulo_fills:
        c = colors_modulo[color_idx % len(colors_modulo)]
        modulo_fills[modulo] = PatternFill(start_color=c, end_color=c, fill_type="solid")
        color_idx += 1

    row_fill = PatternFill(
        start_color=COLOR_ROW_PAR if row_idx % 2 == 0 else COLOR_ROW_IMPAR,
        end_color=COLOR_ROW_PAR if row_idx % 2 == 0 else COLOR_ROW_IMPAR,
        fill_type="solid"
    )

    for col, value in enumerate(caso, 1):
        cell = ws.cell(row=row_idx, column=col, value=value)
        cell.alignment = wrap
        cell.border = border
        if col == 1:  # ID
            cell.font = Font(name="Calibri", bold=True, size=10)
            cell.fill = row_fill
        elif col == 2:  # Módulo
            cell.font = Font(name="Calibri", bold=True, color="FFFFFF", size=10)
            cell.fill = modulo_fills[modulo]
            cell.alignment = Alignment(horizontal="center", vertical="top", wrap_text=True)
        else:
            cell.font = Font(name="Calibri", size=10)
            cell.fill = row_fill
    ws.row_dimensions[row_idx].height = 80

ws.freeze_panes = "A2"
ws.auto_filter.ref = f"A1:F{len(casos)+1}"

os.makedirs("src/test/resources/datadriven", exist_ok=True)
wb.save("src/test/resources/datadriven/test-cases.xlsx")
print(f"Excel generado con {len(casos)} casos de prueba.")
```

Reemplaza la lista `casos` con los casos reales que identificaste al escanear la página.
Instala openpyxl si no está disponible: pip install openpyxl
Ejecuta el script con: python generate_cases.py

---

## 10. GLOSARIO

| Término       | Significado                                                        |
|---------------|--------------------------------------------------------------------|
| Task          | Acción de negocio del actor; agrupa todas las interacciones de UI  |
| Question      | Consulta del estado de la UI que retorna un valor verificable      |
| Interaction   | Acción técnica reutilizable (esperas, acciones complejas)          |
| UserInterface | Clase Page con los `Target` de una pantalla, sin lógica            |
| Happy path    | Flujo exitoso con datos válidos                                    |
