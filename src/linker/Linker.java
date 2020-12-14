package linker;

import assembler.ObjectCode;
import linker.auxiliar.DefinitionTable;
import simulator.Interface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
        Interface.instance().printMessage("Código");
        for(int i = 0; i < lines.size(); i++){
            System.out.println(i + " " + lines.get(i).toString());
        }

        Interface.instance().printMessage("Definições");
        for(Definition def : tgs.values()) {
            Interface.instance().printMessage(def.toString());
        }

        Interface.instance().printMessage("Usos");
        for(Segment seg : segments){
            for(Usage use : seg.usageTable.values()){
                Interface.instance().printMessage(use.toString());
            }
        }
    }

    public static File link(List<File> files) {
        //int offset = 3;
        //TODO iniciar arquivos aqui
        //ArrayList<Segment> segments = readSegments(files);
        //offset += tamanho da pilha
        //DefinitionTable tgs = unifyDefinitions(segments, offset);
        //checkUsages(segments);
        //SecondPass
        //ArrayList<Line> lines = updateAddresses(segments, offset);
        //updateReferences(lines, segments, tgs);

        return null;
    }

}
