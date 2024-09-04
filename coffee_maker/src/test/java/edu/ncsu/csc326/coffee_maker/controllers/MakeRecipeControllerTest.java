/**
 *
 */
package edu.ncsu.csc326.coffee_maker.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import edu.ncsu.csc326.coffee_maker.mapper.InventoryMapper;
import edu.ncsu.csc326.coffee_maker.repositories.InventoryRepository;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;
import edu.ncsu.csc326.coffee_maker.services.RecipeService;

/**
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
class MakeRecipeControllerTest {

    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc             mvc;

    /** Reference to recipe repository */
    @Autowired
    private RecipeRepository    recipeRepository;

    /** Reference to inventory repository */
    @Autowired
    private InventoryRepository inventoryRepository;

    /** Connection to RecipeService */
    @Autowired
    private RecipeService       recipeService;

    /**
     * Sets up the test case.
     *
     * @throws java.lang.Exception
     *             if error
     */
    @BeforeEach
    public void setUp () throws Exception {
        inventoryRepository.deleteAll();
        recipeRepository.deleteAll();
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc326.coffee_maker.controllers.MakeRecipeController#makeRecipe(java.lang.String, java.lang.Integer)}.
     *
     * @throws Exception
     *             an exception that occurs when the api request is invalid
     */
    @Test
    @Transactional
    void testMakeRecipe () throws Exception {
        // Initialize the inventory
        final InventoryDto inventoryDto = new InventoryDto( 6L, 10, 11, 12, 13 );
        final Inventory inventory = InventoryMapper.mapToInventory( inventoryDto );
        inventoryRepository.save( inventory );

        // Create recipes
        final RecipeDto recipeDto1 = new RecipeDto( null, "coffee", 10, 3, 0, 0, 0 );
        recipeService.createRecipe( recipeDto1 );
        final RecipeDto recipeDto2 = new RecipeDto( null, "big_coffee", 30, 10, 0, 0, 0 );
        recipeService.createRecipe( recipeDto2 );

        // Make recipes

        // Make a coffee
        mvc.perform( post( "/api/makerecipe/coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 100 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        final Inventory inv1 = inventoryRepository.findAll().getFirst();
        assertAll( "Inventory contents", () -> assertEquals( 7, inv1.getCoffee() ),
                () -> assertEquals( 11, inv1.getMilk() ), () -> assertEquals( 12, inv1.getSugar() ),
                () -> assertEquals( 13, inv1.getChocolate() ) );

        // Test not enough money
        mvc.perform( post( "/api/makerecipe/coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 9 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isConflict() );

        final Inventory inv2 = inventoryRepository.findAll().getFirst();
        assertAll( "Inventory contents", () -> assertEquals( 7, inv2.getCoffee() ),
                () -> assertEquals( 11, inv2.getMilk() ), () -> assertEquals( 12, inv2.getSugar() ),
                () -> assertEquals( 13, inv2.getChocolate() ) );

        // Test not enough ingredients
        mvc.perform( post( "/api/makerecipe/big_coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 50 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() );

        final Inventory inv3 = inventoryRepository.findAll().getFirst();
        assertAll( "Inventory contents", () -> assertEquals( 7, inv3.getCoffee() ),
                () -> assertEquals( 11, inv3.getMilk() ), () -> assertEquals( 12, inv3.getSugar() ),
                () -> assertEquals( 13, inv3.getChocolate() ) );
    }

}
