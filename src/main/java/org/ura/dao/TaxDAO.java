package org.ura.dao;

import org.ura.entity.Tax;
import org.ura.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class TaxDAO {

    public void save(Tax tax) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(tax);
            session.getTransaction().commit();
        }
    }

    public Tax findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Tax.class, id);
        }
    }

    public List<Tax> findByTaxpayerId(Long taxpayerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Tax> query = session.createQuery(
                    "from Tax t where t.taxpayer.id = :tid", Tax.class);
            query.setParameter("tid", taxpayerId);
            return query.list();
        }
    }

    public void update(Tax tax) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(tax);
            session.getTransaction().commit();
        }
    }
}