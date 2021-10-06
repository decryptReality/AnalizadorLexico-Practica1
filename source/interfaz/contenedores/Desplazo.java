package interfaz.contenedores;

import javax.swing.JScrollPane;
import java.awt.Component;
import java.awt.Dimension;

// modificado el 15/09/2021
public class Desplazo extends JScrollPane 
{
    public Desplazo(Component view, int width, int height)
    {
        super(view);
        setPreferredSize(new Dimension(width, height));
    }
}
