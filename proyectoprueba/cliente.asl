//agente cliente --> Este agente buscará realizar la devolución de un libro

/*Creencias iniciales*/

necesito_colocar_libro. //Necesidad de colocar un libro, como concepto
libro_tomado.//(libro("REVERTE","ALATRISTE","BOLSILLO",20.0,"FICCION","ALQUILER",1,NULL,NULL)). //Creencia inicial de que se posee un libro

/*Planes iniciales*/


!conocer_cajero. //Objetivo inicial de conocer al cajero
//!devolver_libro.//Objetivo inicial de devolver un libro


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
						   
						   
+!at(cliente,P) : at(cliente,P) <- true.
+!at(cliente,P) : not at(cliente,P)
  <- move_towards(P);
     !at(cliente,P).



@a1 //Plan para devolver un libro
+!devolver_libro : libro_tomado(INFO) & cajero(ID)<-// & info_libro(INFO)<- //Se se tiene el concepto de tener un libro
	!at(cliente,caja); //Surge el plan de moverse hacia la caja
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
	.map.put(INFO_LIBRO_2,autor,GEORGE_ORWELL);
	.map.put(INFO_LIBRO_2,titulo,REBELION_EN_LA_GRANJA);
	.map.put(INFO_LIBRO_2,NumEstanteria,null);
	!consultar_info(INFO_LIBRO_2). //Surge el plan de consultar información sobre un libro

@a2 //Plan para preguntar información
+!consultar_info(INFO) : true <- 
	.print("Cliente : Tengo el plan de consultar información sobre un libro");
	.term2string(ZONA,"novela");
	.df_search("asistente",ZONA,ID_ASISTENTE); //Se busca al asistente de dicha zona
	.print("Cliente : Se ha conocido al asistente de la zona : ", ZONA);
	.print("Cliente : Su ID es : ",ID_ASISTENTE);
	+asistente(ID_ASISTENTE,ZONA); //Se añade la percepción del asistente
	.print("Cliente : Se inicia movimiento hacia el asistente");
	!at(cliente,ID_ASISTENTE);//Se le indica que se mueva hacia el asistente
	.print("Cliente : Estoy al lado del asistente");
	.send(ID_ASISTENTE,askOne,libro_existente_area(INFO)). //Le pregunta al asistente de zona sobre la existencia del libro

//En el caso de que se le comunique que no existe el libro que ha preguntado
+libro_no_existente_area(INFO) : true <-
	.print("Cliente : Se me ha comunicado que no existe el libro"). 
	
+libro_existente_estanteria(INFO,ESTANTERIA) : true <-
	.print("Cliente : Se me ha comunicado que existe el libro").
	
+msg(M)[source(Ag)] :  true <- .print("Message from ",Ag,": ",M);-msg(M). //Para cuando llegue un mensaje


