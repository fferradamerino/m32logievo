import struct
import os.path
import sys

def codificar_tipo_a(nombre, opcode, addr, regdest, regsrc):
    instruccion = 0
    instruccion |= (opcode << 24)

    if addr > 0x2000:
        print(nombre + ": la dirección", addr, "está fuera de rango")
        return 0
    elif regdest > 32:
        print(nombre + ": el registro", regdest, "está fuera de rango")
        return 0

    instruccion |= addr
    instruccion |= (regdest << 19)
    instruccion |= (regsrc << 14)
    instruccion |= (1 << 13)

    return instruccion

def codificar_tipo_b(nombre, opcode, regdest, regsrc1, regsrc2):
    instruccion = 0
    instruccion |= (opcode << 24)

    if regdest > 32 or regsrc1 > 32 or regsrc2 > 32:
        print(nombre + ": uno de los registros está fuera de rango")
        return 0

    instruccion |= (regsrc2 << 8)
    instruccion |= (regsrc1 << 14)
    instruccion |= (regdest << 19)

    return instruccion

def codificar_tipo_c(nombre, opcode, disp):
    instruccion = 0
    instruccion = (opcode << 24)

    if disp >= 2**24 or disp <= -(2**24):
        print(nombre + ": el desplazamiento está fuera de rango")
        return 0

    disp &= (1 << 24) - 1
    
    instruccion |= disp

    return instruccion

def make_val_reg_reg(val_token, reg1_token, reg2_token = ""):
    if val_token[:2] == "0x":
        val = int(val_token.replace(",", ""), 16)
    else:
        val = int(val_token.replace(",", ""))

    reg1_token = reg1_token.replace("r", "")
    reg1 = int(reg1_token.replace(",", ""))

    reg2_token = reg2_token.replace("r", "")
    reg2 = int(reg2_token.replace(",", ""))

    return val, reg1, reg2

def make_reg_reg_reg(reg1_token, reg2_token, reg3_token):
    reg1_token = reg1_token.replace("r", "")
    reg1 = int(reg1_token.replace(",", ""))

    reg2_token = reg2_token.replace("r", "")
    reg2 = int(reg2_token.replace(",", ""))

    reg3_token = reg3_token.replace("r", "")
    reg3 = int(reg3_token.replace(",", ""))

    return reg1, reg2, reg3

def make_disp(disp_token, labels):
    if not disp_token.isnumeric():
        for label in labels:
            print(label)
            if label[0] == disp_token:
                return label[1]
        raise Exception("Label no encontrado")

    if disp_token[:2] == "0x":
        val = int(disp_token.replace(",", ""), 16)
    else:
        val = int(disp_token.replace(",", ""))

    return val

def codificar(linea, labels, direccion_actual):
    tokens = linea.split(' ') # Mejorar para incluir espacios y tabs al principio de línea

    if len(tokens) < 1:
        raise Exception("Instrucción inválida")
    
    match(tokens[0]):
        # Tipo A: Usada por instrucciones aritméticas, de lectura y escritura en memoria
        case "ldw": # ldw [regs + imm], regd -> (ldw regs, imm, regd)
            imm, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("LDW", 1, imm, regd, regs)
        case "lduh": # lduh [regs + imm], regd
            imm, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("LDUH", 2, imm, regd, regs)
        case "ldub": # ldub [regs + imm], regd
            imm, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("LDUB", 3, imm, regd, regs)
        case "ldsh": # ldsh [regs + imm], regd
            imm, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("LDSH", 4, imm, regd, regs)
        case "ldsb": # ldsb [regs + imm], regd
            imm, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("LDSB", 5, imm, regd, regs)
        case "stw": # stw regs, [regd + imm] -> (stw regs, regd, imm)
            imm, regs, regd = make_val_reg_reg(tokens[3], tokens[1], tokens[2])
            instruccion = codificar_tipo_a("STW", 6, imm, regs, regd)
        case "sth": # sth regs, [regd + imm]
            imm, regs, regd = make_val_reg_reg(tokens[3], tokens[1], tokens[2])
            instruccion = codificar_tipo_a("STH", 7, imm, regs, regd)
        case "stb": # stb regs, [regd + imm]
            imm, regs, regd = make_val_reg_reg(tokens[3], tokens[1], tokens[2])
            instruccion = codificar_tipo_a("STB", 8, imm, regs, regd)
        case "add": # add regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("ADD", 9, val, regd, regs)
        case "addx": # addx regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("ADDX", 10, val, regd, regs)
        case "sub": # sub regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("SUB", 11, val, regd, regs)
        case "subx": # subx regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("SUBX", 12, val, regd, regs)
        case "and": # and regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("AND", 13, val, regd, regs)
        case "or": # or regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("OR", 14, val, regd, regs)
        case "xor": # xor regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("XOR", 15, val, regd, regs)
        case "sll": # sll regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("SLL", 16, val, regd, regs)
        case "srl": # srl regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("SRL", 17, val, regd, regs)
        case "sra": # sra regs, val, regd
            val, regs, regd = make_val_reg_reg(tokens[2], tokens[1], tokens[3])
            instruccion = codificar_tipo_a("SRA", 18, val, regd, regs)

        # Tipo B: Instrucciones aritméticas, jmpl (saltos) e instrucciones de lectura y escritura en memoria
        case "$add": # $add regs1, regs2, regd
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$ADD", 9, regd, regs1, regs2)
        case "$addx": # $addx regs1, regs2, regd
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$ADDX", 10, regd, regs1, regs2)
        case "$sub": # $sub regs1, regs2, regd
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$SUB", 11, regd, regs1, regs2)
        case "$subx": # $subx regs1, regs2, regd
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$SUBX", 12, regd, regs1, regs2)
        case "$and": # $and regs1, regs2, regd
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$AND", 13, regd, regs1, regs2)
        case "$or": # $or regs1, regs2, regd
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$OR", 14, regd, regs1, regs2)
        case "$xor": # $xor regs1, regs2, regd
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$XOR", 15, regd, regs1, regs2)
        case "$sll": # $sll regs1, regs2, regd
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$SLL", 16, regd, regs1, regs2)
        case "$srl": # $srl regs1, regs2, regd
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$SRL", 17, regd, regs1, regs2)
        case "$sra": # $sra regs1, regs2, regd
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$SRA", 18, regd, regs1, regs2)
        case "jmpl": # jmpl reg_addr, reg_pc
            reg_addr, reg_pc, _ = make_reg_reg_reg(tokens[1], tokens[2], 0)
            instruccion = codificar_tipo_b("JMPL", 19, 0, reg_addr, reg_pc)
        case "$ldw": # $ldw [regs1 + regs2], regd -> $ldw regs1, regs2, regd 
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$LDW", 1, regd, regs2, regs1)
        case "$lduh": # $lduh [regs1 + regs2], regd -> $lduh regs1, regs2, regd 
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$LDUH", 2, regd, regs2, regs1)
        case "$ldub": # $ldub [regs1 + regs2], regd -> $ldub regs1, regs2, regd 
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$LDUB", 3, regd, regs2, regs1)
        case "$ldsh": # $ldsh [regs1 + regs2], regd -> $ldsh regs1, regs2, regd 
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$LDSH", 4, regd, regs2, regs1)
        case "$ldsb": # $ldsb [regs1 + regs2], regd -> $ldsb regs1, regs2, regd 
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$LDSB", 5, regd, regs2, regs1)
        case "$stw": # $stw regd, [regs1 + regs2] -> $stw regd, regs1, regs2
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$STW", 6, regs1, regs2, regd)
        case "$sth": # $sth regd, [regs1 + regs2] -> $sth regd, regs1, regs2
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$STH", 7, regs1, regs2, regd)
        case "$stb": # $stb regd, [regs1 + regs2] -> $stb regd, regs1, regs2
            regs1, regs2, regd = make_reg_reg_reg(tokens[1], tokens[2], tokens[3])
            instruccion = codificar_tipo_b("$STB", 8, regs1, regs2, regd)

        # Tipo C: Formato utilizado por las instrucciones de saltos condicionales.
        case "ba": # ba disp
            disp = make_disp(tokens[1], labels) - direccion_actual
            instruccion = codificar_tipo_c("BA", 20, disp)
        case "be": # be disp
            disp = make_disp(tokens[1], labels) - direccion_actual
            instruccion = codificar_tipo_c("BE", 21, disp)
        case "bne": # bne disp
            disp = make_disp(tokens[1], labels) - direccion_actual
            instruccion = codificar_tipo_c("BNE", 22, disp)
        case "bg": # bg disp
            disp = make_disp(tokens[1], labels) - direccion_actual
            instruccion = codificar_tipo_c("BG", 23, disp)
        case "bge": # bge disp
            disp = make_disp(tokens[1], labels) - direccion_actual
            instruccion = codificar_tipo_c("BGE", 24, disp)
        case "bl": # bl disp
            disp = make_disp(tokens[1], labels) - direccion_actual
            instruccion = codificar_tipo_c("BL", 25, disp)
        case "ble": # ble disp
            disp = make_disp(tokens[1], labels) - direccion_actual
            instruccion = codificar_tipo_c("BLE", 26, disp)
        case "bgu": # bgu disp
            disp = make_disp(tokens[1], labels) - direccion_actual
            instruccion = codificar_tipo_c("BGU", 27, disp)
        case "bgeu": # bgeu disp
            disp = make_disp(tokens[1], labels) - direccion_actual
            instruccion = codificar_tipo_c("BGEU", 28, disp)
        case "blu": # blu disp
            disp = make_disp(tokens[1], labels) - direccion_actual
            instruccion = codificar_tipo_c("BLU", 29, disp)
        case "bleu": # bleu disp
            disp = make_disp(tokens[1], labels) - direccion_actual
            instruccion = codificar_tipo_c("BLEU", 30, disp)

    return struct.pack('!I', instruccion)

def tokenizar_archivo(archivo):
    lineas = []
    linea = ""
	
    for caracter in archivo:
        match caracter:
            case '\n':
                if len(linea) > 0:
                    lineas.append(linea)
                linea = ""
            case _:
                linea = linea + caracter
    
    if len(linea) > 0:
        lineas.append(linea)
		
    return lineas

def generar_programa(tokens):
    programa = b''
    direccion_actual = 0
    labels = []

    for token in tokens:
        print(token) # DBG
        if token[0] == ';':
            continue
        elif token[-1] == ':':
            labels.append((token[0:-1], direccion_actual))
        else:
            programa += codificar(token, labels, direccion_actual)
            direccion_actual += 4
            
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
