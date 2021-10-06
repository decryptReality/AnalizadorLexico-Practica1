package analisis.datos;

public class Lexema extends Token
{
    public Lexema(int fil, int col, String lexema, String token) 
    {
        super(fil,col,token);
        this.lexema = lexema;
    }
    private String lexema;
    
    public String getLexema() 
    {
        return lexema;
    }
}
