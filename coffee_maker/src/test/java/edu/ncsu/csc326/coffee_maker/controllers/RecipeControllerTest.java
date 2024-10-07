package edu.ncsu.csc326.coffee_maker.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.repositories.IngredientRepository;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;

/**
 * The test for recipeController
 */
@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {

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
     * Sets up the test case.
     *
     * @throws java.lang.Exception
     *             if error
     */
    @BeforeEach
    public void setUp () throws Exception {
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
    }

    /**
     * This is a simple test to see if the getRecipes method works for an
     * uninitialized database.
     */
    @Test
    @Transactional
    public void testGetRecipes () throws Exception {

        // Get a string representation of the recipes in the database
        final String recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // There should be nothing at start up
        assertFalse( recipe.contains( "Mocha" ) );
        assertFalse( recipe.contains( "Coffee" ) );
        // Recipe is just empty brackets
        assertEquals( recipe, "[]" );
    }

    /**
     * This method test creating a recipe within the code
     *
     * @throws Exception
     *             an exception that occurs when the api request is invalid
     */
    @Test
    @Transactional
    public void testCreateRecipe () throws Exception {
        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 10 );
        final IngredientDto ingredient2 = new IngredientDto( "Sugar", 10 );
        final IngredientDto ingredient3 = new IngredientDto( "Honey", 10 );
        final IngredientDto ingredient4 = new IngredientDto( "Cream", 10 );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "10" ) )
        .andExpect( jsonPath( "$.name" ).value( "Coffee" ) );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2 ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "10" ) )
        .andExpect( jsonPath( "$.name" ).value( "Sugar" ) );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3 ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "10" ) )
        .andExpect( jsonPath( "$.name" ).value( "Honey" ) );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient4 ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "10" ) )
        .andExpect( jsonPath( "$.name" ).value( "Cream" ) );
        final IngredientDto ingredient1a = new IngredientDto( "Coffee", 1 );
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient1a );

        final IngredientDto ingredient2a = new IngredientDto( "Coffee", 2 );
        final IngredientDto ingredient2b = new IngredientDto( "Sugar", 1 );
        final List<IngredientDto> ingredients2 = new ArrayList<IngredientDto>();
        ingredients2.add( ingredient2a );
        ingredients2.add( ingredient2b );

        final IngredientDto ingredient3a = new IngredientDto( "Coffee", 3 );
        final IngredientDto ingredient3b = new IngredientDto( "Sugar", 2 );
        final IngredientDto ingredient3c = new IngredientDto( "Cream", 1 );
        final List<IngredientDto> ingredients3 = new ArrayList<IngredientDto>();
        ingredients3.add( ingredient3a );
        ingredients3.add( ingredient3b );
        ingredients3.add( ingredient3c );

        // We can add three recipes, then delete them one by one and see what is
        // left
        final RecipeDto recipe1 = new RecipeDto( null, "Coffee1", 300, ingredients1 );
        recipe1.setIngredients( ingredients1 );
        final RecipeDto recipe2 = new RecipeDto( null, "Coffee2", 100, ingredients2 );
        final RecipeDto recipe3 = new RecipeDto( null, "Coffee3", 200, ingredients3 );
        // We are going to get a string representation of the database through
        // this standard function
        String recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // We can first check that there is nothing in the string
        assertEquals( recipe, "[]" );
        // It makes more sense to say we dont have the recipe we are about to
        // add
        assertFalse( recipe.contains( "Mocha" ) );

        assertEquals( 4, ingredientRepository.count() );

        // The standard post function
        mvc.perform( post( "/api/add-recipe" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe1 ) ).accept( MediaType.APPLICATION_JSON ) );

        // Now we should update the string to have the recipe
        recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Lets check that it has all those properties at the right sections
        assertTrue( recipe.contains( recipe1.getName() ) );
        assertTrue( recipe.contains( "price\":300" ) );
        assertTrue( recipe.contains( recipe1.getIngredients().getFirst().getName() ) );
        assertTrue( recipe.contains( String.valueOf( recipe1.getIngredients().getFirst().getAmount() ) ) );

        // The standard post function
        mvc.perform( post( "/api/add-recipe" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ).accept( MediaType.APPLICATION_JSON ) );

        // Now we should update the string to have the recipe, this will have
        // Mocha and hot chocolate
        recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Lets see that it has both
        assertTrue( recipe.contains( recipe2.getName() ) );
        assertTrue( recipe.contains( recipe1.getName() ) );
        // And that these values exist
        assertTrue( recipe.contains( "price\":100" ) );
        assertTrue( recipe.contains( recipe2.getIngredients().getFirst().getName() ) );
        assertTrue( recipe.contains( String.valueOf( recipe2.getIngredients().getFirst().getAmount() ) ) );
        assertTrue( recipe.contains( recipe2.getIngredients().getLast().getName() ) );
        assertTrue( recipe.contains( String.valueOf( recipe2.getIngredients().getLast().getAmount() ) ) );

        // Next we can try to add a duplicate, this is not allowed so we check
        // an error
        // Lets add hot chocolate again and see what happens, we will change
        // status().isOk to test this
        mvc.perform( post( "/api/add-recipe" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().is( HttpStatus.CONFLICT.value() ) );

        // The standard post function
        mvc.perform( post( "/api/add-recipe" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe3 ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() );

        // Update the string
        recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Lets see that it has all three
        assertTrue( recipe.contains( recipe3.getName() ) );
        assertTrue( recipe.contains( recipe2.getName() ) );
        // assertTrue( recipe.contains( recipe1.getName() ) );
        // And that these values exist
        assertTrue( recipe.contains( "price\":" + recipe3.getPrice() ) );
        assertTrue( recipe.contains( recipe3.getIngredients().getFirst().getName() ) );
        assertTrue( recipe.contains( String.valueOf( recipe3.getIngredients().getFirst().getAmount() ) ) );
        assertTrue( recipe.contains( recipe3.getIngredients().get( 1 ).getName() ) );
        assertTrue( recipe.contains( String.valueOf( recipe3.getIngredients().get( 1 ).getAmount() ) ) );
        assertTrue( recipe.contains( recipe3.getIngredients().getLast().getName() ) );
        assertTrue( recipe.contains( String.valueOf( recipe3.getIngredients().getLast().getAmount() ) ) );
        // Finally we will create a drink that overloads the system since 3 is
        // max
        final RecipeDto futureDrink = new RecipeDto( 2L, "badCoffee", 250, ingredients1 );

        // The standard post function
        mvc.perform( post( "/api/add-recipe" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( futureDrink ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().is( HttpStatus.INSUFFICIENT_STORAGE.value() ) );
    }

    /**
     * This test is to check that deleting recipes works
     *
     * @throws Exception
     *             an exception that happens if the API request is invalid
     */
    @Test
    @Transactional
    public void deleteRecipes () throws Exception {
        final IngredientDto ingredient1a = new IngredientDto( "Milk", 2222 );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient1a ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "2222" ) )
        .andExpect( jsonPath( "$.name" ).value( "Milk" ) );
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient1a );

        final IngredientDto ingredient2a = new IngredientDto( "Water", 3333 );
        final IngredientDto ingredient2b = new IngredientDto( "Sugar", 4444 );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2a ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "3333" ) )
        .andExpect( jsonPath( "$.name" ).value( "Water" ) );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2b ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "4444" ) )
        .andExpect( jsonPath( "$.name" ).value( "Sugar" ) );
        final List<IngredientDto> ingredients2 = new ArrayList<IngredientDto>();
        ingredients2.add( ingredient2a );
        ingredients2.add( ingredient2b );

        final IngredientDto ingredient3a = new IngredientDto( "Cinnamon", 5555 );
        final IngredientDto ingredient3b = new IngredientDto( "Honey", 6666 );
        final IngredientDto ingredient3c = new IngredientDto( "Caramel", 7777 );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3a ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5555" ) )
        .andExpect( jsonPath( "$.name" ).value( "Cinnamon" ) );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3b ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "6666" ) )
        .andExpect( jsonPath( "$.name" ).value( "Honey" ) );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3c ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "7777" ) )
        .andExpect( jsonPath( "$.name" ).value( "Caramel" ) );
        final List<IngredientDto> ingredients3 = new ArrayList<IngredientDto>();
        ingredients3.add( ingredient3a );
        ingredients3.add( ingredient3b );
        ingredients3.add( ingredient3c );

        // We can add three recipes, then delete them one by one and see what is
        // left
        final RecipeDto recipe1 = new RecipeDto( 1L, "Coffee1", 300, ingredients1 );
        recipe1.setIngredients( ingredients1 );
        final RecipeDto recipe2 = new RecipeDto( 2L, "Drink2", 100, ingredients2 );
        final RecipeDto recipe3 = new RecipeDto( 3L, "Custom3", 200, ingredients3 );

        String recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Prove it starts empty
        assertEquals( recipe, "[]" );
        // We can add all three
        mvc.perform( post( "/api/add-recipe" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe1 ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() );
        mvc.perform( post( "/api/add-recipe" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() );
        mvc.perform( post( "/api/add-recipe" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe3 ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() );

        // Update the string
        recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Check they were all added
        assertTrue( recipe.contains( recipe1.getName() ) );
        assertTrue( recipe.contains( recipe2.getName() ) );
        assertTrue( recipe.contains( recipe3.getName() ) );

        // Now that they are all added I can do some magic to get the ids
        final String[] ids = new String[3];
        // returns an array where each part is like "id:14", we need the 14
        final String[] parts = recipe.split( "," );
        // Each recipe has 3 parts so we can find part 0, part 3, part 6
        ids[0] = parts[0];
        ids[1] = parts[6];
        ids[2] = parts[15];

        // Then we need to remove the id part from each, always start with "id":
        ids[0] = ids[0].substring( 7, ids[0].length() );
        ids[1] = ids[1].substring( 6, ids[1].length() );
        ids[2] = ids[2].substring( 6, ids[2].length() );

        // Now lets remove some stuff
        mvc.perform( delete( "/api/recipes/{id}", ids[0] ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe1 ) ).accept( MediaType.APPLICATION_JSON ) );

        recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Since we deleted the first one it should be gone and the others
        // should still be there
        assertFalse( recipe.contains( recipe1.getName() ) );
        assertTrue( recipe.contains( recipe2.getName() ) );
        assertTrue( recipe.contains( recipe3.getName() ) );
        // as well as all its parts
        assertFalse( recipe.contains( "price\":" + recipe1.getPrice() ) );
        assertFalse( recipe.contains( recipe1.getIngredients().getFirst().getName() ) );
        assertFalse( recipe.contains( String.valueOf( recipe1.getIngredients().getFirst().getAmount() ) ) );

        // then we can do the same thing with recipe3
        mvc.perform( delete( "/api/recipes/{id}", ids[2] ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe3 ) ).accept( MediaType.APPLICATION_JSON ) );

        recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Since we deleted the first one it should be gone and the others
        // should still be there
        assertFalse( recipe.contains( recipe1.getName() ) );
        assertTrue( recipe.contains( recipe2.getName() ) );
        assertFalse( recipe.contains( recipe3.getName() ) );
        // as well as all its parts
        assertFalse( recipe.contains( "price\":" + recipe3.getPrice() ) );
        assertFalse( recipe.contains( recipe3.getIngredients().getFirst().getName() ) );
        assertFalse( recipe.contains( String.valueOf( recipe3.getIngredients().getFirst().getAmount() ) ) );
        assertFalse( recipe.contains( recipe3.getIngredients().get( 1 ).getName() ) );
        assertFalse( recipe.contains( String.valueOf( recipe3.getIngredients().get( 1 ).getAmount() ) ) );
        assertFalse( recipe.contains( recipe3.getIngredients().getLast().getName() ) );
        assertFalse( recipe.contains( String.valueOf( recipe3.getIngredients().getLast().getAmount() ) ) );

        // And the last one
        mvc.perform( delete( "/api/recipes/{id}", ids[1] ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ).accept( MediaType.APPLICATION_JSON ) );

        recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Since we deleted the first one it should be gone and the others
        // should still be there
        assertFalse( recipe.contains( recipe1.getName() ) );
        assertFalse( recipe.contains( recipe2.getName() ) );
        assertFalse( recipe.contains( recipe3.getName() ) );
        // as well as all its parts
        assertFalse( recipe.contains( "price\":" + recipe2.getPrice() ) );
        assertFalse( recipe.contains( recipe2.getIngredients().getFirst().getName() ) );
        assertFalse( recipe.contains( String.valueOf( recipe2.getIngredients().getFirst().getAmount() ) ) );
        assertFalse( recipe.contains( recipe2.getIngredients().getLast().getName() ) );
        assertFalse( recipe.contains( String.valueOf( recipe2.getIngredients().getLast().getAmount() ) ) );

        assertEquals( recipe, "[]" );
    }

    /**
     * A test that checks getting a specific recipe works properly
     *
     * @throws Exception
     *             an exception that occurs when the API request is invalid
     */
    @Test
    @Transactional
    public void getRecipe () throws Exception {

        final IngredientDto ingredient1a = new IngredientDto( "Milk", 2222 );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient1a ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "2222" ) )
        .andExpect( jsonPath( "$.name" ).value( "Milk" ) );
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient1a );

        final IngredientDto ingredient2a = new IngredientDto( "Water", 3333 );
        final IngredientDto ingredient2b = new IngredientDto( "Sugar", 4444 );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2a ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "3333" ) )
        .andExpect( jsonPath( "$.name" ).value( "Water" ) );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2b ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "4444" ) )
        .andExpect( jsonPath( "$.name" ).value( "Sugar" ) );
        final List<IngredientDto> ingredients2 = new ArrayList<IngredientDto>();
        ingredients2.add( ingredient2a );
        ingredients2.add( ingredient2b );

        final IngredientDto ingredient3a = new IngredientDto( "Cinnamon", 5555 );
        final IngredientDto ingredient3b = new IngredientDto( "Honey", 6666 );
        final IngredientDto ingredient3c = new IngredientDto( "Caramel", 7777 );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3a ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5555" ) )
        .andExpect( jsonPath( "$.name" ).value( "Cinnamon" ) );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3b ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "6666" ) )
        .andExpect( jsonPath( "$.name" ).value( "Honey" ) );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3c ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "7777" ) )
        .andExpect( jsonPath( "$.name" ).value( "Caramel" ) );
        final List<IngredientDto> ingredients3 = new ArrayList<IngredientDto>();
        ingredients3.add( ingredient3a );
        ingredients3.add( ingredient3b );
        ingredients3.add( ingredient3c );

        // We can add three recipes, then delete them one by one and see what is
        // left
        final RecipeDto recipe1 = new RecipeDto( 1L, "Coffee1", 300, ingredients1 );
        recipe1.setIngredients( ingredients1 );
        final RecipeDto recipe2 = new RecipeDto( 2L, "Drink2", 100, ingredients2 );
        final RecipeDto recipe3 = new RecipeDto( 3L, "Custom3", 200, ingredients3 );

        final String recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // Prove it starts empty
        assertEquals( recipe, "[]" );
        // We can add all three
        mvc.perform( post( "/api/add-recipe" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe1 ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() );
        mvc.perform( post( "/api/add-recipe" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() );
        mvc.perform( post( "/api/add-recipe" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe3 ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() );

        // Now we will get each one of them separately
        final String recipe1String = mvc.perform( get( "/api/recipes/" + recipe1.getName() ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Make sure it is the right one by name
        assertTrue( recipe1String.contains( "Milk" ) );
        assertFalse( recipe1String.contains( "Water" ) );
        assertFalse( recipe1String.contains( "Sugar" ) );
        // We can check it is all the other right parts too
        assertTrue( recipe1String.contains( "price\":" + recipe1.getPrice() ) );
        assertTrue( recipe1String.contains( recipe1.getIngredients().getFirst().getName() ) );
        assertTrue( recipe1String.contains( String.valueOf( recipe1.getIngredients().getFirst().getAmount() ) ) );

        // Then we can do the same for the other two
        final String recipe2String = mvc.perform( get( "/api/recipes/" + recipe2.getName() ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Make sure it is the right one by name
        assertFalse( recipe2String.contains( "Milk" ) );
        assertTrue( recipe2String.contains( "Water" ) );
        assertTrue( recipe2String.contains( "Sugar" ) );
        // We can check it is all the other right parts too
        assertTrue( recipe2String.contains( "price\":" + recipe2.getPrice() ) );
        assertTrue( recipe2String.contains( recipe2.getIngredients().getFirst().getName() ) );
        assertTrue( recipe2String.contains( String.valueOf( recipe2.getIngredients().getFirst().getAmount() ) ) );
        assertTrue( recipe2String.contains( recipe2.getIngredients().getLast().getName() ) );
        assertTrue( recipe2String.contains( String.valueOf( recipe2.getIngredients().getLast().getAmount() ) ) );

        final String recipe3String = mvc.perform( get( "/api/recipes/" + recipe3.getName() ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Make sure it is the right one by name
        assertTrue( recipe3String.contains( "Cinnamon" ) );
        assertTrue( recipe3String.contains( "Honey" ) );
        assertTrue( recipe3String.contains( "Caramel" ) );
        assertFalse( recipe3String.contains( "Milk" ) );
        // We can check it is all the other right parts too
        assertTrue( recipe3String.contains( "price\":" + recipe3.getPrice() ) );
        assertTrue( recipe3String.contains( recipe3.getIngredients().getFirst().getName() ) );
        assertTrue( recipe3String.contains( String.valueOf( recipe3.getIngredients().getFirst().getAmount() ) ) );
        assertTrue( recipe3String.contains( recipe3.getIngredients().get( 1 ).getName() ) );
        assertTrue( recipe3String.contains( String.valueOf( recipe3.getIngredients().get( 1 ).getAmount() ) ) );
        assertTrue( recipe3String.contains( recipe3.getIngredients().getLast().getName() ) );
        assertTrue( recipe3String.contains( String.valueOf( recipe3.getIngredients().getLast().getAmount() ) ) );
    }

    /**
     * A test that checks editing a specific recipe works properly
     *
     * @throws Exception
     *             an exception that occurs when the API request is invalid
     */
    @Test
    @Transactional
    public void editRecipe () throws Exception {
        final IngredientDto ingredient1a = new IngredientDto( "Milk", 2222 );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient1a ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "2222" ) )
        .andExpect( jsonPath( "$.name" ).value( "Milk" ) );
        final List<IngredientDto> ingredients1 = new ArrayList<IngredientDto>();
        ingredients1.add( ingredient1a );

        final IngredientDto ingredient2a = new IngredientDto( "Water", 3333 );
        final IngredientDto ingredient2b = new IngredientDto( "Sugar", 4444 );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2a ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "3333" ) )
        .andExpect( jsonPath( "$.name" ).value( "Water" ) );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2b ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "4444" ) )
        .andExpect( jsonPath( "$.name" ).value( "Sugar" ) );
        final List<IngredientDto> ingredients2 = new ArrayList<IngredientDto>();
        ingredients2.add( ingredient2a );
        ingredients2.add( ingredient2b );

        final IngredientDto ingredient3a = new IngredientDto( "Cinnamon", 5555 );
        final IngredientDto ingredient3b = new IngredientDto( "Honey", 6666 );
        final IngredientDto ingredient3c = new IngredientDto( "Caramel", 7777 );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3a ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5555" ) )
        .andExpect( jsonPath( "$.name" ).value( "Cinnamon" ) );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3b ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "6666" ) )
        .andExpect( jsonPath( "$.name" ).value( "Honey" ) );
        mvc.perform( post( "/api/add-ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3c ) ).accept( MediaType.APPLICATION_JSON ) )
        .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "7777" ) )
        .andExpect( jsonPath( "$.name" ).value( "Caramel" ) );
        final List<IngredientDto> ingredients3 = new ArrayList<IngredientDto>();
        ingredients3.add( ingredient3a );
        ingredients3.add( ingredient3b );
        ingredients3.add( ingredient3c );

        // We can add three recipes, then delete them one by one and see what is
        // left
        final RecipeDto recipe1 = new RecipeDto( 1L, "Coffee1", 300, ingredients1 );
        recipe1.setIngredients( ingredients1 );
        final RecipeDto recipe2 = new RecipeDto( 2L, "Drink2", 100, ingredients2 );
        final RecipeDto recipe3 = new RecipeDto( 3L, "Custom3", 200, ingredients3 );
        // final RecipeDto recipe2 = new RecipeDto( 2L, "Coffee2", 100,
        // ingredients2 );
        // final RecipeDto recipe3 = new RecipeDto( 3L, "Coffee3", 200,
        // ingredients3 );

        // We are going to get a string representation of the database through
        // this standard function
        String recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // The standard post function
        mvc.perform( post( "/api/add-recipe" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe1 ) ).accept( MediaType.APPLICATION_JSON ) );

        // Now we should update the string to have the recipe
        recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Now that they are all added I can do some magic to get the ids
        final String[] ids = new String[3];
        // returns an array where each part is like "id:14", we need the 14
        final String[] parts = recipe.split( "," );
        // Each recipe has 3 parts so we can find part 0, part 3, part 6
        ids[0] = parts[0];

        // Then we need to remove the id part from each, always start with "id":
        ids[0] = ids[0].substring( 7, ids[0].length() );

        // Lets check that it has all those properties at the right sections
        assertTrue( recipe.contains( recipe1.getName() ) );
        assertTrue( recipe.contains( "price\":300" ) );
        assertTrue( recipe.contains( recipe1.getIngredients().getFirst().getName() ) );
        assertTrue( recipe.contains( String.valueOf( recipe1.getIngredients().getFirst().getAmount() ) ) );

        recipe1.setPrice( 100 );
        recipe1.setIngredients( ingredients2 );
        recipe1.setId( Long.valueOf( ids[0] ) );
        mvc.perform( put( "/api/edit-recipe/{id}", ids[0] ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe1 ) ).accept( MediaType.APPLICATION_JSON ) );

        recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();
        assertTrue( recipe.contains( recipe1.getName() ) );
        assertTrue( recipe.contains( "price\":100" ) );
        assertTrue( recipe.contains( recipe2.getIngredients().getFirst().getName() ) );
        assertTrue( recipe.contains( String.valueOf( recipe2.getIngredients().getFirst().getAmount() ) ) );
        assertTrue( recipe.contains( recipe2.getIngredients().getLast().getName() ) );
        assertTrue( recipe.contains( String.valueOf( recipe2.getIngredients().getLast().getAmount() ) ) );

    }
}