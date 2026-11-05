# Guía 09 LP3 · Java Swing básico

Repositorio del proyecto: [Nachok20/lp3_guia09](https://github.com/Nachok20/lp3_guia09).

**Presentación grupal**

| Código | Apellidos y nombres | Participación |
|---|---|---|
| 2025003008 | Ancco Flores, Sebastian Jhon | 100 % |
| 2025002346 | Ortiz Condori, Herbert Rodrigo | 100 % |
| 2025002056 | Cayani Poma, Fernando | 100 % |

Los nombres y códigos de los tres integrantes se mantienen iguales en el informe, los comentarios del código y los títulos de las ventanas. El trabajo corresponde al Laboratorio 09 de Lenguajes de Programación III de la Universidad Católica de Santa María.

## Entregables

| Archivo | Contenido |
|---|---|
| [Informe PDF](Guia09_LP3_Ancco_Ortiz_Cayani.pdf) · [descargar](Guia09_LP3_Ancco_Ortiz_Cayani.pdf?raw=true) | Actividad, ejercicios, código explicado, capturas y referencias. |
| [Informe Word](Guia09_LP3_Ancco_Ortiz_Cayani.docx?raw=true) | Versión editable del informe. |
| [Proyecto completo en ZIP](Guia09_LP3_Ancco_Ortiz_Cayani.zip?raw=true) | Copia descargable del proyecto con informes, código, pruebas, evidencias y ejecutable. |
| [Aplicación ejecutable JAR](Guia09_LP3.jar?raw=true) | Menú de las demostraciones y del formulario de compra de pasajes; requiere Java 17 o posterior. |

## Estructura del proyecto

El proyecto se encuentra en la **raíz del repositorio**. Abrir esta carpeta en el editor permite acceder directamente a los scripts y a las tareas de compilación.

| Ruta | Función |
|---|---|
| [codigos/lab09/](codigos/lab09/) | Cinco clases: menú `Main`, demostraciones `LayoutsFrame`, formulario `PasajesFrame`, modelo `Pasaje` y apariencia `Estilo`. |
| [diseno_visual/](diseno_visual/) | Cuatro clases del ejemplo creado con Swing GUI Builder; `MainWindow` es su punto de entrada. |
| [pruebas/](pruebas/) | Tres clases de pruebas, resultados de ejecución y registros de compilación y del entorno. |
| [evidencias/](evidencias/) | Capturas reales del editor y de las aplicaciones. |
| [recursos/](recursos/) | Imágenes utilizadas en el informe. |
| [.vscode/](.vscode/) | Configuración, recomendaciones de extensiones y tareas de VS Code. |
| [.swingbuilder-layout.json](.swingbuilder-layout.json) y [.swingbuilder.json](.swingbuilder.json) | Diseño visual editable y carpeta de salida del generador. |
| `compilar.sh` / `compilar.bat` | Compilan las nueve clases de producción y crean el JAR. |
| `ejecutar.sh` / `ejecutar.bat` | Compilan y abren la aplicación elegida. |
| `probar.sh` / `probar.bat` | Compilan y ejecutan las pruebas del modelo y, con `--gui`, las pruebas gráficas. |

## Descargar y abrir

Clonar el repositorio y entrar en su carpeta:

```sh
git clone https://github.com/Nachok20/lp3_guia09.git
cd lp3_guia09
```

También se puede descargar el ZIP de la entrega y abrir la carpeta que contiene este `README.md`.

Para trabajar en VS Code, elegir **File > Open Folder**, abrir la raíz del proyecto e instalar las extensiones recomendadas en [.vscode/extensions.json](.vscode/extensions.json). Se requiere **JDK 17 o posterior** para compilar; comprobar su instalación con:

```sh
java -version
javac -version
```

El entorno documentado utiliza **VS Code 1.145.0**, **OpenJDK 21.0.12.1** y **Swing GUI Builder 0.0.3**. Las salidas de los comandos están en [versiones_comprobadas.txt](pruebas/versiones_comprobadas.txt); los datos del entorno, en [entorno_verificado.json](pruebas/entorno_verificado.json).

## Compilar y ejecutar

Desde la raíz del proyecto, en Linux o macOS:

```sh
sh compilar.sh
java -jar Guia09_LP3.jar
```

En Windows, desde PowerShell o CMD:

```bat
.\compilar.bat
java -jar Guia09_LP3.jar
```

El JAR abre el menú principal. Los argumentos `flow`, `border`, `grid` y `pasajes` abren directamente cada demostración; por ejemplo:

```sh
java -jar Guia09_LP3.jar pasajes
```

Para compilar y ejecutar el ejemplo del diseñador visual, usar `sh ejecutar.sh visual` o `.\ejecutar.bat visual`. Sus cuatro clases también se pueden compilar de forma independiente del paquete principal.

Compilación manual en Linux o macOS:

```sh
mkdir -p build/classes
javac --release 17 -encoding UTF-8 -Xlint:all -d build/classes codigos/lab09/*.java diseno_visual/*.java
jar --create --file Guia09_LP3.jar --main-class lab09.Main -C build/classes .
java -cp build/classes MainWindow
```

Compilación independiente del ejemplo visual, en Linux o macOS:

```sh
mkdir -p build/visual
javac --release 17 -encoding UTF-8 -Xlint:all -d build/visual diseno_visual/*.java
java -cp build/visual MainWindow
```

En VS Code, **Terminal > Run Build Task** (Ctrl+Shift+B) compila el proyecto. En **Terminal > Run Task** están las tareas para crear el ejecutable, abrir el menú, ejecutar el diseño visual y probar el modelo. La aplicación gráfica necesita una sesión de escritorio.

## Pruebas y resultados

En Linux o macOS:

```sh
sh probar.sh
sh probar.sh --gui
```

En Windows:

```bat
.\probar.bat
.\probar.bat --gui
```

| Pruebas incluidas | Resultado | Registro |
|---|---|---|
| Modelo `Pasaje` | **81 aprobadas; 0 fallidas** | [resultado_modelo.txt](pruebas/resultado_modelo.txt) |
| Interfaz de layouts y pasajes | **20 comprobaciones correctas** | [resultado_interfaz.txt](pruebas/resultado_interfaz.txt) |
| Ejemplo del diseñador visual | **2 comprobaciones correctas** | [resultado_visual.txt](pruebas/resultado_visual.txt) |

La ejecución del modelo muestra sus resultados en la terminal y usa una fecha de referencia fija. Las pruebas gráficas calculan la fecha del viaje como hoy más 42 días y utilizan `java.awt.Robot` para pulsar botones y capturar las ventanas. Requieren una pantalla disponible y que el ratón y el foco permanezcan libres durante la ejecución.

Al repetir las pruebas gráficas, los scripts escriben las imágenes en `evidencias_nuevas/` y los registros en `pruebas/resultado_interfaz_nuevo.txt` y `pruebas/resultado_visual_nuevo.txt`. Las evidencias incluidas conservan sus rutas originales. Las ejecuciones documentadas se realizaron en Linux; los scripts de Windows se revisaron sin ejecutarlos en ese sistema.

La declaración de asistencia de IA se encuentra al inicio del informe.

Consultar también [verificacion_compilacion.txt](pruebas/verificacion_compilacion.txt), [verificacion_independencia.txt](pruebas/verificacion_independencia.txt) y [equipo_verificado.txt](pruebas/equipo_verificado.txt).

## Reabrir el diseñador visual

Con Swing GUI Builder instalado, abrir la raíz del proyecto y ejecutar **Swing GUI Builder: Open** desde Ctrl+Shift+P. El archivo `.swingbuilder-layout.json` restaura los componentes, posiciones, título y nombre del evento. **Generate** escribe las clases en `diseno_visual/`.

El generador crea el esqueleto de `mostrarSaludo`; el método completado está en `MainWindow.java`. Conservar una copia antes de regenerar, porque la herramienta puede reemplazar los archivos Java. El informe explica las ediciones manuales del manejador, la asociación entre etiqueta y campo, el botón predeterminado y la apariencia configurada en `main`. Esta extensión se declara en desarrollo.

## Contenido de las aplicaciones

La actividad demuestra **FlowLayout, BorderLayout y GridLayout**, con títulos que identifican a los tres integrantes. El primer ejercicio presenta la creación de una interfaz visual y sus ventajas y desventajas frente al diseño mediante código.

El segundo ejercicio simula la compra de pasajes: recibe nombres, DNI de ocho dígitos, fecha de viaje, origen, destino, piso del bus, calidad y servicios opcionales. Valida los datos, muestra el resumen en un diálogo y permite limpiar todos los controles. Las evidencias incluyen el resumen, la validación de datos, el reinicio y el nombre vacío en el ejemplo visual.

Los registros [enlaces_comprobados.txt](pruebas/enlaces_comprobados.txt) y [repositorio_publico.txt](pruebas/repositorio_publico.txt) documentan las comprobaciones de acceso. [SHA256SUMS.txt](SHA256SUMS.txt) permite comparar los archivos del proyecto con los del ZIP (excluye el propio manifiesto y el ZIP).
