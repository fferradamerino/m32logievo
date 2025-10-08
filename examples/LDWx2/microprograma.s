fetch1:
	en_a wr_ar op_alu_3 op_alu_1 .

fetch2:
	en_a wr_ir rd sel_d .
	.
	
decod:
	wr_pc op_y_sel_1 .
	execute fetch1

and:
	wr_rd sel_reg op_y_sel_2 op_alu_3 fetch1
	
or:
	wr_rd sel_reg op_y_sel_2 op_alu_3 op_alu_1 fetch1
	
xor:
	wr_rd sel_reg op_y_sel_2 op_alu_3 op_alu_2 fetch1
	
sll:
	wr_rd sel_reg op_y_sel_2 op_alu_3 op_alu_2 op_alu_1 fetch1
	
srl:
	wr_rd sel_reg op_y_sel_2 op_alu_4 fetch1
	
sra:
	wr_rd sel_reg op_y_sel_2 op_alu_4 op_alu_1 fetch1
	
subx:
	wr_rd sel_reg op_y_sel_2 op_alu_2 op_alu_1 wr_sr fetch1

sub:
	wr_rd sel_reg op_y_sel_2 op_alu_2 wr_sr fetch1
	
add:
	wr_rd sel_reg op_y_sel_2 wr_sr fetch1
	
addx:
	wr_rd sel_reg op_y_sel_2 op_alu_1 wr_sr fetch1
	
jmpl:
	wr_rd op_y_sel_2 .
	wr_pc sel_reg op_y_sel_2 fetch1
	
bcond:
	wr_pc op_y_sel_2 op_y_sel_1 fetch1
	wr_pc op_y_sel_1 fetch1
	
stb:
	.
	fetch1
	
sth:
	.
	fetch1
	
stw:
	sel_reg op_y_sel_1 wr_ar en_a .
	wr rd_dest sel_reg op_alu_2 op_alu_1 op_dbi_3 op_dbi_1 en_d en_a .
	.
	fetch1

ldw:
	sel_reg op_y_sel_2 wr_ar en_a .
	wr_rd sel_d en_a rd .
	.
	fetch1

lduh:
	sel_reg op_y_sel_2 wr_ar op_abi_1 en_a .
	wr_rd op_dbi_1 sel_d en_a rd op_abi_1 .
	.
	fetch1

ldub:
	sel_reg op_y_sel_2 wr_ar op_abi_2 en_a .
	wr_rd op_dbi_2 op_dbi_1 sel_d en_a rd op_abi_2 .
	.
	fetch1
	
ldsh:
	sel_reg op_y_sel_2 wr_ar op_abi_1 en_a .
	wr_rd sel_d op_dbi_2 op_abi_1 en_a rd .
	.
	fetch1
	
ldsb:
	sel_reg op_y_sel_2 wr_ar op_abi_2 en_a .
	wr_rd sel_d op_dbi_3 op_abi_2 en_a rd .
	.
	fetch1

ldwx2:
	sel_reg wr_ar en_a rd .
	sel_d ldwx2_dest wr_rd en_a rd .
	sel_reg op_y_sel_1 wr_ar en_a rd .
	sel_d wr_rd en_a rd .
	fetch1
