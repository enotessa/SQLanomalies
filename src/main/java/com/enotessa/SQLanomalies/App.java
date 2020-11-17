package com.enotessa.SQLanomalies;

import com.enotessa.SQLanomalies.entities.Doctor;
import com.enotessa.SQLanomalies.persistence.HibernateUtil;
import com.enotessa.SQLanomalies.entities.Therapy;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;

import java.util.Iterator;
import java.util.List;

public class App
{
    static String query;

    public static void main(String[] args) throws Exception
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();








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
