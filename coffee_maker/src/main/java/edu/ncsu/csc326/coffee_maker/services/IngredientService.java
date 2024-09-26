package edu.ncsu.csc326.coffee_maker.services;

import java.util.List;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;

/**
 * Service interface for managing ingredients. Provides methods for creating,
 * retrieving, updating, and deleting ingredients.
 */
public interface IngredientService {

    /**
     * Creates a new ingredient.
     *
     * @param ingredientDto
     *            the data transfer object containing the details of the
     *            ingredient to create
     * @return the created IngredientDto with updated information (e.g.,
     *         generated ID)
     */
    IngredientDto createIngredient ( IngredientDto ingredientDto );

    /**
     * Retrieves an ingredient by its unique ID.
     *
     * @param ingredientId
     *            the ID of the ingredient to retrieve
     * @return the IngredientDto corresponding to the given ID
     */
    IngredientDto getIngredientById ( Long ingredientId );

    /**
     * Retrieves an ingredient by its name.
     *
     * @param ingredientName
     *            the name of the ingredient to retrieve
     * @return the IngredientDto corresponding to the given name
     */
    IngredientDto getIngredientByName ( String ingredientName );

    /**
     * Retrieves a list of all ingredients.
     *
     * @return a list of IngredientDto objects representing all ingredients
     */
    List<IngredientDto> getAllIngredients ();

    /**
     * Updates an existing ingredient.
     *
     * @param ingredientId
     *            the ID of the ingredient to update
     * @param ingredient
     *            the IngredientDto containing updated ingredient information
     * @return the updated IngredientDto
     */
    IngredientDto updateIngredient ( Long ingredientId, IngredientDto ingredient );

    /**
     * Deletes an ingredient by its unique ID.
     *
     * @param ingredientId
     *            the ID of the ingredient to delete
     */
    void deleteIngredient ( Long ingredientId );

    /**
     * Deletes all ingredients from the system.
     */
    void deleteAllIngredients ();

    /**
     * Updates all existing ingredients.
     *
     * @param ingredientList
     *            all ingredients in the system
     * @return the updated list of ingredients
     */
    List<IngredientDto> updateAllIngredients ( List<IngredientDto> ingredientList );

}
