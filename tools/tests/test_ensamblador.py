import pytest

from src.ensamblador.ensamblador import FatalError
from src.ensamblador.ensamblador import AssemblerError
from src.ensamblador.ensamblador import Assembler

from src.ensamblador.ensamblador import codificar_tipo_a
from src.ensamblador.ensamblador import codificar_tipo_b
from src.ensamblador.ensamblador import codificar_tipo_c
from src.ensamblador.ensamblador import parse_register
from src.ensamblador.ensamblador import parse_immediate

class TestEnsamblador:
    def test_codificar_tipo_a(self):
        # Valores nulos
        with pytest.raises(FatalError, match="Valores nulos entregados"):
            codificar_tipo_a(None, 0, 0, 0, 0, 0) # Valor nulo

        # Valores fuera de rango
        with pytest.raises(AssemblerError, match='fuera de rango'):
            codificar_tipo_a("INST", 0, 0, 0, -5000, 0) # IMM negativo fuera de rango
        with pytest.raises(AssemblerError):
            codificar_tipo_a("INST", 0, 0, 0, 9000, 0) # IMM positivo fuera de rango
        with pytest.raises(AssemblerError):
            codificar_tipo_a("INST", -1, 0, 0, 0, 0) # Opcode negativo
        with pytest.raises(AssemblerError):
            codificar_tipo_a("INST", 1000, 0, 0, 0, 0) # Opcode fuera de rango
        with pytest.raises(AssemblerError):
            codificar_tipo_a("INST", 0, 33, 0, 0, 0) # Regd fuera de rango
        with pytest.raises(AssemblerError):
            codificar_tipo_a("INST", 0, -1, 0, 0, 0) # Regd negativo
        with pytest.raises(AssemblerError):
            codificar_tipo_a("INST", 0, 0, 33, 0, 0) # Regs fuera de rango
        with pytest.raises(AssemblerError):
            codificar_tipo_a("INST", 0, 0, -1, 0, 0) # Regs negativo
        with pytest.raises(FatalError):
            codificar_tipo_a("INST", 0, 0, 0, 0, -1) # Línea negativa
        result = codificar_tipo_a("OP", 1, 1, 1, 1, 1)
        assert result == 0x1086001
        
        result = codificar_tipo_a("OP", 1, 1, 1, -1, 1)
        assert result == 0x1087FFF

    def test_codificar_tipo_b(self):
        # Valores nulos
        with pytest.raises(FatalError, match="Valores nulos entregados"):
            codificar_tipo_b(None, 0, 0, 0, 0, 0)

        # Valores fuera de rango
        with pytest.raises(AssemblerError):
            codificar_tipo_b("INST", -1, 0, 0, 0, 0) # Opcode negativo
        with pytest.raises(AssemblerError):
            codificar_tipo_b("INST", 1000, 0, 0, 0, 0) # Opcode fuera de rango
        with pytest.raises(AssemblerError):
            codificar_tipo_b("INST", 0, 33, 0, 0, 0) # Regd demasiado grande
        with pytest.raises(AssemblerError):
            codificar_tipo_b("INST", 0, -1, 0, 0, 0) # Regd negativo
        with pytest.raises(AssemblerError):
            codificar_tipo_b("INST", 0, 0, 33, 0, 0) # Regs1 desmasiado grande
        with pytest.raises(AssemblerError):
            codificar_tipo_b("INST", 0, 0, -1, 0, 0) # Regs1 negativo
        with pytest.raises(AssemblerError):
            codificar_tipo_b("INST", 0, 0, 0, 33, 0) # Regs2 demasiado grande
        with pytest.raises(AssemblerError):
            codificar_tipo_b("INST", 0, 0, 0, -1, 0) # Regs2 negativo
        with pytest.raises(FatalError):
            codificar_tipo_b("INST", 0, 0, 0, 0, -1) # Línea negativa

        result = codificar_tipo_b("INST", 1, 1, 2, 3, 0)
        assert result == 0x1088300

    def test_codificar_tipo_c(self):
        # Valores nulos
        with pytest.raises(FatalError, match="Valores nulos entregados"):
            codificar_tipo_c("INST", None, 0, 0)
        
        # Valores fuera de rango
        with pytest.raises(AssemblerError):
            codificar_tipo_c("INST", -1, 0, 0) # Opcode negativo
        with pytest.raises(AssemblerError):
            codificar_tipo_c("INST", 1000, 0, 0) # Opcode fuera de rango
        with pytest.raises(AssemblerError):
            codificar_tipo_c("INST", 0, 16777216, 0) # Desplazamiento fuera de rango (pos)
        with pytest.raises(AssemblerError):
            codificar_tipo_c("INST", 0, -8388609, 0) # Desplazamiento fuera de rango (neg)
        with pytest.raises(FatalError):
            codificar_tipo_c("INST", 0, 0, -1) # Línea negativa

        result = codificar_tipo_c("INST", 4, -8388608, 0)
        assert result == 0x4800000

    def test_parse_register(self):
        with pytest.raises(FatalError):
            parse_register(None, 0) # Valor nulo
        with pytest.raises(FatalError):
            parse_register("r0", -1) # Línea fuera de rango

        with pytest.raises(AssemblerError):
            parse_register("0", 0)
        with pytest.raises(AssemblerError):
            parse_register("r60", 0)

        result = parse_register("r21,", 0)
        assert result == 21

    def test_parse_immediate(self):
        with pytest.raises(FatalError):
            parse_immediate(None, 0)
        with pytest.raises(FatalError):
            parse_immediate("0x1", -1)

        with pytest.raises(AssemblerError):
            parse_immediate("0xFEZ", 0)
        with pytest.raises(AssemblerError):
            parse_immediate("hola", 0)

        result = parse_immediate("-0x123", 0)
        assert result == -0x123

    def test_Assembler_init(self):
        assembler = Assembler()

        assert assembler != None

    def test_Assembler_first_pass(self):
        assembler = Assembler()

        lines = [
            ".data",
            ".word 0x1",
            ".text",
            "nop",
            "# Comentario",
            "main:",
            "add r2, r1, r0",
            "ldw r4, [r1 + 0x4]",
            "end: nop",
            "main:"
        ]

        with pytest.raises(AssemblerError):
            assembler.first_pass(lines)

    def test_Assembler_process_data_directive(self):
        assembler = Assembler()

        notokens = assembler._process_data_directive("", 1)
        invalido = assembler._process_data_directive(".invalido", 1)
        word = assembler._process_data_directive(".word 0x1", 1)
        byte = assembler._process_data_directive(".byte 0x1", 1)
        short = assembler._process_data_directive(".short 0x1", 1)
        ascii = assembler._process_data_directive(".ascii \"nonzeropad\"", 1)
        asciiz = assembler._process_data_directive(".asciiz \"zeropad\"", 1)
        space = assembler._process_data_directive(".space 0x1", 1)

        with pytest.raises(AssemblerError):
            assembler._process_data_directive(".word", 1)
        with pytest.raises(AssemblerError):
            assembler._process_data_directive(".byte", 1)
        with pytest.raises(AssemblerError):
            assembler._process_data_directive(".short", 1)
        with pytest.raises(AssemblerError):
            assembler._process_data_directive(".ascii", 1)
        with pytest.raises(AssemblerError):
            assembler._process_data_directive(".asciiz", 1)
        with pytest.raises(AssemblerError):
            assembler._process_data_directive(".ascii hola", 1)
        with pytest.raises(AssemblerError):
            assembler._process_data_directive(".asciiz hola", 1)
        with pytest.raises(AssemblerError):
            assembler._process_data_directive(".space", 1)

        assert notokens == 0
        assert invalido == 0
        assert word == 4
        assert byte == 1
        assert short == 2
        assert ascii == 10
        assert asciiz == 8
        assert space == 1

    def test_Assembler_resolve_label_or_inmediate(self):
        assembler = Assembler()

        assembler.labels["hello"] = 0x0

        label1 = assembler.resolve_label_or_immediate("hello,", 1, 0)
        label2 = assembler.resolve_label_or_immediate("hello", 1, 0)
        val1 = assembler.resolve_label_or_immediate("0x123,", 1, 0)
        val2 = assembler.resolve_label_or_immediate("0x123", 1, 0)

        assert label1 == 0
        assert label2 == 0
        assert val1 == 0x123
        assert val2 == 0x123

    def test_Assembler_parse_memory_operand(self):
        assembler = Assembler()

        reg1, val, isReg, isLabel = assembler.parse_memory_operand("[r0 + 0x123]", 0)

        assert reg1 == 0
        assert val == 0x123
        assert isReg == False
        assert isLabel == False

        reg1, reg2, isReg, isLabel = assembler.parse_memory_operand("[r0 + r1]", 0)

        assert reg1 == 0
        assert reg2 == 1
        assert isReg == True
        assert isLabel == False

        assembler.labels["hello"] = 0x123

        reg1, label, isReg, isLabel = assembler.parse_memory_operand("[r0 + hello]", 0)

        assert reg1 == 0
        assert label == 0x123
        assert isReg == False
        assert isLabel == True

        reg1, reg2, isReg, isLabel = assembler.parse_memory_operand("[r20]", 0)

        assert reg1 == 20
        assert reg2 == 0
        assert isReg == False
        assert isLabel == False

        reg1, reg2, isReg, isLabel = assembler.parse_memory_operand("[hello]", 0)

        assert reg1 == 0
        assert val == 0x123
        assert isReg == False
        assert isLabel == True

        with pytest.raises(AssemblerError):
            reg1, reg2, isReg, isLabel = assembler.parse_memory_operand("hola", 0)

        with pytest.raises(AssemblerError):
            reg1, reg2, isReg, isLabel = assembler.parse_memory_operand("[hola]", 0)

        with pytest.raises(AssemblerError):
            reg1, reg2, isReg, isLabel = assembler.parse_memory_operand("[hola + r1 + r2]", 0)

        with pytest.raises(AssemblerError):
            reg1, reg2, isReg, isLabel = assembler.parse_memory_operand("[r0 + hola]", 0)

    def test_Assembler_get_program(self):
        assembler = Assembler()

        lines = [
            ".data",
            ".word 0x1",
            ".text",
            "nop",
            "",
            "# Comentario",
            "main:",
            "add r2, r1, r0",
            "add r2, r1, 0x4",
            "ldw r4, [r1 + 0x4]",
            "ldw r4, [r3 + r2]",
            "stw [r1 + 0x4], r2",
            "stw [r1 + r2], r3",
            "jmpl [r1 + r2], r2",
            "jmpl [r1 + 0x4], r2",
            "ba main",
            "end: nop",
        ]

        assembler.first_pass(lines)
        assembler.get_program(lines)
