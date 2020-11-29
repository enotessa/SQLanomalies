package com.enotessa.SQLanomalies.grigorov;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.enotessa.SQLanomalies.grigorov.TypeOfAttribute.*;

public class GraphModel {
    MinMaxKnowledgeModule minMaxKnowledgeModule = new MinMaxKnowledgeModule();
    ArrayList<TypeOfAttribute> typeOfColumns = new ArrayList<>();

    /**
     * создать граф GT
     *
     * @param dataAfterQuery запрос для получения данных из таблиц
     * @return граф GT
     */
    Graph<ArrayList, DefaultEdge> createArrayListGraph(ArrayList<ArrayList> dataAfterQuery) throws ParseException {
        Graph<ArrayList, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        typeOfColumns = typeOfColumns(dataAfterQuery.get(0));
        dataAfterQuery.forEach(graph::addVertex);
        minMaxKnowledgeModule.minMax(dataAfterQuery, graph, typeOfColumns);
        //graph.vertexSet().forEach(System.out::println);

        return graph;
    }

    /**
     * определить тип атрибута
     *
     * @param attr запрос для получения данных из таблиц
     * @return тип атрибута : DATA, STRING, NUMBER
     */
    public static TypeOfAttribute getTypeOfAttribute(String attr) {
        // проверка на тип "число"
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(attr);
        if (matcher.find()) {
            if (attr.equals(matcher.group())){
                return NUMBER;
            }
        }
        // проверка на тип "дата"
        pattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d");
        matcher = pattern.matcher(attr);
        if (matcher.find()) {
            if (attr.equals(matcher.group())){
                return DATA;
            }
        }
        // тип "строка"
        return STRING;
    }

    /**
     * определить типы столбцов
     *
     * @param record одна запись
     * @return массив с типами атрибутов
     */
    public static ArrayList<TypeOfAttribute> typeOfColumns(ArrayList record){
        ArrayList<TypeOfAttribute> typeOfColumns = new ArrayList<>();
        for (Object attr : record){
            typeOfColumns.add(getTypeOfAttribute(attr.toString()));
        }
        return typeOfColumns;
    }
}
