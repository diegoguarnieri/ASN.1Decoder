package lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import util.Util;
import bean.Raw;

public class BERFileLoad {

	public static HashMap<Integer, Raw> load(String diretorio, String arquivo) {
		
		HashMap<Integer, Raw> raw = new HashMap<Integer, Raw>();
		
		try {
			InputStream inputStream = new FileInputStream(new File(diretorio + arquivo));
			
			int i = 0;
			while(inputStream.available() > 0) { 

				String hex = String.format("%02x", inputStream.read());
				String bin = Util.padLeft(Util.hexToBin(hex), 8, "0");

				Raw r = new Raw();
				r.setBin(bin);

				raw.put(i, r);
				i++;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}

		return raw;
	}
}