package com.enotessa.SQLanomalies.grigorov;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.List;

public class ScriptRunner {
    private EntityManager entityManager;
    Session session;

    public ScriptRunner(Session session) {
        this.session = session;
    }

    public void runScript(String queryStr) throws SQLException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.enotessa.SQLanomalies");
        entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();

        List<Object[]> results = entityManager.createQuery(queryStr).getResultList();

        for (Object[] result : results) {
            for (int i = 0; i < results.size(); i++)
                System.out.print(result[i] + " ");
            System.out.println("\n");
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
