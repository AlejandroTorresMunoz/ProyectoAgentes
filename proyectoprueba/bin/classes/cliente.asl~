//agente cliente

/*Creencias iniciales*/

/*Planes iniciales*/
!start.
necesito_colocar_libro. //Necesidad de colocar un libro, como concepto


/*----------------*/

 
+!start : necesito_colocar_libro <- .print("Hola, soy el cliente, deposito un libro en la caja");
					.wait(2000); //Espera de 2 segundos
					colocar_libro(2,1,"caja");-necesito_colocar_libro.
-necesito_colocar_libro : true <- .print("Deje de necesitar el colocar un lirbo"). 
