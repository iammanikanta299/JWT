package com.healthcare.Controllers;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.Services.IJweRequest;
import com.healthcare.Services.PublicKeyLoader;
import com.healthcare.Services.PrivateKeyLoader;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.shaded.json.parser.ParseException;


@RestController
public class JweController {

	private final IJweRequest jweRequest;
	private final RSAPublicKey rsaPublicKey;
	private final RSAPrivateKey rsaPrivateKey;
	


	@Autowired
	public JweController(IJweRequest jweRequest, @Value("${publickey}") String publicKeyString,
			@Value("${privatekey}") String privateKeyString) {
	    this.jweRequest = jweRequest;
	    this.rsaPublicKey = loadPublicKeyFromPropertiesString(publicKeyString);
	    this.rsaPrivateKey = loadPrivateKeyFromPropertiesString(privateKeyString);
	}

	
	
	@PostMapping("/create")
	public ResponseEntity<String> encryptData(@RequestBody Map<String, Object> requestPayload) throws ParseException {
		try {
			// Headers for JWE (if needed)
			Map<String, Object> headers = null;
			
			System.out.println("The Publickey is : \n"+rsaPublicKey);
			System.out.println("The Privatekey is : \n"+rsaPrivateKey);

			// Call the encryptRequest method from the IJweRequest interface
			Map<String, String> encryptedObject = jweRequest.encryptRequest(rsaPublicKey, headers, requestPayload);

			Map<String, Object> decryptedObject = jweRequest.decryptRequest(rsaPrivateKey, encryptedObject);

			// Convert the encrypted object map to a JSON string using Jackson ObjectMapper
			String encryptedResponse = mapToJsonString(encryptedObject);
			
			
			System.out.println("ENCRYPTED VALUE"+encryptedResponse);
			System.out.println("DECRYPTED VALUE"+decryptedObject.toString());


			return ResponseEntity.ok(encryptedResponse);
		} catch (JOSEException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Encryption failed.");
		}
	}

	private String mapToJsonString(Map<?, ?> dataMap) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Helper method to load the RSA public key from properties string using
	// PublicKeyLoader class
	private RSAPublicKey loadPublicKeyFromPropertiesString(String publicKeyString) {
		try {
			return PublicKeyLoader.loadPublicKeyFromPropertiesString(publicKeyString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private RSAPrivateKey loadPrivateKeyFromPropertiesString(String privateKeyString) {
		try {
			return PrivateKeyLoader.loadPrivateKeyFromPropertiesString(privateKeyString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
