package linker;

import java.util.ArrayList;

public class Linker {

    public static void main(String[] args) {
        ArrayList<Segment> segments = new ArrayList<>();
        ArrayList<Definition> tgs = new ArrayList<>();      //Tabela de Símbolos Globais (TSG): Armazena todos os símbolos globais definidos. União das tabelas de definição dos diferentes segmentos.

        for(String filename: args){
            Segment seg = new Segment(filename);
            seg.readSegment();
            segments.add(seg);
        }
    }

}
