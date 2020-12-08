package linker;

import assembler.ObjectCode;
import javafx.util.Pair;
import linker.Definition;
import linker.Usage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Segment {
    public String fileName;
    public HashMap<String, Definition> definitionTable; //Tabela de Definição: Lista cada símbolo global definido
    public ArrayList<Usage> usageTable; //Tabela de Uso: Lista cada uso interno de um símbolo global
    public ArrayList<ObjectCode> lines;
    public int length;

    public Segment(String fileName) {
        this.fileName = fileName;
        this.definitionTable = new HashMap<>();
        this.usageTable = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.length = 0;
    }

    public void readSegment(){ //TESTE DE LEITURA
        try {
            File file = new File(fileName);
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();

                String[] sl;

                if(line.trim().equals("DEFTAB")){
                    while((line = reader.readLine()) != null){
                        if(!line.trim().equals("DEFEND")){
                            sl = line.split(" ");
                            Definition def = new Definition(sl[0], Integer.parseInt(sl[1]), sl[2].charAt(0));
                            System.out.println(def.toString());
                            definitionTable.put(def.symbol, def);
                        } else {
                            break;
                        }
                    }
                    line = reader.readLine();
                }

                if(line.trim().equals("USETAB")){
                    while((line = reader.readLine()) != null){
                        if(!line.trim().equals("USEEND")){
                            sl = line.split(" ");
                            Usage use = new Usage(sl[0], Integer.parseInt(sl[1]), sl[2].charAt(0));
                            System.out.println(use.toString());
                            usageTable.add(use);
                        } else {
                            break;
                        }
                    }
                    line = reader.readLine();
                }

                while (line != null) {
                    sl = line.split(" ");

                    int address = Integer.parseInt(sl[0]);
                    int size = Integer.parseInt(sl[1]);

                    length += size;

                    List<Pair<Integer, String>> words = new ArrayList<>();

                    for(int i = 2; i < (size*2)+2 ; ){
                        int op = Integer.parseInt(sl[i]);
                        i++;
                        String mode = sl[i];
                        i++;
                        words.add(new Pair(op, mode));
                    }
                    ObjectCode oc = new ObjectCode(address, size, words);
                    System.out.println(oc.printWords());
                    lines.add(oc);
                    line = reader.readLine();
                }
            }
        } catch (IOException | NumberFormatException e) {
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Arquivo inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
