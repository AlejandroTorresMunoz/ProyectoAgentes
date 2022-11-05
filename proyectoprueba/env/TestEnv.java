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


    //Array con los libros en el cajon de devoluciones
    public int num_libros_cajon = 0;
    public static InfoLibro[] libros_cajon = {};

    //ID's de los objetos --> IMPORTANTE : Por lo que parece, los objetos sólo pueden tener una ID que sea MULTIPLO DE 2
    public static final int CAJA = 16; //Código del objeto de la caja
    public static final int LIBRO = 32; //Código del objeto LIBRO
    public static final int CAJON = 64; //Código del objeto de la caja

    //Posición de los objetos
    public static final int pos_x_caja = 1; //Posición de la caja, en x
    public static final int pos_y_caja = 1; //Posición de la caja, en y
    public static Location lcaja = new Location(pos_x_caja, pos_y_caja); //Location de la caja
    //Posición de la cola de la caja
    public static final int pos_x_caja_delante = 1; //Posición de la caja, en x
    public static final int pos_y_caja_delante = 2; //Posición de la caja, en y
    public static Location lcaja_delante = new Location(pos_x_caja_delante, pos_y_caja_delante); //Location de la caja
    //Posición del cajón de devoluciones
    public static final int pos_x_cajon = 0; //Posición de la caja, en x
    public static final int pos_y_cajon = 0; //Posición de la caja, en y
    public static Location lcajon = new Location(pos_x_cajon, pos_y_cajon);

    //Definición de acciones de los agentes
    public static final Term colocar_libro = Literal.parseLiteral("colocar_libro(x,y,tipo_objeto)"); //Acción para colocar un libro en una posición
    public static final Term tomar_libro   = Literal.parseLiteral("tomar_libro(x,y,tipo_objeto)"); //Acción para tomar un libro de una posición

    //Definición de predicados
    public static final Literal libro_depositado_caja = Literal.parseLiteral("libro_depositado(caja)"); //Lirbo depositado sobre caja
    public static final Literal libro_tomado_caja = Literal.parseLiteral("libro_tomado(caja)"); //Libro tomado de la caja
    public static final Literal ad = Literal.parseLiteral("at(cliente,ID_ASISTENTE)"); //Percepcion de que el cliente se encuentra en la posición cercana al asistente de la zona
    public static final Literal cj = Literal.parseLiteral("at(cliente,caja)"); //Percepción de que el cliente se encuentra en la posición de la caja
    public static final Literal libro_devuelto = Literal.parseLiteral("libro_devuelto"); //Libro devuelto con éxito 


    static Logger logger = Logger.getLogger(TestEnv.class.getName());


    TestModel model; // the model of the grid

    @Override
    public void init(String[] args) {
        model = new TestModel(); //Clase donde se modelan las acciones
        TestView view  = new TestView(model);
        model.setView(view);
        updatePercepts();
        //LocalMasClass = new BaseLocal(TestEnv.class.getName());
    }




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
                String tipo_objeto = (String)((StringTerm)action.getTerm(2)).getString(); //El tercer término es el tipo de objeto donde se deposita el libro
                java.util.Set<Term> claves = ((MapTerm)action.getTerm(3)).keys(); //Claves recibidas
                java.util.Collection<Term> valores = ((MapTerm)action.getTerm(3)).values(); //Valores recibidas

                Object[] aux = valores.toArray(); //Array de objetos con los valores recibidos
                String[] values = new String[aux.length]; //Array de strings donde se van a almacenar los valores recibidos
                for(int i=0;i<aux.length;i++)
                {
                    values[i] = aux[i].toString(); //Se guarda cada uno de los valores
                }

                InfoLibro info = new InfoLibro(values); //Clase para almacenar la información del libro

                model.tomar_libro(x, y, tipo_objeto, info); //Se toma el libro de la posición
                addPercept(libro_depositado_caja); //Se añade la percepción de que se ha tomado el libro de la caja
            
                
            }
            else if (action.getFunctor().equals("move_towards")) 
            {

                String l = action.getTerm(0).toString(); //Se almacena el objeto hacia el que se desea mover 
                Location dest = null;
                if(l.equals(new String("caja")))
                {
                    //Se mueve hacia la posición de la caja
                    dest = lcaja_delante;
                }
                else if(l.equals(new String("[asistente]")))
                {
                    //Se mueve hacia la posición del asistente
                    dest = model.getAgPos(2);
                    dest.x = dest.x-1;
                }
                model.moveTowards(dest);

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
        Location asistenteLoc = model.getAgPos(2); //Actualizar el conocimiento de la posición del agente 2

        Literal poscajero = Literal.parseLiteral("pos(cajero," + cajeroLoc.x + "," + cajeroLoc.y + ")"); //Predicado de la posición del cajero
        addPercept(poscajero); //Se añade la percepción de la posición del cajero
        //Percepciones que son fijas
        Location cajaLoc = new Location(pos_x_caja, pos_y_caja); //Localización de la caja


        Location lcliente = model.getAgPos(1); //Se añade la percepción del cliente 

        if(model.hasObject(LIBRO, cajaLoc))
        {
            //En el caso de que se coloque un libro en la posición de la caja
            
            addPercept(libro_depositado_caja); //Se añade la percepción de que hay un libro depositado sobre la caja
        }

        // Se añade la percepción al cliente de la posición de la caja
        if (lcliente.equals(lcaja_delante)) {
            //En el caso de que la posición del cliente sea la de la caja
            addPercept("cliente", cj); //Se le añade la percepción de de que se encuentra en la caja
        }

        if(lcliente.distance(asistenteLoc) < 2)
        {
            //Si el cliente se encuentra a menos de 1 del asistente
            addPercept("cliente", ad);
        }

    }


    /** Called before the end of MAS execution */

    @Override

    public void stop() {

        super.stop();

    }

}


