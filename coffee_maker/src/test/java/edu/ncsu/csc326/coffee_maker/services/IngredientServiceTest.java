package edu.ncsu.csc326.coffee_maker.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import jakarta.transaction.Transactional;

@SpringBootTest
public class IngredientServiceTest {

    @Autowired
    private IngredientService ingredientService;

    @BeforeEach
    public void setUp () throws Exception {
        ingredientService.deleteAllIngredients();
    }

    @Test
    @Transactional
    public void testCreateIngredient () {

        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );

        final IngredientDto createdIngredient1 = ingredientService.createIngredient( ingredient1 );
        assertAll( "Ingredient contents", () -> assertEquals( "Coffee", createdIngredient1.getName() ),
                () -> assertEquals( 5, createdIngredient1.getAmount() ) );

        final IngredientDto ingredient2 = new IngredientDto( "Sweet Tea", 10 );
        final IngredientDto createdIngredient2 = ingredientService.createIngredient( ingredient2 );
        assertAll( "Ingredient contents", () -> assertEquals( "Sweet Tea", createdIngredient2.getName() ),
                () -> assertEquals( 10, createdIngredient2.getAmount() ) );
    }

    @Test
    @Transactional
    public void testGetIngredientById () {

        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );

        final IngredientDto createdIngredient1 = ingredientService.createIngredient( ingredient1 );
        final IngredientDto fetchedIngredient1 = ingredientService.getIngredientById( createdIngredient1.getId() );
        assertAll( "Ingredient contents", () -> assertEquals( "Coffee", fetchedIngredient1.getName() ),
                () -> assertEquals( 5, fetchedIngredient1.getAmount() ) );

        final IngredientDto ingredient2 = new IngredientDto( "Sweet Tea", 10 );
        final IngredientDto createdIngredient2 = ingredientService.createIngredient( ingredient2 );
        final IngredientDto fetchedIngredient2 = ingredientService.getIngredientById( createdIngredient2.getId() );
        assertAll( "Ingredient contents", () -> assertEquals( "Sweet Tea", fetchedIngredient2.getName() ),
                () -> assertEquals( 10, fetchedIngredient2.getAmount() ) );

        final Exception e = assertThrows( Exception.class, () -> ingredientService.getIngredientById( 100000L ) );
        assertEquals( e.getMessage(), "Ingredient does not exist with id 100000" );
    }

    @Test
    @Transactional
    public void testGetIngredientByName () {

        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );

        final IngredientDto createdIngredient1 = ingredientService.createIngredient( ingredient1 );
        final IngredientDto fetchedIngredient1 = ingredientService.getIngredientByName( createdIngredient1.getName() );
        assertAll( "Ingredient contents", () -> assertEquals( "Coffee", fetchedIngredient1.getName() ),
                () -> assertEquals( 5, fetchedIngredient1.getAmount() ) );

        final IngredientDto ingredient2 = new IngredientDto( "Sweet Tea", 10 );
        final IngredientDto createdIngredient2 = ingredientService.createIngredient( ingredient2 );
        final IngredientDto fetchedIngredient2 = ingredientService.getIngredientByName( createdIngredient2.getName() );
        assertAll( "Ingredient contents", () -> assertEquals( "Sweet Tea", fetchedIngredient2.getName() ),
                () -> assertEquals( 10, fetchedIngredient2.getAmount() ) );

        final Exception e = assertThrows( Exception.class, () -> ingredientService.getIngredientByName( "Not real" ) );
        assertEquals( e.getMessage(), "Ingredient does not exist with name Not real" );
    }

    @Test
    @Transactional
    public void testGetAllIngredients () {

        List<IngredientDto> allIngredients = ingredientService.getAllIngredients();

        // At the start there are no ingredients
        assertEquals( allIngredients, new ArrayList<>() );

        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );
        ingredientService.createIngredient( ingredient1 );

        allIngredients = ingredientService.getAllIngredients();
        // We have added an ingredient lets see if it is there
        assertEquals( allIngredients.size(), 1 );
        // And check that it is the right one
        assertEquals( allIngredients.get( 0 ).getName(), "Coffee" );
        assertEquals( allIngredients.get( 0 ).getAmount(), 5 );

        final IngredientDto ingredient2 = new IngredientDto( "Sweet Tea", 10 );
        ingredientService.createIngredient( ingredient2 );

        allIngredients = ingredientService.getAllIngredients();
        // We have added an ingredient lets see if it is there
        assertEquals( allIngredients.size(), 2 );
        // And check that it is the right one
        assertEquals( allIngredients.get( 0 ).getName(), "Coffee" );
        assertEquals( allIngredients.get( 0 ).getAmount(), 5 );
        assertEquals( allIngredients.get( 1 ).getName(), "Sweet Tea" );
        assertEquals( allIngredients.get( 1 ).getAmount(), 10 );

    }

    @Test
    @Transactional
    public void testDeleteIngredient () {

        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );
        final IngredientDto createdIngredient1 = ingredientService.createIngredient( ingredient1 );
        final IngredientDto ingredient2 = new IngredientDto( "Sweet Tea", 10 );
        final IngredientDto createdIngredient2 = ingredientService.createIngredient( ingredient2 );

        List<IngredientDto> allIngredients = ingredientService.getAllIngredients();
        // We have added an ingredient lets see if it is there
        assertEquals( allIngredients.size(), 2 );
        // And check that it is the right one
        assertEquals( allIngredients.get( 0 ).getName(), "Coffee" );
        assertEquals( allIngredients.get( 0 ).getAmount(), 5 );
        assertEquals( allIngredients.get( 1 ).getName(), "Sweet Tea" );
        assertEquals( allIngredients.get( 1 ).getAmount(), 10 );

        // Lets delete sweet tea
        ingredientService.deleteIngredient( createdIngredient2.getId() );

        // We can see that it was removed by checking the list
        allIngredients = ingredientService.getAllIngredients();
        // We have added an ingredient lets see if it is there
        assertEquals( allIngredients.size(), 1 );
        // And check that it is the right one
        assertEquals( allIngredients.get( 0 ).getName(), "Coffee" );
        assertEquals( allIngredients.get( 0 ).getAmount(), 5 );

        // Additionally, we can see it is gone because trying to remove it again
        // results in an error
        final Exception e = assertThrows( Exception.class,
                () -> ingredientService.deleteIngredient( createdIngredient2.getId() ) );
        assertEquals( e.getMessage(), "Ingredient does not exist with id " + createdIngredient2.getId() );

    }

    @Test
    @Transactional
    public void testUpdateIngredient () {

        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );
        final IngredientDto createdIngredient1 = ingredientService.createIngredient( ingredient1 );

        List<IngredientDto> allIngredients = ingredientService.getAllIngredients();
        // We have added an ingredient lets see if it is there
        assertEquals( allIngredients.size(), 1 );
        // And check that it is the right one
        assertEquals( allIngredients.get( 0 ).getName(), "Coffee" );
        assertEquals( allIngredients.get( 0 ).getAmount(), 5 );

        final IngredientDto desiredChange = new IngredientDto( "Sweet Tea", 100 );

        // update the ingredient to sweet tea
        ingredientService.updateIngredient( createdIngredient1.getId(), desiredChange );

        // We can see that it was removed by checking the list
        allIngredients = ingredientService.getAllIngredients();
        // We have added an ingredient lets see if it is there
        assertEquals( allIngredients.size(), 1 );
        // And check that the update was successful
        assertEquals( allIngredients.get( 0 ).getName(), "Sweet Tea" );
        assertEquals( allIngredients.get( 0 ).getAmount(), 100 );

        // Additionally, we can see it is gone because trying to remove it again
        // results in an error
        final Exception e = assertThrows( Exception.class,
                () -> ingredientService.updateIngredient( 10000L, desiredChange ) );
        assertEquals( e.getMessage(), "Ingredient does not exist with id 10000" );

    }

    @Test
    @Transactional
    public void testUpdateAllIngredients () {
        final IngredientDto ingredient1 = new IngredientDto( "Coffee", 5 );
        ingredientService.createIngredient( ingredient1 );
        final IngredientDto ingredient2 = new IngredientDto( "Sugar", 0 );
        ingredientService.createIngredient( ingredient2 );
        final IngredientDto ingredient3 = new IngredientDto( "Honey", 3 );
        ingredientService.createIngredient( ingredient3 );
        final IngredientDto ingredient4 = new IngredientDto( "Cream", 10 );
        ingredientService.createIngredient( ingredient4 );

        List<IngredientDto> allIngredients = ingredientService.getAllIngredients();

        // We have added ingredients lets see if they are there
        assertEquals( allIngredients.size(), 4 );
        // And check that values are correct
        assertEquals( allIngredients.get( 0 ).getName(), "Coffee" );
        assertEquals( allIngredients.get( 0 ).getAmount(), 5 );
        assertEquals( allIngredients.get( 1 ).getName(), "Sugar" );
        assertEquals( allIngredients.get( 1 ).getAmount(), 0 );
        assertEquals( allIngredients.get( 2 ).getName(), "Honey" );
        assertEquals( allIngredients.get( 2 ).getAmount(), 3 );
        assertEquals( allIngredients.get( 3 ).getName(), "Cream" );
        assertEquals( allIngredients.get( 3 ).getAmount(), 10 );

        final List<IngredientDto> desiredChanges = new ArrayList<IngredientDto>();
        desiredChanges.add( new IngredientDto( "Coffee", 20 ) );
        desiredChanges.add( new IngredientDto( "Sugar", 10 ) );
        desiredChanges.add( new IngredientDto( "Honey", 5 ) );
        desiredChanges.add( new IngredientDto( "Cream", 10 ) );

        // Update our ingredients
        final List<IngredientDto> savedChanges = ingredientService.updateAllIngredients( desiredChanges );

        // Ensure that the changes happened for the returned list
        for ( int i = 0; i < 4; i++ ) {
            assertEquals( savedChanges.get( i ).getName(), desiredChanges.get( i ).getName() );
            assertEquals( savedChanges.get( i ).getAmount(), desiredChanges.get( i ).getAmount() );
        }

        // Ensure that the changes are saved in the database
        allIngredients = ingredientService.getAllIngredients();

        for ( int i = 0; i < 4; i++ ) {
            assertEquals( allIngredients.get( i ).getName(), desiredChanges.get( i ).getName() );
            assertEquals( allIngredients.get( i ).getAmount(), desiredChanges.get( i ).getAmount() );
        }

        // Try with only 2 ingredients in the list

        final List<IngredientDto> desiredChanges2 = new ArrayList<IngredientDto>();
        desiredChanges2.add( new IngredientDto( "Cream", 0 ) );
        desiredChanges2.add( new IngredientDto( "Coffee", 30 ) );

        // Update our ingredients
        final List<IngredientDto> savedChanges2 = ingredientService.updateAllIngredients( desiredChanges2 );

        // Ensure that the changes happened for the returned list
        assertEquals( savedChanges2.get( 0 ).getName(), desiredChanges2.get( 0 ).getName() );
        assertEquals( savedChanges2.get( 0 ).getAmount(), desiredChanges2.get( 0 ).getAmount() );
        assertEquals( savedChanges2.get( 1 ).getName(), desiredChanges2.get( 1 ).getName() );
        assertEquals( savedChanges2.get( 1 ).getAmount(), desiredChanges2.get( 1 ).getAmount() );

        // Ensure that the changes are saved in the database
        allIngredients = ingredientService.getAllIngredients();

        assertEquals( allIngredients.get( 3 ).getName(), desiredChanges2.get( 0 ).getName() );
        assertEquals( allIngredients.get( 3 ).getAmount(), desiredChanges2.get( 0 ).getAmount() );
        assertEquals( allIngredients.get( 0 ).getName(), desiredChanges2.get( 1 ).getName() );
        assertEquals( allIngredients.get( 0 ).getAmount(), desiredChanges2.get( 1 ).getAmount() );

        // Now try with a non-existant ingredient
        final List<IngredientDto> invalidChanges = new ArrayList<IngredientDto>();
        invalidChanges.add( new IngredientDto( "Butter", 20 ) );

        final Exception e = assertThrows( Exception.class,
                () -> ingredientService.updateAllIngredients( invalidChanges ) );
        assertEquals( e.getMessage(), "Ingredient does not exist with name Butter" );
    }

}