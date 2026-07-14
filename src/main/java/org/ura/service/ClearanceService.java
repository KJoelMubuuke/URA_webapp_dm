package org.ura.service;

import org.ura.dao.TaxClearanceDAO;
import org.ura.dao.AuditLogDAO;
import org.ura.entity.AuditLog;
import org.ura.entity.Tax;
import org.ura.entity.TaxClearance;
import org.ura.entity.Taxpayer;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClearanceService {

    private final TaxClearanceDAO clearanceDAO = new TaxClearanceDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    /**
     * Issues a Tax Clearance Certificate for a fully paid tax record.
     * Only CLEARANCE_OFFICERs and ADMINs can issue clearance.
     * Certificate is valid for 1 year from issue date.
     */
    public TaxClearance issueClearance(Tax tax, Taxpayer clearanceOfficer) {

        if (tax.getStatus() != Tax.Status.PAID)
            throw new IllegalStateException(
                    "Clearance can only be issued for PAID taxes. Current: " + tax.getStatus());

        if (clearanceOfficer.getRole() != Taxpayer.Role.CLEARANCE_OFFICER
                && clearanceOfficer.getRole() != Taxpayer.Role.ADMIN)
            throw new SecurityException("Only CLEARANCE_OFFICERs can issue clearance certificates");

        // Prevent duplicate clearance
        if (clearanceDAO.findByTaxId(tax.getId()) != null)
            throw new IllegalStateException("Clearance certificate already issued for this tax record");

        String certNumber = "URA-CLR-" + LocalDateTime.now().getYear()
                + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        TaxClearance clearance = new TaxClearance();
        clearance.setTax(tax);
        clearance.setIssuedBy(clearanceOfficer);
        clearance.setIssuedAt(LocalDateTime.now());
        clearance.setCertificateNumber(certNumber);
        clearance.setValidUntil(LocalDateTime.now().plusYears(1));
        clearanceDAO.save(clearance);

        // Audit
        AuditLog log = new AuditLog();
        log.setAction("CLEARANCE_ISSUED");
        log.setEntityType("TaxClearance");
        log.setEntityId(clearance.getId());
        log.setPerformedBy(clearanceOfficer);
        log.setDescription("Certificate " + certNumber + " issued. Valid until " + clearance.getValidUntil());
        auditLogDAO.save(log);

        return clearance;
    }

    /**
     * Verifies a clearance certificate by its number.
     */
    public TaxClearance verifyCertificate(String certNumber) {
        TaxClearance clearance = clearanceDAO.findByCertificateNumber(certNumber);
        if (clearance == null)
            throw new IllegalArgumentException("Certificate not found: " + certNumber);
        if (clearance.getValidUntil().isBefore(LocalDateTime.now()))
            throw new IllegalStateException("Certificate has expired on: " + clearance.getValidUntil());
        return clearance;
    }
}
