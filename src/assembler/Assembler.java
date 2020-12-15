package assembler;

import linker.Linker;
import macros.MacrosProcessor;
import simulator.Interface;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Assembler {

    public static File assemble(List<File> files) {
        List<File> files2Link = new ArrayList<>();

        for (File file : files) {
            try {
                Interface.instance().printMessage("Montando arquivo " + file.getName());
                files2Link.add(SecondPass.pass(new MacrosProcessor().process(file)));
            } catch (RuntimeException e) {
                Interface.instance().printError(e.getMessage());
            }
        }

        return Linker.link(files2Link);
    }

}
