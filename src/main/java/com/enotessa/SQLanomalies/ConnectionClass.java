package com.enotessa.SQLanomalies;

import com.enotessa.SQLanomalies.Roles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    static private Roles role;
    public Connection connection;

    /**
     * получение соединения с БД
     *
     * @param login логин
     * @param password пароль
     * @return объект Connection
     */
    public ConnectionClass(String login, String password) throws SQLException {
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
        connection = DriverManager.getConnection(URL, login, password);
    }
}
