// Autores:
// 2025003008 Sebastian Jhon Ancco Flores
// 2025002346 Herbert Rodrigo Ortiz Condori
// 2025002056 Fernando Cayani Poma
package lab09;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Pruebas de las reglas de la solicitud; no requiere JUnit ni una pantalla. */
public final class PruebasPasaje {
    private static final LocalDate HOY = LocalDate.of(2026, 11, 3);
    private static int aprobadas;

    private PruebasPasaje() { }

    public static void main(String[] args) {
        System.out.println("PRUEBAS DEL MODELO PASAJE — Laboratorio 09");
        System.out.println("Referencia fija: 03/11/2026. Ejecución independiente del reloj y de Swing.");
        System.out.println();

        comprobar("DNI conserva ceros iniciales", () -> contiene(
                base().resumen(), "DNI: 00123456\n"));
        comprobar("El resumen conserva todos los datos del viaje", () -> {
            String texto = base().resumen();
            for (String esperado : List.of("Pasajero: María Pérez", "Fecha de viaje: 15/12/2026",
                    "Origen: Arequipa", "Destino: Lima", "Ubicación: 1er piso",
                    "Servicio: Económico", "Opcionales: Ninguno", "Simulación académica")) {
                contiene(texto, esperado);
            }
        });
        for (String nombre : List.of("María José Núñez", "Ana-María Pérez", "José O'Connor",
                "María D’Arcy", "Jose\u0301 Pérez")) {
            comprobar("Nombre válido: " + nombre, () -> contiene(
                    crear(nombre, "00123456", "15/12/2026", "Arequipa", "Lima",
                            "1er piso", "Económico", List.of()).resumen(), "Pasajero: " + nombre));
        }
        comprobar("Limpia espacios de los datos de texto", () -> {
            String texto = crear("  María   Pérez  ", " 00123456 ", " 15/12/2026 ",
                    "Arequipa", "Lima", "1er piso", "Económico", List.of()).resumen();
            contiene(texto, "Pasajero: María Pérez\n");
            contiene(texto, "DNI: 00123456\n");
            contiene(texto, "Fecha de viaje: 15/12/2026\n");
        });
        comprobar("Permite viajar hoy", () -> crear("María Pérez", "00123456", "03/11/2026",
                "Arequipa", "Lima", "1er piso", "Económico", List.of()));
        comprobar("Acepta 29 de febrero en año bisiesto", () -> crear("María Pérez", "00123456",
                "29/02/2028", "Arequipa", "Lima", "1er piso", "Económico", List.of()));

        for (String nombre : Arrays.asList(null, "", "   ", "Ana123", "-Ana", "Ana-")) {
            rechazar("Rechaza nombre: " + String.valueOf(nombre), () -> crear(nombre,
                    "00123456", "15/12/2026", "Arequipa", "Lima", "1er piso", "Económico", List.of()));
        }
        for (String dni : Arrays.asList(null, "", "1234567", "123456789", "1234567A", "-1234567",
                "1234 567", "１２３４５６７８")) {
            rechazar("Rechaza DNI: " + String.valueOf(dni), () -> crear("María Pérez",
                    dni, "15/12/2026", "Arequipa", "Lima", "1er piso", "Económico", List.of()));
        }
        for (String fecha : Arrays.asList(null, "", "31/11/2026", "29/02/2027", "00/12/2026",
                "01/13/2026", "1/12/2026", "01/2/2027", "2026-12-15", "15/12/26")) {
            rechazar("Rechaza fecha inválida: " + String.valueOf(fecha), () -> crear("María Pérez",
                    "00123456", fecha, "Arequipa", "Lima", "1er piso", "Económico", List.of()));
        }
        rechazar("Rechaza viaje ayer", () -> crear("María Pérez", "00123456", "02/11/2026",
                "Arequipa", "Lima", "1er piso", "Económico", List.of()));
        rechazar("Rechaza referencia de fecha nula", () -> Pasaje.crear("María Pérez", "00123456",
                "15/12/2026", "Arequipa", "Lima", "1er piso", "Económico", List.of(), null));

        for (String ciudad : List.of("Arequipa", "Lima", "Cusco", "Puno", "Tacna")) {
            rechazar("Rechaza origen y destino iguales: " + ciudad, () -> crear("María Pérez",
                    "00123456", "15/12/2026", ciudad, ciudad, "1er piso", "Económico", List.of()));
        }
        for (String ciudad : Arrays.asList(null, "", "Ciudad inexistente")) {
            rechazar("Rechaza origen sin selección válida: " + String.valueOf(ciudad), () -> crear(
                    "María Pérez", "00123456", "15/12/2026", ciudad, "Lima", "1er piso", "Económico", List.of()));
            rechazar("Rechaza destino sin selección válida: " + String.valueOf(ciudad), () -> crear(
                    "María Pérez", "00123456", "15/12/2026", "Arequipa", ciudad, "1er piso", "Económico", List.of()));
        }
        for (String piso : Arrays.asList(null, "", "3er piso")) {
            rechazar("Rechaza piso sin selección válida: " + String.valueOf(piso), () -> crear(
                    "María Pérez", "00123456", "15/12/2026", "Arequipa", "Lima", piso, "Económico", List.of()));
        }
        for (String servicio : Arrays.asList(null, "", "Premium")) {
            rechazar("Rechaza servicio sin selección válida: " + String.valueOf(servicio), () -> crear(
                    "María Pérez", "00123456", "15/12/2026", "Arequipa", "Lima", "1er piso", servicio, List.of()));
        }
        rechazar("Rechaza colección de opcionales nula", () -> crear("María Pérez", "00123456",
                "15/12/2026", "Arequipa", "Lima", "1er piso", "Económico", null));
        rechazar("Rechaza opcional desconocido", () -> crear("María Pérez", "00123456",
                "15/12/2026", "Arequipa", "Lima", "1er piso", "Económico", List.of("Comida")));
        rechazar("Rechaza elemento opcional nulo", () -> crear("María Pérez", "00123456",
                "15/12/2026", "Arequipa", "Lima", "1er piso", "Económico", Arrays.asList("Manta", null)));

        // Cada combinación posible de los tres JCheckBox en cada uno de los tres servicios.
        List<String> opciones = List.of("Audífonos", "Manta", "Revistas");
        for (String calidad : List.of("Económico", "Standard", "VIP")) {
            for (int mascara = 0; mascara < 8; mascara++) {
                List<String> seleccion = new ArrayList<>();
                for (int bit = 0; bit < 3; bit++) {
                    if ((mascara & (1 << bit)) != 0) {
                        seleccion.add(opciones.get(bit));
                    }
                }
                String piso = mascara % 2 == 0 ? "1er piso" : "2do piso";
                String esperados = seleccion.isEmpty() ? "Ninguno" : String.join(", ", seleccion);
                comprobar("Combinación " + mascara + "/7, " + calidad + ", " + piso, () -> {
                    String texto = crear("María Pérez", "00123456", "15/12/2026", "Arequipa", "Lima",
                            piso, calidad, seleccion).resumen();
                    contiene(texto, "Servicio: " + calidad + "\n");
                    contiene(texto, "Ubicación: " + piso + "\n");
                    contiene(texto, "Opcionales: " + esperados + "\n");
                });
            }
        }
        comprobar("La solicitud conserva copia de los opcionales", () -> {
            List<String> seleccion = new ArrayList<>(List.of("Manta"));
            Pasaje pasaje = crear("María Pérez", "00123456", "15/12/2026", "Arequipa", "Lima",
                    "2do piso", "VIP", seleccion);
            seleccion.clear();
            seleccion.add("Audífonos");
            contiene(pasaje.resumen(), "Opcionales: Manta\n");
        });

        System.out.println();
        System.out.println("RESULTADO: " + aprobadas + " pruebas aprobadas; 0 fallidas.");
        System.out.println("Las pruebas cubren reglas y resumen del modelo. La interfaz requiere comprobación gráfica aparte.");
    }

    private static Pasaje base() {
        return crear("María Pérez", "00123456", "15/12/2026", "Arequipa", "Lima",
                "1er piso", "Económico", List.of());
    }

    private static Pasaje crear(String nombre, String documento, String fecha, String origen,
                                String destino, String piso, String servicio, List<String> opcionales) {
        return Pasaje.crear(nombre, documento, fecha, origen, destino, piso, servicio, opcionales, HOY);
    }

    private static void contiene(String texto, String esperado) {
        if (!texto.contains(esperado)) {
            throw new AssertionError("Falta el valor esperado: " + esperado);
        }
    }

    private static void comprobar(String nombre, Runnable prueba) {
        try {
            prueba.run();
            aprobadas++;
            System.out.println("OK  " + nombre);
        } catch (RuntimeException | AssertionError error) {
            System.err.println("FALLO  " + nombre);
            throw error;
        }
    }

    private static void rechazar(String nombre, Runnable operacion) {
        comprobar(nombre, () -> {
            try {
                operacion.run();
            } catch (IllegalArgumentException esperada) {
                if (esperada.getMessage() == null || esperada.getMessage().isBlank()) {
                    throw new AssertionError("La validación debe explicar el problema.");
                }
                return;
            }
            throw new AssertionError("La entrada inválida fue aceptada.");
        });
    }
}
