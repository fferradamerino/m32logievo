import pytest

from src.microensamblador.microensamblador import FatalError
from src.microensamblador.microensamblador import MicroAssemblerError

from src.microensamblador.microensamblador import parsear_archivo
from src.microensamblador.microensamblador import tokenizar_archivo
from src.microensamblador.microensamblador import tokenizar_linea
from src.microensamblador.microensamblador import label_addr
from src.microensamblador.microensamblador import codificar_direcciones

class TestMicroensamblador:
    def test_tokenizar_archivo(self):
        # Variable nula
        with pytest.raises(FatalError):
            tokenizar_archivo(None)
        
        archivo = "instruccion:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += ""
        archivo += "instruccion2:\n"
        archivo += "    en_a instruccion2"

        result = tokenizar_archivo(archivo)
        assert result == [
            "instruccion:",
            "    wr_sr instruccion",
            "instruccion2:",
            "    en_a instruccion2"
        ]

    def test_tokenizar_linea(self):
        with pytest.raises(FatalError):
            tokenizar_linea(None)

        linea = "estos son 3 tokens"

        result = tokenizar_linea(linea)
        assert result == ["estos", "son", "3", "tokens"]

    def test_label_addr(self):
        with pytest.raises(FatalError):
            label_addr(None, [])
        with pytest.raises(FatalError):
            label_addr("label", None)
        with pytest.raises(MicroAssemblerError):
            label_addr("label", [("label1", 0x1)])

        result = label_addr("label1", [("label1", 0)])

        assert result == b'\x00'

    def test_codificar_direcciones(self):
        with pytest.raises(FatalError):
            codificar_direcciones(None)

        labels_encontrados = [("ldw", 1)]
        labels_encontrados += [("lduh", 2)]
        labels_encontrados += [("ldub", 3)]
        labels_encontrados += [("ldsh", 4)]
        labels_encontrados += [("ldsb", 5)]
        labels_encontrados += [("stw", 6)]
        labels_encontrados += [("sth", 7)]
        labels_encontrados += [("stb", 8)]
        labels_encontrados += [("add", 9)]
        labels_encontrados += [("addx", 10)]
        labels_encontrados += [("sub", 11)]
        labels_encontrados += [("subx", 12)]
        labels_encontrados += [("and", 13)]
        labels_encontrados += [("or", 14)]
        labels_encontrados += [("xor", 15)]
        labels_encontrados += [("sll", 16)]
        labels_encontrados += [("srl", 17)]
        labels_encontrados += [("sra", 18)]
        labels_encontrados += [("jmpl", 19)]
        labels_encontrados += [("bcond", 20)]

        resultado_esperado = b'\x01\x02\x03\x04\x05\x06\x07\x08\x09\x0a\x0b\x0c\x0d\x0e\x0f\x10\x11\x12\x13\x14'
        resultado_esperado = int.from_bytes(resultado_esperado)

        resultado = codificar_direcciones(labels_encontrados)
        resultado = int.from_bytes(resultado)
        assert resultado == resultado_esperado

    def test_parsear_archivo(self):
        archivo = "instruccion:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "ldw:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "lduh:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "ldub:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "ldsh:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "ldsb:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "stw:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "sth:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "stb:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "add:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "addx:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "sub:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "subx:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "and:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "or:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "xor:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "sll:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "srl:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "sra:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "jmpl:\n"
        archivo += "    wr_sr instruccion\n"
        archivo += "bcond:\n"
        archivo += "    wr_sr instruccion\n"

        tokens = tokenizar_archivo(archivo)

        parsear_archivo(tokens)
