package eto.riswan.rumahsewa.core;

public class Parameter {
	public enum ParameterType {
		STRING, BINARY
	}

	public String name;
	public String value;
	public ParameterType type;

	public Parameter(String name, String value) {
		this(name, value, ParameterType.STRING);
	}

	public Parameter(String name, String value, ParameterType type) {
		this.name = name;
		this.value = value;
		this.type = type;
	}
}
