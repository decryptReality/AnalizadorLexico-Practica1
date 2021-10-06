package analisis;

import java.util.Vector;

import analisis.datos.Coincidencia;
import analisis.datos.Fallo;
import analisis.datos.Lexema;
import analisis.datos.PosicionXY;
import analisis.datos.PosicionXX;
import interfaz.ventanas.Ejecutar;

public class Buscador 
{
    private static Vector<PosicionXX> posiciones = new Vector<PosicionXX>();
    private static Vector<Coincidencia> coincidencias = new Vector<Coincidencia>();

    public static Vector<PosicionXX> getPosiciones() {
        return posiciones;
    }

    public static Vector<Coincidencia> getCoincidencias() {
        return coincidencias;
    }    

    // buscar coincidencias para cada lexema del log con 
    // el texto de JTextArea
    public static void buscarParaLexemas()
    {
        coincidencias.clear();

        // para cada objeto de tipo Lexema del log
        for (PosicionXY t1 : Automata.getLog()) 
        {
            if(t1 instanceof Lexema & !(t1 instanceof Fallo))
            {
                String lexema = ((Lexema) t1).getLexema();
                // 160034102 evaluar si se encuentra en historial coincidencias
                boolean enRegistro = enCoincidencias(lexema);
                // si no se encuetra:
                if (!enRegistro) 
                {
                    // agregar al hisorial de coincidencias
                    coincidencias.add(new Coincidencia(lexema, coincidencias(lexema),((Lexema)t1).getToken()));
                }
            }
        }
    }    
    
    // retorna si el "texto" se encuentra en algun objeto Coincidencia 
    // del historial de coincidencias
    static boolean enCoincidencias(String texto)
    {
        boolean r = false;

        for (Coincidencia c : coincidencias) 
        {
            String textoC = c.getLexema();

            if (0 <= texto.length() & texto.length() <= textoC.length()) 
            {
                // aqui se evaluan ambas palabras desde el inicio, javieroswaldo
                // esto es necesario porque cada Coincidencia en coincidencias
                // debe tener texto no repetido (o unico) 
                if(coincidenAyB(texto, textoC, 0))
                {
                    r = true;
                    break;
                }
            }
        }
        return r;
    }

    // comparar con el texto de JTextArea javieroswaldo
    static int coincidencias(String texto)
    {
        Contador k = new Contador();
        buscarAenB(texto, Ejecutar.getJTextArea().getText(), k);
        return k.getContador();
    }

    // ejecutar manipular() del objeto de tipo ManipularCoincidencia 
    // cuando se encuentra una coincidencia
    public static void buscarAenB(String textoA,String textoB,ManipularCoincidencia accion)
    {
        // textoA debe estar contenido en textoB
        // 0 < textoA <= textoB
        if(0 < textoA.length() & textoA.length() <= textoB.length())
        {
            for(int i = 0; i < textoB.length(); i=i+1)
            {
                // primer caracter de textoA debe coincidir con algun caracter de textoB
                if(textoA.charAt(0) == textoB.charAt(i))
                {
                    // SI coincide algun caracter:javieroswaldo
                    // los caracteres restantes desde la posicion de coincidencia 
                    // de textoB deben ser iguales o mayores a los caracteres de textoA
                    if(textoB.length()-i >= textoA.length())
                    {
                        // evaluar coincidencia
                        boolean coinciden = coincidenAyB(textoA,textoB,i);
                        if(coinciden)
                        {
                            // anotar posicion de coincidencia en textoB
                            // System.out.println("[?] Coincidencia en: "+i);

                            // ejecutar el siguiente metodo
                            accion.manipular(i);
                        }
                    }
                }
            }
        }
    }

    // textoA <= textoB
    static boolean coincidenAyB(String textoA,String textoB,int B)
    {
        boolean coinciden = true;

        // i es el indice para textoA, j para textoB desde B
        int i = 0;
        int j = B;
        // recorrer cada caracter de textoA
        while(i < textoA.length())
        {
            // si algun caracter no coincide
            if(textoA.charAt(i) != textoB.charAt(j)) 
            {
                // terminar las iteraciones
                coinciden = false;
                break;
            }
            i = i + 1;
            j = j + 1;
        }

        return coinciden;
    }

    // agregar posiciones
    public static void addPosicion(String textoA,String textoB)
    {
        // borrar posiciones anteriores
        posiciones.clear();

        buscarAenB(textoA,textoB,new ManipularCoincidencia()
        {
            @Override
            public void manipular(int i) 
            {
                posiciones.add(new PosicionXX(i,i+textoA.length()-1));
            };
        });
    }

}
