# Guión de presentación frente a la comisión

Buenas tardes estimados compañeros y profesores. Hoy presentaré los temas relacionados al tema de Diseño, Implementación y Simulación de la Arquitectura del Microprocesador M32 en Logisim Evolution

## Temario

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

## 1. Introducción a la problemática (Contexto)

El estudio de arquitecturas de microprocesadores es un pilar fundamental en la formación de los estudiantes de la carrera de Ingeniería Civil en Informática en la Universidad del Bío-Bío. El M32 es una arquitectura de microprocesador docente que permite explorar conceptos como la organización de la CPU, la ejecución de instrucciones y la gestión del flujo de datos.

Para el desarrollo de este proyecto se utilizará (como ya se ha mencionado previamente) el procesador M32 del profesor Luis Mateu, académico de la Universidad de Chile.

## 2. Descripción del Problema

El problema principal es la necesidad de una implementación mejorada del M32 que ofrezca una visualización clara del funcionamiento del procesador. Específicamente, se busca eliminar el problema de tener que trabajar a lápiz y papel a la hora de tener que resolver ejercicios de arquitecturas de procesadores, lo cual presenta una oportunidad de mejorar el rendimiento de los estudiantes de la asignatura de Arquitectura de computadores.

Además, tópicos tales como la microprogramación, pipelining avanzado o incluso memorias caché, no alcanzan a ser repasados lo que deja a los estudiantes con un nivel de conocimiento incompleto respecto al tópico de Arquitectura de Computadores.

### Limitaciones actuales

Existe una implementación funcional de M32 en Hades realizada por estudiantes tesistas en el año 2004. Lamentablemente, Hades no es compatible con sistemas operativos modernos, lo que representa una limitación considerable. Por lo tanto, se requiere una solución actualizada y compatible con tecnologías actuales.

### Toma de requerimientos

Se realizaron entrevistas a tres docentes; Fernando Santolaya, Martita Muñoz y Juan Carlos Figueroa y a cinco estudiantes de manera presencial. Con ello, se definieron dos perfiles clave de usuario: docentes y estudiantes de Arquitectura de Computadores. Ambos perfiles concuerdan en la necesidad de visualizar el estado de registros y flujo de datos del procesador, y la necesidad de un simulador para ejercicios de implementación de instrucciones y mejoras a la arquitectura.

### Necesidades identificadas

Considerando entonces lo mencionado previamente, se determinaron las siguientes necesidades:

- Visualización del procesador M32.
- Posibilidad de probar módulos de forma independiente (ALU, Unidad de Control, etc).
- Visualización en pantalla de registros, señales y componentes activos en tiempo real.
- Retroalimentación sobre errores detectados en la simulación.
- Notas o guías contextuales que explican conceptos clave sobre el funcionamiento del M32.

## 3. Proceso de Negocio de la Situación Actual

El flujo de trabajo, representado a través de un BPMN, suele revelar, como se puede observar, la inconsistencia entre el profesor de la asignatura y de laboratorio, ya que en clases teóricas se estudia M32, y en clases prácticas se estudian la arquitectura 8086.

## 4. Requerimientos

### Objetivos del proyecto
El objetivo general del proyecto se establece como:

- Diseñar e implementar una versión funcional del procesador M32 en Logisim Evolution, que incluya capacidades de depuración y documentación interactiva, con el fin de apoyar la enseñanza de arquitectura de computadores mediante una herramienta visual e intuitiva.

Los objetivos específicos del proyecto son los siguientes:

- Implementar todos los módulos funcionales del microprocesador M32 en Logisim Evolution, respetando su arquitectura original.
- Incorporar documentación embebida en la herramienta que describa el propósito y funcionamiento de cada componente.
- Facilitar la visualización de estados de registros y componentes dentro del procesador M32 en Logisim Evolution
- Validar el diseño a través de pruebas de usabilidad con docentes o estudiantes de la asignatura.
- Generar ejemplos de programas que puedan correr en la arquitectura M32, demostrando su funcionalidad educativa.

### Requerimientos funcionales y no funcionales
Los requerimientos de este proyecto, al menos los que se preveen durante esta etapa son los siguientes. Primero, tenemos los requisitos funcionales:

- El sistema debe permitir simular la ejecución de instrucciones en la arquitectura M32.
- El usuario debe poder observar el estado de los registros y señales de control en tiempo real.
- Cada módulo del procesador debe tener una descripción accesible mediante etiquetas.
- La herramienta debe permitir cargar y ejecutar programas simples escritos para M32.
- Cada componente (Registros, ALU, ABI, etc) debe construirse con diseño modular

Adicionalmente, tenemos una serie de requisitos no funcionales:

- La interfaz del circuito en Logisim Evolution debe seguir un estándar visual claro y organizado (alineación, colores, etiquetas), que es un requerimiento relacionado con la usabilidad.
- Toda la documentación debe estar escrita en un lenguaje técnico comprensible para estudiantes universitarios, que es también del tipo usabilidad.
- El diseño debe seguir un enfoque modular, permitiendo aislar componentes para pruebas individuales, que es del tipo escalabilidad (en el sentido que facilita la creación y modificación dentro de este)
- La implementación debe ser portable y funcionar sin necesidad de instalación adicional más allá de Logisim Evolution, que es del tipo portabilidad.

## 5. Ambiente de ingeniería de software

### Metodología de desarrollo

Para el desarrollo del proyecto se ha seleccionado la metodología XP (Extreme Programming), debido a su enfoque ágil, iterativo y flexible, ideal para adaptarse a los posibles cambios y mejoras que puedan surgir durante el diseño e implementación del microprocesador.

Además, fomenta la integración continua de nuevas funcionalidades, la refactorización constante, y el uso de metáforas que faciliten la comunicación técnica con el cliente. Todo esto bajo un ritmo de trabajo saludable, priorizando siempre la motivación personal.

### Aplicación de la metodología del proyecto
La aplicación de la metodología del proyecto se basa en una metodología incremental, donde se prioriza primero una implementación funcional básica del procesador M32, para luego iterar en mejoras de usabilidad, documentación y validación por medio de pruebas. Esto permite ajustar el desarrollo en función del feedback recogido durante el proceso.

Dentro de la planificación se destacan:

- Semana 1 a 4: Implementación de M32 en Logisim Evolution
- Semana 5: Primeras pruebas de usabilidad
- Semana 10: Documentación del automata de la unidad de control
- Semana 13 a 16: Escritura de programas de ejemplo para M32
- Semana 17 a 20: Documentación del proyecto (informe final)

### Conjunto de tecnologías a utilizar

Para este proyecto se utilizarán las siguientes tecnologías de desarrollo:

- Logisim Evolution 3.9.0: Simulador de circuitos digitales
- Python 3.x: Automatización de generación de binarios y programas de prueba
- Java 21: Posible desarrollo de plugins o extensiones personalizadas

Además se utilizarán las siguientes herramientas de desarrollo:

- Visual Studio Code y/o Vim, en sus versiones 1.99 y 9.1 respectivamente
- Makefile para facilitar la automatización de tareas de compilación (ensamblado de binarios).

## 6. Estudios de factibilidad

### Estudio técnico del proyecto
El único requisito destacable es contar con acceso a una computadora con Logisim Evolution instalado, lo cual es bastante accesible, dado que la herramienta es gratuita y fácil de instalar, junto a un equipo con un procesador de gama baja y 2 GB de RAM.

### Estudio económico del proyecto

A continuación, se presentarán los cálculos del Valor Actual Neto.

### Estudio económico: Valor Actual Neto

El desarrollo del proyecto del procesador M32 considera la contratación de un ingeniero desarrollador de hardware con una remuneración estimada de 1.000.000 de pesos mensuales por 4 meses de trabajo, equivalente a 4.000.000 de pesos. Sumado también 750.000 pesos en matención del sistema.
En base a estas suposiciones económica, el flujo de caja se describe de esta forma:

- Durante el año 0, solo se gastan 4.000.000 de pesos, que vendría siendo la contratación del desarrollador de hardware
- Durante el año 1 y 2, se reciben los primeros beneficios de 3.701.876 de pesos, junto a los 750.000 en mantención
- Durante los próximos años, esta tendencia se mantiene, compensando la inversión inicial y generando befenicios

Considerando una tasa de descuento del 10%, se obtiene un Valor Actual Neto (VAN) de 7.189.934 pesos, justificando la viabilidad económica del proyecto.

## 7. Casos de uso del simulador del microprocesador M32

### Casos de uso: Introducción a los actores

Tal como se indicó en las presentaciones anteriores, se identificaron dos perfiles principales de usuarios: docentes (con experiencia enseñando en la asignatura) y estudiantes de la carrera de Ingeniería Civil en Informática (que están cursando, o ya cursaron, la asignatura de Arquitectura de Computadores).

En esta diapositiva se puede evidenciar el diagrama de casos de uso, el cual representa la relación de los actores con sus respectivos casos de uso.

### Casos de uso: definición

En base en los requerimientos funcionales del proyecto, los casos de uso principales fueron estructurados de la siguiente manera:

#### Primer caso de uso

Es simular instrucciones M32.

#### Segundo caso de uso

Es visualizar el estado de registros y señales en tiempo real. Esta relacion se da ya que simular instrucciones implica visualizar el estado del sistema.

#### Tercer caso de uso

Es consultar descripciones de módulos del procesador

#### Cuarto caso de uso

- Cargar y ejecutar programas escritos en ensamblador M32. Este caso de uso incluye al caso 1 dado que al ejecutar un programa, internamente se simulan instrucciones.

Se destaca adicionalmente que el primer caso de uso incluye al segundo caso de uso en su funcionamiento.

### Casos de uso: participación de los actores

Ambos actores pueden acceder a la mayoría de los casos de uso, con ligeras diferencias según su rol.

### Mockups

En el mockup podemos ver una representación general del circuito en Logisim Evolution, junto a componentes que ayudan a seguir el flujo de señal. Adicionalmente, este mockup representa un avance ya concreto en el desarrollo del M32.

## Conclusiones

A modo de conclusión, se puede indicar que:

- La implementación del simulador busca mejorar la comprensión del procesador M32 proporcionando herramientas interactivas y de depuración
- A partir de las entrevistas se identificaron requerimientos clave para una herramienta educativa efectiva
- El proyecto generará una herramienta didáctica concreta para mejorar el proceso de enseñanza-aprendizaje
- Facilitará la comprensión de conceptos clave: flujo de datos, ejecución de instrucciones, comportamiento de la unidad de control

¡Muchas gracias por su atención!
