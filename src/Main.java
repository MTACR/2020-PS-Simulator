import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("input/program1");

        if (file.exists()) {
            TestOnly test = new TestOnly();

            test.loadFileToMemory(file);
            test.nextInstruction();
            test.dumpMemory();
        }
    }

}
