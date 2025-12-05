.text
    ba main                     # Salta a la función main del programa

    # r1: dirección de la pantalla TTY
    # r2: dirección de la string
printf:
    loop:
        ldub r4, [r2 + r0]      # Leer un byte de la string
        sll r4, r4, 24          # Para colocar el byte a escribir en la parte superior
                                # de la palabra
        stb [r1 + r0], r4       # Escribir byte a la dirección TTY
        add r2, r2, 1           # Avanzar al siguiente carácter
        ba loop                 # Repetir el bucle
        # Preguntar sobre el flag Z en la ALU !!!!

    end:
        nop                     # Fin de función

main:
	add r1, r0, 9               # r1 = 0x9
	sll r1, r1, 24              # r1 = 0x09000000 (la dirección del teclado)
	
	add r2, r0, 0x10            # r2 = 0x10
	sll r2, r2, 24              # r2 = 0x10000000 (la dirección del clear/read)

    add r3, r3, 0               # Inicializamos r3 en 0 (contador de caracteres leídos)
	
procesar_registro:
    add r6, r0, 2               # Guardaremos de manera temporal el valor 3 en r6
	stw [r2 + r0], r6           # [0x10000000] = 0b01 (se limpia el teclado y se desactiva la lectura
								#                      de los demás caracteres)
	ldw r4, [r1 + r0]           # Cargamos la palabra en el registro 4
	add r5, r4, r0              # Movemos el registro del teclado a r5 para obtener el bit de available
	and r5, r5, 0x40            # Filtramos todos los bits excepto el de available
	or r5, r5, r5               # Verificamos que el registro r5 no sea cero
	bne fin_loop_teclado        # Comienzo del bucle: si no hay datos
								# entonces terminamos el bucle
	add r5, r4, r0              # Movemos el caracter leído a r5
    and r5, r5, 0x7F            # Filtramos todos los bits excepto el del carácter
    stb [r3 + 0x78], r5         # Guardamos el carácter en la variable mensaje
    add r3, r3, 1               # Incrementamos el contador de caracteres leídos
    add r6, r0, 10              # Guardaremos de manera temporal el valor 10 (read) en r6
    stw [r2 + r0], r6           # [0x10000000] = 0b10 (se activa la lectura del siguiente carácter)
	ba procesar_registro        # Repetimos los pasos anteriores
	
fin_loop_teclado:
	# Acá comienza la siguiente rutina: imprimir el texto en pantalla
    add r1, r0, 8               # r1 = 0x8
    sll r1, r1, 24              # r1 = 0x08000000
    add r2, r0, 0x78            # Las instrucciones aritméticas no tienen soporte
                                # de labels aún :p
    jmpl [r0 + printf], r3

.data
    mensaje:
        .ascii "Hola mundo"