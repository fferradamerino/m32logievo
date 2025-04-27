# Guión Presentación 3: Proceso de Negocio Actual y Solución Propuesta

Buenas tardes estimados compañeros y profesores. Hoy presentaré los temas relacionados al proceso de negocio actual y solución propuesta, respecto al tema de Diseño, Implementación y Simulación de la Arquitectura del Microprocesador M32 en Logisim Evolution

# Temario
El temario de esta presentación es el siguiente:

- Primero, presentaré los objetivos de la presentación
- Seguido de ello, presentaré el proceso de negocio de la situación actual
- Luego, presentaré la solución propuesta a la problemática analizada, el flujo de trabajo optimizado post-solución, y las características de la solución.
- Y finalmente presentaré las conclusiones de esta presentación

# Objetivos de la presentación
Los objetivos de esta presentación son:

- Presentar el flujo actual de enseñanza de la arquitectura M32 en la asignatura.
- Presentar el flujo optimizado de enseñanza de la arquitectura M32 en la asignatura
- Mostrar cómo la solución propuesta optimiza el proceso de aprendizaje.

# Proceso de Negocio de la Situación Actual
Durante el desarrollo de las clases de Arquitectura de Computadores, específicamente en la unidad de arquitectura de procesadores, se suele seguir el siguiente flujo:

Tenemos tres actores: el docente de las clases teóricas, el docente de las clases prácticas (o laboratorios), y el estudiante de la asignatura. Los dos docentes trabajan de manera paralela entregando materia y ejercicios a los estudiantes.
El docente de la asignatura, por un lado, empieza la unidad entregando materia sobre el flujo de instrucciones dentro del procesador, y los ciclos necesarios para su ejecución. Posterior a esto el docente hace entrega de ejercicios al estudiante para que este pueda aplicar la materia estudiada. En paralelo, el estudiante toma apuntes de la materia, y resuelve los ejercicios propuestos, aunque sin feedback alguno.
Sumado a esto, el docente de laboratorios, al comenzar la unidad, entrega materia relacionada a la programación en ensamblador de la arquitectura 8086. Esto es debido a que la arquitectura 8086 si tiene implementaciones tangibles (tales como Emu8086) a diferencia de M32, por lo que es ideal para clases prácticas. Igual que en el flujo anterior, el estudiante toma apuntes y resuelve los ejercicios propuestos por parte de los profesores.

# Limitaciones del Método Actual
Basándonos en lo que dice el flujo descrito previamente, se puede destacar que el proceso de enseñanza entre las clases de laboratorio y teóricas no es consistente debido, precisamente, a la falta de herramientas para colocar en práctica los contenidos de M32.

Adicionalmente, esto se complementa con los problemas que ya se han venido describiendo previamente:

- El primero es la falta de interacción, ya que no se puede depurar errores en tiempo real.
- Y luego está el aprendizaje abstracto, ya que los estudiantes no ven cómo sus modificaciones afectan el comportamiento real del procesador.

# Solución Propuesta
La solución propuesta es la de implementar el procesador M32 en Logisim Evolution con estas características:

- Visualización interactiva del flujo de datos. Esto implica que los componentes se iluminan durante la ejecución, mostrando cómo se mueven los datos entre registros, ALU y memoria.
- Autómata de la Unidad de Control editable. Con ello los usuarios pueden modificar microinstrucciones y ver inmediatamente cómo afectan la decodificación.
- Módulos independientes probables. Por ejemplo, la ALU puede testearse por separado antes de integrarla al diseño global.
- Depuración en tiempo real, donde se muestre el estado de registros, señales de control y el contador de programa.

# Solución Propuesta: flujo optimizado
El flujo optimizado que se propone después de haber implementado M32 en Logisim Evolution es el siguiente:

El docente de la asignatura, por un lado, empieza la unidad entregando materia sobre el flujo de instrucciones dentro del procesador, y los ciclos necesarios para su ejecución. Posterior a esto el docente hace entrega de ejercicios al estudiante para que este pueda aplicar la materia estudiada. En paralelo, el estudiante toma apuntes de la materia, y resuelve los ejercicios propuestos, ahora con el feedback de Logisim Evolution.
Adicionalmente, el docente de laboratorios entregará ejercicios relacionados a la arquitectura de M32, específicamente relacionados a la implementación de instrucciones en M32. Igual que en el flujo anterior, el estudiante toma apuntes y resuelve los ejercicios propuestos por parte de los profesores.

Con esto se logra crear una forma de enseñanza consistente y con feedback mejorado, lo que lleva a una mejor forma de aprendizaje para el estudiante. Adicionalmente, logra crear consistencia entre la materia enseñada en clases y las prácticas de laboratorio.

# Características de la solución: modificaciones en tiempo real
Con esta herramienta, añadir una instrucción como MULT (multiplicación) sería un proceso tangible donde el estudiante modificaría el autómata de la Unidad de Control para incluir los estados necesarios. En el caso de inconsistencias, Logisim mostraría errores si hay conflictos en las señales. Al simular, se vería cómo la ALU usa registros temporales para calcular el resultado, paso a paso.

# Características de la solución: Guías e información dentro del simulador
Una de las características principales de la solución es la incorporación de guías visuales integradas en la simulación. Cada componente del procesador, como registros, unidades de control o memorias, incluye una etiqueta informativa en la esquina superior izquierda.
Estas etiquetas explican de forma resumida la función del chip, permitiendo que cualquier persona que interactúe con el simulador, ya sea un estudiante, docente o evaluador, pueda comprender rápidamente el propósito de cada módulo sin necesidad de recurrir a documentación externa.

# Integración con Material Didáctico Existente
La solución se alinea con los recursos actuales de la universidad, donde las guías de laboratorio se actualizarán con ejercicios prácticos en Logisim. Adicionalmente, los profesores podrán crear demostraciones en vivo, como ejecutar una instrucción ciclo por ciclo. También, los estudiantes tendrán acceso a plantillas modulares para experimentar con optimizaciones como pipelining."

# Conclusiones
En conclusión, este proyecto resolverá las limitaciones actuales mediante:

- La interactividad de la solución, permitiendo experimentación práctica sin restricciones técnicas.
- La claridad pedagógica, ya que los conceptos abstractos se vuelven tangibles con visualizaciones dinámicas.
- Y la escalabilidad, donde la modularidad del diseño facilitará añadir características avanzadas en el futuro.

Esta herramienta no solo modernizará la enseñanza del M32, sino que sentará las bases para explorar temas como cachés o arquitecturas superescalares en cursos posteriores.

# Agradecimientos
¡Muchas gracias por su atención!
