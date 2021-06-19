package tqs.proudpapers.component;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author Alexandra & Anthony
 */
@Component
public class PasswordEncryption {
    public String encrypt(String plain_pass) throws Exception {
        try {
            // getInstance() method is called with algorithm SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] encodedhash = md.digest(plain_pass.getBytes(StandardCharsets.UTF_8));

            // byte to hex converter to get the hashed value in hexadecimal
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (int i = 0; i < encodedhash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            String hashText = hexString.toString();

            // return the HashText
            return hashText;
        }

        // For specifying wrong message digest algorithms
        catch (Exception e) {
            throw new Exception(e);
        }
    }
}
