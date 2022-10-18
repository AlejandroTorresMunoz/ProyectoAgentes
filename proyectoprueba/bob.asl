//Archivo del agente bob

happy(bob). //Creencia inicial
msg(en,"Hello"). //Conjunto de creencias
msg(fr,"Bonjour").
msg(pt,"Ola").

!say(hello). //Objetivo inicial
!say(Bonjour). //Objetivo de saludar en francés
language(fr).
+!say(fr) : msg(fr,"Bonjour") <- .print("Bonjour").
-msg(fr,"Bonjour").
!say(Bonjour). //Objetivo de saludar en francés
+!say(fr) : msg(fr,"Bonjour") <- .print("Bonjour").
+!say(X) : happy(bob) <- .print(X).
