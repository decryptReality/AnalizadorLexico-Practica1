package analisis.datos;

// - posiciones en los ejes X y Y
// - ubicacion de texto en un JTextArea
public class PosicionXY 
{
    public PosicionXY(int fil,int col) 
    {
        this.fil = fil;
        this.col = col;
    }

    protected int fil;
    protected int col;
    
    public String getPosicion()
    {
        return fil+","+col;
    }
}
