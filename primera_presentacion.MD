# Presentación del tema de anteproyecto de título

Buenas tardes estimados compañeros y profesores, me presento, soy Fabián Ferrada, estudiante de Ingeniería Civil en Informática. Mi tema de anteproyecto de título es el Diseño, Implementación y Simulación de la Arquitectura del Microprocesador M32 en Logisim Evolution

## Temario
El temario de esta presentación es el siguiente:

- (Comenzaré esta presentación dando a conocer el) Objetivo de la Presentación en la diapositiva X
- (En segundo lugar daré a conocer la) Introducción y Contexto del problema en la diapositiva X
- (Luego continuaré dando a conocer la) Problemática a Abordar en la diapositiva X
- (Continuaré con la presentación de) Sistemas o Estudios Similares en la diapositiva X
- (Y daré término a la presentación dando a conocer las) Conclusiones de la presentación en la diapositiva X

## Objetivo de la Presentación
(El objetivo general de esta presentación es) presentar el tema de anteproyecto de título sobre el diseño e implementación del microprocesador M32 en Logisim Evolution para justificar su relevancia y las problemáticas a abordar.

Los objetivos específicos de esta presentación son:

- Establecer la problemática a abordar del tema de anteproyecto título, con el objetivo de indicar el por qué del tema escogido
- Indicar sistemas y estudios similares al tema de anteproyecto de título, con el objetivo de tener referencias durante el desarrollo del proyecto

## Introducción y Contexto
(A modo de introducción) el estudio de arquitecturas de microprocesadores es un pilar fundamental en la formación de los estudiantes de la carrera de Ingeniería Civil en Informática en la Universidad del Bío-Bío. El M32 es una arquitectura de microprocesador docente que permite explorar conceptos como la organización de la CPU, la ejecución de instrucciones y la gestión del flujo de datos.

Sin embargo, actualmente no se cuenta con una implementación interactiva del microprocesador M32 en un simulador digital moderno.

Dado que el uso de herramientas de simulación como Logisim Evolution permite a los estudiantes y profesionales diseñar y probar arquitecturas sin necesidad de hardware real, una implementación del microprocesador M32 en Logisim Evolution es beneficioso ya que entrega a los estudiantes una herramienta interactiva para estudiar la previamente mencionada arquitectura.

## Problemática a Abordar
(La problemática a abordar es que, como se mencionó anteriormente) actualmente no se cuenta con una implementación interactiva del microprocesador M32 en un simulador digital moderno. Esto representa una limitación para quienes deseen estudiar y experimentar con dicha arquitectura dentro de esta plataforma.

Un ejemplo de ello es con la tarea de crear instrucciones nuevas para una arquitectura existente, lo que implica que el estudiante deba modificar el procesador existente. Esto último se complica debido a que no se puede depurar de manera fiable las modificaciones realizadas.

Los principales desafíos del proyecto incluyen:

- Diseño de la arquitectura en base a la documentación existente.
- Implementación del conjunto de instrucciones y estructura interna.
- Simulación del comportamiento del microprocesador dentro de Logisim Evolution.

## Sistemas o Estudios Similares
(Para considerar este proyecto, también se hizo una investigación de proyectos similares a este). Originalmente un grupo de tesistas de la Universidad del Bío-Bío realizó una implementación de M32 en el software de simulación Hades. Dicha implementación fue una herramienta valiosa en el estudio de la asignatura de Arquitectura de Computadores. Lamentablemente, la última versión de Hades data del año 2005, y está escrita con Java 1.4, por lo que ya presenta problemas de compatibilidad con los sistemas operativos modernos.

Durante la elaboración de propuesta del proyecto, se evaluaron diversos simuladores digitales disponibles en la actualidad, entre los que destacan:

- Proteus: Ofrece simulación avanzada de circuitos y microcontroladores, pero su enfoque comercial implica licencias costosas y su complejidad excede los requerimientos didácticos del proyecto.
- Multisim: Proporciona herramientas robustas para diseño y simulación de circuitos, sin embargo y al igual que la opción anterior, tiene un enfoque comercial lo que implica pagar una licencia.
- CircuitVerse: Simulador online con interfaz moderna. A pesar de su portabilidad (debido a que es un sitio web), puede volverse inestable al estar ejecutándose dentro de un navegador.
- Digital: Alternativa a Logisim con características modernas, pero con una comunidad más reducida y menos recursos didácticos disponibles. De manera adicional, su comunidad ha criticado su nombre debido a que dificulta su búsqueda en motores de búsqueda (valga la redundancia).

Se seleccionó Logisim Evolution debido a sus ventajas específicas para este proyecto:

- Es de código abierto y gratuito, permitiendo acceso universal a estudiantes y docentes.
- Ofrece una interfaz intuitiva especialmente diseñada para fines educativos.
- Cuenta con una comunidad activa y abundante documentación.
- Permite la simulación de componentes complejos manteniendo una visualización clara del flujo de datos.
- Posibilita la implementación modular de la arquitectura completa del M32.
- Facilita la integración con material didáctico existente en la universidad.

De manera adicional, también se destacan algunas referencias a proyectos similares a éste tales como:

- Implementaciones previas en Logisim de otros microprocesadores: Ya que existen otros microprocesadores implementados en Logisim Evolution, como el MIPS de 32 bits o modelos simplificados de procesadores x86.
- Simuladores externos: tales como herramientas como MARIE.js o Ripes permiten estudiar la ejecución de instrucciones en microprocesadores educativos.
- Proyectos de investigación: de algunas universidades, las cuales han desarrollado microprocesadores educativos con fines didácticos, proporcionando bases para este tipo de implementaciones.

## Conclusiones
(A modo de conclusión), este proyecto contribuirá al estudio de la arquitectura del M32 dentro de Logisim Evolution, facilitando la enseñanza y el aprendizaje de conceptos de diseño de microprocesadores.

La implementación del M32 en Logisim Evolution permitirá a los estudiantes experimentar con una arquitectura educativa sin necesidad de recurrir a técnicas de ejecución en papel. Específicamente, los estudiantes podrán añadir instrucciones nuevas a la arquitectura con mayor facilidad, e incluso añadir técnicas de optimización de ejecución de instrucciones, lo que a largo plazo podría añadir una ventana a estudiar tópicos más avanzados dentro de la asignatura.

Este anteproyecto establece las bases para el desarrollo y validación de un modelo funcional del M32 en un entorno de simulación accesible y ampliamente utilizado.
