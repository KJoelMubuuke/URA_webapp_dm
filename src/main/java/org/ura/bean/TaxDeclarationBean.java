package org.ura.bean;

import jakarta.enterprise.context.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.ura.entity.Tax;
import org.ura.service.TaxService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class TaxDeclarationBean implements Serializable {

    private Tax.TaxType taxType;
    private String period;
    private BigDecimal declaredAmount;

    @Inject
    private SessionBean sessionBean;

    private final TaxService taxService = new TaxService();

    public String declare() {
        try {
            taxService.declare(sessionBean.getLoggedInUser(), taxType, period, declaredAmount);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Submitted", "Tax declaration submitted for review."));
            return "/taxpayer/dashboard.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed", e.getMessage()));
            return null;
        }
    }

    public List<Tax.TaxType> getTaxTypes() { return Arrays.asList(Tax.TaxType.values()); }

    public Tax.TaxType getTaxType() { return taxType; }
    public void setTaxType(Tax.TaxType taxType) { this.taxType = taxType; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public BigDecimal getDeclaredAmount() { return declaredAmount; }
    public void setDeclaredAmount(BigDecimal declaredAmount) { this.declaredAmount = declaredAmount; }
}
