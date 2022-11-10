//agente cliente --> Este agente buscará realizar la devolución de un libro

/*Creencias iniciales*/
dinero(35). //Concepción inicial del dinero que se tiene

/*Planes iniciales*/
!conocer_cajero. //Objetivo inicial de conocer al cajero
/*----------------*/

//Plan para conocer un cajero
+!conocer_cajero : true <- .wait(2000); //Espera 2 segundos
						   .df_search("cajero",ID_CAJERO); //Se busca el ID del cajero
						   .send(ID_CAJERO,tell,msg("Cliente : Comunicación establecida"));
						   +cajero(ID_CAJERO); //Se añade la percepción del cajero
						   .print("Cliente : Concepto de ID_CAJERO incluido");
						   .map.create(INFO_LIBRO); //Creación del map info_libro
						   .map.put(INFO_LIBRO,titulo,ALATRISTE); //Se le añade el titulo
						   .map.put(INFO_LIBRO,autor,REVERTE); //Se le añade el autor
						   .map.put(INFO_LIBRO,numestanteria,null); //Se le añade el número de estantería
						   .map.put(INFO_LIBRO,precio,null); //Se le añade el precio
						   +libro_tomado(INFO_LIBRO); //Se añade la percepción del libro tomado
						   .print("Cliente : Concepto de libro tomado incluido");
						   !devolver_libro; //Una vez que conoce al cajero, se le pone la necesidad de devolverle un libro
						   .wait(2000). //Espera de 2 segundos


+!go(X_final,Y_final) : not posicion(X_final,Y_final) <-
	//.print("Cliente : Se lanza el plan para moverse hacia X : ",X_final," Y : ",Y_final);
	movimiento_hacia(X_final,Y_final);
	!go(X_final,Y_final).
+!go(X_final,Y_final) : posicion(X_final,Y_final) <- true.
	
	


@a1 //Plan para devolver un libro
+!devolver_libro : libro_tomado(INFO) & cajero(ID)<- //Se se tiene el concepto de tener un libro y del cajero
	.print("Cliente : Tengo el plan de devolver un libro");
	?pos(caja_del,X_CAJA_DEL,Y_CAJA_DEL); //Se pregunta por el concepto de la posición delantera de la caja
	!go(X_CAJA_DEL,Y_CAJA_DEL); //Se lanza el plan de moverse delante de la caja
	colocar_libro("caja",INFO); //Ejecutar la acción de colocar libro en la caja
	-libro_tomado(INFO); //Se elimina la creencia del libro tomado
	.print("Cliente : Concepto de libro tomado eliminado");
	.send(ID,achieve,registrar_dev(INFO)); //Se le comunica al cajero el request de registrar la devolución
	.print("Cliente : He solicitado la devolución de un libro");
	.wait({+libro_devuelto}); //Se espera a que llegue la percepción de que el libro ha sido devuelto
	-libro_devuelto; //Se elimina la percepción para el futuro
	.print("Cliente : Fin a la devolución del libro"); //Termina la interacción.
	//------------------------//
	//A continuación, el cliente procederá a realizar la consulta de información de un libro
	.wait(1000); //Se espera un 1 segundo
	.map.create(INFO_LIBRO_2); //Creación del map info_libro
	.map.put(INFO_LIBRO_2,autor,KAFKA); //Se le añade el autor
	.map.put(INFO_LIBRO_2,titulo,LA_METAMORFOSIS); //Se le añade el título
	.map.put(INFO_LIBRO_2,numestanteria,null); //Se le añade el número de estantería
	.map.put(INFO_LIBRO_2,precio,null); //Se le añade el precio
	!consultar_info(INFO_LIBRO_2). //Surge el plan de consultar información sobre un libro

@a2 //Plan para preguntar información
+!consultar_info(INFO) : true <- 
	.print("Cliente : Tengo el plan de consultar información sobre un libro");
	.term2string(ZONA,"novela"); //Se guarda en el término ZONA la cadena "novela", que es la especialización del asistente de zona al que se va a consultar
	.df_search("asistente",ZONA,ID_ASISTENTE); //Se busca al asistente de dicha zona
	+asistente(ID_ASISTENTE,ZONA); //Se añade la percepción del asistente
	.print("Cliente : Concepto de ID_ASISTENTE añadido, de la zona : ",ZONA);
	?pos(asistente,X_ASISTENTE,Y_ASISTENTE); //Se pregunta por el concepto de la posición del asistente
	!go(X_ASISTENTE-1,Y_ASISTENTE); //Se mueve hacia la posición del asistente, justo delante para no pisarlo
	.send(ID_ASISTENTE,askOne,libro_existente_area(INFO)). //Le pregunta al asistente de zona sobre la existencia del libro

//En el caso de que se le comunique que no existe el libro que ha preguntado
+libro_no_existente_area(INFO) : true <-
	.print("Cliente : Se me ha comunicado que no existe el libro");
	.print("Cliente : Me elimino del sistema");
	!go(0,9); //Se mueve hacia la esquina del mapa
	.kill_agent(cliente).

	
//En el caso de que se le comunique que existe el libro por el que ha preguntado
+libro_existente_estanteria(INFO,ESTANTERIA) : true <-
	.print("Cliente : Se me ha comunicado que existe el libro");
	.map.get(ESTANTERIA,id,ID_EST); //Se guarda el valor de la ID de la estantería hacia la que moverse
	.print("Cliente : Valor de la id de la estanteria : ",ID_EST);
	?pos(ID_EST,X_EST,Y_EST); //Se pregunta por el concepto de la posición de la estantería que se ha recibido
	!go(X_EST,Y_EST); //Se mueve hacia la posición de la estantería
	tomar_libro("estanteria",INFO); //Toma el libro de la estantería
	+libro_tomado(INFO); //Se le da la percepción de libro tomado
	.print("Cliente : Concepto de libro tomado añadido");
	.wait(1000);
	!comprar_libro; //Se le da la necesidad de comprar un libro
	.print("Cliente : Tengo el plan de comprar un libro");
	.wait(2000).
	
@a3 //Plan para comprar un libro
+!comprar_libro : libro_tomado(INFO) & cajero(ID) & dinero(CANTIDAD_DINERO) <-
	?pos(caja_del,X_CAJA_DEL,Y_CAJA_DEL); //Se pregunta por el concepto de la posición delante de la estantería
	!go(X_CAJA_DEL,Y_CAJA_DEL); //Se mueve hacia la posición de la caja
	colocar_libro("caja",INFO); //Colocar el libro sobre la caja
	-libro_tomado(INFO);
	.print("Cliente : Concepto de libro tomado eliminado");
	.send(ID,achieve,comprar_libro_cliente(INFO,CANTIDAD_DINERO));//Se le pide comprar con la cantidad de dinero que posee el cliente inicialmente, como percepción
	.print("Cliente : He solicitado la compra de un libro a : ",ID).

//En el caso de no tener dinero suficiente	
+dinero_no_suficiente(INFO,DINERO_CLIENTE) : true <-
	.print("Cliente : Se me ha comunicado que no tengo dinero suficinete para comprar el libro");
	.print("Cliente : Mi dinero restante es : ",DINERO_CLIENTE);
	.abolish(dinero(_)); //Se elimina la creencia anterior del dinero
	+dinero(DINERO_CLIENTE); //Se le añade la nueva creencia sobre el dinero restante
	.print("Cliente : No tengo dinero suficiente. Hasta luego, buen día");
	!go(0,9); //Se mueve hacia la esquina del mapa
	.kill_agent(cliente). //Se elimina del sistema
	
//En el caso de tener dinero suficiente
+dinero_suficiente(INFO,DINERO_CLIENTE) : true <-//(INFO) : true <-
	tomar_libro("caja",INFO); //Se toma el libro de la posición de la caja
	+libro_tomado(INFO); //Se añade la posesión del libro
	.print("Cliente : Mi dinero restante es : ",DINERO_CLIENTE);
	.abolish(dinero(_)); //Se elimina la creencia anterior del dinero
	+dinero(DINERO_CLIENTE);
	.print("Cliente : He comprado el libro. Hasta luego, buen dia");
	!go(0,9); //Se mueve hacia la la esquina del mapa
	.kill_agent(cliente). //Se elimina del sistema
	
	
+msg(M)[source(Ag)] :  true <- .print("Message from ",Ag,": ",M);-msg(M). //Para cuando llegue un mensaje


