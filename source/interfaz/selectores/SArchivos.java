package interfaz.selectores;

import java.awt.*;
import java.io.File;
import javax.swing.JFileChooser;

public class SArchivos extends JFileChooser
{
    public File getFile(Component parent)
    {
        //javieroswaldo
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setMultiSelectionEnabled(false);
        setDialogTitle("Select file");
        int stateOnPopDown = showDialog(parent, "Select");
        if (stateOnPopDown == JFileChooser.APPROVE_OPTION)
        {
            return getSelectedFile();
        }
        return null;
    }

    public void setFilter(String description, String[] extensions)
    {
        setFileFilter(new FileFilter_1(description, extensions));
    }
}
