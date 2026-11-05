// Autores:
// 2025003008 Sebastian Jhon Ancco Flores
// 2025002346 Herbert Rodrigo Ortiz Condori
// 2025002056 Fernando Cayani Poma
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Comprueba el formulario creado con VS Code y captura su ejecución real. */
public final class PruebasVisual {
    private PruebasVisual() {
    }

    public static void main(String[] argumentos) throws Exception {
        Path imagen = Path.of(argumentos.length > 0 ? argumentos[0] : "evidencias/12_vscode_ejecucion.png");
        Path registro = Path.of(argumentos.length > 1 ? argumentos[1] : "pruebas/resultado_visual.txt");
        Files.createDirectories(imagen.toAbsolutePath().getParent());
        Files.createDirectories(registro.toAbsolutePath().getParent());
        List<String> resultados = new ArrayList<>();
        // Misma apariencia local que MainWindow; esta prueba tampoco depende de lab09.
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        java.awt.Font fuente = new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 13);
        for (Object clave : UIManager.getDefaults().keySet().toArray()) {
            if (clave.toString().endsWith(".font")) {
                UIManager.put(clave, fuente);
            }
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        Robot robot = new Robot();
        robot.setAutoDelay(90);
        try {
            MainWindow marco = edt(() -> {
                MainWindow ventana = new MainWindow();
                ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ventana.setVisible(true);
                ventana.toFront();
                return ventana;
            });
            JTextField nombre = edt(() -> componente(marco, JTextField.class));
            JButton saludar = edt(() -> componente(marco, JButton.class));
            edt(() -> { nombre.setText("Sebastian Jhon"); return null; });
            clic(robot, saludar);
            Dialog saludo = esperarDialogo(robot);
            String mensaje = edt(() -> componente(saludo, JOptionPane.class).getMessage().toString());
            if (!mensaje.equals("Hola, Sebastian Jhon. Bienvenido a Java Swing.")) {
                throw new AssertionError("Saludo incorrecto: " + mensaje);
            }
            resultados.add("OK: al pulsar Saludar con nombre Sebastian Jhon, el diálogo dice: " + mensaje);
            robot.mouseMove(1400, 1040);
            robot.waitForIdle();
            robot.delay(300);
            Rectangle limites = edt(() -> marco.getBounds().union(saludo.getBounds()));
            ImageIO.write(robot.createScreenCapture(limites), "png", imagen.toFile());
            edt(() -> { saludo.dispose(); nombre.setText("   "); return null; });
            clic(robot, saludar);
            Dialog advertencia = esperarDialogo(robot);
            String aviso = edt(() -> componente(advertencia, JOptionPane.class).getMessage().toString());
            if (!aviso.equals("Ingrese su nombre.")) {
                throw new AssertionError("No se advirtió el nombre vacío: " + aviso);
            }
            robot.mouseMove(1400, 1040);
            robot.waitForIdle();
            robot.delay(300);
            Rectangle limitesVacio = edt(() -> marco.getBounds().union(advertencia.getBounds()));
            Path imagenVacia = imagen.toAbsolutePath().getParent().resolve("16_visual_nombre_vacio.png");
            ImageIO.write(robot.createScreenCapture(limitesVacio), "png", imagenVacia.toFile());
            resultados.add("OK: un nombre vacío o formado por espacios muestra: Ingrese su nombre.");
            resultados.add("RESULTADO: 2 comprobaciones del formulario visual correctas.");
            resultados.add("Evidencias: 12_vscode_ejecucion.png y 16_visual_nombre_vacio.png son capturas reales con java.awt.Robot.");
        } catch (Exception | AssertionError fallo) {
            resultados.add("FALLO: " + fallo);
            throw fallo;
        } finally {
            Files.write(registro, resultados, StandardCharsets.UTF_8);
            resultados.forEach(System.out::println);
            edt(() -> {
                for (Window ventana : Window.getWindows()) {
                    ventana.dispose();
                }
                return null;
            });
        }
    }

    private static void clic(Robot robot, JButton boton) throws Exception {
        Point punto = edt(() -> {
            Point origen = boton.getLocationOnScreen();
            origen.translate(boton.getWidth() / 2, boton.getHeight() / 2);
            return origen;
        });
        robot.mouseMove(punto.x, punto.y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
    }

    private static Dialog esperarDialogo(Robot robot) throws Exception {
        for (int intento = 0; intento < 50; intento++) {
            Dialog dialogo = edt(() -> {
                for (Window ventana : Window.getWindows()) {
                    if (ventana instanceof Dialog candidato && candidato.isShowing()) {
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
        throw new AssertionError("No apareció el diálogo esperado.");
    }

    private static <T extends Component> T componente(Container contenedor, Class<T> tipo) {
        for (Component hijo : contenedor.getComponents()) {
            if (tipo.isInstance(hijo)) {
                return tipo.cast(hijo);
            }
            if (hijo instanceof Container anidado) {
                T encontrado = componente(anidado, tipo);
                if (encontrado != null) {
                    return encontrado;
                }
            }
        }
        return null;
    }

    private static <T> T edt(Callable<T> accion) throws Exception {
        FutureTask<T> tarea = new FutureTask<>(accion);
        SwingUtilities.invokeAndWait(tarea);
        return tarea.get();
    }
}
