package util;

import java.math.BigInteger;

public class Util {
	/*public static String decToHex(int dec) {
		return Integer.toHexString(dec);
	}*/
	
	public static int hexToDec(String hex) {
		return Integer.parseInt(hex, 16);
	}
	
	public static String hexToBin(String hex) {
		return new BigInteger(hex, 16).toString(2);
	}
	
	public static int binToDec(String bin) {
		return Integer.parseInt(bin, 2);
	}
	
	public static String decToBin(int dec) {
		//return Integer.toString(dec, 2);
		return Integer.toBinaryString(dec);
	}
	
	public static String decToBinOct(int dec) {
		return Util.padLeft(decToBin(dec),32,"0").substring(24, 32);
	}
	
	public static String binToHex(String bin) {
		//return Long.toHexString(Long.parseLong(bin,2));
		return new BigInteger(bin, 2).toString(16);
	}
	
	public static String hexToAscii(String hex) {
		int n = hex.length();
		StringBuilder sb = new StringBuilder(n / 2);
		for (int i = 0; i < n; i += 2) {
			char a = hex.charAt(i);
			char b = hex.charAt(i + 1);
			sb.append((char) ((hexToInt(a) << 4) | hexToInt(b)));
		}
		return sb.toString();
	}

	public static int hexToInt(char ch) {
		if ('a' <= ch && ch <= 'f') { return ch - 'a' + 10; }
		if ('A' <= ch && ch <= 'F') { return ch - 'A' + 10; }
		if ('0' <= ch && ch <= '9') { return ch - '0'; }
		throw new IllegalArgumentException(String.valueOf(ch));
	}
	
	public static String padLeft(String s, int length, String car) {
		if(length <= s.length()) {
			return s;
		} else {
			return String.format("%" + (length - s.length()) + "s", "") 
					   .replace(" ", car.substring(0))
					   + s;	
		}
	}
	
	public static String padRight(String s, int length, String car) {
		if(length <= s.length()) {
			return s;
		} else {
			return s + 
					   String.format("%" + (length - s.length()) + "s", "")
					   .replace(" ", car.substring(0));	
		}
	}
	
	public static String padRight(String s, int length) {
		return String.format("%1$-" + length + "s", s);  
	}

	public static String padLeft(String s, int length) {
		return String.format("%1$" + length + "s", s);  
	}
}
