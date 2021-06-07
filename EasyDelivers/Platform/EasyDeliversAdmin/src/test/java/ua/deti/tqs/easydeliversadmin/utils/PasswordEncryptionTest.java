package ua.deti.tqs.easydeliversadmin.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordEncryptionTest {

    private PasswordEncryption encryptor;

    @Test
    public void whenGivenPlainText_ThenCheckItsEncryption() {
        String plainText = "HelloWorld!";
        String expectedPasswd = "a653423925f386225953e7f9dcd961074ba7498ce45ea64400c8c332";
        assertEquals(encryptor.encrypt(plainText), expectedPasswd);
    }

    @Test
    public void whenGivenTwoEqualPlainTexts_ThenCheckIfHashIsTheSame() {
        String plainText1 = "HelloWorld!";
        String plainText2 = "HelloWorld!";

        assertEquals(encryptor.encrypt(plainText1), encryptor.encrypt(plainText2));
    }

    @Test
    public void whenGivenTwoDifferentPlainTexts_ThenCheckIfHashIsNotTheSame() {
        String plainText1 = "HelloWorld!";
        String plainText2 = "HelloWorld;";
        assertThat(encryptor.encrypt(plainText1).isNotEquals(encryptor.encrypt(plainText2));
    }
}
