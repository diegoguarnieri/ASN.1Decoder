package bean;

import java.util.ArrayList;

public class ASN {
	String classe;
	String tipo;
	String tipoDado;
	String name;
	String type;
	String primitive;
	String valor;
	ArrayList<ASN> array = new ArrayList<ASN>();
	
	public ASN() {
		
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipoDado() {
		return tipoDado;
	}

	public void setTipoDado(String tipoDado) {
		this.tipoDado = tipoDado;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrimitive() {
		return primitive;
	}

	public void setPrimitive(String primitive) {
		this.primitive = primitive;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public ArrayList<ASN> getArray() {
		return array;
	}

	public void setArray(ASN array) {
		this.array.add(array);
	}
}