package edu.ncsu.csc326.coffee_maker.services.impl;

import org.springframework.stereotype.Service;

import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Recipe;
import edu.ncsu.csc326.coffee_maker.services.MakeRecipeService;

/**
 * Implementation of the MakeRecipeService interface.
 */
@Service
public class MakeRecipeServiceImpl implements MakeRecipeService {

    /** Connection to the repository to work with the DAO + database */
    /*
     * @Autowired private InventoryRepository inventoryRepository;
     */

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
        /*
         * final Inventory inventory =
         * InventoryMapper.mapToInventory(inventoryDto); final Recipe recipe =
         * RecipeMapper.mapToRecipe(recipeDto); if ( enoughIngredients(
         * inventory, recipe ) ) { inventoryRepository.save(inventory); return
         * true; }
         */

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
        final boolean isEnough = true;
        // if ( inventory.getCoffee() < recipe.getCoffee() ) {
        // isEnough = false;
        // }
        // if ( inventory.getMilk() < recipe.getMilk() ) {
        // isEnough = false;
        // }
        // if ( inventory.getSugar() < recipe.getSugar() ) {
        // isEnough = false;
        // }
        // if ( inventory.getChocolate() < recipe.getChocolate() ) {
        // isEnough = false;
        // }
        return isEnough;
    }

}
