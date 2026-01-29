push 0
push 1
push 0
push 7
push 2
lfp
push -4
add
lw
lfp
push -5
add
lw
div
print
lfp
push -4
add
lw
lfp
push -5
add
lw
sub
print
add
lfp
push -2
add
lw
push 0
beq label0
push 0
b label1
label0:
push 1
label1:
print
add
lfp
push -2
add
lw
push 0
beq label2
lfp
push -3
add
lw
push 0
beq label2
push 1
b label3
label2:
push 0
label3:
print
add
lfp
push -3
add
lw
push 1
beq label4
lfp
push -2
add
lw
push 1
beq label4
push 0
b label5
label4:
push 1
label5:
print
add
lfp
push -4
add
lw
push 7
bleq label6
push 0
b label7
label6:
push 1
label7:
print
add
push 8
lfp
push -4
add
lw
bleq label8
push 0
b label9
label8:
push 1
label9:
print
add
push 10
push 6
push 2
div
sub
print
add
push 10
push 6
sub
push 2
div
print
add
lfp
push -3
add
lw
push 0
beq label12
lfp
push -2
add
lw
push 0
beq label12
push 1
b label13
label12:
push 0
label13:
push 0
beq label10
push 0
b label11
label10:
push 1
label11:
print
add
lfp
push -3
add
lw
push 1
beq label16
lfp
push -2
add
lw
push 1
beq label16
push 0
b label17
label16:
push 1
label17:
push 0
beq label14
push 0
b label15
label14:
push 1
label15:
print
add
halt