
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gusta
 */
public class Interface extends javax.swing.JFrame {

    private Processor processor;
    private File activeFile;
    private Timer instructionTimer; // Temporizador que vai fazer o processador executar o programa inteiro no modo 0
    static public short out;

    /**
     * Creates new form Interface
     */
    public Interface() {
        setLook();
        initComponents();
        initProcessor(null);
    }

    public Interface(File file) {
        setLook();
        initComponents();
        initProcessor(file);
    }

    // Atualiza os valores dos Registradores na interface.
    private void updateRegisters() {
        pcValueLabel.setText(String.format("%05d", processor.getPc()));
        spValueLabel.setText(String.format("%05d", processor.getSp()));
        accValueLabel.setText(String.format("%05d", processor.getAcc()));
        mopValueLabel.setText(String.format("%03d", processor.getMop()));
        //riValueLabel.setText(String.format("%05d", processor.getRi()));
        riValueLabel.setText("" + TestOnly.OPCODE.values()[processor.getRi()]);
        reValueLabel.setText(String.format("%05d", processor.getRe()));
    }

    // Carrega o vetor de memória da máquina e insere na tabela da interface.
    private void updateMemory() {
        short memory[] = processor.getMemory();
        DefaultTableModel model = (DefaultTableModel) memoryTable.getModel();
        int i = 0;
        model.setRowCount(0);
        while (i < memory.length) {
            model.addRow(new Object[]{i, next(memory, i++), next(memory, i++), next(memory, i++), next(memory, i++)});
        }
        memoryTable.setCellSelectionEnabled(true);
        int line = processor.getPc() / 4;
        int col = (processor.getPc() % 4 + 1);
        memoryTable.setRowSelectionInterval(line, line);
        memoryTable.setColumnSelectionInterval(col, col);

    }

    // Para evitar ArrayIndexOutOfBoundsException caso a memória não seja multipla de 4.
    private String next(short[] memory, int index) {
        //if (index < memory.length) return String.format("%05d",memory[index]);
        if (index < memory.length) {
            return ("" + memory[index]);
        } else {
            return "";
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        registersPanel = new javax.swing.JPanel();
        pcLabel = new javax.swing.JLabel();
        spLabel = new javax.swing.JLabel();
        accLabel = new javax.swing.JLabel();
        pcValueLabel = new javax.swing.JLabel();
        spValueLabel = new javax.swing.JLabel();
        mopValueLabel = new javax.swing.JLabel();
        mopLabel = new javax.swing.JLabel();
        accValueLabel = new javax.swing.JLabel();
        riLabel = new javax.swing.JLabel();
        riValueLabel = new javax.swing.JLabel();
        reLabel = new javax.swing.JLabel();
        reValueLabel = new javax.swing.JLabel();
        opPanel = new javax.swing.JPanel();
        opModeLabel = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        resetButton = new javax.swing.JButton();
        stepButton = new javax.swing.JButton();
        registerLabel = new javax.swing.JLabel();
        memoryLabel = new javax.swing.JLabel();
        MemoryScrollPane = new javax.swing.JScrollPane();
        memoryTable = new javax.swing.JTable();
        ioPanel = new javax.swing.JPanel();
        outputLabel = new javax.swing.JLabel();
        jLabelOutput = new javax.swing.JLabel();
        inputTextField = new javax.swing.JTextField();
        jLabelInput = new javax.swing.JLabel();
        ioLabel = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(640, 300));

        registersPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        pcLabel.setText("PC");

        spLabel.setText("SP");

        accLabel.setText("ACC");

        pcValueLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        pcValueLabel.setText("00000");

        spValueLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        spValueLabel.setText("00000");

        mopValueLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        mopValueLabel.setText("000");

        mopLabel.setText("MOP");

        accValueLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        accValueLabel.setText("00000");

        riLabel.setText("RI");

        riValueLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        riValueLabel.setText("00000");

        reLabel.setText("RE");

        reValueLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        reValueLabel.setText("00000");

        javax.swing.GroupLayout registersPanelLayout = new javax.swing.GroupLayout(registersPanel);
        registersPanel.setLayout(registersPanelLayout);
        registersPanelLayout.setHorizontalGroup(
            registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pcLabel)
                    .addComponent(spLabel)
                    .addComponent(pcValueLabel)
                    .addComponent(spValueLabel))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(accLabel)
                    .addComponent(mopValueLabel)
                    .addComponent(mopLabel)
                    .addComponent(accValueLabel))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reValueLabel)
                    .addComponent(reLabel)
                    .addComponent(riValueLabel)
                    .addComponent(riLabel))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        registersPanelLayout.setVerticalGroup(
            registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(pcLabel)
                    .addComponent(accLabel)
                    .addComponent(riLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(pcValueLabel)
                    .addComponent(accValueLabel)
                    .addComponent(riValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(mopLabel)
                    .addComponent(spLabel)
                    .addComponent(reLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(spValueLabel)
                    .addComponent(mopValueLabel)
                    .addComponent(reValueLabel))
                .addContainerGap())
        );

        opModeLabel.setText("Modo de Operação");

        buttonGroup.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Modo 0");

        buttonGroup.add(jRadioButton2);
        jRadioButton2.setText("Modo 1");

        resetButton.setText("Reset");
        resetButton.setMaximumSize(null);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        stepButton.setText("Step");
        stepButton.setMaximumSize(null);
        stepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout opPanelLayout = new javax.swing.GroupLayout(opPanel);
        opPanel.setLayout(opPanelLayout);
        opPanelLayout.setHorizontalGroup(
            opPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(opPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(opPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(opPanelLayout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2))
                    .addComponent(opModeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(stepButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        opPanelLayout.setVerticalGroup(
            opPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(opPanelLayout.createSequentialGroup()
                .addGroup(opPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(opPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(opModeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(opPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButton2)
                            .addComponent(jRadioButton1)))
                    .addGroup(opPanelLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(opPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(resetButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stepButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        registerLabel.setText("Registradores");

        memoryLabel.setText("Memória");

        MemoryScrollPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        memoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Endereço", "Valor (+0)", "Valor (+1)", "Valor (+2)", "Valor (+3)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Short.class, java.lang.Short.class, java.lang.Short.class, java.lang.Short.class, java.lang.Short.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        memoryTable.setMinimumSize(new java.awt.Dimension(60, 60));
        memoryTable.getTableHeader().setReorderingAllowed(false);
        MemoryScrollPane.setViewportView(memoryTable);
        if (memoryTable.getColumnModel().getColumnCount() > 0) {
            memoryTable.getColumnModel().getColumn(0).setResizable(false);
            memoryTable.getColumnModel().getColumn(1).setResizable(false);
            memoryTable.getColumnModel().getColumn(2).setResizable(false);
            memoryTable.getColumnModel().getColumn(3).setResizable(false);
            memoryTable.getColumnModel().getColumn(4).setResizable(false);
        }

        ioPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        outputLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        outputLabel.setText("00000");

        jLabelOutput.setText("Saida");

        inputTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        inputTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputTextFieldActionPerformed(evt);
            }
        });

        jLabelInput.setText("Entrada");

        javax.swing.GroupLayout ioPanelLayout = new javax.swing.GroupLayout(ioPanel);
        ioPanel.setLayout(ioPanelLayout);
        ioPanelLayout.setHorizontalGroup(
            ioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ioPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                    .addComponent(jLabelOutput)
                    .addComponent(jLabelInput)
                    .addComponent(outputLabel))
                .addContainerGap())
        );
        ioPanelLayout.setVerticalGroup(
            ioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ioPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabelInput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelOutput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        ioLabel.setText("I/O");

        jMenu1.setText("Abrir Binário");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar2.add(jMenu1);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(memoryLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ioLabel)
                            .addComponent(ioPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(registerLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(registersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(MemoryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                    .addComponent(opPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(registerLabel)
                    .addComponent(ioLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ioPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(registersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(memoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MemoryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(opPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private boolean executeNextInstruction() {
        boolean errored = processor.nextInstruction();
        updateRegisters();
        updateMemory();
        return errored;
    }

    private void stepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepButtonActionPerformed
        // Executa nextInstruction() no processador e atualiza interface
        if (activeFile != null) {
            if (jRadioButton1.isSelected()) { // Modo não interativo
                if (instructionTimer != null) { // Para de executar o programa se estiver executando
                    instructionTimer.stop();
                    return;
                }

                instructionTimer = new Timer(100, new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        if (!executeNextInstruction())
                            instructionTimer.stop();
                    }
                });
                instructionTimer.setRepeats(true);
                instructionTimer.start();
            } else if (jRadioButton2.isSelected()) { // Modo debug
                executeNextInstruction();
            }
        }
    }//GEN-LAST:event_stepButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // Reabre arquivo e reseta a interface
        initProcessor(activeFile);
    }//GEN-LAST:event_resetButtonActionPerformed

    private void inputTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputTextFieldActionPerformed
        // Testa se a entrada é Short, caso contrário apaga input e mostra uma mensagem de erro
        try {
            short value = Short.parseShort(inputTextField.getText());
            //outputLabel.setText(String.format("%05d", value));
            processor.setAcc(value);
            updateRegisters();
            inputTextField.setText("");
        } catch (NumberFormatException ex) {
            inputTextField.setText("");
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "A entrada deve ser do tipo Short! (-32,768 até 32,767)", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_inputTextFieldActionPerformed
    //Setter para o método write
    static protected void setOutputLabel(short out) {
        outputLabel.setText(String.format("%05d", out));
    }
    
    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        // Abre o menu para escolher um arquivo, se for válido carrega no processador e atualiza interface
        System.out.println("Interface.jMenu1MouseClicked()");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            initProcessor(selectedFile);
        }
    }//GEN-LAST:event_jMenu1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
               /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane MemoryScrollPane;
    private javax.swing.JLabel accLabel;
    private javax.swing.JLabel accValueLabel;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JTextField inputTextField;
    private javax.swing.JLabel ioLabel;
    private javax.swing.JPanel ioPanel;
    private javax.swing.JLabel jLabelInput;
    private javax.swing.JLabel jLabelOutput;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JLabel memoryLabel;
    private javax.swing.JTable memoryTable;
    private javax.swing.JLabel mopLabel;
    private javax.swing.JLabel mopValueLabel;
    private javax.swing.JLabel opModeLabel;
    private javax.swing.JPanel opPanel;
    private static javax.swing.JLabel outputLabel;
    private javax.swing.JLabel pcLabel;
    private javax.swing.JLabel pcValueLabel;
    private javax.swing.JLabel reLabel;
    private javax.swing.JLabel reValueLabel;
    private javax.swing.JLabel registerLabel;
    private javax.swing.JPanel registersPanel;
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel riLabel;
    private javax.swing.JLabel riValueLabel;
    private javax.swing.JLabel spLabel;
    private javax.swing.JLabel spValueLabel;
    private javax.swing.JButton stepButton;
    // End of variables declaration//GEN-END:variables

    // Carrega o binário e atualiza a interface
    private void initProcessor(File file) {
        if (file != null) {
            processor = new Processor(file);
            activeFile = file;
            updateRegisters();
            updateMemory();
            updateMemory();
        }
    }

    private void setLook() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
