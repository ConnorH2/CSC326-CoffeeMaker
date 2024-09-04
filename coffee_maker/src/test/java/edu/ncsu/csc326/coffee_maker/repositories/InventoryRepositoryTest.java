package edu.ncsu.csc326.coffee_maker.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * Tests InventoryRepository.  Uses the real database - not an embedded one.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class InventoryRepositoryTest {
	
	/** Reference to inventory repository */
	@Autowired
	private InventoryRepository inventoryRepository;
	
	/** Reference to EntityManager */
	@Autowired
	private TestEntityManager testEntityManager;
	
	/** Reference to inventory */
	private Inventory inventory;

	/**
	 * Sets up the test case.  We assume only one inventory row.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
		EntityManager entityManager = testEntityManager.getEntityManager();
		Query query = entityManager.createNativeQuery("TRUNCATE TABLE inventory");
		query.executeUpdate();
		
		//Make sure that Inventory always has an id of 1L.
		inventory = new Inventory(1L, 20, 14, 32, 10);
		inventoryRepository.save(inventory);
	}


	/**
	 * Test saving the inventory and retrieving from the repository.
	 */
	@Test
	public void testSaveAndGetInventory() {
		Inventory fetchedInventory = inventoryRepository.findById(1L).get();
		assertAll("Inventory contents",
				() -> assertEquals(1L, fetchedInventory.getId()),
				() -> assertEquals(20, fetchedInventory.getCoffee()),
				() -> assertEquals(14, fetchedInventory.getMilk()),
				() -> assertEquals(32, fetchedInventory.getSugar()),
				() -> assertEquals(10, fetchedInventory.getChocolate()));
	}
	
	/**
	 * Tests updating the inventory
	 */
	@Test
	public void testUpdateInventory() {
		Inventory fetchedInventory = inventoryRepository.findById(1L).get();
		fetchedInventory.setCoffee(13);
		fetchedInventory.setMilk(14);
		fetchedInventory.setSugar(27);
		fetchedInventory.setChocolate(23);
		
		Inventory updatedInventory = inventoryRepository.save(fetchedInventory);
		assertAll("Inventory contents",
				() -> assertEquals(1L, updatedInventory.getId()),
				() -> assertEquals(13, updatedInventory.getCoffee()),
				() -> assertEquals(14, updatedInventory.getMilk()),
				() -> assertEquals(27, updatedInventory.getSugar()),
				() -> assertEquals(23, updatedInventory.getChocolate()));
	}
}
