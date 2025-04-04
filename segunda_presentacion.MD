# Guion de Presentación: Simulador del Microprocesador M32

## 1. Introducción

Buenas tardes estimados compañeros y profesores. Hoy presentaré los temas relacionados a la organización, problemas y resultados de la primera reunión con los clientes, respecto a un simulador para el microprocesador M32 en la herramienta Logisim Evolution, en el contexto de la asignatura de Arquitectura de Computadores en la Universidad del Bío-Bío.

El temario de la presentación será el siguiente:
- Primero, presentaré la Descripción de la Organización en la diapositiva X
- Luego, presentaré la Descripción del Problema en la diapositiva X
- Con eso en mente, presentaré los Resultados de la Primera Reunión con los clientes en la diapositiva X
- Y finalmente presentaré la conclusión en la diapositiva X

## 2. Descripción de la Organización

La organización que hará uso de esta herramienta es la Universidad del Bío-Bío. La Universidad del Bío-Bío es una institución que ofrece una serie de carreras orientadas a la tecnología, dentro de las cuales está la carrera de Ingeniería Civil en Informática. Otra carrera que destaca dentro de este ámbito es la carrera de Ingeniería de Ejecución en Computación e Informática, en la sede Concepción.

Dentro de la carrera de Ingeniería Civil en Informática, la asignatura de Arquitectura de Computadores es la que se enfoca en entregar el conocimiento necesario para comprender los principios del diseño y operación de procesadores.

El microprocesador M32 es un procesador docente utilizado en esta asignatura para enseñar la estructura y el comportamiento de una arquitectura de CPU. Actualmente, se dispone de una versión previa implementada en el simulador Hades, pero este al encontrarse desactualizado (principalmente debido a que está implementado en Java 1.4), supone una limitación a la hora de aprender sobre lo ya mencionado.

Se recalca que este proyecto busca proporcionar una herramienta visual e interactiva para el aprendizaje de los conceptos clave del procesador M32, destacando su funcionamiento interno y la interacción entre sus componentes. Por lo tanto, este nuevo simulador buscará ser una herramienta didáctica más completa, permitiendo visualizar el paso de datos, la ejecución de instrucciones y el funcionamiento interno de la Unidad de Control.

## 3. Descripción del Problema

El problema principal es la necesidad de una implementación mejorada del M32 que ofrezca una visualización clara del funcionamiento del procesador. Si bien Hades ofrece una implementación funcional, el hecho de que no sea compatible con los sistemas operativos modernos significa un problema, más que una ayuda.

Por ello, el objetivo del proyecto es re-implementar en un nuevo simulador (Logisim Evolution en nuestro caso) que no solo replique la funcionalidad del M32 a nivel de componente, sino que también brinde una experiencia educativa enriquecida, con elementos visuales y capacidades de depuración.

## 4. Resultados de la Primera Reunión con el Cliente

Para comprender mejor las necesidades y expectativas de los usuarios finales, se llevaron a cabo entrevistas con profesores y estudiantes de la carrera. Estos son los puntos clave obtenidos:

- La visualización del flujo de datos, que necesita una representación visual del paso de los datos a través de los buses y componentes del procesador.
- El estado del autómata del M32, el cual resulta fundamental para mostrar cómo la Unidad de Control procesa y decodifica las instrucciones.
- La depuración modular, ya que los profesores desean la capacidad de probar los módulos del procesador por separado.
- Los registros de estados y señales, ya que es necesario mostrar las señales emitidas en cada ciclo de reloj, los valores de los registros y la memoria.
- Y finalmente, la interactividad y asistencia, donde se requiere un mecanismo de validación de conexiones, un tutorial y guías de uso.

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
- Implementación de nuevas instrucciones de 64 bits o variaciones en los accesos a memoria.

### Entrevista con estudiantes de la carrera
El segundo perfil de usuario determinado durante las entrevistas sobre el simulador es el de los estudiantes de la carrera. Se destaca que los estudiantes entrevistados ya han aprobado la asignatura de Arquitectura de Computadores.
Las necesidades determinadas de este perfil son las siguientes:
- Interfaz interactiva que permita realizar conexiones y detectar errores automáticamente.
- Mensajes de retroalimentación sobre los errores detectados en la simulación.
- Notas o guías contextuales que expliquen conceptos clave sobre el funcionamiento del M32.
- Un tutorial o sistema de ayuda integrado para facilitar el aprendizaje del simulador.

Además, se identificaron las siguientes oportunidades:
- Uso del simulador como apoyo para resolver ejercicios de certámenes y evaluaciones previas.
- Experimentación con conceptos más avanzados, como simulaciones de máquinas de Turing (autómatas) dentro del entorno del M32.

## 6. Conclusión

En resumen, la implementación de este simulador busca mejorar la comprensión del procesador M32, proporcionando herramientas visuales interactivas y capacidades de depuración. A partir de las entrevistas con profesores y estudiantes, se han identificado una serie de requerimientos clave para diseñar una herramienta educativa efectiva.

Los siguientes pasos consisten en continuar analizando el panorama de los clientes, y del ámbito en el cual el proyecto se llevará a cabo.

¡Gracias por su atención!

