package interfaz.texto;

import javax.swing.*;
import java.awt.*;

// modificado el 17/09/2021
public class LineNumbers extends JTextArea
{
    // adherir JLineNumbers a JTextArea 
    private JTextArea adherible;

    public LineNumbers(JTextArea adherible)
    {
        this.adherible = adherible;
        backgroundSettings(238,238,238);
        setEditable(false);
    }

    public void updateLineNumbers()
    {
        setText(getLineNumbers());
    }

    private String getLineNumbers()
    {
        int l = adherible.getLineCount();

        if(l > 1)
        {
            StringBuilder numbers = new StringBuilder();
            //javieroswaldo
            for (int i=1; i<l; i=i+1) 
            {
                numbers.append(" "+i+" ");
                numbers.append(System.lineSeparator());
            }
            numbers.append(" "+l+" ");

            return numbers.toString();
        }
        else 
            {
                return " 1 ";
            }
    }

    public void backgroundSettings(int r,int g,int b)
    {
        setBackground(new Color(r,g,b));
    }
}
