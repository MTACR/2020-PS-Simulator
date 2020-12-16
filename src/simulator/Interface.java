package simulator;

import assembler.Assembler;
import gui.CustomDocumentFilter;
import loader.Loader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import javax.swing.undo.*;

public class Interface extends javax.swing.JFrame {

    private Processor processor;
    private File exec;
    private Timer timer; // Temporizador que vai fazer o processador executar o programa inteiro no modo 0
    //private boolean abort;
    //private boolean running;
    private int newFileCount;
    private Map<String, String> activeFilesList = new HashMap<>();
    final List<UndoManager> undoManagerList;

    public Interface() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        setLook();
        initComponents();

        mainSplit.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "none");
        editorSplit.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "none");
        DefaultCaret caret = (DefaultCaret) asmOutText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        undoManagerList = new LinkedList<>();
        asmOutText.setEditable(false);
        newFileCount = 0;
        newFile();
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
        toolbar = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
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
        jPanel4 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        newFileButton = new javax.swing.JButton();
        openFileButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        redoButton = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        runButton = new javax.swing.JButton();
        stepButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        speedSlider = new javax.swing.JSlider();
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

        toolbar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jPanel1.setAlignmentX(0.0F);
        jPanel1.setAlignmentY(0.0F);
        toolbar.add(jPanel1);

        jSeparator3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jSeparator3.setMinimumSize(new java.awt.Dimension(100, 0));
        toolbar.add(jSeparator3);
        toolbar.add(jPanel2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulador");
        setSize(new java.awt.Dimension(640, 300));

        mainSplit.setDividerLocation(400);
        mainSplit.setResizeWeight(1.0);

        editorSplit.setDividerLocation(300);
        editorSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        editorSplit.setResizeWeight(0.8);

        javax.swing.GroupLayout codePaneLayout = new javax.swing.GroupLayout(codePane);
        codePane.setLayout(codePaneLayout);
        codePaneLayout.setHorizontalGroup(
            codePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(codePaneTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
        );
        codePaneLayout.setVerticalGroup(
            codePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(codePaneTabs, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
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
                            .addComponent(registersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addComponent(MemoryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainSplit.setRightComponent(simulator);

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0);
        flowLayout1.setAlignOnBaseline(true);
        jPanel4.setLayout(flowLayout1);

        jToolBar1.setRollover(true);

        newFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/new.png"))); // NOI18N
        newFileButton.setBorderPainted(false);
        newFileButton.setContentAreaFilled(false);
        newFileButton.setFocusable(false);
        newFileButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newFileButton.setMaximumSize(new java.awt.Dimension(40, 40));
        newFileButton.setMinimumSize(new java.awt.Dimension(40, 40));
        newFileButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newFileButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(newFileButton);

        openFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/open.png"))); // NOI18N
        openFileButton.setBorderPainted(false);
        openFileButton.setContentAreaFilled(false);
        openFileButton.setFocusable(false);
        openFileButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openFileButton.setMaximumSize(new java.awt.Dimension(40, 40));
        openFileButton.setMinimumSize(new java.awt.Dimension(40, 40));
        openFileButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(openFileButton);

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/save.png"))); // NOI18N
        saveButton.setBorderPainted(false);
        saveButton.setContentAreaFilled(false);
        saveButton.setFocusable(false);
        saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setMaximumSize(new java.awt.Dimension(40, 40));
        saveButton.setMinimumSize(new java.awt.Dimension(40, 40));
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(saveButton);

        undoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/undo.png"))); // NOI18N
        undoButton.setBorderPainted(false);
        undoButton.setContentAreaFilled(false);
        undoButton.setFocusable(false);
        undoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        undoButton.setMaximumSize(new java.awt.Dimension(40, 40));
        undoButton.setMinimumSize(new java.awt.Dimension(40, 40));
        undoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(undoButton);

        redoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/redo.png"))); // NOI18N
        redoButton.setBorderPainted(false);
        redoButton.setContentAreaFilled(false);
        redoButton.setFocusable(false);
        redoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        redoButton.setMaximumSize(new java.awt.Dimension(40, 40));
        redoButton.setMinimumSize(new java.awt.Dimension(40, 40));
        redoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        redoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(redoButton);

        jPanel4.add(jToolBar1);

        jToolBar2.setRollover(true);

        runButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/run.png"))); // NOI18N
        runButton.setBorderPainted(false);
        runButton.setContentAreaFilled(false);
        runButton.setFocusable(false);
        runButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        runButton.setMaximumSize(new java.awt.Dimension(40, 40));
        runButton.setMinimumSize(new java.awt.Dimension(40, 40));
        runButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });
        jToolBar2.add(runButton);

        stepButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/step.png"))); // NOI18N
        stepButton.setBorderPainted(false);
        stepButton.setContentAreaFilled(false);
        stepButton.setFocusable(false);
        stepButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stepButton.setMaximumSize(new java.awt.Dimension(40, 40));
        stepButton.setMinimumSize(new java.awt.Dimension(40, 40));
        stepButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepButtonActionPerformed(evt);
            }
        });
        jToolBar2.add(stepButton);

        resetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/reset.png"))); // NOI18N
        resetButton.setBorderPainted(false);
        resetButton.setContentAreaFilled(false);
        resetButton.setFocusable(false);
        resetButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        resetButton.setMaximumSize(new java.awt.Dimension(40, 40));
        resetButton.setMinimumSize(new java.awt.Dimension(40, 40));
        resetButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        jToolBar2.add(resetButton);

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/stop.png"))); // NOI18N
        stopButton.setBorderPainted(false);
        stopButton.setContentAreaFilled(false);
        stopButton.setFocusable(false);
        stopButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopButton.setMaximumSize(new java.awt.Dimension(40, 40));
        stopButton.setMinimumSize(new java.awt.Dimension(40, 40));
        stopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        jToolBar2.add(stopButton);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Velocidade de Execução");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel3.add(jLabel1, java.awt.BorderLayout.PAGE_START);

        speedSlider.setToolTipText("Velocidade");
        speedSlider.setName("Velocidade"); // NOI18N
        jPanel3.add(speedSlider, java.awt.BorderLayout.CENTER);

        jToolBar2.add(jPanel3);

        jPanel4.add(jToolBar2);

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
            .addComponent(mainSplit, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(mainSplit))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /*private boolean executeNextInstruction() {
        if (processor != null) {
            boolean error = processor.step();
            updateGUI();
            return error;
        } else
            throw new RuntimeException("No executable loaded");
    }*/
    //Setter para o método write
    public void setOutputLabel(short out) {
        outputLabel.setText(String.format("%05d", out));
    }

    private void menuCloseAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCloseAllActionPerformed
        codePaneTabs.removeAll();
        undoManagerList.clear();
    }//GEN-LAST:event_menuCloseAllActionPerformed

    private void menuNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewActionPerformed
        newFile();
    }//GEN-LAST:event_menuNewActionPerformed

    private void newFile() {
        JScrollPane tab = newTab("");
        codePaneTabs.add("new " + newFileCount + ".asm", tab);
        codePaneTabs.setSelectedIndex(codePaneTabs.indexOfComponent(tab));
        undoManagerList.add(codePaneTabs.getSelectedIndex(), new UndoManager());
        newFileCount++;
    }

    private void menuCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCloseActionPerformed
        undoManagerList.remove(codePaneTabs.getSelectedIndex());
        codePaneTabs.remove(codePaneTabs.getSelectedIndex());
    }//GEN-LAST:event_menuCloseActionPerformed

    private void menuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenActionPerformed
        // Abre o menu para escolher um arquivo, se for válido carrega no processador e atualiza interface
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.asm", "asm"));//não necessariamente
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
          List<File> files = buildFiles();

        if (files.isEmpty())
            printError("No files to assemble");
        else
            try {
                clearTerminal();

                exec = Assembler.assemble(files);

                processor = new Processor(exec, (byte) 0);

                if (timer != null)
                    timer.stop();

                timer = new Timer((100 - speedSlider.getValue()) * 10, (ActionEvent evt1) -> {
                    processor.step();
                    updateGUI();
                });
                timer.setRepeats(true);
                timer.start();

                processor.setOnStopListener(() -> {
                    timer.stop();
                    processor = null;
                    printMessage("Execution finished successful");
                });

                updateGUI();

            } catch (RuntimeException e) {
                printError(e.getMessage());
            }
    }//GEN-LAST:event_menuRunFileActionPerformed

    private void cleanAsmOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanAsmOutBtnActionPerformed
        clearTerminal();
    }//GEN-LAST:event_cleanAsmOutBtnActionPerformed

    private void newFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newFileButtonActionPerformed
        menuNewActionPerformed(evt);
    }//GEN-LAST:event_newFileButtonActionPerformed

    private void openFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileButtonActionPerformed
        menuOpenActionPerformed(evt);
    }//GEN-LAST:event_openFileButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        menuSaveActionPerformed(evt);
    }//GEN-LAST:event_saveButtonActionPerformed

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        /*JViewport viewport = ((JScrollPane) codePaneTabs.getSelectedComponent()).getViewport();
        JTextPane textPane = (JTextPane) viewport.getView();*/
        if (undoManagerList.get(codePaneTabs.getSelectedIndex()).canUndo())
            undoManagerList.get(codePaneTabs.getSelectedIndex()).undo();
    }//GEN-LAST:event_undoButtonActionPerformed

    private void redoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoButtonActionPerformed
        if (undoManagerList.get(codePaneTabs.getSelectedIndex()).canRedo())
            undoManagerList.get(codePaneTabs.getSelectedIndex()).redo();
    }//GEN-LAST:event_redoButtonActionPerformed

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        menuRunFileActionPerformed(evt);
    }//GEN-LAST:event_runButtonActionPerformed

    private void stepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepButtonActionPerformed
        if (processor != null) {
            processor.step();
            updateGUI();

        } else {
            List<File> files = buildFiles();

            if (files.isEmpty())
                printError("No files to assemble");
            else
                try {
                    clearTerminal();

                    exec = Assembler.assemble(files);

                    processor = new Processor(exec, (byte) 1);
                    processor.setOnStopListener(() -> {
                        processor = null;
                        printMessage("Execution finished successful");
                    });

                    updateGUI();

                } catch (RuntimeException e) {
                    printError(e.getMessage());
                }
        }
    }//GEN-LAST:event_stepButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        if (processor != null) {
            assert (timer != null);
                timer.stop();

            processor = new Processor(exec, processor.getMop());
            updateGUI();
            printMessage("Execution resetted");
        }
    }//GEN-LAST:event_resetButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        if (processor != null) {
            assert (timer != null);
                timer.stop();

            processor = null;
            updateGUI();
            printMessage("Execution stopped");
        }
    }//GEN-LAST:event_stopButtonActionPerformed

    private List<File> buildFiles() {
        List<File> files = new ArrayList<>();
        File tmp = new File("tmp");
        tmp.mkdir();

        for (int i = 0; i < codePaneTabs.getTabCount(); i++) {
            String code = ((JEditorPane) (((JViewport) ((JScrollPane) codePaneTabs.getComponent(i)).getComponents()[0]).getComponents()[0])).getText();

            if (!code.isEmpty()) {
                try {
                    File file = new File("tmp/" + codePaneTabs.getTitleAt(i));
                    files.add(file);
                    FileWriter writer = new FileWriter(file);
                    writer.write(code);
                    writer.close();
                } catch (IOException e) {
                    printError("File " + codePaneTabs.getTitleAt(i) + " failed");
                }
            }
        }

        return files;
    }

    private void openFile(File file) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()));
        JScrollPane tab = newTab(content);
        codePaneTabs.add(file.getName(), tab);
        codePaneTabs.setSelectedIndex(codePaneTabs.indexOfComponent(tab));
        undoManagerList.add(codePaneTabs.getSelectedIndex(), new UndoManager());
    }

    private JScrollPane newTab(String content) {
        JScrollPane tab = new JScrollPane();
        JTextPane text = new JTextPane();
        text.setFont(new java.awt.Font("Consolas", 0, 14));
        text.setText(content);
        JLabel lines = new JLabel();
        lines.setText("<html><p style='margin: 2px 20px 0 5px;'>1</p>");
        lines.setFocusable(false);
        lines.setOpaque(true);
        lines.setFont(new java.awt.Font("Consolas", 0, 14));
        lines.setBackground(new Color(230, 230, 230));
        lines.setForeground(new Color(80, 80, 80));
        lines.setHorizontalAlignment(JLabel.LEFT);
        lines.setVerticalAlignment(JLabel.TOP);
//        StyledDocument style = lines.getStyledDocument();
        SimpleAttributeSet align = new SimpleAttributeSet();
        StyleConstants.setAlignment(align, StyleConstants.ALIGN_RIGHT);
//        style.setParagraphAttributes(0, style.getLength(), align, false);

        text.getDocument().addDocumentListener(new DocumentListener() {
            public String getText() {

                int caretPosition = text.getDocument().getLength();
                Element root = text.getDocument().getDefaultRootElement();
                String text = "<html><p style='margin: 2px 20px 0 5px;'>1</p><p style='margin: 0 5px 0 5px;'> ";// coloquei o segundo <p> aqui pra ver se ficava mais rapido

                for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                    text += i + "<br/>";
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
            if (!evt.getEdit().getPresentationName().equals("alteração de estilo")) {
                undoManagerList.get(codePaneTabs.getSelectedIndex()).addEdit(evt.getEdit());
            }
        });

        text.getActionMap().put("Undo", new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (undoManagerList.get(codePaneTabs.getSelectedIndex()).canUndo()) {
                    undoManagerList.get(codePaneTabs.getSelectedIndex()).undo();
                }
            }
        });

        text.getActionMap().put("Redo", new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (undoManagerList.get(codePaneTabs.getSelectedIndex()).canRedo()) {
                    undoManagerList.get(codePaneTabs.getSelectedIndex()).redo();
                }
            }
        });
        text.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
        text.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");

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
        } catch (BadLocationException e) {
        }
    }

    public void printMessage(String message) {
        System.out.println(message);

        StyledDocument doc = asmOutText.getStyledDocument();

        Style style = asmOutText.addStyle("Message", null);
        StyleConstants.setForeground(style, Color.DARK_GRAY);

        try {
            doc.insertString(doc.getLength(), message + "\n", style);
        } catch (BadLocationException e) {
        }
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
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
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel riLabel;
    private javax.swing.JLabel riTextLabel;
    private javax.swing.JLabel riValueLabel;
    private javax.swing.JButton runButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JPanel simulator;
    private javax.swing.JLabel spLabel;
    private javax.swing.JLabel spValueLabel;
    private javax.swing.JSlider speedSlider;
    private javax.swing.JButton stepButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JPanel toolbar;
    private javax.swing.JButton undoButton;
    // End of variables declaration//GEN-END:variables

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
