// Autores:
// 2025003008 Sebastian Jhon Ancco Flores
// 2025002346 Herbert Rodrigo Ortiz Condori
// 2025002056 Fernando Cayani Poma
package lab09;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

/** Datos validados e inmutables de una solicitud de pasaje. */
public final class Pasaje {
    public static final List<String> CIUDADES =
            List.of("Arequipa", "Lima", "Cusco", "Puno", "Tacna");
    public static final List<String> SERVICIOS = List.of("Económico", "Standard", "VIP");
    public static final List<String> PISOS = List.of("1er piso", "2do piso");
    public static final List<String> OPCIONALES = List.of("Audífonos", "Manta", "Revistas");
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private final String nombres;
    private final String documento;
    private final LocalDate fechaViaje;
    private final String origen;
    private final String destino;
    private final String piso;
    private final String servicio;
    private final List<String> opcionales;

    private Pasaje(String nombres, String documento, LocalDate fechaViaje, String origen,
                   String destino, String piso, String servicio, List<String> opcionales) {
        this.nombres = nombres;
        this.documento = documento;
        this.fechaViaje = fechaViaje;
        this.origen = origen;
        this.destino = destino;
        this.piso = piso;
        this.servicio = servicio;
        this.opcionales = List.copyOf(opcionales);
    }

    /** Construye la solicitud usando la fecha local actual como referencia. */
    public static Pasaje crear(String nombres, String documento, String fecha, String origen,
                               String destino, String piso, String servicio, List<String> opcionales) {
        return crear(nombres, documento, fecha, origen, destino, piso, servicio, opcionales,
                LocalDate.now());
    }

    /** La fecha de referencia explícita permite probar las reglas sin depender del reloj. */
    public static Pasaje crear(String nombres, String documento, String fecha, String origen,
                               String destino, String piso, String servicio, List<String> opcionales,
                               LocalDate hoy) {
        String nombreLimpio = limpiar(nombres).replaceAll("\\s+", " ");
        if (nombreLimpio.isEmpty()
                || !nombreLimpio.matches("[\\p{L}\\p{M}]+(?:[ '’-][\\p{L}\\p{M}]+)*")) {
            throw new IllegalArgumentException("Ingrese nombres y apellidos válidos; use letras y espacios.");
        }
        String dniLimpio = limpiar(documento);
        if (!dniLimpio.matches("[0-9]{8}")) {
            throw new IllegalArgumentException("El DNI debe contener exactamente 8 dígitos.");
        }
        String fechaLimpia = limpiar(fecha);
        if (!fechaLimpia.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")) {
            throw new IllegalArgumentException("Ingrese la fecha en formato dd/MM/yyyy, por ejemplo 15/12/2026.");
        }
        LocalDate fechaValidada;
        try {
            fechaValidada = LocalDate.parse(fechaLimpia, FECHA);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("La fecha de viaje no existe. Revise el día, mes y año.");
        }
        if (hoy == null) {
            throw new IllegalArgumentException("Se requiere una fecha actual de referencia.");
        }
        if (fechaValidada.isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha de viaje debe ser hoy o una fecha futura.");
        }
        if (origen == null || destino == null || !CIUDADES.contains(origen) || !CIUDADES.contains(destino)) {
            throw new IllegalArgumentException("Seleccione una ciudad de origen y una de destino.");
        }
        if (origen.equals(destino)) {
            throw new IllegalArgumentException("El origen y el destino deben ser diferentes.");
        }
        if (piso == null || !PISOS.contains(piso)) {
            throw new IllegalArgumentException("Seleccione el 1er o el 2do piso.");
        }
        if (servicio == null || !SERVICIOS.contains(servicio)) {
            throw new IllegalArgumentException("Seleccione una calidad de servicio de la lista.");
        }
        if (opcionales == null || !opcionales.stream().allMatch(s -> s != null && OPCIONALES.contains(s))) {
            throw new IllegalArgumentException("Los servicios opcionales seleccionados no son válidos.");
        }
        return new Pasaje(nombreLimpio, dniLimpio, fechaValidada, origen, destino, piso,
                servicio, opcionales);
    }

    private static String limpiar(String texto) {
        return texto == null ? "" : texto.strip();
    }

    /** Texto de la solicitud; no realiza cobros ni reserva asientos reales. */
    public String resumen() {
        return "RESUMEN DE LA SOLICITUD DE PASAJE\n\n"
                + "Pasajero: " + nombres + "\n"
                + "DNI: " + documento + "\n"
                + "Fecha de viaje: " + fechaViaje.format(FECHA) + "\n"
                + "Origen: " + origen + "\n"
                + "Destino: " + destino + "\n"
                + "Ubicación: " + piso + "\n"
                + "Servicio: " + servicio + "\n"
                + "Opcionales: " + (opcionales.isEmpty() ? "Ninguno" : String.join(", ", opcionales))
                + "\n\nSimulación académica: no se ha efectuado ninguna compra.";
    }
}
