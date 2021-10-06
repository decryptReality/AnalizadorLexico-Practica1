package analisis.datos;

public abstract class Token extends PosicionXY
{
    public Token(int fil,int col,String nombre) 
    {
        super(fil,col);
        this.nombre = nombre;
    }
    protected String nombre;

    public String getToken() {
        return nombre;
    }
    public void setToken(String token) {
        this.nombre = token;
    }
}