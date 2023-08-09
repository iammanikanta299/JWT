package com.healthcare.Services;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PublicKeyLoader {

	public static RSAPublicKey loadPublicKeyFromPropertiesString(String publicKeyString)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] publicKeyBytes = extractPublicKeyBytes(publicKeyString);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return (RSAPublicKey) keyFactory.generatePublic(spec);
	}

	private static byte[] extractPublicKeyBytes(String publicKeyString) {
		// Remove the header, footer, and newlines if present
		String publicKeyPEMTrimmed = publicKeyString.replaceAll("-----BEGIN PUBLIC KEY-----", "")
				.replaceAll("-----END PUBLIC KEY-----", "").replaceAll("\n", "").trim();

		// Convert from Base64 to bytes
		return Base64.getDecoder().decode(publicKeyPEMTrimmed);
	}
}
