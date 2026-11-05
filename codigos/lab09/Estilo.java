// Autores:
// 2025003008 Sebastian Jhon Ancco Flores
// 2025002346 Herbert Rodrigo Ortiz Condori
// 2025002056 Fernando Cayani Poma
package lab09;

import java.awt.Font;
import javax.swing.UIManager;

/** Apariencia compartida de las aplicaciones del equipo. */
public final class Estilo {
    private Estilo() { }

    public static void aplicar() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            // Si Nimbus no está disponible, se conserva la apariencia de la plataforma.
        }
        Font fuente = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
        for (Object clave : UIManager.getDefaults().keySet().toArray()) {
            if (clave.toString().endsWith(".font")) {
                UIManager.put(clave, fuente);
            }
        }
    }
}
