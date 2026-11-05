#!/bin/sh
# Autores: 2025003008 Sebastian Jhon Ancco Flores; 2025002346 Herbert Rodrigo Ortiz Condori; 2025002056 Fernando Cayani Poma
set -eu
cd "$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
mkdir -p build/classes
javac --release 17 -encoding UTF-8 -d build/classes codigos/lab09/*.java diseno_visual/*.java
jar --create --file Guia09_LP3.jar --main-class lab09.Main -C build/classes .
