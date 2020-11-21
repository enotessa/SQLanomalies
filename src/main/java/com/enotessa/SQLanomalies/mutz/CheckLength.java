package com.enotessa.SQLanomalies.mutz;
/*Длина строки
Проверяет, является ли длина маловероятной.
Учитывая, что распределение длины последовательности неизвестно и
вряд ли можно сделать какие-либо разумные предположения о нем
, вместо этого используется неравенство Чебышева.
Неравенство Чебышева говорит, что вероятность того, что что-то (x)
отклоняется от среднего значения больше, чем порог (t), меньше, чем `\sigma^2 / t^2`
                    p(|x - \mu| > t) < \sigma^2 / t^2
Это переформулируется в "вероятность того, что что-то (x)
отклоняется больше от среднего, чем текущее отклонение (|l - \mu|). Where `l`
это текущая длина строки ресурса.
              p(|x - \mu| > |l - \mu|) < \sigma^2 / (l - \mu)^2*/

import java.util.ArrayList;

/**
 * Модель: Длина строки
 */
public class CheckLength {
    public double mean; // среднее значение длин mu
    public double var;  // выборочная дисперсия sigma^2
    ArrayList<Integer> lengths = new ArrayList();


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

    public boolean validate(String sequence) {
        double threshold = 0.1;
        double upper_bound = this.var / Math.pow((sequence.length() - this.mean), 2);
        return upper_bound >= threshold;
    }
}

