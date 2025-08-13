# Purchase
add product and purchase


Comenzando 🚀 Estas instrucciones te permitirán conocer las tecnologias y herramientas utilizadas en la automatizacion.

Pre-requisitos 📋 Tener instalado JDK y Jre con las versiones 11.0.18 las cuales se ubican en esta ruta cuando se instala el programa: C:\Program Files\Java\jdk11.0.18 --> C:\Program Files\Java\jre11.0.18 El sistema operativo usado es windows 11 de 64 bits Gradle: 5.2.1 IDE: Eclipse Intellij - Instalar los siguientes plugin en caso de no tenerlo: Distribucion de gradle y Cucumber. 

Instalación 🔧 --Configurar las variables de entorno de JAVA (jdk y jre) en la variable path. Configurar variable de entorno de Gradle: se realiza el mismo paso de arriba se crea una nueva variable y se le asigna a path Se descarga y instala el IDE: Intellij 2023.2 

Ejecutando las pruebas ⚙️ --Se debe correr el comando Gradle clean test aggregate para ejecutar la prueba automatizada --Gradle: especifca que vamos a utilizar gradle --clean: Limpia la prueba para generar una nueva -- test: corre la automatizacion --aggregate: Genera el reporte de las pruebas con Serenity Report

Construido con 🛠️ ---> Se utilizan las dependencias de Serenity core ---> Serenity con Junit, Serenity con Cucumber ----> Serenity Screenplay y Serenity WebDriver con la ultima version 2.0.91 --->Webdriver: 139.0.7258.67 ---> Navegador utilizado: Chome Versión 139.0.7258.67 (Build oficial) (64 bits)

Comandos:
pasos para crear proyecto java con gradle desde consola:
-
* ir hacia ruta de proyecto
* desde la ubicación de tu proyecto ir a la consola cmd 
* ingresar gradle init
* seleccionar opción 6
* seleccionar opción 1 y enter
* seleccionar opción 1 (junit)
* ingresar name project
* ingresar source package
se visualiza una seríe de carpetas
importantes para iniciar con py.

comandos ejecutar proyecto y generar reporte:
  -
  * ir hacia ruta de proyecto
  * abrir consola cmd o git bash con clic derecho
en el explorador de archivos
* ingresar gradle clean test agregate
con esto se ejecuta la prueba y se genera reporte
en la siguiente ubicación:
C:\ubicacion_equipo\carpeta_proyecto\target\site\serenity

Autor ✒️ Carolina Muñoz
