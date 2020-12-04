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
        System.out.println(queryStr);
        double alpha = 0.25;
        String queryForSelectFullData = createQueryForSelectFullData(queryStr);
        String queryForSelectDataAfter = createQueryForSelectData(queryStr);
        System.out.println(queryForSelectFullData);
        System.out.println(queryForSelectDataAfter);

        assert connection != null;
        //scriptRunner.runScript(queryStr);     //TODO потом раскомментить
        System.out.println("\n");
        GraphModel graphModel = new GraphModel();
        Graph<ArrayList, DefaultWeightedEdge> graphDataFull = graphModel.createGraph(scriptRunner.getDataFromTable(queryForSelectFullData));

        Graph<ArrayList, DefaultWeightedEdge> graphDataAfterQuery = graphModel.createGraphDataAfterQuery(scriptRunner.getDataFromTable(queryForSelectDataAfter), graphDataFull);    //TODO посмотреть алгоритм. тут нужен другой


        double density = densityGraph(graphDataAfterQuery);
        if (density>alpha)
            return true;
        else return false;
    }

    private String createQueryForSelectData(String queryStr) {
        String regex = "SELECT ([^F]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(queryStr);
        matcher.find();
        String query = queryStr.replace(matcher.group(1),"* ");

        return query;
    }

    /**
     * создание строки запроса для запроса данных из таблиц, которые упомянуты в исходном запросе
     *
     * @param queryStr запрос
     * @return строка запроса
     */
    private String createQueryForSelectFullData(String queryStr) {
        usingTables = new LinkedHashSet<>();
        StringBuilder query =  new StringBuilder("SELECT * ");
        String regex = "(FROM |NATURAL JOIN )([^ ()]*)";
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
        System.out.println(graph.vertexSet().size()*(graph.vertexSet().size()));
        density = graph.edgeSet().size();
        density = density / (graph.vertexSet().size()*graph.vertexSet().size());

        return density;
    }


}
