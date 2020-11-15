package com.enotessa.SQLanomalies;

import com.enotessa.SQLanomalies.persistence.HibernateUtil;
import com.enotessa.SQLanomalies.entities.Therapy;
import org.hibernate.Session;

public class App
{
    static String query;

    public static void main(String[] args) throws Exception
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Therapy therapy = new Therapy();
        therapy.setId_therapy(500016);
        therapy.setDescription("курс Bayer");
        session.save(therapy);
        session.getTransaction().commit();


        /*Scanner in = new Scanner(System.in);
        System.out.println("Введите запрос");
        query = in.nextLine();
        System.out.println("Введено");*/
    }
}
