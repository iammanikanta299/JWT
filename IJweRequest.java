package com.healthcare.Services;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.shaded.json.parser.ParseException;

public interface IJweRequest {
	Map<String, String> encryptRequest(RSAPublicKey rsaPublicKey, Map<String, Object> headers,
			Map<String, Object> payload) throws JOSEException;

	public Map<String, Object> decryptRequest(RSAPrivateKey rsaPrivateKey, Map<String, String> encryptedObject)
			throws ParseException, JOSEException;
}
