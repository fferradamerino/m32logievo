# Guion de Presentación: Simulador del Microprocesador M32

Buenas tardes estimados compañeros y profesores. Hoy presentaré los temas relacionados a la propuesta de casos de uso, historias de usuario y mockups, relacionados al tema de Diseño, Implementación y Simulación de la Arquitectura del Microprocesador M32 en Logisim Evolution

# Temario

El temario de la presentación será el siguiente:

- Primero, presentaré los Objetivos de la Presentación
- Seguido de ello, presentaré los casos de uso definidos para el proyecto
- Luego, presentaré las historias de usuario asociadas a los requerimientos del proyecto
- Seguido de ello, presentaré los mockups que servirán de referencia para la versión final del proyecto
- Y finalmente presentaré la conclusión

## Objetivos de la presentación
Los objetivos de esta presentación son:

- Presentar los casos de uso del proyecto junto al diagrama de casos de uso asociados
- Presentar las historias de usuario asociadas al proyecto
- Presentar los mockups que serviran de referencia para el desarrollo del proyecto

# Diagrama de casos de uso

Para estructurar y guiar el desarrollo del simulador de la arquitectura M32, se recomendó previamente utilizar casos de uso. Esta técnica permite representar las funcionalidades clave del sistema desde la perspectiva de los usuarios, y ayuda a identificar claramente cómo interactúan con el simulador.

Los casos de uso no solo ordenan el desarrollo funcional, sino que también permiten vincular los requisitos técnicos con situaciones concretas de uso real, facilitando la validación, la comunicación con los usuarios y el diseño de pruebas.

# Casos de uso: Introducción a los actores

Tal como se indicó en las presentaciones anteriores, se identificaron dos perfiles principales de usuarios: docentes y estudiantes de la carrera de Ingeniería Civil en Informática:

- Por un lado están los docentes, lo cuales tienen experiencia previa enseñando con simuladores (como por ejemplo, el Emu8086), y que a su vez buscan herramientas para ilustrar conceptos complejos.
- Por otro lado están los estudiantes de la asignatura, que por su parte, ya aprobaron (o están cursando aún) la asignatura de Arquitectura de Computadores y buscan reforzar sus conocimientos de manera práctica.

# Casos de uso: definición

En base en los requerimientos funcionales del proyecto, los casos de uso principales fueron estructurados de la siguiente manera:

# Casos de uso: primer caso

- Primer caso de uso es simular instrucciones M32.
 - Sus precondiciones son: el circuito debe estar cargado y configurado correctamente, y que no debe haber errores de conexión o componentes faltantes.
 - Sus postcondiciones son: la instrucción se ha ejecutado, y el estado del sistema refleja los cambios (registros, memoria, señales de control).
 - Los Requisitos no funcionales afectados por este caso de uso son: interfaz clara (usabilidad visual), y la ejecución confiable en Logisim Evolution sin necesidad de instalación adicional (portabilidad)

# Casos de uso: segundo caso

- Segundo caso de uso es visualizar el estado de registros y señales en tiempo real. Esta relacion se da ya que simular instrucciones implica visualizar el estado del sistema.
 - Sus precondiciones son: que el sistema debe estar en estado de simulación activa, y que los módulos deben estar conectados a mecanismos de visualización (probes, displays, etc.).
 - Sus postcondiciones son: que el usuario puede observar cambios en tiempo real durante la ejecución.
 - Los Requisitos no funcionales afectados por este caso de uso son: el diseño modular y ordenado que permita inspección visual (usabilidad, escalabilidad), y el estilo visual estandarizado (alineación, colores, etiquetas)
 
# Casos de uso: tercer caso
 
- Tercer caso de uso: consultar descripciones de módulos del procesador
 - Sus precondiciones son: que el simulador debe tener etiquetas o mecanismos de información embebidos en los módulos.
 - Sus postcondiciones son: que el estudiante ha accedido a información relevante que le ayuda a entender el módulo.
 - Los Requisitos no funcionales afectados por este caso de uso son: la documentación comprensible y visible (usabilidad), y los estándares visuales claros para facilitar el acceso a información

# Casos de uso: cuarto caso 

- Cuarto caso de uso: cargar y ejecutar programas escritos en ensamblador M32. Este caso de uso incluye al caso 1 dado que al ejecutar un programa, internamente se simulan instrucciones.
 - Sus precondiciones son: que el programa debe estar escrito en el formato esperado, y que los componentes del simulador ofrezcan la capacidad de carga (ej. un selector de archivo o memoria precargada).
 - Sus postcondiciones son: que el programa se ejecuta en el simulador y los resultados son visibles en el sistema.
 - Los Requisitos no funcionales afectados por este caso de uso son: la portabilidad del sistema para ejecución sin configuración adicional, y el diseño modular que permita cargar nuevos programas sin modificar el circuito base

Se destaca adicionalmente que el primer caso de uso incluye al segundo caso de uso en su funcionamiento.

# Casos de uso: participación de los actores

Ambos actores pueden acceder a la mayoría de los casos de uso, con ligeras diferencias según su rol.

- Aquí se destaca que los docentes utilizan principalmente CU01, CU02 y CU04 para enseñar.
- Y que los estudiantes acceden a los mismos, pero también hacen uso del CU03 para profundizar en el funcionamiento de cada módulo.

# Mockups

En el mockup podemos ver una representación general del circuito en Logisim Evolution, junto a componentes que ayudan a seguir el flujo de señal.

En el mockup se puede apreciar el estado de cada registro dentro del banco de registro, donde cada dato se actualiza dinámicamente. Este tipo de visualización es clave para entender el funcionamiento paso a paso de la arquitectura.

El mockup muestra cómo al abrir el circuito de un componente, aparece una etiqueta con información clara, por ejemplo: ‘Unidad de Control: se encarga de activar las señales correspondientes a cada instrucción’.

En el mockup se ilustra la opción integrada de ‘Cargar archivo’ en una ROM, que permite importar un programa escrito en ensamblador M32. Tras cargarlo, el circuito lo ejecuta instrucción por instrucción, simulando el comportamiento real del procesador.

# Conclusiones

En conclusión, se puede indicar que:

- El diagrama de casos de uso no solo organiza las funcionalidades previstas, sino que también garantiza que cada perfil de usuario tenga acceso a las herramientas necesarias según sus necesidades y nivel de experiencia.
- Cada una de las historias de usuario representa una funcionalidad clave del sistema, desarrollada desde la perspectiva de los usuarios reales: estudiantes y docentes.
- Los mockups permiten anticipar cómo será la experiencia de uso final, y sirven también como guía visual para continuar con la implementación en Logisim Evolution.

¡Gracias por su atención!
