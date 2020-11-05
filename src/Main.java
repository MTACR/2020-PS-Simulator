import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("input/program3");

        if (file.exists()) {
			Assembler assembler = new Assembler();
			file = assembler.convert(file);
					
            TestOnly test = new TestOnly();

            test.loadFileToMemory(file);
            try {
                test.nextInstruction();
                test.dumpMemory();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Processor processor = new Processor(file);
            //processor.dump();
        }
    }
}
