push 0
lhp
push function0
lhp
sw
lhp
push 1
add
shp
push 5
push 1
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
push 10000
push -2
add
lw
lhp
sw
lhp
lhp
push 1
add
shp
push -1
lfp
lfp
push -3
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
lfp
push -2
add
lw
stm
pop
sra
pop
sfp
ltm
lra
js