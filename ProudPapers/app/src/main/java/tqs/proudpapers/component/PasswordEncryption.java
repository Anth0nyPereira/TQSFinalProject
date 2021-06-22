package tqs.proudpapers.component;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Alexandra & Anthony
 */
@Component
public class PasswordEncryption {
    public String encrypt(String plainPass) throws NoSuchAlgorithmException {
            // getInstance() method is called with algorithm SHA-256
            var md = MessageDigest.getInstance("SHA-256");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] encodedhash = md.digest(plainPass.getBytes(StandardCharsets.UTF_8));

            // byte to hex converter to get the hashed value in hexadecimal
            var hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                var hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // return the HashText
            return hexString.toString();
    }
}
