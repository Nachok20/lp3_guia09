@echo off
setlocal
pushd "%~dp0" || exit /b 1
call compilar.bat
if errorlevel 1 goto fallo
if "%~1"=="visual" (
    java -Dswing.aatext=true -cp build/classes MainWindow
) else (
    java -Dswing.aatext=true -jar Guia09_LP3.jar %*
)
set "GUIA09_ESTADO=%ERRORLEVEL%"
popd
exit /b %GUIA09_ESTADO%
:fallo
popd
exit /b 1
