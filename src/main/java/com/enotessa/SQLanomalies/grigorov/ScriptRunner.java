package com.enotessa.SQLanomalies.grigorov;

import org.python.antlr.ast.Str;

import java.sql.*;
import java.util.ArrayList;

public class ScriptRunner {
    private ResultSet rs;
    Connection connection = null;
    private int qountColumns;
    Statement stmt;

    public ScriptRunner(Connection connection, Statement stmt) {
        this.stmt = stmt;
        this.connection = connection;
    }

    /**
     * выполнить запрос пользователя
     *
     * @param queryStr запрос
     */
    public void runScript(String queryStr){
        String output;
        try {
            output="\n";
            rs = stmt.executeQuery(queryStr);
            ResultSetMetaData rsmd = rs.getMetaData();
            qountColumns = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i < qountColumns; i++) {
                    output +=rs.getString(i) + " : ";
                }
                output +=rs.getString(qountColumns) + "\n";
            }
            System.out.println(output);
            rs.close();
        } catch (SQLException e) {
            System.out.println("Нет достаточных привелегий");
        }
    }

    /**
     * получить таблицу с данными
     *
     * @param queryForGetData запрос для получения данных из таблиц
     * @return таблица с данными
     */
    ArrayList<ArrayList> getDataFromTable(String queryForGetData){
        ArrayList<ArrayList> data = new ArrayList<>();
        String output;
        try {
            rs = stmt.executeQuery(queryForGetData);
            ResultSetMetaData rsmd = rs.getMetaData();
            qountColumns = rsmd.getColumnCount();
            while (rs.next()) {
                data.add(new ArrayList());
                for (int i = 1; i <= qountColumns; i++) {
                    data.get(data.size()-1).add(rs.getString(i));
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Нет достаточных привелегий");
        }
        return data;
    }
}
