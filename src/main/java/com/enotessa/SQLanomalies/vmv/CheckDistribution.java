package com.enotessa.SQLanomalies.vmv;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Модель: Распределение символов строки
 */
public class CheckDistribution {
    double countAllChars = 0;
    double threshold;

    HashMap<Character, Double> collapsСhars = new HashMap<>(); // для подсчета символов
    HashMap<Character, Double> frequencies = new HashMap();
    HashMap<Character, Double> globalFrequencies = new HashMap<>();
    HashMap<Character, Double> sortedGlobalFrequencies = new HashMap<>();
    ArrayList<Double> intervals = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0));

    /**
     * Подсчет количества вхождений для каждого символа во всех обучающих запросах
     *
     * @param sequence запрос
     */
    public void fillCollapsСhars(String sequence) { // Подсчет количества вхождений для каждого символа
        char ch;
        for (int i = 0; i < sequence.length(); i++) {
            ch = sequence.charAt(i);
            if (collapsСhars.containsKey(ch))
                collapsСhars.put(ch, collapsСhars.get(ch) + 1);
            else collapsСhars.put(ch, (double) 1);
        }
    }

    /**
     * Подсчет количества вхождений для каждого символа в строке
     *
     * @param sequence запрос
     * @return отображение "символ" -> "его количество"
     */
    public HashMap<Character, Double> computeUnorderedFrequency(String sequence) {
        HashMap<Character, Double> frequency = new HashMap<>();
        char ch;
        for (int i = 0; i < sequence.length(); i++) {
            ch = sequence.charAt(i);
            if (frequency.containsKey(ch))
                frequency.put(ch, frequency.get(ch) + 1);
            else frequency.put(ch, (double) 1);
        }
        return frequency;
    }

    /**
     * тренировка модели "Распределение символов строки"
     *
     * @param sequences запросы для обучения
     */
    public void train(ArrayList<String> sequences) {
        // Заполняем collapsСhars, т.е. считаем количество каждых символов
        for (String sequence : sequences) {
            fillCollapsСhars(sequence);
            countAllChars += sequence.length();
        }
        // вычисляем частоту каждого символа
        double finalCountAllChars1 = countAllChars;

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
        int k = 0;
        // делим вероятности на интервалы
        for (Map.Entry<Character, Double> pair : sortedGlobalFrequencies.entrySet()) {
            double value = pair.getValue();
            boolean F = true;
            if (value < 0.0005) {
                intervals.set(0, intervals.get(0) + value);
                k++;
            } else if (value < 0.001) {
                if (F) {
                    intervals.set(0, intervals.get(0) / k);
                    k = 0;
                    F = false;
                }
                intervals.set(1, intervals.get(1) + value);
                k++;
            } else if (value < 0.009) {
                if (!F) {
                    intervals.set(1, intervals.get(1) / k);
                    k = 0;
                    F = true;
                }
                intervals.set(2, intervals.get(2) + value);
                k++;
            } else if (value < 0.03) {
                if (F) {
                    intervals.set(2, intervals.get(2) / k);
                    k = 0;
                    F = false;
                }
                intervals.set(3, intervals.get(3) + value);
                k++;
            } else {
                if (!F) {
                    intervals.set(3, intervals.get(3) / k);
                    k = 0;
                    F = true;
                }
                intervals.set(4, intervals.get(4) + value);
                k++;
            }
        }
        intervals.set(4, intervals.get(4) / k);
        findingThreshold(sequences);

        System.out.println("=====================");
        sortedGlobalFrequencies.entrySet().stream().forEach(System.out::println);
        //System.out.println("threshold = "+threshold);
    }

    /**
     * определение границы аномальности
     *
     * @param sequences массив запросов
     */
    void findingThreshold(ArrayList<String> sequences) {
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
    }

    /**
     * вычисление вероятности
     *
     * @param sequence запрос
     * @return вероятность
     */
    double probability(String sequence) {
        // построить частотный массив для данной строки
        HashMap<Character, Double> frequency = new HashMap<>();
        frequency = computeUnorderedFrequency(sequence);
        return chiSquareTest(frequency, sortedGlobalFrequencies, sequence.length());
    }


    /**
     * обнаружение аномалий по модели "Распределение символов строки"
     *
     * @param sequence запрос
     * @return true - нормальный, false - аномальный
     */
    public boolean validate(String sequence) {
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
        return p < threshold;
    }

    private double chiSquare(double observedValue, double expectedValue) {
        return Math.pow(observedValue - expectedValue, 2) / expectedValue;
    }

    /**
     * хи квадрат тест
     *
     * @param observed наблюдаемая
     * @param expected  ожидаемая
     * @param sequenceLength длина последовательности
     * @return вероятность
     */
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
