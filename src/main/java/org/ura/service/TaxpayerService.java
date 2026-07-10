package org.ura.service;

import org.ura.dao.TaxpayerDAO;
import org.ura.entity.Taxpayer;
import org.mindrot.jbcrypt.BCrypt;

public class TaxpayerService {

    private final TaxpayerDAO taxpayerDAO = new TaxpayerDAO();

    public Taxpayer register(String tin, String firstName, String lastName,
                             String email, String phone, String rawPassword) {

        if (taxpayerDAO.existsByTin(tin))
            throw new IllegalArgumentException("TIN already registered");

        if (taxpayerDAO.existsByEmail(email))
            throw new IllegalArgumentException("Email already registered");

        if (rawPassword == null || rawPassword.length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters");

        Taxpayer taxpayer = new Taxpayer();
        taxpayer.setTin(tin);
        taxpayer.setFirstName(firstName);
        taxpayer.setLastName(lastName);
        taxpayer.setEmail(email);
        taxpayer.setPhone(phone);
        taxpayer.setPasswordHash(BCrypt.hashpw(rawPassword, BCrypt.gensalt()));

        taxpayerDAO.save(taxpayer);
        return taxpayer;
    }
}