package com.enotessa.SQLanomalies.mutz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckDistribution {
    HashMap<String, Integer> order = new HashMap<>();
    /*HashMap<Character, Float> frequency = new HashMap<>();*/

    HashMap<Character, Integer> collapsСhars = new HashMap<>(); // для подсчета символов
    ArrayList<HashMap<Character, Float>> frequencies = new ArrayList();
    //HashMap<Integer, HashMap<Character, Float>> frequencies = new HashMap<>();
    HashMap<Character, Float> globalFrequencies = new HashMap<>();

    public CheckDistribution(){
        for (int i = 0; i<=9; i++)
            collapsСhars.put((char)(i + '0'), 0);
        for (int i = (int)'a'; i <= (int)'z'; i++)
            collapsСhars.put((char) i, 0);
        for (int i = (int)'A'; i <= (int)'Z'; i++)
            collapsСhars.put((char) i, 0);

        for (int i = 0; i<=9; i++)
            globalFrequencies.put((char)(i + '0'), (float) 0.0);
        for (int i = (int)'a'; i <= (int)'z'; i++)
            globalFrequencies.put((char) i, (float) 0.0);
        for (int i = (int)'A'; i <= (int)'Z'; i++)
            globalFrequencies.put((char) i, (float) 0.0);
    }

    public HashMap<Character, Float> computeUnorderedFrequency(String sequence){
        // Подсчет количества вхождений для каждого символа
        HashMap<Character, Float> frequency = new HashMap<>();
        char ch;
        for (int i=0;i<sequence.length();i++) {
            ch = sequence.charAt(i);
            if (collapsСhars.containsKey(ch)) {
                collapsСhars.put(ch, collapsСhars.get(ch) + 1);
            }
        }
        for (int i = 0; i<=9; i++)
            frequency.put((char)(i + '0'), (Float.valueOf(collapsСhars.get((char)(i + '0')))) / sequence.length());
        for (int i = (int)'a'; i <= (int)'z'; i++)
            frequency.put((char) i, (Float.valueOf(collapsСhars.get((char) i))) / sequence.length());
        for (int i = (int)'A'; i <= (int)'Z'; i++)
            frequency.put((char) i, (Float.valueOf(collapsСhars.get((char) i))) / sequence.length());
        return frequency;
    }

    public void train(ArrayList<String> sequences){
        // Частота вычисления для каждой последовательности
        int i=0;
        for (String sequence : sequences){
            frequencies.add(computeUnorderedFrequency(sequence));
            //frequencies.put(i, computeUnorderedFrequency(sequence));
            i++;
        }
        //frequency.clear();
        //sequences.forEach(this::computeUnorderedFrequency);

        // вычислить частоту по всем последовательностям
        i=0;
        for (HashMap<Character, Float> frequency : frequencies) {
            for (Map.Entry<Character, Float> pair: frequency.entrySet())
                globalFrequencies.put(pair.getKey(), globalFrequencies.get(pair.getKey())+pair.getValue());
            i++;
        }
        int lengthOfFrequencies = frequencies.size();
        for (Map.Entry<Character, Float> pair: globalFrequencies.entrySet())
            globalFrequencies.put(pair.getKey(), pair.getValue()/lengthOfFrequencies);  // вычислили среднюю частоту по всем обучающим последовательностям
    }

    public boolean validate(String sequence){
        /*double threshold = 0.1;
        // Построить частотный массив для данной строки
        HashMap<Character, Float> frequency = new HashMap<>(computeUnorderedFrequency(sequence));
        for (Map.Entry<Character, Float> pair: frequency.entrySet()){
            if (!globalFrequencies.containsKey(pair.getKey())){
                System.out.println("Invalid character");
                return false;
            }
        }
        // Сравнить входную последовательность с опорной частотой

        *//*
        (chisq, p) = scipy.stats.chisquare(ordered_frequency, model.frequency)*//*
        return p >= threshold;*/
        return true;
    }

}
