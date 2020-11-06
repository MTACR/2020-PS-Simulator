import java.io.File;
import java.util.Scanner;

public class Processor {

    private short pc;
    private short acc;
    //private short sp; //implementado na memoria
    private short ri;
    private short re;
    private byte mop;

    private final Memory memory;

    public Processor(File file) {
        memory = new Memory(1024);
        memory.loadFileToMemory(file); //Carrega programa para memória e retorna início da área de dados

        pc = memory.firstPosition();

        //TODO: alterar de acordo com modo de operação
        while (nextInstruction());
    }

    private boolean nextInstruction() {
        if (pc == 0) {
            System.err.println("Stack overflow");
            return false;
        }

        if (pc > memory.size() || pc < 0) {
            System.err.println("Program counter out of memory bounds");
            return false;
        }

        if (pc > 0 && pc < memory.firstPosition()) {
            System.err.println("Program counter cannot access stack area");
            return false;
        }

        memory.setDebug(pc);

        short word = memory.getWord(pc++, false, true);

        ri = (short) (word & 15);

        boolean f1 = (word & 32)  != 0;
        boolean f2 = (word & 64)  != 0;
        boolean f3 = (word & 128) != 0;

        TestOnly.OPCODE opcode = TestOnly.OPCODE.values()[ri];
        //System.out.print("\n" + opcode);

        return parseOpCode(opcode, f1, f2, f3);
    }

    // seleciona qual código a ser processado e lida com a quantidade de palavras a ser lida pros operandos
    private boolean parseOpCode(TestOnly.OPCODE opcode, boolean f1, boolean f2, boolean f3) {
        switch (opcode) {
            case BR:
                branch(f1);
                break;
            case BRPOS:
                branchPos(f1);
                break;
            case ADD:
                add(f1, f3);
                break;
            case LOAD:
                load(f1, f3);
                break;
            case BRZERO:
                branchZero(f1);
                break;
            case BRNEG:
                branchNeg(f1);
                break;
            case SUB:
                sub(f1, f3);
                break;
            case STORE:
                store(f1);
                break;
            case WRITE:
                write(f1, f3);
                break;
            case RET:
                return ret();
            case DIVIDE:
                divide(f1, f3);
                break;
            case STOP:
                return false;
            case READ:
                read(f1);
                break;
            case COPY:
                copy(f1, f2, f3);
                break;
            case MULT:
                mult(f1, f3);
                break;
            case CALL:
                return call(f1);
            default:
                System.err.println("Invalid opcode");
                return false;
        }

        return true;
    }

    private void write(boolean f1, boolean f3){
        //Placeholder?
        re = pc;
        short word = memory.getWord(pc++, f1, f3);
        System.out.println("Output: " + word);
        //TODO?
    }

    private void read(boolean f1) {
        //Placeholder?
        Scanner inputScanner = new Scanner(System.in);
        short input = inputScanner.nextByte();

        re = pc;
        short address = memory.getAddress(pc++, f1);
        memory.storeWord(address, input);
    }

    private void copy(boolean f1, boolean f2, boolean f3) {
        re = pc;
        short address = memory.getAddress(pc++, f1);
        re = pc;
        short word = memory.getWord(pc++, f2, f3);
        memory.storeWord(address, word);
    }

    private boolean call(boolean f1) {
        if (memory.push(pc)) {
            pc = memory.getWord(pc++, f1, false);
            return true;
        } else
            return false;
    }

    private boolean ret() {
        short pop = memory.pop();

        if (pop >= 0) {
            pc = pop;
            return true;
        } else
            return false;
    }

    private void branch(boolean f1) {
        re = pc;
        pc = memory.getAddress(pc++, f1);
    }

    private void branchNeg(boolean f1) {
        if (acc < 0)
            branch(f1);
        else
            pc++;
    }

    private void branchPos(boolean f1) {
        if (acc > 0)
            branch(f1);
        else
            pc++;
    }

    private void branchZero(boolean f1) {
        if (acc == 0)
            branch(f1);
        else
            pc++;
    }

    private void add(boolean f1, boolean f3) {
        re = pc;
        acc += memory.getWord(pc++, f1, f3);
    }

    private void divide(boolean f1, boolean f3) {
        re = pc;
        acc /= memory.getWord(pc++, f1, f3);
    }

    private void load(boolean f1, boolean f3) {
        re = pc;
        acc = memory.getWord(pc++, f1, f3);
    }

    private void mult(boolean f1, boolean f3) {
        re = pc;
        acc *= memory.getWord(pc++, f1, f3);
    }

    private void sub(boolean f1, boolean f3) {
        re = pc;
        acc -= memory.getWord(pc++, f1, f3);
    }

    private void store(boolean f1) {
        re = pc;
        short address = memory.getAddress(pc++, f1);
        memory.storeWord(address, acc);
    }

    public void dump() {
        System.out.println("ACC -\t" + acc);
        System.out.println("RE -\t" + re);
        System.out.println("RI -\t" + ri);
        System.out.println("PC -\t" + pc);
        System.out.println("------------");
        memory.dumpMemory();
    }

}
