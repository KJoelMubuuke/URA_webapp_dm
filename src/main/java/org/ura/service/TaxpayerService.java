package org.ura.service;

import org.ura.dao.TaxpayerDAO;
import org.ura.dao.AuditLogDAO;
import org.ura.entity.AuditLog;
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

        // Audit
        AuditLog log = new AuditLog();
        log.setAction("TAXPAYER_REGISTERED");
        log.setEntityType("Taxpayer");
        log.setPerformedBy(taxpayer);
        log.setDescription("New taxpayer registered: " + tin);
        new AuditLogDAO().save(log);

        return taxpayer;
    }

    /**
     * Authenticates a taxpayer by email and password.
     */
    public Taxpayer login(String email, String rawPassword) {
        Taxpayer taxpayer = taxpayerDAO.findByEmail(email);

        if (taxpayer == null)
            throw new IllegalArgumentException("No account found with that email");

        if (taxpayer.getStatus() == Taxpayer.Status.SUSPENDED)
            throw new IllegalStateException("Account is suspended. Contact URA.");

        if (taxpayer.getStatus() == Taxpayer.Status.DEACTIVATED)
            throw new IllegalStateException("Account has been deactivated.");

        if (!BCrypt.checkpw(rawPassword, taxpayer.getPasswordHash()))
            throw new IllegalArgumentException("Invalid password");

        return taxpayer;
    }
}