import struct
import os.path
import sys

def codificar_tipo_a(nombre, opcode, addr, regdest, regsrc = 0):
    instruccion = 0
    instruccion += opcode

    if addr > 0x2000:
        print(nombre + ": la dirección", addr, "está fuera de rango")
        return 0
    elif regdest > 32:
        print(nombre + ": el registro", regdest, "está fuera de rango")
        return 0

    instruccion += addr
    instruccion += (regdest << 19)
    instruccion += (regsrc << 14)
    instruccion += (1 << 13)

    return instruccion

def make_val_reg_reg(val_token, reg1_token, reg2_token = ""):
    val = int(val_token.replace(",", ""), 16)
    reg1 = int(reg1_token.replace(",", ""))
    reg2 = int(reg2_token.replace(",", ""))
    return val, reg1, reg2

def codificar(linea):
    tokens = linea.split(' ')

    if len(tokens) < 1:
        raise Exception("Instrucción inválida")
    
    match(tokens[0]):
        case "ldw": # ldw addr, reg
            addr, reg, _ = make_val_reg_reg(tokens[1], tokens[2])
            instruccion = codificar_tipo_a("LDW", 1 << 24, addr, reg)
        case "lduh": # lduh addr, reg
            addr, reg, _ = make_val_reg_reg(tokens[1], tokens[2])
            instruccion = codificar_tipo_a("LDUH", 2 << 24, addr, reg)
        case "ldub": # ldub addr, reg
            addr, reg, _ = make_val_reg_reg(tokens[1], tokens[2])
            instruccion = codificar_tipo_a("LDUB", 3 << 24, addr, reg)
        case "ldsh": # ldsh addr, reg
            addr, reg, _ = make_val_reg_reg(tokens[1], tokens[2])
            instruccion = codificar_tipo_a("LDSH", 4 << 24, addr, reg)
        case "ldsb": # ldsb addr, reg
            addr, reg, _ = make_val_reg_reg(tokens[1], tokens[2])
            instruccion = codificar_tipo_a("LDSB", 5 << 24, addr, reg)
        case "stw": # stw reg, addr
            addr, reg, _ = make_val_reg_reg(tokens[2], tokens[1])
            instruccion = codificar_tipo_a("STW", 6 << 24, addr, 0, reg)
        case "sth": # sth reg, addr
            addr, reg, _ = make_val_reg_reg(tokens[2], tokens[1])
            instruccion = codificar_tipo_a("STH", 7 << 24, addr, 0, reg)
        case "stb": # stb reg, addr
            addr, reg, _ = make_val_reg_reg(tokens[2], tokens[1])
            instruccion = codificar_tipo_a("STB", 8 << 24, addr, 0, reg)
        case "add": # add regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("ADD", 9 << 24, val, regd, regs)
        case "addx": # addx regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("ADDX", 10 << 24, val, regd, regs)
        case "sub": # sub regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("SUB", 11 << 24, val, regd, regs)
        case "subx": # subx regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("SUBX", 12 << 24, val, regd, regs)
        case "and": # and regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("AND", 13 << 24, val, regd, regs)
        case "or": # or regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("OR", 14 << 24, val, regd, regs)
        case "xor": # xor regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("XOR", 15 << 24, val, regd, regs)
        case "sll": # sll regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("SLL", 16 << 24, val, regd, regs)
        case "srl": # srl regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("SRL", 17 << 24, val, regd, regs)
        case "sra": # sra regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("SRA", 18 << 24, val, regd, regs)

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
