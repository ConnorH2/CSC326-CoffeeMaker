package edu.ncsu.csc326.coffee_maker.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Recipe;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.mapper.RecipeMapper;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;
import edu.ncsu.csc326.coffee_maker.services.MakeRecipeService;

/**
 * Implementation of the MakeRecipeService interface.
 */
@Service
public class MakeRecipeServiceImpl implements MakeRecipeService {

    /** The service that lets us interact with ingredientRepository */

    @Autowired
    private IngredientService ingredientService;

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param inventoryDto
     *            current inventory
     * @param recipeDto
     *            recipe to make
     * @return updated inventory
     */
    @Override
    public boolean makeRecipe ( final RecipeDto recipeDto ) {
        final Recipe recipe = RecipeMapper.mapToRecipe( recipeDto );
        if ( enoughIngredients( recipe ) ) {
            // Subtract the needed ingredients
            for ( int i = 0; i < recipe.getIngredients().size(); i++ ) {
                final String ingredientName = recipe.getIngredients().get( i ).getName();
                final IngredientDto updatedIngredient = ingredientService.getIngredientByName( ingredientName );

                updatedIngredient
                        .setAmount( updatedIngredient.getAmount() - recipe.getIngredients().get( i ).getAmount() );
                ingredientService.updateIngredient( updatedIngredient.getId(), updatedIngredient );
            }
            return true;
        }

        return false;

    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param inventory
     *            coffee maker inventory
     * @param recipe
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    private boolean enoughIngredients ( final Recipe recipe ) {
        boolean isEnough = true;

        for ( int i = 0; i < recipe.getIngredients().size(); i++ ) {
            final String ingredientName = recipe.getIngredients().get( i ).getName();
            try {
                // Check that the ingredient exists
                final IngredientDto inventoryIngredient = ingredientService.getIngredientByName( ingredientName );

                // Check that there is enough of the ingredient
                if ( inventoryIngredient.getAmount() - recipe.getIngredients().get( i ).getAmount() < 0 ) {
                    isEnough = false;
                }
            }
            catch ( final ResourceNotFoundException e ) {
                isEnough = false;
            }
        }

        return isEnough;
    }

}
