package com.barchart.feed.inst.provider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public final class FileHasher {
	
	private FileHasher() {
		
	}
	
	@SuppressWarnings("unused")
	public static String hash(final File file) throws Exception {
		
		final MessageDigest md = MessageDigest.getInstance("MD5");
		final FileInputStream fis = new FileInputStream(file);
	    final BufferedInputStream bis = new BufferedInputStream(fis);
	    final DigestInputStream dis = new DigestInputStream(bis, md);
	    
		int ch;
	    while ((ch = dis.read()) != -1) {}
	    
	    final StringBuffer hexString = new StringBuffer();
	    byte digest[] = md.digest();
	    int digestLength = digest.length;
	    for (int i = 0; i < digestLength; i++) {
	    	hexString.append(hexDigit(digest[i]));
	    }
	    
	    return hexString.toString();
	}

	static private String hexDigit(byte x) {
	    StringBuffer sb = new StringBuffer();
	    char c;
	    // First nibble
	    c = (char) ((x >> 4) & 0xf);
	    if (c > 9) {
	      c = (char) ((c - 10) + 'a');
	    } else {
	      c = (char) (c + '0');
	    }
	    sb.append(c);
	    // Second nibble
	    c = (char) (x & 0xf);
	    if (c > 9) {
	      c = (char) ((c - 10) + 'a');
	    } else {
	      c = (char) (c + '0');
	    }
	    sb.append(c);
	    return sb.toString();
	}
	
}
