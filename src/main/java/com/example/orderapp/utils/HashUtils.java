package com.example.orderapp.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashUtils {

	private static final BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder();

	// ======================= BASIC HASH =======================

	public static String md5(String data) {
		return hash(data, "MD5");
	}

	public static String sha1(String data) {
		return hash(data, "SHA-1");
	}

	public static String sha256(String data) {
		return hash(data, "SHA-256");
	}

	public static String sha384(String data) {
		return hash(data, "SHA-384");
	}

	public static String sha512(String data) {
		return hash(data, "SHA-512");
	}

	private static String hash(String data, String algorithm) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			byte[] bytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(bytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Hash algorithm not found: " + algorithm, e);
		}
	}

	// ======================= HMAC =======================

	public static String hmacSha256(String data, String secret) {
		return hmac(data, secret, "HmacSHA256");
	}

	public static String hmacSha512(String data, String secret) {
		return hmac(data, secret, "HmacSHA512");
	}

	private static String hmac(String data, String secret, String algorithm) {
		try {
			Mac mac = Mac.getInstance(algorithm);
			SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), algorithm);
			mac.init(key);
			byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(rawHmac);
		} catch (Exception e) {
			throw new RuntimeException("HMAC error", e);
		}
	}

	// ======================= BCRYPT (PASSWORD) =======================

	public static String bcryptHash(String password) {
		return BCRYPT.encode(password);
	}

	public static boolean bcryptVerify(String rawPassword, String hashedPassword) {
		return BCRYPT.matches(rawPassword, hashedPassword);
	}

	// ======================= BASE64 =======================

	public static String base64Encode(String data) {
		return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
	}

	public static String base64Decode(String base64) {
		return new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
	}

	// ======================= UTIL =======================

	private static String bytesToHex(byte[] bytes) {
		StringBuilder hex = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			hex.append(String.format("%02x", b));
		}
		return hex.toString();
	}
}
