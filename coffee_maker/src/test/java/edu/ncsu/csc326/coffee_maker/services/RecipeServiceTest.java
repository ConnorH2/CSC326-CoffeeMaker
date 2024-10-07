/**
 *
 */
package edu.ncsu.csc326.coffee_maker.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;

/**
 *
 */
@SpringBootTest
public class RecipeServiceTest {

    @Autowired
    private RecipeService     recipeService;

    /** Reference to recipe repository */
    @Autowired
    private RecipeRepository  recipeRepository;

    /**
     * Reference to ingredient service so that we can add to inventory before
     * adding a recipe
     */
    @Autowired
    private IngredientService ingredientService;

    /**
     * Sets up the test case.
     *
     * @throws java.lang.Exception
     *             if error
     */
    @BeforeEach
    public void setUp () throws Exception {
        recipeRepository.deleteAll();
        ingredientService.deleteAllIngredients();
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#createRecipe(edu.ncsu.csc326.coffee_maker.dto.RecipeDto)}.
     */
    @Test
    @Transactional
    public void testCreateRecipe () {
        final IngredientDto ingredient1a = new IngredientDto( "Milk", 2222 );
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient1a );
        ingredientService.createIngredient( ingredient1a );

        assertEquals( ingredientService.getAllIngredients().size(), 1 );

        final RecipeDto recipeDto = new RecipeDto( 0L, "Coffee", 5, ingredients1 );
        final RecipeDto savedRecipe = recipeService.createRecipe( recipeDto );
        assertAll( "Recipe contents", () -> assertTrue( savedRecipe.getId() > 1L ),
                () -> assertEquals( "Coffee", savedRecipe.getName() ),
                () -> assertEquals( 5, savedRecipe.getPrice() ) );

        final RecipeDto retrievedRecipe = recipeService.getRecipeById( savedRecipe.getId() );
        assertAll( "Recipe contents", () -> assertEquals( savedRecipe.getId(), retrievedRecipe.getId() ),
                () -> assertEquals( "Coffee", retrievedRecipe.getName() ),
                () -> assertEquals( 5, retrievedRecipe.getPrice() ) );
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#getRecipeById(java.lang.Long)}.
     */
    @Test
    void testGetRecipeById () {
        final IngredientDto ingredient1a = new IngredientDto( "Milk", 2222 );
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient1a );
        ingredientService.createIngredient( ingredient1a );

        final RecipeDto recipeDto = new RecipeDto( 1L, "Cube of sugar", 10, ingredients1 );
        final RecipeDto savedRecipe = recipeService.createRecipe( recipeDto );
        final RecipeDto retrievedRecipe = recipeService.getRecipeById( savedRecipe.getId() );

        assertAll( "Recipe contents", () -> assertEquals( savedRecipe.getId(), retrievedRecipe.getId() ),
                () -> assertEquals( "Cube of sugar", retrievedRecipe.getName() ),
                () -> assertEquals( 10, retrievedRecipe.getPrice() ) );
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#getRecipeByName(java.lang.String)}.
     */
    @Test
    void testGetRecipeByName () {
        final IngredientDto ingredient1a = new IngredientDto( "Milk", 2222 );
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient1a );
        ingredientService.createIngredient( ingredient1a );

        final RecipeDto recipeDto = new RecipeDto( 0L, "Coffee", 50, ingredients1 );
        final RecipeDto savedRecipe = recipeService.createRecipe( recipeDto );
        final RecipeDto retrievedRecipe = recipeService.getRecipeByName( savedRecipe.getName() );

        final IngredientDto ingredient2a = new IngredientDto( "Milk", 2222 );
        final List<IngredientDto> ingredients2 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient2a );
        ingredientService.createIngredient( ingredient2a );
        final RecipeDto recipeDto2 = new RecipeDto( 3L, "Chocolate bar", 30, ingredients2 );
        final RecipeDto savedRecipe2 = recipeService.createRecipe( recipeDto2 );
        final RecipeDto retrievedRecipe2 = recipeService.getRecipeByName( savedRecipe2.getName() );

        assertAll( "Recipe contents", () -> assertEquals( savedRecipe.getId(), retrievedRecipe.getId() ),
                () -> assertEquals( "Coffee", retrievedRecipe.getName() ),
                () -> assertEquals( 50, retrievedRecipe.getPrice() ) );

        assertAll( "Recipe contents", () -> assertEquals( savedRecipe2.getId(), retrievedRecipe2.getId() ),
                () -> assertEquals( "Chocolate bar", retrievedRecipe2.getName() ),
                () -> assertEquals( 30, retrievedRecipe2.getPrice() ) );
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#isDuplicateName(java.lang.String)}.
     */
    @Test
    void testIsDuplicateName () {
        final IngredientDto ingredient1a = new IngredientDto( "Milk", 2222 );
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient1a );
        ingredientService.createIngredient( ingredient1a );

        final RecipeDto recipeDto = new RecipeDto( 0L, "Coffee", 50, ingredients1 );
        recipeService.createRecipe( recipeDto );

        final IngredientDto ingredient2a = new IngredientDto( "Milk", 2222 );
        final List<IngredientDto> ingredients2 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient2a );
        ingredientService.createIngredient( ingredient2a );

        final RecipeDto recipeDto2 = new RecipeDto( 3L, "Chocolate bar", 30, ingredients2 );
        recipeService.createRecipe( recipeDto2 );

        assertFalse( recipeService.isDuplicateName( "coffeee" ) );
        assertFalse( recipeService.isDuplicateName( "chocolate" ) );
        assertFalse( recipeService.isDuplicateName( "cofe" ) );
        assertFalse( recipeService.isDuplicateName( "C0ffee" ) );

        assertTrue( recipeService.isDuplicateName( "Coffee" ) );
        assertTrue( recipeService.isDuplicateName( "Chocolate bar" ) );
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#getAllRecipes()}.
     */
    @Test
    void testGetAllRecipes () {
        final IngredientDto ingredient1a = new IngredientDto( "Milk", 2222 );
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient1a );
        ingredientService.createIngredient( ingredient1a );
        final RecipeDto recipeDto = new RecipeDto( 0L, "Coffee", 50, ingredients1 );
        final RecipeDto savedRecipe = recipeService.createRecipe( recipeDto );
        final IngredientDto ingredient2a = new IngredientDto( "Milk", 2222 );
        final List<IngredientDto> ingredients2 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient2a );
        ingredientService.createIngredient( ingredient2a );
        final RecipeDto recipeDto2 = new RecipeDto( 3L, "Chocolate bar", 30, ingredients2 );
        final RecipeDto savedRecipe2 = recipeService.createRecipe( recipeDto2 );
        final IngredientDto ingredient3a = new IngredientDto( "Milk", 2222 );
        final List<IngredientDto> ingredients3 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient3a );
        ingredientService.createIngredient( ingredient3a );
        final RecipeDto recipeDto3 = new RecipeDto( 1L, "Cube of sugar", 10, ingredients3 );
        final RecipeDto savedRecipe3 = recipeService.createRecipe( recipeDto3 );

        final List<RecipeDto> r = recipeService.getAllRecipes();
        assertEquals( r.size(), 3 );
        final RecipeDto r1 = r.get( 0 );
        final RecipeDto r2 = r.get( 1 );
        final RecipeDto r3 = r.get( 2 );

        assertAll( "Recipe contents", () -> assertEquals( savedRecipe.getId(), r1.getId() ),
                () -> assertEquals( "Coffee", r1.getName() ), () -> assertEquals( 50, r1.getPrice() ) );

        assertAll( "Recipe contents", () -> assertEquals( savedRecipe2.getId(), r2.getId() ),
                () -> assertEquals( "Chocolate bar", r2.getName() ), () -> assertEquals( 30, r2.getPrice() ) );

        assertAll( "Recipe contents", () -> assertEquals( savedRecipe3.getId(), r3.getId() ),
                () -> assertEquals( "Cube of sugar", r3.getName() ), () -> assertEquals( 10, r3.getPrice() ) );
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#updateRecipe(java.lang.Long, edu.ncsu.csc326.coffee_maker.dto.RecipeDto)}.
     */
    @Test
    void testUpdateRecipe () {
        final IngredientDto ingredient1a = new IngredientDto( "Milk", 1111 );
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient1a );
        ingredientService.createIngredient( ingredient1a );
        RecipeDto recipeDto = new RecipeDto( 0L, "Coffee", 50, ingredients1 );
        final RecipeDto r1 = recipeService.createRecipe( recipeDto );

        assertEquals( 1, r1.getIngredients().size() );
        assertEquals( 1111, r1.getIngredients().get( 0 ).getAmount() );
        assertEquals( "Milk", r1.getIngredients().get( 0 ).getName() );

        assertThrows( ResourceNotFoundException.class, () -> recipeService.updateRecipe( "adbabsgda", r1 ) );

        final long id = r1.getId();

        assertAll( "Recipe contents", () -> assertEquals( id, r1.getId() ),
                () -> assertEquals( "Coffee", r1.getName() ), () -> assertEquals( 50, r1.getPrice() ) );
        assertEquals( 1, r1.getIngredients().size() );
        assertEquals( 1111, r1.getIngredients().get( 0 ).getAmount() );
        assertEquals( "Milk", r1.getIngredients().get( 0 ).getName() );

        final IngredientDto ingredient2a = new IngredientDto( "Milk", 2222 );
        final IngredientDto ingredient2b = new IngredientDto( "Sugar", 222 );
        final List<IngredientDto> ingredients2 = new ArrayList<IngredientDto>();
        ingredients2.add( ingredient2a );
        ingredients2.add( ingredient2b );
        ingredientService.createIngredient( ingredient2b );
        recipeDto = new RecipeDto( 0L, "Cuppa Coffee", 70, ingredients2 );
        recipeService.updateRecipe( "Coffee", recipeDto );

        assertEquals( 2, ingredients2.size() );
        assertEquals( 2222, recipeDto.getIngredients().get( 0 ).getAmount() );
        assertEquals( "Milk", recipeDto.getIngredients().get( 0 ).getName() );
        assertEquals( 222, recipeDto.getIngredients().get( 1 ).getAmount() );
        assertEquals( "Sugar", recipeDto.getIngredients().get( 1 ).getName() );

        final RecipeDto r2 = recipeService.getRecipeById( id );

        assertAll( "Recipe contents", () -> assertEquals( id, r2.getId() ),
                () -> assertEquals( "Cuppa Coffee", r2.getName() ), () -> assertEquals( 70, r2.getPrice() ) );
        assertEquals( 2, r2.getIngredients().size() );
        assertEquals( 2222, r2.getIngredients().get( 0 ).getAmount() );
        assertEquals( "Milk", r2.getIngredients().get( 0 ).getName() );
        assertEquals( 222, r2.getIngredients().get( 1 ).getAmount() );
        assertEquals( "Sugar", r2.getIngredients().get( 1 ).getName() );

    }

    /**
     * Test method for
     * {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#deleteRecipe(java.lang.Long)}.
     */
    @Test
    void testDeleteRecipe () {
        final IngredientDto ingredient1a = new IngredientDto( "Milk", 2222 );
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient1a );
        ingredientService.createIngredient( ingredient1a );
        final RecipeDto recipeDto = new RecipeDto( 0L, "Coffee", 50, ingredients1 );
        final RecipeDto savedRecipe = recipeService.createRecipe( recipeDto );

        final IngredientDto ingredient2a = new IngredientDto( "Milk", 2222 );
        final List<IngredientDto> ingredients2 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient2a );
        ingredientService.createIngredient( ingredient2a );
        final RecipeDto recipeDto2 = new RecipeDto( 3L, "Chocolate bar", 30, ingredients2 );
        final RecipeDto savedRecipe2 = recipeService.createRecipe( recipeDto2 );

        final IngredientDto ingredient3a = new IngredientDto( "Milk", 2222 );
        final List<IngredientDto> ingredients3 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient3a );
        ingredientService.createIngredient( ingredient3a );
        final RecipeDto recipeDto3 = new RecipeDto( 1L, "Cube of sugar", 10, ingredients3 );
        final RecipeDto savedRecipe3 = recipeService.createRecipe( recipeDto3 );

        final List<RecipeDto> rlist1 = recipeService.getAllRecipes();
        assertEquals( rlist1.size(), 3 );

        recipeService.deleteRecipe( savedRecipe2.getId() );

        assertThrows( ResourceNotFoundException.class, () -> recipeService.getRecipeById( savedRecipe2.getId() ) );
        assertThrows( ResourceNotFoundException.class, () -> recipeService.deleteRecipe( savedRecipe2.getId() ) );

        final List<RecipeDto> rlist2 = recipeService.getAllRecipes();
        assertEquals( rlist2.size(), 2 );
        final RecipeDto r1 = rlist2.get( 0 );
        final RecipeDto r3 = rlist2.get( 1 );

        assertAll( "Recipe contents", () -> assertEquals( savedRecipe.getId(), r1.getId() ),
                () -> assertEquals( "Coffee", r1.getName() ), () -> assertEquals( 50, r1.getPrice() ) );

        assertAll( "Recipe contents", () -> assertEquals( savedRecipe3.getId(), r3.getId() ),
                () -> assertEquals( "Cube of sugar", r3.getName() ), () -> assertEquals( 10, r3.getPrice() ) );
    }

}