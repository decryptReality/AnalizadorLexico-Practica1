package analisis.datos;

public class Transicion extends PosicionXY
{
    public Transicion(int fil,int col,char caracter,int estado1,int estado2) 
    {
        super(fil,col);
        this.estadoActual = estado1;
        this.caracter = caracter;
        this.estadoDestino = estado2;
    }

    private int estadoActual;
    private char caracter;
    private int estadoDestino;    

    public char getCaracter() {
        return caracter;
    }
    public int getEstadoActual() {
        return estadoActual;
    }
    public int getEstadoDestino() {
        return estadoDestino;
    }
}
