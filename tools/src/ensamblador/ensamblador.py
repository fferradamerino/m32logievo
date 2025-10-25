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

def codificar_tipo_a(nombre, opcode, regd, regs, val, line_num):
    """
    Codifica instrucciones de tipo A
    Formato: Op || Regd || Regs || 1 || Val
    Op: 8 bits, Regd: 5 bits, Regs: 5 bits, bit i: 1, Val: 13 bits
    """
    instruccion = 0
    
    # Validaciones para val (13 bits con signo: -4096 a 8191)
    if val < -4096 or val > 8191:
        raise AssemblerError(line_num, f"{nombre}: valor inmediato {val} fuera de rango (-4096 a 8191)")
    
    # Convertir a representación de 13 bits
    if val < 0:
        val = val & 0x1FFF
    
    if regd > 31 or regd < 0:
        raise AssemblerError(line_num, f"{nombre}: registro destino r{regd} fuera de rango (r0-r31)")
    if regs > 31 or regs < 0:
        raise AssemblerError(line_num, f"{nombre}: registro fuente r{regs} fuera de rango (r0-r31)")
    
    # Construcción de la instrucción
    instruccion |= (opcode & 0xFF) << 24      # Op: bits 31-24
    instruccion |= (regd & 0x1F) << 19        # Regd: bits 23-19
    instruccion |= (regs & 0x1F) << 14        # Regs: bits 18-14
    instruccion |= (1 << 13)                  # Bit i: bit 13 (siempre 1 para tipo A)
    instruccion |= (val & 0x1FFF)             # Val: bits 12-0
    
    return instruccion

def codificar_tipo_b(nombre, opcode, regd, regs1, regs2, line_num):
    """
    Codifica instrucciones de tipo B
    Formato: Op || Regd || Regs1 || 0 || Regs2 || <7 bits en 0>
    Op: 8 bits, Regd: 5 bits, Regs1: 5 bits, bit i: 0, Regs2: 5 bits, padding: 7 bits
    """
    instruccion = 0
    
    # Validaciones
    if regd > 31 or regd < 0:
        raise AssemblerError(line_num, f"{nombre}: registro destino r{regd} fuera de rango (r0-r31)")
    if regs1 > 31 or regs1 < 0:
        raise AssemblerError(line_num, f"{nombre}: registro fuente 1 r{regs1} fuera de rango (r0-r31)")
    if regs2 > 31 or regs2 < 0:
        raise AssemblerError(line_num, f"{nombre}: registro fuente 2 r{regs2} fuera de rango (r0-r31)")
    
    # Construcción de la instrucción
    instruccion |= (opcode & 0xFF) << 24      # Op: bits 31-24
    instruccion |= (regd & 0x1F) << 19        # Regd: bits 23-19
    instruccion |= (regs1 & 0x1F) << 14       # Regs1: bits 18-14
    instruccion |= (0 << 13)                  # Bit i: bit 13 (siempre 0 para tipo B)
    instruccion |= (regs2 & 0x1F) << 8        # Regs2: bits 12-8
    # Los 7 bits restantes (6-0) quedan en 0
    
    return instruccion

def codificar_tipo_c(nombre, opcode, disp, line_num):
    """
    Codifica instrucciones de tipo C (saltos)
    Formato: Op || Desplazamiento
    Op: 8 bits, Desplazamiento: 24 bits con signo
    """
    instruccion = 0
    
    # Validación: 24 bits con signo (-8388608 a 8388607)
    if disp < -(2**23) or disp >= (2**23):
        raise AssemblerError(line_num, f"{nombre}: desplazamiento {disp} fuera de rango (-8388608 a 8388607)")
    
    # Convertir a representación de 24 bits
    if disp < 0:
        disp = disp & 0xFFFFFF
    
    instruccion |= (opcode & 0xFF) << 24      # Op: bits 31-24
    instruccion |= (disp & 0xFFFFFF)          # Desplazamiento: bits 23-0
    
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
        elif token.startswith('-0x') or token.startswith('-0X'):
            return -int(token[3:], 16)
        else:
            return int(token)
    except ValueError:
        raise AssemblerError(line_num, f"Valor inmediato inválido: '{token}'")

class Assembler:
    def __init__(self):
        self.labels = {}
        self.instructions = []
        self.current_address = 0
        self.data_section = b''
        self.data_address = 0
        self.in_data_section = False
        # Para seguimiento durante el ensamblado
        self.current_pc = 0
        
    def first_pass(self, lines):
        """Primera pasada: recolectar etiquetas y procesar directivas de datos"""
        address = 0
        
        for line_num, line in enumerate(lines, 1):
            # Remover comentarios
            line = re.sub(r'[;#].*', '', line).strip()
            
            if not line:
                continue
            
            # Detectar secciones
            if line.lower() == '.text':
                self.in_data_section = False
                continue
            elif line.lower() == '.data':
                self.in_data_section = True
                continue
            
            # Procesar directivas de datos
            if line.startswith('.'):
                if self.in_data_section:
                    address += self._process_data_directive(line, line_num)
                continue
            
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
            elif line and not self.in_data_section:
                # Es una instrucción
                address += 4
    
    def _process_data_directive(self, directive, line_num):
        """Procesa directivas de datos y retorna bytes agregados"""
        tokens = directive.split()
        if not tokens:
            return 0
        
        directive_type = tokens[0].lower()
        
        if directive_type == '.word':
            if len(tokens) < 2:
                raise AssemblerError(line_num, ".word requiere al menos un valor")
            
            bytes_added = 0
            for token in tokens[1:]:
                value = parse_immediate(token.rstrip(','), line_num)
                self.data_section += struct.pack('!I', value & 0xFFFFFFFF)
                bytes_added += 4
            return bytes_added
        
        elif directive_type == '.byte':
            if len(tokens) < 2:
                raise AssemblerError(line_num, ".byte requiere al menos un valor")
            
            bytes_added = 0
            for token in tokens[1:]:
                value = parse_immediate(token.rstrip(','), line_num)
                self.data_section += struct.pack('!B', value & 0xFF)
                bytes_added += 1
            return bytes_added
        
        elif directive_type == '.short' or directive_type == '.half':
            if len(tokens) < 2:
                raise AssemblerError(line_num, f"{directive_type} requiere al menos un valor")
            
            bytes_added = 0
            for token in tokens[1:]:
                value = parse_immediate(token.rstrip(','), line_num)
                self.data_section += struct.pack('!H', value & 0xFFFF)
                bytes_added += 2
            return bytes_added
        
        elif directive_type == '.ascii':
            if len(tokens) < 2:
                raise AssemblerError(line_num, ".ascii requiere una cadena")
            
            string_content = ' '.join(tokens[1:])
            match = re.search(r'"([^"]*)"', string_content)
            if not match:
                raise AssemblerError(line_num, ".ascii: cadena no entre comillas")
            
            string_value = match.group(1)
            string_value = string_value.replace('\\n', '\n').replace('\\t', '\t').replace('\\\\', '\\')
            
            self.data_section += string_value.encode('utf-8')
            return len(string_value.encode('utf-8'))
        
        elif directive_type == '.asciiz':
            if len(tokens) < 2:
                raise AssemblerError(line_num, ".asciiz requiere una cadena")
            
            string_content = ' '.join(tokens[1:])
            match = re.search(r'"([^"]*)"', string_content)
            if not match:
                raise AssemblerError(line_num, ".asciiz: cadena no entre comillas")
            
            string_value = match.group(1)
            string_value = string_value.replace('\\n', '\n').replace('\\t', '\t').replace('\\\\', '\\')
            
            self.data_section += string_value.encode('utf-8') + b'\x00'
            return len(string_value.encode('utf-8')) + 1
        
        elif directive_type == '.space':
            if len(tokens) < 2:
                raise AssemblerError(line_num, ".space requiere un tamaño")
            
            size = parse_immediate(tokens[1], line_num)
            self.data_section += b'\x00' * size
            return size
        
        else:
            return 0
    
    def resolve_label_or_immediate(self, token, line_num, pc):
        """Resuelve una etiqueta o valor inmediato"""
        token = token.strip().rstrip(',')
        
        # Verificar si es una etiqueta
        if token in self.labels:
            return self.labels[token] - pc
        
        # Si no, debe ser un valor inmediato
        return parse_immediate(token, line_num)

    def parse_memory_operand(self, token, line_num):
        """
        Parsea operando de memoria en formato [reg + offset] o [reg + reg] o [reg]
        Retorna (registro1, valor/registro2, es_registro, es_etiqueta)
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
            reg1 = parse_register(parts[0].strip(), line_num)
            
            # Verificar si el segundo operando es un registro o inmediato/etiqueta
            second_op = parts[1].strip()
            if second_op.startswith('r'):
                # Es un registro - Formato B
                reg2 = parse_register(second_op, line_num)
                return reg1, reg2, True, False
            else:
                # Es un inmediato o etiqueta
                try:
                    # Primero intentar como etiqueta
                    if second_op in self.labels:
                        return reg1, self.labels[second_op], False, True
                    else:
                        # Si no es etiqueta, parsear como inmediato
                        offset = parse_immediate(second_op, line_num)
                        return reg1, offset, False, False
                except AssemblerError:
                    # Si falla el parsing, podría ser una etiqueta no definida
                    raise AssemblerError(line_num, f"Etiqueta '{second_op}' no definida")
        else:
            # Solo registro, offset = 0 (Formato A)
            # Pero podría ser una etiqueta directa como [label]
            if inner.startswith('r'):
                reg = parse_register(inner, line_num)
                return reg, 0, False, False
            else:
                # Es una etiqueta directa [label]
                if inner in self.labels:
                    # Usar r0 como registro base cuando es solo [label]
                    return 0, self.labels[inner], False, True
                else:
                    raise AssemblerError(line_num, f"Etiqueta '{inner}' no definida")    

    def assemble_instruction(self, mnemonic, operands, line_num, pc):
        """Ensambla una instrucción individual según la nueva arquitectura"""
        mnemonic = mnemonic.lower()
        
        # Mapeo de opcodes según nueva_arq.txt
        opcodes = {
            'ldw': 1, 'lduh': 2, 'ldub': 3, 'ldsh': 4, 'ldsb': 5,
            'stw': 6, 'sth': 7, 'stb': 8,
            'add': 9, 'addx': 10, 'sub': 11, 'subx': 12,
            'and': 13, 'or': 14, 'xor': 15,
            'sll': 16, 'srl': 17, 'sra': 18,
            'jmpl': 19,
            'ba': 20, 'be': 21, 'bne': 22, 'bg': 23, 'bge': 24,
            'bl': 25, 'ble': 26, 'bgu': 27, 'bgeu': 28, 'blu': 29, 'bleu': 30
        }
        
        if mnemonic == 'nop':
            return 0
        
        if mnemonic not in opcodes:
            raise AssemblerError(line_num, f"Instrucción desconocida: '{mnemonic}'")
        
        opcode = opcodes[mnemonic]
        
        # Instrucciones de carga: op regd, [regs + val/reg]
        if mnemonic in ['ldw', 'lduh', 'ldub', 'ldsh', 'ldsb']:
            if len(operands) != 2:
                raise AssemblerError(line_num, f"{mnemonic}: se esperan 2 operandos (regd, [regs + val/reg])")
            
            regd = parse_register(operands[0], line_num)
            regs, offset_or_reg, is_reg, is_label = self.parse_memory_operand(operands[1], line_num, pc)
            
            if is_reg:
                # Formato B: op regd, [regs1 + regs2]
                return codificar_tipo_b(mnemonic.upper(), opcode, regd, regs, offset_or_reg, line_num)
            else:
                # Formato A: op regd, [regs + val]
                #if is_label:
                #   offset_or_reg += 4
                return codificar_tipo_a(mnemonic.upper(), opcode, regd, regs, offset_or_reg, line_num)

        # Instrucciones de almacenamiento: op [regd + val/reg], regs
        elif mnemonic in ['stw', 'sth', 'stb']:
            if len(operands) != 2:
                raise AssemblerError(line_num, f"{mnemonic}: se esperan 2 operandos ([regd + val/reg], regs)")
            
            regd, offset_or_reg, is_reg, is_label = self.parse_memory_operand(operands[0], line_num, pc)
            regs = parse_register(operands[1], line_num)
            
            if is_reg:
                # Formato B: op [regd + regs1], regs2
                return codificar_tipo_b(mnemonic.upper(), opcode, regd, offset_or_reg, regs, line_num)
            else:
                # Formato A: op [regd + val], regs
                #if is_label:
                    #offset_or_reg += 4
                return codificar_tipo_a(mnemonic.upper(), opcode, regs, regd, offset_or_reg, line_num)
        
        # Instrucciones aritméticas/lógicas: op regd, regs, val/reg
        elif mnemonic in ['add', 'addx', 'sub', 'subx', 'and', 'or', 'xor', 'sll', 'srl', 'sra']:
            if len(operands) != 3:
                raise AssemblerError(line_num, f"{mnemonic}: se esperan 3 operandos (regd, regs, val/reg)")
            
            regd = parse_register(operands[0], line_num)
            regs = parse_register(operands[1], line_num)
            
            # Verificar si el tercer operando es registro o inmediato
            third_op = operands[2].strip().rstrip(',')
            if third_op.startswith('r'):
                # Formato B: op regd, regs1, regs2
                regs2 = parse_register(third_op, line_num)
                return codificar_tipo_b(mnemonic.upper(), opcode, regd, regs, regs2, line_num)
            else:
                # Formato A: op regd, regs, val
                val = parse_immediate(third_op, line_num)
                return codificar_tipo_a(mnemonic.upper(), opcode, regd, regs, val, line_num)
        
        # JMPL: Sintaxis de almacenamiento según la especificación
        elif mnemonic == 'jmpl':
            if len(operands) != 2:
                raise AssemblerError(line_num, f"{mnemonic}: se esperan 2 operandos ([regs + val/reg], regd)")
            
            regs, offset_or_reg, is_reg, is_label = self.parse_memory_operand(operands[0], line_num, pc)
            regd = parse_register(operands[1], line_num)
            
            if is_reg:
                # Formato B: jmpl [regd + regs1], regs2
                return codificar_tipo_b("JMPL", opcode, regs, offset_or_reg, regd, line_num)
            else:
                # Formato A: jmpl [regs + val], regd
                # Si es etiqueta, calcular desplazamiento relativo
                if is_label:
                    offset_or_reg = offset_or_reg - pc
                return codificar_tipo_a("JMPL", opcode, regd, regs, offset_or_reg, line_num)
        
        # Saltos condicionales (Tipo C): op disp
        elif mnemonic in ['ba', 'be', 'bne', 'bg', 'bge', 'bl', 'ble', 'bgu', 'bgeu', 'blu', 'bleu']:
            if len(operands) != 1:
                raise AssemblerError(line_num, f"{mnemonic}: se espera 1 operando (etiqueta/desplazamiento)")
            
            disp = self.resolve_label_or_immediate(operands[0], line_num, pc)
            disp -= 4 # Si esto se vuelve a romper, XD
            return codificar_tipo_c(mnemonic.upper(), opcode, disp, line_num)
        
        raise AssemblerError(line_num, f"Error al ensamblar instrucción: '{mnemonic}'")
    
    def second_pass(self, lines):
        """Segunda pasada: ensamblar instrucciones"""
        program = b''
        address = 0
        in_text_section = True
        
        for line_num, line in enumerate(lines, 1):
            # Remover comentarios
            line = re.sub(r'[;#].*', '', line).strip()
            
            if not line:
                continue
            
            # Detectar secciones
            if line.lower() == '.text':
                in_text_section = True
                address = 0
                continue
            elif line.lower() == '.data':
                in_text_section = False
                address = 0
                continue
            
            # Ignorar directivas en segunda pasada
            if line.startswith('.'):
                continue
            
            if in_text_section:
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
                    operand_str = ' '.join(tokens[1:])
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
    
    def get_program(self, lines):
        """Retorna el programa completo (sección de texto + datos)"""
        text_program = self.second_pass(lines)
        return text_program + self.data_section

def main():
    if len(sys.argv) != 3:
        print("Uso: ensamblador.py archivo.s programa.bin")
        print("\nEjemplo:")
        print("  python ensamblador.py programa.s salida.bin")
        return
    
    if not os.path.isfile(sys.argv[1]):
        print(f"Error: El archivo '{sys.argv[1]}' no existe")
        return
    
    try:
        with open(sys.argv[1], 'r', encoding='utf-8') as file:
            lines = file.readlines()
        
        assembler = Assembler()
        
        # Primera pasada: recolectar etiquetas y procesar datos
        assembler.first_pass(lines)
        
        # Segunda pasada: ensamblar
        program = assembler.get_program(lines)
        
        # Escribir archivo binario
        with open(sys.argv[2], 'wb') as output:
            output.write(program)

        text_size = len(program) - len(assembler.data_section)
        print(f"✓ Ensamblado exitoso: {len(program)} bytes escritos en '{sys.argv[2]}'")
        print(f"  Sección .text: {text_size} bytes ({text_size // 4} instrucciones)")
        print(f"  Sección .data: {len(assembler.data_section)} bytes")
        print(f"  {len(assembler.labels)} etiquetas definidas")
        
    except AssemblerError as e:
        print(f"Error de ensamblado: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"Error inesperado: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)

if __name__ == "__main__":
    main()