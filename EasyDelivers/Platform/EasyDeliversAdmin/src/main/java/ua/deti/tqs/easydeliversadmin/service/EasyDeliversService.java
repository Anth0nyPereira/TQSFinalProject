package ua.deti.tqs.easydeliversadmin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.deti.tqs.easydeliversadmin.entities.Admin;
import ua.deti.tqs.easydeliversadmin.entities.Rider;
import ua.deti.tqs.easydeliversadmin.repository.AdminRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class EasyDeliversService {

    @Autowired
    public AdminRepository adminRepository;


    public Admin getAdminByEmail(String email) throws AdminNotFoundException {
        Admin user = adminRepository.findAdminByEmail(email);

        if (user == null) {
            throw new AdminNotFoundException("Admin not found.");
        }

        return user;
    }

    public boolean authenticateRider(String email, String password) {
        return true;
    }

    public Rider getRider(String email) {
        return null;
    }

    public Rider createRider(String name, String email, String password, String telephone, String transportation) {
        return null;
    }

    public String createDelivery(int store, String client_telephone, String start, String destination) {
        return "Delivery accepted";
    }


    private class AdminNotFoundException extends Throwable {
        public AdminNotFoundException(String s) {
            final Logger log = LoggerFactory.getLogger(EasyDeliversService.class);
            log.info(s);
        }
    }
}
