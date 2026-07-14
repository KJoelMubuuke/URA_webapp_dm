package org.ura.dao;

import org.ura.entity.TaxClearance;
import org.ura.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class TaxClearanceDAO {

    public void save(TaxClearance clearance) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(clearance);
            session.getTransaction().commit();
        }
    }

    public TaxClearance findByTaxId(Long taxId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<TaxClearance> query = session.createQuery(
                    "from TaxClearance c where c.tax.id = :taxId", TaxClearance.class);
            query.setParameter("taxId", taxId);
            return query.uniqueResult();
        }
    }

    public TaxClearance findByCertificateNumber(String certNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<TaxClearance> query = session.createQuery(
                    "from TaxClearance c where c.certificateNumber = :cert", TaxClearance.class);
            query.setParameter("cert", certNumber);
            return query.uniqueResult();
        }
    }
}
