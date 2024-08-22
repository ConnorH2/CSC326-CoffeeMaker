package edu.ncsu.csc326.coffee_maker.dto;

/**
 * Used to transfer Recipe data between the client and server.  
 * This class will serve as the response in the REST API.
 */
public class RecipeDto {

	/** Recipe Id */
    private Long    id;

    /** Recipe name */
    private String  name;

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
     * Default constructor for Recipe.
     */
    public RecipeDto() {
    	
    }
    
    /**
     * Creates recipe from field values.
     * @param id recipe's id
     * @param name recipe's name
     * @param price recipe's price
     * @param coffee recipe's amount coffee
     * @param milk recipe's amount milk
     * @param sugar recipe's amount sugar
     * @param chocolate recipe's amount chocolate
     */
	public RecipeDto(Long id, String name, Integer price, Integer coffee, Integer milk, Integer sugar,
			Integer chocolate) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.coffee = coffee;
		this.milk = milk;
		this.sugar = sugar;
		this.chocolate = chocolate;
	}

	/**
	 * Gets the recipe id.
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Recipe id to set.
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets recipe's name
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Recipe name to set.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the recipe's price
	 * @return the price
	 */
	public Integer getPrice() {
		return price;
	}

	/**
	 * Prices value to set.
	 * @param price the price to set
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}

	/**
	 * Gets the amount coffee.
	 * @return the coffee
	 */
	public Integer getCoffee() {
		return coffee;
	}

	/**
	 * Amount of coffee to set.
	 * @param coffee the coffee to set
	 */
	public void setCoffee(Integer coffee) {
		this.coffee = coffee;
	}

	/**
	 * Gets the amount milk.
	 * @return the milk
	 */
	public Integer getMilk() {
		return milk;
	}

	/**
	 * Amount of milk to set.
	 * @param milk the milk to set
	 */
	public void setMilk(Integer milk) {
		this.milk = milk;
	}

	/**
	 * Gets the amount sugar.
	 * @return the sugar
	 */
	public Integer getSugar() {
		return sugar;
	}

	/**
	 * Amount of sugar to set.
	 * @param sugar the sugar to set
	 */
	public void setSugar(Integer sugar) {
		this.sugar = sugar;
	}

	/**
	 * Gets the amount chocolate.
	 * @return the chocolate
	 */
	public Integer getChocolate() {
		return chocolate;
	}

	/**
	 * Amount of chocolate to set.
	 * @param chocolate the chocolate to set
	 */
	public void setChocolate(Integer chocolate) {
		this.chocolate = chocolate;
	}
}
