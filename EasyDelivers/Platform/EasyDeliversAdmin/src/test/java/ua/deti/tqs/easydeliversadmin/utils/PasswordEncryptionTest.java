package ua.deti.tqs.easydeliversadmin.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordEncryptionTest {

    private PasswordEncryption encryptor;

    @Test
    public void whenGivenPlainText_ThenCheckItsEncryption() throws Exception {
        String plainText = "HelloWorld!";
        String expectedPasswd = "729e344a01e52c822bdfdec61e28d6eda02658d2e7d2b80a9b9029f41e212dde";
        assertEquals(encryptor.encrypt(plainText), expectedPasswd);
    }

    @Test
    public void whenGivenTwoEqualPlainTexts_ThenCheckIfHashIsTheSame() throws Exception {
        String plainText1 = "HelloWorld!";
        String plainText2 = "HelloWorld!";

        assertEquals(encryptor.encrypt(plainText1), encryptor.encrypt(plainText2));
    }

    @Test
    public void whenGivenTwoDifferentPlainTexts_ThenCheckIfHashIsNotTheSame() throws Exception {
        String plainText1 = "HelloWorld!";
        String plainText2 = "HelloWorld;";
        assertThat(encryptor.encrypt(plainText1)).isNotEqualTo(plainText2);
    }
}
