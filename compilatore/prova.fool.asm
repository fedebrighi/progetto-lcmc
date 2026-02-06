push 0
lhp
push function0
lhp
sw
lhp
push 1
add
shp
lhp
push function1
lhp
sw
lhp
push 1
add
shp
push function2
push 99
lhp
sw
lhp
push 1
add
shp
push 10000
push -3
add
lw
lhp
sw
lhp
lhp
push 1
add
shp
lfp
lfp
push -5
add
lw
lfp
stm
ltm
ltm
push -4
add
lw
js
print
halt

function0:
cfp
lra
lfp
lw
push -1
add
lw
stm
sra
pop
sfp
ltm
lra
js

function1:
cfp
lra
lfp
lw
push -1
add
lw
stm
sra
pop
sfp
ltm
lra
js

function2:
cfp
lra
lfp
lfp
push 1
add
lw
stm
ltm
ltm
lw
push 0
add
lw
js
stm
sra
pop
pop
sfp
ltm
lra
js