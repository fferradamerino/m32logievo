# Guión de presentación frente a la comisión

Buenas tardes estimados compañeros y profesores. Hoy presentaré los temas relacionados al tema de Diseño, Implementación y Simulación de la Arquitectura del Microprocesador M32 en Logisim Evolution

# Temario

El temario de la presentación será el siguiente:

- Primero, presentaré los objetivos de la presentación
- En segundo lugar, presentaré la introducción a la problemática
- Luego, presentaré la descripción del problema
- Seguido de eso, presentaré el proceso de Negocio de la Situación Actual
- Con ello, presentaré los requerimientos
- Posteriormente, presentaré el ambiente de ingeniería de software
- También, presentaré los estudios de factibilidad
- Luego, presentaré el modelo entidad relación
- Seguido de eso, presentaré los casos de uso del simulador del microprocesador M32
- Finalmente, daremos a conocer las conclusiones

## Objetivos de la presentación
Los objetivos de esta presentación son:

- Presentar la problemática y requerimientos del proyecto
- Presentar la información elaborada durante la asignatura de Anteproyecto sobre el tema a exponer

## 1. Introducción a la problemática

El estudio de arquitecturas de microprocesadores es un pilar fundamental en la formación de los estudiantes de la carrera de Ingeniería Civil en Informática en la Universidad del Bío-Bío. El M32 es una arquitectura de microprocesador docente que permite explorar conceptos como la organización de la CPU, la ejecución de instrucciones y la gestión del flujo de datos.

Dado que el uso de herramientas de simulación como Logisim Evolution permite a los estudiantes y profesionales diseñar y probar arquitecturas sin necesidad de hardware real, una implementación del microprocesador M32 en Logisim Evolution es beneficioso ya que entrega a los estudiantes una herramienta interactiva para estudiar la previamente mencionada arquitectura.

## 2. Descripción del Problema

El problema principal es la necesidad de una implementación mejorada del M32 que ofrezca una visualización clara del funcionamiento del procesador. Específicamente, se busca eliminar el problema de tener que trabajar a lápiz y papel a la hora de tener que resolver ejercicios de arquitecturas de procesadores, lo cual presenta una oportunidad de mejorar el rendimiento de los estudiantes de la asignatura de Arquitectura de computadores.

De manera adicional, el contenido relacionado a algunos tópicos en Arquitectura de Computadores no se pueden estudiar con la profundidad que debería tener. Tópicos tales como la microprogramación, pipelining avanzado o incluso memorias caché, no alcanzan a ser repasados lo que deja a la Universidad del Bío-Bío en una situación de desventaja, comparado con otras universidades del país.

Tal como se mencionó anteriormente, si bien ya existe una implementación funcional de M32 en Hades realizada por estudiantes tesistas de Ingeniería Civil en Informática, el hecho de que Hades no sea compatible con los sistemas operativos modernos significa una limitación considerable.

### Resultados de la Primera Reunión con el Cliente

Para poder desarrollar una visión clara de las necesidades de los usuarios de este simulador, se llevaron a cabo entrevistas con profesores y estudiantes de la carrera. Con esta información, se logró definir dos perfiles claves de usuario: docentes y estudiantes de Arquitectura de Computadores.

Ambos perfiles concuerdan con la necesidad de tener una forma de visualizar el estado de los registros y del flujo de datos del procesador. De manera adicional, ambos perfiles también sugieren el uso del simulador para ejercicios de implementación de instrucciones y mejoras a la arquitectura.

#### Entrevistas con docentes de la carrera
El primer perfil de usuario del simulador de M32 son los docentes de la carrera de Ingeniería Civil en Informática. Para este grupo se entrevistaron tres docentes que han dado clases sobre esta arquitectura: el profesor Fernando Santolaya, la profesora Martita Muñoz, y el profesor Juan Carlos Figueroa.
Las necesidades determinadas en base a las entrevistas realizadas a los potenciales clientes son:

- Visualización del procesador M32.
- Posibilidad de probar módulos de forma independiente (ALU, Unidad de Control, etc.).
- Visualización en pantalla de registros, señales y componentes activos en tiempo real.

#### Entrevistas con estudiantes de la carrera
El segundo perfil de usuario determinado durante las entrevistas sobre el simulador es el de los estudiantes de la carrera, que ya han aprobado la asignatura de Arquitectura de Computadores.
Las necesidades determinadas de este perfil son las siguientes:

- Retroalimentación sobre los errores detectados en la simulación.
- Notas o guías contextuales que expliquen conceptos clave sobre el funcionamiento del M32.

## 3. Proceso de Negocio de la Situación Actual

Se suele seguir el siguiente flujo:

Tenemos tres actores: el docente de las clases teóricas, el docente de las clases prácticas (o laboratorios), y el estudiante de la asignatura.
El docente de la asignatura, por un lado, empieza la unidad entregando material. Posterior a esto el docente hace entrega de ejercicios al estudiante para que este pueda aplicar la materia estudiada. El estudiante toma apuntes de la materia, y resuelve los ejercicios propuestos, aunque sin feedback alguno.
Sumado a esto, el docente de laboratorios, al comenzar la unidad, entrega materia relacionada a la programación en ensamblador de la arquitectura 8086.

## 4. Requerimientos

## Objetivos del proyecto
El objetivo general del proyecto se establece como:

- Diseñar e implementar una versión funcional del procesador M32 en Logisim Evolution, que incluya capacidades de depuración y documentación interactiva, con el fin de apoyar la enseñanza de arquitectura de computadores mediante una herramienta visual e intuitiva.

## Primeros requerimientos de software
Durante la etapa inicial, los requerimientos funcionales más críticos son la implementación de los componentes internos del procesador. Otros requerimientos, como la documentación embebida, se abordarán en etapas posteriores, una vez asegurada la funcionalidad básica.

Para ahondar más en lo que se acaba de mencionar, los requerimientos de este proyecto, al menos los que se preveen durante esta etapa son los siguientes. Primero, tenemos los requisitos funcionales:

- El sistema debe permitir simular la ejecución de instrucciones en la arquitectura M32.
- El usuario debe poder observar el estado de los registros y señales de control en tiempo real.
- Cada módulo del procesador debe tener una descripción accesible mediante etiquetas.
- La herramienta debe permitir cargar y ejecutar programas simples escritos para M32.

Adicionalmente, tenemos una serie de requisitos no funcionales:

- La interfaz del circuito en Logisim Evolution debe seguir un estándar visual claro y organizado (alineación, colores, etiquetas), que es un requerimiento relacionado con la usabilidad.
- Toda la documentación debe estar escrita en un lenguaje técnico comprensible para estudiantes universitarios, que es también del tipo usabilidad.
- El diseño debe seguir un enfoque modular, permitiendo aislar componentes para pruebas individuales, que es del tipo escalabilidad (en el sentido que facilita la creación y modificación dentro de este)
- La implementación debe ser portable y funcionar sin necesidad de instalación adicional más allá de Logisim Evolution, que es del tipo portabilidad.

## Planificación de requerimientos
La planificación se basa en una metodología incremental, donde se prioriza primero una implementación funcional básica del procesador M32, para luego iterar en mejoras de usabilidad, documentación y validación por medio de pruebas. Esto permite ajustar el desarrollo en función del feedback recogido durante el proceso.

Dentro de la planificación se destacan
- Semana 1 a 4: Implementación de M32 en Logisim Evolution
- Semana 5: Primeras pruebas de usabilidad
- Semana 10: Documentación del automata de la unidad de control
- Semana 13 a 16: Escritura de programas de ejemplo para M32
- Semana 17 a 20: Documentación del proyecto (informe final)

Se destaca que las pruebas de usabilidad consistirán en entregar la herramienta a estudiantes y docentes del semestre actual, observando su interacción con la interfaz, midiendo tiempos de comprensión, y recolectando feedback durante la actividad.

# 5. Ambiente de ingeniería de software

## Metodología escogida
Para el desarrollo del proyecto se ha seleccionado la metodología XP (Extreme Programming), debido a su enfoque ágil, iterativo y flexible, ideal para adaptarse a los posibles cambios y mejoras que puedan surgir durante el diseño e implementación del microprocesador.

Además, fomenta la integración continua de nuevas funcionalidades, la refactorización constante para mejorar el código, y el uso de metáforas que faciliten la comunicación técnica con el cliente. Todo esto bajo un ritmo de trabajo saludable, priorizando siempre la motivación del equipo. Gracias a estas prácticas, XP permite desarrollar proyectos más flexibles, colaborativos y enfocados en las necesidades reales del usuario.

# 6. Estudios de factibilidad

## Estudio técnico del proyecto
El único requisito destacable es contar con acceso a una computadora con Logisim Evolution instalado, lo cual es bastante accesible, dado que la herramienta es gratuita y fácil de instalar, junto a un equipo con un procesador de gama baja y 2 GB de RAM.

## Estudio económico del proyecto

A continuación, se presentarán los cálculos del Valor Actual Neto.

## Estudio económico: Valor Actual Neto

El desarrollo del proyecto del procesador M32 considera la contratación de un ingeniero desarrollador de hardware con una remuneración estimada de 1.000.000 de pesos mensuales durante sus primeros años de experiencia laboral. Se requiere una inversión de 4 meses de trabajo, lo que equivale a un costo total de 4.000.000 de pesos. Adicionalmente, se contempla un gasto mensual de 750.000 pesos para la corrección y mantenimiento del sistema. En cuanto al ahorro de tiempo para los profesionales involucrados, el docente de la asignatura trabaja 132 horas mensuales con un valor hora de 20.860 pesos, mientras que el ayudante trabaja 32 horas mensuales a 7.271 pesos la hora. Se proyecta un ahorro anual de 144 horas para el docente y 96 horas para el ayudante, lo que representa un ahorro total anual de 3.701.876 pesos. Considerando una tasa de descuento del 10%, se obtiene un Valor Actual Neto (VAN) de 7.189.934 pesos, justificando la viabilidad económica del proyecto.

# 7. Modelo entidad relación

En primer lugar, es importante señalar que este proyecto no contempla el uso de una base de datos relacional como sistema de persistencia principal, ya que el procesador M32 simulado en Logisim no almacena datos de usuarios en un sentido convencional.
Sin embargo, el modelo entidad-relación se reutiliza aquí como una herramienta conceptual que nos permite representar y modelar las interacciones de los usuarios con el simulador, así como la forma en que estas interacciones pueden generar datos útiles para evaluar y mejorar el sistema.
Dicho modelo considera elementos tales como los registros, el usuario, las microinstrucciones de la Control Unit, etcétera.

# 8. Casos de uso del simulador del microprocesador M32

## Casos de uso: Introducción a los actores

Tal como se indicó en las presentaciones anteriores, se identificaron dos perfiles principales de usuarios: docentes (con experiencia enseñando en la asignatura) y estudiantes de la carrera de Ingeniería Civil en Informática (que están cursando, o ya cursaron, la asignatura de Arquitectura de Computadores):

## Casos de uso: definición

En base en los requerimientos funcionales del proyecto, los casos de uso principales fueron estructurados de la siguiente manera:

### Primer caso de uso

Es simular instrucciones M32.

### Segundo caso de uso

Es visualizar el estado de registros y señales en tiempo real. Esta relacion se da ya que simular instrucciones implica visualizar el estado del sistema.

### Tercer caso

Es consultar descripciones de módulos del procesador

### Cuarto caso 

- Cargar y ejecutar programas escritos en ensamblador M32. Este caso de uso incluye al caso 1 dado que al ejecutar un programa, internamente se simulan instrucciones.

Se destaca adicionalmente que el primer caso de uso incluye al segundo caso de uso en su funcionamiento.

## Casos de uso: participación de los actores

Ambos actores pueden acceder a la mayoría de los casos de uso, con ligeras diferencias según su rol.

## Mockups

En el mockup podemos ver una representación general del circuito en Logisim Evolution, junto a componentes que ayudan a seguir el flujo de señal.

El mockup muestra cómo al abrir el circuito de un componente, aparece una etiqueta con información clara relacionada al AR.

## Conclusiones

A modo de conclusión, se puede indicar que:

- La implementación de este simulador busca mejorar la comprensión del procesador M32, proporcionando herramientas interactivas y depuración. A partir de las entrevistas con profesores y estudiantes, se han identificado una serie de requerimientos clave para diseñar una herramienta educativa efectiva.

- El proyecto busca generar una herramienta didáctica concreta que permita mejorar el proceso de enseñanza-aprendizaje en la asignatura de Arquitectura de Computadores, a través de la implementación y documentación del procesador M32 en Logisim Evolution, que pretende facilitar la comprensión de conceptos clave como el flujo de datos, la ejecución de instrucciones, y el comportamiento de la unidad de control.

¡Muchas gracias por su atención!
