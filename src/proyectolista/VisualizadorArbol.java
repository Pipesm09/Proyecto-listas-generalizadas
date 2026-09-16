package proyectolista;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;

public class VisualizadorArbol extends JFrame {

    public VisualizadorArbol(Nodo raiz) {
        setTitle("Visualización del Árbol Genealógico");

        // Abre la ventana ocupando toda la pantalla para que quepa mejor el árbol
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000, 700); // Tamaño de respaldo por si no se maximiza

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // AÑADIMOS EL PANEL DIRECTAMENTE (Sin el JScrollPane para que no haya barras)
        PanelArbol panel = new PanelArbol(raiz);
        add(panel);
    }
}

class PanelArbol extends JPanel {

    private Nodo raiz;

    private final int ANCHO_NODO = 110;
    private final int ALTO_NODO = 55;
    private final int DISTANCIA_VERTICAL = 65;

    public PanelArbol(Nodo raiz) {
        this.raiz = raiz;
        setBackground(Color.WHITE);
        // Eliminamos el setPreferredSize(2000, 1200) para que tome el tamaño de la ventana
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (raiz != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // CALCULAMOS EL CENTRO EXACTO DE LA VENTANA
            int centroX = getWidth() / 2;

            // Iniciamos el dibujo usando centroX en lugar del 1000 fijo
            dibujarArbolRecursivo(g2, raiz, centroX, 40, 300);
        }
    }

    private void dibujarArbolRecursivo(Graphics2D g2, Nodo actual, int x, int y, int espacioH) {
        if (actual == null) {
            return;
        }

        // 1. Dibujar el cuadro del nodo
        g2.setColor(new Color(220, 235, 252));
        g2.fillRoundRect(x - ANCHO_NODO / 2, y, ANCHO_NODO, ALTO_NODO, 15, 15);

        g2.setColor(new Color(40, 90, 140));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x - ANCHO_NODO / 2, y, ANCHO_NODO, ALTO_NODO, 15, 15);

        // 2. Dibujar el texto (Nombre, Cédula y Edad)
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));

        String nombre = (actual.getInfo() != null && actual.getInfo().getNombre() != null)
                ? actual.getInfo().getNombre() : "Sin Nombre";
        if (nombre.length() > 12) {
            nombre = nombre.substring(0, 10) + "..";
        }
        g2.drawString(nombre, x - ANCHO_NODO / 2 + 8, y + 16);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g2.setColor(Color.DARK_GRAY);
        String cedula = (actual.getInfo() != null && actual.getInfo().getCedula() != null)
                ? actual.getInfo().getCedula() : "S/N";
        g2.drawString("Cédula: " + cedula, x - ANCHO_NODO / 2 + 8, y + 30);

        String edadTexto = "Edad: N/D";
        if (actual.getInfo() != null && actual.getInfo().getFechaNacimiento() != null) {
            int edad = Period.between(actual.getInfo().getFechaNacimiento(), LocalDate.now()).getYears();
            edadTexto = "Edad: " + edad + " años";
        }
        g2.drawString(edadTexto, x - ANCHO_NODO / 2 + 8, y + 44);

        // 3. Dibujar las conexiones con los hijos
        if (actual.getLigalista() != null) {
            int numHijos = 0;
            Nodo temp = actual.getLigalista();
            while (temp != null) {
                numHijos++;
                temp = temp.getLiga();
            }

            int hijoY = y + ALTO_NODO + DISTANCIA_VERTICAL;
            int inicioX = x - (espacioH * (numHijos - 1)) / 2;

            int i = 0;
            temp = actual.getLigalista();
            while (temp != null) {
                int hijoX = (numHijos == 1) ? x : inicioX + (i * espacioH);

                g2.setColor(new Color(120, 120, 120));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(x, y + ALTO_NODO, hijoX, hijoY);

                // Reducimos el espacio horizontal para los nietos y bisnietos
                dibujarArbolRecursivo(g2, temp, hijoX, hijoY, Math.max(espacioH / 2, ANCHO_NODO + 15));

                temp = temp.getLiga();
                i++;
            }
        }
    }
}
