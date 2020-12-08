package simulator;

import assembler.Assembler;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("input/fatorial_bin");

        Interface f = new Interface(file);
        f.setVisible(true);
    }

}
