#!/bin/sh
set -eu
cd "$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
if [ "$#" -gt 1 ] || { [ "$#" -eq 1 ] && [ "$1" != "--gui" ]; }; then
    echo "Uso: sh probar.sh [--gui]" >&2
    exit 1
fi
sh compilar.sh
mkdir -p build/pruebas
javac --release 17 -encoding UTF-8 -cp build/classes -d build/pruebas pruebas/lab09/*.java pruebas/PruebasVisual.java
java -cp build/classes:build/pruebas lab09.PruebasPasaje
if [ "${1:-}" = "--gui" ]; then
    java -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -cp build/classes:build/pruebas lab09.PruebasInterfaz evidencias_nuevas pruebas/resultado_interfaz_nuevo.txt
    java -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -cp build/classes:build/pruebas PruebasVisual evidencias_nuevas/12_vscode_ejecucion.png pruebas/resultado_visual_nuevo.txt
fi
