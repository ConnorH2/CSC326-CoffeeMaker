package edu.ncsu.csc326.coffee_maker.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase ( replace = Replace.NONE )
class IngredientRepositoryTest {

    /**
     * A private instance of the ingredient repository to interact with the
     * database
     */
    @Autowired
    private IngredientRepository ingredientRepository;

    /** The id of ingredient 1 */
    private Long                 ingredient1Id;
    /** The id of ingredient 2 */
    private Long                 ingredient2Id;

    @BeforeEach
    public void setUp () throws Exception {
        ingredientRepository.deleteAll();

        final Ingredient ingredient1 = new Ingredient( "Coffee", 5 );
        final Ingredient ingredient2 = new Ingredient( "Sweet Tea", 10 );

        ingredient1Id = ingredientRepository.save( ingredient1 ).getId();
        ingredient2Id = ingredientRepository.save( ingredient2 ).getId();

        System.out.println( ingredient1Id + " " + ingredient2Id );
    }

    @Test
    @Transactional
    public void testAddIngredients () {
        final Ingredient i1 = ingredientRepository.findById( ingredient1Id ).get();
        assertAll( "Ingredient contents", () -> assertEquals( ingredient1Id, i1.getId() ),
                () -> assertEquals( "Coffee", i1.getName() ), () -> assertEquals( 5, i1.getAmount() ) );

        final Ingredient i2 = ingredientRepository.findById( ingredient2Id ).get();
        assertAll( "Ingredient contents", () -> assertEquals( ingredient2Id, i2.getId() ),
                () -> assertEquals( "Sweet Tea", i2.getName() ), () -> assertEquals( 10, i2.getAmount() ) );
    }

}