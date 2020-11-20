package com.enotessa.SQLanomalies;

import com.enotessa.SQLanomalies.mutz.CheckDistribution;
import com.enotessa.SQLanomalies.mutz.CheckLength;
import com.enotessa.SQLanomalies.persistence.HibernateUtil;
import org.hibernate.Session;

import java.io.FileReader;
import java.util.Scanner;

public class App
{
    static String query;
    static private Roles role;
    static Scanner in = new Scanner(System.in);
    static FileReader fileWithQueries;
    static ReadSQL readSQL;

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

        checkLength.train(readSQL.arrayList);
        System.out.println("var(средн значение) = " + checkLength.var+" , mean(дисперсия) = "+checkLength.mean);
        System.out.println(checkLength.validate("UPDATE owner SET patron = \"Игоревич\" WHERE tel = \"89527356477\";"));
        checkDistribution.train(readSQL.arrayList);



        /*
        ВСТАВКА В БД

        Therapy therapy = new Therapy();
        therapy.setId_therapy(500017);
        therapy.setDescription("курс Bayer");
        session.save(therapy);
        session.getTransaction().commit();*/

        /*
        ЗАПРОС SELECT в БД

        Query query = session.createSQLQuery("SELECT * FROM vetclinic.doctor;").addEntity(Doctor.class);
        List<Doctor> doctors = query.list();

        System.out.println("users.size = " + doctors.size());
        for (Iterator<Doctor> it = doctors.iterator(); it.hasNext();) {
            Doctor doctor = (Doctor) it.next();
            System.out.println(doctor.toString());
        }*/

        /*Scanner in = new Scanner(System.in);
        System.out.println("Введите запрос");
        query = in.nextLine();
        System.out.println("Введено");*/
    }
}