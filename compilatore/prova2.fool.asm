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
push function0
lhp
sw
lhp
push 1
add
shp
push function1
lhp
sw
lhp
push 1
add
shp
push 0
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
push 0
stm
sra
pop
sfp
ltm
lra
js