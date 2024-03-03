package org.example;

public enum Color {
	GREEN("\u001B[32m"),
	RESET("\u001B[0m"),
	RED("\u001B[31m"),
	CYAN("\u001B[36m"),
	YELLOW("\u001B[33m"),
	PURPLE( "\u001B[35m");
	
	public final String value;

	Color(String string) {
		this.value = string;
	}
}
