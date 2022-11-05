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


    //Clase que hereda de la clase que sirve para un mundo definido por un grid
    //Sirve para modelar el comportamiento dentro del environment
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
                setAgPos(2,6,6); //Agente asistente
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
            add(TestEnv.CAJA, TestEnv.lcaja.x, TestEnv.lcaja.y); //Inicialización de un objeto de tipo CAJA sobre la posición 1,1
            add(TestEnv.CAJON, TestEnv.lcajon.x, TestEnv.lcajon.y); //Inicialización de un objeto de tipo CAJON sobre la posición 0,0
        }

        public static <T> T[] append(T[] arr, T element) 
        {
            //Función para añadir elementos a un array de estructuras
            final int N = arr.length;
            arr = Arrays.copyOf(arr, N + 1);
            arr[N] = element;
            return arr;
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
}
