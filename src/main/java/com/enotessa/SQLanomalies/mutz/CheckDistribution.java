package com.enotessa.SQLanomalies.mutz;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Модель: Распределение символов строки
 */
public class CheckDistribution {
    double countAllChars = 0;

    HashMap<Character, Double> collapsСhars = new HashMap<>(); // для подсчета символов
    HashMap<Character, Double> frequencies = new HashMap();
    HashMap<Character, Double> globalFrequencies = new HashMap<>();
    HashMap<Character, Double> sortedGlobalFrequencies = new HashMap<>();
    ArrayList<Double> intervals = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0));

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
        // Заполняем collapsСhars, т.е. считаем количество каждых символов
        for (String sequence : sequences) {
            fillCollapsСhars(sequence);
            countAllChars += sequence.length();
        }
        // вычисляем частоту каждого символа
        double finalCountAllChars1 = countAllChars;
        // для округления

        collapsСhars.forEach((k, v) -> {
            globalFrequencies.put(k, Double.parseDouble(String.format("%.7f", (v / finalCountAllChars1)).replace(",", ".")));
        });
        // сортируем по значениям, то есть, по вероятностям
        sortedGlobalFrequencies = globalFrequencies.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        //sortedGlobalFrequencies.entrySet().forEach(System.out::println);
        //System.out.println(sortedGlobalFrequencies.size());
        int k=0;
        // делим вероятности на интервалы
        for (Map.Entry<Character, Double> pair : sortedGlobalFrequencies.entrySet()) {
            double value = pair.getValue();
            boolean F = true;
            if (value < 0.0005) {
                intervals.set(0, intervals.get(0) + value);
                k++;
            }
            else if (value < 0.001) {
                if (F) {
                    intervals.set(0, intervals.get(0)/k);
                    k=0;
                    F= false;
                }
                intervals.set(1, intervals.get(1) + value);
                k++;
            }
            else if (value < 0.005) {
                if (!F) {
                    intervals.set(1, intervals.get(1)/k);
                    k=0;
                    F= true;
                }
                intervals.set(2, intervals.get(2) + value);
                k++;
            }
            else if (value < 0.09) {
                if (F) {
                    intervals.set(2, intervals.get(2)/k);
                    k=0;
                    F= false;
                }
                intervals.set(3, intervals.get(3) + value);
                k++;
            }
            else {
                if (!F) {
                    intervals.set(3, intervals.get(3)/k);
                    k=0;
                    F= true;
                }
                intervals.set(4, intervals.get(4) + value);
                k++;
            }
        }
        intervals.set(4, intervals.get(4)/k);
        k=0;
    }

    public boolean validate(String sequence) {
        double threshold = 4000;
        // Построить частотный массив для данной строки
        HashMap<Character, Double> frequency = new HashMap<>();
        frequency = computeUnorderedFrequency(sequence);
        for (Map.Entry<Character, Double> pair : frequency.entrySet()) {
            if (!sortedGlobalFrequencies.containsKey(pair.getKey())) {
                System.out.println("Invalid character: " + pair.getKey());
                return false;
            }
        }

        // Сравнить входную последовательность с опорной частотой
        double p = chiSquareTest(frequency, sortedGlobalFrequencies, sequence.length());
        System.out.println(p);
        return p < threshold;
    }

    private double chiSquare(double observedValue, double expectedValue) {
        return Math.pow(observedValue - expectedValue, 2) / expectedValue;
    }

    public double chiSquareTest(HashMap<Character, Double> observed, HashMap<Character, Double> expected, int sequenceLength) {
        double p = 0, expectedValue = 0, observedValue = 0;
        for (Map.Entry<Character, Double> pair : expected.entrySet()) {
            expectedValue = pair.getValue();
            if (expectedValue < 0.001) expectedValue = intervals.get(0);
            else if (expectedValue < 0.005) expectedValue = intervals.get(1);
            else if (expectedValue < 0.01) expectedValue = intervals.get(2);
            else if (expectedValue < 0.1) expectedValue = intervals.get(3);
            else expectedValue = intervals.get(4);
            observedValue = observed.getOrDefault(pair.getKey(), 0.0);
            p += (chiSquare(observedValue, expectedValue * sequenceLength));
        }
        return p;
    }
}
