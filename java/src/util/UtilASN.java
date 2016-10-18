package util;

import java.util.ArrayList;
import java.util.HashMap;

import bean.ASN;
import bean.ASNSchema;

public class UtilASN {
	public static String converteClasse(String classe) {
		if(classe.equals("00")) {
			return "universal";
		} else if(classe.equals("01")) {
			return "application"; 
		} else if(classe.equals("10")) {
			return "context-especific"; 
		} else if(classe.equals("11")) {
			return "private";
		} else {
			return null;
		}
	}

	public static String converteTipo(String tipo) {
		if(tipo.equals("0")) {
			return "primitive";
		} else if(tipo.equals("1")) {
			return "constructed";
		} else {
			return null;
		}
	}

	public static String converteTipoDado(String classe, String tipoDado) {
		if(classe.equals("10")) {
			return String.valueOf(Util.binToDec(tipoDado));
		} else {
			if(tipoDado.equals("00000")) { //0
				return "reserved_for_BER";
			} else if(tipoDado.equals("00001")) { //1
				return "BOOLEAN";
			} else if(tipoDado.equals("00010")) { //2
				return "INTEGER";
			} else if(tipoDado.equals("00011")) { //3
				return "BIT_STRING";
			} else if(tipoDado.equals("00100")) { //4
				return "OCTET_STRING";
			} else if(tipoDado.equals("00101")) { //5
				return "NULL";
			} else if(tipoDado.equals("00110")) { //6
				return "OBJECT_IDENTIFIER";
			} else if(tipoDado.equals("00111")) { //7
				return "OBJECT_DESCRIPTOR";
			} else if(tipoDado.equals("01000")) { //8
				return "INSTANCE_AND_EXTERNAL";
			} else if(tipoDado.equals("01001")) { //9
				return "REAL";
			} else if(tipoDado.equals("01010")) { //10
				return "ENUMERATED";
			} else if(tipoDado.equals("01011")) { //11
				return "EMBEDDED_PDV";
			} else if(tipoDado.equals("01100")) { //12
				return "UTF8_STRING";
			} else if(tipoDado.equals("01101")) { //13
				return "RELATIVE_OID";
			} else if(tipoDado.equals("01110")) { //14
				return "TIME";
			} else if(tipoDado.equals("01111")) { //15
				return "reserved_for_future";
			} else if(tipoDado.equals("10000")) { //16
				return "SEQUENCE_OF_SEQUENCE";
			} else if(tipoDado.equals("10001")) { //17
				return "SET_OF_SET";
			} else if(tipoDado.equals("10010")) { //18
				return "NumericString";
			} else if(tipoDado.equals("10011")) { //19
				return "PrintableString";
			} else if(tipoDado.equals("10100")) { //20
				return "T61String TeletexString";
			} else if(tipoDado.equals("10101")) { //21
				return "VideotexString";
			} else if(tipoDado.equals("10110")) { //22
				return "IA5String";
			} else if(tipoDado.equals("10111")) { //23
				return "UTCTime";
			} else if(tipoDado.equals("11000")) { //24
				return "GeneralizedTime";
			} else if(tipoDado.equals("11001")) { //25
				return "GraphicString";
			} else if(tipoDado.equals("11010")) { //26
				return "ISO646String VisibleString";
			} else if(tipoDado.equals("11011")) { //27
				return "GeneralString";
			} else if(tipoDado.equals("11100")) { //28
				return "UniversalString";
			} else if(tipoDado.equals("11101")) { //29
				return "CHARACTER STRING";
			} else if(tipoDado.equals("11110")) { //30
				return "BMPString";
			} else if(tipoDado.equals("11110")) { //31
				return "DATE";
			} else if(tipoDado.equals("11110")) { //32
				return "TIME_OF_DAY";
			} else if(tipoDado.equals("11110")) { //33
				return "DATE_TIME";
			} else if(tipoDado.equals("11110")) { //34
				return "DURATION";
			} else if(tipoDado.equals("11110")) { //35
				return "OID";
			} else if(tipoDado.equals("11110")) { //36
				return "RELATIVE_OID";
			} else if(tipoDado.equals("11110")) { //37
				return "reserved_for_IS";
			} else {
				return null;
			}
		}
	}
	
	public static void print(ASN asn) {
		ArrayList<ASN> array = asn.getArray();
		
		for(ASN asnFilho : array) {
			print(asnFilho, 0);
		}
	}
	
	public static void print(ASN asn, int ident) {
		String classe = asn.getClasse();
		String tipo = asn.getTipo();
		String tipoDado = asn.getTipoDado();
		String valor = asn.getValor();
		ArrayList<ASN> array = asn.getArray();
		
		ASNSchema attribute = null;
		String name = "";
		String type = "";
		if(classe.equals("context-especific")) {
			name = asn.getName();
			type = asn.getType();
		}
		
		System.out.println("");
		System.out.print(Util.padLeft(" ", ident, " ") +
				"C->" + classe +
				" T->" + tipo + 
				" TD->" + tipoDado +
				" Name->" + name +
				" Type->" + type);
		
		if(tipo.equals("primitive")) {
			System.out.print(" Primitive->" + asn.getPrimitive());
		}
		
		if(valor == null) {
			for(ASN asnFilho : array) {
				print(asnFilho, ident + 3);
			}
		} else {
			//System.out.print(" V->" + valor);
			System.out.print(" V->" + valor);
		}
	}
	
	public static String buscaTipoPrimitivo(HashMap<String, ASNSchema> asnSchema, String type) {
		try {
			ASNSchema primitive = asnSchema.get(type);
			
			if(primitive != null) {
				type = buscaTipoPrimitivo(asnSchema, primitive.getType());
			}
		} catch(Exception e) {
			
		}
		
		return type;
	}
	
	public static ASNSchema buscaNo(HashMap<String, ASNSchema> asnSchema, String nomeNo, int pos) {
		ASNSchema pai = asnSchema.get(nomeNo);
		
		HashMap<Integer, ASNSchema> filhos = pai.getAttribute();
		
		ASNSchema filho = filhos.get(pos);
		
		//previne a situacao onde nao existe uma lista de tipos
		//ex.: GSNAddress ::= IPAddress
		if(filho == null) {
			filho = pai;
		}
		
		return filho;
	}
}