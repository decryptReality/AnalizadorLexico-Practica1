package analisis;

// interfaz cuyo metodo se ejecuta cuando se encuentra alguna coincidencia
// de un texto contenido en el texto contenedor
public interface ManipularCoincidencia 
{
    // el valor de i representa la posicion de la coincidencia 
    // en la cadena contenedora
    void manipular(int i);
}
