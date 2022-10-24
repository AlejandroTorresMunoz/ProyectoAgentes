package env;
// Environment code for project proyectoprueba.mas2j



import jason.asSyntax.*;
import jason.environment.Environment; //Clase para el environment
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView; //Clase para el mundo
import jason.environment.grid.Location;

import jason.mas2j.MAS2JProject;
import jason.mas2j.AgentParameters;
import jason.infra.local.BaseLocalMAS;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.logging.Logger;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.HashMap;
import java.util.Iterator;  
import java.lang.String;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import java.util.logging.*;
import java.lang.reflect.Field;


public class TestEnv extends Environment {

    //Definición de variables globales
    public static final int GSize = 15; // Tamaño del grid que define el mundo

    class InfoLibro
    {
        //Clase para almacenar la información de un libro
        String autor;
        String titulo;
        private InfoLibro(String[] valueStrings)
        {
            //Constructor de la clase
            titulo = valueStrings[0];
            autor = valueStrings[1];
        }
    }

    public static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }

    //Array con los libros en el cajon de devoluciones
    public int num_libros_cajon = 0;
    public InfoLibro[] libros_cajon = {};

    //ID's de los objetos --> IMPORTANTE : Por lo que parece, los objetos sólo pueden tener una ID que sea MULTIPLO DE 2
    public static final int CAJA = 16; //Código del objeto de la caja
    public static final int LIBRO = 32; //Código del objeto LIBRO
    public static final int CAJON = 64; //Código del objeto de la caja

    //Posición de los objetos
    public static final int pos_x_caja = 1; //Posición de la caja, en x
    public static final int pos_y_caja = 1; //Posición de la caja, en y
    //Posición del cajón de devoluciones
    public static final int pos_x_cajon = 0; //Posición de la caja, en x
    public static final int pos_y_cajon = 0; //Posición de la caja, en y
    //Definición de acciones de los agentes
    public static final Term colocar_libro = Literal.parseLiteral("colocar_libro(x,y,tipo_objeto)"); //Acción para colocar un libro en una posición
    public static final Term tomar_libro   = Literal.parseLiteral("tomar_libro(x,y,tipo_objeto)"); //Acción para tomar un libro de una posición

    //Definición de predicados
    public static final Literal libro_depositado_caja = Literal.parseLiteral("libro_depositado(caja)"); //Lirbo depositado sobre caja


    static Logger logger = Logger.getLogger(TestEnv.class.getName());

    private MarsModel model;
    private MarsView view;
    //private BaseLocal LocalMasClass;

    @Override
    public void init(String[] args) {
        model = new MarsModel(); //Clase donde se modelan las acciones
        view  = new MarsView(model); //Clase donde se modela 
        model.setView(view);
        updatePercepts();
        //LocalMasClass = new BaseLocal(TestEnv.class.getName());
    }

    /* 

    class LocalMas extends BaseLocalMAS
    {
        public LocalMas()
        {
            logger.info("Hola");
            //super(); //Constructor de la clase dela que hereda
            //logger.info("Num agentes : "+Integer.toString(super.getNbAgents()));
        }
    }
    */




    @Override
    public boolean executeAction(String agName, Structure action) { 
        /*
         * Este método es ejecutado cada que un agente intenta ejecutar una acción en este Environment.
         * agName : Nombre del agente
         * action : Estructura de la acción
         */
        logger.info(agName+" is doing : "+action); //Se dice qué agente está ejecutando qué acción

        try{
            if(action.getFunctor().equals("colocar_libro"))
            {
                //En el caso de que se trate de colocar un libro
                int x = (int)((NumberTerm)action.getTerm(0)).solve(); //En primer parámetro de la función es la x
                int y = (int)((NumberTerm)action.getTerm(1)).solve(); //El segundo parámetro de la función es la y
                String tipo_objeto = (String)((StringTerm)action.getTerm(2)).getString(); //El tercer término es el tipo de objeto donde se deposita el libro
                java.util.Collection<Term> valores = ((MapTerm)action.getTerm(3)).values(); //Valores recibidas

                Object[] aux = valores.toArray(); //Array de objetos con los valores recibidos
                String[] values = new String[aux.length]; //Array de strings donde se van a almacenar los valores recibidos
                for(int i=0;i<aux.length;i++)
                {
                    values[i] = aux[i].toString(); //Se guarda cada uno de los valores
                }

                InfoLibro info = new InfoLibro(values); //Clase para almacenar la información del libro
                
                model.colocarlibro(x, y, tipo_objeto, info); //Se ejecuta la acción
            }
            else if(action.getFunctor().equals("tomar_libro"))
            {
                //En el caso de que se trate de la acción de tomar un libro
                int x = (int)((NumberTerm)action.getTerm(0)).solve(); //En primer parámetro de la función es la x
                int y = (int)((NumberTerm)action.getTerm(1)).solve(); //El segundo parámetro de la función es la y

                java.util.Set<Term> claves = ((MapTerm)action.getTerm(2)).keys(); //Claves recibidas
                java.util.Collection<Term> valores = ((MapTerm)action.getTerm(2)).values(); //Valores recibidas

                Object[] aux = valores.toArray(); //Array de objetos con los valores recibidos
                String[] values = new String[aux.length]; //Array de strings donde se van a almacenar los valores recibidos
                for(int i=0;i<aux.length;i++)
                {
                    values[i] = aux[i].toString(); //Se guarda cada uno de los valores
                }

                InfoLibro info = new InfoLibro(values); //Clase para almacenar la información del libro
            }
            else
            {
                //En el caso de que se intente ejecutar una acción no contemplada
                return false; 
            }

        }
        catch (Exception e)
        {
            e.printStackTrace(); //En el caso de fallo
        }

        updatePercepts(); //Actualización de la percepción del ambiente para los agentes

        try {
            Thread.sleep(200); //Dormir 200 ms
        } catch (Exception e) {}
        informAgsEnvironmentChanged(); //Informar a los agentes del cambio del environment
        
        //LocalMasClass.getNumberAgents();
        return true; // the action was executed with success
        
    }

    void updatePercepts()
    {
        //Función para actualizar la percepción de todos los agentes
        clearPercepts(); //Función que limpia la lista de percepciones de manera global
        
        Location cajeroLoc = model.getAgPos(0); //Actualizar el conocimiento de la posición del agente 0

        Literal poscajero = Literal.parseLiteral("pos(cajero," + cajeroLoc.x + "," + cajeroLoc.y + ")"); //Predicado de la posición del cajero

        //Percepciones que son fijas
        Location cajaLoc = new Location(pos_x_caja, pos_y_caja); //Localización de la caja


        addPercept(poscajero); //Se añade la percepción de la posición del cajero

        
        if(model.hasObject(LIBRO, cajaLoc))
        {
            logger.info("Libro presente en x = "+cajaLoc.x);
            logger.info("Libro presente en y = "+cajaLoc.y);
            addPercept(libro_depositado_caja); //Se añade la percepción de que hay un libro depositado sobre la caja
        }
        

        
        /*/
        MAS2JProject class_aux = new MAS2JProject();
        List<AgentParameters> lista_agentes = class_aux.getAgents();
        
        logger.info("Longitud de la lista : "+Integer.toString(lista_agentes.size()));
        */
    }


    class MarsModel extends GridWorldModel{
        //Clase que hereda de la clase que sirve para un mundo definido por un grid
        //Sirve para modelar el comportamiento dentro del environment
        private MarsModel()
        {
            //Inicializador
            super(GSize, GSize, 2); 
            /*
             * Los parámetros de entrada del constructor de la clase de la que hereda, GridWorldModel, son :
             * w : Ancho del mundo
             * h : Altura del mundo
             * nbAgs : ¿Número de agentes?
             */

            //Se inicializa la posición de los agentes
            try{
                setAgPos(0,0,0); //Agente cajero
                setAgPos(1,3,3); //Agente cliente
                /*
                 * Función de la clase GridWordModel, que sirve para colocar a un agente en una posición y tiene los siguientes parámetros
                 * ag : Número de agente
                 * x : Posición x
                 * y : Posición y
                 */
            }
            catch (Exception e)
            {
                e.printStackTrace(); //En caso de fallo
            }

            //Inicialización de la posición de los objetos
            //add(CAJA, pos_x_caja, pos_y_caja); //Inicialización de un objeto de tipo CAJA sobre la posición 1,1
            //super.add(LIBRO, 8,8);
            add(CAJA, pos_x_caja, pos_y_caja); //Inicialización de un objeto de tipo CAJA sobre la posición 1,1
            add(CAJON, pos_x_cajon, pos_y_cajon); //Inicialización de un objeto de tipo CAJON sobre la posición 0,0

        }

        /*
         * Lista de acciones internas : 
         * Colocar un libro
         */

        void colocarlibro(int x, int y, String tipo_objeto, InfoLibro Libro)
        {
            /*
             * Función para colocar un libro sobre una posición dada, sobre un tipo de objeto dado
             */
            Location pos_col = new Location(x,y); //Posición donde se va a colocar el libro
            if(tipo_objeto == "cajon_dev")
            {
                //Si se coloca sobre el cajón de devoluciones
                //libros_cajon = append(libros_cajon,Libro);  
                if(num_libros_cajon == 0)
                {
                    libros_cajon = append(libros_cajon, Libro);
                    num_libros_cajon++;
                    logger.info(Integer.toString(libros_cajon.length));
                }                   
            }
            else
            {
                //Otro caso
                add(LIBRO, pos_col.x, pos_col.y); //Se añade el libro sobre la posición x,y 
            }
            logger.info("Libro colocado");
            logger.info("x : "+Integer.toString(x));
            logger.info("y : "+Integer.toString(y));
            
        }

    }

    class MarsView extends GridWorldView{
        //Clase para realizar los dibujos sobre el grid. Dicha clase es para la representación gráfica
        public MarsView(MarsModel model)
        {
            //Inicializador de la clase
            super(model, "Mars Wordls", 600); 
            /*
             * Inicialización de la clase de la que hereda
             * Recibe como parámetros de entrada lo siguiente : 
             * GridWorldModel : modelo del mundo --> Grid del mundo con el número de agentes, en "MarsModel está inicializado"
             * title : Título del modelo, de la ventana gráfica
             * windowSize : Tamaño de la ventana gráfica
             */
            defaultFont = new Font("Arial", Font.BOLD, 18); //Fuente con la que se pondrán los textos
            setVisible(true); //Activar la visibilidad de la ventana de la gráfica
            repaint(); //Actualización de todo el frame
        }

        @Override
        public void draw(Graphics g, int x, int y, int object) 
        {
            //Método para dibujar un objeto desconocido. Se puede sobreescribir su funcionamiento, tal y como se hace aquí
            switch(object)
            {
                case TestEnv.CAJA:
                    //En el caso de que se tenga que dibujar la caja
                    drawCaja(g,x,y);
                    break;
                case TestEnv.LIBRO:
                    //En el caso de que se tenga que dibujar un libro
                    drawLibro(g,x,y);
                    break;
                case TestEnv.CAJON:
                    drawCajon(g,x,y);
                    break;
            }
        }

        @Override
        public void drawAgent(Graphics g, int x, int y, Color c, int id)
        {
            //Método para dibujar un agente
            String label = new String(); //Nombre que aparecerá sobre el agente
            c = Color.red;
            if(id == 0)
            {
                //Para el caso de un cajero
                c = Color.red; //Color rojo
                label += "caj";
                super.drawAgent(g, x, y, c ,id);
                g.setColor(Color.white);
                super.drawString(g, x, y, defaultFont, label);
            }
            else if(id == 1)
            {
                //Para el caso de un cliente
                c = Color.blue; //Color rojo
                label += "cliente";
                super.drawAgent(g, x, y, c ,id);
                g.setColor(Color.white);
                super.drawString(g, x, y, defaultFont, label);
            }
        }

        public void drawCaja(Graphics g, int x, int y)
        {
            //Función para dibujar la caja
            g.setColor(Color.BLACK);
            super.drawObstacle(g, x, y);
            g.setColor(Color.WHITE);
            drawString(g,x,y,defaultFont, "Caja"); //Se dibuja la cadena sobre la posición de la caja
            //repaint();
        }

        public void drawCajon(Graphics g, int x, int y)
        {
            //Función para dibujar la caja
            g.setColor(Color.BLACK);
            super.drawObstacle(g, x, y);
            g.setColor(Color.MAGENTA);
            drawString(g,x,y,defaultFont, "Cajon"); //Se dibuja la cadena sobre la posición de la caja
            //repaint();
        }

        public void drawLibro(Graphics g, int x, int y)
        {
            //Función para dibujar un libro
            g.setColor(Color.BLUE);
            super.drawObstacle(g, x, y);
            g.setColor(Color.WHITE);
            drawString(g,x,y,defaultFont, "Libro"); //Se dibuja la cadena sobre la posición del lirbo
        }

    }

    /** Called before the end of MAS execution */

    @Override

    public void stop() {

        super.stop();

    }

}


