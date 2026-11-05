// Autores:
// 2025003008 Sebastian Jhon Ancco Flores
// 2025002346 Herbert Rodrigo Ortiz Condori
// 2025002056 Fernando Cayani Poma
package lab09;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

/** Formulario de compra de pasajes del laboratorio 09. */
public final class PasajesFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String AYUDA = "Complete los datos y pulse Mostrar resumen. Ctrl+L limpia el formulario.";
    private final JTextField nombres = new JTextField(26);
    private final JTextField documento = new JTextField(15);
    private final JTextField fecha = new JTextField(15);
    private final JComboBox<String> origen = new JComboBox<>(Pasaje.CIUDADES.toArray(String[]::new));
    private final JComboBox<String> destino = new JComboBox<>(Pasaje.CIUDADES.toArray(String[]::new));
    private final JRadioButton primerPiso = new JRadioButton("1er piso");
    private final JRadioButton segundoPiso = new JRadioButton("2do piso");
    private final ButtonGroup pisos = new ButtonGroup();
    private final JList<String> servicio = new JList<>(Pasaje.SERVICIOS.toArray(String[]::new));
    private final JCheckBox audifonos = new JCheckBox("Audífonos");
    private final JCheckBox manta = new JCheckBox("Manta");
    private final JCheckBox revistas = new JCheckBox("Revistas");
    private final JButton limpiar = new JButton("Limpiar");
    private final JButton resumen = new JButton("Mostrar resumen");
    private final JLabel estado = new JLabel(AYUDA);

    public PasajesFrame() {
        super("Compra de pasajes | Ancco Flores, Sebastian Jhon / Ortiz Condori, Herbert Rodrigo / Cayani Poma, Fernando");
        setName("pasajesFrame");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(14, 14));
        JPanel contenido = new JPanel(new BorderLayout(14, 14));
        contenido.setBorder(BorderFactory.createEmptyBorder(18, 22, 16, 22));
        setContentPane(contenido);
        add(crearCabecera(), BorderLayout.NORTH);
        add(crearFormulario(), BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);
        configurarEventos();
        limpiarFormulario();
        setMinimumSize(new Dimension(820, 800));
        setSize(850, 850);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent evento) {
                nombres.requestFocusInWindow();
            }
        });
    }

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout(0, 5));
        JLabel titulo = new JLabel("Solicitud de pasaje terrestre");
        titulo.setName("titulo");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 23f));
        titulo.setForeground(new Color(25, 65, 104));
        JLabel subtitulo = new JLabel("<html>Laboratorio 09 · Java Swing<br>"
                + "Ancco Flores, Sebastian Jhon<br>Ortiz Condori, Herbert Rodrigo<br>"
                + "Cayani Poma, Fernando</html>");
        subtitulo.setName("subtitulo");
        JLabel instrucciones = new JLabel("Datos obligatorios; los servicios adicionales son opcionales.");
        instrucciones.setName("instrucciones");
        cabecera.add(titulo, BorderLayout.NORTH);
        cabecera.add(subtitulo, BorderLayout.CENTER);
        cabecera.add(instrucciones, BorderLayout.SOUTH);
        return cabecera;
    }

    private JPanel crearFormulario() {
        nombres.setName("nombres");
        documento.setName("documento");
        fecha.setName("fecha");
        origen.setName("origen");
        destino.setName("destino");
        primerPiso.setName("primerPiso");
        segundoPiso.setName("segundoPiso");
        servicio.setName("servicio");
        audifonos.setName("audifonos");
        manta.setName("manta");
        revistas.setName("revistas");
        nombres.setToolTipText("Nombres y apellidos del pasajero.");
        documento.setToolTipText("DNI peruano: exactamente 8 dígitos. Se conservan ceros iniciales.");
        fecha.setToolTipText("Formato dd/MM/yyyy. La fecha debe ser hoy o posterior.");
        origen.setToolTipText("Seleccione la ciudad de partida.");
        destino.setToolTipText("Seleccione una ciudad diferente de la de partida.");
        servicio.setToolTipText("Seleccione una calidad: Económico, Standard o VIP.");
        servicio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        servicio.setVisibleRowCount(3);
        servicio.setFixedCellHeight(25);
        pisos.add(primerPiso);
        pisos.add(segundoPiso);
        primerPiso.setActionCommand("1er piso");
        segundoPiso.setActionCommand("2do piso");
        JPanel panelPisos = new JPanel(new GridLayout(1, 2, 10, 0));
        panelPisos.add(primerPiso);
        panelPisos.add(segundoPiso);
        JPanel panelOpcionales = new JPanel(new GridLayout(1, 3, 10, 0));
        panelOpcionales.add(audifonos);
        panelOpcionales.add(manta);
        panelOpcionales.add(revistas);
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setName("formulario");
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del pasajero y del viaje"));
        agregarFila(formulario, 0, "Nombres y apellidos:", nombres, KeyEvent.VK_N);
        agregarFila(formulario, 1, "Documento (DNI):", documento, KeyEvent.VK_D);
        agregarFila(formulario, 2, "Fecha (dd/MM/yyyy):", fecha, KeyEvent.VK_F);
        agregarFila(formulario, 3, "Ciudad de origen:", origen, KeyEvent.VK_O);
        agregarFila(formulario, 4, "Ciudad de destino:", destino, KeyEvent.VK_T);
        agregarFila(formulario, 5, "Piso del bus:", panelPisos, 0);
        JScrollPane lista = new JScrollPane(servicio);
        lista.setName("listaServicioScroll");
        lista.setPreferredSize(new Dimension(280, 84));
        lista.setMinimumSize(new Dimension(180, 84));
        agregarFila(formulario, 6, "Calidad del servicio:", lista, 0);
        agregarFila(formulario, 7, "Servicios opcionales:", panelOpcionales, 0);
        return formulario;
    }

    private static void agregarFila(JPanel panel, int fila, String texto, JComponent componente,
                                    int tecla) {
        GridBagConstraints posicion = new GridBagConstraints();
        posicion.gridx = 0;
        posicion.gridy = fila;
        posicion.anchor = GridBagConstraints.WEST;
        posicion.insets = new Insets(9, 12, 9, 10);
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setName("etiqueta" + fila);
        etiqueta.setLabelFor(componente);
        if (tecla != 0) {
            etiqueta.setDisplayedMnemonic(tecla);
        }
        panel.add(etiqueta, posicion);
        posicion.gridx = 1;
        posicion.weightx = 1.0;
        posicion.fill = GridBagConstraints.HORIZONTAL;
        posicion.insets = new Insets(9, 2, 9, 12);
        panel.add(componente, posicion);
    }

    private JPanel crearPie() {
        JPanel pie = new JPanel(new BorderLayout(0, 11));
        JPanel botones = new JPanel(new GridLayout(1, 2, 14, 0));
        limpiar.setName("limpiar");
        resumen.setName("resumen");
        limpiar.setMnemonic(KeyEvent.VK_L);
        resumen.setMnemonic(KeyEvent.VK_R);
        limpiar.setToolTipText("Borra todos los datos y selecciones. Atajo: Ctrl+L.");
        resumen.setToolTipText("Valida los datos y muestra la solicitud en un cuadro de diálogo.");
        botones.add(limpiar);
        botones.add(resumen);
        estado.setName("estado");
        estado.setFont(estado.getFont().deriveFont(12f));
        estado.setHorizontalAlignment(SwingConstants.LEFT);
        pie.add(botones, BorderLayout.NORTH);
        pie.add(estado, BorderLayout.SOUTH);
        return pie;
    }

    private void configurarEventos() {
        limpiar.addActionListener(evento -> limpiarFormulario());
        resumen.addActionListener(evento -> mostrarResumen());
        origen.addActionListener(evento -> actualizarRuta());
        destino.addActionListener(evento -> actualizarRuta());
        getRootPane().setDefaultButton(resumen);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK), "limpiar");
        getRootPane().getActionMap().put("limpiar", new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent evento) {
                limpiarFormulario();
            }
        });
        // MouseAdapter implementa solo el evento necesario: muestra ayuda al pasar el ratón.
        resumen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evento) {
                estado.setText("Enter o Alt+R: validar y mostrar el resumen de los datos ingresados.");
            }
            @Override
            public void mouseExited(MouseEvent evento) {
                actualizarRuta();
            }
        });
    }

    private void actualizarRuta() {
        Object desde = origen.getSelectedItem();
        Object hasta = destino.getSelectedItem();
        estado.setText(desde != null && hasta != null
                ? "Ruta seleccionada: " + desde + " → " + hasta + ". Pulse Mostrar resumen para validar."
                : AYUDA);
    }

    /** Restaura todos los componentes, incluidas selecciones mutuamente excluyentes. */
    public void limpiarFormulario() {
        nombres.setText("");
        documento.setText("");
        fecha.setText("");
        origen.setSelectedIndex(-1);
        destino.setSelectedIndex(-1);
        pisos.clearSelection();
        servicio.clearSelection();
        audifonos.setSelected(false);
        manta.setSelected(false);
        revistas.setSelected(false);
        estado.setText(AYUDA);
        nombres.requestFocusInWindow();
    }

    private void mostrarResumen() {
        List<String> opcionales = new ArrayList<>();
        if (audifonos.isSelected()) {
            opcionales.add("Audífonos");
        }
        if (manta.isSelected()) {
            opcionales.add("Manta");
        }
        if (revistas.isSelected()) {
            opcionales.add("Revistas");
        }
        try {
            Pasaje pasaje = Pasaje.crear(nombres.getText(), documento.getText(), fecha.getText(),
                    (String) origen.getSelectedItem(), (String) destino.getSelectedItem(),
                    pisos.getSelection() == null ? null : pisos.getSelection().getActionCommand(),
                    servicio.getSelectedValue(), opcionales);
            JOptionPane.showMessageDialog(this, pasaje.resumen(), "Resumen del pasaje",
                    JOptionPane.INFORMATION_MESSAGE);
            estado.setText("Resumen mostrado. Puede editar los datos "
                    + "o iniciar una nueva solicitud con Limpiar.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Revise los datos",
                    JOptionPane.WARNING_MESSAGE);
            estado.setText("Corrija los datos indicados y vuelva a pulsar Mostrar resumen.");
        }
    }
}
