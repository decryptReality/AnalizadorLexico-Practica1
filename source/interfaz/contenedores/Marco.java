package interfaz.contenedores;

import javax.swing.*;
import java.awt.*;

public class Marco extends JFrame
{
    public Marco(String title)
    {
        super(title);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void sizeSettings(boolean resize, int width, int height)
    {
        setSize(width, height);
        setResizable(resize);
    }

    public void locationSettings()
    {
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
