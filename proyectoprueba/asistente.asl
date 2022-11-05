//agente asistente

/*Planes iniciales*/

!registrarse. //Objetivo inicial de registrarse



/*Script del asistente*/


+!registrarse : true <-
					.print("Asistente : Me he registrado en el sistema");
					.df_register("asistente",novela). //Se registra como asistente de la zona de novela
