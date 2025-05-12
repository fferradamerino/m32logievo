# Guion de Presentación: Modelo Entidad Relación

Buenas tardes estimados compañeros y profesores. Hoy presentaré los temas relacionados al modelo entidad-relación, respecto al tema de Diseño, Implementación y Simulación de la Arquitectura del Microprocesador M32 en Logisim Evolution

El temario de la presentación será el siguiente:

- Primero, presentaré los Objetivos de la Presentación
- Luego, presentaré las Consideraciones Previas al Análisis del Modelo Entidad Relación
- Con eso en mente, presentaré el Modelo Entidad Relación
- Y finalmente presentaré la Conclusión

# Objetivos de la Presentación

Los objetivos de esta presentación son:

- Presentar el Modelo Entidad Relación asociado al proyecto
- Presentar las consideraciones respecto al modelo entidad relación asociado al proyecto
- Explicar las relaciones y entidades del modelo entidad relación asociado al proyecto

# Consideraciones Previas al Análisis del Modelo Entidad Relación

Una de las limitaciones que se presenta para trabajar en este proyecto, es que el proyecto no contempla trabajar en una base de datos: el procesador M32 no almacena información de usuarios en el sentido tradicional.
A pesar de ello, un modelo ER no suele utilizarse para representar componentes de hardware, este diagrama replantea la simulación de la CPU M32 como un sistema interactivo basado en datos donde los componentes y las interacciones del usuario se tratan como entidades de datos para fines de modelado conceptual.
Adicionalmente, para respetar la estructura de base de datos del modelo entidad-relación, se hizo uso de claves primarias y foráneas con el fin de definir relaciones dentro del modelo.

# Modelo Entidad Relación

Este modelo trata de representar la interacción que tienen los usuarios con el procesador, y como la información referente a su feedback se puede recopilar para mejorar el estado actual del procesador.

# Modelo Entidad Relación: entidad usuario

La entidad usuario representa tanto al docente como al estudiante que hará uso del simulador. Información relevante que se debe registrar sobre estos es su nombre y su rol (sea docente o estudiante).

# Modelo Entidad Relación: entidad sesión de simulación

Otra entidad relevante es la de sesión de simulación. Información relevante que se debe registrar sobre estos es la hora de inicio y la hora de fin de la simulación. Esto con el fin de registrar dentro de las bitácoras del proyecto en qué clases el simulador presentó errores o sugerencias.

# Modelo Entidad Relación: relación entre usuario y sesión de simulación

Un usuario puede iniciar muchas simulaciones (añadir mas texto acá)

# Modelo Entidad Relación: entidad instrucción

La tercera entidad dentro del modelo es la entidad Instrucción, la que representa una instrucción que se ejecutará dentro del procesador. Esta entidad entrega información relevante tal como el opcode de la instrucción, su mnemotécnico asociado y los operandos de esta instrucción.

# Modelo Entidad Relación: relación entre sesión de simulación e instrucción

Dentro de una sesión de simulación se pueden ejecutar múltiples instrucciones

# Modelo Entidad Relación: entidad registro

La cuarta entidad dentro del modelo es la entidad Registro. Este representa una memoria de 4 bytes (32 bits) dentro del procesador, la cual permite realizar operaciones de propósito general (tales como la suma, saltos a direcciones de memoria, etcétera). De esta entidad se necesita saber el nombre del registro, y el valor de este al final de la simulación.

# Modelo Entidad Relación: relación entre instrucción y registro

Muchas instrucciones pueden leer o escribir múltiples registros (añadir más desarrollo sobre esta idea).

# Modelo Entidad Relación: entidad bloque de memoria

Finalmente, la quinta entidad dentro del modelo entidad-relación es la entidad Bloque de Memoria. Esta entidad entrega información importante de en qué lugar se está trabajando dentro de la RAM en el procesador, tal como la dirección de inicio del bloque en la RAM, el tamaño del bloque, y el contenido de este bloque. Aquí se destaca que el tamaño del bloque (debido a las limitaciones de RAM en algunos dispositivos) no debería de pasar de 1GB de memoria RAM.

# Modelo Entidad Relación: relación entre entre registro y bloque de memoria

Múltiples registros pueden apuntar a múltiples regiones de memoria. (desarrollar más este punto)

# Conclusiones

En conclusión, a pesar de que el modelo entidad relación no pueda implementarse de manera tradicional en una base de datos, considerando este proyecto, es posible registrar información de cada clase sobre M32, lo que ayuda a tener mejor feedback en la mantención del proyecto de Logisim Evolution, y en como la interacción del usuario puede ayudar durante el desarrollo del usuario.

¡Muchas gracias por su atención!
