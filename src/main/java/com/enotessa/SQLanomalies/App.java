package com.enotessa.SQLanomalies;

import com.enotessa.SQLanomalies.grigorov.MainClassGrigorov;
import com.enotessa.SQLanomalies.mutz.CheckDistribution;
import com.enotessa.SQLanomalies.mutz.CheckLength;
import com.enotessa.SQLanomalies.mutz.CheckToken;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

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
        Connection connection = null;
        System.out.println("логин");
        String login = "Admin";
        //String login = in.nextLine();
        System.out.println("пароль");
        String password = "Admin";
        //String password = in.nextLine();
        try {
            connection = getConnection(login, password);       // подсоединение к бд

        } catch (SQLException e) {
            e.printStackTrace();
        }
        fileWithQueries = new FileReader("C:\\Users\\Admin\\IdeaProjects\\SQLanomalies\\src\\main\\resources\\queries.txt");
        readSQL = new ReadSQL(fileWithQueries);


        CheckLength checkLength = new CheckLength();
        CheckDistribution checkDistribution = new CheckDistribution();
        //CheckGramma checkGramma = new CheckGramma();
        CheckToken checkToken =new CheckToken();
        System.out.println("Введите проверяемую строку");
        //String query = in.nextLine();
        String query = "SELECT count(id_pet) AS count_pet_from_Moscow FROM pet NATURAL JOIN owner WHERE owner.address REGEXP \"г.Тверь\" = 1;";

        /*checkLength.train(readSQL.arrayList);
        System.out.println("Проверка на длину строки : " + checkLength.validate(query));

        checkDistribution.train(readSQL.arrayList);
        System.out.println("Проверка на распределение символов строки : " + checkDistribution.validate(query));

        //checkGramma.train(readSQL.arrayList);

        checkToken.train(readSQL.arrayList);
        System.out.println("Проверка токенов. независимая модель : " + checkToken.validate(query));*/

        MainClassGrigorov mainClassGrigorov = new MainClassGrigorov(connection);
        mainClassGrigorov.methodRun(query);






    }

    /**
     * получение соединения с БД
     *
     * @param login логин
     * @param password пароль
     * @return объект Connection
     */
    private static Connection getConnection(String login, String password) throws SQLException {
        String URL = "jdbc:mysql://localhost:3306/vetClinic";
        switch (login) {
            case "Admin":
                role = Roles.ADMIN;
                break;
            case "Registry":
                role = Roles.REGISTRY;
                break;
            case "Doctor":
                role = Roles.DOCTOR;
                break;
            case "Visitor":
                role = Roles.VISITOR;
                break;
        }
        return DriverManager.getConnection(URL, login, password);
    }

}