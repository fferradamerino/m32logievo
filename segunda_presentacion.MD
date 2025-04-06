# Guion de Presentación: Simulador del Microprocesador M32

Buenas tardes estimados compañeros y profesores. Hoy presentaré los temas relacionados a la organización, definición del problema y resultados de la primera reunión con el cliente, respecto al tema de Diseño, Implementación y Simulación de la Arquitectura del Microprocesador M32 en Logisim Evolution

El temario de la presentación será el siguiente:

- Primero, presentaré la Descripción de la Organización en la diapositiva 3
- Luego, presentaré la Descripción del Problema en la diapositiva 4
- Con eso en mente, presentaré los Resultados de la Primera Reunión con los clientes en la diapositiva 5
- Seguido de ello, presentaré los Perfiles de usuario en las diapositivas 6 y 7
- Y finalmente presentaré la conclusión en la diapositiva 8

## 1. Objetivos de la presentación
Los objetivos de esta presentación son:
	Describir la organización que será usuario del resultado del proyecto
	Describir el problema asociado al tema del proyecto
	Indicar el resultado de la primera reunión con el cliente del tema del proyecto

## 2. Descripción de la Organización

La organización que hará uso de esta herramienta es la Universidad del Bío-Bío. La Universidad del Bío-Bío es una institución que ofrece una serie de carreras orientadas a la tecnología, dentro de las cuales está la carrera de Ingeniería Civil en Informática. Otra carrera que destaca dentro de este ámbito es la carrera de Ingeniería de Ejecución en Computación e Informática, en la sede Concepción.

Tanto en la carrera de Ingeniería Civil en Informática, como en la carrera de Ingeniería de Ejecución en Computación e Informática, la asignatura de Arquitectura de Computadores es la que se enfoca en entregar el conocimiento necesario para comprender los principios del diseño y operación de procesadores.

El microprocesador M32 es un procesador docente utilizado en esta asignatura para enseñar la estructura y el comportamiento de una arquitectura de CPU. Se dispone de una versión previa implementada en Hades por tesistas de años anteriores, pero este al encontrarse desactualizado, supone una limitación a la hora de aprender sobre arquitectura de procesadores.

Se recalca que este proyecto busca proporcionar una herramienta visual e interactiva para el aprendizaje de los conceptos clave del procesador M32, destacando su funcionamiento interno y la interacción entre sus componentes. Por lo tanto, esta nuevo simulación buscará ser una herramienta didáctica más completa, permitiendo visualizar el paso de datos, la ejecución de instrucciones y el funcionamiento interno de la Unidad de Control (la que será implementada utilizando la técnica de microprogramación).

## 3. Descripción del Problema

El problema principal es la necesidad de una implementación mejorada del M32 que ofrezca una visualización clara del funcionamiento del procesador. Específicamente, se busca eliminar el problema de tener que trabajar a lápiz y papel a la hora de tener que resolver ejercicios de arquitecturas de procesadores, lo cual presenta una oportunidad de mejorar el rendimiento de los estudiantes de la asignatura de Arquitectura de computadores.

De manera adicional, el contenido relacionado a algunos tópicos en Arquitectura de Computadores no se pueden estudiar con la profundidad que debería tener. Tópicos tales como la microprogramación, pipelining avanzado o incluso memorias caché, no alcanzan a ser repasados lo que deja a la Universidad del Bío-Bío en una situación de desventaja, comparado con otras universidades del país.

Tal como se mencionó anteriormente, si bien ya existe una implementación funcional de M32 en Hades realizada por estudiantes tesistas de Ingeniería Civil en Informática, el hecho de que Hades no sea compatible con los sistemas operativos modernos significa una limitación considerable.

## 4. Resultados de la Primera Reunión con el Cliente

Para poder desarrollar una visión clara de las necesidades de los usuarios de este simulador, se llevaron a cabo entrevistas con profesores y estudiantes de la carrera. Con esta información, se logró definir dos perfiles claves de usuario: docentes y estudiantes de Arquitectura de Computadores.

Ambos perfiles concuerdan con la necesidad de tener una forma de visualizar el estado de los registros y del flujo de datos del procesador. De manera adicional, ambos perfiles también sugieren el uso del simulador para ejercicios de implementación de instrucciones y mejoras a la arquitectura.

### Entrevistas con docentes de la carrera:
El primer perfil de usuario del simulador de M32 son los docentes de la carrera de Ingeniería Civil en Informática. Para este grupo se entrevistaron tres docentes que han dado clases sobre esta arquitectura: el profesor Fernando Santolaya, la profesora Martita Muñoz, y el profesor Juan Carlos Figueroa.
Las necesidades determinadas en base a las entrevistas realizadas a los potenciales clientes son:

- Visualización del procesador M32 en un formato gráfico intuitivo basado en su diagrama arquitectónico.
- Iluminación de los componentes en ejecución para visualizar el flujo de datos e instrucciones.
- Representación detallada del autómata de la Unidad de Control para entender la decodificación de instrucciones.
- Posibilidad de probar módulos de forma independiente (ALU, Unidad de Control, etc.).
- Información sobre señales emitidas en cada ciclo de reloj para depuración y análisis.
- Visualización en pantalla de registros, señales y componentes activos en tiempo real.

Además, se identificaron las siguientes oportunidades:

- Uso del simulador como herramienta pedagógica para mostrar cómo se ejecutan instrucciones en el M32.
- Realización de ejercicios donde los estudiantes modifiquen la arquitectura del M32 para agregar nuevas instrucciones.
- Implementación de instrucciones complejas de 64 bits o variaciones en los accesos a memoria.

### Entrevistas con estudiantes de la carrera
El segundo perfil de usuario determinado durante las entrevistas sobre el simulador es el de los estudiantes de la carrera, que ya han aprobado la asignatura de Arquitectura de Computadores.
Las necesidades determinadas de este perfil son las siguientes:

- Interfaz interactiva que permita realizar conexiones y detectar errores automáticamente.
- Retroalimentación sobre los errores detectados en la simulación.
- Notas o guías contextuales que expliquen conceptos clave sobre el funcionamiento del M32.

Además, se identificaron las siguientes oportunidades:

- Uso del simulador como apoyo para resolver ejercicios de certámenes y evaluaciones previas.
- Experimentación con conceptos más avanzados, como simulaciones de autómatas a nivel ensamblador.

## 6. Conclusión

En conclusión, la implementación de este simulador busca mejorar la comprensión del procesador M32, proporcionando herramientas visuales interactivas y capacidades de depuración. A partir de las entrevistas con profesores y estudiantes, se han identificado una serie de requerimientos clave para diseñar una herramienta educativa efectiva.

¡Gracias por su atención!

