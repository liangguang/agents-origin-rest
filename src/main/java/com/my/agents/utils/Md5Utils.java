package com.my.agents.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
	private MessageDigest messageDigest = null;

	public Md5Utils() throws NoSuchAlgorithmException {
		if (messageDigest == null) {
			messageDigest = MessageDigest.getInstance("MD5");
		}
	}

	public String getMd5ByFile(File file) throws IOException {
		in = new FileInputStream(file);
		int length = -1;
		byte[] buffer = new byte[1048576];
		while ((length = in.read(buffer)) != -1) {
			messageDigest.update(buffer, 0, length);
		}
		return bufferToHex(messageDigest.digest());
	}

	private String bufferToHex(byte bytes[]) throws IOException {
		return toHex(bytes, 0, bytes.length);
	}

	private String toHex(byte bytes[], int m, int n) throws IOException {
		StringBuffer sb = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], sb);
		}
		in.close();
		return sb.toString();
	}

	protected char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private FileInputStream in;

	private void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
}
