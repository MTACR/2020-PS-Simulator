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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
        load(file);
    }

    private String fillBinary(String numBinary) {
        String aux = "";
        while (numBinary.length() + aux.length() < 16) {
            aux += '0';
        }
        return aux + numBinary;
    }

    public void load(File file) {
        try {
            File out = new File(file.getPath() + "_bin");
            FileWriter writer = new FileWriter(out);

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    line = fillBinary(Integer.toBinaryString(Integer.parseInt(line)));
                    Interface.instance().printMessage(line);
                    writer.write(line + "\n");
                }
                writer.close();
            }
        } catch (IOException | NumberFormatException e) {
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Arquivo inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
