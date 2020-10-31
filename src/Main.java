import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("input/program2");

        if (file.exists()) {
            TestOnly test = new TestOnly();

            test.loadFileToMemory(file);
            test.nextInstruction();
            test.dumpMemory();
        }
    }

}
