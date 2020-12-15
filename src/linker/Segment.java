package linker;

import linker.auxiliar.DefinitionTable;
import linker.auxiliar.UsageTable;

import java.util.*;

public class Segment {
    public String fileName;
    public DefinitionTable definitionTable; //Tabela de Definição: Lista cada símbolo global definido
    public UsageTable usageTable; //Tabela de Uso: Lista cada uso interno de um símbolo global
    public ArrayList<Line> lines;
    public int stackSize;
    /*public int length;*/ //bem provavel que possa ser substituido por um lines.size()

    public Segment(String fileName, DefinitionTable definitionTable, UsageTable usageTable, ArrayList<Line> lines, int stackSize) {
        this.fileName = fileName;
        this.definitionTable = definitionTable;
        this.usageTable = usageTable;
        this.lines = lines;
        this.stackSize = stackSize;
    }

    public int length(){
        return lines.size();
    }

}
