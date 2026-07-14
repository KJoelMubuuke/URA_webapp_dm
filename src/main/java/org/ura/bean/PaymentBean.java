package org.ura.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.ura.dao.TaxDAO;
import org.ura.entity.Payment;
import org.ura.entity.Tax;
import org.ura.service.PaymentService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class PaymentBean implements Serializable {

    private List<Tax> payableTaxes;
    private Long selectedTaxId;
    private BigDecimal amount;
    private Payment.PaymentMethod method;

    @Inject
    private SessionBean sessionBean;

    private final TaxDAO taxDAO = new TaxDAO();
    private final PaymentService paymentService = new PaymentService();

    @PostConstruct
    public void init() {
        payableTaxes = taxDAO.findByTaxpayerId(sessionBean.getLoggedInUser().getId())
                .stream()
                .filter(t -> t.getStatus() == Tax.Status.ASSESSED || t.getStatus() == Tax.Status.PARTIALLY_PAID)
                .collect(Collectors.toList());
    }

    public String pay() {
        try {
            Tax tax = taxDAO.findById(selectedTaxId);
            paymentService.processPayment(tax, sessionBean.getLoggedInUser(), amount, method);
            return "/taxpayer/dashboard.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Payment Failed", e.getMessage()));
            return null;
        }
    }

    public List<Payment.PaymentMethod> getPaymentMethods() { return List.of(Payment.PaymentMethod.values()); }
    public List<Tax> getPayableTaxes() { return payableTaxes; }
    public Long getSelectedTaxId() { return selectedTaxId; }
    public void setSelectedTaxId(Long selectedTaxId) { this.selectedTaxId = selectedTaxId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Payment.PaymentMethod getMethod() { return method; }
    public void setMethod(Payment.PaymentMethod method) { this.method = method; }
}
