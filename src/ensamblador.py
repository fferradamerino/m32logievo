import struct
import os.path
import sys

def codificar_tipo_a(nombre, opcode, addr, reg):
    instruccion = 0
    instruccion += opcode

    if addr > 0x2000:
        print(nombre + ": la dirección", addr, "está fuera de rango")
        return 0
    elif reg > 32:
        print(nombre + ": el registro", reg, "está fuera de rango")
        return 0

    instruccion += addr
    instruccion += (reg << 19)
    instruccion += (1 << 13)

    return instruccion

def make_addr_reg(addr_token, reg_token):
    addr = int(addr_token.replace(",", ""), 16)
    reg = int(reg_token.replace(",", ""))
    return addr, reg

def codificar(input):
    tokens = input.split(' ')

    if len(tokens) < 1:
        raise Exception("Instrucción inválida")
    
    match(tokens[0]):
        case "ldw": # ldw addr, reg
            addr, reg = make_addr_reg(tokens[1], tokens[2])
            instruccion = codificar_tipo_a("LDW", 1 << 24, addr, reg)
        case "lduh": # lduh addr, reg
            addr, reg = make_addr_reg(tokens[1], tokens[2])
            instruccion = codificar_tipo_a("LDUH", 2 << 24, addr, reg)
        case "ldub": # ldub addr, reg
            addr, reg = make_addr_reg(tokens[1], tokens[2])
            instruccion = codificar_tipo_a("LDUB", 3 << 24, addr, reg)
        case "ldsh": # ldsh addr, reg
            addr, reg = make_addr_reg(tokens[1], tokens[2])
            instruccion = codificar_tipo_a("LDSH", 4 << 24, addr, reg)
        case "ldsb": # ldsb addr, reg
            addr, reg = make_addr_reg(tokens[1], tokens[2])
            instruccion = codificar_tipo_a("LDSB", 5 << 24, addr, reg)

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
