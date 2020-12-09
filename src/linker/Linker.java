package linker;

import assembler.ObjectCode;
import linker.auxiliar.DefinitionTable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static linker.FirstPass.readSegments;
import static linker.FirstPass.unifyDefinitions;
import static linker.SecondPass.updateAddresses;

public class Linker {

    public static void main(String[] args) {
        args = new String[]{"output/MASMAPRG.obj", "output/MASMAPRG1.obj"};

        //FirstPass
        ArrayList<Segment> segments = readSegments(args);
        DefinitionTable tgs = unifyDefinitions(segments);
        //checkUsages(segments);

        //SecondPass
        ArrayList<ObjectCode> list = updateAddresses(segments);


        for(ObjectCode oc : list){
            System.out.println(oc.toString());
        }
    }
}
