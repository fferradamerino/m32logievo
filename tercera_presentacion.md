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

(Describir el flujo que tengo en el BPMN)

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

(flujo optimizado acá)

Con esto se logra crear una forma de enseñanza consistente y con feedback mejorado, lo que lleva a una mejor forma de aprendizaje para el estudiante.

# Características de la solución: modificaciones en tiempo real
Con esta herramienta, añadir una instrucción como MULT (multiplicación) sería un proceso tangible:

- El estudiante modificaría el autómata de la Unidad de Control para incluir los estados necesarios.
- Logisim mostraría errores si hay conflictos en las señales.
- Al simular, vería cómo la ALU usa registros temporales para calcular el resultado, paso a paso.

# Características de la solución: Guías e información dentro del simulador

# Integración con Material Didáctico Existente
La solución se alinea con los recursos actuales de la universidad:

- Las guías de laboratorio se actualizarán con ejercicios prácticos en Logisim.
- Los profesores podrán crear demostraciones en vivo, como ejecutar una instrucción ciclo por ciclo.
- Los estudiantes tendrán acceso a plantillas modulares para experimentar con optimizaciones como pipelining."

# Conclusiones
En conclusión, este proyecto resolverá las limitaciones actuales mediante:

- La interactividad de la solución, permitiendo experimentación práctica sin restricciones técnicas.
- La claridad pedagógica, ya que los conceptos abstractos se vuelven tangibles con visualizaciones dinámicas.
- Y la escalabilidad, donde la modularidad del diseño facilitará añadir características avanzadas en el futuro.

Esta herramienta no solo modernizará la enseñanza del M32, sino que sentará las bases para explorar temas como cachés o arquitecturas superescalares en cursos posteriores.

# Agradecimientos
¡Muchas gracias por su atención!
