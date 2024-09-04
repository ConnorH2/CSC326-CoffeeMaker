package edu.ncsu.csc326.coffee_maker.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;

/**
 * The test for recipeController
 */
@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {

    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc          mvc;
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
        // We can add three recipes, then delete them one by one and see what is
        // left
        final RecipeDto recipe1 = new RecipeDto( null, "Mocha", 300, 3, 3, 3, 3 );
        final RecipeDto recipe2 = new RecipeDto( null, "Coffee", 100, 1, 1, 1, 1 );
        final RecipeDto recipe3 = new RecipeDto( null, "Black", 200, 2, 2, 2, 2 );

        // We are going to get a string representation of the database through
        // this standard function
        String recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // We can first check that there is nothing in the string
        assertEquals( recipe, "[]" );
        // It makes more sense to say we dont have the recipe we are about to
        // add
        assertFalse( recipe.contains( "Mocha" ) );

        // The standard post function
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe1 ) ).accept( MediaType.APPLICATION_JSON ) );

        // Now we should update the string to have the recipe
        recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Lets check that it has all those properties at the right sections
        assertTrue( recipe.contains( recipe1.getName() ) );
        assertTrue( recipe.contains( "price\":300" ) );
        assertTrue( recipe.contains( "coffee\":3" ) );
        assertTrue( recipe.contains( "milk\":3" ) );
        assertTrue( recipe.contains( "sugar\":3" ) );
        assertTrue( recipe.contains( "chocolate\":3" ) );

        // The standard post function
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
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
        assertTrue( recipe.contains( "coffee\":1" ) );
        assertTrue( recipe.contains( "milk\":1" ) );
        assertTrue( recipe.contains( "sugar\":1" ) );
        assertTrue( recipe.contains( "chocolate\":1" ) );

        // Next we can try to add a duplicate, this is not allowed so we check
        // an error
        // Lets add hot chocolate again and see what happens, we will change
        // status().isOk to test this
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().is( HttpStatus.CONFLICT.value() ) );

        // The standard post function
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe3 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        // Update the string
        recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Lets see that it has all three
        assertTrue( recipe.contains( recipe3.getName() ) );
        assertTrue( recipe.contains( recipe2.getName() ) );
        assertTrue( recipe.contains( recipe1.getName() ) );
        // And that these values exist
        assertTrue( recipe.contains( "price\":" + recipe3.getPrice() ) );
        assertTrue( recipe.contains( "coffee\":" + recipe3.getCoffee() ) );
        assertTrue( recipe.contains( "milk\":" + recipe3.getMilk() ) );
        assertTrue( recipe.contains( "sugar\":" + recipe3.getSugar() ) );
        assertTrue( recipe.contains( "chocolate\":" + recipe3.getChocolate() ) );

        // Finally we will create a drink that overloads the system since 3 is
        // max
        final RecipeDto futureDrink = new RecipeDto( 2L, "nnnnnn", 250, 5, 1, 0, 0 );

        // The standard post function
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
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
        // We can add three recipes, then delete them one by one and see what is
        // left
        final RecipeDto recipe1 = new RecipeDto( 1L, "Mocha", 300, 3, 3, 3, 3 );
        final RecipeDto recipe2 = new RecipeDto( 2L, "Coffee", 100, 1, 1, 1, 1 );
        final RecipeDto recipe3 = new RecipeDto( 3L, "Black", 200, 2, 2, 2, 2 );

        String recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Prove it starts empty
        assertEquals( recipe, "[]" );
        // We can add all three
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
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
        // Each recipe has 7 parts so we can find part 0, part 7, part 14
        ids[0] = parts[0];
        ids[1] = parts[7];
        ids[2] = parts[14];

        System.out.println( ids[0] );
        System.out.println( ids[1] );
        System.out.println( ids[2] );

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
        assertFalse( recipe.contains( "coffee\":" + recipe1.getCoffee() ) );
        assertFalse( recipe.contains( "milk\":" + recipe1.getMilk() ) );
        assertFalse( recipe.contains( "sugar\":" + recipe1.getSugar() ) );
        assertFalse( recipe.contains( "chocolate\":" + recipe1.getChocolate() ) );

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
        assertFalse( recipe.contains( "coffee\":" + recipe3.getCoffee() ) );
        assertFalse( recipe.contains( "milk\":" + recipe3.getMilk() ) );
        assertFalse( recipe.contains( "sugar\":" + recipe3.getSugar() ) );
        assertFalse( recipe.contains( "chocolate\":" + recipe3.getChocolate() ) );

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
        assertFalse( recipe.contains( "coffee\":" + recipe2.getCoffee() ) );
        assertFalse( recipe.contains( "milk\":" + recipe2.getMilk() ) );
        assertFalse( recipe.contains( "sugar\":" + recipe2.getSugar() ) );
        assertFalse( recipe.contains( "chocolate\":" + recipe2.getChocolate() ) );

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

        // This time we can just add three recipes and get each one of them
        final RecipeDto recipe1 = new RecipeDto( 1L, "Mocha", 300, 3, 3, 3, 3 );
        final RecipeDto recipe2 = new RecipeDto( 2L, "Coffee", 100, 1, 1, 1, 1 );
        final RecipeDto recipe3 = new RecipeDto( 3L, "Black", 200, 2, 2, 2, 2 );

        final String recipe = mvc.perform( get( "/api/recipes" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // Prove it starts empty
        assertEquals( recipe, "[]" );
        // We can add all three
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe2 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );
        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe3 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        // Now we will get each one of them separately
        final String recipe1String = mvc.perform( get( "/api/recipes/" + recipe1.getName() ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Make sure it is the right one by name
        assertTrue( recipe1String.contains( "Mocha" ) );
        assertFalse( recipe1String.contains( "Coffee" ) );
        assertFalse( recipe1String.contains( "Black" ) );
        // We can check it is all the other right parts too
        assertTrue( recipe1String.contains( "price\":" + recipe1.getPrice() ) );
        assertTrue( recipe1String.contains( "coffee\":" + recipe1.getCoffee() ) );
        assertTrue( recipe1String.contains( "milk\":" + recipe1.getMilk() ) );
        assertTrue( recipe1String.contains( "sugar\":" + recipe1.getSugar() ) );
        assertTrue( recipe1String.contains( "chocolate\":" + recipe1.getChocolate() ) );

        // Then we can do the same for the other two
        final String recipe2String = mvc.perform( get( "/api/recipes/" + recipe2.getName() ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Make sure it is the right one by name
        assertFalse( recipe2String.contains( "Mocha" ) );
        assertTrue( recipe2String.contains( "Coffee" ) );
        assertFalse( recipe2String.contains( "Black" ) );
        // We can check it is all the other right parts too
        assertTrue( recipe2String.contains( "price\":" + recipe2.getPrice() ) );
        assertTrue( recipe2String.contains( "coffee\":" + recipe2.getCoffee() ) );
        assertTrue( recipe2String.contains( "milk\":" + recipe2.getMilk() ) );
        assertTrue( recipe2String.contains( "sugar\":" + recipe2.getSugar() ) );
        assertTrue( recipe2String.contains( "chocolate\":" + recipe2.getChocolate() ) );

        final String recipe3String = mvc.perform( get( "/api/recipes/" + recipe3.getName() ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Make sure it is the right one by name
        assertFalse( recipe3String.contains( "Mocha" ) );
        assertFalse( recipe3String.contains( "Coffee" ) );
        assertTrue( recipe3String.contains( "Black" ) );
        // We can check it is all the other right parts too
        assertTrue( recipe3String.contains( "price\":" + recipe3.getPrice() ) );
        assertTrue( recipe3String.contains( "coffee\":" + recipe3.getCoffee() ) );
        assertTrue( recipe3String.contains( "milk\":" + recipe3.getMilk() ) );
        assertTrue( recipe3String.contains( "sugar\":" + recipe3.getSugar() ) );
        assertTrue( recipe3String.contains( "chocolate\":" + recipe3.getChocolate() ) );
    }
}
