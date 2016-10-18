package lib;

import java.util.HashMap;

import util.Util;
import bean.Raw;

public class HexDecoder {
	
	public static HashMap<Integer, Raw> load(String hexString) {
		
		int key = 0;
		HashMap<Integer, Raw> raw = new HashMap<Integer, Raw>();
		for(int i = 0; i < hexString.length(); i = i + 2) {
			String octeto = Util.padLeft(Util.hexToBin(hexString.substring(i, i + 2)), 8, "0");
			
			Raw r = new Raw();
			r.setBin(octeto);
			
			raw.put(key, r);
			key++;
		}
		
		return raw;
	}
}