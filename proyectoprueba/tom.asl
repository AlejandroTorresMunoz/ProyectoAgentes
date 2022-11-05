//Archivo del agente tom

!start. //Nace inicialmente con el objetivo start

+!start : true <- .send(bob,tell,hello). //Cuando salta el objetivo start, ejecuta ese plan
