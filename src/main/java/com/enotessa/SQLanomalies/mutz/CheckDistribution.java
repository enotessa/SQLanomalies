package com.enotessa.SQLanomalies.mutz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CheckDistribution {
    HashMap<Character, Double> collapsСhars = new HashMap<>(); // для подсчета символов
    //ArrayList<HashMap<Character, Double>> frequencies = new ArrayList();
    HashMap<Character, Double> frequencies = new HashMap();
    HashMap<Character, Double> globalFrequencies = new HashMap<>();

    public void fillCollapsСhars(String sequence) { // Подсчет количества вхождений для каждого символа
        char ch;
        for (int i = 0; i < sequence.length(); i++) {
            ch = sequence.charAt(i);
            if (collapsСhars.containsKey(ch))
                collapsСhars.put(ch, collapsСhars.get(ch) + 1);
            else collapsСhars.put(ch, (double) 1);
        }
    }

    public HashMap<Character, Double> computeUnorderedFrequency(String sequence) {
        // Подсчет количества вхождений для каждого символа
        HashMap<Character, Double> frequency = new HashMap<>();
        char ch;
        for (int i = 0; i < sequence.length(); i++) {
            ch = sequence.charAt(i);
            if (frequency.containsKey(sequence.charAt(i)))
                frequency.put(ch, frequency.get(ch) + 1);
            else frequency.put(ch, (double) 1);
        }
        return frequency;
    }

    public void train(ArrayList<String> sequences) {
        // Частота вычисления для каждой последовательности
        double countAllChars = 0;
        // Заполняем collapsСhars, т.е. считаем количество каждых символов
        for (String sequence : sequences) {
            fillCollapsСhars(sequence);
            countAllChars += sequence.length();
        }
        // вычисляем частоту каждого символа
        double finalCountAllChars1 = countAllChars;
        collapsСhars.forEach((k, v) ->{
            globalFrequencies.put(k, v/ finalCountAllChars1);
        });


        /*for (String sequence : sequences) {
            frequencies.add(computeUnorderedFrequency(sequence));
        }
        // вычислить частоту по всем последовательностям
        for (HashMap<Character, Double> frequency : frequencies) {
            for (Map.Entry<Character, Double> pair : frequency.entrySet())
                if (globalFrequencies.containsKey(pair.getKey()))
                    globalFrequencies.put(pair.getKey(), globalFrequencies.get(pair.getKey()) + pair.getValue());
                else globalFrequencies.put(pair.getKey(), pair.getValue());
        }
        double finalCountAllChars = countAllChars;
        globalFrequencies.replaceAll((k, v) -> v / finalCountAllChars);  // вычислили среднюю частоту по всем обучающим последовательностям
        System.out.println(finalCountAllChars);
        System.out.println(1.0 / finalCountAllChars);*/
        double u=0;
        for (Map.Entry<Character, Double> pair: globalFrequencies.entrySet())
            u+=pair.getValue();
        System.out.println(u);
    }

    public boolean validate(String sequence) {
        double threshold = 0.1;
        // Построить частотный массив для данной строки
        HashMap<Character, Double> frequency = new HashMap<>();
        frequency = computeUnorderedFrequency(sequence);
        for (Map.Entry<Character, Double> pair : frequency.entrySet()) {
            if (!globalFrequencies.containsKey(pair.getKey())) {
                System.out.println("Invalid character");
                return false;
            }
        }
        // Сравнить входную последовательность с опорной частотой
        //ChiSquareTest chiSquareTest = new ChiSquareTest();
        //chiSquareTest.chiSquare();
        frequency.replaceAll((k, v) -> v / sequence.length());  // вычислили частоту для данной строки

        double p = chiSquareTest(globalFrequencies, frequency);
        System.out.println(p);
        return p >= threshold;
    }

    double chiSquareTest(HashMap<Character, Double> observed, HashMap<Character, Double> expected) {
        HashMap<Character, Double> tp = new HashMap<>();
        double p = (double) 0.0;
        //Chi Square Formula
        for (Map.Entry<Character, Double> pair : observed.entrySet()) {
            char key = pair.getKey();
            double expectedValue = 0;
            if (expected.containsKey(key)) expectedValue = expected.get(key);
            else expectedValue = 0;
            tp.put(key, observed.get(key) - expectedValue);
            tp.put(key, tp.get(key) * tp.get(key));
            tp.put(key, tp.get(key) / expectedValue);
            p = p + tp.get(key);
        }
        p /= observed.size();
        return p;
    }

}
