// Autores:
// 2025003008 Sebastian Jhon Ancco Flores
// 2025002346 Herbert Rodrigo Ortiz Condori
// 2025002056 Fernando Cayani Poma
package lab09;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Pruebas reproducibles de la GUI; las imágenes se capturan de ventanas reales con Robot. */
public final class PruebasInterfaz {
    private static final List<String> RESULTADOS = new ArrayList<>();
    private static Robot robot;
    private static Path evidencias;

    private PruebasInterfaz() {
    }

    public static void main(String[] argumentos) throws Exception {
        evidencias = Path.of(argumentos.length > 0 ? argumentos[0] : "evidencias");
        Path registro = Path.of(argumentos.length > 1 ? argumentos[1] : "pruebas/resultado_interfaz.txt");
        Files.createDirectories(evidencias);
        Files.createDirectories(registro.toAbsolutePath().getParent());
        Estilo.aplicar();
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        robot = new Robot();
        robot.setAutoDelay(85);
        try {
            probarLayouts();
            probarPasajes();
            RESULTADOS.add("RESULTADO: " + RESULTADOS.size() + " comprobaciones de interfaz correctas.");
            RESULTADOS.add("Las 8 imágenes PNG son capturas reales de pantalla realizadas con java.awt.Robot.");
        } catch (Exception | AssertionError fallo) {
            RESULTADOS.add("FALLO: " + fallo);
            throw fallo;
        } finally {
            Files.write(registro, RESULTADOS, StandardCharsets.UTF_8);
            for (String linea : RESULTADOS) {
                System.out.println(linea);
            }
            edt(() -> {
                for (Window ventana : Window.getWindows()) {
                    ventana.dispose();
                }
                return null;
            });
        }
    }

    private static void probarLayouts() throws Exception {
        JFrame flow = edt(() -> mostrar(LayoutsFrame.crear("FLOW")));
        esperarPintado();
        comprobar(edt(() -> buscar(flow, "flowBoton1").getY()
                == buscar(flow, "flowBoton6").getY()), "FlowLayout amplio: seis botones en la misma fila");
        capturar(flow, "01_flow_amplio.png");
        edt(() -> {
            flow.setSize(720, 300);
            flow.setLocationRelativeTo(null);
            return null;
        });
        esperarPintado();
        comprobar(edt(() -> buscar(flow, "flowBoton6").getY()
                > buscar(flow, "flowBoton1").getY()), "FlowLayout estrecho: reorganización en varias filas");
        capturar(flow, "02_flow_estrecho.png");
        edt(() -> { flow.dispose(); return null; });

        JFrame border = edt(() -> mostrar(LayoutsFrame.crear("BORDER")));
        esperarPintado();
        comprobar(edt(() -> buscar(border, "borderNorte").getY() < buscar(border, "borderCentro").getY()
                && buscar(border, "borderSur").getY() > buscar(border, "borderCentro").getY()
                && buscar(border, "borderOeste").getX() < buscar(border, "borderCentro").getX()
                && buscar(border, "borderEste").getX() > buscar(border, "borderCentro").getX()),
                "BorderLayout: las cinco regiones ocupan las posiciones esperadas");
        capturar(border, "03_border.png");
        edt(() -> { border.dispose(); return null; });

        JFrame grid = edt(() -> mostrar(LayoutsFrame.crear("GRID")));
        esperarPintado();
        comprobar(edt(() -> {
            Component primero = buscar(grid, "gridBoton1");
            for (int i = 2; i <= 6; i++) {
                if (!primero.getSize().equals(buscar(grid, "gridBoton" + i).getSize())) {
                    return false;
                }
            }
            return buscar(grid, "gridBoton4").getY() > primero.getY();
        }), "GridLayout: seis celdas del mismo tamaño en dos filas y tres columnas");
        capturar(grid, "04_grid.png");
        edt(() -> { grid.dispose(); return null; });
    }

    private static void probarPasajes() throws Exception {
        String fechaViaje = LocalDate.now().plusDays(42)
                .format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
        PasajesFrame formulario = edt(() -> {
            PasajesFrame marco = new PasajesFrame();
            return (PasajesFrame) mostrar(marco);
        });
        esperarPintado();
        edt(() -> {
            texto(formulario, "nombres").setText("Ana Lucía Pérez");
            texto(formulario, "documento").setText("01234567");
            texto(formulario, "fecha").setText(fechaViaje);
            combo(formulario, "origen").setSelectedItem("Arequipa");
            combo(formulario, "destino").setSelectedItem("Lima");
            lista(formulario).setSelectedValue("VIP", true);
            return null;
        });
        clic(buscarSeguro(formulario, "primerPiso"));
        clic(buscarSeguro(formulario, "segundoPiso"));
        comprobar(edt(() -> !((JRadioButton) buscar(formulario, "primerPiso")).isSelected()
                && ((JRadioButton) buscar(formulario, "segundoPiso")).isSelected()),
                "Piso del bus: seleccionar segundo piso desmarca el primero");
        comprobar(edt(() -> lista(formulario).getSelectionMode() == ListSelectionModel.SINGLE_SELECTION
                && lista(formulario).getSelectedIndices().length == 1
                && "VIP".equals(lista(formulario).getSelectedValue())),
                "JList: solo se admite una calidad de servicio");
        for (String nombre : List.of("audifonos", "manta", "revistas")) {
            clic(buscarSeguro(formulario, nombre));
        }
        esperarPintado();
        capturar(formulario, "05_formulario_completo.png");
        clic(buscarSeguro(formulario, "resumen"));
        Dialog resumen = esperarDialogo("Resumen del pasaje");
        String mensaje = edt(() -> panelOpcion(resumen).getMessage().toString());
        for (String dato : List.of("Ana Lucía Pérez", "01234567", fechaViaje, "Arequipa", "Lima",
                "2do piso", "VIP", "Audífonos", "Manta", "Revistas")) {
            comprobar(mensaje.contains(dato), "Resumen: contiene " + dato);
        }
        esperarPintado();
        capturar(formulario, "06_resumen.png");
        cerrarDialogo(resumen);

        clic(buscarSeguro(formulario, "limpiar"));
        comprobar(edt(() -> texto(formulario, "nombres").getText().isEmpty()
                && texto(formulario, "documento").getText().isEmpty()
                && texto(formulario, "fecha").getText().isEmpty()), "Limpiar: borra todos los campos de texto");
        comprobar(edt(() -> combo(formulario, "origen").getSelectedIndex() == -1
                && combo(formulario, "destino").getSelectedIndex() == -1
                && lista(formulario).isSelectionEmpty()), "Limpiar: quita origen, destino y calidad");
        comprobar(edt(() -> {
            for (String nombre : List.of("primerPiso", "segundoPiso", "audifonos", "manta", "revistas")) {
                if (((AbstractButton) buscar(formulario, nombre)).isSelected()) {
                    return false;
                }
            }
            return true;
        }), "Limpiar: desmarca ambos pisos y todos los servicios opcionales");

        clic(buscarSeguro(formulario, "resumen"));
        Dialog validacion = esperarDialogo("Revise los datos");
        comprobar(edt(() -> panelOpcion(validacion).getMessage().toString().contains("nombres")),
                "Validación: formulario vacío produce advertencia y no muestra resumen");
        esperarPintado();
        capturar(formulario, "07_validacion.png");
        cerrarDialogo(validacion);
        clic(buscarSeguro(formulario, "limpiar"));
        esperarPintado();
        capturar(formulario, "08_formulario_limpio.png");
        edt(() -> { formulario.dispose(); return null; });
    }

    private static JFrame mostrar(JFrame ventana) {
        ventana.setVisible(true);
        ventana.toFront();
        return ventana;
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
        RESULTADOS.add("OK: " + mensaje);
    }

    private static Component buscar(Container contenedor, String nombre) {
        for (Component componente : contenedor.getComponents()) {
            if (nombre.equals(componente.getName())) {
                return componente;
            }
            if (componente instanceof Container hijo) {
                Component encontrado = buscar(hijo, nombre);
                if (encontrado != null) {
                    return encontrado;
                }
            }
        }
        return null;
    }

    private static Component buscarSeguro(Container contenedor, String nombre) throws Exception {
        Component componente = edt(() -> buscar(contenedor, nombre));
        if (componente == null) {
            throw new AssertionError("No existe el componente " + nombre);
        }
        return componente;
    }

    private static JTextField texto(Container contenedor, String nombre) {
        return (JTextField) buscar(contenedor, nombre);
    }

    private static JComboBox<?> combo(Container contenedor, String nombre) {
        return (JComboBox<?>) buscar(contenedor, nombre);
    }

    private static JList<?> lista(Container contenedor) {
        return (JList<?>) buscar(contenedor, "servicio");
    }

    private static void clic(Component componente) throws Exception {
        Point punto = edt(() -> {
            Point ubicacion = componente.getLocationOnScreen();
            ubicacion.translate(componente.getWidth() / 2, componente.getHeight() / 2);
            return ubicacion;
        });
        robot.mouseMove(punto.x, punto.y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
    }

    private static void capturar(Window ventana, String archivo) throws Exception {
        robot.mouseMove(1400, 1040);
        esperarPintado();
        Rectangle limites = edt(ventana::getBounds);
        ImageIO.write(robot.createScreenCapture(limites), "png", evidencias.resolve(archivo).toFile());
    }

    private static Dialog esperarDialogo(String titulo) throws Exception {
        for (int intento = 0; intento < 50; intento++) {
            Dialog dialogo = edt(() -> {
                for (Window ventana : Window.getWindows()) {
                    if (ventana instanceof Dialog candidato && candidato.isShowing()
                            && titulo.equals(candidato.getTitle())) {
                        return candidato;
                    }
                }
                return null;
            });
            if (dialogo != null) {
                return dialogo;
            }
            robot.delay(100);
        }
        throw new AssertionError("No apareció el diálogo " + titulo);
    }

    private static JOptionPane panelOpcion(Container contenedor) {
        for (Component componente : contenedor.getComponents()) {
            if (componente instanceof JOptionPane panel) {
                return panel;
            }
            if (componente instanceof Container hijo) {
                JOptionPane panel = panelOpcion(hijo);
                if (panel != null) {
                    return panel;
                }
            }
        }
        return null;
    }

    private static void cerrarDialogo(Dialog dialogo) throws Exception {
        edt(() -> { dialogo.dispose(); return null; });
        esperarPintado();
    }

    private static void esperarPintado() {
        robot.waitForIdle();
        robot.delay(250);
    }

    private static <T> T edt(Callable<T> operacion) throws Exception {
        FutureTask<T> tarea = new FutureTask<>(operacion);
        SwingUtilities.invokeAndWait(tarea);
        return tarea.get();
    }
}
