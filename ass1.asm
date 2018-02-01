;file handling to replace the macro definitions in the place of their calls and taking care of the actual parameters as well
.model small
.data
msg1 d6 10,13,"str1$"
msg2 d6 10,13,"str2$"

macro accept
mov ah,01
int 21h
mend

macro macro2 &msg,&1
mov ah,09h
lea dx, &msg
; needed this space after ','
int 21h
add bl, &1
mend

.code
mov ax,@data
mov ds,ax

mov ax,5000h
add ax,05h
accept
mov bl,08h
macro2 msg1,10
macro2 msg2,20

mov ah,4ch
int 21h
end

