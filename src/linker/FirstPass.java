package linker;

import assembler.ObjectCode;
import javafx.util.Pair;
import linker.auxiliar.DefinitionTable;
import linker.auxiliar.UsageTable;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirstPass {
    public static ArrayList<Segment> readSegments(String[] fileNames){ //Protótipo de leitura de leitura, considerando que as tabelas de definição e uso estão no inicio do arquivo
        ArrayList<Segment> segments = new ArrayList<>();
        for(String fileName : fileNames) {
            DefinitionTable definitionTable = new DefinitionTable();
            UsageTable usageTable = new UsageTable();
            ArrayList<ObjectCode> lines = new ArrayList<>();
            int length = 0;

            try {
                File file = new File(fileName);
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line = reader.readLine();

                    String[] sl;

                    if (line.trim().equals("DEFTAB")) {
                        while ((line = reader.readLine()) != null) {
                            if (!line.trim().equals("DEFEND")) {
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

                    if (line.trim().equals("USETAB")) {
                        while ((line = reader.readLine()) != null) {
                            if (!line.trim().equals("USEEND")) {
                                sl = line.split(" ");
                                Usage use = new Usage(sl[0], Integer.parseInt(sl[1]), sl[2].charAt(0));
                                System.out.println(use.toString());
                                usageTable.put(use.symbol, use);
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

                        for (int i = 2; i < (size * 2) + 2; ) {
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


            segments.add(new Segment(fileName, definitionTable, usageTable, lines, length));
        }
        return segments;
    }

    public static DefinitionTable unifyDefinitions(ArrayList<Segment> segments) throws Exception {
        DefinitionTable tgs = (DefinitionTable) segments.get(0).definitionTable.clone(); //Tabela de Símbolos Globais (TSG): Armazena todos os símbolos globais definidos. União das tabelas de definição dos diferentes segmentos.
        int offset = segments.get(0).length;

        for(int i = 1; i < segments.size(); i++){
            Segment seg = segments.get(i);

            for(Map.Entry defEntry : seg.definitionTable.entrySet()) {
                Definition def = (Definition) defEntry.getValue();

                if(tgs.get(def.symbol) == null){
                    def.offset(offset);
                    tgs.put(def.symbol, def);

                } else {
                    throw new Exception("Redefined Symbol in " + seg.fileName + ": " + def.symbol);
                }
            }
        }
        return tgs;
    }

    public static void checkUsages(ArrayList<Segment> segments) throws Exception { //Provavelmente desnecessario, deve ser possivel fazer esse teste em uma etada posterior do ligador
        for (Segment segUse : segments) {
            for (Map.Entry use : segUse.usageTable.entrySet()) {
                String key = (String) use.getKey();
                boolean found = false;
                for (Segment segDef : segments) {
                    if (segUse != segDef) { //Bem provavelmente desnecessario
                        if (segDef.definitionTable.get(key) != null) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    throw new Exception("Undefined Symbol in " + segUse.fileName + ": " + key + "'s definition not found");
                }
            }
        }
    }

}
