package assembler;

import javafx.util.Pair;
import java.util.List;

public class ObjectCode {
    public int address;
    public int size;
    public List<Pair<Integer, Character>> word;

    public ObjectCode(int address, int size, List<Pair<Integer, Character>> word) {
        this.address = address;
        this.size = size;
        this.word = word;
    }

    public String printWords() {
        String out = "";

        for (int i = 0; i < size; i++) {
            out += word.get(i).getKey() + " " + word.get(i).getValue() + " ";
        }

        return out;
    }

}


