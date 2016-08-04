package lib;

import java.util.ArrayList;
import java.util.HashMap;

import util.Util;
import util.UtilASN;
import bean.ASN;
import bean.ASNSchema;
import bean.Raw;

public class ASNDecode {
	
	private HashMap<Integer, Raw> raw;
	private ASN asn;
	
	public ASNDecode() {
		asn = new ASN();
	}
	
	public void setRaw(HashMap<Integer, Raw> raw) {
		this.raw = raw;
	}
	
	public ASN getASN() {
		return this.asn;
	}
	
	public ASN decode() {
		String tlv = "T"; //Tag-Length-Value
		//int pos = 0;
		int length = 1;
		//ASN asn = new ASN();
		//int qtdOctetos = 0;
		
		//decode(asn, tlv, pos, length, qtdOctetos);
		int octetosUtilizados = 0;
		int totalOctetos = raw.size();
		
		while(octetosUtilizados < totalOctetos) {
			ASN asnFilho = new ASN();
			asn.setArray(asnFilho);
			
			octetosUtilizados += decode(asnFilho, tlv, octetosUtilizados, length);
		}
		
		return asn;
	}
	
	/*
	 * asn - objeto ASN para receber a estrutura do arquivo BER
	 * tlv - tag/length/value - inicia com o valor T
	 * pos - posicao atual do vetor de octetos do arquivo BER
	 * length - quantidade de octetos utilizado no tlv - inicia com 1
	 * 
	 */
	public int decode(ASN asn, String tlv, int pos, int length) {

		String bin = raw.get(pos).getBin();
		
		if(tlv.equals("T")) {

			String classe = bin.substring(0,2);
			String tipo = bin.substring(2,3);
			
			int octetosUtilizados = 1;
			String tipoDado = "";
			if(bin.substring(3,8).equals("11111")) {
				int i = pos + 1;
				do {
					tipoDado += raw.get(i).getBin().substring(1,8); 
					i++;
					octetosUtilizados++;
				} while(raw.get(i - 1).getBin().substring(0,1).equals("1"));
				
			} else {
				tipoDado = bin.substring(3,8);				
			}

			asn.setClasse(UtilASN.converteClasse(classe));
			asn.setTipo(UtilASN.converteTipo(tipo));
			asn.setTipoDado(UtilASN.converteTipoDado(classe, tipoDado));
			
			octetosUtilizados += decode(asn, "L", pos + octetosUtilizados, length);
			
			return octetosUtilizados;

		} else if(tlv.equals("L")) {
			
			String bitMS = bin.substring(0,1);
			String bitLength = bin.substring(1,8);
			
			int octetosUtilizados = 0;
			int qtdOctetosTamanho = 0;
			if(bitMS.equals("0")) {
				//tamanho menor ou igual a 128 octetos

				length = Util.binToDec(bitLength);
				octetosUtilizados += 1;
				
			} else {
				//tamanho maior que 128 octetos

				qtdOctetosTamanho = Util.binToDec(bitLength);

				bitLength = "";
				for(int e = pos + 1; e <= pos + qtdOctetosTamanho; e++) {
					bitLength += raw.get(e).getBin();
				}

				length = Util.binToDec(bitLength);

				pos += qtdOctetosTamanho; //na proxima iteracao deve estar posicionado na posicao do valor
				octetosUtilizados += qtdOctetosTamanho + 1;
			}

			if(length != 0) {
				octetosUtilizados += decode(asn, "V", pos + 1, length);
			}
			
			return octetosUtilizados;

		} else {
			if(asn.getTipo().equals("constructed")) {
				
				int octetosUtilizados = 0;
				while(octetosUtilizados < length) {
					ASN asnFilho = new ASN();					
					asn.setArray(asnFilho);
	
					octetosUtilizados += decode(asnFilho, "T", pos + octetosUtilizados, 1);
				}
				
				return octetosUtilizados;
				
			} else {
				String bitValor = "";
				for(int e = pos; e < pos + length; e++) {
					bitValor += raw.get(e).getBin();
				}
				
				asn.setValor(bitValor);
				
				return length;
			}
		}
	}
	
	public void join(HashMap<String, ASNSchema> asnSchema, String root) {
		ArrayList<ASN> array = asn.getArray();
		
		for(ASN asnFilho : array) {
			join(asnSchema, asnFilho, root);
		}
	}
	
	public void join(HashMap<String, ASNSchema> asnSchema, ASN asn, String root) {
		String classe = asn.getClasse();
		String tipo = asn.getTipo();
		String tipoDado = asn.getTipoDado();
		String valor = asn.getValor();
		ArrayList<ASN> array = asn.getArray();
		
		ASNSchema attribute = null;
		String name = "";
		String type = "";
		if(classe.equals("context-especific")) {
			attribute = UtilASN.buscaNo(asnSchema, root, Integer.parseInt(tipoDado));
			name = attribute.getName();
			type = attribute.getType();
		}
		
		asn.setName(name);
		asn.setType(type);
		
		if(tipo.equals("primitive")) {
			String primitive = UtilASN.buscaTipoPrimitivo(asnSchema, type);
			asn.setPrimitive(primitive);

		}
		
		if(valor == null) {
			for(ASN asnFilho : array) {
				if(classe.equals("context-especific")) {
					root = type;	
				}
				join(asnSchema, asnFilho, root);
			}
		} else {
			//System.out.print(" V->" + valor);
			//System.out.print(" V->" + Util.binToHex(valor));
			
			valor = Util.binToHex(valor);
			asn.setValor(valor);
		}
	}
}