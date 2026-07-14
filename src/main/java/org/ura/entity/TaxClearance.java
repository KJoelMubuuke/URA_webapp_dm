package org.ura.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tax_clearance")
public class TaxClearance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "tax_id", nullable = false, unique = true)
    private Tax tax;

    @ManyToOne
    @JoinColumn(name = "issued_by", nullable = false)
    private Taxpayer issuedBy;

    @Column(nullable = false)
    private LocalDateTime issuedAt = LocalDateTime.now();

    @Column(unique = true, nullable = false)
    private String certificateNumber;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    // Getters and Setters
    public Long getId() { return id; }
    public Tax getTax() { return tax; }
    public void setTax(Tax tax) { this.tax = tax; }
    public Taxpayer getIssuedBy() { return issuedBy; }
    public void setIssuedBy(Taxpayer issuedBy) { this.issuedBy = issuedBy; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
    public String getCertificateNumber() { return certificateNumber; }
    public void setCertificateNumber(String certificateNumber) { this.certificateNumber = certificateNumber; }
    public LocalDateTime getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }
}
