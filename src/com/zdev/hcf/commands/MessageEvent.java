package com.zdev.hcf.commands;

public class MessageEvent {

	public static String format(String message) {
		message = message.replaceAll("&", "�");
		return message;
	}

	public static String toString(String[] arguments, int start) {
		String returnString = "";
		for (int count = start; count < arguments.length; count++) {
			if (count == start) {
				returnString = arguments[count];
			} else {
				returnString = returnString + " " + arguments[count];
			}
		}
		return returnString;
	}
}
