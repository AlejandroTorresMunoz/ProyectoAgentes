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
	consultar_estanteria(INFO). //Lanza la acción de consultar la estantería 
					
-libro_existente_area : true <-
	.print("Asistente : No se conoce el libro").
	
+libro_existente_estanteria(POSX,POSY) : true <-
	.print("Asistente : Se conoce el libro");
	.print("Asistente : El valor de x recibido es : ",POSX);
	.print("Asistente : El valor de y recibido es : ",POSY).
					
+msg(M)[source(Ag)] :  true <- .print("Message from ",Ag,": ",M);-msg(M). //Para cuando llegue un mensaje
