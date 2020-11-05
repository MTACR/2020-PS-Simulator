import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("input/program4");

        if (file.exists()) {
            //Processor processor = new Processor(file);
            Processor processor = new Processor(Assembler.convert(file));
            processor.dump();
        }
    }
}
