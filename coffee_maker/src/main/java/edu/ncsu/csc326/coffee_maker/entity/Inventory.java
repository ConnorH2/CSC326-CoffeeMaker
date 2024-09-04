package edu.ncsu.csc326.coffee_maker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Inventory for the coffee maker. Inventory is a Data Access Object (DAO) is tied to the database using
 * Hibernate libraries. InventoryRepository provides the methods for database CRUD operations.
 */
@Entity
public class Inventory {
	
	/** id for inventory entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    /** amount of coffee */    
    private Integer coffee;
    /** amount of milk */
    private Integer milk;
    /** amount of sugar */
    private Integer sugar;
    /** amount of chocolate */
    private Integer chocolate;
    
    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }
    
    /**
     * Creates an Inventory with all fields
     * @param id inventory's id
     * @param coffee inventory's amount coffee
     * @param milk inventory's amount milk
     * @param sugar inventory's amount sugar
     * @param chocolate inventory's amount chocolate
     */
    public Inventory(Long id, Integer coffee, Integer milk, Integer sugar, Integer chocolate) {
		super();
		this.id = id;
		this.coffee = coffee;
		this.milk = milk;
		this.sugar = sugar;
		this.chocolate = chocolate;
	}


    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the current number of chocolate units in the inventory.
     *
     * @return amount of chocolate
     */
    public Integer getChocolate () {
        return chocolate;
    }

    /**
     * Sets the number of chocolate units in the inventory to the specified
     * amount.
     *
     * @param amtChocolate
     *            amount of chocolate to set
     */
    public void setChocolate ( final Integer amtChocolate ) {
        if ( amtChocolate >= 0 ) {
            chocolate = amtChocolate;
        }
    }

    /**
     * Returns the current number of coffee units in the inventory.
     *
     * @return amount of coffee
     */
    public Integer getCoffee () {
        return coffee;
    }

    /**
     * Sets the number of coffee units in the inventory to the specified amount.
     *
     * @param amtCoffee
     *            amount of coffee to set
     */
    public void setCoffee ( final Integer amtCoffee ) {
        if ( amtCoffee >= 0 ) {
            coffee = amtCoffee;
        }
    }

    /**
     * Returns the current number of milk units in the inventory.
     *
     * @return int
     */
    public Integer getMilk () {
        return milk;
    }

    /**
     * Sets the number of milk units in the inventory to the specified amount.
     *
     * @param amtMilk
     *            amount of milk to set
     */
    public void setMilk ( final Integer amtMilk ) {
        if ( amtMilk >= 0 ) {
            milk = amtMilk;
        }
    }

    /**
     * Returns the current number of sugar units in the inventory.
     *
     * @return int
     */
    public Integer getSugar () {
        return sugar;
    }

    /**
     * Sets the number of sugar units in the inventory to the specified amount.
     *
     * @param amtSugar
     *            amount of sugar to set
     */
    public void setSugar ( final Integer amtSugar ) {
        if ( amtSugar >= 0 ) {
            sugar = amtSugar;
        }
    }

}
