/**
 *
 */
package edu.ncsu.csc326.coffee_maker.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.repositories.IngredientRepository;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;

/**
 *
 */
@SpringBootTest
class MakeRecipeServiceTest {

    @Autowired
    private IngredientService    ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeService        recipeService;

    @Autowired
    private RecipeRepository     recipeRepository;

    @Autowired
    private MakeRecipeService    makeRecipeService;

    @BeforeEach
    public void setUp () throws Exception {
        ingredientRepository.deleteAll();
        recipeRepository.deleteAll();
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc326.coffee_maker.services.impl.MakeRecipeServiceImpl#makeRecipe(edu.ncsu.csc326.coffee_maker.dto.RecipeDto)}.
     */
    @Test
    void testMakeRecipe () {
        // Create ingredients
        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );
        ingredientService.createIngredient( ingredient1 );
        final IngredientDto ingredient2 = new IngredientDto( "Sugar", 0 );
        ingredientService.createIngredient( ingredient2 );
        final IngredientDto ingredient3 = new IngredientDto( "Honey", 3 );
        ingredientService.createIngredient( ingredient3 );
        final IngredientDto ingredient4 = new IngredientDto( "Cream", 10 );
        ingredientService.createIngredient( ingredient4 );

        List<IngredientDto> inventory = ingredientService.getAllIngredients();
        assertEquals( 4, inventory.size() );
        assertEquals( "Coffee", inventory.get( 0 ).getName() );
        assertEquals( 5, inventory.get( 0 ).getAmount() );
        assertEquals( "Sugar", inventory.get( 1 ).getName() );
        assertEquals( 0, inventory.get( 1 ).getAmount() );
        assertEquals( "Honey", inventory.get( 2 ).getName() );
        assertEquals( 3, inventory.get( 2 ).getAmount() );
        assertEquals( "Cream", inventory.get( 3 ).getName() );
        assertEquals( 10, inventory.get( 3 ).getAmount() );

        // Create recipes
        final IngredientDto recipeIngredient1 = new IngredientDto( "Coffee", 3 );
        final IngredientDto recipeIngredient2 = new IngredientDto( "Sugar", 1 );
        final IngredientDto recipeIngredient3 = new IngredientDto( "Cream", 2 );
        final IngredientDto recipeIngredient4 = new IngredientDto( "Water", 3 );

        // Make a valid recipe
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( recipeIngredient1 );
        ingredients1.add( recipeIngredient3 );
        final RecipeDto R1 = new RecipeDto( null, "R1", 10, ingredients1 );
        recipeService.createRecipe( R1 );

        assertTrue( makeRecipeService.makeRecipe( R1 ) );

        inventory = ingredientService.getAllIngredients();
        assertEquals( 4, inventory.size() );
        assertEquals( "Coffee", inventory.get( 0 ).getName() );
        assertEquals( 2, inventory.get( 0 ).getAmount() );
        assertEquals( "Sugar", inventory.get( 1 ).getName() );
        assertEquals( 0, inventory.get( 1 ).getAmount() );
        assertEquals( "Honey", inventory.get( 2 ).getName() );
        assertEquals( 3, inventory.get( 2 ).getAmount() );
        assertEquals( "Cream", inventory.get( 3 ).getName() );
        assertEquals( 8, inventory.get( 3 ).getAmount() );

        // Make a valid recipe with more ingredients than inventory has
        final List<IngredientDto> ingredients2 = new ArrayList<IngredientDto>();
        ingredients2.add( recipeIngredient1 );
        ingredients2.add( recipeIngredient2 );
        final RecipeDto R2 = new RecipeDto( null, "R2", 20, ingredients2 );
        recipeService.createRecipe( R2 );

        assertFalse( makeRecipeService.makeRecipe( R2 ) );

        inventory = ingredientService.getAllIngredients();
        assertEquals( 4, inventory.size() );
        assertEquals( "Coffee", inventory.get( 0 ).getName() );
        assertEquals( 2, inventory.get( 0 ).getAmount() );
        assertEquals( "Sugar", inventory.get( 1 ).getName() );
        assertEquals( 0, inventory.get( 1 ).getAmount() );
        assertEquals( "Honey", inventory.get( 2 ).getName() );
        assertEquals( 3, inventory.get( 2 ).getAmount() );
        assertEquals( "Cream", inventory.get( 3 ).getName() );
        assertEquals( 8, inventory.get( 3 ).getAmount() );

        // Make a recipe with invalid ingredients
        final List<IngredientDto> ingredients3 = new ArrayList<IngredientDto>();
        ingredients3.add( recipeIngredient1 );
        ingredients3.add( recipeIngredient4 );
        final RecipeDto R3 = new RecipeDto( null, "R3", 30, ingredients3 );
        recipeService.createRecipe( R3 );

        assertFalse( makeRecipeService.makeRecipe( R3 ) );

        inventory = ingredientService.getAllIngredients();
        assertEquals( 4, inventory.size() );
        assertEquals( "Coffee", inventory.get( 0 ).getName() );
        assertEquals( 2, inventory.get( 0 ).getAmount() );
        assertEquals( "Sugar", inventory.get( 1 ).getName() );
        assertEquals( 0, inventory.get( 1 ).getAmount() );
        assertEquals( "Honey", inventory.get( 2 ).getName() );
        assertEquals( 3, inventory.get( 2 ).getAmount() );
        assertEquals( "Cream", inventory.get( 3 ).getName() );
        assertEquals( 8, inventory.get( 3 ).getAmount() );
    }

}
