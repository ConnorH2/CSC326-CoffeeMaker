package edu.ncsu.csc326.coffee_maker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;

/**
 * Controller class for handling HTTP requests related to ingredients. Provides
 * end points for creating, retrieving, updating, and deleting ingredients.
 */
@CrossOrigin ( "*" )
@RestController
@RequestMapping ( "/api/ingredients" )
public class IngredientController {

    /** The service that lets us interact with ingredientRepository */
    @Autowired
    private IngredientService ingredientService;

    /**
     * Retrieves an ingredient by its unique ID.
     *
     * @param id
     *            the ID of the ingredient to retrieve
     * @return ResponseEntity containing the IngredientDto if found
     */
    @GetMapping ( "{id}" )
    public ResponseEntity<IngredientDto> getIngredient ( @PathVariable ( "id" ) final Long id ) {
        final IngredientDto ingredientDto = ingredientService.getIngredientById( id );
        return ResponseEntity.ok( ingredientDto );
    }

    /**
     * Retrieves an ingredient by its name.
     *
     * @param name
     *            the name of the ingredient to retrieve
     * @return ResponseEntity containing the IngredientDto if found
     */
    @GetMapping ( "/name/{name}" )
    public ResponseEntity<IngredientDto> getIngredientByName ( @PathVariable ( "name" ) final String name ) {
        final IngredientDto ingredientDto = ingredientService.getIngredientByName( name );
        return ResponseEntity.ok( ingredientDto );
    }

    /**
     * Retrieves a list of all ingredients.
     *
     * @return ResponseEntity containing the list of all IngredientDto objects
     */
    @GetMapping
    public List<IngredientDto> getAllIngredients () {
        return ingredientService.getAllIngredients();
    }

    /**
     * Deletes an ingredient by its unique ID.
     *
     * @param id
     *            the ID of the ingredient to delete
     * @return ResponseEntity with a success message
     */
    @DeleteMapping ( "{id}" )
    public ResponseEntity<String> deleteIngredient ( @PathVariable ( "id" ) final Long id ) {
        ingredientService.deleteIngredient( id );
        return ResponseEntity.ok( "Ingredient successfully deleted" );
    }

    /**
     * Deletes all ingredients from the system.
     *
     * @return ResponseEntity with a success message
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAll () {
        ingredientService.deleteAllIngredients();
        return ResponseEntity.ok( "All ingredients successfully deleted" );
    }

    /**
     * Creates a new ingredient.
     *
     * @param ingredientDto
     *            the data transfer object containing ingredient details
     * @return ResponseEntity containing the created IngredientDto
     */
    @PostMapping
    public ResponseEntity<IngredientDto> createIngredient ( @RequestBody final IngredientDto ingredientDto ) {
        final IngredientDto savedIngredientDto = ingredientService.createIngredient( ingredientDto );
        return ResponseEntity.ok( savedIngredientDto );
    }

    /**
     * Updates an existing ingredient.
     *
     * @param id
     *            the ID of the ingredient to update
     * @param ingredientDto
     *            the IngredientDto containing updated information
     * @return ResponseEntity containing the updated IngredientDto
     */
    @PutMapping ( "{id}" )
    public ResponseEntity<IngredientDto> updateIngredient ( @PathVariable ( "id" ) final Long id,
            @RequestBody final IngredientDto ingredientDto ) {
        final IngredientDto updatedIngredientDto = ingredientService.updateIngredient( id, ingredientDto );
        return ResponseEntity.ok( updatedIngredientDto );
    }

    /**
     * Updates all ingredients in the system.
     *
     * @param ingredientList
     *            all ingredients in the system
     * @return response to the request
     */
    @PutMapping
    public List<IngredientDto> updateAllIngredients ( @RequestBody final List<IngredientDto> ingredientList ) {
        // If this doesn't work check
        // https://stackoverflow.com/questions/35266289/how-to-get-list-of-objects-via-requestbody-in-spring-boot-api
        return ingredientService.updateAllIngredients( ingredientList );
    }

}
