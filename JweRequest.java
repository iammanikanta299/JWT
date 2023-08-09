package com.healthcare.Services;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSAEncrypter;

import com.nimbusds.jose.shaded.json.parser.ParseException;
import com.nimbusds.jose.util.Base64URL;

@Service
public class JweRequest implements IJweRequest {

	public static final JWEAlgorithm KEY_MANAGEMENT_ALGORITHM = JWEAlgorithm.RSA_OAEP_256;
	public static final EncryptionMethod CONTENT_ENCRYPTION_ALGORITHM = EncryptionMethod.A256GCM;

	public JweRequest() {
	}

	@Override
	public Map<String, String> encryptRequest(RSAPublicKey rsaPublicKey, Map<String, Object> headers,
			Map<String, Object> payload) throws JOSEException {
		Map<String, String> encryptedObject = new HashMap<>();
		JWEHeader jweHeader = new JWEHeader.Builder(KEY_MANAGEMENT_ALGORITHM, CONTENT_ENCRYPTION_ALGORITHM)
				.customParams(headers).build();
		Payload jwePayload = new Payload(payload);

		JWEObject jweObject = new JWEObject(jweHeader, jwePayload);
		RSAEncrypter rsaEncrypter = new RSAEncrypter(rsaPublicKey);
		jweObject.encrypt(rsaEncrypter);

		String serializedString = jweObject.serialize();

		String[] jweParts = serializedString.split("\\.");
		encryptedObject.put("protected", jweParts[0]);
		encryptedObject.put("encrypted_key", jweParts[1]);
		encryptedObject.put("iv", jweParts[2]);
		encryptedObject.put("ciphertext", jweParts[3]);
		encryptedObject.put("tag", jweParts[4]);
		return encryptedObject;
	}

	@Override
	public Map<String, Object> decryptRequest(RSAPrivateKey rsaPrivateKey, Map<String, String> encryptedObject)
			throws JOSEException {
		Map<String, Object> payload = null;

		JWEObject jweObject = null;
		try {
			jweObject = new JWEObject(new Base64URL(encryptedObject.get("protected")),
					new Base64URL(encryptedObject.get("encrypted_key")), new Base64URL(encryptedObject.get("iv")),
					new Base64URL(encryptedObject.get("ciphertext")), new Base64URL(encryptedObject.get("tag")));
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RSADecrypter rsaDecrypter = new RSADecrypter(rsaPrivateKey);
		jweObject.decrypt(rsaDecrypter);
		payload = new HashMap<>(jweObject.getPayload().toJSONObject());

		System.out.println("Payload: " + payload);
		return payload;
	}
}
