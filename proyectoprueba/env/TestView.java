package env;

import jason.environment.grid.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


/** class that implements the View of Domestic Robot application */
public class TestView extends GridWorldView {

    TestModel tmodel;

    private int num_estanteria; //Último número de estantería

    public TestView(TestModel model)
    {
        //Inicializador de la clase
        super(model, "Mars Wordls", 600); 
        /*
         * Inicialización de la clase de la que hereda
         * Recibe como parámetros de entrada lo siguiente : 
         * GridWorldModel : modelo del mundo --> Grid del mundo con el número de agentes, en "TestModel está inicializado"
         * title : Título del modelo, de la ventana gráfica
         * windowSize : Tamaño de la ventana gráfica
         */
        defaultFont = new Font("Arial", Font.BOLD, 18); //Fuente con la que se pondrán los textos
        setVisible(true); //Activar la visibilidad de la ventana de la gráfica
        repaint(); //Actualización de todo el frame

        num_estanteria = 0; //Se inicia el número de estantería
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
                //En el caso de que se tenga que dibujar el cajón
                drawCajon(g,x,y);
                break;
            case TestEnv.ESTANTERIA:
                //En el acso de que se tenga que dibujar una estantería
                drawEstanteria(g,x,y);
                break;
        }
    }
    //Función para dibujar un agente
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
        else if(id == 2)
        {
            //Para el caso de un asistente
            c = Color.yellow; //Color amarillo
            label += "asistente";
            super.drawAgent(g, x, y, c ,id);
            g.setColor(Color.white);
            super.drawString(g, x, y, defaultFont, label);
        }
    }

    public void drawEstanteria(Graphics g, int x, int y)
    {
        //Función para dibujar la estantería
        g.setColor(Color.BLUE);
        super.drawObstacle(g, x, y);
        g.setColor(Color.WHITE);
        drawString(g,x,y,defaultFont, "Estanteria");
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
        //Función para dibujar el cajón
        g.setColor(Color.BLACK);
        super.drawObstacle(g, x, y);
        g.setColor(Color.WHITE);
        drawString(g,x,y,defaultFont, "Cajon"); //Se dibuja la cadena sobre la posición de la caja
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
