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
+!registrar_dev(INFO) : msg(M)[source(Ag)] & libro_depositado(caja)<-  //Si se da la condición de recibir la necesidad de registrar de una devolución, habiendo recibido un mensaje
	.print("Cajero : Surge la necesidad de registrar la devolución de un libro de : ",Ag);
	.print("Cajero : El ID del clienbte que se hija recibido es : ", source(Ag));
	.print("info : ",INFO);
	tomar_libro("caja",INFO); //Se toma el libro de la posición de la caja
	colocar_libro("cajon_dev",INFO); //Se coloca el libro en el cajón de devoluciones
	.wait(2000); //Se simula un retraso de 2 segundos antes de informar del concepto de que el libro ha sido devuelto
	.send(Ag,tell,libro_devuelto). //Se le informa al cliente de que el libro ha sido devuelto

@comp1
+!comprar_libro_cliente(INFO,DINERO) : libro_depositado(caja) & msg(M)[source(Ag)] <-
	.print("Cajero : Solicitud de compra recibida");
	.print("Cajero : El valor de INFO es : ",INFO);
	.print("Cajero : El valor de DINERO es : ",DINERO);
	.print("Cajero : Procesando compra del agente : ",Ag);
	comprobar_dinero(DINERO,INFO,Ag).

		
