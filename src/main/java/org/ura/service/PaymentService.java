package org.ura.service;

import org.ura.dao.PaymentDAO;
import org.ura.dao.TaxDAO;
import org.ura.dao.AuditLogDAO;
import org.ura.entity.AuditLog;
import org.ura.entity.Payment;
import org.ura.entity.Tax;
import org.ura.entity.Taxpayer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentService {

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final TaxDAO taxDAO = new TaxDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    /**
     * Process a payment for a given tax record.
     * Supports partial and full payments.
     * Automatically updates tax status to PARTIALLY_PAID or PAID.
     */
    public Payment processPayment(Tax tax, Taxpayer payer,
                                  BigDecimal amount, Payment.PaymentMethod method) {

        if (tax == null) throw new IllegalArgumentException("Tax record is required");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Payment amount must be positive");

        if (tax.getStatus() == Tax.Status.PAID)
            throw new IllegalStateException("Tax is already fully paid");

        if (tax.getStatus() != Tax.Status.ASSESSED
                && tax.getStatus() != Tax.Status.PARTIALLY_PAID)
            throw new IllegalStateException("Tax must be ASSESSED before payment. Current status: " + tax.getStatus());

        BigDecimal outstanding = tax.getAssessedAmount().subtract(tax.getAmountPaid());
        if (amount.compareTo(outstanding) > 0)
            throw new IllegalArgumentException(
                    "Payment amount exceeds outstanding balance of " + outstanding);

        // Build payment record
        Payment payment = new Payment();
        payment.setTax(tax);
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setPaymentReference("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setReceiptNumber("RCP-" + System.currentTimeMillis());
        payment.setPaymentDate(LocalDateTime.now());
        paymentDAO.save(payment);

        // Update tax balance and status
        BigDecimal newAmountPaid = tax.getAmountPaid().add(amount);
        tax.setAmountPaid(newAmountPaid);
        if (newAmountPaid.compareTo(tax.getAssessedAmount()) >= 0) {
            tax.setStatus(Tax.Status.PAID);
        } else {
            tax.setStatus(Tax.Status.PARTIALLY_PAID);
        }
        taxDAO.update(tax);

        // Audit
        AuditLog log = new AuditLog();
        log.setAction("PAYMENT_PROCESSED");
        log.setEntityType("Payment");
        log.setEntityId(payment.getId());
        log.setPerformedBy(payer);
        log.setDescription("Paid " + amount + " via " + method + ". Receipt: " + payment.getReceiptNumber());
        auditLogDAO.save(log);

        return payment;
    }
}
