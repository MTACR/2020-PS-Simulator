
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Processor {
    private static final short MEMORY_SIZE = 512;
    
    private short pc;
    private short acc;
    private short ri;
    private short re;
    private byte mop;

    private final Memory memory;
    private final Interface gui;
    private OnStep step;

    public interface OnStep {
        boolean onStep();
    }

    public boolean step() {
        return step.onStep();
    }

    public Processor(File file) {
        gui = null;
        memory = new Memory(MEMORY_SIZE);
        memory.loadFileToMemory(file); //Carrega programa para memória e retorna início da área de dados
        pc = memory.firstPosition();

        step = this::nextInstruction;
    }

    public Processor(File file, Interface gui) {
        this.gui = gui;
        memory = new Memory(MEMORY_SIZE);
        memory.loadFileToMemory(file); //Carrega programa para memória e retorna início da área de dados
        pc = memory.firstPosition();

        step = this::nextInstruction;
    }
    
    private boolean nextInstruction() {
        if (ri != 11) { // Se ri=11(STOP), parar execução
            if (pc == 0) {
                System.err.println("Stack overflow");
                return false;
            }

            if (pc > memory.size() || pc < 0) {
                System.err.println("Program counter out of memory bounds");
                return false;
            }
            
            memory.setDebug(pc);
            ri = memory.getWord(pc++, false, true);
            step = this::parseOpCode;
            return true;
            //return parseOpCode(opcode, f1, f2, f3);
        } else {
            return false;
        }
    }

    // Seleciona qual código a ser processado e lida com a quantidade de palavras a ser lida pros operandos
    private boolean parseOpCode() {
        boolean f1 = (ri & 32) != 0;
        boolean f2 = (ri & 64) != 0;
        boolean f3 = (ri & 128) != 0;

        TestOnly.OPCODE opcode = TestOnly.OPCODE.values()[ri & 15];
        System.out.print("\n" + opcode);
        step = this::nextInstruction;
        
        
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

    private void write(boolean f1, boolean f3) {
        // Carrega um enderço e coloca o valor na interface gráfica
        short word = memory.getWord(pc++, f1, f3);
        re = memory.getAccessed();
        gui.setOutputLabel(word);
    }

    private void read(boolean f1) {
        // Carrega o endereço do operando e abre uma janela com um campo para a entrada,
        // salva o valor lido no endereço do operando.
        short address = memory.getAddress(pc++, f1);
        Short input = null;
        do {
            try {
                input = Short.parseShort(JOptionPane.showInputDialog("Insira a entrada"));
                memory.storeWord(address, input);
            } catch (NumberFormatException ex) {
                final JPanel panel = new JPanel();
                JOptionPane.showMessageDialog(panel, "A entrada deve ser do tipo Short! (-32,768 até 32,767)", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (input == null);
    }

    private void copy(boolean f1, boolean f2, boolean f3) {
        short address = memory.getAddress(pc++, f1);
        re = memory.getAccessed();
        short word = memory.getWord(pc++, f2, f3);
        re = memory.getAccessed();
        memory.storeWord(address, word);
    }

    private boolean call(boolean f1) {
        if (memory.push(pc)) {
            pc = memory.getWord(pc++, f1, false);
            return true;
        } else {
            return false;
        }
    }

    private boolean ret() {
        short pop = memory.pop();

        if (pop >= 0) {
            pc = pop;
            return true;
        } else {
            return false;
        }
    }

    private void branch(boolean f1) {
        pc = memory.getAddress(pc/*++*/, f1); 
        re = memory.getAccessed();
    }

    private void branchNeg(boolean f1) {
        if (acc < 0) {
            branch(f1);
        } else {
            pc++;
        }
    }

    private void branchPos(boolean f1) {
        if (acc > 0) {
            branch(f1);
        } else {
            pc++;
        }
    }

    private void branchZero(boolean f1) {
        if (acc == 0) {
            branch(f1);
        } else {
            pc++;
        }
    }

    private void add(boolean f1, boolean f3) {
        acc += memory.getWord(pc++, f1, f3);
        re = memory.getAccessed();
    }

    private void divide(boolean f1, boolean f3) {
        acc /= memory.getWord(pc++, f1, f3);
        re = memory.getAccessed();
    }

    private void load(boolean f1, boolean f3) {
        acc = memory.getWord(pc++, f1, f3);
        re = memory.getAccessed();
    }

    private void mult(boolean f1, boolean f3) {
        acc *= memory.getWord(pc++, f1, f3);
        re = memory.getAccessed();
    }

    private void sub(boolean f1, boolean f3) {
        acc -= memory.getWord(pc++, f1, f3);
        re = memory.getAccessed();
    }

    private void store(boolean f1) {
        short address = memory.getAddress(pc++, f1);
        re = memory.getAccessed();
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
    
    public void setMop(byte mop) {
        this.mop = mop;
    }

    //Getters para a interface
    public short getPc() {
        return pc;
    }

    public short getSp() {
        return memory.getSp();
    }

    public short getAcc() {
        return acc;
    }

    public short getRi() {
        return ri;
    }

    public short getRe() {
        return re;
    }

    public byte getMop() {
        return mop;
    }

    public short[] getMemory() {
        return memory.getMemory();
    }
}
