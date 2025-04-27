# Guion de Presentación: Ambiente de Ingeniería de software (Metodología y tecnologías)

Buenas tardes estimados compañeros y profesores. Hoy presentaré los temas relacionados al ambiente de ingeniería de software (es decir, a la metodología y tecnologías utilizadas), respecto al tema de Diseño, Implementación y Simulación de la Arquitectura del Microprocesador M32 en Logisim Evolution.

# Temario
El temario de esta presentación es el siguiente:

- Primero, presentaré los objetivos de la presentación
- Seguido de ello, presentaré la metodología que se utilizará para trabajar
- Con lo anterior en mente, se procederá a describir la planificación de los sprints
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
Para el desarrollo del proyecto se ha seleccionado la metodología XP (Extreme Programming), debido a su enfoque ágil, iterativo y flexible, ideal para adaptarse a los posibles cambios y mejoras que puedan surgir durante el diseño e implementación del microprocesador.

La programación extrema, conocida como XP (Extreme Programming), es una metodología ágil de desarrollo de software que se centra en mejorar la calidad del producto y la capacidad de adaptarse a los cambios del cliente. Propone ciclos de trabajo cortos y sostenibles, donde se realizan reuniones semanales de planificación junto al cliente para definir y ajustar las funcionalidades prioritarias. Entre sus principios destacan:

- Las entregas frecuentes
- La simplicidad en el diseño
- La programación por parejas
- La propiedad colectiva del código
- El desarrollo orientado a pruebas.

Además, fomenta la integración continua de nuevas funcionalidades, la refactorización constante para mejorar el código, y el uso de metáforas que faciliten la comunicación técnica con el cliente. Todo esto bajo un ritmo de trabajo saludable, priorizando siempre la motivación del equipo. Gracias a estas prácticas, XP permite desarrollar proyectos más flexibles, colaborativos y enfocados en las necesidades reales del usuario.

# Planificación de los sprints
Se han planificado 11 sprints, distribuidos en un rango de 1 a 2 semanas de duración cada uno, de la siguiente manera:

- Sprint 1 (2 semanas): Implementación inicial del microprocesador M32 en Logisim Evolution.
- Sprint 2 (1 semana): Realización de las primeras pruebas de usabilidad sobre la implementación.
- Sprint 3 (1 semana): Corrección de errores detectados en la primera versión.
- Sprint 4 (1 semana): Documentación de los componentes diseñados.
- Sprint 5 (1 semana): Ejecución de una segunda ronda de pruebas de usabilidad.
- Sprint 6 (1 semana): Correcciones basadas en los resultados de la segunda prueba.
- Sprint 7 (1 semana): Documentación detallada del autómata de la unidad de control.
- Sprint 8 (1 semana): Tercera y última ronda de pruebas de usabilidad.
- Sprint 9 (1 semana): Aplicación de correcciones finales.
- Sprint 10 (2 semanas): Desarrollo de programas de ejemplo que validen el funcionamiento del M32.
- Sprint 11 (2 semanas): Elaboración del informe final de documentación del proyecto.

# Conjunto de tecnologías a utilizar
El conjunto de tecnologías seleccionadas para este proyecto busca soportar tanto la implementación como la automatización de tareas recurrentes, facilitando así el proceso de desarrollo y validación. Estas tecnologías son:

- Logisim Evolution (en su versión de Agosto 2024, 3.9.0): Utilizado como simulador de circuitos digitales, permitirá construir, probar y depurar la arquitectura del microprocesador M32 de manera visual e interactiva.
- Python (en su versión 3.x): Se utilizará para la creación de scripts de automatización, incluyendo:
 - La generación automática de programas de ejemplo en lenguaje ensamblador M32 y su conversión a formato binario ejecutable.
 - La conversión de programas escritos en un "micro-ensamblador" específico a formatos binarios que puedan ser cargados en la ROM de la unidad de control.
- Java (en su versión 21): Se considerará para el desarrollo de plugins o extensiones personalizados en Logisim Evolution, en caso de ser necesario para mejorar las capacidades del simulador o integrar funcionalidades específicas.

# Hardware necesario
Para llevar a cabo el proyecto de implementación y simulación del microprocesador M32, se requiere un hardware básico pero suficiente para ejecutar de manera fluida las herramientas seleccionadas. Aquí se destaca que la elección de herramientas mencionadas previamente tenían como objetivo, adicionalmente, el que sean multiplataforma. Los requisitos principales son:

- CPU de doble núcleo o superior (para este proyecto se está utilizando un equipo con un procesador Intel Pentium Silver N5000).
- Al menos 4 GB, aunque se recomienda 8 GB para trabajar cómodamente con múltiples aplicaciones abiertas (Logisim Evolution, Python, IDEs de desarrollo, entre otros).
- Un mínimo de 2GB de espacio disponible, considerando la instalación de Logisim, entornos de programación y almacenamiento de archivos de proyecto.
- Sistema Operativo Windows, Linux o macOS. Todas las herramientas utilizadas son multiplataforma.
- Resolución de pantalla mínima de 1280x720 para visualizar adecuadamente los circuitos, y acceso a internet para descargas y consultas técnicas.

# Conclusiones
En conclusión, respecto a la metodología y el conjunto de tecnologías seleccionadas para el proyecto, se concluye lo siguiente:

- La metodología XP (Extreme Programming) permitirá un desarrollo ágil y flexible, enfocado en la entrega continua de avances y en la rápida adaptación frente a cambios o problemas que puedan surgir durante el proceso de diseño e implementación.
- El conjunto de tecnologías elegido, Logisim Evolution, Python y Java, busca no solo facilitar la construcción y validación del microprocesador M32, sino también optimizar tareas de automatización y extender las capacidades del simulador si fuera necesario.

En conjunto, tanto la metodología como las tecnologías priorizan la flexibilidad, la iteración constante y la adaptabilidad, factores que son fundamentales para el éxito del proyecto en sus distintas etapas de desarrollo.

# Agradecimientos
¡Muchas gracias por su atención!
