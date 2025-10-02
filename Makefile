init:
	cd src/m32plugin && make
	cd src && python microensamblador.py microprograma.s mc.bin d.bin