package com.enotessa.SQLanomalies.mutz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO придумать запросы для перечислений. То есть таких, чтобы значений атрибута было мало, а самих атрибуьов много. (повторяющиеся атрибуты)

// Поиск токенов. независимая модель
public class CheckToken {
    double threshold;

    HashMap<String, ArrayList<String>> tokens = new HashMap<>();
    HashMap<String, ArrayList<String>> tokensCopy = new HashMap<>();
    HashMap<String, Double> probabilities;

    /**
     * тренировка модели "Поиск токенов. независимая модель"
     *
     * @param sequences запросы для обучения
     */
    public void train(ArrayList<String> sequences) {
        for (String sequence : sequences)
            token(sequence);
        for (Map.Entry<String, ArrayList<String>> pair : tokens.entrySet())
            tokensCopy.put(pair.getKey(), new ArrayList<String>(pair.getValue()));

        int K=0;
        probabilities = new HashMap<>(tokens.size());
        // считаем вероятности
        // N-количество всех выборок, n-текущее количество выбранных выборок,
        // K-всего найдено данных литералов, k-найдено новых данных литералов в текущей выборке(которых не было)+ старые
        for (Map.Entry<String, ArrayList<String>> pair : tokens.entrySet()) {
            K=pair.getValue().size();
            probabilities.put(pair.getKey(), probabilityForTrain(sequences, pair.getKey(),K));
        }
    }

    /*void findingThreshold(ArrayList<String> sequences) {
        threshold = 0;
        double p;
        for (String str : sequences) {
            p = probability(str);
            if (threshold == 0)
                threshold = p;
            else if (threshold < p) {
                threshold = p;
            }
        }
    }*/

    /*double probability(String sequence) {
        HashMap<String, Double> probabilitiesSequence;
        double K=0;
        HashMap<String, ArrayList<String>> tokensSequence = new HashMap<>();
        String regex = "(([^ .]*)( (=|>=|<=|<|>|LIKE) )([^ (]*)( |;))";
        Pattern pattern = Pattern.compile( regex );
        Matcher matcher = pattern.matcher(sequence);
        // ищем токены
        while (matcher.find()){
            if (tokensSequence.containsKey(matcher.group(2)))
                tokensSequence.get(matcher.group(2)).add(matcher.group(5));
            else tokensSequence.put(matcher.group(2), new ArrayList<String>(Collections.singleton(matcher.group(5))));
        }
        boolean F;
        for (Map.Entry<String, ArrayList<String>> pair : tokensSequence.entrySet()) {
            K=pair.getValue().size();
            probabilitiesSequence.put(pair.getKey(), probabilityForTrain(sequences, pair.getKey(),K));

        }

    }*/


    /**
     * обнаружение аномалий по модели "Поиск токенов. независимая модель"
     *
     * @param sequence запрос
     * @return true - нормальный, false - аномальный
     */
    public boolean validate(String sequence) {
        double threshold = 0.563;
        HashMap<String, ArrayList<String>> tokensSequence = new HashMap<>();
        String regex = "(([^ .]*)( (=|>=|<=|<|>|LIKE) )([^ (]*)( |;))";
        Pattern pattern = Pattern.compile( regex );
        Matcher matcher = pattern.matcher(sequence);
        // ищем токены
        while (matcher.find()){
            if (tokensSequence.containsKey(matcher.group(2)))
                tokensSequence.get(matcher.group(2)).add(matcher.group(5));
            else tokensSequence.put(matcher.group(2), new ArrayList<String>(Collections.singleton(matcher.group(5))));
        }
        boolean F;
        for (Map.Entry<String, ArrayList<String>> pair : tokensSequence.entrySet()) {
            if (threshold>=probabilities.get(pair.getKey())){
                for (String str1: pair.getValue()){
                    F=false;
                    for (String str2 : tokens.get(pair.getKey())) {
                        if (str1.equals(str2))
                            F = true;
                    }
                    if (!F) return false;
                }
            }
        }
        return true;
    }

    /*double probability(String sequence, HashMap<String, ArrayList<String>> tokensSequence, double K, String key){
        double probability=0;
        double N=1, n=1;
        double k=0;
        String regex = "("+key+"( (=|>=|<=|<|>|LIKE) )([^ (]*)( |;))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sequence);
        boolean F=true;
        while (matcher.find()) {
            if (F) n++;
            if (!tokensSequence.get(key).isEmpty() && tokensSequence.get(key).contains(matcher.group(4))) {   // найдено новых вхождений
                tokensSequence.get(key).remove(matcher.group(4));
                k++;
            }
            F = false;
        }
        probability = Math.abs(k / K - n / N);


        return probability*Math.sqrt(K);
    }*/

    /**
     * статистический тест Колмогорова-Смирнова
     *
     * @param sequences запросы
     * @param key атрибут
     * @param K всего найдено данных литералов
     * @return вероятность
     */
    double probabilityForTrain(ArrayList<String> sequences, String key,double K){
        double k=0;
        double N=0;
        String regex = "("+key+"( (=|>=|<=|<|>|LIKE) )([^ (]*)( |;))";
        Pattern pattern;
        Matcher matcher;
        double max=0;
        // считаем N
        for (String s : sequences) {
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(s);
            if (matcher.find())
                N++;
        }

        boolean F;
        double n = 0;
        for (String sequence : sequences) {
            F = true;
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(sequence);
            while (matcher.find()) {
                if (F) n++;
                if (!tokensCopy.get(key).isEmpty() && tokens.get(key).contains(matcher.group(4))) {   // найдено новых вхождений
                    tokensCopy.get(key).remove(matcher.group(4));
                    //tokensCopy.remove(key, tokensCopy.get(key).remove(matcher.group(4)));
                    k++;
                }
                F = false;
            }
            if (!F) {
                if (max == 0) {
                    max = Math.abs(k / K - (double) n / N);
                } else if (max < (Math.abs(k / K - (double)n / N))) {
                    max = Math.abs(k / K - (double)n / N);
                }
                if (k == K) break;
            }
        }
        return max*Math.sqrt(K);
    }

    /**
     * токенизация
     *
     * @param sequence запросы
     */
    void token(String sequence){
        String result = sequence;
        String regex = "(([^ .]*)( (=|>=|<=|<|>|LIKE) )([^ (]*)( |;))";
        Pattern pattern = Pattern.compile( regex );
        Matcher matcher = pattern.matcher(sequence);
        while (matcher.find()){
            if (tokens.containsKey(matcher.group(2)))
                tokens.get(matcher.group(2)).add(matcher.group(5));
            else tokens.put(matcher.group(2), new ArrayList<String>(Collections.singleton(matcher.group(5))));
            result = result.replace(matcher.group(5), "#$"+matcher.group(2));
        }
    }
}
