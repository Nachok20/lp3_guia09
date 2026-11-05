@echo off
setlocal
pushd "%~dp0" || exit /b 1
if not exist build\classes mkdir build\classes
javac --release 17 -encoding UTF-8 -d build/classes codigos/lab09/*.java diseno_visual/*.java
if errorlevel 1 goto fallo
jar --create --file Guia09_LP3.jar --main-class lab09.Main -C build/classes .
if errorlevel 1 goto fallo
popd
exit /b 0
:fallo
 echo Error al compilar. Instala JDK 17 o posterior y agrega su carpeta bin a PATH.
popd
exit /b 1
