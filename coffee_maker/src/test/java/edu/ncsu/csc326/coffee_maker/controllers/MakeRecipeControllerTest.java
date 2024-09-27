/**
 *
 */
package edu.ncsu.csc326.coffee_maker.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.repositories.IngredientRepository;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;

/**
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
class MakeRecipeControllerTest {

    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc              mvc;

    /** Reference to recipe repository */
    @Autowired
    private RecipeRepository     recipeRepository;

    /** Reference to ingredient repository */
    @Autowired
    private IngredientRepository ingredientRepository;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp () throws Exception {
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc326.coffee_maker.controllers.MakeRecipeController#makeRecipe(java.lang.String, java.lang.Integer)}.
     */
    @Test
    void testMakeRecipe () throws Exception {
        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );
        final IngredientDto ingredient2 = new IngredientDto( "Sugar", 0 );
        final IngredientDto ingredient3 = new IngredientDto( "Honey", 3 );
        final IngredientDto ingredient4 = new IngredientDto( "Cream", 10 );

        // Add the ingredients
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Coffee" ) );
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "0" ) )
                .andExpect( jsonPath( "$.name" ).value( "Sugar" ) );
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "3" ) )
                .andExpect( jsonPath( "$.name" ).value( "Honey" ) );
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient4 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "10" ) )
                .andExpect( jsonPath( "$.name" ).value( "Cream" ) );

        String ingredientStr = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // check that the values are correct
        assertTrue( ingredientStr.contains( "\"name\":\"Coffee\",\"amount\":5" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Sugar\",\"amount\":0" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Honey\",\"amount\":3" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Cream\",\"amount\":10" ) );

        // Create recipes
        final IngredientDto recipeIngredient1 = new IngredientDto( "Coffee", 3 );
        // final IngredientDto recipeIngredient2 = new IngredientDto( "Sugar", 1
        // );
        final IngredientDto recipeIngredient3 = new IngredientDto( "Cream", 2 );
        final IngredientDto recipeIngredient4 = new IngredientDto( "Water", 3 );

        // Make a valid recipe
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( recipeIngredient1 );
        ingredients1.add( recipeIngredient3 );
        final RecipeDto R1 = new RecipeDto( null, "R1", 10, ingredients1 );

        // Create the recipe in the system
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( R1 ) ).accept( MediaType.APPLICATION_JSON ) );
        assertEquals( 1, recipeRepository.count() );

        // Call makeRecipe
        String result = mvc
                .perform( post( "/api/makerecipe/{name}", "R1" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 100 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Integer change = Integer.valueOf( result );
        assertEquals( 90, change );

        ingredientStr = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // check that the values are correct
        assertTrue( ingredientStr.contains( "\"name\":\"Coffee\",\"amount\":2" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Sugar\",\"amount\":0" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Honey\",\"amount\":3" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Cream\",\"amount\":8" ) );

        // Call the same recipe without enough change
        result = mvc
                .perform( post( "/api/makerecipe/{name}", "R1" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 9 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isConflict() ).andReturn().getResponse().getContentAsString();
        change = Integer.valueOf( result );
        assertEquals( 9, change );

        ingredientStr = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // check that the values are correct
        assertTrue( ingredientStr.contains( "\"name\":\"Coffee\",\"amount\":2" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Sugar\",\"amount\":0" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Honey\",\"amount\":3" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Cream\",\"amount\":8" ) );

        // Call the same recipe, now without enough ingredients in inventory
        result = mvc
                .perform( post( "/api/makerecipe/{name}", "R1" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 100 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
        change = Integer.valueOf( result );
        assertEquals( 100, change );

        ingredientStr = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // check that the values are correct
        assertTrue( ingredientStr.contains( "\"name\":\"Coffee\",\"amount\":2" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Sugar\",\"amount\":0" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Honey\",\"amount\":3" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Cream\",\"amount\":8" ) );

        // Call a new recipe with invalid ingredients(water)
        final List<IngredientDto> ingredients3 = new ArrayList<IngredientDto>();
        ingredients3.add( recipeIngredient1 );
        ingredients3.add( recipeIngredient4 );
        final RecipeDto R3 = new RecipeDto( null, "R3", 30, ingredients3 );
        // Create the recipe in the system
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( R3 ) ).accept( MediaType.APPLICATION_JSON ) );
        assertEquals( 2, recipeRepository.count() );

        // Call makeRecipe
        result = mvc
                .perform( post( "/api/makerecipe/{name}", "R1" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( 100 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
        change = Integer.valueOf( result );
        assertEquals( 100, change );

        ingredientStr = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // check that the values are correct
        assertTrue( ingredientStr.contains( "\"name\":\"Coffee\",\"amount\":2" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Sugar\",\"amount\":0" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Honey\",\"amount\":3" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Cream\",\"amount\":8" ) );

        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
    }

}
