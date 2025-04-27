fetch1:
	.
	op_alu_3 op_alu_1 .
	wr_ar .
	.
	en_a .

fetch2:
	.
	.
	sel_d .
	wr_ir .
	en_a .
	rd .
	fetch .
	
decod:
	wr_pc .
	op_y_sel_1 .
	fetch1
