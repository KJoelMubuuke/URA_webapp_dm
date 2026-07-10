package org.ura.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        createDatabaseIfNotExists();
        return new Configuration().configure().buildSessionFactory();
    }

    private static void createDatabaseIfNotExists() {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "light";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS ura_tax_db");
        } catch (Exception e) {
            throw new RuntimeException("Failed to auto-create database", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}