package assembler;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

public class ObjectCode {
    public int address;
    public int size;
    public Pair<Integer, Character>[] words;

    public ObjectCode(int address, int size, Pair<Integer, Character>... words) {
        this.address = address;
        this.size = size;
        this.words = words;
    }

    public String printWords() {
        String out = "";

        for (int i = 0; i < size; i++) {
            out += words[i].getKey() + " " + words[i].getValue() + " ";
        }

        return out;
    }

    public void offset(int offset){
        address += offset;

        for(int i = 0; i < words.length; i++){
            Pair<Integer, Character> currentWord = words[i];

            if (currentWord.getValue() == 'r')
                words[i] = new Pair<>(currentWord.getKey() + offset, currentWord.getValue());
        }
    }

    @Override
    public String toString() {
        return address +
                " " + size +
                " " + Arrays.toString(words);
    }
}


