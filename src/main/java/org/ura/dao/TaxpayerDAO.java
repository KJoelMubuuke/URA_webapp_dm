package org.ura.dao;

import org.ura.entity.Taxpayer;
import org.ura.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class TaxpayerDAO {

    public void save(Taxpayer taxpayer) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(taxpayer);
            session.getTransaction().commit();
        }
    }

    public boolean existsByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "select count(t) from Taxpayer t where t.email = :email", Long.class);
            query.setParameter("email", email);
            return query.uniqueResult() > 0;
        }
    }

    public boolean existsByTin(String tin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "select count(t) from Taxpayer t where t.tin = :tin", Long.class);
            query.setParameter("tin", tin);
            return query.uniqueResult() > 0;
        }
    }

    public Taxpayer findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Taxpayer> query = session.createQuery(
                    "from Taxpayer t where t.email = :email", Taxpayer.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        }
    }

    public Taxpayer findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Taxpayer.class, id);
        }
    }

    public List<Taxpayer> findByRole(Taxpayer.Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Taxpayer> query = session.createQuery(
                    "from Taxpayer t where t.role = :role", Taxpayer.class);
            query.setParameter("role", role);
            return query.list();
        }
    }
}