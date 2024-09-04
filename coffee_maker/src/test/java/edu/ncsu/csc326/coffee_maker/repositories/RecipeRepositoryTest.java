/**
 *
 */
package edu.ncsu.csc326.coffee_maker.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

        final Recipe recipe1 = new Recipe( 1L, "Coffee", 50, 3, 0, 0, 0 );
        final Recipe recipe2 = new Recipe( 2L, "Latte", 100, 3, 2, 1, 0 );

        recipeRepository.save( recipe1 );
        recipeRepository.save( recipe2 );
    }

    @Test
    public void testGetRecipeByName () {
        final Optional<Recipe> recipe = recipeRepository.findByName( "Coffee" );
        final Recipe actualRecipe = recipe.get(); 
        assertAll( "Recipe contents", () -> assertEquals( "Coffee", actualRecipe.getName() ),
                () -> assertEquals( 50, actualRecipe.getPrice() ), () -> assertEquals( 3, actualRecipe.getCoffee() ),
                () -> assertEquals( 0, actualRecipe.getMilk() ), () -> assertEquals( 0, actualRecipe.getSugar() ),
                () -> assertEquals( 0, actualRecipe.getChocolate() ) );

        //assertEquals( Long.compare( actualRecipe.getId(), 1L ), 0 ) ;
        assertTrue( actualRecipe.getMilk() == 0 );
        assertTrue( actualRecipe.getSugar() == 0 );
        assertTrue( actualRecipe.getChocolate() == 0 );

        actualRecipe.setName( "Coffee2" );
        actualRecipe.setPrice( 0 );
        actualRecipe.setCoffee( 5 );
        actualRecipe.setMilk( 1 );
        actualRecipe.setSugar( 1 );
        actualRecipe.setChocolate( 1 );

        assertAll( "Recipe contents", () -> assertEquals( "Coffee2", actualRecipe.getName() ),
                () -> assertEquals( 0, actualRecipe.getPrice() ), () -> assertEquals( 5, actualRecipe.getCoffee() ),
                () -> assertEquals( 1, actualRecipe.getMilk() ), () -> assertEquals( 1, actualRecipe.getSugar() ),
                () -> assertEquals( 1, actualRecipe.getChocolate() ) );

        actualRecipe.setName( "Coffee" );
        actualRecipe.setPrice( 50 );
        actualRecipe.setCoffee( 3 );
        actualRecipe.setMilk( 0 );
        actualRecipe.setSugar( 0 );
        actualRecipe.setChocolate( 0 );

        final Optional<Recipe> recipe2 = recipeRepository.findByName( "Latte" );
        final Recipe actualRecipe2 = recipe2.get();
        assertAll( "Recipe contents", () -> assertEquals( "Latte", actualRecipe2.getName() ),
                () -> assertEquals( 100, actualRecipe2.getPrice() ), () -> assertEquals( 3, actualRecipe2.getCoffee() ),
                () -> assertEquals( 2, actualRecipe2.getMilk() ), () -> assertEquals( 1, actualRecipe2.getSugar() ),
                () -> assertEquals( 0, actualRecipe2.getChocolate() ) );

    }

    @Test
    public void testGetRecipeByNameInvalid () {
        final Optional<Recipe> recipe = recipeRepository.findByName( "Unknown" );
        assertTrue( recipe.isEmpty() );
    }
}
