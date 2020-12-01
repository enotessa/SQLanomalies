package com.enotessa.SQLanomalies;

import java.io.*;
import java.util.ArrayList;

public class ReadSQL {
    FileReader file;
    public ArrayList<String> arrayList = new ArrayList();

    public ReadSQL() throws IOException {
        readFile();
    }

    /**
     * прочитать файл с запросами и поместить их в массив
     */
    void readFile() throws IOException {
        int n = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Admin\\IdeaProjects\\SQLanomalies\\src\\main\\resources\\queries.txt"), "UTF-8"));

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
    }

}
