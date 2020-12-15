package simulator;

import assembler.Assembler;
import gui.CustomDocumentFilter;
import loader.Loader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import javax.swing.undo.*;
import java.util.List;

public class Interface extends javax.swing.JFrame {

    private Processor processor;
    private File activeFile;
    private Timer instructionTimer; // Temporizador que vai fazer o processador executar o programa inteiro no modo 0
    private boolean abort;
    private boolean running;
    private int newFileCount;
    private Map<String, String> activeFilesList = new HashMap<>();
    final UndoManager undo;

    public Interface() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        setLook();
        initComponents();
        newFileCount = 0;
        newFile();
        mainSplit.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "none");
        editorSplit.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "none");
        undo = new UndoManager();
        DefaultCaret caret = (DefaultCaret)asmOutText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        asmOutText.setEditable(false);
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
        outputPane = new javax.swing.JPanel();
        asmOutLabel = new javax.swing.JLabel();
        asmOutScroll = new javax.swing.JScrollPane();
        asmOutText = new javax.swing.JTextPane();
        cleanAsmOutBtn = new javax.swing.JButton();
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
        toolbar = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        newFileButton = new javax.swing.JButton();
        openFileButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        redoButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        runButton = new javax.swing.JButton();
        run1Button = new javax.swing.JButton();
        reset1Button = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        menuNew = new javax.swing.JMenuItem();
        menuOpen = new javax.swing.JMenuItem();
        menuClose = new javax.swing.JMenuItem();
        menuCloseAll = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuSave = new javax.swing.JMenuItem();
        menuSaveAs = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuQuit = new javax.swing.JMenuItem();
        menuRun = new javax.swing.JMenu();
        menuRunFile = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulador");
        setSize(new java.awt.Dimension(640, 300));

        mainSplit.setDividerLocation(1000);
        mainSplit.setResizeWeight(1.0);

        editorSplit.setDividerLocation(500);
        editorSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        editorSplit.setResizeWeight(0.8);

        javax.swing.GroupLayout codePaneLayout = new javax.swing.GroupLayout(codePane);
        codePane.setLayout(codePaneLayout);
        codePaneLayout.setHorizontalGroup(
            codePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(codePaneTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        codePaneLayout.setVerticalGroup(
            codePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(codePaneTabs, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
        );

        editorSplit.setTopComponent(codePane);

        outputPane.setPreferredSize(new java.awt.Dimension(534, 100));

        asmOutLabel.setText("Saída do Montador");

        asmOutText.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        asmOutScroll.setViewportView(asmOutText);

        cleanAsmOutBtn.setText("Limpar");
        cleanAsmOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanAsmOutBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout outputPaneLayout = new javax.swing.GroupLayout(outputPane);
        outputPane.setLayout(outputPaneLayout);
        outputPaneLayout.setHorizontalGroup(
            outputPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outputPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(outputPaneLayout.createSequentialGroup()
                        .addComponent(asmOutLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(outputPaneLayout.createSequentialGroup()
                        .addComponent(cleanAsmOutBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(asmOutScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
        );
        outputPaneLayout.setVerticalGroup(
            outputPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(asmOutLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outputPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(asmOutScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, outputPaneLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cleanAsmOutBtn)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addComponent(MemoryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(opPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        mainSplit.setRightComponent(simulator);

        toolbar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jPanel1.setAlignmentX(0.0F);
        jPanel1.setAlignmentY(0.0F);

        newFileButton.setIcon(new javax.swing.ImageIcon("D:\\Projects\\PS-1\\res\\new.png")); // NOI18N
        newFileButton.setBorderPainted(false);
        newFileButton.setContentAreaFilled(false);
        newFileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newFileButtonMouseClicked(evt);
            }
        });
        jPanel1.add(newFileButton);

        openFileButton.setIcon(new javax.swing.ImageIcon("D:\\Projects\\PS-1\\res\\open.png")); // NOI18N
        openFileButton.setBorderPainted(false);
        openFileButton.setContentAreaFilled(false);
        openFileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openFileButtonMouseClicked(evt);
            }
        });
        jPanel1.add(openFileButton);

        saveButton.setIcon(new javax.swing.ImageIcon("D:\\Projects\\PS-1\\res\\save.png")); // NOI18N
        saveButton.setBorderPainted(false);
        saveButton.setContentAreaFilled(false);
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveButtonMouseClicked(evt);
            }
        });
        jPanel1.add(saveButton);

        undoButton.setIcon(new javax.swing.ImageIcon("D:\\Projects\\PS-1\\res\\undo.png")); // NOI18N
        undoButton.setBorderPainted(false);
        undoButton.setContentAreaFilled(false);
        undoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                undoButtonMouseClicked(evt);
            }
        });
        jPanel1.add(undoButton);

        redoButton.setIcon(new javax.swing.ImageIcon("D:\\Projects\\PS-1\\res\\redo.png")); // NOI18N
        redoButton.setBorderPainted(false);
        redoButton.setContentAreaFilled(false);
        redoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                redoButtonMouseClicked(evt);
            }
        });
        jPanel1.add(redoButton);

        toolbar.add(jPanel1);

        jSeparator3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jSeparator3.setMinimumSize(new java.awt.Dimension(100, 0));
        toolbar.add(jSeparator3);

        runButton.setIcon(new javax.swing.ImageIcon("D:\\Projects\\PS-1\\res\\run.png")); // NOI18N
        runButton.setBorderPainted(false);
        runButton.setContentAreaFilled(false);
        runButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                runButtonMouseClicked(evt);
            }
        });
        jPanel2.add(runButton);

        run1Button.setIcon(new javax.swing.ImageIcon("D:\\Projects\\PS-1\\res\\step.png")); // NOI18N
        run1Button.setBorderPainted(false);
        run1Button.setContentAreaFilled(false);
        run1Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                run1ButtonMouseClicked(evt);
            }
        });
        jPanel2.add(run1Button);

        reset1Button.setIcon(new javax.swing.ImageIcon("D:\\Projects\\PS-1\\res\\reset.png")); // NOI18N
        reset1Button.setBorderPainted(false);
        reset1Button.setContentAreaFilled(false);
        reset1Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reset1ButtonMouseClicked(evt);
            }
        });
        jPanel2.add(reset1Button);

        toolbar.add(jPanel2);

        jMenu4.setText("Arquivo");
        jMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu4ActionPerformed(evt);
            }
        });

        menuNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuNew.setText("Novo");
        menuNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewActionPerformed(evt);
            }
        });
        jMenu4.add(menuNew);

        menuOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuOpen.setText("Abrir ...");
        menuOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenActionPerformed(evt);
            }
        });
        jMenu4.add(menuOpen);

        menuClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuClose.setText("Fechar");
        menuClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCloseActionPerformed(evt);
            }
        });
        jMenu4.add(menuClose);

        menuCloseAll.setText("Fechar Todos");
        menuCloseAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCloseAllActionPerformed(evt);
            }
        });
        jMenu4.add(menuCloseAll);

        jSeparator1.setEnabled(false);
        jMenu4.add(jSeparator1);

        menuSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuSave.setText("Salvar");
        menuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSaveActionPerformed(evt);
            }
        });
        jMenu4.add(menuSave);

        menuSaveAs.setText("Salvar Como ...");
        menuSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSaveAsActionPerformed(evt);
            }
        });
        jMenu4.add(menuSaveAs);
        jMenu4.add(jSeparator2);

        menuQuit.setText("Sair");
        menuQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuQuitActionPerformed(evt);
            }
        });
        jMenu4.add(menuQuit);

        menuBar.add(jMenu4);

        menuRun.setText("Executar");
        menuRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRunActionPerformed(evt);
            }
        });

        menuRunFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        menuRunFile.setText("Executar");
        menuRunFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRunFileActionPerformed(evt);
            }
        });
        menuRun.add(menuRunFile);

        menuBar.add(menuRun);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainSplit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1077, Short.MAX_VALUE)
            .addComponent(toolbar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(toolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainSplit, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
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

    private void menuCloseAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCloseAllActionPerformed
        codePaneTabs.removeAll();
    }//GEN-LAST:event_menuCloseAllActionPerformed

    private void menuNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewActionPerformed
        newFile();
    }//GEN-LAST:event_menuNewActionPerformed

    private void newFile() {
        JScrollPane tab = newTab();
        codePaneTabs.add("novo" + newFileCount + ".asm", tab);
        codePaneTabs.setSelectedIndex(codePaneTabs.indexOfComponent(tab));
        newFileCount++;
    }

    private void menuCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCloseActionPerformed
        codePaneTabs.remove(codePaneTabs.getSelectedIndex());
    }//GEN-LAST:event_menuCloseActionPerformed

    private void menuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenActionPerformed
        // Abre o menu para escolher um arquivo, se for válido carrega no processador e atualiza interface
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.asm", "asm"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                openFile(selectedFile);
                activeFilesList.put(selectedFile.getName(), selectedFile.getPath());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_menuOpenActionPerformed

    private void menuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSaveActionPerformed
        String fileName, path, content;

        JViewport viewport = ((JScrollPane) codePaneTabs.getSelectedComponent()).getViewport();
        JTextPane textPane = (JTextPane) viewport.getView();
        content = textPane.getText();
        fileName = codePaneTabs.getTitleAt(codePaneTabs.getSelectedIndex());
        FileWriter writer = null;
        try {
            if (activeFilesList.containsKey(fileName)) {
                System.out.println("simulator.Interface.menuSaveActionPerformed()");
                path = activeFilesList.get(fileName);
                File f = new File(path);
                writer = new FileWriter(f);
                writer.write(content);
                writer.close();
                activeFilesList.put(f.getName(), f.getPath());
            } else {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                fileChooser.setSelectedFile(new File(fileName));
                fileChooser.setFileFilter(new FileNameExtensionFilter("*.asm", "asm"));

                int result = fileChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    fileName = fileChooser.getSelectedFile().getName();
                    path = fileChooser.getCurrentDirectory().toString();
                    System.out.println(path + "\\" + fileName);
                    File f = new File(path + "\\" + fileName);
                    writer = new FileWriter(f);
                    writer.write(content);
                    writer.close();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuSaveActionPerformed

    private void menuRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRunActionPerformed
        System.out.println("simulator.Interface.menuRunActionPerformed()");
    }//GEN-LAST:event_menuRunActionPerformed

    private void jMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu4ActionPerformed
    }//GEN-LAST:event_jMenu4ActionPerformed

    private void menuSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSaveAsActionPerformed
        String fileName, path, content;
        JViewport viewport = ((JScrollPane) codePaneTabs.getSelectedComponent()).getViewport();
        JTextPane textPane = (JTextPane) viewport.getView();
        content = textPane.getText();
        fileName = codePaneTabs.getTitleAt(codePaneTabs.getSelectedIndex());
        FileWriter writer = null;
        try {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("*.asm", "asm"));
            path = fileChooser.getCurrentDirectory().toString();
            if (activeFilesList.containsKey(fileName)) {
                fileChooser.setSelectedFile(new File(activeFilesList.get(fileName)));
            }
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                fileName = fileChooser.getSelectedFile().getName();
                System.out.println(path + "\\" + fileName);
                File f = new File(path + "\\" + fileName);
                writer = new FileWriter(f);
                writer.write(content);
                writer.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuSaveAsActionPerformed

    private void menuQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuQuitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_menuQuitActionPerformed

    private void menuRunFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRunFileActionPerformed
        clearTerminal();
        printMessage("Montando...");

        List<File> files = new ArrayList<>();
        File tmp = new File("tmp");
        tmp.mkdir();

        for (int i = 0; i < codePaneTabs.getTabCount(); i++) {
            String code = ((JEditorPane)(((JViewport)((JScrollPane)codePaneTabs.getComponent(i)).getComponents()[0]).getComponents()[0])).getText();

            /*JViewport viewport = ((JScrollPane) codePaneTabs.getTabComponentAt(i)).getViewport();
            JTextPane textPane = (JTextPane) viewport.getView();
            String code = textPane.getText();*/
            
            if (!code.isEmpty()) {
                try {
                    File file = new File("tmp/" + codePaneTabs.getTitleAt(i));
                    files.add(file);
                    FileWriter writer = new FileWriter(file);
                    writer.write(code);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        initProcessor(Assembler.assemble(files));

        for (File file : files)
            file.delete();
    }//GEN-LAST:event_menuRunFileActionPerformed

    private void cleanAsmOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanAsmOutBtnActionPerformed
        clearTerminal();
    }//GEN-LAST:event_cleanAsmOutBtnActionPerformed

    private void openFileButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openFileButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_openFileButtonMouseClicked

    private void newFileButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newFileButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_newFileButtonMouseClicked

    private void runButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_runButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_runButtonMouseClicked

    private void run1ButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_run1ButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_run1ButtonMouseClicked

    private void saveButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_saveButtonMouseClicked

    private void undoButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_undoButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_undoButtonMouseClicked

    private void redoButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redoButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_redoButtonMouseClicked

    private void reset1ButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reset1ButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_reset1ButtonMouseClicked

    private void openFile(File file) throws FileNotFoundException, IOException {
        JScrollPane tab = new JScrollPane();
        JTextPane text = new JTextPane();
        text.setFont(new java.awt.Font("Consolas", 0, 14));
        FileReader fr = new FileReader(file);
        text.read(fr, null);
        tab.setViewportView(text);
        AbstractDocument doc = (AbstractDocument) text.getDocument();
        doc.setDocumentFilter(new CustomDocumentFilter(text)); //TODO botar listener pro redo aqui tbm?
        codePaneTabs.add(file.getName(), tab);
        codePaneTabs.setSelectedIndex(codePaneTabs.indexOfComponent(tab));
        newFileCount++;
    }

    private JScrollPane newTab() {
        JScrollPane tab = new JScrollPane();
        JTextPane text = new JTextPane();
        text.setFont(new java.awt.Font("Consolas", 0, 14));

        JTextPane lines = new JTextPane();
        lines.setText("1");
        lines.setEditable(false);
        lines.setFocusable(false);
        lines.setOpaque(true);
        lines.setFont(new java.awt.Font("Consolas", 0, 14));
        lines.getMargin().set(5, 10, 0, 0);
        StyledDocument style = lines.getStyledDocument();
        SimpleAttributeSet align= new SimpleAttributeSet();
        StyleConstants.setAlignment(align, StyleConstants.ALIGN_RIGHT);
        style.setParagraphAttributes(0, style.getLength(), align, false);

        text.getDocument().addDocumentListener(new DocumentListener() {
            public String getText() {

                int caretPosition = text.getDocument().getLength();
                Element root = text.getDocument().getDefaultRootElement();
                String text = "1" + "\n";

                for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                    text += i + "\n";
                }

                return text;
            }
            @Override
            public void changedUpdate(DocumentEvent de) {
                lines.setText(getText());
            }
            @Override
            public void insertUpdate(DocumentEvent de) {
                lines.setText(getText());
            }
            @Override
            public void removeUpdate(DocumentEvent de) {
                lines.setText(getText());
            }
            
        });

        text.getDocument().addUndoableEditListener((UndoableEditEvent evt) -> {
            if (!evt.getEdit().getPresentationName().equals("alteração de estilo"))
                undo.addEdit(evt.getEdit());
        });

        text.getActionMap().put("Undo", new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (undo.canUndo()) {
                        undo.undo();
                    }
                } catch (CannotUndoException e) {
                }
            }
        });
        text.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

       // tab.getViewport().add(text);
        tab.setRowHeaderView(lines);

        tab.setViewportView(text);
        AbstractDocument doc = (AbstractDocument) text.getDocument();
        doc.setDocumentFilter(new CustomDocumentFilter(text));
        return tab;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            instance().setVisible(true);
            instance().setExtendedState(instance().getExtendedState() | JFrame.MAXIMIZED_BOTH);
        });
    }

    private static Interface instance;

    public static synchronized Interface instance() {
        if (instance == null) {
            instance = new Interface();
        }

        return instance;
    }

    public void printError(String message) {
        System.err.println(message);

        StyledDocument doc = asmOutText.getStyledDocument();

        Style style = asmOutText.addStyle("Error", null);
        StyleConstants.setForeground(style, Color.red);

        try {
            doc.insertString(doc.getLength(), message + "\n", style);
        } catch (BadLocationException e) {}
    }

    public void printMessage(String message) {
        System.out.println(message);

        StyledDocument doc = asmOutText.getStyledDocument();

        Style style = asmOutText.addStyle("Message", null);
        StyleConstants.setForeground(style, Color.DARK_GRAY);

        try {
            doc.insertString(doc.getLength(), message + "\n", style);
        } catch (BadLocationException e) {}
    }

    public void clearTerminal() {
        asmOutText.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane MemoryScrollPane;
    private javax.swing.JLabel accLabel;
    private javax.swing.JLabel accValueLabel;
    private javax.swing.JLabel asmOutLabel;
    private javax.swing.JScrollPane asmOutScroll;
    private javax.swing.JTextPane asmOutText;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JButton cleanAsmOutBtn;
    private javax.swing.JPanel codePane;
    private javax.swing.JTabbedPane codePaneTabs;
    private javax.swing.JSplitPane editorSplit;
    private javax.swing.JLabel ioLabel;
    private javax.swing.JPanel ioPanel;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButtonMode0;
    private javax.swing.JRadioButton jRadioButtonMode1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSplitPane mainSplit;
    private javax.swing.JLabel memoryLabel;
    private javax.swing.JTable memoryTable;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuClose;
    private javax.swing.JMenuItem menuCloseAll;
    private javax.swing.JMenuItem menuNew;
    private javax.swing.JMenuItem menuOpen;
    private javax.swing.JMenuItem menuQuit;
    private javax.swing.JMenu menuRun;
    private javax.swing.JMenuItem menuRunFile;
    private javax.swing.JMenuItem menuSave;
    private javax.swing.JMenuItem menuSaveAs;
    private javax.swing.JLabel mopLabel;
    private javax.swing.JLabel mopValueLabel;
    private javax.swing.JButton newFileButton;
    private javax.swing.JLabel opModeLabel;
    private javax.swing.JPanel opPanel;
    private javax.swing.JButton openFileButton;
    private static javax.swing.JLabel outputLabel;
    private javax.swing.JPanel outputPane;
    private javax.swing.JLabel outputStreamLabel;
    private javax.swing.JLabel pcLabel;
    private javax.swing.JLabel pcValueLabel;
    private javax.swing.JLabel reLabel;
    private javax.swing.JLabel reValueLabel;
    private javax.swing.JButton redoButton;
    private javax.swing.JLabel registerLabel;
    private javax.swing.JPanel registersPanel;
    private javax.swing.JButton reset1Button;
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel riLabel;
    private javax.swing.JLabel riTextLabel;
    private javax.swing.JLabel riValueLabel;
    private javax.swing.JButton run1Button;
    private javax.swing.JButton runButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JPanel simulator;
    private javax.swing.JLabel spLabel;
    private javax.swing.JLabel spValueLabel;
    private javax.swing.JButton stepButton;
    private javax.swing.JPanel toolbar;
    private javax.swing.JButton undoButton;
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

        } else {
            printError("Arquivo inválido");
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public void updateGUI() {
        updateRegisters();
        updateMemory();
    }

}
