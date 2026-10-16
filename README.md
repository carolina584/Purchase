# Purchase - Automatización E2E con Serenity BDD 4 + Screenplay

Proyecto de automatización de pruebas funcionales E2E para la tienda OpenCart (`http://opencart.abstracta.us/`) utilizando **Serenity BDD 4.0.22**, **Cucumber 7.14.0**, **Java 11**, **Gradle 8.14** y el patrón **Screenplay**.

## Prerrequisitos
- JDK 11 instalado y configurado en la variable de entorno `JAVA_HOME`.
- Gradle 8.14 (o utilizar el Gradle Wrapper incluido `gradlew.bat`).
- Google Chrome instalado.

## Ejecución de Pruebas
Para ejecutar las pruebas y generar el reporte HTML de Serenity en Windows, ejecute en la raíz del proyecto:
```cmd
gradlew.bat clean test
```

El reporte detallado con capturas de pantalla se generará en:
`target/site/serenity/index.html`
