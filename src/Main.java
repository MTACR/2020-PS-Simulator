import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("input/fatorial.txt");
        // Rodar via console
        /* 
        if (file.exists()) {
            Processor processor = new Processor(file);
            //Processor processor = new Processor(Assembler.convert(file));
            processor.dump();
            while (processor.nextInstruction());
        } 
        */
        
        // Rodar via interface
        Interface f = new Interface(Assembler.convert(file, 10));
        f.setVisible(true);
    }
}
