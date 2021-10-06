package interfaz.texto;

import javax.swing.*;
import java.awt.*;

// Etiqueta de texto
public class ETexto extends JLabel
{
    // position SwingConstants.(LEFT/CENTER/RIGHT)
    public ETexto(String text, int position, int width, int height)
    {
        super(text, position);
        setPreferredSize(new Dimension(width, height));
    }

    public void backgroundSettings(int r, int g, int b)
    {
        setOpaque(true);
        setBackground(new Color(r, g, b));
    }
    public void backgroundSettings(Color bg)
    {
        setOpaque(true);
        setBackground(bg);
    }
    public void foregroundSettings(int r, int g, int b)
    {
        setForeground(new Color(r, g, b));
    }

}
