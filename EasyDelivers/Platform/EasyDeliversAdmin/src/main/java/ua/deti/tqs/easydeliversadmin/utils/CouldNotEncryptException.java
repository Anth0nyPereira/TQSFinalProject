package ua.deti.tqs.easydeliversadmin.utils;

import java.util.logging.Logger;

public class CouldNotEncryptException extends Throwable {
    public CouldNotEncryptException() {
        final Logger log = Logger.getLogger(CouldNotEncryptException.class.getName());
        log.info("Could not encrypt the inputted string");
    }
}