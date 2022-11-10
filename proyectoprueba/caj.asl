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
+!registrar_dev(INFO) : msg(M)[source(Ag)] & libro_depositado(caja)<- //: msg(M)[source(Ag)] & libro_depositado(caja)<-  //Si se da la condición de recibir la necesidad de registrar de una devolución, habiendo recibido un mensaje
	.print("Cajero : Surge la necesidad de registrar la devolución de un libro de : ",Ag);
	.wait(2000); //Espera de 2 segundos
	tomar_libro("caja",INFO); //Se toma el libro de la posición de la caja
	+libro_tomado(INFO); //Se añade el concepto de libro tomado
	.print("Cajero : Concepto de libro tomado añadido");
	colocar_libro("cajon_dev",INFO); //Se coloca el libro en el cajón de devoluciones
	-libro_tomado(INFO); //Se elimina el concepto de libro tomado
	-print("Cajero : Concepto de libro tomado eliminado");
	.wait(2000); //Se simula un retraso de 2 segundos antes de informar del concepto de que el libro ha sido devuelto
	.send(Ag,tell,libro_devuelto). //Se le informa al cliente de que el libro ha sido devuelto

@comp1 //En el caso de surgir el plan de comprar un libro por parte de un cliente
+!comprar_libro_cliente(INFO,DINERO) : libro_depositado(caja) & msg(M)[source(Ag)] <- //Si se ha depositado el libro sobre la caja y se ha recibido un mensaje de un cliente
	.print("Cajero : Solicitud de compra recibida");
	.print("Cajero : Procesando compra del agente : ",Ag);
	comprobar_dinero(DINERO,INFO,Ag).

		
