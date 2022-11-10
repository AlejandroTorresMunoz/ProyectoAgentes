package env;

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

public class TestModel extends GridWorldModel {
    //Sirve para modelar el comportamiento dentro del environment

    // the grid size
    public static final int GSize = 10; // Tamaño del grid que define el mundo

    //Clase para almacenar los términos del concepto Estanteria
    public class Estanteria
    {
        //Clase para almacenar la información de una estanteria
        public int id_; //ID de la estanteria
        public Location pos_estanteria_; //Position de la estanteria
        public TestEnv.InfoLibro[] libros = {}; //Array con los valores de los libros 
        private Estanteria(int id, int x, int y)
        {
            //Constructor de la clase
            id_ = id;
            pos_estanteria_ = new Location(x,y); 
        }
    }

    //Clase para almacenar los valores de la consulta a una estantería como resultado
    public class ConsultaEstanteria
    {
        //Clase para almacenar los valores de la consulta a una estantería
        public TestEnv.InfoLibro libro_consultado;
        public Estanteria est_consultada;
        private ConsultaEstanteria(TestEnv.InfoLibro lib, Estanteria est)
        {
            //Constructor de la clase
            libro_consultado = lib;
            est_consultada = est;
        }
    }

    public static Estanteria[] array_estanterias = {};


    
    

    public static <T> T[] append(T[] arr, T element) 
    {
        //Función para añadir elementos a un array de estructuras
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }



    public TestModel()
    {
        //Inicializador
        super(GSize, GSize, 3);  
        /*
             * Los parámetros de entrada del constructor de la clase de la que hereda, GridWorldModel, son :
             * w : Ancho del mundo
             * h : Altura del mundo
             * nbAgs : Número de agentes
        */

        //Se inicializa la posición de los agentes
        try{
            setAgPos(0,1,0); //Agente cajero

            setAgPos(1,3,3); //Agente cliente

            setAgPos(2,7,4); //Agente asistente
            //Se inicia el conocimiento del agente asistente sobre las estanterías

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
        add(TestEnv.CAJA, TestEnv.lcaja.x, TestEnv.lcaja.y); //Inicialización de un objeto de tipo CAJA sobre la posición 1,1
        add(TestEnv.CAJON, TestEnv.lcajon.x, TestEnv.lcajon.y); //Inicialización de un objeto de tipo CAJON sobre la posición 0,0
            
        //Se crean las estanterías del sistema
        //Estantería 0
        add(TestEnv.ESTANTERIA, 9, 1);
        Estanteria est0 = new Estanteria(0, 9, 1);
        //Se añaden los libros a la estantería 0
        HashMap<String,String> mapa_0_0 = new HashMap<String,String>();
        mapa_0_0.put("titulo","LA_METAMORFOSIS");
        mapa_0_0.put("autor","KAFKA");
        mapa_0_0.put("numestanteria","0");
        mapa_0_0.put("precio","20");
        est0 = add_libro_est(est0, mapa_0_0);
        HashMap<String,String> mapa_0_1 = new HashMap<String,String>();
        mapa_0_1.put("titulo","STONER");
        mapa_0_1.put("autor","JOHN_WILLIAMS");
        mapa_0_1.put("numestanteria","0");
        mapa_0_1.put("precio","25");
        est0 = add_libro_est(est0, mapa_0_1);

        array_estanterias = append(array_estanterias,est0);

        //Estantería 1
        add(TestEnv.ESTANTERIA, 9, 2);
        Estanteria est1 = new Estanteria(1, 9, 2);
        //Se añaden los libros a la estantería 1

        HashMap<String,String> mapa_1_0 = new HashMap<String,String>();
        mapa_1_0.put("titulo","1984");
        mapa_1_0.put("autor","GEORGE_ORWELL");
        mapa_1_0.put("numestanteria","1");
        mapa_1_0.put("precio","10");
        est1 = add_libro_est(est1, mapa_1_0);
        HashMap<String,String> mapa_1_1 = new HashMap<String,String>();
        mapa_1_1.put("titulo","REBELION_EN_LA_GRANJA");
        mapa_1_1.put("autor","GEORGE_ORWELL");
        mapa_1_1.put("numestanteria","1");
        mapa_1_1.put("precio","15");
        est1 = add_libro_est(est1, mapa_1_1);

        array_estanterias = append(array_estanterias,est1);

    }


    Estanteria add_libro_est(Estanteria est, HashMap<String,String> dict)
    {
        //Función para añadir un libro a una estantería
        TestEnv.InfoLibro libro = new TestEnv.InfoLibro(dict);
        est.libros = append(est.libros, libro);

        return est; 
    }


    void tomar_libro(String tipo_objeto,TestEnv.InfoLibro info)
    {
        /*
             * Función para tomar un libro de una posición dada
        */
        if (tipo_objeto.equals("caja"))
        {
            //En el caso de que se tome el libro de la caja
            Location pos_toma = new Location(TestEnv.lcaja.x,TestEnv.lcaja.y); //Posición de la caja en x e y
            remove(TestEnv.LIBRO, TestEnv.lcaja.x, TestEnv.lcaja.y); 
        }
        else if(tipo_objeto.equals("estanteria"))
        {
            //En el caso de que se tome el libro de una estanteria
            int id_est_toma = info.numestanteria; //Se obtiene la ID de la estanteria donde se encuentra el libro
            Estanteria est_to_manipulate; //Estantería sobre la que se va a trabajar
            est_to_manipulate = array_estanterias[id_est_toma]; //Se busca la estantería con la que se va a trabajar
                
            TestEnv.InfoLibro[] new_libros = {};//new TestEnv.InfoLibro[est_to_manipulate.libros.length-1]; //Nuevo array que se va a implementar sobre la estantería
                
            int index_to_remove = -1; //Índice dentro del array de libros de la estantería que se va a retirar
            for(int i=0;i<est_to_manipulate.libros.length;i++)
            {
                if(est_to_manipulate.libros[i].autor.equals(info.autor) && est_to_manipulate.libros[i].titulo.equals(info.titulo))
                {
                    //En el caso de que se encuentre el libro que se desea
                    index_to_remove = i;
                    break;
                }
            }

            //Finalmente, se modifica el array "new_libros" y se carga sobre la estantería
            for(int i=0;i<est_to_manipulate.libros.length;i++)
            {
                if(i==index_to_remove)
                {
                    //Se encuentra el libro que se va a tomar
                    continue;
                }
                else
                {   
                    //Para el resto de libros
                    new_libros = append(new_libros,est_to_manipulate.libros[i]);
                }
            }

            //Finalmente, se actualiza la lista de libros de la estantería correspondiente
            array_estanterias[id_est_toma].libros = new_libros;
            TestEnv.logger.info("El estado de la estantería "+Integer.toString(id_est_toma)+" es : ");
            for(int i=0;i<array_estanterias[id_est_toma].libros.length;i++)
            {
                TestEnv.logger.info("autor número "+Integer.toString(i)+" es : "+array_estanterias[id_est_toma].libros[i].autor);
                TestEnv.logger.info("título número "+Integer.toString(i)+" es : "+array_estanterias[id_est_toma].libros[i].titulo);
            }
  
        }

    }

    void colocarlibro(String tipo_objeto, TestEnv.InfoLibro Libro)
    {
        /*
             * Función para colocar un libro sobre una posición dada, sobre un tipo de objeto dado
        */

        if(tipo_objeto.equals("cajon_dev"))
        {
            //Si se coloca sobre el cajón de devoluciones
            TestEnv.libros_cajon = append(TestEnv.libros_cajon,Libro);   
            TestEnv.logger.info("El estado del cajon de devoluciones es el siguiente : ");
            for(int i=0;i<TestEnv.libros_cajon.length;i++)
            {
                TestEnv.logger.info("autor número "+Integer.toString(i)+" registrado : "+TestEnv.libros_cajon[i].autor);
                TestEnv.logger.info("título número "+Integer.toString(i)+" registrado : "+TestEnv.libros_cajon[i].titulo);
            }
        }
        else if(tipo_objeto.equals("caja"))
        {
            //Si se coloca sobre la caja
            Location pos_col = new Location(TestEnv.lcaja.x,TestEnv.lcaja.y); //Posición de la caja en x e y
            add(TestEnv.LIBRO, pos_col.x, pos_col.y); //Se añade el libro sobre la posición x,y 
        }
            
    }

    void moveTowards(Location dest) throws Exception {
        Location r1 = getAgPos(1); //Para el cliente
        Location r_last = r1; //Se obtiene la posición del agente previa al movimiento, para luego actualizar el grid en dicha
        if (r1.x < dest.x)
            r1.x++;
        else if (r1.x > dest.x)
            r1.x--;
        if (r1.y < dest.y)
            r1.y++;
        else if (r1.y > dest.y)
            r1.y--;
        setAgPos(1, r1);

        // Se repinta la posición de 
        if (view != null) {
            //view.update(r_last.x,r_last.y);
            view.update(); //Se repinta todo el frame
        }

    }

    void movimiento_hacia(Location dest) throws Exception
    {
        Location r1 = getAgPos(1); //Para el cliente
        Location r_last = r1; //Se obtiene la posición del agente previa al movimiento, para luego actualizar el grid en dicha
        if (r1.x < dest.x)
            r1.x++;
        else if (r1.x > dest.x)
            r1.x--;
        if (r1.y < dest.y)
            r1.y++;
        else if (r1.y > dest.y)
            r1.y--;
        setAgPos(1, r1);

        // Se repinta la posición de 
        if (view != null) {
                //view.update(r_last.x,r_last.y);
            view.update(); //Se repinta todo el frame
        }
     }


    ConsultaEstanteria consultar_estanteria(TestEnv.InfoLibro info)
    {
        //Función para consultar si un libro existe o no sobre las estanterías
        int num_estanterias = array_estanterias.length; //Número de estanterías

        boolean existe = false; //Sólo será true si se encuentra el título del libro
        ConsultaEstanteria request_value_to_return = new ConsultaEstanteria(null,null);
        Estanteria est_to_return;
        TestEnv.InfoLibro lib_to_return;
        for(int num_est=0;num_est<num_estanterias;num_est++)
        {
            //Se consulta cada una de las estanterías
            Estanteria est_consulta = array_estanterias[num_est]; //Se obtiene la estantería
            int num_libros = est_consulta.libros.length; //Se obtiene el número de libros almacenados en la estantería
            for(int num_libro=0;num_libro<num_libros;num_libro++)
            {
                //Se consulta cada uno de los libros de la estantería actual
                TestEnv.InfoLibro libro_consulta = est_consulta.libros[num_libro]; //Se obtiene el libro actual

                if(Objects.equals(libro_consulta.titulo, info.titulo))
                {
                    //En el caso de que exista el libro, se guarda la información del libro y estantería correspondiente
                    existe = true;
                    est_to_return = est_consulta;
                    lib_to_return = libro_consulta;
                    request_value_to_return.est_consultada = est_to_return;
                    request_value_to_return.libro_consultado = lib_to_return;
                    break;
                }
            }
        } 

        if(existe==false)
        {
            //En en caso de que no exista el libro, el único campo que retorna un valor distinto al nulo es el InfoLibro del libro que se ha consultado
            request_value_to_return.libro_consultado = info;
        }
            
        return request_value_to_return;
    }
}
