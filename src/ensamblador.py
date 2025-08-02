import struct
import os.path
import sys

def codificar(input):
    tokens = input.split(' ')

    instruccion = 0

    if len(tokens) < 1:
        raise Exception("Instrucción inválida")
    
    match(tokens[0]):
        case "ldw": # ldw addr, reg
            instruccion += (1 << 24)

            addr = int(tokens[1].replace(",", ""), 16)
            reg = int(tokens[2].replace(",", ""))

            if addr > 0x2000:
                print("LDW: la dirección", addr, "está fuera de rango")
                return 0
            elif reg > 32:
                print("LDW: el registro", reg, "está fuera de rango")
                return 0

            instruccion += addr
            instruccion += (reg << 19)
            instruccion += (1 << 13)
        case "lduh": # lduh addr, reg
            instruccion += (2 << 24)

            addr = int(tokens[1].replace(",", ""), 16)
            reg = int(tokens[2].replace(",", ""))

            if addr > 0x2000:
                print("LDUH: la dirección", addr, "está fuera de rango")
                return 0
            elif reg > 32:
                print("LDUH: el registro", reg, "está fuera de rango")
                return 0

            instruccion += addr
            instruccion += (reg << 19)
            instruccion += (1 << 13)
        case "ldub": # ldub addr, reg
            instruccion += (3 << 24)

            addr = int(tokens[1].replace(",", ""), 16)
            reg = int(tokens[2].replace(",", ""))

            if addr > 0x2000:
                print("LDUB: la dirección", addr, "está fuera de rango")
                return 0
            elif reg > 32:
                print("LDUB: el registro", reg, "está fuera de rango")
                return 0

            instruccion += addr
            instruccion += (reg << 19)
            instruccion += (1 << 13)
        case "ldsh": # ldsh addr, reg
            instruccion += (4 << 24)

            addr = int(tokens[1].replace(",", ""), 16)
            reg = int(tokens[2].replace(",", ""))

            if addr > 0x2000:
                print("LDSH: la dirección", addr, "está fuera de rango")
                return 0
            elif reg > 32:
                print("LDSH: el registro", reg, "está fuera de rango")
                return 0

            instruccion += addr
            instruccion += (reg << 19)
            instruccion += (1 << 13)
        case "ldsb": # ldsb addr, reg
            instruccion += (5 << 24)

            addr = int(tokens[1].replace(",", ""), 16)
            reg = int(tokens[2].replace(",", ""))

            if addr > 0x2000:
                print("LDSB: la dirección", addr, "está fuera de rango")
                return 0
            elif reg > 32:
                print("LDSB: el registro", reg, "está fuera de rango")
                return 0

            instruccion += addr
            instruccion += (reg << 19)
            instruccion += (1 << 13)            

    return struct.pack('!I', instruccion)

def tokenizar_archivo(archivo):
	lineas = []
	linea = ""
	
	for caracter in archivo:
		match caracter:
			case '\n':
				lineas.append(linea)
				linea = ""
			case _:
				linea = linea + caracter

	if linea != "":
		lineas.append(linea)
		
	return lineas

def generar_programa(tokens):
	programa = b''

	for token in tokens:
		programa += codificar(token)
            
	return programa

def main():
	if len(sys.argv) != 3:
		print("Uso: ensamblador.py archivo.s programa.bin")
		return
	
	if not os.path.isfile(sys.argv[1]):
		print("El archivo", sys.argv[1], "no existe")
		return
	
	with open(sys.argv[1]) as file:
		archivo = file.read()
		tokens = tokenizar_archivo(archivo)
		
		if os.path.isfile(sys.argv[2]):
			os.remove(sys.argv[2])
			
		instrucciones = generar_programa(tokens)
		with open(sys.argv[2], "wb") as a_instrucciones:
			a_instrucciones.write(instrucciones)
	
main()
