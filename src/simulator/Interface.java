package simulator;

import assembler.Assembler;
import gui.CustomDocumentFilter;
import loader.Loader;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.Timer;
import javax.swing.text.*;

public class Interface extends javax.swing.JFrame {

    private Processor processor;
    private File activeFile;
    private Timer instructionTimer; // Temporizador que vai fazer o processador executar o programa inteiro no modo 0
    private boolean abort;
    private boolean running;
    private int newFileCount;

    public Interface() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        setLook();
        initComponents();
        newFileCount = 0;
        ((AbstractDocument) codeTextPane.getDocument()).setDocumentFilter(new CustomDocumentFilter(codeTextPane));

    }

    // Atualiza os valores dos Registradores na interface.
    private void updateRegisters() {
        pcValueLabel.setText(String.format("%05d", processor.getPc()));
        spValueLabel.setText(String.format("%05d", processor.getSp()));
        accValueLabel.setText(String.format("%05d", processor.getAcc()));
        mopValueLabel.setText(String.format("%03d", processor.getMop()));
        riValueLabel.setText(String.format("%05d", processor.getRi()));
        riTextLabel.setText("" + Processor.OPCODE.values()[(processor.getRi() & 15)]);
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
        
        int position = processor.getPc();
        int line = position / 4;
        int col = (position % 4 + 1);
        memoryTable.setRowSelectionInterval(line, line);
        memoryTable.setColumnSelectionInterval(col, col);
    }

    // Para evitar ArrayIndexOutOfBoundsException caso a memória não seja multipla de 4.
    private String next(short[] memory, int index) {
        if (index < memory.length) {
            return ("" + memory[index]);
        } else {
            return "";
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        mainSplit = new javax.swing.JSplitPane();
        editorSplit = new javax.swing.JSplitPane();
        codePane = new javax.swing.JPanel();
        codePaneTabs = new javax.swing.JTabbedPane();
        codeScrollPane = new javax.swing.JScrollPane();
        codeTextPane = new javax.swing.JTextPane();
        outputPane = new javax.swing.JPanel();
        asmOutLabel = new javax.swing.JLabel();
        asmOutScroll = new javax.swing.JScrollPane();
        asmOutText = new javax.swing.JTextPane();
        simulator = new javax.swing.JPanel();
        ioLabel = new javax.swing.JLabel();
        ioPanel = new javax.swing.JPanel();
        outputLabel = new javax.swing.JLabel();
        outputStreamLabel = new javax.swing.JLabel();
        registerLabel = new javax.swing.JLabel();
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
        riTextLabel = new javax.swing.JLabel();
        memoryLabel = new javax.swing.JLabel();
        MemoryScrollPane = new javax.swing.JScrollPane();
        memoryTable = new javax.swing.JTable();
        opPanel = new javax.swing.JPanel();
        opModeLabel = new javax.swing.JLabel();
        jRadioButtonMode0 = new javax.swing.JRadioButton();
        jRadioButtonMode1 = new javax.swing.JRadioButton();
        resetButton = new javax.swing.JButton();
        stepButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        menuNewFile = new javax.swing.JMenu();
        menuOpenFile = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulador");
        setSize(new java.awt.Dimension(640, 300));

        mainSplit.setDividerLocation(1000);
        mainSplit.setResizeWeight(1.0);

        editorSplit.setDividerLocation(1000);
        editorSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        editorSplit.setResizeWeight(0.8);

        codeTextPane.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        codeTextPane.setText("START TESTE\n*\nMACRO\nSCALE &RP\nMACRO\nMULTSC &A,&B,&C\nLOAD &A\nMULT &B\n*SHIFTR &RP\nSTORE &C\nMEND\nMACRO\nDIVSC &A,&B,&C\nLOAD &A\nDIV &B\n*SHIFTL &RP\nSTORE &C\nMEND\nMEND\n*\nMACRO\n&LAB DISCR &A,&B,&C,&D\n&LAB MULTSC &A,&C,TEMP1\nMULTSC TEMP1,4,TEMP1\nMULTSC &A,&B,TEMP2\nSUB TEMP1\nSTORE &D\nMEND\n*\nREAD A\nREAD B\nREAD C\nSCALE 3\nDISCR A,B,C,D\nWRITE D\nSTOP\n*\nA SPACE\nB SPACE\nC SPACE\nD SPACE\nTEMP1 SPACE\nTEMP2 SPACE\n*\nEND\n");
        codeScrollPane.setViewportView(codeTextPane);

        codePaneTabs.addTab("arquivo.asm", codeScrollPane);

        javax.swing.GroupLayout codePaneLayout = new javax.swing.GroupLayout(codePane);
        codePane.setLayout(codePaneLayout);
        codePaneLayout.setHorizontalGroup(
            codePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(codePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(codePaneTabs)
                .addContainerGap())
        );
        codePaneLayout.setVerticalGroup(
            codePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(codePaneLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(codePaneTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        editorSplit.setTopComponent(codePane);

        outputPane.setPreferredSize(new java.awt.Dimension(534, 100));

        asmOutLabel.setText("Saída do Montador");

        asmOutText.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        asmOutScroll.setViewportView(asmOutText);

        javax.swing.GroupLayout outputPaneLayout = new javax.swing.GroupLayout(outputPane);
        outputPane.setLayout(outputPaneLayout);
        outputPaneLayout.setHorizontalGroup(
            outputPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outputPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(outputPaneLayout.createSequentialGroup()
                        .addComponent(asmOutScroll)
                        .addContainerGap())
                    .addGroup(outputPaneLayout.createSequentialGroup()
                        .addComponent(asmOutLabel)
                        .addGap(0, 159, Short.MAX_VALUE))))
        );
        outputPaneLayout.setVerticalGroup(
            outputPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPaneLayout.createSequentialGroup()
                .addComponent(asmOutLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(asmOutScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                .addContainerGap())
        );

        editorSplit.setRightComponent(outputPane);

        mainSplit.setLeftComponent(editorSplit);

        simulator.setEnabled(false);

        ioLabel.setText("Saída");

        ioPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        outputLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        outputLabel.setText("00000");

        outputStreamLabel.setText("Output Stream");

        javax.swing.GroupLayout ioPanelLayout = new javax.swing.GroupLayout(ioPanel);
        ioPanel.setLayout(ioPanelLayout);
        ioPanelLayout.setHorizontalGroup(
            ioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ioPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outputStreamLabel)
                    .addComponent(outputLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ioPanelLayout.setVerticalGroup(
            ioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ioPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(outputStreamLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(outputLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        registerLabel.setText("Registradores");

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

        riTextLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        riTextLabel.setText("NOP");

        javax.swing.GroupLayout registersPanelLayout = new javax.swing.GroupLayout(registersPanel);
        registersPanel.setLayout(registersPanelLayout);
        registersPanelLayout.setHorizontalGroup(
            registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pcLabel)
                    .addComponent(spLabel)
                    .addComponent(pcValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                    .addComponent(spValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(accLabel)
                    .addComponent(mopLabel)
                    .addComponent(accValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                    .addComponent(mopValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reLabel)
                    .addComponent(riLabel)
                    .addGroup(registersPanelLayout.createSequentialGroup()
                        .addGroup(registersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(riValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                            .addComponent(reValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(riTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(riValueLabel)
                    .addComponent(riTextLabel))
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

        opModeLabel.setText("Modo de Operação");

        buttonGroup.add(jRadioButtonMode0);
        jRadioButtonMode0.setSelected(true);
        jRadioButtonMode0.setText("Modo 0");
        jRadioButtonMode0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMode0ActionPerformed(evt);
            }
        });

        buttonGroup.add(jRadioButtonMode1);
        jRadioButtonMode1.setText("Modo 1");
        jRadioButtonMode1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMode1ActionPerformed(evt);
            }
        });

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
                        .addComponent(jRadioButtonMode0)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButtonMode1))
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
                            .addComponent(jRadioButtonMode1)
                            .addComponent(jRadioButtonMode0)))
                    .addGroup(opPanelLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(opPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(resetButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stepButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout simulatorLayout = new javax.swing.GroupLayout(simulator);
        simulator.setLayout(simulatorLayout);
        simulatorLayout.setHorizontalGroup(
            simulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(simulatorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(simulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MemoryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addGroup(simulatorLayout.createSequentialGroup()
                        .addComponent(memoryLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(simulatorLayout.createSequentialGroup()
                        .addGroup(simulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ioLabel)
                            .addComponent(ioPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(simulatorLayout.createSequentialGroup()
                                .addComponent(registerLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(registersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(opPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        simulatorLayout.setVerticalGroup(
            simulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, simulatorLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(simulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(registerLabel)
                    .addComponent(ioLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(simulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ioPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(registersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addComponent(memoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MemoryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(opPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        mainSplit.setRightComponent(simulator);

        jMenu4.setText("Arquivo");

        menuNewFile.setText("Novo Arquivo");
        menuNewFile.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                menuNewFileMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        menuNewFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuNewFileMouseClicked(evt);
            }
        });
        jMenu4.add(menuNewFile);

        menuOpenFile.setText("Abrir Arquivo");
        jMenu4.add(menuOpenFile);

        menuBar.add(jMenu4);

        jMenu1.setText("Abrir Binário");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        menuBar.add(jMenu1);

        jMenu2.setText("Abrir Código");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        menuBar.add(jMenu2);

        jMenu3.setText("Executar");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        menuBar.add(jMenu3);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(mainSplit, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainSplit, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private boolean executeNextInstruction() {
        boolean errored = processor.step();
        updateGUI();
        return errored;
    }

    private void stepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepButtonActionPerformed
        // Executa nextInstruction() no processador e atualiza interface.
        // Dependendo do modo de operação selecionado, executa continuamente ou apenas uma instrução
        if (activeFile != null) {
            if (processor.getMop() == 0) { // Modo não interativo
                if (instructionTimer != null) { // Para de executar o programa se estiver executando
                    instructionTimer.stop();
                    instructionTimer = null;
                    return;
                }

                instructionTimer = new Timer(50, (java.awt.event.ActionEvent evt1) -> {
                    running = true;
                    if (abort || !executeNextInstruction()) {
                        instructionTimer.stop();
                        instructionTimer = null;
                        abort = false;
                        running = false;
                    }
                });
                instructionTimer.setRepeats(true);
                instructionTimer.start();

            } else if (processor.getMop() == 1) { // Modo debug
                executeNextInstruction();
            }
            running = false;
        }

    }//GEN-LAST:event_stepButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // Reabre arquivo e reseta a interface
        if (running) {
            abort = true;
        }
        initProcessor(activeFile);
    }//GEN-LAST:event_resetButtonActionPerformed
    //Setter para o método write
    public void setOutputLabel(short out) {
        outputLabel.setText(String.format("%05d", out));
    }

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        // Abre o menu para escolher um arquivo, se for válido carrega no processador e atualiza interface
        System.out.println("Simulator.Interface.jMenu1MouseClicked()");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            initProcessor(selectedFile);
        }
    }//GEN-LAST:event_jMenu1MouseClicked

    private void jRadioButtonMode1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMode1ActionPerformed
        // Altera o registrador de modo de operação no processador
        processor.setMop((byte) 1);
        updateGUI();
    }//GEN-LAST:event_jRadioButtonMode1ActionPerformed

    private void jRadioButtonMode0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMode0ActionPerformed
        // Altera o registrador de modo de operação no processador
        processor.setMop((byte) 0);
        updateGUI();
    }//GEN-LAST:event_jRadioButtonMode0ActionPerformed

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
        // Abre o menu para escolher um arquivo de código, se for válido carrega no processador e atualiza interface
        System.out.println("Simulator.Interface.jMenu1MouseClicked()");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            //File selectedFile = Assembler.convert(fileChooser.getSelectedFile(), 10);
            //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            //initProcessor(selectedFile);
            //TODO
            throw new RuntimeException("Precisa implementar dnv");
        }
    }//GEN-LAST:event_jMenu2MouseClicked

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        printMessage("Montando...");

        File[] files = new File[codePaneTabs.getTabCount()];
        
        File tmp = new File("tmp");
        tmp.mkdir();

        for (int i = 0; i < codePaneTabs.getTabCount(); i++) {
            String code = ((JEditorPane)(((JViewport)((JScrollPane)codePaneTabs.getComponent(i)).getComponents()[0]).getComponents()[0])).getText();
            
            if (!code.isEmpty()) {
                try {
                    files[i] = new File("tmp/" + i + ".asm");
                    FileWriter writer = new FileWriter(files[i]);
                    writer.write(code);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        initProcessor(Assembler.assemble(files));

        //Loader.load(exec);

        for (int i = 0; i < files.length; i++)
            if (files[i].exists())
                files[i].delete();

        tmp.delete();

    }//GEN-LAST:event_jMenu3MouseClicked

    private void menuNewFileMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_menuNewFileMenuKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuNewFileMenuKeyPressed

    private void menuNewFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuNewFileMouseClicked
        JScrollPane tab = newTab();
        codePaneTabs.add("novo"+newFileCount+".asm",tab);
        codePaneTabs.setSelectedIndex(codePaneTabs.indexOfComponent(tab));
        newFileCount++;
    }//GEN-LAST:event_menuNewFileMouseClicked

    private JScrollPane newTab(){
        JScrollPane tab = new JScrollPane();
        JTextPane text = new JTextPane();
        text.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        text.setText("");
	tab.setViewportView(text);
        AbstractDocument doc = (AbstractDocument) text.getDocument();
        doc.setDocumentFilter(new CustomDocumentFilter(text));
        return tab;
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            //new Interface().setVisible(true);
            instance().setVisible(true);
            instance().setExtendedState(instance().getExtendedState() | JFrame.MAXIMIZED_BOTH);
        });
    }

    private static Interface instance;

    public static synchronized Interface instance() {
        if (instance == null)
            instance = new Interface();

        return instance;
    }

    public void printError(String message) {
        System.err.println(message);

        StyledDocument doc = asmOutText.getStyledDocument();

        Style style = asmOutText.addStyle("Error", null);
        StyleConstants.setForeground(style, Color.red);

        try { doc.insertString(doc.getLength(), message + "\n", style); }
        catch (BadLocationException e){}
    }

    public void printMessage(String message) {
        System.out.println(message);

        StyledDocument doc = asmOutText.getStyledDocument();

        Style style = asmOutText.addStyle("Message", null);
        StyleConstants.setForeground(style, Color.DARK_GRAY);

        try { doc.insertString(doc.getLength(), message + "\n", style); }
        catch (BadLocationException e){}
    }

    public void clearTerminal() {
        asmOutText.setDocument(new DefaultStyledDocument());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane MemoryScrollPane;
    private javax.swing.JLabel accLabel;
    private javax.swing.JLabel accValueLabel;
    private javax.swing.JLabel asmOutLabel;
    private javax.swing.JScrollPane asmOutScroll;
    private javax.swing.JTextPane asmOutText;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JPanel codePane;
    private javax.swing.JTabbedPane codePaneTabs;
    private javax.swing.JScrollPane codeScrollPane;
    private javax.swing.JTextPane codeTextPane;
    private javax.swing.JSplitPane editorSplit;
    private javax.swing.JLabel ioLabel;
    private javax.swing.JPanel ioPanel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JRadioButton jRadioButtonMode0;
    private javax.swing.JRadioButton jRadioButtonMode1;
    private javax.swing.JSplitPane mainSplit;
    private javax.swing.JLabel memoryLabel;
    private javax.swing.JTable memoryTable;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuNewFile;
    private javax.swing.JMenu menuOpenFile;
    private javax.swing.JLabel mopLabel;
    private javax.swing.JLabel mopValueLabel;
    private javax.swing.JLabel opModeLabel;
    private javax.swing.JPanel opPanel;
    private static javax.swing.JLabel outputLabel;
    private javax.swing.JPanel outputPane;
    private javax.swing.JLabel outputStreamLabel;
    private javax.swing.JLabel pcLabel;
    private javax.swing.JLabel pcValueLabel;
    private javax.swing.JLabel reLabel;
    private javax.swing.JLabel reValueLabel;
    private javax.swing.JLabel registerLabel;
    private javax.swing.JPanel registersPanel;
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel riLabel;
    private javax.swing.JLabel riTextLabel;
    private javax.swing.JLabel riValueLabel;
    private javax.swing.JPanel simulator;
    private javax.swing.JLabel spLabel;
    private javax.swing.JLabel spValueLabel;
    private javax.swing.JButton stepButton;
    // End of variables declaration//GEN-END:variables

    // Carrega o binário e atualiza a interface
    private void initProcessor(File file) {
        if (file != null) {
            processor = new Processor(file);
            activeFile = file;
            if (jRadioButtonMode0.isSelected()) {
                processor.setMop((byte) 0);
            } else {
                processor.setMop((byte) 1);
            }
            updateGUI();

        } else
            printError("Arquivo inválido");
    }

    private void setLook() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }

    public void updateGUI() {
        updateRegisters();
        updateMemory();
    }

}
