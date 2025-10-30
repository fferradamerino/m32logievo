.text
    ba main                     # Salta a la función main del programa

    # r1: dirección de la pantalla TTY
    # r2: dirección de la string
printf:
    loop:
        ldub r4, [r2 + r0]      # Leer un byte de la string
        be end                  # Si es 0 (fin de cadena), salir
        stb [r1 + r0], r4       # Escribir byte a la dirección TTY
        add r2, r2, 1           # Avanzar al siguiente carácter
        ba loop                 # Repetir el bucle

    end:
        nop                     # Fin de función

main:
    add r1, r0, 8               # r1 = 0x8
    sll r1, r1, 24              # r1 = 0x08000000
    add r2, r0, 0x30            # Las instrucciones aritméticas no tienen soporte
                                # de labels aún :p
    jmpl [r0 + printf], r3

.data
    mensaje:
        .ascii "Hola mundo"