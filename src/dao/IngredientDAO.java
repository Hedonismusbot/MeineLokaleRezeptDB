package dao;

import java.util.List;

import model.Ingredient;

public interface IngredientDAO {

	List<Ingredient> findAllIngredients();

	boolean addNewIngredient(Ingredient rz);

	boolean deleteIngredient(int idjava);

	boolean deleteAllIngredients();

	boolean updateIngredientString(String field, String newValue, int id);

	Ingredient findSingleIngredient(int idjava);

}
