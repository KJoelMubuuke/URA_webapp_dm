package org.ura.service;

import org.ura.dao.TaxDAO;
import org.ura.dao.AuditLogDAO;
import org.ura.entity.AuditLog;
import org.ura.entity.Tax;
import org.ura.entity.Taxpayer;

import java.math.BigDecimal;

public class AssessmentService {

    private final TaxDAO taxDAO = new TaxDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    /**
     * Tax Officer reviews a declaration and sets the assessed amount.
     * Moves status from DECLARED → ASSESSED.
     */
    public Tax assess(Tax tax, Taxpayer officer, BigDecimal assessedAmount) {

        if (tax.getStatus() != Tax.Status.DECLARED)
            throw new IllegalStateException("Only DECLARED taxes can be assessed. Current: " + tax.getStatus());

        if (assessedAmount == null || assessedAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Assessed amount must be positive");

        if (officer.getRole() != Taxpayer.Role.TAX_OFFICER && officer.getRole() != Taxpayer.Role.ADMIN)
            throw new SecurityException("Only TAX_OFFICERs can assess tax declarations");

        tax.setAssessedAmount(assessedAmount);
        tax.setStatus(Tax.Status.ASSESSED);
        tax.setReviewedBy(officer);
        taxDAO.update(tax);

        // Audit
        AuditLog log = new AuditLog();
        log.setAction("TAX_ASSESSED");
        log.setEntityType("Tax");
        log.setEntityId(tax.getId());
        log.setPerformedBy(officer);
        log.setDescription("Assessed amount set to " + assessedAmount + " by " + officer.getFirstName());
        auditLogDAO.save(log);

        return tax;
    }

    /**
     * Tax Officer rejects a declaration with a reason.
     * Moves status from DECLARED → REJECTED.
     */
    public Tax reject(Tax tax, Taxpayer officer, String reason) {

        if (tax.getStatus() != Tax.Status.DECLARED)
            throw new IllegalStateException("Only DECLARED taxes can be rejected. Current: " + tax.getStatus());

        if (reason == null || reason.isBlank())
            throw new IllegalArgumentException("Rejection reason is required");

        if (officer.getRole() != Taxpayer.Role.TAX_OFFICER && officer.getRole() != Taxpayer.Role.ADMIN)
            throw new SecurityException("Only TAX_OFFICERs can reject declarations");

        tax.setStatus(Tax.Status.REJECTED);
        tax.setRejectionReason(reason);
        tax.setReviewedBy(officer);
        taxDAO.update(tax);

        // Audit
        AuditLog log = new AuditLog();
        log.setAction("TAX_REJECTED");
        log.setEntityType("Tax");
        log.setEntityId(tax.getId());
        log.setPerformedBy(officer);
        log.setDescription("Rejected: " + reason);
        auditLogDAO.save(log);

        return tax;
    }
}
