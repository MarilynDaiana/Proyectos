.MODEL  LARGE
.386
.STACK 200h

.DATA

a1					dd		?
b2					dd		?
c3					dd		?
d4					db		?
d5					db		?
_1					dd		1
d6					db		?
_2					dd		2
genero					db		?
_18					dd		18
_19					dd		19
edad					dd		?

.CODE

START:
mov AX,@DATA
mov DS,AX
mov es,ax

FLD _19
FSTP edad

FLD edad
FLD _18

FXCH
FCOM
FSTSW AX
SAHF
JB etiq_salto_1

FLD _1
FSTP a1

etiq_salto_1:

FLD _1
FLD _2
FDIV

FLD a1
FADD

FSTP b2



mov ax, 4C00h
int 21h
END START
