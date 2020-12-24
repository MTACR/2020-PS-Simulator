/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loader;

import simulator.Interface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import linker.Line;

import static loader.Output.*;

/**
 *
 * @author gusta
 */
public class Loader {

    public static void main(String[] args) {
        Loader l = new Loader();
    }

    public Loader() {
        File file = new File("input/loader");
        //load(file);
    }

    private static String fillBinary(String numBinary) {
        String aux = "";
        while (numBinary.length() + aux.length() < 16) {
            aux += '0';
        }
        return aux + numBinary;
    }

    public static File load(ArrayList<Line> lines, int offset, String outputName) {
		String binOutput = "";
		binOutput += createStart(offset - 3);

		for (int i = offset; i < lines.size(); i++) {
			Line l = lines.get(i);
			if (l.reallocMode == 'a' || l.reallocMode == 'r') binOutput += opToBin(l.word) + "\n";
		}
		
		File fileOutput = new File("output/" + outputName + ".hpx");

        try {
            FileWriter writer = new FileWriter(fileOutput);
			
			writer.write(binOutput);
			
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		
		return fileOutput;
    }
}
