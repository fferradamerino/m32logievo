# Guión Presentación 3: Proceso de Negocio Actual y Solución Propuesta

Buenas tardes estimados compañeros y profesores. Hoy presentaré los temas relacionados al proceso de negocio actual y solución propuesta, respecto al tema de Diseño, Implementación y Simulación de la Arquitectura del Microprocesador M32 en Logisim Evolution

# Temario
El temario de esta presentación es el siguiente:

- Primero, presentaré los objetivos de la presentación
- Seguido de ello, presentaré el proceso de negocio de la situación actual
- Luego, presentaré la solución propuesta a la problemática analizada
- Y finalmente presentaré las conclusiones de esta presentación

# Objetivos de la presentación
Los objetivos de esta presentación son:

- Presentar el flujo actual de enseñanza de la arquitectura M32 en la asignatura.
- Mostrar cómo la solución propuesta optimiza el proceso de aprendizaje.

# Proceso de Negocio de la Situación Actual
Actualmente, los profesores siguen un flujo de tres etapas:

- Primero está la enseñanza del flujo de datos, donde se explican las instrucciones básicas del M32 usando un diagrama impreso en una hoja de papel.
- Luego se enseña a añadir capacidades a componentes, donde se trabaja en modificaciones teóricas, como ampliar la ALU o ajustar el banco de registros.
- Con esto ya en mente, se procede a enseñar la implementación de instrucciones nuevas, donde los estudiantes modifican el autómata de la Unidad de Control o las señales de control. 

Se destaca que todo este proceso es a lápiz y papel.

# Limitaciones del Método Actual
Este enfoque genera tres problemas críticos:

- El primero es la falta de interacción, ya que no se puede depurar errores en tiempo real.
- Y luego está el aprendizaje abstracto, ya que los estudiantes no ven cómo sus modificaciones afectan el comportamiento real del procesador.

# Solución Propuesta
La solución propuesta es la de implementar el procesador M32 en Logisim Evolution con estas características:

- Visualización interactiva del flujo de datos. Esto implica que los componentes se iluminan durante la ejecución, mostrando cómo se mueven los datos entre registros, ALU y memoria.
- Autómata de la Unidad de Control editable. Con ello los usuarios pueden modificar microinstrucciones y ver inmediatamente cómo afectan la decodificación.
- Módulos independientes probables. Por ejemplo, la ALU puede testearse por separado antes de integrarla al diseño global.
- Depuración en tiempo real, donde se muestre el estado de registros, señales de control y el contador de programa.

# Ejemplo: Implementación de una Nueva Instrucción
Con esta herramienta, añadir una instrucción como MULT (multiplicación) sería un proceso tangible:

- El estudiante modificaría el autómata de la Unidad de Control para incluir los estados necesarios.
- Logisim mostraría errores si hay conflictos en las señales.
- Al simular, vería cómo la ALU usa registros temporales para calcular el resultado, paso a paso.

# Integración con Material Didáctico Existente
La solución se alinea con los recursos actuales de la universidad:

- Las guías de laboratorio se actualizarán con ejercicios prácticos en Logisim.
- Los profesores podrán crear demostraciones en vivo, como ejecutar una instrucción ciclo por ciclo.
- Los estudiantes tendrán acceso a plantillas modulares para experimentar con optimizaciones como pipelining."

# Conclusiones
En conclusión, este proyecto resolverá las limitaciones actuales mediante:

- Interactividad: Permitiendo experimentación práctica sin restricciones técnicas.
- Claridad pedagógica: Los conceptos abstractos se vuelven tangibles con visualizaciones dinámicas.
- Escalabilidad: La modularidad del diseño facilitará añadir características avanzadas en el futuro.

Esta herramienta no solo modernizará la enseñanza del M32, sino que sentará las bases para explorar temas como cachés o arquitecturas superescalares en cursos posteriores.

# Agradecimientos
¡Muchas gracias por su atención!
