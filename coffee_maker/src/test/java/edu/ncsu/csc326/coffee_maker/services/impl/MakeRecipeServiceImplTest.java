/**
 *
 */
package edu.ncsu.csc326.coffee_maker.services.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import edu.ncsu.csc326.coffee_maker.mapper.InventoryMapper;
import edu.ncsu.csc326.coffee_maker.repositories.InventoryRepository;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;
import edu.ncsu.csc326.coffee_maker.services.MakeRecipeService;

/**
 *
 */
@SpringBootTest
class MakeRecipeServiceImplTest {

    @Autowired
    private MakeRecipeService   makeRecipeService;

    /** Reference to inventory repository */
    @Autowired
    private InventoryRepository inventoryRepository;

    /** Reference to recipe repository */
    @Autowired
    private RecipeRepository    recipeRepository;

    /**
     * Sets up the test case.
     *
     * @throws java.lang.Exception
     *             if error
     */
    @BeforeEach
    void setUp () throws Exception {
        inventoryRepository.deleteAll();
        recipeRepository.deleteAll();
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc326.coffee_maker.services.impl.MakeRecipeServiceImpl#makeRecipe(edu.ncsu.csc326.coffee_maker.dto.InventoryDto, edu.ncsu.csc326.coffee_maker.dto.RecipeDto)}.
     */
    @Test
    void testMakeRecipe () {
        final InventoryDto invdto1 = new InventoryDto( 5L, 12, 11, 11, 11 );

        final RecipeDto r1 = new RecipeDto( 1L, "1 of everything", 111, 1, 1, 1, 1 );
        final RecipeDto r2 = new RecipeDto( 2L, "BIG COFFEE", 30, 10, 0, 0, 0 );
        final RecipeDto r3 = new RecipeDto( 3L, "BIG CHOCOLATE", 50, 0, 0, 0, 10 );
        final RecipeDto r4 = new RecipeDto( 4L, "empty the store", 15, 0, 9, 9, 9 );

        // Test valid recipes
        assertTrue( makeRecipeService.makeRecipe( invdto1, r1 ) );
        final Inventory inv1 = inventoryRepository.findAll().getFirst();
        assertAll( "Inventory contents", () -> assertEquals( 11, inv1.getCoffee() ),
                () -> assertEquals( 10, inv1.getMilk() ), () -> assertEquals( 10, inv1.getSugar() ),
                () -> assertEquals( 10, inv1.getChocolate() ) );

        assertTrue( makeRecipeService.makeRecipe( InventoryMapper.mapToInventoryDto( inv1 ), r1 ) );
        final Inventory inv2 = inventoryRepository.findAll().getFirst();
        assertAll( "Inventory contents", () -> assertEquals( 10, inv2.getCoffee() ),
                () -> assertEquals( 9, inv2.getMilk() ), () -> assertEquals( 9, inv2.getSugar() ),
                () -> assertEquals( 9, inv2.getChocolate() ) );

        assertTrue( makeRecipeService.makeRecipe( InventoryMapper.mapToInventoryDto( inv2 ), r2 ) );
        final Inventory inv3 = inventoryRepository.findAll().getFirst();
        assertAll( "Inventory contents", () -> assertEquals( 0, inv3.getCoffee() ),
                () -> assertEquals( 9, inv3.getMilk() ), () -> assertEquals( 9, inv3.getSugar() ),
                () -> assertEquals( 9, inv3.getChocolate() ) );

        assertFalse( makeRecipeService.makeRecipe( InventoryMapper.mapToInventoryDto( inv3 ), r3 ) );
        final Inventory inv4 = inventoryRepository.findAll().getFirst();
        assertAll( "Inventory contents", () -> assertEquals( 0, inv4.getCoffee() ),
                () -> assertEquals( 9, inv4.getMilk() ), () -> assertEquals( 9, inv4.getSugar() ),
                () -> assertEquals( 9, inv4.getChocolate() ) );

        assertTrue( makeRecipeService.makeRecipe( InventoryMapper.mapToInventoryDto( inv4 ), r4 ) );
        final Inventory inv5 = inventoryRepository.findAll().getFirst();
        assertAll( "Inventory contents", () -> assertEquals( 0, inv5.getCoffee() ),
                () -> assertEquals( 0, inv5.getMilk() ), () -> assertEquals( 0, inv5.getSugar() ),
                () -> assertEquals( 0, inv5.getChocolate() ) );

        assertFalse( makeRecipeService.makeRecipe( InventoryMapper.mapToInventoryDto( inv5 ), r1 ) );
        final Inventory inv6 = inventoryRepository.findAll().getFirst();
        assertAll( "Inventory contents", () -> assertEquals( 0, inv6.getCoffee() ),
                () -> assertEquals( 0, inv6.getMilk() ), () -> assertEquals( 0, inv6.getSugar() ),
                () -> assertEquals( 0, inv6.getChocolate() ) );
    }

}
