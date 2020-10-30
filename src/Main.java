import java.io.File;

public class Main {

    public static void main(String[] args) {
        testInstructions();
    }

    private static void testData() {
        File file = new File("input/data");

        if (file.exists()) {
            TestOnly test = new TestOnly();

            test.loadFileToMemory(file);
            test.dumpMemory();
        }
    }

    private static void testInstructions() {
        File file = new File("input/instructions");

        if (file.exists()) {
            TestOnly test = new TestOnly();

            test.loadFileToMemory(file);
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
            test.doStep();
        }
    }

}
