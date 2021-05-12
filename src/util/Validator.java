package util;

public class Validator {
	/**
	 * Überprüft ob besondere Zeichen vorhanden sind <p>( Nummmer & Leerzeichen ausgenommen )
	 * 
	 * @param text
	 * @return if yes returns false
	 */

	public static boolean validateCharactersWithSpace(String text) {
		//Erlaubt : a-z / A-Z / 0-9 / " "/
		final String title_REGEX = "^[a-zA-Z0-9 ]+$";
		return text.matches(title_REGEX);

	}

	/**
	 * Überprüft ob besondere Zeichen vorhanden sind <p>( Nummmer  ausgenommen )
	 * 
	 * @param text
	 * @return if yes returns false
	 */

	public static boolean validateCharacters(String text) {
		//Erlaubt : a-z / A-Z / 0-9 
		final String title_REGEX = "^[a-zA-Z0-9]+$";
		return text.matches(title_REGEX);

	}

	/**
	 * Überprüft länge des Textes
	 * @param text
	 * @param min
	 * @param max
	 * @return if out of Bounds returns false
	 */
	public static boolean validateLength(String text, int min, int max) {
		if (text.length() >= min && text.length() <= max)
			return true;

		return false;
	}

	/**
	 * Überprüft ob Text nur aus Ganzzahlen 
	 * 
	 * @param text
	 * @return true if Text is Integer Value
	 */
	public static boolean validateInteger(String text) {
		final String integer_Regex = "\\d+$";
		return text.matches(integer_Regex);
	}

	/**
	 *  Überprüft ob Text nur aus Zahlen oder "." besteht 
	 * 
	 * @param text
	 * @return true if Text is Double Value
	 */
	public static boolean validateDouble(String text) {
		// vorname Regex java
		final String double_Regex = "^-?\\d+(\\.\\d+)?$";
		return text.matches(double_Regex);
	}

	/**
	 * Überprüft ob auf leere Eingabe
	 * 
	 * @param text
	 * @return false wenn Empty
	 */
	public static boolean validateInputNotEmpty(String text) {
		return !(text == null || text.length() == 0);
	}

	/**
	 * Überprüft ob auf leere Eingabe
	 * 
	 * @param text
	 * @return true wenn Empty
	 */
	public static boolean validateInputEmpty(String text) {
		return (text == null || text.length() == 0);
	}
}
