package org.ura.service;

import org.ura.dao.TaxDAO;
import org.ura.entity.Tax;
import org.ura.entity.Taxpayer;

import java.math.BigDecimal;

public class TaxService {

    private final TaxDAO taxDAO = new TaxDAO();

    // URA_UCD_003 — Tax Declaration
    public Tax declare(Taxpayer taxpayer, Tax.TaxType taxType, String period, BigDecimal declaredAmount) {

        if (declaredAmount == null || declaredAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Declared amount must be positive");

        if (period == null || period.isBlank())
            throw new IllegalArgumentException("Period is required");

        Tax tax = new Tax();
        tax.setTaxpayer(taxpayer);
        tax.setTaxType(taxType);
        tax.setPeriod(period);
        tax.setDeclaredAmount(declaredAmount);
        tax.setStatus(Tax.Status.DECLARED);

        taxDAO.save(tax);
        return tax;
    }
}