# Guión Presentación 4: Objetivos del proyecto, planificación de requerimientos y primeros requerimientos de software

Buenas tardes estimados compañeros y profesores. Hoy presentaré los temas relacionados a los objetivos del proyecto, planificación de requerimientos y primeros requerimientos de software, respecto al tema de Diseño, Implementación y Simulación de la Arquitectura del Microprocesador M32 en Logisim Evolution

# Temario
El temario de esta presentación es el siguiente:

- Primero, presentaré los objetivos de la presentación
- Luego presentaré los objetivos del proyecto
- Con ello en mente presentaré los primeros requerimientos de software
- Continuaré presentando la planificación de requerimientos
- Y finalmente presentaré las conclusiones de esta presentación

# Objetivos de la presentación
Los objetivos de esta presentación son los siguientes:

- Definir los objetivos del proyecto (tanto el objetivo general como los específicos)
- Presentar los primeros requerimientos de software del proyecto
- Entregar la planificación de requerimientos del proyecto

# Objetivos del proyecto
El objetivo general del proyecto se establece como:

- Diseñar e implementar una versión funcional del procesador M32 en Logisim Evolution, que incluya capacidades de depuración y documentación interactiva, con el fin de apoyar la enseñanza de arquitectura de computadores mediante una herramienta visual e intuitiva.

Para lograr este objetivo, se establecen los siguientes objetivos generales:

- Implementar todos los módulos funcionales del microprocesador M32 en Logisim Evolution, respetando su arquitectura original.
- Incorporar documentación embebida en la herramienta que describa el propósito y funcionamiento de cada componente.
- Facilitar la visualización de estados de registros y componentes dentro del procesador M32 en Logisim Evolution
- Validar el diseño a través de pruebas de usabilidad con docentes o estudiantes de la asignatura.
- Generar ejemplos de programas que puedan correr en la arquitectura M32, demostrando su funcionalidad educativa.

Toda esta información responde a lo que ya se ha venido presentando previamente: la necesidad de contar con herramientas didácticas interactivas que permitan a los estudiantes visualizar el funcionamiento interno de un microprocesador.

# Primeros requerimientos de software
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

# Planificación de requerimientos
La planificación se basa en una metodología incremental, donde se prioriza primero una implementación funcional básica del procesador M32, para luego iterar en mejoras de usabilidad, documentación y validación por medio de pruebas. Esto permite ajustar el desarrollo en función del feedback recogido durante el proceso.

Para el desarrollo del proyecto se propone la siguiente planificación:
- Semana 1 a 4: Implementación de M32 en Logisim Evolution
- Semana 5: Primeras pruebas de usabilidad
- Semana 6: Correcciones a la version inicial
- Semana 7: Documentación de componentes
- Semana 8: Segunda prueba de usabilidad
- Semana 9: Correcciones a la segunda versión
- Semana 10: Documentación del automata de la unidad de control
- Semana 11: Última prueba de usabilidad
- Semana 12: Correcciones finales
- Semana 13 a 16: Escritura de programas de ejemplo para M32
- Semana 17 a 20: Documentación del proyecto (informe final)

Se destaca que las pruebas de usabilidad consistirán en entregar la herramienta a estudiantes y docentes del semestre actual, observando su interacción con la interfaz, midiendo tiempos de comprensión, y recolectando feedback durante la actividad.

# Conclusiones
En conclusión, el proyecto busca generar una herramienta didáctica concreta que permita mejorar el proceso de enseñanza-aprendizaje en la asignatura de Arquitectura de Computadores.
- A través de la implementación y documentación del procesador M32 en Logisim Evolution, se pretende facilitar la comprensión de conceptos clave como el flujo de datos, la ejecución de instrucciones, y el comportamiento de la unidad de control.
- La planificación detallada y el levantamiento de requerimientos permiten asegurar un desarrollo controlado y alineado con los tiempos académicos.
- Finalmente, se espera que este proyecto no solo beneficie al curso actual, sino que quede como un recurso reutilizable para futuras generaciones.

Se destaca que el éxito del proyecto se medirá por la claridad de la documentación del procesador, y la recepción positiva en las pruebas de usabilidad.
