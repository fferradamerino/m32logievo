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
	
decod:
	wr_pc .
	op_y_sel_1 .
	fetch .
	fetch1

ldw:
	sel_reg .
	op_y_sel_2 .
	.
	wr_ar .
	.
	en_a .
	fetch1
	
ldw_2:
	wr_rd .
	.
	sel_d .
	en_a .
	rd .
	.
	fetch1

lduh:
	sel_reg .
	op_y_sel_2 .
	.
	wr_ar .
	op_abi_1 .
	en_a .
	fetch1
	
lduh_2:
	wr_rd .
	op_dbi_1 .
	sel_d .
	en_a .
	rd .
	op_abi_1 .
	fetch1

ldub:
	sel_reg .
	op_y_sel_2 .
	.
	wr_ar .
	op_abi_2 .
	en_a .
	fetch1

ldub_2:
	wr_rd .
	op_dbi_2 op_dbi_1 .
	sel_d .
	en_a .
	rd .
	op_abi_2 .
	fetch1
