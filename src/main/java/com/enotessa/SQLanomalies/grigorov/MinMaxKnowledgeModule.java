package com.enotessa.SQLanomalies.grigorov;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Класс, оценивающий результирующее множество, строя граф по правилу "MinMax".
 *
 * @author Andrey Grigorov
 */
public class MinMaxKnowledgeModule {
    ArrayList<ArrayList> topBound;
    ArrayList<ArrayList> bottomBound;
    ArrayList<TypeOfAttribute> typeOfColumns = new ArrayList<>();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    void minMax(ArrayList<ArrayList> dataAfterQuery, Graph<ArrayList, DefaultWeightedEdge> graph, ArrayList<TypeOfAttribute> typeOfColumns) throws ParseException {
        this.typeOfColumns = typeOfColumns;
        // находим верхнюю и нижнюю границы значений
        topBound = getTopBound(dataAfterQuery);
        bottomBound = getBottomBound(dataAfterQuery);


        System.out.println("topBound");
        topBound.stream().forEach(System.out::println);
        System.out.println("bottomBound");
        bottomBound.stream().forEach(System.out::println);


        for (int i=0; i< dataAfterQuery.size(); i++){
            for (int j = 0; j<dataAfterQuery.size(); j++){
                if (isRelations(i,j, dataAfterQuery)) graph.addEdge(dataAfterQuery.get(i), dataAfterQuery.get(j));

                /*DefaultWeightedEdge e = graph.addEdge(dataAfterQuery.get(i), dataAfterQuery.get(j));
                if (isRelations(i,j, dataAfterQuery))
                    graph.setEdgeWeight(e, 1);
                else graph.setEdgeWeight(e, 0);*/
            }
        }

    }

    private boolean isRelations(int i, int j,ArrayList<ArrayList> dataAfterQuery) throws ParseException {
        int summ = 0;
        for (int k=0; k<dataAfterQuery.get(i).size(); k++){
            TypeOfAttribute type;
            type = typeOfColumns.get(k);
            switch (type){

                case DATA :
                    if (
                            formatter.parse(dataAfterQuery.get(i).get(k).toString()).compareTo(formatter.parse(bottomBound.get(j).get(k).toString()))>=0 &&
                            formatter.parse(dataAfterQuery.get(i).get(k).toString()).compareTo(formatter.parse(topBound.get(j).get(k).toString()))<=0 &&
                            formatter.parse(dataAfterQuery.get(j).get(k).toString()).compareTo(formatter.parse(bottomBound.get(i).get(k).toString()))>=0 &&
                            formatter.parse(dataAfterQuery.get(j).get(k).toString()).compareTo(formatter.parse(topBound.get(i).get(k).toString()))<=0
                    ){
                        summ++;
                    }
                    break;

                case STRING :
                    if (
                            dataAfterQuery.get(i).get(k).toString().length() >= Integer.parseInt(bottomBound.get(j).get(k).toString()) &&
                            dataAfterQuery.get(i).get(k).toString().length() <= Integer.parseInt(topBound.get(j).get(k).toString()) &&
                            dataAfterQuery.get(j).get(k).toString().length() >= Integer.parseInt(bottomBound.get(i).get(k).toString()) &&
                            dataAfterQuery.get(j).get(k).toString().length() <= Integer.parseInt(topBound.get(i).get(k).toString())
                    ){
                        summ++;
                    }
                    break;

                case NUMBER :
                    if (
                            dataAfterQuery.get(i).get(k).toString().length() >= Integer.parseInt(bottomBound.get(j).get(k).toString()) &&
                            dataAfterQuery.get(i).get(k).toString().length() <= Integer.parseInt(topBound.get(j).get(k).toString()) &&
                            dataAfterQuery.get(j).get(k).toString().length() >= Integer.parseInt(bottomBound.get(i).get(k).toString()) &&
                            dataAfterQuery.get(j).get(k).toString().length() <= Integer.parseInt(topBound.get(i).get(k).toString())
                    ){
                        summ++;
                    }
                    break;
            }
        }
        if (summ == dataAfterQuery.get(i).size())
            return true;
        return false;

    }

    /**
     * Определение верхней границы для защищаемых неключевых полей таблицы
     *
     * @param dataAfterQuery результат выборки
     * @return верхние границы значений полей
     */
    private ArrayList<ArrayList> getTopBound(ArrayList<ArrayList> dataAfterQuery) throws ParseException {
        TypeOfAttribute type;
        ArrayList<ArrayList> topBound = new ArrayList<ArrayList>();

        for (ArrayList record : dataAfterQuery){
            topBound.add(new ArrayList());
            for (int i = 0; i<record.size(); i++){
                //topBound.get(topBound.size() - 1).add(record.get(i));
                type = typeOfColumns.get(i);
                switch (type){

                    case DATA :
                        topBound.get(topBound.size() - 1).add(record.get(i));
                        break;

                    case STRING :
                        topBound.get(topBound.size() - 1).add(record.get(i).toString().length());
                        break;

                    case NUMBER :
                        topBound.get(topBound.size() - 1).add(record.get(i).toString().length());
                        break;
                }
            }
        }

        for (int i=0;  i<dataAfterQuery.size(); i++){
            for (int j=0;  j<dataAfterQuery.get(i).size(); j++){
                type = typeOfColumns.get(j);
                switch (type){
                    case DATA :
                        for (ArrayList record : dataAfterQuery){
                            if ((formatter.parse(record.get(j).toString())).compareTo(formatter.parse(topBound.get(i).get(j).toString()))>0){
                                topBound.get(i).set(j, record.get(j));
                            }
                        }
                        break;

                    case STRING :
                        for (ArrayList record : dataAfterQuery){
                            if (record.get(j).equals(dataAfterQuery.get(i).get(j))){
                                for (int k=0; k<record.size(); k++){
                                    if (typeOfColumns.get(k).equals(TypeOfAttribute.STRING)){
                                        if (record.get(k).toString().length()>topBound.get(i).get(k).toString().length()-5){
                                            topBound.get(i).set(k, record.get(k).toString().length()+5);
                                        }
                                    }
                                }
                            }
                        }
                        break;

                    case NUMBER :
                        break;
                }
            }
        }

        return topBound;
    }

    /**
     * Определение нижней границы для защищаемых неключевых полей таблицы
     *
     * @param dataAfterQuery результат выборки
     * @return нижние границы значений полей
     */
    private ArrayList<ArrayList> getBottomBound(ArrayList<ArrayList> dataAfterQuery) throws ParseException {
        TypeOfAttribute type;
        ArrayList<ArrayList> bottomBound = new ArrayList<ArrayList>();

        for (ArrayList record : dataAfterQuery){
            bottomBound.add(new ArrayList());
            for (int i = 0; i<record.size(); i++){
                //bottomBound.get(bottomBound.size() - 1).add(record.get(i));
                type = typeOfColumns.get(i);
                switch (type){

                    case DATA :
                        bottomBound.get(bottomBound.size() - 1).add(record.get(i));
                        break;

                    case STRING :
                        bottomBound.get(bottomBound.size() - 1).add(record.get(i).toString().length());
                        break;

                    case NUMBER :
                        bottomBound.get(bottomBound.size() - 1).add(record.get(i).toString().length());
                        break;
                }
            }
        }

        for (int i=0;  i<dataAfterQuery.size(); i++){
            for (int j=0;  j<dataAfterQuery.get(i).size(); j++){
                type = typeOfColumns.get(j);
                switch (type){
                    case DATA :
                        for (ArrayList record : dataAfterQuery){
                            if ((formatter.parse(record.get(j).toString())).compareTo(formatter.parse(bottomBound.get(i).get(j).toString()))<0){
                                bottomBound.get(i).set(j, record.get(j));
                            }
                        }
                        break;

                    case STRING :
                        for (ArrayList record : dataAfterQuery){
                            if (record.get(j).equals(dataAfterQuery.get(i).get(j))){    //TODO проверка на одну и ту же запись
                                for (int k=0; k<record.size(); k++){
                                    if (typeOfColumns.get(k).equals(TypeOfAttribute.STRING)){
                                        if (record.get(k).toString().length()<dataAfterQuery.get(i).get(k).toString().length()+5){   //TODO dataAfterQuery заменить на bottomBound
                                            bottomBound.get(i).set(k, record.get(k).toString().length()-5);
                                        }
                                    }
                                }
                            }
                        }
                        break;

                    case NUMBER :
                        break;
                }
            }
        }

        return bottomBound;
    }
}
