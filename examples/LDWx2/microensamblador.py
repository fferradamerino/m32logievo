import os.path
import struct
import sys

# Cada microinstrucción son 8 bytes
class Microinstruccion:
	def __init__(self, linea, labels_encontrados, pos_en_memoria):
		self.wr_pc = True if "wr_pc" in linea else False
		self.wr_rd = True if "wr_rd" in linea else False
		self.rd_dest = True if "rd_dest" in linea else False
		self.wr_ir = True if "wr_ir" in linea else False
		self.sel_reg = True if "sel_reg" in linea else False
		self.op_y_sel_1 = True if "op_y_sel_1" in linea else False
		self.op_y_sel_2 = True if "op_y_sel_2" in linea else False
		self.op_alu_1 = True if "op_alu_1" in linea else False
		self.op_alu_2 = True if "op_alu_2" in linea else False
		self.op_alu_3 = True if "op_alu_3" in linea else False
		self.op_alu_4 = True if "op_alu_4" in linea else False
		self.wr_sr = True if "wr_sr" in linea else False
		self.sel_d = True if "sel_d" in linea else False
		self.wr_ar = True if "wr_ar" in linea else False
		self.op_abi_1 = True if "op_abi_1" in linea else False
		self.op_abi_2 = True if "op_abi_2" in linea else False
		self.op_dbi_1 = True if "op_dbi_1" in linea else False
		self.op_dbi_2 = True if "op_dbi_2" in linea else False
		self.op_dbi_3 = True if "op_dbi_3" in linea else False
		self.en_d = True if "en_d" in linea else False
		self.en_a = True if "en_a" in linea else False
		self.rd = True if "rd" in linea else False
		self.wr = True if "wr" in linea else False
		self.ldwx2_dest = True if "ldwx2_dest" in linea else False
		self.fetch = True if "execute" in linea else False
			
		self.next_addr = self.label_addr(linea[-1], labels_encontrados, pos_en_memoria)
	
	def dbg_print(self):
		print("** DEBUG PRINT **")
		print("wr_pc", self.wr_pc)
		print("wr_rd", self.wr_rd)
		print("rd_dest", self.rd_dest)
		print("wr_ir", self.wr_ir)
		print("sel_reg", self.sel_reg)
		print("op_y_sel_1", self.op_y_sel_1)
		print("op_y_sel_2", self.op_y_sel_2)
		print("op_alu_1", self.op_alu_1)
		print("op_alu_2", self.op_alu_2)
		print("op_alu_3", self.op_alu_3)
		print("op_alu_4", self.op_alu_4)
		print("wr_sr", self.wr_sr)
		print("sel_d", self.sel_d)
		print("wr_ar", self.wr_ar)
		print("op_abi_1", self.op_abi_1)
		print("op_abi_2", self.op_abi_2)
		print("op_dbi_1", self.op_dbi_1)
		print("op_dbi_2", self.op_dbi_2)
		print("op_dbi_3", self.op_dbi_3)
		print("en_d", self.en_d)
		print("en_a", self.en_a)
		print("rd", self.rd)
		print("wr", self.wr)
		print("execute", self.fetch)
		print("next_addr", self.next_addr)
	
	# El último token es hacia donde se debe ir en el microprograma
	# Si es un ".", entonces se sigue a la dirección siguiente
	# Si es un label, entonces se salta hacia dicho label
	def label_addr(self, label, labels_encontrados, pos_en_memoria):
		if label == ".":
			return pos_en_memoria + 1
		
		for x in labels_encontrados:
			if x[0] == label:
				return x[1]
			
		raise Exception("Label no encontrado: " + label)
	
	def codificar(self):
		codificacion = 0
		
		codificacion |= (1 << 0) if self.wr_pc else 0
		codificacion |= (1 << 1) if self.wr_rd else 0
		codificacion |= (1 << 2) if self.rd_dest else 0
		codificacion |= (1 << 3) if self.wr_ir else 0
		codificacion |= (1 << 4) if self.sel_reg else 0
		codificacion |= (1 << 5) if self.op_y_sel_1 else 0
		codificacion |= (1 << 6) if self.op_y_sel_2 else 0
		codificacion |= (1 << 7) if self.op_alu_1 else 0
		codificacion |= (1 << 8) if self.op_alu_2 else 0
		codificacion |= (1 << 9) if self.op_alu_3 else 0
		codificacion |= (1 << 10) if self.op_alu_4 else 0
		codificacion |= (1 << 11) if self.wr_sr else 0
		codificacion |= (1 << 12) if self.sel_d else 0
		codificacion |= (1 << 13) if self.wr_ar else 0
		codificacion |= (1 << 14) if self.op_abi_1 else 0
		codificacion |= (1 << 15) if self.op_abi_2 else 0
		codificacion |= (1 << 16) if self.op_dbi_1 else 0
		codificacion |= (1 << 17) if self.op_dbi_2 else 0
		codificacion |= (1 << 18) if self.op_dbi_3 else 0
		codificacion |= (1 << 19) if self.en_d else 0
		codificacion |= (1 << 20) if self.en_a else 0
		codificacion |= (1 << 21) if self.rd else 0
		codificacion |= (1 << 22) if self.wr else 0
		codificacion |= (1 << 32) if self.ldwx2_dest else 0
		codificacion |= (1 << 23) if self.fetch else 0
		codificacion |= self.next_addr << 24

		return struct.pack('!Q', codificacion)

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
	
def tokenizar_linea(linea):
	tokens = []
	token = ""
	
	for caracter in linea:
		if (caracter == ' ' or caracter == '\t'):
			if len(token) > 0:
				tokens.append(token)
			token = ""
		else:
			token = token + caracter
	
	if len(token) > 0:
		tokens.append(token)
		
	return tokens

def label_addr(label, labels_encontrados):
	for x in labels_encontrados:
		if x[0] == label:
			print("Dirección", label + ":", x[1])
			return struct.pack('!B', x[1])
		
	raise Exception("Label no encontrado: " + label)

def codificar_direcciones(labels_encontrados):
	direcciones = b''
	
	direcciones += struct.pack('!B', 0)
	direcciones += label_addr("ldw", labels_encontrados)
	direcciones += label_addr("lduh", labels_encontrados)
	direcciones += label_addr("ldub", labels_encontrados)
	direcciones += label_addr("ldsh", labels_encontrados)
	direcciones += label_addr("ldsb", labels_encontrados)
	direcciones += label_addr("stw", labels_encontrados)
	direcciones += label_addr("sth", labels_encontrados)
	direcciones += label_addr("stb", labels_encontrados)
	direcciones += label_addr("add", labels_encontrados)
	direcciones += label_addr("addx", labels_encontrados)
	direcciones += label_addr("sub", labels_encontrados)
	direcciones += label_addr("subx", labels_encontrados)
	direcciones += label_addr("and", labels_encontrados)
	direcciones += label_addr("or", labels_encontrados)
	direcciones += label_addr("xor", labels_encontrados)
	direcciones += label_addr("sll", labels_encontrados)
	direcciones += label_addr("srl", labels_encontrados)
	direcciones += label_addr("sra", labels_encontrados)
	direcciones += label_addr("jmpl", labels_encontrados)
	direcciones += label_addr("bcond", labels_encontrados)

	# Esta sección de código es para rellenar los valores de las instrucciones
	# de salto (que comparten la misma microinstrucción)
	for _ in range(21, 31):
		direcciones += struct.pack('!B', 0)

	direcciones += label_addr("ldwx2", labels_encontrados)
	
	return direcciones

def parsear_archivo(tokens):
	pos_en_memoria = 0 # En bytes
	labels_encontrados = [] # (label, pos_en_memoria)
	microinstrucciones = []
	
	for token in tokens:
		linea = tokenizar_linea(token)
		if len(linea) < 1:
			continue
		if linea[0][-1] == ":":
			labels_encontrados.append((linea[:len(token) - 1][0][:-1], pos_en_memoria))
		else:
			microinstrucciones.append(Microinstruccion(linea, labels_encontrados, pos_en_memoria))
			pos_en_memoria += 1
	
	instrucciones = b''	
	for microinstruccion in microinstrucciones:
		microinstruccion.dbg_print()
		instrucciones += microinstruccion.codificar()
		
	direcciones = codificar_direcciones(labels_encontrados)

	return instrucciones, direcciones
	
def main():
	if len(sys.argv) != 4:
		print("Uso: microensamblador.py archivo.s microprograma.bin direcciones.bin")
		return
	
	if not os.path.isfile(sys.argv[1]):
		print("El archivo", sys.argv[1], "no existe")
		return
	
	with open(sys.argv[1]) as file:
		archivo = file.read()
		tokens = tokenizar_archivo(archivo)
		
		if os.path.isfile(sys.argv[2]) or os.path.isfile(sys.argv[3]):
			os.remove(sys.argv[2])
			os.remove(sys.argv[3])
			
		instrucciones, direcciones = parsear_archivo(tokens)
		with open(sys.argv[2], "wb") as a_instrucciones:
			a_instrucciones.write(instrucciones)
		with open(sys.argv[3], "wb") as a_direcciones:
			a_direcciones.write(direcciones)
	
main()
