package linker;

import assembler.ObjectCode;
import linker.auxiliar.DefinitionTable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static linker.FirstPass.readSegments;
import static linker.FirstPass.unifyDefinitions;
import static linker.SecondPass.updateAddresses;
import static linker.SecondPass.updateReferences;

public class Linker {

    public static void main(String[] args) {
        int offset = 3; //indicador do inicio do programa + nada + tamanho máximo da pilha

        args = new String[]{"output/firstpass.obj", "output/MASMAPRG1.obj"};

        //FirstPass
        ArrayList<Segment> segments = readSegments(args);

        //offset += tamanho da pilha

        DefinitionTable tgs = unifyDefinitions(segments, offset);
        //checkUsages(segments);

        //SecondPass
        ArrayList<Line> lines = updateAddresses(segments, offset);

        updateReferences(lines, segments, tgs);

        //Prints
        System.out.println("Código");
        for(int i = 0; i < lines.size(); i++){
            System.out.println(i + " " + lines.get(i).toString());
        }

        System.out.println("Definições");
        for(Definition def : tgs.values()) {
            System.out.println(def.toString());
        }

        System.out.println("Usos");
        for(Segment seg : segments){
            for(Usage use : seg.usageTable.values()){
                System.out.println(use.toString());
            }
        }
    }
}
