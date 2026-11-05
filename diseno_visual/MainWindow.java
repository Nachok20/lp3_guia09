// Autores:
// 2025003008 Sebastian Jhon Ancco Flores
// 2025002346 Herbert Rodrigo Ortiz Condori
// 2025002056 Fernando Cayani Poma
// Diseño generado con Swing GUI Builder 0.0.3. Revisión en VS Code 1.145.0.
import javax.swing.*;
import java.awt.*;

public final class MainWindow extends JFrame {
  private static final long serialVersionUID = 1L;


  private CustomLabel1 etiquetaNombre;
  private CustomTextField1 campoNombre;
  private CustomButton1 botonSaludar;

  public MainWindow() {
    setTitle("Diseño visual | Ancco Flores, Sebastian Jhon / Ortiz Condori, Herbert Rodrigo / Cayani Poma, Fernando");
    setSize(820, 330);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setBackground(new Color(244, 244, 244));
    setLayout(null);

    etiquetaNombre = new CustomLabel1();
    etiquetaNombre.setBounds(36, 77, 120, 28);
    this.add(etiquetaNombre);

    campoNombre = new CustomTextField1();
    campoNombre.setBounds(195, 73, 180, 36);
    this.add(campoNombre);

    botonSaludar = new CustomButton1();
    botonSaludar.setBounds(182, 143, 120, 36);
    botonSaludar.addActionListener(e -> mostrarSaludo());
    this.add(botonSaludar);

    etiquetaNombre.setLabelFor(campoNombre);
    getRootPane().setDefaultButton(botonSaludar);
    setLocationRelativeTo(null);
  }

  private void mostrarSaludo() {
    String nombre = campoNombre.getText().strip();
    if (nombre.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Ingrese su nombre.");
      return;
    }
    JOptionPane.showMessageDialog(this,
        "Hola, " + nombre + ". Bienvenido a Java Swing.",
        "Saludo", JOptionPane.INFORMATION_MESSAGE);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      // Configuración local: este ejemplo se compila sin el paquete lab09.
      try {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
      } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
        System.err.println("Nimbus no está disponible; se conserva la apariencia predeterminada.");
      }
      Font fuente = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
      for (Object clave : UIManager.getDefaults().keySet().toArray()) {
        if (clave.toString().endsWith(".font")) {
          UIManager.put(clave, fuente);
        }
      }
      MainWindow frame = new MainWindow();
      frame.setVisible(true);
    });
  }
}
