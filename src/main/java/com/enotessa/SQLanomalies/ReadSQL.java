package com.enotessa.SQLanomalies;

import java.io.*;
import java.util.ArrayList;

public class ReadSQL {
    public ReadSQL() throws IOException {
    }

    /**
     * прочитать файл с запросами и поместить их в массив
     */
    ArrayList<String> readFile(String file) throws IOException {
        ArrayList<String> arrayList = new ArrayList();
        int n = 0;
        System.out.println(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Admin\\IdeaProjects\\SQLanomalies\\src\\main\\resources\\"+file), "UTF-8"));

        //BufferedReader reader = new BufferedReader(file);
        // считаем сначала первую строку
        String line = null;
        line = reader.readLine();
        while (line != null) {
            if (line.equals("")) {
                n++;
            } else {
                if (arrayList.size() == n) {
                    arrayList.add(line);
                } else {
                    arrayList.set(n, arrayList.get(n) + " " + line);
                }
            }
            // считываем остальные строки в цикле
            line = reader.readLine();
        }
        return arrayList;
    }

}
