import os.path
import sys

# Cada microinstrucción son 4 bytes
class Microinstruccion:
	def __init__(self, linea, labels_encontrados, pos_en_memoria):
		self.wr_pc = True if "wr_pc" in linea else False
		self.wr_rd = True if "wr_rd" in linea else False
		self.rd_dest = True if "rd_dest" in linea else False
		self.wr_ir = True if "wr_ir" in linea else False
		self.sel_reg = True if "sel_reg" in linea else False
		self.op_y_sel = True if "op_y_sel" in linea else False
		self.op_alu = True if "op_alu" in linea else False
		self.wr_sr = True if "wr_sr" in linea else False
		self.sel_d = True if "sel_d" in linea else False
		self.wr_ar = True if "wr_ar" in linea else False
		self.op_abi = True if "op_abi" in linea else False
		self.op_dbi = True if "op_dbi" in linea else False
		self.en_d = True if "en_d" in linea else False
		self.en_a = True if "en_a" in linea else False
			
		self.next_addr = self.label_addr(linea[-1], labels_encontrados, pos_en_memoria)
	
	def dbg_print(self):
		print("** DEBUG PRINT **")
		print("wr_pc", self.wr_pc)
		print("wr_rd", self.wr_rd)
		print("wr_rd", self.rd_dest)
		print("wr_ir", self.wr_ir)
		print("sel_reg", self.sel_reg)
		print("op_y_sel", self.op_y_sel)
		print("op_alu", self.op_alu)
		print("wr_sr", self.wr_sr)
		print("sel_d", self.sel_d)
		print("wr_ar", self.wr_ar)
		print("op_abi", self.op_abi)
		print("op_dbi", self.op_dbi)
		print("en_d", self.en_d)
		print("en_a", self.en_a)
		print("next_addr", self.next_addr)
	
	# El último token es hacia donde se debe ir en el microprograma
	# Si es un ".", entonces se sigue a la dirección siguiente
	# Si es un label, entonces se salta hacia dicho label
	def label_addr(self, label, labels_encontrados, pos_en_memoria):
		if label == ".":
			return pos_en_memoria + 4
		
		for x in labels_encontrados:
			if x[0] == label:
				return x[1]
			
		raise Exception("Label no encontrado")
	
	def codificar(self):
		codificacion = 0
		
		codificacion |= (1 << 0) if self.wr_pc else 0
		codificacion |= (1 << 1) if self.wr_rd else 0
		codificacion |= (1 << 2) if self.rd_dest else 0
		codificacion |= (1 << 3) if self.wr_ir else 0
		codificacion |= (1 << 4) if self.sel_reg else 0
		codificacion |= (1 << 5) if self.op_y_sel else 0
		codificacion |= (1 << 6) if self.op_alu else 0
		codificacion |= (1 << 7) if self.wr_sr else 0
		codificacion |= (1 << 8) if self.sel_d else 0
		codificacion |= (1 << 9) if self.wr_ar else 0
		codificacion |= (1 << 10) if self.op_abi else 0
		codificacion |= (1 << 11) if self.op_dbi else 0
		codificacion |= (1 << 12) if self.en_d else 0
		codificacion |= (1 << 13) if self.en_a else 0
		codificacion |= self.next_addr << 14
		
		return codificacion

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

def parsear_archivo(tokens):
	pos_en_memoria = 0 # En bytes
	labels_encontrados = [] # (label, pos_en_memoria)
	microinstrucciones = []
	
	for token in tokens:
		linea = tokenizar_linea(token)
		if linea[0][-1] == ":":
			labels_encontrados.append((linea[:len(token) - 1][0][:-1], pos_en_memoria))
		else:
			microinstrucciones.append(Microinstruccion(linea, labels_encontrados, pos_en_memoria))
			pos_en_memoria += 4
	
	instrucciones = []	
	for microinstruccion in microinstrucciones:
		print(microinstruccion.dbg_print())
		instrucciones.append(microinstruccion.codificar())
		
	return instrucciones
	
def main():
	if len(sys.argv) != 2:
		print("Uso: microensamblador.py archivo.s")
		return
	
	if not os.path.isfile(sys.argv[1]):
		print("El archivo", sys.argv[1], "no existe")
		return
	
	with open(sys.argv[1]) as file:
		archivo = file.read()
		tokens = tokenizar_archivo(archivo)
		print(parsear_archivo(tokens))
	
main()
