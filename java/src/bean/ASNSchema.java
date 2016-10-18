package bean;

import java.util.HashMap;

public class ASNSchema {
	private String name;
	private String type;
	private String length;
	private boolean mandatory = true;
	private HashMap<Integer, ASNSchema> attribute;
	
	public ASNSchema() {
		attribute = new HashMap<Integer, ASNSchema>();
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

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public ASNSchema getAttribute(int pos) {
		return attribute.get(pos);
	}
	
	public HashMap<Integer, ASNSchema> getAttribute() {
		return attribute;
	}

	public void setAttribute(int pos, ASNSchema attribute) {
		this.attribute.put(pos, attribute);
	}
}