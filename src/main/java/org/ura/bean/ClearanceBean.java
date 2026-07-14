package org.ura.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.ura.dao.TaxDAO;
import org.ura.entity.Tax;
import org.ura.entity.TaxClearance;
import org.ura.service.ClearanceService;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ClearanceBean implements Serializable {

    private List<Tax> paidTaxes;
    private Tax selectedTax;
    private TaxClearance issuedClearance;
    private String certNumberToVerify;
    private TaxClearance verifiedClearance;

    @Inject
    private SessionBean sessionBean;

    private final TaxDAO taxDAO = new TaxDAO();
    private final ClearanceService clearanceService = new ClearanceService();

    @PostConstruct
    public void init() {
        paidTaxes = taxDAO.findByStatus(Tax.Status.PAID);
    }

    public void selectTax(Tax tax) {
        this.selectedTax = tax;
        this.issuedClearance = null;
    }

    public void issueClearance() {
        try {
            issuedClearance = clearanceService.issueClearance(selectedTax, sessionBean.getLoggedInUser());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Issued",
                            "Certificate: " + issuedClearance.getCertificateNumber()));
            init();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void verifyCertificate() {
        try {
            verifiedClearance = clearanceService.verifyCertificate(certNumberToVerify);
        } catch (Exception e) {
            verifiedClearance = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid", e.getMessage()));
        }
    }

    public List<Tax> getPaidTaxes() { return paidTaxes; }
    public Tax getSelectedTax() { return selectedTax; }
    public void setSelectedTax(Tax selectedTax) { this.selectedTax = selectedTax; }
    public TaxClearance getIssuedClearance() { return issuedClearance; }
    public String getCertNumberToVerify() { return certNumberToVerify; }
    public void setCertNumberToVerify(String certNumberToVerify) { this.certNumberToVerify = certNumberToVerify; }
    public TaxClearance getVerifiedClearance() { return verifiedClearance; }
}
