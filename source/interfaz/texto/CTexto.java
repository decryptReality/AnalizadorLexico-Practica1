package interfaz.texto;

import javax.swing.*;
import java.awt.*;

// Campo de texto
public class CTexto extends JTextField
{
    public CTexto(int width, int height)
    {
        setPreferredSize(new Dimension(width, height));
    }
}
