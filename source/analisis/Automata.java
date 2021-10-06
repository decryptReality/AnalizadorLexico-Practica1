package analisis;

import java.util.Vector;

import analisis.datos.Fallo;
import analisis.datos.Lexema;
import analisis.datos.PosicionXY;
import analisis.datos.Rescate;
import analisis.datos.Transicion;

public class Automata 
{
    private static Vector<PosicionXY> log = new Vector<PosicionXY>();

    public static Vector<PosicionXY> getLog() { return log; }

    // obtener algun estado de tablaTransiciones
    // valores = {-2,-1,[1,7]}
    // -2 representa fallo desconocido: simbolos que NO pertenecen al alfabeto
    // -1 representa fallo conocido: simbolos que SI pertenecen al alfabeto
    static int estadoDestino(int estadoActual,int tipoCaracter)
    {
        if((0 <= tipoCaracter & tipoCaracter <= 5) & (0 <= estadoActual & estadoActual <= 7))
        {
            return matrizEstadosDestino[estadoActual][tipoCaracter];
        }
        return -2;
    }

    // [estado][tipo de caracter]
    // tipos de caracter:
    // Digito = 0,Letra = 1,Punto = 2,Puntuacion = 3,Operador = 4,Agrupacion = 5
    private static int[][] matrizEstadosDestino = 
    {
        { 2, 1,-1, 5, 6, 7},    // s0
        { 1, 1,-1,-1,-1,-1},    // s1
        { 2,-1, 3,-1,-1,-1},    // s2
        { 4,-1,-1,-1,-1,-1},    // s3
        { 4,-1,-1,-1,-1,-1},    // s4
        {-1,-1,-1,-1,-1,-1},    // s5
        {-1,-1,-1,-1,-1,-1},    // s6
        {-1,-1,-1,-1,-1,-1}     // s7
    };

    private static String getNombreToken(int estado)
    {
        switch (estado)
        {
            case -2:
                return "<no-conocido>";
            case -1:
                return "<conocido>";
            case 0:
                return "<no-conocido>";
            case 1:
                return "identificador";
            case 2:
                return "entero";
            case 3:
                return "decimal";
            case 4:
                return "decimal";
            case 5:
                return "puntuacion";
            case 6:
                return "operador";
            case 7:
                return "agrupacion";
        }
        return "<fallo>";
    }

    // obtener el tipo de caracter
    static int tipoCaracter(char caracter)
    {
        for(int i=0;i<digitos.length;i=i+1)
        {
            if(digitos[i] == caracter)
            {
                return 0;
            }
        }        
        for(int i=0;i<letras.length;i=i+1)
        {
            if(letras[i] == caracter)
            {
                return 1;
            }
        }
        for(int i=0;i<puntuacion.length;i=i+1)
        {
            if(puntuacion[i] == caracter)
            {
                return 3;
            }
        }
        for(int i=0;i<operadores.length;i=i+1)
        {
            if(operadores[i] == caracter)
            {
                return 4;
            }
        }
        for(int i=0;i<agrupacion.length;i=i+1)
        {
            if(agrupacion[i] == caracter)
            {
                return 5;
            }
        }
        return -1;
    }

    public static void ejecutar(String texto)
    {
        // limpiar log
        log.clear();

        // i = indice del caracter actual
        // fil,col = posicion del caracter actual
        int i = 0;
        int fil = 1;
        int col = 1;

        int estadoActual = 0;
        String tokenActual = "";
        String lexemaActual = "";
        // acarreaFallo especifica si hay un fallo al final de un lexema,
        // mas no si hay fallos antes en ese mismo lexema, sin esto 
        // cualquier fallo al final de un lexema no se agregaria al log
        boolean acarreaFallo = false;
        
        // para cada caracter del texto
        while (i < texto.length())
        {
            // obtener caracter y su tipo valores: [-1,5]
            char caracterActual = texto.charAt(i);
            int tipoCaracter = tipoCaracter(caracterActual);

            // aqui se resuleve el problema del punto (decimal o puntuacion)
            if (caracterActual == '.' & estadoActual == 2) 
            {
                // tipoCaracter cambia de puntuacion (3) a punto decimal (2) (valor: 3 a 2)
                tipoCaracter = 2;
            }

            int estadoDestino = estadoActual;
            String tokenDestino = tokenActual;
            String lexemaDestino = lexemaActual + caracterActual;

            boolean esEspacio = Character.isWhitespace(caracterActual);

            int x = col - lexemaActual.length();
            int y = fil;

            // si caracter no es espacio:
            if(!esEspacio) 
            {
                tokenActual = getNombreToken(estadoActual);
                // valores = {-2,-1,[1,7]} 
                estadoDestino = estadoDestino(estadoActual,tipoCaracter);
                // valores = {nombreToken,fallo}
                tokenDestino = getNombreToken(estadoDestino);
                
                // agregar transicion al log
                log.add(new Transicion(fil,col,caracterActual,estadoActual,estadoDestino));
                
                boolean esFallo = estadoDestino == -2 | estadoDestino == -1;

                if(esFallo)
                {
                    // agregar rescate al log SI:
                    if(0 < lexemaActual.length() & esEstadoAceptacion(estadoActual))
                    {
                        log.add(new Rescate(y,x, lexemaActual, tokenActual));
                    }

                    acarreaFallo = true;
    
                    // agregar fallo
                    log.add(new Fallo(y,x,lexemaDestino,tokenActual));

                    // reiniciar automata
                    estadoActual = 0;
                    tokenActual = "";
                    lexemaActual = "";
                }
                if (!esFallo) 
                {
                    estadoActual = estadoDestino;
                    tokenActual = tokenDestino;
                    lexemaActual = lexemaDestino;
                }
            }

            // acarreaFallo cambia a false SI:
            if (esEspacio & acarreaFallo & lexemaActual.length() < 1) 
            {
                acarreaFallo = false;    
            }
            
            // agregar rescates, lexemas al log
            if((esEspacio | i == texto.length()-1) & 0 < lexemaActual.length())
            {
                // decimal incloncluso: estadoDestino = -1
                if(estadoActual == 3) 
                {
                    tokenActual = "decimal";    
                    log.add(new Fallo(y,x,lexemaActual,tokenActual));
                }

                // este es el objetivo de acarreaFallo
                if(acarreaFallo & esEstadoAceptacion(estadoActual))
                {
                    // se alcanzo un espacio o final de texto sin un estadoActual = -1,
                    // antes no se habria registrado el rescate, sino hasta aqui
                    log.add(new Rescate(y,x,lexemaActual,tokenActual));    
                }
                if(!acarreaFallo & esEstadoAceptacion(estadoActual)) 
                {
                    if(esEspacio)
                    {
                        // si lexema finaliza por espacio
                        log.add(new Lexema(y,x,lexemaActual,tokenActual));
                    }
                    if(!esEspacio)
                    {
                        if(i == texto.length()-1)
                        {
                            // si lexema finaliza por ser final de texto
                            log.add(new Lexema(y,x,lexemaActual,tokenActual));
                        }        
                    }
                }

                // reiniciar automata
                acarreaFallo = false;
                estadoActual = 0;
                tokenActual = "";
                lexemaActual = "";
            }

            if(caracterActual == '\n') 
            {
                fil = fil + 1;
                col = 0;
            }

            col = col + 1;
            i = i + 1;
        }
    }

    static private int[] estadosAceptacion = {1,2,4,5,6,7};

    static boolean esEstadoAceptacion(int estado)
    {
        for(int i = 0; i < estadosAceptacion.length; i=i+1)
        {
            if (estadosAceptacion[i] == estado)
            {
                return true;
            }
        }
        return false;
    }

    static char[] digitos = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    static char[] letras = 
    {
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
        'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
        's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 
        'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 
        'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    static char[] puntuacion = {':', ';', ',', '.' };
    static char[] operadores = {'+', '-', '*', '/', '%'};
    static char[] agrupacion = {'{', '}', '[', ']', '(', ')'};
}
