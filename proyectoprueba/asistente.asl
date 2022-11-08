//agente asistente

/*Planes iniciales*/

!registrarse. //Objetivo inicial de registrarse



/*Script del asistente*/


+!registrarse : true <-
					.print("Asistente : Me he registrado en el sistema");
					.df_register("asistente",novela). //Se registra como asistente de la zona de novela
					
					
//Plan para cuando un cliente pregunta sobre un libro 
+?libro_existente_area(INFO)[source(Ag)] : true <- 	
	.print("Asistente : He recibido un mensaje de : ",Ag); 
	consultar_estanteria(INFO,Ag). //Lanza la acción de consultar la estantería 
					
+libro_no_existente_area(INFO,ID_AG) : true <-
	.print("Asistente : No se conoce el libro");
	.map.get(INFO,"titulo",VALOR_TITULO); //Se obtiene el valor del titulo
	.map.get(INFO,"autor",VALOR_AUTOR); //Se obtiene el valor del autor
	.map.get(INFO,"NumEstanteria",VALOR_ESTANTERIA); //Se obtiene el valor de la estanteria
	.print("Asistente : El valor de autor que no se ha encontrado es : ",VALOR_AUTOR);
	.print("Asistente : El valor de titulo que no se ha encontrado es : ",VALOR_TITULO);
	.print("Asistente : El valor de estanteria que no se ha encontrado es : ",VALOR_ESTANTERIA);
	.print("Asistente : El valor de Ag es : ",ID_AG);
	.send(ID_AG,tell,libro_no_existente_area(INFO)).
	
	//.print("Asistente : El valor recibido del titulo para el libro desconocido es : ",.map.get(INFO,autor,aux_valor_autor)).
	
+libro_existente_estanteria(INFO,ESTANTERIA,ID_AG) : true <-
	.print("Asistente : Se conoce el libro");
	.map.get(ESTANTERIA,"id",VALOR_ID); //Se guarda el valor de la ID 
	.print("Asistente : El valor de la ID de la estanteria es : ",VALOR_ID);
	.send(ID_AG,tell,libro_existente_estanteria(INFO,ESTANTERIA)).
	
	
					
+msg(M)[source(Ag)] :  true <- .print("Message from ",Ag,": ",M);-msg(M). //Para cuando llegue un mensaje
