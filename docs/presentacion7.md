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

En primer lugar, es importante señalar que este proyecto no contempla el uso de una base de datos relacional como sistema de persistencia principal, ya que el procesador M32 simulado en Logisim no almacena datos de usuarios en un sentido convencional.
Sin embargo, el modelo entidad-relación se reutiliza aquí como una herramienta conceptual que nos permite representar y modelar las interacciones de los usuarios con el simulador, así como la forma en que estas interacciones pueden generar datos útiles para evaluar y mejorar el sistema.
Además, se han definido claves primarias y foráneas para dar mayor estructura y coherencia a las relaciones entre entidades, manteniendo así la lógica del modelado ER incluso en un contexto que no es puramente de base de datos.

# Modelo Entidad Relación

Este modelo trata de representar la interacción que tienen los usuarios con el procesador, y como la información referente a su feedback se puede recopilar para mejorar el estado actual del procesador.

# Modelo Entidad Relación: entidad usuario

La entidad usuario representa tanto al docente como al estudiante que hará uso del simulador. Información relevante que se debe registrar sobre estos es su nombre y su rol (sea docente o estudiante).

# Modelo Entidad Relación: entidad sesión de simulación

Otra entidad relevante es la de sesión de simulación. Información relevante que se debe registrar sobre estos es la hora de inicio y la hora de fin de la simulación. Esto con el fin de registrar dentro de las bitácoras del proyecto en qué clases el simulador presentó errores o sugerencias.
Esta información permite generar una bitácora de uso del simulador, útil tanto para el análisis técnico como pedagógico del desempeño del sistema.

# Modelo Entidad Relación: relación entre usuario y sesión de simulación

Un usuario puede iniciar múltiples sesiones de simulación, pero cada sesión está asociada a un único usuario.
Esto implica una relación de uno a muchos, donde la clave foránea ID de usuario en la entidad sesión establece el vínculo.
Este diseño permite, por ejemplo, generar informes sobre cuántas simulaciones ha realizado un estudiante específico, o qué docente ha evaluado más el sistema.

# Modelo Entidad Relación: entidad instrucción

La tercera entidad dentro del modelo es la entidad Instrucción, la que representa una instrucción que se ejecutará dentro del procesador. Esta entidad entrega información relevante tal como el opcode de la instrucción, su mnemotécnico asociado y los operandos de esta instrucción.

# Modelo Entidad Relación: relación entre sesión de simulación e instrucción

Una sesión puede contener muchas instrucciones ejecutadas, lo que genera una relación de uno a muchos desde sesión hacia instrucción.
De este modo, se puede rastrear el comportamiento de una sesión particular y entender cómo fue utilizado el procesador paso a paso.

# Modelo Entidad Relación: entidad registro

La cuarta entidad dentro del modelo es la entidad Registro. Este representa una memoria de 4 bytes (32 bits) dentro del procesador, la cual permite realizar operaciones de propósito general (tales como la suma, saltos a direcciones de memoria, etcétera). De esta entidad se necesita saber el nombre del registro, y el valor de este al final de la simulación.

# Modelo Entidad Relación: relación entre instrucción y registro

Las instrucciones pueden leer y/o modificar múltiples registros, y a su vez, un mismo registro puede ser afectado por múltiples instrucciones.
Esto genera una relación de muchos a muchos, que puede representarse mediante una entidad intermedia (por ejemplo, "ModificaciónRegistro") que registre:

- El tipo de acceso (lectura o escritura)
- El valor antes y después de la operación
- La instrucción asociada

Este tipo de modelado permite reconstruir la ejecución a nivel de registros, paso a paso.

# Modelo Entidad Relación: entidad bloque de memoria

Finalmente, la quinta entidad dentro del modelo entidad-relación es la entidad Bloque de Memoria. Esta entidad entrega información importante de en qué lugar se está trabajando dentro de la RAM en el procesador, tal como la dirección de inicio del bloque en la RAM, el tamaño del bloque, y el contenido de este bloque. Aquí se destaca que el tamaño del bloque (debido a las limitaciones de RAM en algunos dispositivos) no debería de pasar de 1GB de memoria RAM.

# Modelo Entidad Relación: relación entre entre registro y bloque de memoria
Múltiples registros pueden referenciar distintas regiones de la memoria, ya sea para lectura o escritura, y una misma región de memoria puede ser accedida por varios registros.
Esto también constituye una relación de muchos a muchos, que puede ser modelada mediante una tabla intermedia (por ejemplo, “AccesoMemoria”), con los siguientes campos:

- Registro que accede
- Dirección de memoria afectada
- Tipo de operación (lectura/escritura)
- Timestamp o momento del acceso

Este nivel de detalle permite modelar cómo fluye la información a nivel de memoria y qué registros actúan como punteros o índices.

# Conclusiones

En conclusión, si bien el modelo entidad-relación no será implementado directamente en una base de datos tradicional, su uso en este proyecto permite:

- Representar de manera estructurada las interacciones entre usuario y simulador
- Modelar conceptualmente los procesos internos del procesador M32, desde las instrucciones hasta la manipulación de registros y memoria
- Facilitar el análisis posterior del uso del sistema, la depuración, y la mejora continua del simulador en Logisim Evolution

Este enfoque híbrido entre hardware y modelado de datos aporta una perspectiva innovadora para la gestión del proyecto.

¡Muchas gracias por su atención!
