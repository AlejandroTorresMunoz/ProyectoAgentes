//agente asistente

/*Planes iniciales*/

!registrarse. //Objetivo inicial de registrarse



/*Script del asistente*/


+!registrarse : true <-
					.print("Asistente : Me he registrado en el sistema");
					.df_register("asistente",novela). //Se registra como asistente de la zona de novela
					
					
//Plan para cuando un cliente pregunta sobre un libro 
+?libro_existente_area(INFO)[source(Ag)] : true <- 	
	.print("Asistente : He  sido preguntado por la existencia de un libro por parte de : ",Ag); 
	consultar_estanteria(INFO,Ag). //Lanza la acción de consultar la estantería 
					
//En el caso de que el libro no se conozca para el asistente
+libro_no_existente_area(INFO,ID_AG) : true <- //Se le añade a la percepción de libro no existente tanto el InfoLibro como el agente al que responder
	.print("Asistente : No se conoce el libro");
	.send(ID_AG,tell,libro_no_existente_area(INFO)). //Se le informa al agente de la no existencia del libro
	
//En el caso de que el libro se conozca para el asistente
+libro_existente_estanteria(INFO,ESTANTERIA,ID_AG) : true <- //Se le añade a la percepción de libro existente tanto el InfoLibro como el agente al que responder
	.print("Asistente : Se conoce el libro");
	.send(ID_AG,tell,libro_existente_estanteria(INFO,ESTANTERIA)). //Se le informa al agente de la existencia del libro
	
	
					
+msg(M)[source(Ag)] :  true <- .print("Message from ",Ag,": ",M);-msg(M). //Para cuando llegue un mensaje
