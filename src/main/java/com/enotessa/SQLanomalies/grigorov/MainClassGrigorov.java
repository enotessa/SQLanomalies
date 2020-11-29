package com.enotessa.SQLanomalies.grigorov;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClassGrigorov {
    Connection connection;
    ScriptRunner scriptRunner;
    Statement stmt;
    Set<String> usingTables;

    ArrayList<ArrayList> dataAfterQuery;

    public MainClassGrigorov(Connection connection) throws SQLException {
        stmt = connection.createStatement();
        this.connection = connection;
        scriptRunner = new ScriptRunner(connection, stmt);
    }

    /**
     * проверка запроса методом Григорова
     *
     * @param queryStr запрос
     */
    public void methodRun(String queryStr) throws ParseException {
        String queryForSelectData = createQueryForSelectData(queryStr);
        assert connection != null;

        //scriptRunner.runScript(queryStr);     //TODO потом раскомментить
        dataAfterQuery = scriptRunner.getDataFromTable(queryForSelectData.toString());
        dataAfterQuery.forEach(System.out::println);
        System.out.println("\n");

        GraphModel graphModel = new GraphModel();
        Graph<ArrayList, DefaultEdge> graph = graphModel.createArrayListGraph(dataAfterQuery);

    }

    /**
     * создание строки запроса для запроса данных из таблиц, которые упомянуты в исходном запросе
     *
     * @param queryStr запрос
     * @return строка запроса
     */
    private String createQueryForSelectData(String queryStr) {
        usingTables = new LinkedHashSet<>();
        StringBuilder query =  new StringBuilder("SELECT * ");
        /*String regex1 = "^SELECT";
        Pattern pattern = Pattern.compile(regex1);
        Matcher matcher = pattern.matcher(queryStr);
        if (matcher.find()){
            return queryStr;
        }*/
        String regex = "(FROM |NATURAL JOIN |UPDATE )([^ (]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(queryStr);
        boolean F = true;
        // определяем, к каким таблицам сделан запрос
        while (matcher.find()) {
            if (!matcher.group(2).isEmpty()) {
                if (F) {
                    query.append("FROM ").append(matcher.group(2)).append(" ");
                    F = false;
                } else {
                    if (!usingTables.contains(matcher.group(2)))
                        query.append("NATURAL JOIN ").append(matcher.group(2)).append(" ");
                }
                usingTables.add(matcher.group(2));
            }
        }
        return query.toString();
    }


}
