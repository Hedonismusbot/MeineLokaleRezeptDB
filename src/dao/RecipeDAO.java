package dao;

import java.util.List;

import model.Recipe;

public interface RecipeDAO {

	List<Recipe> findAllRecipe();

	boolean addNewRecipe(Recipe rz);

	boolean deleteRecipe(int idjava);

	boolean deleteAllRecipe();

	boolean updateRecipeString(String field, String newValue, int id);

	boolean updateRecipeDouble(String field, Double newValue, int id);

	boolean updateRecipeInteger(String field, int newValue, int id);

	Recipe findRecipe(int idjava);

}
