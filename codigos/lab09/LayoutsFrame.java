// Autores:
// 2025003008 Sebastian Jhon Ancco Flores
// 2025002346 Herbert Rodrigo Ortiz Condori
// 2025002056 Fernando Cayani Poma
package lab09;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** Demostraciones de los tres administradores de esquemas de la actividad IV.1. */
public final class LayoutsFrame {
    private static final String INTEGRANTES = "Ancco Flores, Sebastian Jhon / Ortiz Condori, Herbert Rodrigo / Cayani Poma, Fernando";
    private static final Color AZUL = new Color(219, 234, 254);
    private static final Color VERDE = new Color(220, 252, 231);
    private static final Color AMBAR = new Color(254, 243, 199);

    private LayoutsFrame() {
        // Clase de fábrica: no necesita instancias.
    }

    /**
     * Crea una ventana sin mostrarla. Tipos admitidos: FLOW, BORDER y GRID.
     * Debe invocarse desde el hilo de eventos de Swing.
     */
    public static JFrame crear(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Debe indicar FLOW, BORDER o GRID.");
        }
        String clave = tipo.toUpperCase(Locale.ROOT);
        String nombre;
        switch (clave) {
            case "FLOW":
                nombre = "FlowLayout";
                break;
            case "BORDER":
                nombre = "BorderLayout";
                break;
            case "GRID":
                nombre = "GridLayout";
                break;
            default:
                throw new IllegalArgumentException("Tipo desconocido: " + tipo);
        }

        JFrame ventana = new JFrame(nombre + " | " + INTEGRANTES);
        ventana.setName("demo" + clave);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel contenido = new JPanel();
        contenido.setName("panel" + clave);
        contenido.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        ventana.setContentPane(contenido);

        switch (clave) {
            case "FLOW":
                configurarFlow(contenido);
                ventana.setSize(940, 240);
                ventana.setMinimumSize(new Dimension(260, 220));
                break;
            case "BORDER":
                configurarBorder(contenido);
                ventana.setSize(760, 380);
                ventana.setMinimumSize(new Dimension(520, 280));
                break;
            case "GRID":
                configurarGrid(contenido);
                ventana.setSize(760, 380);
                ventana.setMinimumSize(new Dimension(400, 260));
                break;
            default:
                throw new AssertionError("Tipo ya validado.");
        }
        ventana.setLocationRelativeTo(null);
        return ventana;
    }

    private static void configurarFlow(JPanel panel) {
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 12));
        for (int indice = 1; indice <= 6; indice++) {
            JButton boton = crearBoton("Botón " + indice, "flowBoton" + indice, AZUL);
            boton.setPreferredSize(new Dimension(135, 48));
            boton.setToolTipText("FlowLayout: conserva su tamaño "
                    + "y pasa a otra fila si falta espacio.");
            panel.add(boton);
        }
    }

    private static void configurarBorder(JPanel panel) {
        panel.setLayout(new BorderLayout(10, 10));
        panel.add(crearBoton("NORTE · NORTH", "borderNorte", AZUL), BorderLayout.NORTH);
        panel.add(crearBoton("SUR · SOUTH", "borderSur", AZUL), BorderLayout.SOUTH);
        panel.add(crearBoton("OESTE · WEST", "borderOeste", AMBAR), BorderLayout.WEST);
        panel.add(crearBoton("ESTE · EAST", "borderEste", AMBAR), BorderLayout.EAST);
        panel.add(crearBoton("CENTRO · CENTER", "borderCentro", VERDE), BorderLayout.CENTER);
    }

    private static void configurarGrid(JPanel panel) {
        panel.setLayout(new GridLayout(2, 3, 10, 10));
        for (int indice = 1; indice <= 6; indice++) {
            int fila = (indice - 1) / 3 + 1;
            int columna = (indice - 1) % 3 + 1;
            JButton boton = crearBoton(
                    "<html><center>Botón " + indice + "<br>Fila " + fila
                            + " · Columna " + columna + "</center></html>",
                    "gridBoton" + indice, indice % 2 == 0 ? AZUL : VERDE);
            boton.setToolTipText("GridLayout: todas las celdas tienen el mismo tamaño.");
            panel.add(boton);
        }
    }

    private static JButton crearBoton(String texto, String nombre, Color color) {
        JButton boton = new JButton(texto);
        boton.setName(nombre);
        boton.setBackground(color);
        boton.setOpaque(true);
        boton.setFocusPainted(true);
        boton.setFont(boton.getFont().deriveFont(15.0f));
        return boton;
    }
}
