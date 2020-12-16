package linker;

import linker.auxiliar.DefinitionTable;
import simulator.Interface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static linker.FirstPass.*;
import static linker.SecondPass.*;

public class Linker {

    public static void main(String[] args) {
        int offset = 3; //indicador do inicio do programa + nada + tamanho máximo da pilha

        args = new String[]{"output/A.obj", "output/A.tbl"};

        //FirstPass
        ArrayList<Segment> segments = readSegments(args);
        offset += getStackSum(segments);
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
        for (Segment seg : segments){
            for(Usage use : seg.usageTable){
                System.out.println(use.toString());
            }
        }
    }

    public static File link(List<File> files) {
        Interface.instance().printMessage("Linking...");

        int offset = 3; //TODO não é 2? pq o primeiro endereço do .obj já é o tamamnho da pilha

        String[] args = new String[files.size()];

        for (int i = 0; i < args.length; i++)
            args[i] = files.get(i).getPath();

        ArrayList<Segment> segments = readSegments(args);
        offset += getStackSum(segments);
        DefinitionTable tgs = unifyDefinitions(segments, offset);
        //checkUsages(segments);

        //SecondPass
        ArrayList<Line> lines = updateAddresses(segments, offset);
        updateReferences(lines, segments, tgs);

        return null;
    }

}
