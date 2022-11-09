package env;
// Environment code for project proyectoprueba.mas2j



import jason.asSyntax.*;
import jason.environment.Environment; //Clase para el environment
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView; //Clase para el mundo
import jason.environment.grid.Location;
import jason.asSemantics.*;
import jason.mas2j.MAS2JProject;
import jason.mas2j.AgentParameters;
import jason.infra.local.BaseLocalMAS;
import jason.stdlib.term2string;

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
    /* 
    public static class InfoLibro
    {
        //Clase para almacenar la información de un libro
        String autor;
        String titulo;
        int NumEstanteria;
        public  InfoLibro(String[] valueStrings)
        {
            //Constructor de la clase
            titulo = valueStrings[0];
            autor = valueStrings[1];
            try
            {
                NumEstanteria = Integer.parseInt(valueStrings[2]);
            }
            catch (NumberFormatException e)
            {
                NumEstanteria = -1; //En caso de que el valor sea nulo, se le asigna un valor inexistente de estantería
            }
            
        }
    }
    */

    public static class InfoLibro
    {
        //Clase para almacenar la información de un libro
        String autor;
        String titulo;
        int NumEstanteria;
        public  InfoLibro(HashMap<String,String> dict)
        {
            //Constructor de la clase
            titulo = dict.get("titulo");
            autor = dict.get("autor");
            try
            {
                NumEstanteria = Integer.parseInt(dict.get("NumEstanteria"));
            }
            catch (NumberFormatException e)
            {
                NumEstanteria = -1; //En caso de que el valor sea nulo, se le asigna un valor inexistente de estantería
            }
            
        } 
    }


    //Array con los libros en el cajon de devoluciones
    public int num_libros_cajon = 0;
    public static InfoLibro[] libros_cajon = {};

    //ID's de los objetos --> IMPORTANTE : Por lo que parece, los objetos sólo pueden tener una ID que sea MULTIPLO DE 2
    public static final int CAJA = 16; //Código del objeto de la caja
    public static final int LIBRO = 32; //Código del objeto LIBRO
    public static final int CAJON = 64; //Código del objeto del cajón de devoluciones
    public static final int ESTANTERIA = 128; //Código del objeto estantería

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
    public static final Literal ae = Literal.parseLiteral("at(cliente,ID_EST)"); //Percepción de que el cliente se encuentra en la posición de la estantería recibida
    public static final Literal libro_devuelto = Literal.parseLiteral("libro_devuelto"); //Libro devuelto con éxito 
    public static final Literal libro_existente_area = Literal.parseLiteral("libro_existente_area"); //Libro existente en la zona
    public static final Literal libro_no_existente_area = Literal.parseLiteral("libro_no_existente_area"); //Libro existente en la zona
    public static final Literal libro_existente_estanteria = Literal.parseLiteral("libro_existente_estanteria");//(posx,posy)");

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
                java.util.Collection<Term> claves = ((MapTerm)action.getTerm(3)).keys(); //Claves recibidas

                HashMap<String,String> mapa = new HashMap<String,String>();

                for(int i=0;i<valores.size();i++)
                {
                    mapa.put(claves.toArray()[i].toString(),valores.toArray()[i].toString());
                }

                InfoLibro info = new InfoLibro(mapa); //Clase para almacenar la información del libro

                logger.info("info.autor : "+info.autor);
                logger.info("info.titulo : "+info.titulo);
               
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

                HashMap<String,String> mapa = new HashMap<String,String>();

                for(int i=0;i<valores.size();i++)
                {
                    mapa.put(claves.toArray()[i].toString(),valores.toArray()[i].toString());
                }

                InfoLibro info = new InfoLibro(mapa); //Clase para almacenar la información del libro

                model.tomar_libro(x, y, tipo_objeto, info); //Se toma el libro de la posición
                addPercept(libro_depositado_caja); //Se añade la percepción de que se ha tomado el libro de la caja
            
                
            }
            else if (action.getFunctor().equals("move_towards")) 
            {

                String l = action.getTerm(0).toString(); //Se almacena el objeto hacia el que se desea mover 
                Location dest = null;
                logger.info("move_towards: valor de l : "+l);
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
                else if(l.equals(new String("1")))
                {
                    logger.info("Se ha recibido la orden movimiento hacia la estantería.");
                }
                model.moveTowards(dest);

            }
            else if(action.getFunctor().equals("movimiento_hacia"))
            {
                //Función para moverse hacia una posición dados x e y como términos
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                Location dest = new Location(x,y);
                model.movimiento_hacia(dest);
            }
            else if(action.getFunctor().equals("consultar_estanteria"))
            {
                //Función para consultar la existencia de un libro dentro de las estanterías
                java.util.Set<Term> claves = ((MapTerm)action.getTerm(0)).keys(); //Claves recibidas
                java.util.Collection<Term> valores = ((MapTerm)action.getTerm(0)).values(); //Valores recibidas
                String agente_a_responder = action.getTerm(1).toString(); //Agente al que se le debe responder
                logger.info("El agente al que se debe responder es : "+agente_a_responder);

                HashMap<String,String> mapa = new HashMap<String,String>(); //Mapa para almacenar los valores de InfoLibro

                for(int i=0;i<valores.size();i++)
                {
                    mapa.put(claves.toArray()[i].toString(),valores.toArray()[i].toString());
                }


                InfoLibro info = new InfoLibro(mapa); //Clase para almacenar la información del libro
                TestModel.Estanteria est =  model.consultar_estanteria(info); //Se ejecuta la acción de consultar si dicho libro existe en las librerías
                if(est==null)
                {
                    logger.info("No se ha encontrado el libro");
                    MapTermImpl INFO_MAP = new MapTermImpl(); //Mapa del concepto Info_Libro que se va a devolver
                    
                    //Se introduce el término del autor
                    StringTermImpl clave_autor = new StringTermImpl("autor");
                    StringTermImpl valor_autor = new StringTermImpl(info.autor);
                    INFO_MAP.put(clave_autor,valor_autor);
                    //Se introduce el término del título
                    StringTermImpl clave_titulo = new StringTermImpl("titulo");
                    StringTermImpl valor_titulo = new StringTermImpl(info.titulo);
                    INFO_MAP.put(clave_titulo,valor_titulo);
                    //Se intoruce el término del número de estantería
                    StringTermImpl clave_estanteria = new StringTermImpl("NumEstanteria");
                    StringTermImpl valor_estanteria = new StringTermImpl(Integer.toString(info.NumEstanteria));
                    INFO_MAP.put(clave_estanteria,valor_estanteria);

                    libro_no_existente_area.clearAnnots(); //Se eliminan los posibles términos que pudiera contener la creencia
                    
                    //Se añade el término de InfoLibro a la creencia
                    libro_no_existente_area.addTerm(INFO_MAP);
                    //Se añade el término del agente al que responder
                    StringTermImpl Ag = new StringTermImpl(agente_a_responder);
                    libro_no_existente_area.addTerm(Ag);
                    //Se añade la creencia
                    addPercept("asistente",libro_no_existente_area);
                    //libro_no_existente_area.delTerm(0);
                }
                else
                {
                    logger.info("Se ha encontrado el libro");

                    MapTermImpl INFO_MAP = new MapTermImpl(); //Mapa del concepto Info_Libro que se va a devolver
                    MapTermImpl ESTANTERIA_MAP = new MapTermImpl(); //Mapa del concepto Estanteria que se va a devolver

                    //Se define el concepto del Info_Libro
                    //Se introduce el término del autor
                    StringTermImpl clave_autor = new StringTermImpl("autor");
                    StringTermImpl valor_autor = new StringTermImpl(info.autor);
                    INFO_MAP.put(clave_autor,valor_autor);
                    //Se introduce el término del título
                    StringTermImpl clave_titulo = new StringTermImpl("titulo");
                    StringTermImpl valor_titulo = new StringTermImpl(info.titulo);
                    INFO_MAP.put(clave_titulo,valor_titulo);
                    //Se intoruce el término del número de estantería
                    StringTermImpl clave_estanteria = new StringTermImpl("NumEstanteria");
                    StringTermImpl valor_estanteria = new StringTermImpl(Integer.toString(info.NumEstanteria));
                    INFO_MAP.put(clave_estanteria,valor_estanteria);  
                    
                    //Se define el concepto de la Estanteria
                    //Se introduce el término del id
                    StringTermImpl clave_id = new StringTermImpl("id");
                    StringTermImpl valor_id = new StringTermImpl(Integer.toString(est.id_));
                    ESTANTERIA_MAP.put(clave_id,valor_id);

                    libro_existente_estanteria.clearAnnots(); //Se eliminan los posibles términos que pudiera contener la creencia

                    libro_existente_estanteria.addTerm(INFO_MAP); //Se añade el término del Info_Libro

                    libro_existente_estanteria.addTerm(ESTANTERIA_MAP); //Se añade el término de la Estanteria
                    //Se añade el término del agente al que responder
                    StringTermImpl Ag = new StringTermImpl(agente_a_responder);
                    libro_existente_estanteria.addTerm(Ag);

                    addPercept("asistente",libro_existente_estanteria);
                }
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


        //PRUEBA NUEVA
        //Se actualiza la posición del cliente
        Location lcliente = model.getAgPos(1); //Se añade la percepción del cliente 
        Literal position_client = Literal.parseLiteral("posicion");
        NumberTermImpl val_x = new NumberTermImpl(lcliente.x); //Val x de la posición del cliente
        NumberTermImpl val_y = new NumberTermImpl(lcliente.y); //Val y de la posición del cliente
        position_client.addTerm(val_x);
        position_client.addTerm(val_y);
        addPercept("cliente",position_client);

        //Se actualiza la posición de la caja
        Literal pos_caja = Literal.parseLiteral("pos(caja,"+lcaja_delante.x+","+lcaja_delante.y+")");
        addPercept("cliente",pos_caja);
        //Se actualiza la posición del asistente
        Location position_assistent =   model.getAgPos(2);
        Literal pos_assistent = Literal.parseLiteral("pos(asistente,"+position_assistent.x+","+position_assistent.y+")");
        addPercept("cliente",pos_assistent);

        //removePercept("cliente",poscliente);
        //addPercept("cliente",poscliente); //Se le añade la percepción al cliente 
        

        if(model.hasObject(LIBRO, cajaLoc))
        {
            //En el caso de que se coloque un libro en la posición de la caja
            addPercept(libro_depositado_caja); //Se añade la percepción de que hay un libro depositado sobre la caja
        }
        else
        {
            removePercept(libro_depositado_caja);
        }

        // Se añade la percepción al cliente de la posición de la caja
        if (lcliente.equals(lcaja_delante)) {
            //En el caso de que la posición del cliente sea la de la caja
            addPercept("cliente", cj); //Se le añade la percepción de de que se encuentra en la caja
        }
        else
        {
            removePercept("cliente", cj);
        }

        if(lcliente.distance(asistenteLoc) < 2)
        {
            //Si el cliente se encuentra a menos de 1 del asistente
            addPercept("cliente", ad);
        }
        else
        {
            removePercept("cliente", ad);
        }

        //logger.info("El valor de la creencia ad : "+Boolean.toString(containsPercept​("cliente",ad)));
        //logger.info("El valor de la creencia cj : "+Boolean.toString(containsPercept​("cliente",cj)));
    }



    /** Called before the end of MAS execution */

    @Override

    public void stop() {

        super.stop();

    }

}


