import java.io.File;
import java.util.Scanner;

public class Processor {

    private final short re;
    private short pc;
    private short acc = 0;
    private short sp = 0;
    private short ri = 0;
    private byte mop = 0;

    private final Memory memory;

    public Processor(File file) {
        memory = new Memory(1024);
        re = memory.loadFileToMemory(file); //Carrega programa para memória e retorna início da área de dados

        short stackSize = (short) 0; //TODO: MUDAR DEPOIS DE PILHA PRONTA!
        //-----------------------------------
        pc = (short) (stackSize + 1);

        //TODO: alterar de acordo com modo de operação
        while (nextInstruction());
    }

    private boolean nextInstruction() {
        if (pc == 0) {
            System.err.println("Stack overflow");
            return false;
        }

        if (pc >= re) {
            System.err.println("Program counter cannot access data memory");
            return false;
        }

        //TODO: MUDAR DEPOIS DE PILHA PRONTA!

        /*if (pc > 0 && pc < 3) {
            System.err.println("Program counter cannot access stack address");
            return false;
        }*/

        //memory.setDebug(pc);

        short word = memory.getWord(pc++, false, true);

        ri = (short) ((word & 0xF000) >> 12);

        boolean f1 = (word & 0x800) >> 11 != 0;
        boolean f2 = (word & 0x400) >> 10 != 0;
        boolean f3 = (word & 0x200) >> 9  != 0;

        TestOnly.OPCODE opcode = TestOnly.OPCODE.values()[ri];
        //System.out.print("\n" + opcode);

        return parseOpCode(opcode, f1, f2, f3);
    }

    // seleciona qual código a ser processado e lida com a quantidade de palavras a ser lida pros operandos
    private boolean parseOpCode(TestOnly.OPCODE opcode, boolean f1, boolean f2, boolean f3) {
        switch (opcode) {
            case BR:
                return branch(f1);
            case BRPOS:
                return branchPos(f1);
            case ADD:
                add(f1, f3);
                //System.out.println("\n= " + acc);
                break;
            case LOAD:
                load(f1, f3);
                break;
            case BRZERO:
                return branchZero(f1);
            case BRNEG:
                return branchNeg(f1);
            case SUB:
                sub(f1, f3);
                //System.out.println("= " + acc);
                break;
            case STORE:
                return store(f1);
            case WRITE:
                write(f1, f3);
                break;
            case RET:
                ret();
                break;
            case DIVIDE:
                divide(f1, f3);
                //System.out.println("= " + acc);
                break;
            case STOP:
                //stop();
                //System.out.println("\n");
                return false;
            case READ:
                read(f1);
                break;
            case COPY:
                return copy(f1, f2, f3);
            case MULT:
                mult(f1, f3);
                //System.out.println("= " + acc);
                break;
            case CALL:
                call(f1);
                break;
        }

        return true;
    }

    private void write(boolean f1, boolean f3){
        //Placeholder?
        short word = memory.getWord(pc++, f1, f3);
        System.out.println("Output: " + word);
        //TODO?
    }

    private void ret(){
        //TODO
    }

    private boolean read(boolean f1){
        //Placeholder?
        Scanner inputScanner = new Scanner(System.in);
        short input = inputScanner.nextByte();

        short address = memory.getAddress(pc++, f1);

        if (address < re) {
            System.err.println("Cannot \"read\" (save) in program memory");
            return false;
        } else {
            memory.storeWord(address, input);
            return true;
        }
    }

    private boolean copy(boolean f1, boolean f2, boolean f3){
        short address = memory.getAddress(pc++, f1);

        if (address < re) {
            System.err.println("Cannot copy from program memory");
            return false;
        } else {
            short word = memory.getWord(pc++, f2, f3);
            memory.storeWord(address, word);
            return true;
        }
    }

    private void call(boolean f1){
        //TODO
        //pc = memory.getWord(pc++, f1, false);
    }

    private boolean branch(boolean f1) {
        short address = memory.getAddress(pc++, f1);

        if (address >= re) {
            System.err.println("Cannot branch to data memory");
            return false;
        } else {
            pc = address;
            return true;
        }
    }

    private boolean branchNeg(boolean f1) {
        if (acc < 0)
            return branch(f1);
        else {
            pc++;
            return true;
        }
    }

    private boolean branchPos(boolean f1) {
        if (acc > 0)
            return branch(f1);
        else {
            pc++;
            return true;
        }
    }

    private boolean branchZero(boolean f1) {
        if (acc == 0)
            return branch(f1);
        else {
            pc++;
            return true;
        }
    }

    private void add(boolean f1, boolean f3) {
        acc += memory.getWord(pc++, f1, f3);
    }

    private void divide(boolean f1, boolean f3) {
        acc /= memory.getWord(pc++, f1, f3);
    }

    private void load(boolean f1, boolean f3) {
        acc = memory.getWord(pc++, f1, f3);
    }

    private void mult(boolean f1, boolean f3) {
        acc *= memory.getWord(pc++, f1, f3);
    }

    private void sub(boolean f1, boolean f3) {
        acc -= memory.getWord(pc++, f1, f3);
    }

    private boolean store(boolean f1) {
        short address = memory.getAddress(pc++, f1);

        if (address < re) {
            System.err.println("Cannot store in program memory");
            return false;
        } else {
            memory.storeWord(address, acc);
            return true;
        }
    }

    public void dump() {
        System.out.println("ACC - " + acc);
        memory.dumpMemory();
    }

}
