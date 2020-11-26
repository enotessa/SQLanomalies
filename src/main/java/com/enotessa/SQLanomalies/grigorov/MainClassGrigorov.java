package com.enotessa.SQLanomalies.grigorov;

import org.hibernate.Session;

import java.sql.SQLException;

public class MainClassGrigorov {
    Session session;
    ScriptRunner scriptRunner;

    public MainClassGrigorov(Session session){
        this.session=session;
        scriptRunner = new ScriptRunner(session);
    }

    public void methodRun(String queryStr) throws SQLException {
        scriptRunner.runScript(queryStr);
    }


}
