package org.ura.dao;

import org.ura.entity.Payment;
import org.ura.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class PaymentDAO {

    public void save(Payment payment) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(payment);
            session.getTransaction().commit();
        }
    }

    public Payment findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Payment.class, id);
        }
    }

    public List<Payment> findByTaxId(Long taxId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Payment> query = session.createQuery(
                    "from Payment p where p.tax.id = :taxId", Payment.class);
            query.setParameter("taxId", taxId);
            return query.list();
        }
    }
}
