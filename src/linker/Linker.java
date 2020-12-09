package linker;

import linker.auxiliar.DefinitionTable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Linker {

    public static void main(String[] args) {
        args = new String[]{"output/MASMAPRG.obj"};
        ArrayList<Segment> segments = FirstPass.readSegments(args);


        DefinitionTable tgs = FirstPass.unifyDefinitions(segments);
        FirstPass.checkUsages(segments);

    }
}
