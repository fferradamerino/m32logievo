# Guion de Presentación: Ambiente de Ingeniería de software (Metodología y tecnologías)

Buenas tardes estimados compañeros y profesores. Hoy presentaré los temas relacionados al ambiente de ingeniería de software (es decir, a la metodología y tecnologías utilizadas), respecto al tema de Diseño, Implementación y Simulación de la Arquitectura del Microprocesador M32 en Logisim Evolution.

# Temario
El temario de esta presentación es el siguiente:

- Primero, presentaré los objetivos de la presentación
- Seguido de ello, presentaré la metodología que se utilizará para trabajar
- Posterior a eso, presentaré el conjunto de tecnologías que se utilizarán en el proyecto
- Y finalmente presentaré las conclusiones de esta presentación

# Objetivos de la presentación
Los objetivos de esta presentación son:

A modo de objetivo general:

- Presentar el ambiente de ingeniería de software seleccionado para el desarrollo del proyecto

A modo de objetivos específicos:

- Presentar y justificar la metodología de trabajo escogida para el proyecto
- Presentar el conjunto de tecnologías a utilizar en la elaboración del proyecto

# Metodología escogida
- Metodología escogida: XP (programación extrema)
- Cantidad de sprints: 11 (1 a 2 semanas cada uno)
	- Sprint 1, 2 semanas: Implementación de M32 en Logisim Evolution
	- Sprint 2, 1 semana: Primeras pruebas de usabilidad
	- Sprint 3, 1 semana: Correcciones a la version inicial
	- Sprint 4, 1 semana: Documentación de componentes
	- Sprint 5, 1 semana: Segunda prueba de usabilidad
	- Sprint 6, 1 semana: Correcciones a la segunda versión
	- Sprint 7, 1 semana: Documentación del automata de la unidad de control
	- Sprint 8, 1 semana: Última prueba de usabilidad
	- Sprint 9, 1 semana: Correcciones finales
	- Sprint 10, 2 semana: Escritura de programas de ejemplo para M32
	- Sprint 11, 2 semana: Documentación del proyecto (informe final)

# Conjunto de tecnologías a utilizar
- Logisim como simulador
- Python para automatizar la creación de programas de ejemplo y la microprogramación (es decir, crear scripts de Python para convertir programas en ensamblador M32 a ejecutables binarios, o programas en "micro-ensamblador" a un programa binario que se pueda cargar en una ROM dentro de la Control Unit)
- Java para la creación de plugins, en el caso de que sea necesario

# Conclusiones
En conclusión, se puede indicar lo siguiente tanto de la metodología seleccionada, como del conjunto de tecnologías a utilizar:

- La metodología escogida prioriza el trabajo ágil a través del desarrollo de aquellos componentes y módulos que sean de prioridad en el momento.
- El conjunto de tecnologías seleccionado tiene como objetivo crear herramientas que faciliten la implementación de programas de ejemplo y de componentes del M32

Junto a ello, también se indica que ambos (tanto la metodología como el conjunto de tecnologías) priorizan la flexibilidad ante problemas y cambios durante el desarrollo.

# Agradecimientos
¡Muchas gracias por su atención!
