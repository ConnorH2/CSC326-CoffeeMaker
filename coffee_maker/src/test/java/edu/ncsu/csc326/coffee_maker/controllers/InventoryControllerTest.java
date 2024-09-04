/**
 * 
 */
package edu.ncsu.csc326.coffee_maker.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * Test class for inventory controller
 */
@SpringBootTest
@AutoConfigureMockMvc
public class InventoryControllerTest {
		
	/** Mock MVC for testing controller */
	@Autowired
	private MockMvc mvc;
	
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
	 * Tests the GET /api/inventory endpoint.
	 * @throws Exception if issue when running the test.
	 */
	@Test
	@Transactional
	public void testGetInventory() throws Exception {
		InventoryDto expectedInventory = new InventoryDto(1L, 0, 0, 0, 0);
		
		mvc.perform(get("/api/inventory"))
				.andExpect(content().string(TestUtils.asJsonString(expectedInventory)))
				.andExpect(status().isOk());
	}

	/**
	 * Tests the PUT /api/inventory endpoint.
	 * @throws Exception if issue when running the test.
	 */
	@Test
	@Transactional
	public void testUpdateInventory() throws Exception {
		InventoryDto expectedInventory = new InventoryDto(1L, 0, 0, 0, 0);
		
		mvc.perform(get("/api/inventory"))
				.andExpect(content().string(TestUtils.asJsonString(expectedInventory)))
				.andExpect(status().isOk());
		
		InventoryDto updatedInventory = new InventoryDto(1L, 5, 10, 15, 20);
		
		mvc.perform(put("/api/inventory")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(updatedInventory))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.coffee").value("5"))
				.andExpect(jsonPath("$.milk").value("10"))
				.andExpect(jsonPath("$.sugar").value("15"))
				.andExpect(jsonPath("$.chocolate").value("20"));
				
	}
	
}
