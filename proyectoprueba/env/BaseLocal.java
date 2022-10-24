package env;


import jason.asSyntax.*;
import jason.environment.Environment; //Clase para el environment
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView; //Clase para el mundo
import jason.environment.grid.Location;

import jason.mas2j.MAS2JProject;
import jason.mas2j.AgentParameters;
import jason.infra.local.BaseLocalMAS;
import jason.infra.local.RunLocalMAS;
import jason.infra.local.LocalAgArch;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.logging.Logger;

import java.util.List;

import java.lang.String;



import java.util.logging.*;



public class BaseLocal{

    private Logger local_logger; //Objeto para el terminal de texto
    private RunLocalMAS LocalMAS; //Clase para el local MAS (Multiagent service)

    public BaseLocal(String EnvName)
    {
        /*
         * Recibe como parámetro de entrada el nombre de environment
         */
        local_logger = Logger.getLogger(EnvName); //Accede al servicio de terminal de mensajes del Environment
        //BaseLocalMAS BaseLocalClass = new BaseLocalMAS();
        //super(); //Coonstructor de la clase de la que hereda
        LocalMAS = new RunLocalMAS(); //Clase para 
        local_logger.info("BaseLocal ejecutándose. Esta clase servirá para llevar el control de los agentes en el sistema");
        
    }

    public void createAgent()
    {
        LocalAgArch Ag = new LocalAgArch(); //Clase para el agente

    }

    public void getNumberAgents()
    {
        local_logger.info("Num agentes : "+Integer.toString(LocalMAS.getNbAgents()));
    }
}
