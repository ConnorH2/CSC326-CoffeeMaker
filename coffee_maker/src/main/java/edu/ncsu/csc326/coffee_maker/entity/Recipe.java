package edu.ncsu.csc326.coffee_maker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Recipe for the coffee maker. Recipe is a Data Access Object (DAO) is tied to the database using
 * Hibernate libraries. RecipeRepository provides the methods for database CRUD operations.
 */
@Entity
@Table(name = "recipes")
public class Recipe {

    /** Recipe id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;

    /** Recipe name */
    private String name;

    /** Recipe price */
    private Integer price;

    /** Amount coffee */
    private Integer coffee;

    /** Amount milk */
    private Integer milk;

    /** Amount sugar */
    private Integer sugar;

    /** Amount chocolate */
    private Integer chocolate;
    
    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
    }
    
    /**
     * Creates a recipe from all the fields
     */
    public Recipe(Long id, String name, Integer price, Integer coffee, Integer milk, Integer sugar, Integer chocolate) {
    	this.id = id;
    	this.name = name;
    	this.price = price;
    	this.coffee = coffee;
    	this.milk = milk;
    	this.sugar = sugar;
    	this.chocolate = chocolate;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    public Long getId() {
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
     * Returns amount of chocolate in the recipe.
     *
     * @return Returns the amtChocolate.
     */
    public Integer getChocolate () {
        return chocolate;
    }

    /**
     * Sets the amount of chocolate in the recipe.
     *
     * @param chocolate
     *            The amtChocolate to set.
     */
    public void setChocolate ( final Integer chocolate ) {
        this.chocolate = chocolate;
    }

    /**
     * Returns amount of coffee in the recipe.
     *
     * @return Returns the amtCoffee.
     */
    public Integer getCoffee () {
        return coffee;
    }

    /**
     * Sets the amount of coffee in the recipe.
     *
     * @param coffee
     *            The amtCoffee to set.
     */
    public void setCoffee ( final Integer coffee ) {
        this.coffee = coffee;
    }

    /**
     * Returns amount of milk in the recipe.
     *
     * @return Returns the amtMilk.
     */
    public Integer getMilk () {
        return milk;
    }

    /**
     * Sets the amount of milk in the recipe.
     *
     * @param milk
     *            The amtMilk to set.
     */
    public void setMilk ( final Integer milk ) {
        this.milk = milk;
    }

    /**
     * Returns amount of sugar in the recipe.
     *
     * @return Returns the amtSugar.
     */
    public Integer getSugar () {
        return sugar;
    }

    /**
     * Sets the amount of sugar in the recipe.
     *
     * @param sugar
     *            The amtSugar to set.
     */
    public void setSugar ( final Integer sugar ) {
        this.sugar = sugar;
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

}
