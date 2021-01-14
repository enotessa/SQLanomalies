package com.enotessa.SQLanomalies.vmv;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Модель: Длина строки
 */
public class CheckLength {
    public double mean; // среднее значение длин mu
    public double var;  // выборочная дисперсия sigma^2
    ArrayList<Integer> lengths = new ArrayList();
    ArrayList<String> arguments = new ArrayList<>();

    Pattern pattern;
    Matcher matcher;


    /**
     * тренировка модели "Длина строки"
     *
     * @param sequences запросы для обучения
     */
    public void train(ArrayList<String> sequences) {
        arguments = getArguments(sequences);
        arguments.forEach(arguments -> lengths.add(arguments.length()));
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

    /**
     * метод. который берет аргументы строк
     */
    public ArrayList<String> getArguments(ArrayList<String> sequences){
        ArrayList<String> arguments = new ArrayList<>();
        for (String s : sequences){
            pattern = Pattern.compile("INSERT");
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                s = s.split("\\(")[1];
                pattern = Pattern.compile("([\\( ])([^,)]*)");
                matcher = pattern.matcher(s);
                while (matcher.find())
                    arguments.add(matcher.group(2));
            }
            else {
                String regex = "(([^ .]*)( (=|>=|<=|<|>|LIKE) )([^ (]*)( |;))";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(s);
                while (matcher.find())
                    arguments.add(matcher.group(5));
            }
        }
        return arguments;
    }
}

