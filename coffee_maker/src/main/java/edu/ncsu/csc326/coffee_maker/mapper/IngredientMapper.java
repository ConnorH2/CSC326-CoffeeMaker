package edu.ncsu.csc326.coffee_maker.mapper;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

public class IngredientMapper {

    public static IngredientDto mapToIngredientDto(final Ingredient ingredient) {
        final IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(ingredient.getId());
        ingredientDto.setName(ingredient.getName());
        ingredientDto.setAmount(ingredient.getAmount());
        return ingredientDto;
    }

    public static Ingredient mapToIngredient(final IngredientDto ingredientDto) {
        final Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientDto.getId());
        ingredient.setName(ingredientDto.getName());
        ingredient.setAmount(ingredientDto.getAmount());
        return ingredient;
    }

}