package com.enotessa.SQLanomalies.grigorov;

import com.enotessa.SQLanomalies.ConnectionClass;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
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
    ConnectionClass connectionClass;
    Connection connection;
    ScriptRunner scriptRunner;
    Statement stmt;
    Set<String> usingTables;

    ArrayList<ArrayList> dataFull;

    public MainClassGrigorov(ConnectionClass cC) {
        connectionClass = cC;
        try {
            stmt = connectionClass.connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.connection = connection;
        scriptRunner = new ScriptRunner(connection, stmt);
    }

    /**
     * проверка запроса методом Григорова
     *
     * @param queryStr запрос
     */
    public boolean methodRun(String queryStr) throws ParseException, IOException {
        double alpha = 0.25;
        String queryForSelectData = createQueryForSelectData(queryStr);
        assert connection != null;

        //scriptRunner.runScript(queryStr);     //TODO потом раскомментить
        //TODO добавить данные из исх запроса
        dataFull = scriptRunner.getDataFromTable(queryForSelectData.toString());  //TODO переименовать в общие данные
        dataFull.forEach(System.out::println);
        System.out.println("\n");

        GraphModel graphModel = new GraphModel();
        //TODO добавить граф и для исх запроса
        Graph<ArrayList, DefaultWeightedEdge> graph = graphModel.createGraph(dataFull);   // TODO можно сократить, записав вместо dataAfterQuery ->scriptRunner.getDataFromTable(queryForSelectData.toString())
        double density = densityGraph(graph);
        if (density>alpha)
            return true;
        else return false;
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

    /**
     * вычислить плотность графа
     *
     * @param graph граф
     * @return плотность графа
     */
    private double densityGraph(Graph<ArrayList, DefaultWeightedEdge> graph) {
        double density = 0;
        System.out.println(graph.edgeSet().size());
        System.out.println(graph.vertexSet().size()*(graph.vertexSet().size()-1));
        density = graph.edgeSet().size();
        density = density / (graph.vertexSet().size()*graph.vertexSet().size()-1);

        return density;
    }


}
