package analisis.datos;

public class Coincidencia 
{
    public Coincidencia(String lexema,int k,String token) {
        this.lexema = lexema;
        this.token = token;
        this.k = k;
    }
    private String lexema;
    private String token;
    // cantidad de veces que se encontro el lexema en el texto
    private int k;

    public String getLexema() 
    {
        return lexema;
    }
    public int getK() 
    {
        return k;
    }
    public String getToken() {
        return token;
    }
}
