package edu.ncsu.csc326.coffee_maker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * A class that represents an arbitrary ingredient for a recipe.
 */
@Entity
public class Ingredient {

    /** the id of the ingredient. */
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long   id;

    /** The name of the ingredient */
    private String name;

    /** The amount of the ingredient */
    private int    amount;

    /**
     * A default constructor for the ingredient
     */
    public Ingredient () {
        super();
    }

    /**
     * The primary constructor for the ingredient class
     *
     * @param name
     *            - the name of the ingredient
     * @param amount
     *            - the amount owned of the ingredient
     */
    public Ingredient ( final String name, final int amount ) {

        this.name = name;
        this.amount = amount;
    }

    /**
     * gets the id of the ingredient
     *
     * @return the id of the ingredient
     */
    public Long getId () {
        return id;
    }

    /**
     *
     * sets the id of the ingredient
     *
     * @param id
     *            the id of the ingredient
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * gets the name of the ingredient
     *
     * @return the name of the ingredient
     */
    public String getName () {
        return name;
    }

    /**
     * sets the name of the ingredient
     *
     * @param name
     *            the name of the ingredient
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * gets the amount of the ingredient
     *
     * @return the amount of the ingredient
     */
    public int getAmount () {
        return amount;
    }

    /**
     * sets the amount of the ingredient
     *
     * @param amount
     *            the amount of the ingredient
     */
    public void setAmount ( final int amount ) {
        this.amount = amount;
    }

}