#!/bin/sh
set -eu
cd "$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
sh compilar.sh
if [ "${1:-}" = "visual" ]; then
    exec java -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -cp build/classes MainWindow
fi
exec java -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -jar Guia09_LP3.jar "$@"
