package com.enotessa.SQLanomalies.grigorov;

import org.jgrapht.Graph;
import org.jgrapht.graph.*;

import java.io.IOException;
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
    Graph<ArrayList, DefaultWeightedEdge> createGraph(ArrayList<ArrayList> dataAfterQuery) throws ParseException, IOException {
        Graph<ArrayList, DefaultWeightedEdge> graph = new DirectedWeightedPseudograph<ArrayList, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        typeOfColumns = typeOfColumns(dataAfterQuery.get(0));
        for (ArrayList arr : dataAfterQuery){
            graph.addVertex(arr);
        }
        minMaxKnowledgeModule.minMax(dataAfterQuery, graph, typeOfColumns);
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
