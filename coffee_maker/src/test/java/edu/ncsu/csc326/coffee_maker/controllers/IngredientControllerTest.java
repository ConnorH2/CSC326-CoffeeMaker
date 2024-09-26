/**
 *
 */
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;

/**
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class IngredientControllerTest {

    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc           mvc;

    /** reference to ingredient service **/
    @Autowired
    private IngredientService ingredientService;

    /** set up environemnt **/
    @BeforeEach
    public void setUp () throws Exception {
        ingredientService.deleteAllIngredients();
    }

    @Test
    @Transactional
    void testCreateIngredient () throws Exception {
        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );

        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Coffee" ) );
    }

    @Test
    @Transactional
    void getAllIngredients () throws Exception {
        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );
        final IngredientDto ingredient2 = new IngredientDto( "Sweet Tea", 5 );

        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Coffee" ) );

        String ingredients = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // Check that it has the coffee but not the sweet tea
        assertTrue( ingredients.contains( "Coffee" ) );
        assertFalse( ingredients.contains( "Sweet Tea" ) );

        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Sweet Tea" ) );

        ingredients = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Check that we got both
        assertTrue( ingredients.contains( "Coffee" ) );
        assertTrue( ingredients.contains( "Sweet Tea" ) );
    }

    @Test
    @Transactional
    void getIngredientByName () throws Exception {
        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );
        final IngredientDto ingredient2 = new IngredientDto( "Sweet Tea", 5 );

        // Add both the ingredients
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Coffee" ) );
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Sweet Tea" ) );

        String ingredients = mvc
                .perform( get( "/api/ingredients/name/{name}", "Coffee" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Check that the ingredient retrieved was coffee not sweet tea
        assertTrue( ingredients.contains( "Coffee" ) );
        assertFalse( ingredients.contains( "Sweet Tea" ) );

        ingredients = mvc
                .perform( get( "/api/ingredients/name/{name}", "Sweet Tea" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Check that we got sweet tea, and that it is no longer coffee
        assertFalse( ingredients.contains( "Coffee" ) );
        assertTrue( ingredients.contains( "Sweet Tea" ) );
    }

    @Test
    @Transactional
    void getIngredientById () throws Exception {
        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );
        final IngredientDto ingredient2 = new IngredientDto( "Sweet Tea", 5 );

        // Add both the ingredients
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Coffee" ) );
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Sweet Tea" ) );

        final String i = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Now that they are all added I can do some magic to get the ids
        final String[] ids = new String[3];
        // returns an array where each part is like "id:14", we need the 14
        final String[] parts = i.split( "," );
        // Each recipe has 3 parts so we can find part 0, part 3, part 6
        ids[0] = parts[0];
        ids[1] = parts[3];

        System.out.println( ids[0] );
        System.out.println( ids[1] );

        // Then we need to remove the id part from each, always start with "id":
        ids[0] = ids[0].substring( 7, ids[0].length() );
        ids[1] = ids[1].substring( 6, ids[1].length() );

        String ingredients = mvc
                .perform( get( "/api/ingredients/{id}", ids[0] ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Check that the ingredient retrieved was coffee not sweet tea
        assertTrue( ingredients.contains( "Coffee" ) );
        assertFalse( ingredients.contains( "Sweet Tea" ) );

        ingredients = mvc
                .perform( get( "/api/ingredients/{id}", ids[1] ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Check that we got sweet tea, and that it is no longer coffee
        assertFalse( ingredients.contains( "Coffee" ) );
        assertTrue( ingredients.contains( "Sweet Tea" ) );
    }

    @Test
    @Transactional
    void deleteAndDeleteAll () throws Exception {
        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );
        final IngredientDto ingredient2 = new IngredientDto( "Sweet Tea", 5 );
        final IngredientDto ingredient3 = new IngredientDto( "Water", 5 );

        // Add both the ingredients
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Coffee" ) );
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Sweet Tea" ) );
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Water" ) );

        String i = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Now that they are all added I can do some magic to get the ids
        final String[] ids = new String[3];
        // returns an array where each part is like "id:14", we need the 14
        final String[] parts = i.split( "," );
        // Each recipe has 3 parts so we can find part 0, part 3, part 6
        ids[0] = parts[0];
        ids[1] = parts[3];

        // Then we need to remove the id part from each, always start with "id":
        ids[0] = ids[0].substring( 7, ids[0].length() );
        ids[1] = ids[1].substring( 6, ids[1].length() );

        // Remove Sweet Tea from the list
        String successMessage = mvc
                .perform( delete( "/api/ingredients/{id}", ids[1] ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( successMessage, "Ingredient successfully deleted" );

        i = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Check that the ingredient retrieved was coffee not sweet tea
        assertTrue( i.contains( "Water" ) );
        assertTrue( i.contains( "Coffee" ) );
        assertFalse( i.contains( "Sweet Tea" ) );

        // Remove all
        successMessage = mvc
                .perform( delete( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertEquals( successMessage, "All ingredients successfully deleted" );

        i = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Check that the ingredient retrieved was coffee not sweet tea
        assertFalse( i.contains( "Water" ) );
        assertFalse( i.contains( "Coffee" ) );
        assertFalse( i.contains( "Sweet Tea" ) );

    }

    @Test
    @Transactional
    void testUpdateIngredient () throws Exception {
        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );
        final IngredientDto ingredient2 = new IngredientDto( "Sweet Tea", 5 );
        final IngredientDto ingredient3 = new IngredientDto( "Water", 5 );

        // Add both the ingredients
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Coffee" ) );
        mvc.perform( post( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.name" ).value( "Sweet Tea" ) );

        String i = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Now that they are all added I can do some magic to get the ids
        final String[] ids = new String[3];
        // returns an array where each part is like "id:14", we need the 14
        final String[] parts = i.split( "," );
        // Each recipe has 3 parts so we can find part 0, part 3, part 6
        ids[0] = parts[0];
        ids[1] = parts[3];

        // Then we need to remove the id part from each, always start with "id":
        ids[0] = ids[0].substring( 7, ids[0].length() );
        ids[1] = ids[1].substring( 6, ids[1].length() );

        // Before the update, water is not in the list but sweet tea is, we will
        // change this
        assertFalse( i.contains( "Water" ) );
        assertTrue( i.contains( "Coffee" ) );
        assertTrue( i.contains( "Sweet Tea" ) );

        // Switch Tea to water by using tea's id and water's info
        mvc.perform( put( "/api/ingredients/{id}", ids[1] ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Check the new list of ingredients
        i = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // Check that the ingredient retrieved was coffee not sweet tea
        assertTrue( i.contains( "Water" ) );
        assertTrue( i.contains( "Coffee" ) );
        assertFalse( i.contains( "Sweet Tea" ) );

    }

    @Test
    @Transactional
    void testUpdateAllIngredients () throws Exception {
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

        final List<IngredientDto> desiredChanges = new ArrayList<IngredientDto>();
        desiredChanges.add( new IngredientDto( "Coffee", 22 ) );
        desiredChanges.add( new IngredientDto( "Sugar", 10 ) );
        desiredChanges.add( new IngredientDto( "Honey", 5 ) );
        desiredChanges.add( new IngredientDto( "Cream", 10 ) );

        // Execute the change

        mvc.perform( put( "/api/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( desiredChanges ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        ingredientStr = mvc.perform( get( "/api/ingredients" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        // check that the values are correct
        assertTrue( ingredientStr.contains( "\"name\":\"Coffee\",\"amount\":22" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Sugar\",\"amount\":10" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Honey\",\"amount\":5" ) );
        assertTrue( ingredientStr.contains( "\"name\":\"Cream\",\"amount\":10" ) );
    }

}
