import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("input/fatorial-contando-n-1.txt");

        Interface f = new Interface(Assembler.convert(file, 10));
        f.setVisible(true);
    }
}
