import struct
import os.path
import sys
import re

class AssemblerError(Exception):
    """Excepción para errores de ensamblado"""
    def __init__(self, line_num, message):
        self.line_num = line_num
        self.message = message
        super().__init__(f"Línea {line_num}: {message}")

def codificar_tipo_a(nombre, opcode, addr, regdest, regsrc, line_num):
    """Codifica instrucciones de tipo A"""
    instruccion = 0
    instruccion |= (opcode << 24)

    if addr > 0x1FFF or addr < 0:  # 13 bits sin signo
        raise AssemblerError(line_num, f"{nombre}: dirección {addr} fuera de rango (0-0x1FFF)")
    if regdest > 31:
        raise AssemblerError(line_num, f"{nombre}: registro destino r{regdest} fuera de rango (r0-r31)")
    if regsrc > 31:
        raise AssemblerError(line_num, f"{nombre}: registro fuente r{regsrc} fuera de rango (r0-r31)")

    instruccion |= addr
    instruccion |= (regdest << 19)
    instruccion |= (regsrc << 14)
    instruccion |= (1 << 13)

    return instruccion

def codificar_tipo_b(nombre, opcode, regdest, regsrc1, regsrc2, line_num):
    """Codifica instrucciones de tipo B"""
    instruccion = 0
    instruccion |= (opcode << 24)

    if regdest > 31:
        raise AssemblerError(line_num, f"{nombre}: registro destino r{regdest} fuera de rango")
    if regsrc1 > 31:
        raise AssemblerError(line_num, f"{nombre}: registro fuente 1 r{regsrc1} fuera de rango")
    if regsrc2 > 31:
        raise AssemblerError(line_num, f"{nombre}: registro fuente 2 r{regsrc2} fuera de rango")

    instruccion |= (regsrc2 << 8)
    instruccion |= (regsrc1 << 14)
    instruccion |= (regdest << 19)

    return instruccion

def codificar_tipo_c(nombre, opcode, disp, line_num):
    """Codifica instrucciones de tipo C (saltos)"""
    instruccion = 0
    instruccion = (opcode << 24)

    # 24 bits con signo
    if disp >= 2**23 or disp < -(2**23):
        raise AssemblerError(line_num, f"{nombre}: desplazamiento {disp} fuera de rango")

    # Convertir a representación de 24 bits
    disp &= (1 << 24) - 1
    
    instruccion |= disp

    return instruccion

def parse_register(token, line_num):
    """Parsea un token de registro (r0-r31)"""
    token = token.strip().rstrip(',')
    if not token.startswith('r'):
        raise AssemblerError(line_num, f"Se esperaba registro, se obtuvo '{token}'")
    
    try:
        reg_num = int(token[1:])
        if reg_num < 0 or reg_num > 31:
            raise ValueError()
        return reg_num
    except ValueError:
        raise AssemblerError(line_num, f"Número de registro inválido: '{token}'")

def parse_immediate(token, line_num):
    """Parsea un valor inmediato (decimal o hexadecimal)"""
    token = token.strip().rstrip(',')
    try:
        if token.startswith('0x') or token.startswith('0X'):
            return int(token, 16)
        else:
            return int(token)
    except ValueError:
        raise AssemblerError(line_num, f"Valor inmediato inválido: '{token}'")

def parse_memory_operand(token, line_num):
    """
    Parsea operando de memoria en formato [reg + offset] o [reg]
    Retorna (registro, offset)
    """
    token = token.strip().rstrip(',')
    if not (token.startswith('[') and token.endswith(']')):
        raise AssemblerError(line_num, f"Formato de memoria inválido: '{token}'")
    
    # Remover corchetes
    inner = token[1:-1].strip()
    
    # Buscar el operador '+'
    if '+' in inner:
        parts = inner.split('+')
        if len(parts) != 2:
            raise AssemblerError(line_num, f"Formato de memoria inválido: '{token}'")
        reg = parse_register(parts[0].strip(), line_num)
        offset = parse_immediate(parts[1].strip(), line_num)
        return reg, offset
    else:
        # Solo registro, offset = 0
        reg = parse_register(inner, line_num)
        return reg, 0

class Assembler:
    def __init__(self):
        self.labels = {}
        self.instructions = []
        self.current_address = 0
        
    def first_pass(self, lines):
        """Primera pasada: recolectar etiquetas"""
        address = 0
        
        for line_num, line in enumerate(lines, 1):
            # Remover comentarios
            line = re.sub(r'[;#].*', '', line).strip()
            
            if not line:
                continue
            
            # Directivas
            if line.startswith('.'):
                continue  # Ignorar por ahora
            
            # Detectar etiquetas
            if ':' in line:
                parts = line.split(':', 1)
                label = parts[0].strip()
                if label:
                    if label in self.labels:
                        raise AssemblerError(line_num, f"Etiqueta '{label}' duplicada")
                    self.labels[label] = address
                
                # Revisar si hay instrucción en la misma línea
                remaining = parts[1].strip() if len(parts) > 1 else ""
                if remaining:
                    address += 4
            elif line:
                # Es una instrucción
                address += 4
    
    def resolve_label_or_immediate(self, token, line_num, pc):
        """Resuelve una etiqueta o valor inmediato"""
        token = token.strip().rstrip(',')
        
        # Verificar si es una etiqueta
        if token in self.labels:
            return self.labels[token] - pc
        
        # Si no, debe ser un valor inmediato
        return parse_immediate(token, line_num)
    
    def assemble_instruction(self, mnemonic, operands, line_num, pc):
        """Ensambla una instrucción individual"""
        mnemonic = mnemonic.lower()
        
        # Instrucciones de carga (Tipo A)
        if mnemonic in ['ldw', 'lduh', 'ldub', 'ldsh', 'ldsb']:
            if len(operands) != 2:
                raise AssemblerError(line_num, f"{mnemonic}: se esperan 2 operandos")
            
            regs, offset = parse_memory_operand(operands[0], line_num)
            regd = parse_register(operands[1], line_num)
            
            opcodes = {'ldw': 1, 'lduh': 2, 'ldub': 3, 'ldsh': 4, 'ldsb': 5}
            return codificar_tipo_a(mnemonic.upper(), opcodes[mnemonic], offset, regd, regs, line_num)
        
        # Instrucciones de almacenamiento (Tipo A)
        elif mnemonic in ['stw', 'sth', 'stb']:
            if len(operands) != 2:
                raise AssemblerError(line_num, f"{mnemonic}: se esperan 2 operandos")
            
            regs = parse_register(operands[0], line_num)
            regd, offset = parse_memory_operand(operands[1], line_num)
            
            opcodes = {'stw': 6, 'sth': 7, 'stb': 8}
            return codificar_tipo_a(mnemonic.upper(), opcodes[mnemonic], offset, regs, regd, line_num)
        
        # Instrucciones aritméticas con inmediato (Tipo A)
        elif mnemonic in ['add', 'addx', 'sub', 'subx', 'and', 'or', 'xor', 'sll', 'srl', 'sra']:
            if len(operands) != 3:
                raise AssemblerError(line_num, f"{mnemonic}: se esperan 3 operandos")
            
            regs = parse_register(operands[0], line_num)
            imm = parse_immediate(operands[1], line_num)
            regd = parse_register(operands[2], line_num)
            
            opcodes = {'add': 9, 'addx': 10, 'sub': 11, 'subx': 12, 'and': 13, 
                      'or': 14, 'xor': 15, 'sll': 16, 'srl': 17, 'sra': 18}
            return codificar_tipo_a(mnemonic.upper(), opcodes[mnemonic], imm, regd, regs, line_num)
        
        # Instrucciones aritméticas con registro (Tipo B)
        elif mnemonic in ['addr', 'addxr', 'subr', 'subxr', 'andr', 'orr', 'xorr', 'sllr', 'srlr', 'srar']:
            if len(operands) != 3:
                raise AssemblerError(line_num, f"{mnemonic}: se esperan 3 operandos")
            
            regs1 = parse_register(operands[0], line_num)
            regs2 = parse_register(operands[1], line_num)
            regd = parse_register(operands[2], line_num)
            
            base_mnemonics = {'addr': 'add', 'addxr': 'addx', 'subr': 'sub', 'subxr': 'subx',
                             'andr': 'and', 'orr': 'or', 'xorr': 'xor', 'sllr': 'sll', 
                             'srlr': 'srl', 'srar': 'sra'}
            opcodes = {'add': 9, 'addx': 10, 'sub': 11, 'subx': 12, 'and': 13, 
                      'or': 14, 'xor': 15, 'sll': 16, 'srl': 17, 'sra': 18}
            
            base = base_mnemonics[mnemonic]
            return codificar_tipo_b(mnemonic.upper(), opcodes[base], regd, regs1, regs2, line_num)
        
        # Instrucciones de carga/almacenamiento con registro (Tipo B)
        elif mnemonic in ['ldwr', 'lduhr', 'ldubr', 'ldshr', 'ldsbr']:
            if len(operands) != 2:
                raise AssemblerError(line_num, f"{mnemonic}: se esperan 2 operandos")
            
            regs1, regs2 = parse_memory_operand(operands[0], line_num)
            if regs2 == 0:
                raise AssemblerError(line_num, f"{mnemonic}: versión con registro requiere [reg + reg]")
            regd = parse_register(operands[1], line_num)
            
            opcodes = {'ldwr': 1, 'lduhr': 2, 'ldubr': 3, 'ldshr': 4, 'ldsbr': 5}
            base = mnemonic[:-1]  # Remover 'r'
            return codificar_tipo_b(mnemonic.upper(), opcodes[mnemonic], regd, regs2, regs1, line_num)
        
        elif mnemonic in ['stwr', 'sthr', 'stbr']:
            if len(operands) != 2:
                raise AssemblerError(line_num, f"{mnemonic}: se esperan 2 operandos")
            
            regd = parse_register(operands[0], line_num)
            regs1, regs2 = parse_memory_operand(operands[1], line_num)
            
            opcodes = {'stwr': 6, 'sthr': 7, 'stbr': 8}
            return codificar_tipo_b(mnemonic.upper(), opcodes[mnemonic], regs1, regs2, regd, line_num)
        
        # JMPL (Tipo B)
        elif mnemonic == 'jmpl':
            if len(operands) != 2:
                raise AssemblerError(line_num, f"{mnemonic}: se esperan 2 operandos")
            
            reg_addr = parse_register(operands[0], line_num)
            reg_pc = parse_register(operands[1], line_num)
            
            return codificar_tipo_b("JMPL", 19, 0, reg_addr, reg_pc, line_num)
        
        # Saltos condicionales (Tipo C)
        elif mnemonic in ['ba', 'be', 'bne', 'bg', 'bge', 'bl', 'ble', 'bgu', 'bgeu', 'blu', 'bleu']:
            if len(operands) != 1:
                raise AssemblerError(line_num, f"{mnemonic}: se espera 1 operando")
            
            disp = self.resolve_label_or_immediate(operands[0], line_num, pc)
            
            opcodes = {'ba': 20, 'be': 21, 'bne': 22, 'bg': 23, 'bge': 24, 
                      'bl': 25, 'ble': 26, 'bgu': 27, 'bgeu': 28, 'blu': 29, 'bleu': 30}
            return codificar_tipo_c(mnemonic.upper(), opcodes[mnemonic], disp, line_num)
        
        else:
            raise AssemblerError(line_num, f"Instrucción desconocida: '{mnemonic}'")
    
    def second_pass(self, lines):
        """Segunda pasada: ensamblar instrucciones"""
        program = b''
        address = 0
        
        for line_num, line in enumerate(lines, 1):
            # Remover comentarios
            line = re.sub(r'[;#].*', '', line).strip()
            
            if not line:
                continue
            
            # Directivas
            if line.startswith('.'):
                continue
            
            # Procesar etiquetas
            if ':' in line:
                parts = line.split(':', 1)
                line = parts[1].strip() if len(parts) > 1 else ""
                
                if not line:
                    continue
            
            # Parsear instrucción
            tokens = line.split()
            if not tokens:
                continue
            
            mnemonic = tokens[0]
            operands = []
            
            # Recolectar operandos
            if len(tokens) > 1:
                # Unir todo después del mnemónico y dividir por comas
                operand_str = ' '.join(tokens[1:])
                # Dividir por comas, pero respetar corchetes
                operands = []
                current = ""
                bracket_depth = 0
                
                for char in operand_str:
                    if char == '[':
                        bracket_depth += 1
                    elif char == ']':
                        bracket_depth -= 1
                    
                    if char == ',' and bracket_depth == 0:
                        operands.append(current.strip())
                        current = ""
                    else:
                        current += char
                
                if current.strip():
                    operands.append(current.strip())
            
            # Ensamblar
            instruction = self.assemble_instruction(mnemonic, operands, line_num, address)
            program += struct.pack('!I', instruction)
            address += 4
        
        return program

def main():
    if len(sys.argv) != 3:
        print("Uso: ensamblador.py archivo.s programa.bin")
        return
    
    if not os.path.isfile(sys.argv[1]):
        print(f"Error: El archivo '{sys.argv[1]}' no existe")
        return
    
    try:
        with open(sys.argv[1], 'r') as file:
            lines = file.readlines()
        
        assembler = Assembler()
        
        # Primera pasada: recolectar etiquetas
        assembler.first_pass(lines)
        
        # Segunda pasada: ensamblar
        program = assembler.second_pass(lines)
        
        # Escribir archivo binario
        with open(sys.argv[2], 'wb') as output:
            output.write(program)
        
        print(f"✓ Ensamblado exitoso: {len(program)} bytes escritos en '{sys.argv[2]}'")
        print(f"  {len(program) // 4} instrucciones")
        print(f"  {len(assembler.labels)} etiquetas definidas")
        
    except AssemblerError as e:
        print(f"Error de ensamblado: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"Error inesperado: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()