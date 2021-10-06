package analisis;

// clase cuyo objetivo es contar las coincidencias encontradas
public class Contador implements ManipularCoincidencia 
{
    private int k = 0;

    @Override
    public void manipular(int i) 
    {
        k = k + 1;
    }

    public int getContador()
    {
        return k;
    }
}
