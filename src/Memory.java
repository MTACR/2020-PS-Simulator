import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Memory {

    private final short[] memory;
    private final boolean[] debug; // usado pra identificar se é opcode ou dado
    private short sp; //stackpointer

    private static final short stackZero = 3; //posição do inicio da pilha //definidos assim pra facilitar possiveis modificações
    private static short stackSize = 0; // O tamanho da pilha é definido ao carregar o arquivo.
    private short accessed; //um "RE" atualizado a cada acesso na memory
    
    public Memory(int size) {
        memory = new short[size];
        debug = new boolean[size];
        sp = stackZero;
    }
    

    //Pilha

    public boolean push(short word) {  //insere na pilha
        //sp++
        if (sp > stackSize + 2) { //verifica se a pilha está cheia
            System.err.println("Stack overflow " + sp + "size: " + stackSize);
            sp = 0; //"causando um desvio para o endereço 0 (zero)"
            return false;
        } else {
            memory[sp] = word;
            sp++; // Só aumenta SP se foi inserido algo na pilha
            return true;
        }
    }

    public short pop() { //retira da pilha
        System.err.println("sp" + sp +" zero: " + stackZero + " valorPilha " + memory[sp]);  
        if (sp <= stackZero) { //verifica se a pilha está vazia 
            System.err.println("Stack underflow");
            sp = memory[2];
            return 0; // -1 iria dar OutOfBoundsException
        } else
            return memory[--sp];
    }

    public short firstPosition() {
        return (short) (memory[2] + 3);
    }

    /*public boolean isFull(){ //confere se pilha cheia
        return (sp==12);
    }

    public boolean isEmpty(){ //confere se pilha vazia
        return (sp==2);
    }*/

    //Fim pilha

    public void loadFileToMemory(File file) {
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int i = 0;
                
                while ((line = reader.readLine()) != null)
                    memory[i++] = (short) Integer.parseInt(line, 2);
                
                // TAMANHO DA PILHA É DEFINIDO DEPOIS DE CARREGAR O ARQUIVO
                stackSize = (short) memory[2]; // usar memory[2] para obter o endereço limite da pilha
            }
        } catch (IOException | NumberFormatException e) {
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Arquivo inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void storeWord(short pos, short word) {
        memory[pos] = word;
    }

    // retorna um endereço da memória dependendo do modo de endereçamento
    // f1 diz se é direto ou indireto
    public short getAddress(int pos, boolean f1, boolean f3) { 
        if (pos > memory.length || pos < 0)
            throw new RuntimeException("Out of bounds");

        if (f3){
            accessed = (short) pos;
        } else {
            short s = memory[pos];
            if (f1) {
                accessed = memory[s];
            } else {
                accessed = s;
            }
        }
        
        return accessed;
    }
    
    public short getAddress(int pos, boolean f1) { 
        return getAddress(pos, f1, false);
    }
    
    // retorna um valor da memória dependendo do modo de endereçamento
    // f1 diz se é direto ou indireto e f3 diz se é imediato
    /*public short getWord(int pos, boolean f1, boolean f3) {
        if (pos > memory.length || pos < 0)
            throw new RuntimeException("Out of bounds");

        return memory[getAddress(pos, f1, f3)];
    }*/
    public short getWord(int pos, boolean f1, boolean f3) {
        if (pos > memory.length || pos < 0)
            throw new RuntimeException("Out of bounds");

        short s = memory[pos];

        if (f3) {
            return s;
        } else if (f1) {
            short contentsAddress = memory[s];
            return memory[contentsAddress];
        } else {
            return memory[s];
        }
    }
    

    // imprime o conteudo da memória
    public void dumpMemory() {
        //System.out.println("---DUMP---");

        for (int j = 0; j < 20; j++)
           /* if (debug[j])
                System.out.println("0x" + j + " -\t" + TestOnly.OPCODE.values()[(short) ((memory[j] & 0xF000) >> 12)]);
            else*/
                System.out.println("0x" + j + " -\t" + memory[j]);
    }

    public void setDebug(int pos) {
        debug[pos] = true;
    }

    public int size() {
        return memory.length;
    }

    short getSp() {
        return sp;
    }

    public short[] getMemory() {
        return memory;
    }
    
    public short getAccessed(){ //chamado nas operações para atualizar o RE com o endereço mais recentemente acessado
        return accessed;
    }
    
}
