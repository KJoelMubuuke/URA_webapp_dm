package org.ura.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.ura.dao.TaxDAO;
import org.ura.entity.Tax;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class TaxpayerDashboardBean implements Serializable {

    private List<Tax> myTaxes;

    @Inject
    private SessionBean sessionBean;

    private final TaxDAO taxDAO = new TaxDAO();

    @PostConstruct
    public void init() {
        myTaxes = taxDAO.findByTaxpayerId(sessionBean.getLoggedInUser().getId());
    }

    public List<Tax> getMyTaxes() { return myTaxes; }
}
