package edu.ncsu.csc326.coffee_maker.mapper;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Recipe;

/**
 * Converts between RecipeDto and Recipe entity
 */
public class RecipeMapper {

    /**
     * Converts a Recipe entity to RecipeDto
     *
     * @param recipe
     *            Recipe to convert
     * @return RecipeDto object
     */
    public static RecipeDto mapToRecipeDto ( final Recipe recipe ) {
        final List<IngredientDto> ingredientsDto = new ArrayList<IngredientDto>();
        for ( final Ingredient ingredient : recipe.getIngredients() ) {
            ingredientsDto.add( IngredientMapper.mapToIngredientDto( ingredient ) );
        }
        return new RecipeDto( recipe.getId(), recipe.getName(), recipe.getPrice(), ingredientsDto );
    }

    /**
     * Converts a RecipeDto object to a Recipe entity.
     *
     * @param recipeDto
     *            RecipeDto to convert
     * @return Recipe entity
     */
    public static Recipe mapToRecipe ( final RecipeDto recipeDto ) {
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();
        for ( final IngredientDto ingredientDto : recipeDto.getIngredients() ) {
            ingredients.add( IngredientMapper.mapToIngredient( ingredientDto ) );
        }
        return new Recipe( recipeDto.getId(), recipeDto.getName(), recipeDto.getPrice(), ingredients );
    }

}
