//agente cajero

/*Creencias iniciales*/

at(P) :- pos(P,X,Y) & pos(r1,X,Y). //Creencia inicial de que se encuentra en la posición P

/*Planes iniciales*/
!start.


/*----------------*/

 
+!start : true <- .print("Hola, soy el cajero").
+libro_depositado(caja) : true <- .print("Cajero : Se ha encontrado un libro en la caja"). //En el caso de que tenga la percepción de que se ha depositado un libro en la caja
 
