package com.linzx.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.linzx.utils.ByteUtils;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityUtil {
	public static final String ALG_MD5 = "MD5";
	public static final String ALG_SHA_1 = "SHA-1";

	private static final Logger log = LoggerFactory.getLogger(SecurityUtil.class);

	/**
	 * 用algorithm加密缺省编码的origin成字符串
	 *
	 * @param origin
	 * @param algorithm
	 * @return
	 * @author fengdg
	 */
	public static String encode(String origin, String algorithm) {
		return encode(origin, null, algorithm);
	}

	/**
	 * 用algorithm加密charsetname编码的origin成字符串
	 *
	 * @param origin
	 * @param charsetName
	 * @param algorithm
	 * @return
	 * @author fengdg
	 */
	public static String encode(String origin, String charsetName, String algorithm) {
		byte[] digest = encodeToBytes(origin, charsetName, algorithm);
		return byteToHex(digest);
	}

	/**
	 * 用algorithm加密bytes
	 *
	 * @param bytes
	 * @param algorithm
	 * @return
	 * @author fengdg
	 */
	public static String encode(byte[] bytes, String algorithm) {
		byte[] digest = encodeToBytes(bytes, algorithm);
		return byteToHex(digest);
	}

	/**
	 * 用algorithm加密缺省编码的origin成byte数组
	 *
	 * @param origin
	 * @param algorithm
	 * @return
	 * @author fengdg
	 */
	public static byte[] encodeToBytes(String origin, String algorithm) {
		return encodeToBytes(origin, null, algorithm);
	}

	/**
	 * 用algorithm加密charsetname编码的origin成byte数组
	 *
	 * @param origin      待加密字符串
	 * @param charsetName 字符集
	 * @param algorithm   加密算法
	 * @return
	 * @author fengdg
	 */
	public static byte[] encodeToBytes(String origin, String charsetName, String algorithm) {
		try {
			byte[] bytes = StringUtils.isEmpty(charsetName) ? origin.getBytes() : origin.getBytes(charsetName);
			return encodeToBytes(bytes, algorithm);
		} catch (UnsupportedEncodingException e) {
			log.error("SecurityUtil.encodeToBytes:", e);
		}
		return null;
	}

	/**
	 * 用algorithm加密bytes
	 *
	 * @param bytes
	 * @param algorithm
	 * @return
	 * @author fengdg
	 */
	public static byte[] encodeToBytes(byte[] bytes, String algorithm) {
		try {
			MessageDigest crypt = MessageDigest.getInstance(algorithm);
			crypt.reset();
			return crypt.digest(bytes);
		} catch (NoSuchAlgorithmException e) {
			log.error("SecurityUtil.encodeToBytes:", e);
		}
		return null;
	}

	public static String byteToHex(final byte[] hash) {
		return ByteUtils.bytesToHexStr(hash);
	}
}
