package util;

import java.util.HashMap;

import model.Ingredient;
import model.Recipe;

public class FreshRecipeCreator {

	// ----------- Variablen -----------

	private static FreshRecipeCreator instance;

	private Recipe freshRecipe = new Recipe();
	private HashMap<String, Ingredient> allIngredientList = new HashMap<String, Ingredient>();
	private final Integer MAXINGRDIENTS = 5;

	// ----------- Methoden -----------
	/**
	 * Maximale Zutaten Anzahl = 5 
	 * @return maximale Zutaten Anzahl
	 */
	public Integer getMAXINGRDIENTS() {
		//TODO Maximale Zutaten Erweitern
		return MAXINGRDIENTS;
	}

	public HashMap<String, Ingredient> getAllIngredientList() {
		return allIngredientList;
	}

	public void setAllIngredientList(HashMap<String, Ingredient> allIngredientList) {
		this.allIngredientList = allIngredientList;
	}

	public Recipe getFreshRecipe() {
		return freshRecipe;
	}

	public void setFreshRecipe(Recipe freshRecipe) {
		this.freshRecipe = freshRecipe;
	}

	public synchronized static FreshRecipeCreator getInstance() {
		if (instance == null) {
			instance = new FreshRecipeCreator();
		}

		return instance;
	}

}
