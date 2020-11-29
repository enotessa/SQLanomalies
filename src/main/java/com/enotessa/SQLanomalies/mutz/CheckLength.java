package com.enotessa.SQLanomalies.mutz;
import java.util.ArrayList;

/**
 * Модель: Длина строки
 */
public class CheckLength {
    public double mean; // среднее значение длин mu
    public double var;  // выборочная дисперсия sigma^2
    ArrayList<Integer> lengths = new ArrayList();


    /**
     * тренировка модели "Длина строки"
     *
     * @param sequences запросы для обучения
     */
    public void train(ArrayList<String> sequences) {
        sequences.forEach(sequence -> lengths.add(sequence.length()));
        mean = 0;
        for (Integer x : lengths)
            mean += x;
        mean /= lengths.size();
        double temp = 0;
        for (double x : lengths)
            temp += (x - mean) * (x - mean);
        var = temp / (lengths.size() - 1);
    }

    /**
     * обнаружение аномалий по модели "Длина строки"
     *
     * @param sequence запрос
     * @return true - нормальный, false - аномальный
     */
    public boolean validate(String sequence) {
        double threshold = 0.1;
        double upper_bound = this.var / Math.pow((sequence.length() - this.mean), 2);
        return upper_bound >= threshold;
    }
}

