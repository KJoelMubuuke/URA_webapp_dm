package org.ura.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;        // e.g. REGISTERED, DECLARED, ASSESSED, PAID, CLEARED

    @Column(nullable = false)
    private String entityType;    // e.g. Taxpayer, Tax, Payment

    private Long entityId;        // PK of affected record

    @ManyToOne
    @JoinColumn(name = "performed_by")
    private Taxpayer performedBy;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    // Getters and Setters
    public Long getId() { return id; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }
    public Taxpayer getPerformedBy() { return performedBy; }
    public void setPerformedBy(Taxpayer performedBy) { this.performedBy = performedBy; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
