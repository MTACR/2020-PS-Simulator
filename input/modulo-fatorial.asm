MACRO
GERA_FATORIAL &VAL,&TEMP,&SALVA
FATORIAL CALL FAT
FIM BR &SALVA
FAT LOAD #1
    STORE &TEMP
    LOAD &VAL,I
FAT2    SUB #1
    BRPOS POS
    RET
POS    STORE &TEMP
    MULT &VAL,I
    STORE &VAL
    LOAD &TEMP,I
    CALL FAT2
    RET
MEND

GERA_FATORIAL VAL,TEMP,SALVA

SALVA EXTR
VAL EXTR
INICIO EXTR
EXTDEF FATORIAL
TEMP CONST 1
