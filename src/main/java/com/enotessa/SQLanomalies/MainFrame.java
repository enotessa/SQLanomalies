/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enotessa.SQLanomalies;

import com.enotessa.SQLanomalies.grigorov.MainClassGrigorov;
import com.enotessa.SQLanomalies.vmv.CheckDistribution;
import com.enotessa.SQLanomalies.vmv.CheckLength;
import com.enotessa.SQLanomalies.vmv.CheckToken;
import org.python.util.PythonInterpreter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainFrame extends javax.swing.JFrame {
    ReadSQL readSQL;
    ConnectionClass connectionClass;
    Connection connection = null;
    CheckLength checkLength = new CheckLength();
    CheckDistribution checkDistribution = new CheckDistribution();
    CheckToken checkToken = new CheckToken();
    ArrayList<String> arrayQueries;

    /**
     * Creates new form main
     */
    public MainFrame() throws IOException {
        initComponents();
        readSQL = new ReadSQL();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        Jlogin = new javax.swing.JTextField();
        Jpassword = new javax.swing.JPasswordField();
        Jconnection = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        Jtrain = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        J1Method = new javax.swing.JButton();
        J2Method = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextResultTrain = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextMethod1 = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextQuery = new javax.swing.JTextArea();
        jButtonFile = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextFile = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextResult = new javax.swing.JTextArea();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 153, 153));

        jTabbedPane1.setBackground(new java.awt.Color(102, 0, 153));

        jPanel1.setBackground(new java.awt.Color(38, 70, 83));

        jLabel2.setForeground(new java.awt.Color(242, 239, 233));
        jLabel2.setLabelFor(Jlogin);
        jLabel2.setText("логин");

        jLabel1.setForeground(new java.awt.Color(242, 239, 233));
        jLabel1.setText("пароль");

        Jlogin.setBackground(new java.awt.Color(242, 239, 233));
        Jlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JloginActionPerformed(evt);
            }
        });

        Jpassword.setBackground(new java.awt.Color(242, 239, 233));
        Jpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JpasswordActionPerformed(evt);
            }
        });

        Jconnection.setBackground(new java.awt.Color(42, 157, 143));
        Jconnection.setForeground(new java.awt.Color(242, 239, 233));
        Jconnection.setText("подключиться к БД");
        Jconnection.setBorderPainted(false);
        Jconnection.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Jconnection.setDefaultCapable(false);
        Jconnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JconnectionActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(242, 239, 233));
        jLabel4.setText("Подключение к БД");

        jTextField2.setBackground(new java.awt.Color(242, 239, 233));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/18939.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(223, 223, 223)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(25, 25, 25)
                            .addComponent(Jconnection, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(Jlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Jpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)))
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)))
                .addContainerGap(211, Short.MAX_VALUE))
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Jlogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Jpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(Jconnection)
                .addGap(18, 18, 18)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addComponent(jLabel5)
                .addContainerGap(141, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        jPanel2.setBackground(new java.awt.Color(38, 70, 83));

        Jtrain.setBackground(new java.awt.Color(42, 157, 143));
        Jtrain.setForeground(new java.awt.Color(242, 239, 233));
        Jtrain.setText("тренировать");
        Jtrain.setBorder(null);
        Jtrain.setBorderPainted(false);
        Jtrain.setDefaultCapable(false);
        Jtrain.setFocusPainted(false);
        Jtrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JtrainActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(242, 239, 233));
        jLabel3.setLabelFor(jTextQuery);
        jLabel3.setText("введите запрос для проверки");

        J1Method.setBackground(new java.awt.Color(42, 157, 143));
        J1Method.setForeground(new java.awt.Color(242, 239, 233));
        J1Method.setText("1. метод Вигна, Валер, Мутц");
        J1Method.setBorder(null);
        J1Method.setBorderPainted(false);
        J1Method.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                J1MethodActionPerformed(evt);
            }
        });

        J2Method.setBackground(new java.awt.Color(42, 157, 143));
        J2Method.setForeground(new java.awt.Color(242, 239, 233));
        J2Method.setText("2. метод Григорова");
        J2Method.setBorder(null);
        J2Method.setBorderPainted(false);
        J2Method.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                J2MethodActionPerformed(evt);
            }
        });

        jTextResultTrain.setBackground(new java.awt.Color(242, 239, 233));
        jTextResultTrain.setColumns(20);
        jTextResultTrain.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jTextResultTrain.setRows(5);
        jScrollPane4.setViewportView(jTextResultTrain);

        jTextMethod1.setBackground(new java.awt.Color(242, 239, 233));
        jTextMethod1.setColumns(20);
        jTextMethod1.setRows(5);
        jScrollPane5.setViewportView(jTextMethod1);

        jSeparator1.setBackground(new java.awt.Color(42, 157, 143));

        jLabel6.setBackground(new java.awt.Color(242, 239, 233));
        jLabel6.setForeground(new java.awt.Color(242, 239, 233));
        jLabel6.setText("результат");

        jTextQuery.setBackground(new java.awt.Color(242, 239, 233));
        jTextQuery.setColumns(20);
        jTextQuery.setRows(5);
        jScrollPane2.setViewportView(jTextQuery);

        jButtonFile.setBackground(new java.awt.Color(42, 157, 143));
        jButtonFile.setForeground(new java.awt.Color(242, 239, 233));
        jButtonFile.setText("загрузить запросы для проверки из файла");
        jButtonFile.setBorderPainted(false);
        jButtonFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(242, 239, 233));
        jLabel7.setText("выбрать файл :");

        jTextFile.setBackground(new java.awt.Color(242, 239, 233));

        jCheckBox1.setBackground(new java.awt.Color(42, 157, 143));
        jCheckBox1.setForeground(new java.awt.Color(242, 239, 233));
        jCheckBox1.setText("проверить на ошибки 1 и 2 рода");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jTextResult.setBackground(new java.awt.Color(242, 239, 233));
        jTextResult.setColumns(20);
        jTextResult.setRows(5);
        jScrollPane3.setViewportView(jTextResult);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jScrollPane5)
            .addComponent(jScrollPane2)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 6, Short.MAX_VALUE)
                        .addComponent(J1Method, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(J2Method, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(Jtrain, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jButtonFile)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextFile, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Jtrain, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonFile)
                    .addComponent(jLabel7)
                    .addComponent(jTextFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(J2Method, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(J1Method, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab2", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 781, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void JconnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JconnectionActionPerformed
        jTextField2.setText("...");
        String login = Jlogin.getText();
        String password = Jpassword.getText();
        try {
            connectionClass = new ConnectionClass("Admin", "Admin");
            jTextField2.setText("подключено");
            System.out.println("подключено");
        } catch (SQLException e) {
            e.printStackTrace();
            jTextField2.setText("ошибка при подключении");
            System.out.println("ошибка при подключении");
        }
    }//GEN-LAST:event_JconnectionActionPerformed

    private void JtrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JtrainActionPerformed
        try {
            arrayQueries = readSQL.readFile("queries.txt");
            checkLength.train(arrayQueries);
            jTextResultTrain.setText("обучена модель: Длина строки");
            checkDistribution.train(arrayQueries);
            jTextResultTrain.setText(jTextResultTrain.getText() + "\nобучена модель: Распределение символов строки");
            checkToken.train(arrayQueries, connectionClass);
            jTextResultTrain.setText(jTextResultTrain.getText() + "\nобучена модель: Поиск токенов. независимая модель");
            jTextResultTrain.setText(jTextResultTrain.getText() + "\nОБУЧЕНО!");
        } catch (IOException e) {
            jTextResultTrain.setText("не удалось открыть файл");
        }

    }//GEN-LAST:event_JtrainActionPerformed


    private void J1MethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_J1MethodActionPerformed
        double errors1 = 0;
        double errors2 = 0;
        double trueAnomaly = 0;
        double trueNormal = 0;

        jTextMethod1.setText("");
        String q = jTextQuery.getText();
        String[]queries = q.split("\n");
        ArrayList<String> arrayQueries = new ArrayList<String>(Arrays.asList(queries));
        ArrayList<Boolean> bool = new ArrayList<Boolean>();
        int i = 0;
        for (String qu : arrayQueries) {
            String query;
            if (jCheckBox1.isSelected()) {
                Boolean b = Boolean.FALSE;
                String substr = qu.substring(0, 1);
                if (substr.equals("1"))
                    bool.add(true);
                else bool.add(false);
                query = qu.substring(2);
            }
            else query = qu;
            jTextMethod1.setText(jTextMethod1.getText() + "\nзапрос : " + query);
            boolean chLength = checkLength.validate(query);
            jTextMethod1.setText(jTextMethod1.getText() + "\n   Проверка на длину строки : " + chLength);
            boolean chDistribution = checkDistribution.validate(query);
            jTextMethod1.setText(jTextMethod1.getText() + "\n   Проверка модели: Проверка на распределение символов строки : " + chDistribution);
            boolean chToken = checkToken.validate(query);
            jTextMethod1.setText(jTextMethod1.getText() + "\n   Проверка модели: Поиск токенов. независимая модель : " + chToken);
            if (chDistribution&&chLength&&chToken){
                jTextMethod1.setText(jTextMethod1.getText() + "\n запрос НОРМАЛЬНЫЙ");
                if (jCheckBox1.isSelected()) {
                    if (!bool.get(i)) errors2++;    // ложное отрицание (1 рода)
                    else trueNormal++;
                }
            }
            else {
                jTextMethod1.setText(jTextMethod1.getText() + "\n запрос АНОМАЛЬНЫЙ");
                if (jCheckBox1.isSelected()) {
                    if (bool.get(i)) errors1++;    // ложное срабатывание (2 рода)
                    else trueAnomaly++;
                }
            }
            jTextMethod1.setText(jTextMethod1.getText() + "");
            i++;
        }
        if (jCheckBox1.isSelected()) {
            //jTextResult.setText("ложное срабатывание : " + errors1);
            //jTextResult.setText(jTextResult.getText() + "\n ложное отрицание : " + errors2);

            double n1 = (double)(trueNormal/(trueNormal+errors2));
            jTextResult.setText(" распознано нормальных запросов : " + n1);
            double n2 = (double)(trueAnomaly/(trueAnomaly+errors1));
            jTextResult.setText(jTextResult.getText() + "\n распознано аномальных запросов : " + n2);
            double n3 = (double)(errors1/(errors1+trueNormal));
            jTextResult.setText(jTextResult.getText() + "\n процент ложного срабатывания : " + n3);
            double n4 = (double)(errors2/(errors2+trueAnomaly));
            jTextResult.setText(jTextResult.getText() + "\n процент ложного отрицания : " + n4);
            double n5 = (double)(errors1+errors2)/(double)arrayQueries.size();
            jTextResult.setText(jTextResult.getText() + "\n процент ложного срабатывания и отрицания : " + n5);
        }
    }//GEN-LAST:event_J1MethodActionPerformed


    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void JpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JpasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JpasswordActionPerformed

    private void JloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JloginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JloginActionPerformed



    private void J2MethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_J2MethodActionPerformed
        double errors1 = 0;
        double errors2 = 0;
        double trueAnomaly = 0;
        double trueNormal = 0;
        MainClassGrigorov mainClassGrigorov = new MainClassGrigorov(connectionClass);

        jTextMethod1.setText("");
        String q = jTextQuery.getText();
        String[]queries = q.split("\n");
        ArrayList<String> arrayQueries = new ArrayList<String>(Arrays.asList(queries));
        ArrayList<Boolean> bool = new ArrayList<Boolean>();
        int i = 0;
        for (String qu : arrayQueries) {
            String query;
            if (jCheckBox1.isSelected()) {
                Boolean b = Boolean.FALSE;
                String substr = qu.substring(0, 1);
                if (substr.equals("1"))
                    bool.add(true);
                else bool.add(false);
                query = qu.substring(2);
            }
            else query = qu;


            boolean k = false;
            try {
                k = mainClassGrigorov.methodRun(query);
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
            if (k){
                jTextMethod1.setText(jTextMethod1.getText() + "\n запрос НОРМАЛЬНЫЙ");
                if (jCheckBox1.isSelected()) {
                    if (!bool.get(i)) errors2++;    // ложное отрицание (1 рода)
                    else trueNormal++;
                }
            }
            else {
                jTextMethod1.setText(jTextMethod1.getText() + "\n запрос АНОМАЛЬНЫЙ");
                if (jCheckBox1.isSelected()) {
                    if (bool.get(i)) errors1++;    // ложное срабатывание (2 рода)
                    else trueAnomaly++;
                }
            }
        }


        if (jCheckBox1.isSelected()) {
            //jTextResult.setText("ложное срабатывание : " + errors1);
            //jTextResult.setText(jTextResult.getText() + "\n ложное отрицание : " + errors2);

            double n1 = (double)(trueNormal/(trueNormal+errors2));
            jTextResult.setText(" распознано нормальных запросов : " + n1);
            double n2 = (double)(trueAnomaly/(trueAnomaly+errors1));
            jTextResult.setText(jTextResult.getText() + "\n распознано аномальных запросов : " + n2);
            double n3 = (double)(errors1/(errors1+trueNormal));
            jTextResult.setText(jTextResult.getText() + "\n процент ложного срабатывания : " + n3);
            double n4 = (double)(errors2/(errors2+trueAnomaly));
            jTextResult.setText(jTextResult.getText() + "\n процент ложного отрицания : " + n4);
            double n5 = (double)(errors1+errors2)/(double)arrayQueries.size();
            jTextResult.setText(jTextResult.getText() + "\n процент ложного срабатывания и отрицания : " + n5);
        }
    }//GEN-LAST:event_J2MethodActionPerformed

    private void jButtonFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileActionPerformed
        try {
            arrayQueries = readSQL.readFile(jTextFile.getText());
            StringBuilder str = new StringBuilder();
            arrayQueries.forEach(s -> str.append(s).append("\n"));
            jTextQuery.setText(str.toString());
        } catch (IOException e) {
            e.printStackTrace();
            jTextFile.setText("файл не найден");
        }
    }//GEN-LAST:event_jButtonFileActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainFrame().setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton J1Method;
    private javax.swing.JButton J2Method;
    private javax.swing.JButton Jconnection;
    private javax.swing.JTextField Jlogin;
    private javax.swing.JPasswordField Jpassword;
    private javax.swing.JButton Jtrain;
    private javax.swing.JButton jButtonFile;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextFile;
    private javax.swing.JTextArea jTextMethod1;
    private javax.swing.JTextArea jTextQuery;
    private javax.swing.JTextArea jTextResult;
    private javax.swing.JTextArea jTextResultTrain;
    // End of variables declaration//GEN-END:variables
}
