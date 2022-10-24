//agente cajero

/*Creencias iniciales*/

at(P) :- pos(P,X,Y) & pos(r1,X,Y). //Creencia inicial de que se encuentra en la posición P
caj_dev(0,0). //Creencia inicial del concepto del cajón de devoluciones, con una posición inicial definida
pos_caj_dev(P) :- pos(P,0,0).
/*Planes iniciales*/

!registrarse. //Objetivo inicial de registrarse



/*----------------*/

+!registrarse : true <- .df_register("cajero"). //Se registra como cajero

+msg(M)[source(Ag)] :  true <- .print("Message from ",Ag,": ",M);-msg(M). //Para cuando llegue un mensaje

@dev1 //Plan para cuando surja la necesidad de registrar la devolución de un libro
+!registrar_dev(INFO) : msg(M)[source(Ag)] <-  //Si se da la condición de recibir la necesidad de registrar de una devolución, habiendo recibido un mensaje
	.print("Cajero : Surge la necesidad de registrar la devolución de un libro de : ",Ag);
	.print("info : ",INFO);
	tomar_libro(0,0,INFO). //PROBAR ESTA ACCIÓN
	/*
	tomar_libro(x_caj,y_caj,info_libro); //Se toma el libro
	colocar_libro(info_libro,caj_dev); //Se coloca sobre el concepto del cajón de devoluciones 
	.send
	*/
	


+libro_depositado(caja) : true <- .print("Cajero : Se ha encontrado un libro en la caja"). //En el caso de que tenga la percepción de que se ha depositado un libro en la caja
 
