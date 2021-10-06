package interfaz.ventanas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.text.*;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import analisis.*;
import analisis.datos.*;
import interfaz.contenedores.*;
import interfaz.controles.*;
import interfaz.selectores.*;
import interfaz.texto.*;

public class Ejecutar
{
    private static Color cTextoFondo = new Color(20,20,20);
    private static Color cTexto = new Color(0,250,0);

    private static Boton bAcerca = new Boton("?", 30, 26);

    private static Color cResaltado = new Color(120,0,0);
    private static DefaultHighlighter hBuscar = new DefaultHighlighter();
    private static DefaultHighlighter.DefaultHighlightPainter hpBuscar = new DefaultHighlighter.DefaultHighlightPainter(cResaltado);
    private static Boton bBuscar = new Boton("BUSCAR", 100, 26);
    private static CTexto tfBuscar = new CTexto(360, 26);
    private static ETexto lBuscar = new ETexto("0", 0, 130, 26);

    // delimiter = character used to separate the directory names
    // private static String ds = File.separator;
    private static String ls = System.lineSeparator();

    private static String fontPackage = "/interfaz/fuentes/";
    private static String fontName = "SourceCodePro-Regular.ttf";
    private static float fontSize = 14f;
    private static Font fuente;

    private static ATexto taTexto = new ATexto(true);
    private static Desplazo spTexto = new Desplazo(taTexto,600,360);
    private static Color cLineNumbers = new Color(180,180,180);
    private static Color cFondoLineNumbers = new Color(40,40,40);

    // adherir JLineNumbers a JTextArea
    private static LineNumbers lnTexto = new LineNumbers(taTexto);
    
    private static Boton bAnalizar = new Boton("ANALIZAR", 100, 26);
    private static Boton bImportar = new Boton("IMPORTAR", 100, 26);
    private static Boton bExportar = new Boton("EXPORTAR", 100, 26);

    private static Marco fInicio = new Marco("ANALIZADOR LEXICO");

    private static SArchivos fcArchivo = new SArchivos();

    public static void main(String[] args) 
    {
        inicio();
    }

    static void inicio()
    {
        // fc.resetChoosableFileFilters();
        fcArchivo.setFilter(".txt", new String[] { "txt" });

        // aqui se carga la fuente
        cargarFuente();

        taTexto.setBackground(cTextoFondo);
        taTexto.setForeground(cTexto);
        taTexto.setCaretColor(cLineNumbers);
        taTexto.setFont(fuente);
        taTexto.setHighlighter(hBuscar);//160034102
        // actualizar JLineNumber a partir del texto de JTextArea
        taTexto.getDocument().addDocumentListener(dl);

        // lnTexto.backgroundSettings(201,430,061);
        lnTexto.setBackground(cFondoLineNumbers);
        lnTexto.setForeground(cLineNumbers);
        lnTexto.setFont(fuente);

        // adherir JLineNumbers como RowHeader a JScrollPane 
        spTexto.setRowHeaderView(lnTexto);
        
        //lBuscar.backgroundSettings(201,430,061);
        lBuscar.backgroundSettings(cResaltado);
        lBuscar.setForeground(cLineNumbers);
        lBuscar.setFont(fuente);

        tfBuscar.setFont(fuente);

        // agregar acciones a botones
        bAnalizar.addActionListener(al1);
        bImportar.addActionListener(al1);
        bExportar.addActionListener(al1);
        bBuscar.addActionListener(al1);
        bAcerca.addActionListener(al1);
        
        fInicio.add(bBuscar);
        fInicio.add(tfBuscar);
        fInicio.add(lBuscar);
        fInicio.add(spTexto);

        fInicio.add(bAnalizar);
        fInicio.add(bImportar);
        fInicio.add(bExportar);
        fInicio.add(bAcerca);

        fInicio.sizeSettings(false,640,474);
        fInicio.getContentPane().setBackground(cTextoFondo);
        fInicio.locationSettings();
    }

    // acciones para ventana inicial
    private static ActionListener al1 = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            switch (e.getActionCommand()) 
            {
                case "IMPORTAR":
                    importar();
                    break;
                case "EXPORTAR":
                    exportar();
                    break;
                case "BUSCAR":
                    bBuscar.setEnabled(false);
                    hBuscar.removeAllHighlights();
                    Buscador.addPosicion(tfBuscar.getText(), taTexto.getText());
                    for (PosicionXX p : Buscador.getPosiciones()) 
                    {
                        try 
                        {
                            hBuscar.addHighlight(p.getX1(), p.getX2() + 1, hpBuscar);
                        } 
                        catch (Exception e2) 
                            {
                                System.out.println("[!] Highlight");
                            }
                    }
                    lBuscar.setText("" + Buscador.getPosiciones().size());
                    bBuscar.setEnabled(true);
                    break;
                case "ANALIZAR":
                    bAnalizar.setEnabled(false);
                    Automata.ejecutar(taTexto.getText());
                    // imprimir informacion de los objetos de tipo Token
                    // del historial tokens generados con Automata.analizar()
                    Buscador.buscarParaLexemas();
                    bAnalizar.setEnabled(true);
                    reportes();
                    break;
                case "?":
                    String devInfo = "[?] Programa desarrollado por:\n\n    Javier Oswaldo Sacor Quijivix, 201430061\n    Septiembre, 2021";
                    taTexto.setText(devInfo);                    
                    break;
                default:
                    break;
            }
        }
    };

    // acciones para edicion de texto
    private static DocumentListener dl = new DocumentListener()
    {
        @Override
        public void insertUpdate(DocumentEvent e) {
            buqueda();     
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            buqueda();        
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            buqueda();
        }
    };

    static void buqueda()
    {
        hBuscar.removeAllHighlights();
        lBuscar.setText("0");
        lnTexto.updateLineNumbers();
    }

    static void cargarFuente()
    {
        InputStream fontR = Ejecutar.class.getResourceAsStream(fontPackage+fontName);
        try
        {
            // javieroswaldo:
            // createFont() retorna un objeto Font con pointSize = 1, style = Font.PLAIN
            // deriveFont() permite cambiar a un objeto Font su pointSize 
            fuente = Font.createFont(Font.TRUETYPE_FONT,fontR).deriveFont(fontSize);
        } 
        catch (Exception e) 
            {
                System.out.println("[!] Font");
            }
    }

    static void reportes()
    {
        Dialogo dReportes = new Dialogo("REPORTES DE ANALISIS",fInicio,false);
        Boton bLexemas = new Boton("LEXEMAS", 160, 26);
        Boton bErrores = new Boton("FALLOS", 160, 26);//160034102
        Boton bRecuperaciones = new Boton("RESCATES", 160, 26);
        Boton bTransiciones = new Boton("TRANSICIONES", 160, 26);
        Boton bCoincidencias = new Boton("COINCIDENCIAS", 160, 26);

        // acciones para ventana de reportes
        ActionListener al2 = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String title = e.getActionCommand();
                switch (title) 
                {
                    // setColumnIdentifiers() ajusta el encabezado de columnas
                    case "LEXEMAS":
                        DefaultTableModel tmLexemas = new DefaultTableModel();
                        tmLexemas.setColumnIdentifiers(new Object[]{"FIL,COL","LEXEMA","TOKEN"});
                        
                        for (PosicionXY token : Automata.getLog())
                        {
                            if(token instanceof Lexema & !(token instanceof Fallo) & !(token instanceof Rescate))
                            {
                                tmLexemas.addRow(new Object[]
                                {
                                    token.getPosicion(),
                                    ((Lexema)token).getLexema(),
                                    ((Lexema)token).getToken()
                                });
                            }
                        }
                        dTablas(dReportes,title,tmLexemas,360,400);
                        break;

                    case "FALLOS":
                        DefaultTableModel tmErrores = new DefaultTableModel();
                        tmErrores.setColumnIdentifiers(new Object[]{"FIL,COL","LEXEMA","TOKEN"});

                        for (PosicionXY token : Automata.getLog())
                        {
                            if(token instanceof Fallo & !(token instanceof Rescate))
                            {
                                tmErrores.addRow(new Object[]
                                {
                                    token.getPosicion(),
                                    ((Fallo)token).getLexema(),
                                    ((Fallo)token).getToken()
                                });
                            }
                        }
                        dTablas(dReportes,title,tmErrores,360,400);
                        break;

                    case "RESCATES":
                        DefaultTableModel tmRecuperaciones = new DefaultTableModel(0,3);
                        tmRecuperaciones.setColumnIdentifiers(new Object[]{"FIL,COL","LEXEMA","TOKEN"});
                
                        for (PosicionXY p : Automata.getLog())
                        {
                            if(p instanceof Rescate)
                            {
                                tmRecuperaciones.addRow(new Object[]
                                {
                                    p.getPosicion(),
                                    ((Rescate)p).getLexema(),
                                    ((Rescate)p).getToken()
                                });
                            }
                        }
                        dTablas(dReportes,title,tmRecuperaciones,360,400);
                        break;
                    
                    case "TRANSICIONES":
                        DefaultTableModel tmTransiciones = new DefaultTableModel(0,3);
                        tmTransiciones.setColumnIdentifiers(new Object[]{"FIL,COL","CARACTER","ESTADO1","ESTADO2"});
                
                        for (PosicionXY token : Automata.getLog())
                        {
                            if(token instanceof Transicion)
                            {
                                tmTransiciones.addRow(new Object[]
                                {
                                    token.getPosicion(),
                                    ((Transicion)token).getCaracter(),
                                    ((Transicion)token).getEstadoActual(),
                                    ((Transicion)token).getEstadoDestino(),
                                });
                            }
                        }
                        dTablas(dReportes,title,tmTransiciones,460,500);
                        break;

                    case "COINCIDENCIAS":
                        DefaultTableModel tmCoincidencias = new DefaultTableModel(0,3);
                        tmCoincidencias.setColumnIdentifiers(new Object[]{"LEXEMA","K","TOKEN"});
                
                        for (Coincidencia c : Buscador.getCoincidencias())
                        {
                            tmCoincidencias.addRow(new Object[] 
                            { c.getLexema(),c.getK(),c.getToken() });
                        }
                        dTablas(dReportes,title,tmCoincidencias,360,400);
                        break;

                    default:
                        break;
                }
            }
        };

        // actualizar JLineNumber cuando cambia texto de JTextArea
        bLexemas.addActionListener(al2);
        bTransiciones.addActionListener(al2);
        bErrores.addActionListener(al2);
        bRecuperaciones.addActionListener(al2);
        bCoincidencias.addActionListener(al2);

        dReportes.add(bLexemas);
        dReportes.add(bCoincidencias);
        dReportes.add(bErrores);
        dReportes.add(bRecuperaciones);
        dReportes.add(bTransiciones);

        dReportes.sizeSettings(false,360,140);
        dReportes.getContentPane().setBackground(cTextoFondo);
        dReportes.locationSettings();
    }

    // ventana para presentacion de tablas de reportes
    static void dTablas(Dialogo owner,String title,DefaultTableModel tm,int widthSP,int widthD)
    {
        JTable tTokens = new JTable(tm);
        tTokens.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        Desplazo spTokens = new Desplazo(tTokens, widthSP, 260);

        Boton bResize = new Boton("AJUSTAR", 140, 26);
        bResize.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                switch (tTokens.getAutoResizeMode()) 
                {
                    case JTable.AUTO_RESIZE_ALL_COLUMNS:
                        tTokens.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                        break;
                    case JTable.AUTO_RESIZE_OFF:
                        tTokens.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                        break;
                    default:
                        break;
                }
            }
        });

        Dialogo dVentana = new Dialogo(title,owner,false);
        dVentana.add(spTokens);
        dVentana.add(bResize);
        dVentana.getContentPane().setBackground(cTextoFondo);
        dVentana.sizeSettings(false,widthD,340);
        dVentana.locationSettings();
    }
    
    static void importar()
    {
        File txt = fcArchivo.getFile(null);
        //System.out.println(txt.getPath());

        try(Scanner scanner = new Scanner(txt);)
        {
            while (scanner.hasNextLine()) 
            {
                String linea1 = scanner.nextLine() + ls;
                taTexto.append(linea1);
            }
        } 
        catch(Exception e) 
            {
                System.out.println("[!] Importar *.txt");
            }
    }
    static void exportar()
    {
        File txt = fcArchivo.getFile(null);
        //System.out.println(txt.getPath());
        
        try(PrintWriter pwTexto = new PrintWriter(txt);)
        {
            pwTexto.print(taTexto.getText());
        } 
        catch (Exception e) 
            {
                System.out.println("[!] Exportar *.txt");
            }
    }

    public static ATexto getJTextArea() {
        return taTexto;
    }
}
