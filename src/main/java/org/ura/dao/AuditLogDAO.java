package org.ura.dao;

import org.ura.entity.AuditLog;
import org.ura.util.HibernateUtil;
import org.hibernate.Session;

public class AuditLogDAO {

    public void save(AuditLog log) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(log);
            session.getTransaction().commit();
        }
    }
}
