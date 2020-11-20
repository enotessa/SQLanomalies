package com.enotessa.SQLanomalies;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReadSQL {
    FileReader file;
    public ArrayList<String> arrayList = new ArrayList();

    public ReadSQL(FileReader fileWithQueries){
        file = fileWithQueries;
        readFile();
    }

    void readFile(){
        int n = 0;
        BufferedReader reader = new BufferedReader(file);
        // считаем сначала первую строку
        String line = null;
        try {
            line = reader.readLine();
        while (line != null) {
            if (line.equals("")){
                n++;
            }
            else {
                if (arrayList.size()==n){
                    arrayList.add(line);
                }
                else {
                    arrayList.set(n, arrayList.get(n) + " " + line);
                }
            }
            // считываем остальные строки в цикле
            line = reader.readLine();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
