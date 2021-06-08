package ua.deti.tqs.easydeliversadmin.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AdminDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Logger log = Logger.getLogger(UserDetails.class.getName());
        log.info("Email: " + email);
        return new AdminDetails(email);
    }
}
