package org.ura.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.ura.entity.Taxpayer;
import org.ura.service.TaxpayerService;

@Named
@RequestScoped
public class LoginBean {

    private String email;
    private String password;

    @Inject
    private SessionBean sessionBean;

    private final TaxpayerService taxpayerService = new TaxpayerService();

    public String login() {
        try {
            Taxpayer taxpayer = taxpayerService.login(email, password);
            sessionBean.setLoggedInUser(taxpayer);
            return switch (taxpayer.getRole()) {
                case TAXPAYER        -> "/taxpayer/dashboard.xhtml?faces-redirect=true";
                case TAX_OFFICER     -> "/officer/dashboard.xhtml?faces-redirect=true";
                case CLEARANCE_OFFICER -> "/clearance/dashboard.xhtml?faces-redirect=true";
                case ADMIN           -> "/officer/dashboard.xhtml?faces-redirect=true";
            };
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", e.getMessage()));
            return null;
        }
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
