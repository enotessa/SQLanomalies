package com.enotessa.SQLanomalies.vmv;

import com.enotessa.SQLanomalies.ConnectionClass;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO придумать запросы для перечислений. То есть таких, чтобы значений атрибута было мало, а самих атрибуьов много. (повторяющиеся атрибуты)

// Поиск токенов. независимая модель
public class CheckToken {
    ConnectionClass connectionClass;
    double threshold;

    HashMap<String, ArrayList<String>> tokens = new HashMap<>();
    HashMap<String, ArrayList<String>> tokensCopy = new HashMap<>();
    Set<String> tokensCopySet;
    HashMap<String, Double> probabilities;

    /**
     * тренировка модели "Поиск токенов. независимая модель"
     *
     * @param sequences запросы для обучения
     */
    public void train(ArrayList<String> sequences, ConnectionClass cC) {
        connectionClass = cC;
        for (String sequence : sequences)
            token(sequence);
        // создаем HashMap с уникальными значениями
        for (Map.Entry<String, ArrayList<String>> pair : tokens.entrySet()){
            for (String str : pair.getValue()){
                if (tokensCopy.containsKey(pair.getKey())){
                    if (!tokensCopy.get(pair.getKey()).contains(str)){
                        tokensCopy.get(pair.getKey()).add(str);
                    }
                }
                else {
                    tokensCopy.put(pair.getKey(), new ArrayList<>());
                    tokensCopy.get(pair.getKey()).add(str);
                }
            }
        }


        int K = 0;
        probabilities = new HashMap<>(tokens.size());
        // считаем вероятности
        // N-количество всех выборок, n-текущее количество выбранных выборок,
        // K-всего найдено данных литералов, k-найдено новых данных литералов в текущей выборке(которых не было)+ старые
        for (Map.Entry<String, ArrayList<String>> pair : tokens.entrySet()) {
            K = pair.getValue().size();
            probabilities.put(pair.getKey(), probabilityForTrain(sequences, pair.getKey(), K));
        }
    }

    /**
     * обнаружение аномалий по модели "Поиск токенов. независимая модель"
     *
     * @param sequence запрос
     * @return true - нормальный, false - аномальный
     */
    public boolean validate(String sequence) {
        double threshold = 0.8;
        HashMap<String, ArrayList<String>> tokensSequence = new HashMap<>();

        Pattern pattern = Pattern.compile("INSERT");
        Matcher matcher = pattern.matcher(sequence);
        if (matcher.find()) {
            ArrayList<String> nameOfColumns= new ArrayList<>();
            pattern = Pattern.compile("INTO ([^ ]*)");
            matcher = pattern.matcher(sequence);
            matcher.find();
            String table = matcher.group(1);
            StringBuilder str = new StringBuilder("SELECT * FROM ");
            str.append(table).append(";");
            Statement stmt = null;
            int qountColumns = 0;
            try {
                stmt = connectionClass.connection.createStatement();
                ResultSet rs = null;
                rs = stmt.executeQuery(str.toString());
                ResultSetMetaData rsmd = rs.getMetaData();
                qountColumns = rsmd.getColumnCount();
                for (int i = 1; i<= qountColumns; i++){
                    nameOfColumns.add(rsmd.getColumnName(i));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            pattern = Pattern.compile("( \\(.*)");
            matcher = pattern.matcher(sequence);
            matcher.find();
            StringBuilder str2 = new StringBuilder(matcher.group(1));

            pattern = Pattern.compile("[( \"']([\\d]+)[,'\")]|([\"'][^'\"]+[\"'])");
            matcher = pattern.matcher(str2);
            int k=0;
            while (matcher.find()) {
                if (tokensSequence.containsKey(nameOfColumns.get(k))) {
                    if (matcher.group(1) != null)
                        tokensSequence.get(nameOfColumns.get(k)).add(matcher.group(1));
                    else tokensSequence.get(nameOfColumns.get(k)).add(matcher.group(2));
                }
                else {
                    if (matcher.group(1) != null)
                        tokensSequence.put(nameOfColumns.get(k), new ArrayList<String>(Collections.singleton(matcher.group(1))));
                    else tokensSequence.put(nameOfColumns.get(k), new ArrayList<String>(Collections.singleton(matcher.group(2))));
                }
                k++;
                if (k==qountColumns)
                    k=0;
            }

        } else {
            pattern = Pattern.compile("(([^ .]*)( (=|>=|<=|<|>|LIKE) )([^ (]*)( |;))");
            matcher = pattern.matcher(sequence);
            while (matcher.find()) {
                if (tokensSequence.containsKey(matcher.group(2)))
                    tokensSequence.get(matcher.group(2)).add(matcher.group(5));
                else tokensSequence.put(matcher.group(2), new ArrayList<String>(Collections.singleton(matcher.group(5))));  // TODO кладет с запятыми??
            }
        }

        boolean F;
        for (Map.Entry<String, ArrayList<String>> pair : tokensSequence.entrySet()) {
            if (threshold <= probabilities.get(pair.getKey())) {
                for (String str1 : pair.getValue()) {
                    F = false;
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

    /**
     * статистический тест Колмогорова-Смирнова
     *
     * @param sequences запросы
     * @param key       атрибут
     * @param K         всего найдено данных литералов
     * @return вероятность
     */
    // считаем вероятности
    // N-количество всех выборок, n-текущее количество выбранных выборок,
    // K-всего найдено данных литералов, k-найдено новых данных литералов в текущей выборке(которых не было)+ старые
    double probabilityForTrain(ArrayList<String> sequences, String key, double K) {
        double k = 0;
        double N = 0;
        Pattern pattern;
        Matcher matcher;
        // считаем N
        for (String s : sequences) {
            pattern = Pattern.compile("INSERT");
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                pattern = Pattern.compile("INTO ([^ ]*)");
                matcher = pattern.matcher(s);
                matcher.find();
                String table = matcher.group(1);
                StringBuilder str = new StringBuilder("SELECT * FROM ");
                str.append(table).append(";");
                Statement stmt = null;
                int qountColumns = 0;
                try {
                    stmt = connectionClass.connection.createStatement();
                    ResultSet rs = null;
                    rs = stmt.executeQuery(str.toString());
                    ResultSetMetaData rsmd = rs.getMetaData();
                    qountColumns = rsmd.getColumnCount();
                    for (int i = 1; i<= qountColumns; i++){
                        if (rsmd.getColumnName(i).equals(key)){
                            N++;
                            break;
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            else {
                String regex = "(" + key + "( (=|>=|<=|<|>|LIKE) )([^ (]*)( |;))";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(s);
                if (matcher.find())
                    N++;
            }
        }
        boolean F;
        double n = 0;
        double max = 0;
        for (String sequence : sequences) {
            F = true;
            pattern = Pattern.compile("INSERT");
            matcher = pattern.matcher(sequence);
            if (matcher.find()){
                ArrayList<String> nameOfColumns= new ArrayList<>();
                pattern = Pattern.compile("INTO ([^ ]*)");
                matcher = pattern.matcher(sequence);
                matcher.find();
                String table = matcher.group(1);
                StringBuilder str = new StringBuilder("SELECT * FROM ");
                str.append(table).append(";");
                Statement stmt = null;
                int qountColumns = 0;
                int columnI =100;    // номер столбца с нужным атрибутом
                try {
                    stmt = connectionClass.connection.createStatement();
                    ResultSet rs = null;
                    rs = stmt.executeQuery(str.toString());
                    ResultSetMetaData rsmd = rs.getMetaData();
                    qountColumns = rsmd.getColumnCount();
                    for (int i = 1; i<= qountColumns; i++){
                        nameOfColumns.add(rsmd.getColumnName(i));
                        if (rsmd.getColumnName(i).equals(key)){
                            columnI = i;    //TODO break;
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                pattern = Pattern.compile("( \\(.*)");
                matcher = pattern.matcher(sequence);
                matcher.find();
                StringBuilder str2 = new StringBuilder(matcher.group(1));

                pattern = Pattern.compile("[( \"']([\\d]+)[,'\")]|([\"'][^'\"]+[\"'])");
                matcher = pattern.matcher(str2);
                int o=0; // текущий столбец //TODO if (!columnI==100)
                while (matcher.find()) {
                    if (o==columnI-1) {
                        if (F) n++;
                        if (matcher.group(1) != null) {
                            if (!tokensCopy.get(key).isEmpty() && tokensCopy.get(key).contains(matcher.group(1))) {   // найдено новых вхождений
                                tokensCopy.get(key).remove(matcher.group(1));
                                k++;
                            }
                            F = false;
                        }
                        if (matcher.group(2) != null) {
                            if (!tokensCopy.get(key).isEmpty() && tokensCopy.get(key).contains(matcher.group(2))) {   // найдено новых вхождений
                                tokensCopy.get(key).remove(matcher.group(2));
                                k++;
                            }
                            F = false;
                        }
                        if (!F) {
                            if (max == 0) {
                                max = Math.abs(k / K - (double) n / N);
                            } else if (max < (Math.abs(k / K - (double) n / N))) {
                                max = Math.abs(k / K - (double) n / N);
                            }
                            if (k == K) break;
                        }
                    }
                    o++;
                    if (o==qountColumns) o=0;
                }
            }
            else {
                String regex = "(" + key + "( (=|>=|<=|<|>|LIKE) )([^ (]*)( |;))";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(sequence);
                while (matcher.find()) {
                    if (F) n++;
                    if (!tokensCopy.get(key).isEmpty() && tokensCopy.get(key).contains(matcher.group(4))) {   // найдено новых вхождений
                        tokensCopy.get(key).remove(matcher.group(4));
                        //tokensCopy.remove(key, tokensCopy.get(key).remove(matcher.group(4)));
                        k++;
                    }
                    F = false;
                }
                if (!F) {
                    if (max == 0) {
                        max = Math.abs(k / K - (double) n / N);
                    } else if (max < (Math.abs(k / K - (double) n / N))) {
                        max = Math.abs(k / K - (double) n / N);
                    }
                    if (k == K) break;
                }
            }
            if (n==N) break;
        }




        /*String regex = "(" + key + "( (=|>=|<=|<|>|LIKE) )([^ (]*)( |;))";
        Pattern pattern;
        Matcher matcher;
        double max = 0;
        // считаем N
        for (String s : sequences) {
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(s);
            if (matcher.find())
                N++;
        }*/

        /*boolean F;
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
                } else if (max < (Math.abs(k / K - (double) n / N))) {
                    max = Math.abs(k / K - (double) n / N);
                }
                if (k == K) break;
            }
        }*/
        return max * Math.sqrt(K);
    }

    /**
     * токенизация
     *
     * @param sequence запросы
     */
    void token(String sequence) {
        Pattern pattern = Pattern.compile("INSERT");
        Matcher matcher = pattern.matcher(sequence);
        if (matcher.find()) {
            ArrayList<String> nameOfColumns= new ArrayList<>();
            pattern = Pattern.compile("INTO ([^ ]*)");
            matcher = pattern.matcher(sequence);
            matcher.find();
            String table = matcher.group(1);
            StringBuilder str = new StringBuilder("SELECT * FROM ");
            str.append(table).append(";");
            Statement stmt = null;
            int qountColumns = 0;
            try {
                stmt = connectionClass.connection.createStatement();
                ResultSet rs = null;
                rs = stmt.executeQuery(str.toString());
                ResultSetMetaData rsmd = rs.getMetaData();
                qountColumns = rsmd.getColumnCount();
                for (int i = 1; i<= qountColumns; i++){
                    nameOfColumns.add(rsmd.getColumnName(i));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            pattern = Pattern.compile("( \\(.*)");
            matcher = pattern.matcher(sequence);
            matcher.find();
            StringBuilder str2 = new StringBuilder(matcher.group(1));

            pattern = Pattern.compile("[( \"']([\\d]+)[,'\")]|([\"'][^'\"]+[\"'])");
            matcher = pattern.matcher(str2);
            int k=0;
            while (matcher.find()) {
                if (tokens.containsKey(nameOfColumns.get(k))) {
                    if (matcher.group(1) != null)
                        tokens.get(nameOfColumns.get(k)).add(matcher.group(1));
                    else tokens.get(nameOfColumns.get(k)).add(matcher.group(2));
                }
                else {
                    if (matcher.group(1) != null)
                        tokens.put(nameOfColumns.get(k), new ArrayList<String>(Collections.singleton(matcher.group(1))));
                    else tokens.put(nameOfColumns.get(k), new ArrayList<String>(Collections.singleton(matcher.group(2))));
                }
                k++;
                if (k==qountColumns)
                    k=0;
            }

        } else {
            pattern = Pattern.compile("(([^ .]*)( (=|>=|<=|<|>|LIKE) )([^ (]*)( |;))");
            matcher = pattern.matcher(sequence);
            while (matcher.find()) {
                if (tokens.containsKey(matcher.group(2)))
                    tokens.get(matcher.group(2)).add(matcher.group(5));
                else tokens.put(matcher.group(2), new ArrayList<String>(Collections.singleton(matcher.group(5))));
            }
        }
    }
}
