# Bajo desarrollo

def codificar(instruccion):
    tokens = instruccion.split(' ') # También pueden haber comas, como en "ldw 0x1000, r1"

    instruccion = 0

    if len(tokens) < 1:
        raise Exception("Instrucción inválida")
    
    match(tokens[0]):
        case "ldw":
            instruccion += (1 << 23)
            instruccion += int(tokens[1], 16) # También, validar
            instruccion += int(tokens[2]) << 18 # Añadir validación de si es INT o no

    return instruccion

instruccion = input("Ingrese instrucción: ")

print(bin(codificar(instruccion)))