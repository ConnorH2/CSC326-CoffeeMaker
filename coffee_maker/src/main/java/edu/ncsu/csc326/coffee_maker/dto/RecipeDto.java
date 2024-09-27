package edu.ncsu.csc326.coffee_maker.dto;

import java.util.List;

/**
 * Used to transfer Recipe data between the client and server. This class will
 * serve as the response in the REST API.
 */
public class RecipeDto {

    /** Recipe Id */
    private Long                id;

    /** Recipe name */
    private String              name;

    /** Recipe price */
    private Integer             price;

    /** List of recipes **/
    private List<IngredientDto> ingredients;

    /**
     * Default constructor for Recipe.
     */
    public RecipeDto () {

    }

    /**
     * Creates recipe from field values.
     *
     * @param id
     *            recipe's id
     * @param name
     *            recipe's name
     * @param price
     *            recipe's price
     * @param coffee
     *            recipe's amount coffee
     * @param milk
     *            recipe's amount milk
     * @param sugar
     *            recipe's amount sugar
     * @param chocolate
     *            recipe's amount chocolate
     */
    public RecipeDto ( final Long id, final String name, final Integer price, final List<IngredientDto> ingredients ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    /**
     * Gets the recipe id.
     *
     * @return the id
     */
    public Long getId () {
        return id;
    }

    /**
     * Recipe id to set.
     *
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Gets recipe's name
     *
     * @return the name
     */
    public String getName () {
        return name;
    }

    /**
     * Recipe name to set.
     *
     * @param name
     *            the name to set
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Gets the recipe's price
     *
     * @return the price
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Prices value to set.
     *
     * @param price
     *            the price to set
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
