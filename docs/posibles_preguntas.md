# Posibles preguntas

1. ¿Por qué decidiste trabajar con la arquitectura M32 y no con otra arquitectura más moderna o industrialmente usada?
- Esto es debido a la simpleza de M32: posee un conjunto de instrucciones relativamente corto (no más de 30 instrucciones), y a diferencia de otras arquitecturas (tales como Intel x86 o ARM), no posee optimizaciones que quiten importancia a procesos relevantes dentro del procesador

2. ¿Cómo sabes que los problemas actuales con el aprendizaje de arquitectura se deben principalmente a la falta de visualización?
- Esto es debido a que, al no existir visualización, no se puede realmente obtener un feedback inmediato en caso de realizar modificaciones a la arquitectura

3. ¿Qué ventajas específicas ofrece tu propuesta frente al uso tradicional de lápiz y papel en la enseñanza?
- Como se mencionó anteriormente, ofrece feedback inmediato y fácil de rastrear a nivel de depuración

4. ¿Cómo validaste que los requerimientos obtenidos en las entrevistas realmente representan las necesidades generales de los estudiantes y docentes?
- Esto es debido a que, originalmente, se realizó un conjunto inicial de requerimientos, y dentro de las entrevistas se iba consultado a los estudiantes y docentes si les parecería una buena idea (o necesario) que dichas capacidades se añadiesen a la simulación

5. ¿Has considerado que los usuarios podrían tener dificultades técnicas para utilizar Logisim Evolution? ¿Cómo planeas abordar eso?
- Efectivamente, pero esto sólo es el caso de los estudiantes que aún no han llegado a la material de autómatas en Arquitectura de Computadores. El currículum actual de la asignatura implica enseñar Logisim Evolution previo al estudio de M32, por lo que el riesgo de no comprender el uso de esta herramienta se mitiga de esa forma.

6. ¿Por qué elegiste XP y no otra metodología ágil como Scrum?
- Esto es por los plazos acotados que se tiene para trabajar en el proyecto: hay reuniones semanales, además de que el "equipo" es pequeño (trabajo individual)

7. ¿Qué mecanismos específicos usarás para recoger feedback durante el desarrollo incremental?
- Se llevarán a cabo encuestas después del uso del microprocesador M32 recopilando información tales como errores o sugerencias.

8. ¿Cómo aplicarás los principios de refactorización en un entorno visual como Logisim?
- Aquí se puede tomar componentes que ya existan para simplificarlos y reducir su complejidad (es decir, refactorizar en su escencia)

9. ¿Cómo justificas el cálculo del VAN si se trata de un proyecto académico sin fines comerciales directos?
- Esto último (el VAN) se calculó bajo la necesidad de entender como beneficia, a nivel de tiempo y económico, a los docentes de la asignatura, además de demostrar que un proyecto sin fines comerciales directos siempre puede tener un impacto positivo en la economía de una organización

10. ¿Qué impacto tendría en la viabilidad si no pudieras contar con la validación de docentes o estudiantes durante la etapa final?
- Se tendría que recurrir a técnicas de desarrollo estándares de la industria del hardware: documentación robusta y diagramas limpios del hardware implementado

11. ¿Has considerado una estrategia de mantenimiento a largo plazo del simulador?
- Sí. Se utilizará un sistema de tickets de soporte para resolver problemas que tengan los estudiantes: cada estudiante que tenga una duda o sugerencia respecto al simulador podrá abrir un ticket en Github para resolver el problema

12. ¿Cómo asegurarás que el simulador sea intuitivo para estudiantes que no tienen experiencia previa con Logisim Evolution?
- Como ya se mencionó, el uso de Logisim Evolution ya es parte del programa de la asignatura. Sin embargo, parte de la documentación en el informe final del proyecto implica documentar como abrir el proyecto

13. ¿Qué medidas tomarás para evitar que errores en el simulador generen confusión pedagógica?
- Esto último se escapa del alcance de mi proyecto: los errores en Logisim Evolution son parte de la responsabilidad del equipo desarrollador de este simulador. Sin embargo, una forma de mitigar esto sería documentar los errores conocidos del simulador e indicarlos de antemano al usuario.

14. ¿Cuál es el alcance del soporte para el lenguaje ensamblador de M32 dentro de tu simulador?
- El diseño actual del lenguaje ensamblador de M32 abarca comentarios, labels e instrucciones. La construcción de macros está fuera del alcance del proyecto, dado que la elaboración de un lenguaje completo es lo suficientemente complejo para desarrollar otro proyecto de título

15. ¿Cuál es el grado de avance actual del simulador? ¿Puedes demostrar alguna funcionalidad ya operativa?
- Efectivamente, se pueden mostrar avances en cada componente modular, pero dado el contexto de la presentación (hay estudiantes esperando presentar), se tendría que agendar la revisión para otro momento.

16. ¿Cómo representarás visualmente los estados de los registros y señales de control en tiempo real?
- Esto se puede lograr gracias al diseño del banco de registros como un componente modular. Además, se contempla crear componentes adicionales, incluido un programa en ensablador M32 para dicho componente, que permita visualizar el estado actual de los registros del M32

17. ¿Qué tan escalable es tu diseño en Logisim para futuras mejoras o extensiones?
- Se pueden añadir nuevos componentes, e incluso hacer uso de la API de Logisim Evolution para añadir plugins

18. ¿Qué harías si encuentras que Logisim Evolution tiene una limitación técnica crítica para representar ciertas funciones del M32?
- Esto último se puede mitigar gracias a la capacidad de desarrollar plugins para Logisim Evolution. Si un componente no existe, perfectamente se puede crear

19. ¿Cómo medirás el impacto educativo real de tu simulador? ¿Tienes planeado algún tipo de evaluación?
- Usar estadísticas de cursos anteriores en los certamenes correspondientes a M32. Debería observarse una mejora en el rendimiento (siempre y cuando se utilice el mismo formato de certamenes sin incluir material nuevo en este).

20. ¿Qué innovaciones destacarías en tu propuesta frente a soluciones anteriores como la de Hades?
- Se destaca la compatibilidad con sistemas operativos modernos, y el uso de documentación incluida en el archivo simulable
