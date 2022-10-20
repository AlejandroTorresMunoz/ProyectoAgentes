//agente cliente --> Este agente buscará realizar la devolución de un libro

/*Creencias iniciales*/

necesito_colocar_libro. //Necesidad de colocar un libro, como concepto
libro_tomado.//(libro("REVERTE","ALATRISTE","BOLSILLO",20.0,"FICCION","ALQUILER",1,NULL,NULL)). //Creencia inicial de que se posee un libro

/*Planes iniciales*/


!conocer_cajero. //Objetivo inicial de conocer al cajero
//!devolver_libro.//Objetivo inicial de devolver un libro


/*----------------*/
+!conocer_cajero : true <- .wait(2000); //Espera 2 segundos
						   .df_search("cajero",ID_CAJERO);
						   .print(ID_CAJERO);
						   .send(ID_CAJERO,tell,msg("Saludo"));
						   +cajero(ID_CAJERO); //Se añade la percepción del cajero
						   .print("Concepto de ID_CAJERO incluido");
						   .map.create(INFO_LIBRO); //Creación del map info_libro
						   .map.put(INFO_LIBRO,autor,"REVERTE");
						   .map.put(INFO_LIBRO,titulo,"ALATRISTE");
						   +libro_tomado(INFO_LIBRO); //Se añade la posesión del libro
						   .print("Concepto de libro tomado incluido");
						   !devolver_libro; //Una vez que conoce al cajero, se le pone la necesidad de devolverle un libro
						   .wait(2000). //Le envia el concepto de cajero



@a1 //Plan para devolver un libro
+!devolver_libro : libro_tomado(INFO) & cajero(ID)<-// & info_libro(INFO)<- //Se se tiene el concepto de tener un libro
	//.wait(2000);
	colocar_libro(2,1,"caja"); //Ejecutar la acción de colocar libro en la caja;
	//.send(ID,tell,msg("Hola"));
	.send(ID,achieve,registrar_dev(INFO)); //Se le comunica al cajero el request de registrar la devolución
	.print("Request enviado").
	
 

					

