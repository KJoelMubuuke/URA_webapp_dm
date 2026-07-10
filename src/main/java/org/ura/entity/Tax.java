package org.ura.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tax")
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "taxpayer_id", nullable = false)
    private Taxpayer taxpayer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaxType taxType;

    @Column(nullable = false)
    private String period;

    @Column(nullable = false)
    private BigDecimal declaredAmount;

    private BigDecimal assessedAmount;

    private BigDecimal amountPaid = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.DRAFT;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private Taxpayer reviewedBy;

    private String rejectionReason;

    public enum TaxType { INCOME_TAX, VAT, RENTAL_TAX }
    public enum Status { DRAFT, DECLARED, ASSESSED, REJECTED, PARTIALLY_PAID, PAID }

    // getters and setters — Alt+Insert in IntelliJ, select all fields
    public Long getId() {
        return id; }
    public Taxpayer getTaxpayer() {
        return taxpayer; }
    public void setTaxpayer(Taxpayer taxpayer) {
        this.taxpayer = taxpayer; }
    public TaxType getTaxType() {
        return taxType; }
    public void setTaxType(TaxType taxType) { this.taxType = taxType; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public BigDecimal getDeclaredAmount()
    { return declaredAmount; }
    public void setDeclaredAmount(BigDecimal declaredAmount) { this.declaredAmount = declaredAmount; }
    public BigDecimal getAssessedAmount()
    { return assessedAmount; }

    public void setAssessedAmount(BigDecimal assessedAmount) { this.assessedAmount = assessedAmount; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Taxpayer getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Taxpayer reviewedBy) { this.reviewedBy = reviewedBy; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}