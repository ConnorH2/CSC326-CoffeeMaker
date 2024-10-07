package edu.ncsu.csc326.coffee_maker.entity;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

/**
 * Recipe for the coffee maker. Recipe is a Data Access Object (DAO) is tied to
 * the database using Hibernate libraries. RecipeRepository provides the methods
 * for database CRUD operations.
 */
@Entity
@Table ( name = "recipes" )
public class Recipe {

    /** Recipe id */
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long                id;

    /** Recipe name */
    private String              name;

    /** Recipe price */
    private Integer             price;

    /** List of recipes **/
    @ElementCollection ( fetch = FetchType.EAGER )
    @CollectionTable ( name = "recipes_ingredients", joinColumns = @JoinColumn ( name = "recipe_id" ) )
    private List<IngredientDto> ingredients = new ArrayList<IngredientDto>();

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
    }

    /**
     * Creates a recipe from all the fields
     *
     * @param id
     *            the id of the recipe
     * @param name
     *            the name of the recipe
     * @param price
     *            the price of the recipe
     */
    public Recipe ( final Long id, final String name, final Integer price, final List<IngredientDto> ingredients ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /*
     * Gets the list of ingredients in this recipe
     * @return list of the recipe's ingredients
     */
    public List<IngredientDto> getIngredients () {
        return ingredients;
    }

    /*
     * Sets the list of ingredients in this recipe
     * @return list of the recipe's ingredients
     */
    public void setIngredients ( final List<IngredientDto> ingredients ) {
        this.ingredients = ingredients;
    }
}