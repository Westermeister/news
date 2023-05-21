package com.westermeister.news;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64.Encoder;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * CryptoHelper provides useful utility functions related to cryptography.
 */
@Service
public class CryptoHelper {
	private Encoder encoder;

	/**
	 * Construct new instance with injected dependencies.
	 *
	 * @param encoder  should be a base64url encoder without any padding
	 */
	public CryptoHelper(Encoder encoder) {
		this.encoder = encoder;
	}

	/**
	 * Generates random bytes using a CSPRNG.
	 *
	 * @param numBytes  number of bytes to generate
	 * @return          bytes encoded as base64url w/o padding
	 */
	public String generateRandomBytes(int numBytes) {
		if (numBytes <= 0) {
			throw new IllegalArgumentException("`numBytes` must be >= 1");
		}
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		String result = this.encoder.encodeToString(bytes);
		return result;
	}

	/**
	 * Hashes a string with SHA3-512.
	 *
	 * @param input  string to hash
	 * @return       digest encoded as base64url w/o padding
	 */
	public String hash(String input) {
		try {
			MessageDigest hashFunction = MessageDigest.getInstance("SHA3-512");
			byte[] digest = hashFunction.digest(input.getBytes(StandardCharsets.UTF_8));
			String result = this.encoder.encodeToString(digest);
			return result;
		} catch (NoSuchAlgorithmException e) {
			// Should NEVER happen; app needs to be run on a platform that supports Java 17+
			throw new RuntimeException("SHA3-512 is not supported on this platform");
		}
	}

	/**
	 * Verifies an input against an existing SHA3-512 hash.
	 *
	 * @param input  the input string to check
	 * @param hash   an existing SHA3-512 hash
	 * @return       true if match, false if not
	 */
	public boolean verifyHash(String input, String hash) {
		return this.hash(input).equals(hash);
	}

	/**
	 * Hashes a password with argon2id.
	 *
	 * @param password  plain text user password
	 * @return          resulting hash with params:
	 *                  salt length = 16 bytes, hash length = 32 bytes, parallelism = 1, memory cost = 1 << 14, iterations = 2
	 * @see             org.springframework.security.crypto.argon2.Argon2PasswordEncoder#defaultsForSpringSecurity_v5_8
	 *                  Default parameters for argon2id (Spring Security v5.8)
	 */
	public String passwordHash(String password) {
		PasswordEncoder passwordHash = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
		return passwordHash.encode(password);
	}

	/**
	 * Verifies a password against an existing argon2id hash.
	 *
	 * @param input  the input password to check
	 * @param hash   an existing argon2id hash
	 * @return       true if match, false if not
	 * @see          #passwordHash(String) argon2id params
	 */
	public boolean verifyPasswordHash(String input, String hash) {
		PasswordEncoder passwordHash = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
		return passwordHash.matches(input, hash);
	}
}
