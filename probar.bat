@echo off
setlocal
pushd "%~dp0" || exit /b 1
if not "%~2"=="" goto fallo
if not "%~1"=="" if not "%~1"=="--gui" goto fallo
call compilar.bat
if errorlevel 1 goto fallo
if not exist build\pruebas mkdir build\pruebas
javac --release 17 -encoding UTF-8 -cp build/classes -d build/pruebas pruebas/lab09/*.java pruebas/PruebasVisual.java
if errorlevel 1 goto fallo
java -cp "build/classes;build/pruebas" lab09.PruebasPasaje
if errorlevel 1 goto fallo
if "%~1"=="--gui" (
    java -Dswing.aatext=true -cp "build/classes;build/pruebas" lab09.PruebasInterfaz evidencias_nuevas pruebas/resultado_interfaz_nuevo.txt
    if errorlevel 1 goto fallo
    java -Dswing.aatext=true -cp "build/classes;build/pruebas" PruebasVisual evidencias_nuevas/12_vscode_ejecucion.png pruebas/resultado_visual_nuevo.txt
    if errorlevel 1 goto fallo
)
popd
exit /b 0
:fallo
popd
exit /b 1
