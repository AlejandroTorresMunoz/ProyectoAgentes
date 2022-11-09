//agente cliente --> Este agente buscará realizar la devolución de un libro

/*Creencias iniciales*/


/*Planes iniciales*/


!conocer_cajero. //Objetivo inicial de conocer al cajero



/*----------------*/
//Plan para conocer un cajero
+!conocer_cajero : true <- .wait(2000); //Espera 2 segundos
						   .df_search("cajero",ID_CAJERO);
						   .print(ID_CAJERO);
						   .send(ID_CAJERO,tell,msg("Saludo"));
						   +cajero(ID_CAJERO); //Se añade la percepción del cajero
						   .print("Concepto de ID_CAJERO incluido");
						   .map.create(INFO_LIBRO); //Creación del map info_libro
						   .map.put(INFO_LIBRO,titulo,ALATRISTE);
						   .map.put(INFO_LIBRO,autor,REVERTE);
						   .map.put(INFO_LIBRO,NumEstanteria,null);
						   +libro_tomado(INFO_LIBRO); //Se añade la posesión del libro
						   .print("Concepto de libro tomado incluido");
						   !devolver_libro; //Una vez que conoce al cajero, se le pone la necesidad de devolverle un libro
						   .wait(2000). //Le envia el concepto de cajero


+!go(X_final,Y_final) : not posicion(X_final,Y_final) <-
	//.print("Cliente : Se lanza el plan para moverse hacia X : ",X_final," Y : ",Y_final);
	movimiento_hacia(X_final,Y_final);
	!go(X_final,Y_final).
+!go(X_final,Y_final) : posicion(X_final,Y_final) <- true.
	
	


@a1 //Plan para devolver un libro
+!devolver_libro : libro_tomado(INFO) & cajero(ID)<-// & info_libro(INFO)<- //Se se tiene el concepto de tener un libro
	?pos(caja_del,X_CAJA_DEL,Y_CAJA_DEL);
	!go(X_CAJA_DEL,Y_CAJA_DEL);
	colocar_libro(1,1,"caja",INFO); //Ejecutar la acción de colocar libro en la caja;
	-libro_tomado(INFO); //Se elimina la creencia del libro tomado
	.send(ID,achieve,registrar_dev(INFO)); //Se le comunica al cajero el request de registrar la devolución
	.print("Cliente : He solicitado la devolución de un libro");
	.wait({+libro_devuelto}); //Se espera a que llegue la percepción de que el libro ha sido devuelto
	-libro_devuelto; //Se elimina la percepción para el futuro
	.print("Cliente : Fin a la devolución del libro"); //Termina la interacción.
	//------------------------//
	.wait(1000); //Se espera un 1 segundo
	.map.create(INFO_LIBRO_2); //Creación del map info_libro
	.map.put(INFO_LIBRO_2,autor,KAFKA);
	.map.put(INFO_LIBRO_2,titulo,LA_METAMORFOSIS);
	.map.put(INFO_LIBRO_2,NumEstanteria,null);
	!consultar_info(INFO_LIBRO_2). //Surge el plan de consultar información sobre un libro

@a2 //Plan para preguntar información
+!consultar_info(INFO) : true <- 
	.print("Cliente : Tengo el plan de consultar información sobre un libro");
	.term2string(ZONA,"novela");
	.df_search("asistente",ZONA,ID_ASISTENTE); //Se busca al asistente de dicha zona
	+asistente(ID_ASISTENTE,ZONA); //Se añade la percepción del asistente
	?pos(asistente,X_ASISTENTE,Y_ASISTENTE);
	!go(X_ASISTENTE-1,Y_ASISTENTE);
	.send(ID_ASISTENTE,askOne,libro_existente_area(INFO)). //Le pregunta al asistente de zona sobre la existencia del libro

//En el caso de que se le comunique que no existe el libro que ha preguntado
+libro_no_existente_area(INFO) : true <-
	.print("Cliente : Se me ha comunicado que no existe el libro"). 
	
//En el caso de que se le comunique que existe el libro por el que ha preguntado
+libro_existente_estanteria(INFO,ESTANTERIA) : true <-
	.print("Cliente : Se me ha comunicado que existe el libro");
	.map.get(ESTANTERIA,"id",ID_EST); //Se guarda el valor de la ID de la estantería hacia la que moverse
	.print("Cliente : Valor de la id de la estanteria : ",ID_EST);
	?pos(ID_EST,X_EST,Y_EST);
	!go(X_EST,Y_EST).
	
	
+msg(M)[source(Ag)] :  true <- .print("Message from ",Ag,": ",M);-msg(M). //Para cuando llegue un mensaje


