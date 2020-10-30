import java.io.File;

public class Main {

    public static void main(String[] args) {
        testData();
    }

    private static void testData() {
        File file = new File("input/program2");

        if (file.exists()) {
            TestOnly test = new TestOnly();

            test.loadFileToMemory(file);
            //test.dumpMemory();
            test.nextInstruction();
        }
    }

    private static void testInstructions() {
        File file = new File("input/instructions");

        if (file.exists()) {
            TestOnly test = new TestOnly();

            test.loadFileToMemory(file);
        }
    }

    private static void testFlags() {
        File file = new File("input/flags");

        if (file.exists()) {
            TestOnly test = new TestOnly();

            test.loadFileToMemory(file);
        }
    }

}
