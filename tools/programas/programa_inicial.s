.text

ldw r5, [r2 + a]
ldw r20, [r2 + b]
add r3, r5, r20
stw [r2 + c], r3

.data

a:
    .word 25
b:
    .word 5
c:
    .word 0
