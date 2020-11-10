import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("input/testbin");
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
        Interface f = new Interface(file);
        f.setVisible(true);
    }
}
