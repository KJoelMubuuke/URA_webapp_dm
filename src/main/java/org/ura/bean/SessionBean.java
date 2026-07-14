package org.ura.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.ura.entity.Taxpayer;
import java.io.Serializable;

@Named
@SessionScoped
public class SessionBean implements Serializable {

    private Taxpayer loggedInUser;

    public Taxpayer getLoggedInUser() { return loggedInUser; }
    public void setLoggedInUser(Taxpayer loggedInUser) { this.loggedInUser = loggedInUser; }

    public boolean isLoggedIn()          { return loggedInUser != null; }
    public boolean isTaxpayer()          { return loggedInUser != null && loggedInUser.getRole() == Taxpayer.Role.TAXPAYER; }
    public boolean isOfficer()           { return loggedInUser != null && loggedInUser.getRole() == Taxpayer.Role.TAX_OFFICER; }
    public boolean isClearanceOfficer()  { return loggedInUser != null && loggedInUser.getRole() == Taxpayer.Role.CLEARANCE_OFFICER; }
    public boolean isAdmin()             { return loggedInUser != null && loggedInUser.getRole() == Taxpayer.Role.ADMIN; }

    public String getFullName() {
        return loggedInUser == null ? "" : loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }
}
