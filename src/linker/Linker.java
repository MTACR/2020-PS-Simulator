package linker;

import linker.auxiliar.DefinitionTable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Linker {

    public static void main(String[] args) {
        args = new String[]{"output/teste.obj"};
        ArrayList<Segment> segments = FirstPass.readSegments(args);

        DefinitionTable tgs;

        try {
            tgs = FirstPass.unifyDefinitions(segments);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try {
            checkUsages(segments);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
}
