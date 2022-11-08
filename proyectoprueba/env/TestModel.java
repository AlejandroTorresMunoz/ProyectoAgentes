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


    // the grid size
    public static final int GSize = 10; // Tamaño del grid que define el mundo

    class Estanteria
    {
        //Clase para almacenar la información de una estanteria
        int id_; //ID de la estanteria
        Location pos_estanteria_; //Position de la estanteria
        TestEnv.InfoLibro[] libros = {}; //Array con los valores de los libros 
        private Estanteria(int id, int x, int y)
        {
            //Constructor de la clase
            id_ = id;
            pos_estanteria_ = new Location(x,y); 
        }
    }

    public static Estanteria[] array_estanterias = {};


    //Clase que hereda de la clase que sirve para un mundo definido por un grid
    //Sirve para modelar el comportamiento dentro del environment

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
            mapa_0_0.put("NumEstanteria","0");
            est0 = add_libro_est(est0, mapa_0_0);
            HashMap<String,String> mapa_0_1 = new HashMap<String,String>();
            mapa_0_1.put("titulo","STONER");
            mapa_0_1.put("autor","JOHN_WILLIAMS");
            mapa_0_1.put("NumEstanteria","0");
            est0 = add_libro_est(est0, mapa_0_1);

            array_estanterias = append(array_estanterias,est0);

            //Estantería 1
            add(TestEnv.ESTANTERIA, 9, 2);
            Estanteria est1 = new Estanteria(1, 9, 2);
            //Se añaden los libros a la estantería 1

            HashMap<String,String> mapa_1_0 = new HashMap<String,String>();
            mapa_1_0.put("titulo","1984");
            mapa_1_0.put("autor","GEORGE_ORWELL");
            mapa_1_0.put("NumEstanteria","1");
            est1 = add_libro_est(est1, mapa_1_0);
            HashMap<String,String> mapa_1_1 = new HashMap<String,String>();
            mapa_1_1.put("titulo","REBELION_EN_LA_GRANJA");
            mapa_1_1.put("autor","GEORGE_ORWELL");
            mapa_1_1.put("NumEstanteria","1");
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


        void tomar_libro(int x,int y, String tipo_objeto,TestEnv.InfoLibro info)
        {
            /*
             * Función para tomar un libro de una posición dada
             */
            if (tipo_objeto.equals("caja"))
            {
                remove(TestEnv.LIBRO, TestEnv.lcaja.x, TestEnv.lcaja.y); 
            }
        }

        void colocarlibro(int x, int y, String tipo_objeto, TestEnv.InfoLibro Libro)
        {
            /*
             * Función para colocar un libro sobre una posición dada, sobre un tipo de objeto dado
             */
            Location pos_col = new Location(x,y); //Posición donde se va a colocar el libro

            if(tipo_objeto.equals("cajon_dev"))
            {
                //Si se coloca sobre el cajón de devoluciones
                TestEnv.libros_cajon = append(TestEnv.libros_cajon,Libro);   
                TestEnv.logger.info("El estado del cajon de devoluciones es el siguiente : ");
                for(int i=0;i<TestEnv.libros_cajon.length;i++)
                {
                    TestEnv.logger.info("autor numero "+Integer.toString(i)+" registrado : "+TestEnv.libros_cajon[i].autor);
                    TestEnv.logger.info("autor titulo "+Integer.toString(i)+" registrado : "+TestEnv.libros_cajon[i].titulo);
                }
            }
            else if(tipo_objeto.equals("caja"))
            {
                //Si se coloca sobre la caja
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

        Location obtener_pos_ag(int id) throws Exception
        {
            //Función para obtener la posición de un agente dada su id
            return getAgPos(id);
        }

        Estanteria consultar_estanteria(TestEnv.InfoLibro info)
        {
            //Función para consultar si un libro existe o no sobre las estanterías
            //Devuelve true en caso de que exista, o false en caso contrario
            //TestEnv.logger.info("El número de estanterías es : "+ Integer.toString(array_estanterias.length));
            //TestEnv.logger.info("El libro que se consulta es : "+info.titulo);
            //TestEnv.logger.info("Longitud de info : "+info.titulo.length());
            int num_estanterias = array_estanterias.length; //Número de estanterías

            boolean existe = false; //Valor a devolver. Sólo será true si se encuentra el título del libro
            Estanteria est_to_return = null;
            for(int num_est=0;num_est<num_estanterias;num_est++)
            {
                //Se consulta cada una de las estanterías
                Estanteria est_consulta = array_estanterias[num_est]; //Se obtiene la estantería
                int num_libros = est_consulta.libros.length; //Se obtiene el número de libros almacenados en la estantería
                //TestEnv.logger.info("Se está consultando la estantería : "+Integer.toString(num_est));
                for(int num_libro=0;num_libro<num_libros;num_libro++)
                {
                    //Se consulta cada uno de los libros de la estantería actual
                    TestEnv.InfoLibro libro_consulta = est_consulta.libros[num_libro]; //Se obtiene el libro actual

                    if(Objects.equals(libro_consulta.titulo, info.titulo))
                    {
                        //TestEnv.logger.info("Se ha encontrado el libro");
                        existe = true;
                        est_to_return = est_consulta;
                        break;
                    }
                }
            } 
            
            return est_to_return;
        }
}
