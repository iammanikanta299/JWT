package com.healthcare.Services;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class PrivateKeyLoader {

    public static RSAPrivateKey loadPrivateKeyFromPropertiesString(String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Remove the header and footer from the private key string
        String privateKeyContent = privateKeyString
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s+", ""); // Remove any whitespace

        // Base64 decode the private key content
        byte[] decodedPrivateKeyBytes = Base64.getDecoder().decode(privateKeyContent);

        // Convert the decoded bytes to PKCS8EncodedKeySpec
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedPrivateKeyBytes);

        // Get the RSA KeyFactory instance and generate the private key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    // Additional methods or logic can be added if needed
}
