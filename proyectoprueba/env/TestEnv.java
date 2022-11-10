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

import javax.sound.sampled.FloatControl;

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

    public static class InfoLibro
    {
        //Clase para almacenar la información de un libro
        String autor;
        String titulo;
        int numestanteria;
        float precio;
        public  InfoLibro(HashMap<String,String> dict)
        {
            //Constructor de la clase
            titulo = dict.get("titulo");
            autor = dict.get("autor");
            
            try
            {
                numestanteria = Integer.parseInt(dict.get("numestanteria"));
            }
            catch (NumberFormatException e)
            {
                numestanteria = -1; //En caso de que el valor sea nulo, se le asigna un valor inexistente de estantería
            }
            
            try
            {
                precio = Float.parseFloat(dict.get("precio"));
            }
            catch (NumberFormatException e)
            {
                precio = -1;
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
    //Posición delante de la estantería 0
    public static final int pos_x_est_0_delante = 8;
    public static final int pos_y_est_0_delante = 1;
    public static Location est0_delante = new Location(pos_x_est_0_delante, pos_y_est_0_delante);
    //Posición delante de la estantería 1
    public static final int pos_x_est_1_delante = 8;
    public static final int pos_y_est_1_delante = 2;
    public static Location est1_delante = new Location(pos_x_est_1_delante, pos_y_est_1_delante);
    //Posición del cajón de devoluciones
    public static final int pos_x_cajon = 0; //Posición del cajón, en x
    public static final int pos_y_cajon = 0; //Posición del cajón, en y
    public static Location lcajon = new Location(pos_x_cajon, pos_y_cajon);


    //Definición de predicados empleados
    public static final Literal libro_depositado_caja = Literal.parseLiteral("libro_depositado(caja)"); //Lirbo depositado sobre caja
    public static final Literal libro_tomado_caja = Literal.parseLiteral("libro_tomado(caja)"); //Libro tomado de la caja
    public static final Literal libro_devuelto = Literal.parseLiteral("libro_devuelto"); //Libro devuelto con éxito 
    public static final Literal libro_no_existente_area = Literal.parseLiteral("libro_no_existente_area"); //Libro existente en la zona
    public static final Literal libro_existente_estanteria = Literal.parseLiteral("libro_existente_estanteria");//(posx,posy)");
    public static final Literal dinero_suficiente = Literal.parseLiteral("dinero_suficiente"); //Dinero suficinete
	public static final Literal dinero_no_suficiente = Literal.parseLiteral("dinero_no_suficiente"); //Dinero Insuficiente


    static Logger logger = Logger.getLogger(TestEnv.class.getName()); //Clase para mostrar mensajes por pantalla


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
                String tipo_objeto = (String)((StringTerm)action.getTerm(0)).getString(); //El tercer término es el tipo de objeto donde se deposita el libro
                java.util.Collection<Term> valores = ((MapTerm)action.getTerm(1)).values(); //Valores recibidas
                java.util.Collection<Term> claves = ((MapTerm)action.getTerm(1)).keys(); //Claves recibidas

                HashMap<String,String> mapa = new HashMap<String,String>();

                for(int i=0;i<valores.size();i++)
                {
                    mapa.put(claves.toArray()[i].toString(),valores.toArray()[i].toString());
                }

                InfoLibro info = new InfoLibro(mapa); //Clase para almacenar la información del libro

               
                model.colocarlibro(tipo_objeto, info); //Se ejecuta la acción
            }
            else if(action.getFunctor().equals("tomar_libro"))
            {
                //En el caso de que se trate de la acción de tomar un libro
                String tipo_objeto = (String)((StringTerm)action.getTerm(0)).getString(); //El tercer término es el tipo de objeto donde se deposita el libro
                java.util.Set<Term> claves = ((MapTerm)action.getTerm(1)).keys(); //Claves recibidas
                java.util.Collection<Term> valores = ((MapTerm)action.getTerm(1)).values(); //Valores recibidas

                HashMap<String,String> mapa = new HashMap<String,String>();

                for(int i=0;i<valores.size();i++)
                {
                    mapa.put(claves.toArray()[i].toString(),valores.toArray()[i].toString());
                }

                InfoLibro info = new InfoLibro(mapa); //Clase para almacenar la información del libro

                model.tomar_libro(tipo_objeto, info); //Se toma el libro de la posición
            
                
            }
            else if(action.getFunctor().equals("movimiento_hacia"))
            {
                //Función para moverse hacia una posición dados x e y como términos
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                Location dest = new Location(x,y);
                model.movimiento_hacia(dest);
            }
            else if(action.getFunctor().equals("comprobar_dinero"))
            {
                //Función para comprobar el dinero del cliente
                float dinero_cliente = (float)((NumberTerm)action.getTerm(0)).solve();
                java.util.Set<Term> claves = ((MapTerm)action.getTerm(1)).keys(); //Claves recibidas
                java.util.Collection<Term> valores = ((MapTerm)action.getTerm(1)).values(); //Valores recibidas
				HashMap<String,String> mapa = new HashMap<String,String>(); //Mapa para almacenar los valores de InfoLibro
                String agente_a_responder = action.getTerm(2).toString(); //Agente al que se le debe responder
								
				for(int i=0;i<valores.size();i++)
                {
                    mapa.put(claves.toArray()[i].toString(),valores.toArray()[i].toString());
                }
				
				InfoLibro info = new InfoLibro(mapa); //Clase para almacenar la información del libro
				
                
                float precio = info.precio; //Se extrae el precio del libro

                //Se define el concepto del Info_Libro
                MapTermImpl INFO_MAP = new MapTermImpl(); //Mapa del concepto Info_Libro que se va a devolver

                //Se le introduce el término del autor
                Atom clave_autor = new Atom("autor");
                VarTerm valor_autor = new VarTerm(info.autor);
                INFO_MAP.put(clave_autor,valor_autor);                

                //Se introduce el término del título
                Atom clave_titulo = new Atom("titulo");
                VarTerm valor_titulo = new VarTerm(info.titulo);
                INFO_MAP.put(clave_titulo,valor_titulo);
                
                //Se intoruce el término del número de estantería
                Atom clave_estanteria = new Atom("numestanteria");
                VarTerm valor_estanteria = new VarTerm(Integer.toString(info.numestanteria));
                INFO_MAP.put(clave_estanteria,valor_estanteria);  

                //Se introduce el término del precio del libro
                Atom clave_precio = new Atom("precio");
                VarTerm valor_precio = new VarTerm(Float.toString(info.precio));
                INFO_MAP.put(clave_precio,valor_precio);
                
                if (dinero_cliente >= precio)
				{
                    logger.info("SE POSEE DINERO SUFICIENTE");
                    dinero_suficiente.clearAnnots();
                    dinero_suficiente.addTerm(INFO_MAP);
                    NumberTermImpl dinero_restante = new NumberTermImpl(dinero_cliente-precio);
                    dinero_suficiente.addTerm(dinero_restante);
					addPercept("cliente",dinero_suficiente);
				}
				else
				{
                    logger.info("NO SE POSEE DINERO SUFICIENTE");
                    dinero_no_suficiente.clearAnnots();
                    dinero_no_suficiente.addTerm(INFO_MAP);
                    NumberTermImpl dinero_restante = new NumberTermImpl(dinero_cliente);
                    dinero_no_suficiente.addTerm(dinero_restante);
					addPercept("cliente",dinero_no_suficiente);	
				}
                
            }
            else if(action.getFunctor().equals("consultar_estanteria"))
            {
                //Función para consultar la existencia de un libro dentro de las estanterías
                java.util.Set<Term> claves = ((MapTerm)action.getTerm(0)).keys(); //Claves recibidas
                java.util.Collection<Term> valores = ((MapTerm)action.getTerm(0)).values(); //Valores recibidas
                String agente_a_responder = action.getTerm(1).toString(); //Agente al que se le debe responder

                HashMap<String,String> mapa = new HashMap<String,String>(); //Mapa para almacenar los valores de InfoLibro

                for(int i=0;i<valores.size();i++)
                {
                    mapa.put(claves.toArray()[i].toString(),valores.toArray()[i].toString());
                }


                InfoLibro info = new InfoLibro(mapa); //Clase para almacenar la información del libro
                TestModel.ConsultaEstanteria resu_consulta = model.consultar_estanteria(info); //Se ejecuta la acción de consultar si dicho libro existe en las librerías
                TestModel.Estanteria est = resu_consulta.est_consultada;
                info = resu_consulta.libro_consultado;

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
                    StringTermImpl clave_estanteria = new StringTermImpl("numestanteria");
                    StringTermImpl valor_estanteria = new StringTermImpl(Integer.toString(info.numestanteria));
                    INFO_MAP.put(clave_estanteria,valor_estanteria);

                    //Se introduce el término de precio
                    Atom clave_precio = new Atom("precio");
                    VarTerm valor_precio = new VarTerm(Float.toString(info.precio));
                    INFO_MAP.put(clave_precio,valor_precio);

                    libro_no_existente_area.clearAnnots(); //Se eliminan los posibles términos que pudiera contener la creencia
                    
                    //Se añade el concepto de InfoLibro a la creencia
                    libro_no_existente_area.addTerm(INFO_MAP);
                    //Se crea el término del agente al que responder
                    StringTermImpl Ag = new StringTermImpl(agente_a_responder);
                    //Se añade el concepto del agente al que responder a la creencia
                    libro_no_existente_area.addTerm(Ag);
                    //Se añade la creencia
                    addPercept("asistente",libro_no_existente_area);
                }
                else
                {
                    logger.info("Se ha encontrado el libro");

                    MapTermImpl INFO_MAP = new MapTermImpl(); //Mapa del concepto Info_Libro que se va a devolver
                    MapTermImpl ESTANTERIA_MAP = new MapTermImpl(); //Mapa del concepto Estanteria que se va a devolver


                    //Se introduce el término del autor
                    Atom clave_autor = new Atom("autor");
                    VarTerm valor_autor = new VarTerm(info.autor);
                    INFO_MAP.put(clave_autor,valor_autor);                

                    //Se introduce el término del título
                    Atom clave_titulo = new Atom("titulo");
                    VarTerm valor_titulo = new VarTerm(info.titulo);
                    INFO_MAP.put(clave_titulo,valor_titulo);
                    
                    //Se introduce el término del número de estantería
                    Atom clave_estanteria = new Atom("numestanteria");
                    VarTerm valor_estanteria = new VarTerm(Integer.toString(est.id_));
                    INFO_MAP.put(clave_estanteria,valor_estanteria);  
                    
                    //Se introduce el término del precio del libro
                    Atom clave_precio = new Atom("precio");
                    VarTerm valor_precio = new VarTerm(Float.toString(info.precio));
                    INFO_MAP.put(clave_precio,valor_precio);
                    
                    //Se introduce el término del id de la estantería
                    Atom clave_id = new Atom("id");
                    NumberTermImpl valor_id = new NumberTermImpl(est.id_);
                    ESTANTERIA_MAP.put(clave_id,valor_id);

                    libro_existente_estanteria.clearAnnots(); //Se eliminan los posibles términos que pudiera contener la creencia

                    libro_existente_estanteria.addTerm(INFO_MAP); //Se añade el concepto del Info_Libro

                    libro_existente_estanteria.addTerm(ESTANTERIA_MAP); //Se añade el concepto de la Estanteria
                    //Se añade el término del agente al que responder
                    StringTermImpl Ag = new StringTermImpl(agente_a_responder);
                    libro_existente_estanteria.addTerm(Ag); //Se añade el concepto del agente al que responder

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
        
        return true; // the action was executed with success
        
    }

    void updatePercepts()
    {
        //Función para actualizar la percepción de todos los agentes
        clearPercepts(); //Función que limpia la lista de percepciones de manera global

        Location cajaLoc = new Location(pos_x_caja, pos_y_caja); //Localización de la caja
        
        //Se actualiza la posición del cliente
        Location lcliente = model.getAgPos(1); //Se añade la percepción del cliente 
        Literal position_client = Literal.parseLiteral("posicion");
        
        /*
         * A continuación, se procede a guardar cada una de las creencias que poseía el agente tipo cliente, excepto las de posición.
         * Tras ello, se eliminan todas sus creencias
         * Se cargan todas las creencias almacenadas, excepto las de posición
         * Se añade la crenecia de la posición actual del agente.
         * 
         * Nota : Esto se ha debido de realizar así para que el agente no acumulara las creencias de las distintas posiciones que les eran lanzadas desde el environment.
         * De esta manera, se elimina la creencia de que el agente cliente posee múltiples posiciones en un mismo instante de ejecución.
         */
        //Se comprueban cada una de las percepciones
        Literal[] creencias_cliente = {}; //Se guardan todas las posibles percepciones que hayan podido ser lanzadas desde el environment
        if(containsPercept​("cliente", libro_depositado_caja))
        {
            creencias_cliente = TestModel.append(creencias_cliente,libro_depositado_caja);
        }
        if(containsPercept​("cliente", libro_tomado_caja))
        {
            creencias_cliente = TestModel.append(creencias_cliente,libro_tomado_caja);
        }
        if(containsPercept​("cliente", libro_devuelto))
        {
            creencias_cliente = TestModel.append(creencias_cliente,libro_devuelto);
        }
        if(containsPercept​("cliente", libro_no_existente_area))
        {
            creencias_cliente = TestModel.append(creencias_cliente,libro_no_existente_area);
        }
        if(containsPercept​("cliente", libro_existente_estanteria))
        {
            creencias_cliente = TestModel.append(creencias_cliente,libro_existente_estanteria);
        }
        if(containsPercept​("cliente", dinero_suficiente))
        {
            creencias_cliente = TestModel.append(creencias_cliente,dinero_suficiente);
        }
        if(containsPercept​("cliente", dinero_no_suficiente))
        {
            creencias_cliente = TestModel.append(creencias_cliente,dinero_no_suficiente);
        }

        clearPercepts("cliente");
        for(int num_creencias = 0; num_creencias<creencias_cliente.length;num_creencias++)
        {
            addPercept("cliente",creencias_cliente[num_creencias]);
        }



        NumberTermImpl val_x = new NumberTermImpl(lcliente.x); //Val x de la posición del cliente
        NumberTermImpl val_y = new NumberTermImpl(lcliente.y); //Val y de la posición del cliente
        position_client.addTerm(val_x);
        position_client.addTerm(val_y);
        addPercept("cliente",position_client);

        //Se actualiza la posición de la caja
        Literal pos_caja_del = Literal.parseLiteral("pos(caja_del,"+lcaja_delante.x+","+lcaja_delante.y+")");
        addPercept("cliente",pos_caja_del);
        //Se actualiza la posición del asistente
        Location position_assistent =   model.getAgPos(2);
        Literal pos_assistent = Literal.parseLiteral("pos(asistente,"+position_assistent.x+","+position_assistent.y+")");
        addPercept("cliente",pos_assistent);
        //Se actualiza la posición de la estantería 0
        Literal pos_est_0 = Literal.parseLiteral("pos(0,"+est0_delante.x+","+est0_delante.y+")");
        addPercept("cliente",pos_est_0);
        //Se actualiza la posición de la estantería 1
        Literal pos_est_1 = Literal.parseLiteral("pos(1,"+est1_delante.x+","+est1_delante.y+")");
        addPercept("cliente",pos_est_1);


        

        if(model.hasObject(LIBRO, cajaLoc))
        {
            //En el caso de que se coloque un libro en la posición de la caja
            addPercept(libro_depositado_caja); //Se añade la percepción de que hay un libro depositado sobre la caja
        }
        else
        {
            removePercept(libro_depositado_caja);
        }



    }



    /** Called before the end of MAS execution */

    @Override

    public void stop() {

        super.stop();

    }

}


