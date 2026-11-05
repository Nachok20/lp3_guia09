// Autores:
// 2025003008 Sebastian Jhon Ancco Flores
// 2025002346 Herbert Rodrigo Ortiz Condori
// 2025002056 Fernando Cayani Poma
package lab09;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/** Punto de entrada del laboratorio 09. */
public final class Main {
    private Main() {
    }

    /** Sin argumentos abre el menú; acepta flow, border, grid o pasajes. */
    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(() -> {
            Estilo.aplicar();
            if (argumentos.length == 0) {
                crearMenu().setVisible(true);
                return;
            }
            String opcion = argumentos[0].toLowerCase(Locale.ROOT);
            JFrame ventana;
            switch (opcion) {
                case "flow":
                case "border":
                case "grid":
                    ventana = LayoutsFrame.crear(opcion);
                    break;
                case "pasajes":
                    ventana = new PasajesFrame();
                    break;
                default:
                    System.err.println("Uso: java -jar Guia09_LP3.jar [flow|border|grid|pasajes]");
                    return;
            }
            // Una ejecución directa debe terminar al cerrar su única ventana.
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setVisible(true);
        });
    }

    private static JFrame crearMenu() {
        JFrame ventana = new JFrame("Laboratorio 09 | Ancco Flores, Sebastian Jhon / Ortiz Condori, Herbert Rodrigo / Cayani Poma, Fernando");
        ventana.setName("menuLaboratorio");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel contenido = new JPanel(new BorderLayout(16, 16));
        contenido.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        JLabel titulo = new JLabel("<html><center>JAVA SWING BÁSICO<br>"
                + "<small>Ancco Flores, Sebastian Jhon<br>Ortiz Condori, Herbert Rodrigo<br>"
                + "Cayani Poma, Fernando</small></center></html>", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(21.0f));
        contenido.add(titulo, BorderLayout.NORTH);

        JPanel opciones = new JPanel(new GridLayout(2, 2, 12, 12));
        agregarLanzador(opciones, "1. FlowLayout", "abrirFlow", () -> mostrarLayout("FLOW"));
        agregarLanzador(opciones, "2. BorderLayout", "abrirBorder", () -> mostrarLayout("BORDER"));
        agregarLanzador(opciones, "3. GridLayout", "abrirGrid", () -> mostrarLayout("GRID"));
        agregarLanzador(opciones, "4. Compra de pasajes", "abrirPasajes", () -> {
            JFrame pasajes = new PasajesFrame();
            pasajes.setLocationRelativeTo(ventana);
            pasajes.setVisible(true);
        });
        contenido.add(opciones, BorderLayout.CENTER);
        contenido.add(new JLabel("Selecciona una demostración. Cada ventana se puede redimensionar.",
                SwingConstants.CENTER), BorderLayout.SOUTH);
        ventana.setContentPane(contenido);
        ventana.setMinimumSize(new Dimension(820, 430));
        ventana.setSize(850, 460);
        ventana.setLocationRelativeTo(null);
        return ventana;
    }

    private static void mostrarLayout(String tipo) {
        LayoutsFrame.crear(tipo).setVisible(true);
    }

    private static void agregarLanzador(JPanel panel, String texto, String nombre, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.setName(nombre);
        boton.setToolTipText("Abrir " + texto.substring(3));
        boton.addActionListener(evento -> accion.run());
        panel.add(boton);
    }
}
