package edu.ncsu.csc326.coffee_maker.dto;


/**
 * Used to transfer Inventory data between the client and server.  
 * This class will serve as the response in the REST API.
 */
public class InventoryDto {
	
	/** id for inventory entry */
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
     * Default InventoryDto constructor.
     */
    public InventoryDto() {
    	
    }
    
    /**
     * Constructs an InventoryDto object from field values.
     * @param id inventory id
     * @param coffee amount coffee in inventory
     * @param milk amount milk in inventory
     * @param sugar amount sugar in inventory
     * @param chocolate amount chocolate in inventory
     */
	public InventoryDto(Long id, Integer coffee, Integer milk, Integer sugar, Integer chocolate) {
		super();
		this.id = id;
		this.coffee = coffee;
		this.milk = milk;
		this.sugar = sugar;
		this.chocolate = chocolate;
	}

	/**
	 * Gets the inventory id.
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Inventory id to set.
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
