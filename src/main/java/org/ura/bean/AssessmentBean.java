package org.ura.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.ura.dao.TaxDAO;
import org.ura.entity.Tax;
import org.ura.service.AssessmentService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Named
@ViewScoped
public class AssessmentBean implements Serializable {

    private List<Tax> pendingTaxes;
    private Tax selectedTax;
    private BigDecimal assessedAmount;
    private String rejectionReason;

    @Inject
    private SessionBean sessionBean;

    private final TaxDAO taxDAO = new TaxDAO();
    private final AssessmentService assessmentService = new AssessmentService();

    @PostConstruct
    public void init() {
        pendingTaxes = taxDAO.findByStatus(Tax.Status.DECLARED);
    }

    public void selectTax(Tax tax) {
        this.selectedTax = tax;
        this.assessedAmount = tax.getDeclaredAmount();
        this.rejectionReason = null;
    }

    public void assess() {
        try {
            assessmentService.assess(selectedTax, sessionBean.getLoggedInUser(), assessedAmount);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Assessed", "Tax record assessed successfully."));
            selectedTax = null;
            init();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void reject() {
        try {
            assessmentService.reject(selectedTax, sessionBean.getLoggedInUser(), rejectionReason);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Rejected", "Declaration rejected."));
            selectedTax = null;
            init();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public List<Tax> getPendingTaxes() { return pendingTaxes; }
    public Tax getSelectedTax() { return selectedTax; }
    public void setSelectedTax(Tax selectedTax) { this.selectedTax = selectedTax; }
    public BigDecimal getAssessedAmount() { return assessedAmount; }
    public void setAssessedAmount(BigDecimal assessedAmount) { this.assessedAmount = assessedAmount; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
