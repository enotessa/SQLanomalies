package com.enotessa.SQLanomalies;

import com.enotessa.SQLanomalies.grigorov.MainClassGrigorov;
import com.enotessa.SQLanomalies.grigorov.ScriptRunner;
import com.enotessa.SQLanomalies.mutz.CheckDistribution;
import com.enotessa.SQLanomalies.mutz.CheckLength;
import com.enotessa.SQLanomalies.mutz.CheckToken;
import com.enotessa.SQLanomalies.persistence.HibernateUtil;
import org.hibernate.Session;
import org.python.core.PyInteger;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App
{
    static String query;
    static private Roles role;
    static Scanner in = new Scanner(System.in);
    static FileReader fileWithQueries;
    static ReadSQL readSQL;
    static PythonInterpreter interp = new PythonInterpreter();

    public static void main(String[] args) throws Exception
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        System.out.println("Введите роль");
        //String r = in.nextLine();
        String r="r1";
        switch (r){
            case "r1":
                role = Roles.ADMIN;
                fileWithQueries = new FileReader("C:\\Users\\Admin\\IdeaProjects\\SQLanomalies\\src\\main\\resources\\queries.txt");
                //fileWithQueries = new FileReader("ADMINqueries.txt");
                break;

            case "r2":
                role = Roles.REGISTRY;
                fileWithQueries = new FileReader("REGISTRYqueries.txt");
                break;

            case "r3":
                role = Roles.DOCTOR;
                fileWithQueries = new FileReader("DOCTORqueries.txt");
                break;

            case "r4":
                role = Roles.VISITOR;
                fileWithQueries = new FileReader("VISITORqueries.txt");
                break;

        }
        System.out.println("!!!");
        readSQL = new ReadSQL(fileWithQueries);

        for (String str : readSQL.arrayList){
            System.out.println(str);
        }


        CheckLength checkLength = new CheckLength();
        CheckDistribution checkDistribution = new CheckDistribution();
        //CheckGramma checkGramma = new CheckGramma();
        CheckToken checkToken =new CheckToken();
        System.out.println("Введите проверяемую строку");
        //String query = in.nextLine();
        String query = "UPDATE owner SET patron = \"Игоревич\" WHERE tel = \"89527356477\";";

        /*checkLength.train(readSQL.arrayList);
        System.out.println("Проверка на длину строки : " + checkLength.validate(query));

        checkDistribution.train(readSQL.arrayList);
        System.out.println("Проверка на распределение символов строки : " + checkDistribution.validate(query));

        //checkGramma.train(readSQL.arrayList);

        checkToken.train(readSQL.arrayList);
        System.out.println("Проверка токенов. независимая модель : " + checkToken.validate(query));*/


        MainClassGrigorov mainClassGrigorov = new MainClassGrigorov(session);
        mainClassGrigorov.methodRun(query);
    }

}