/**
 *
 */
package edu.ncsu.csc326.coffee_maker.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Recipe;

/**
 * Tests Recipe repository
 */
@DataJpaTest
@AutoConfigureTestDatabase ( replace = Replace.NONE )
class RecipeRepositoryTest {
    /** Reference to recipe repository */
    @Autowired
    private RecipeRepository recipeRepository;

    /**
     * Sets up the test case.
     *
     * @throws java.lang.Exception
     *             if error
     */
    @BeforeEach
    public void setUp () throws Exception {
        recipeRepository.deleteAll();
        final Ingredient ingredient1a = new Ingredient( "Milk", 2222 );
        final List<Ingredient> ingredients1 = new ArrayList<Ingredient>();
        ingredients1.add( ingredient1a );

        final Ingredient ingredient2a = new Ingredient( "Water", 3333 );
        final Ingredient ingredient2b = new Ingredient( "Sugar", 4444 );
        final List<Ingredient> ingredients2 = new ArrayList<Ingredient>();
        ingredients2.add( ingredient2a );
        ingredients2.add( ingredient2b );

        final Recipe recipe1 = new Recipe( 1L, "Coffee", 50, ingredients1 );
        final Recipe recipe2 = new Recipe( 2L, "Latte", 100, ingredients2 );

        recipeRepository.save( recipe1 );
        recipeRepository.save( recipe2 );
    }

    /**
     * Tets getRecipeByName
     */
    @Test
    public void testGetRecipeByName () {
        final Optional<Recipe> recipe = recipeRepository.findByName( "Coffee" );
        final Recipe actualRecipe = recipe.get();
        assertAll( "Recipe contents", () -> assertEquals( "Coffee", actualRecipe.getName() ),
                () -> assertEquals( 50, actualRecipe.getPrice() ) );

        actualRecipe.setName( "Coffee2" );
        actualRecipe.setPrice( 0 );

        assertAll( "Recipe contents", () -> assertEquals( "Coffee2", actualRecipe.getName() ),
                () -> assertEquals( 0, actualRecipe.getPrice() ) );

        actualRecipe.setName( "Coffee" );
        actualRecipe.setPrice( 50 );

        final Optional<Recipe> recipe2 = recipeRepository.findByName( "Latte" );
        final Recipe actualRecipe2 = recipe2.get();
        assertAll( "Recipe contents", () -> assertEquals( "Latte", actualRecipe2.getName() ),
                () -> assertEquals( 100, actualRecipe2.getPrice() ) );

    }

    /**
     * Test get recipe get by name invalid
     */
    @Test
    public void testGetRecipeByNameInvalid () {
        final Optional<Recipe> recipe = recipeRepository.findByName( "Unknown" );
        assertTrue( recipe.isEmpty() );
    }
}
