package edu.ncsu.csc326.coffee_maker.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * Tests InventoryServiceImpl.
 */
@SpringBootTest
public class InventoryServiceTest {
	
	/** Reference to InventoryService (and InventoryServiceImpl). */
	@Autowired
	private InventoryService inventoryService;
	
	/** Reference to EntityManager */
	@Autowired
	private EntityManager entityManager;
	
	/**
	 * Sets up the test case.  We assume only one inventory row. Because inventory is
	 * treated as a singleton (only one row), we must truncate for 
	 * auto increment on the id to work correctly.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
		Query query = entityManager.createNativeQuery("TRUNCATE TABLE inventory");
		query.executeUpdate();
	}
	
	/**
	 * Tests InventoryService.createInventory().
	 */
	@Test
	@Transactional
	public void testCreateInventory() {
		InventoryDto inventoryDto = new InventoryDto(1L, 5, 9, 14, 23);
		
		InventoryDto createdInventoryDto = inventoryService.createInventory(inventoryDto);
		//Check contents of returned InventoryDto
		assertAll("InventoryDto contents",
				() -> assertEquals(1L, createdInventoryDto.getId()),
				() -> assertEquals(5, createdInventoryDto.getCoffee()),
				() -> assertEquals(9, createdInventoryDto.getMilk()),
				() -> assertEquals(14, createdInventoryDto.getSugar()),
				() -> assertEquals(23, createdInventoryDto.getChocolate()));
	}
	
	/**
	 * Tests InventoryService.updateInventory()
	 */
	@Test
	@Transactional
	public void testUpdateInventory() {
		InventoryDto inventoryDto = inventoryService.getInventory();
		
		inventoryDto.setCoffee(35);
		inventoryDto.setMilk(17);
		inventoryDto.setSugar(12);
		inventoryDto.setChocolate(14);
		
		InventoryDto updatedInventoryDto = inventoryService.updateInventory(inventoryDto);
		assertAll("InventoryDto contents",
				() -> assertEquals(1L, updatedInventoryDto.getId()),
				() -> assertEquals(35, updatedInventoryDto.getCoffee()),
				() -> assertEquals(17, updatedInventoryDto.getMilk()),
				() -> assertEquals(12, updatedInventoryDto.getSugar()),
				() -> assertEquals(14, updatedInventoryDto.getChocolate()));
	}
}
