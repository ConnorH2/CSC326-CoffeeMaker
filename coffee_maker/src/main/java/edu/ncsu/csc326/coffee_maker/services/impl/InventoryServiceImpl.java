package edu.ncsu.csc326.coffee_maker.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.mapper.InventoryMapper;
import edu.ncsu.csc326.coffee_maker.repositories.InventoryRepository;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;

/**
 * Implementation of the InventoryService interface.
 */
@Service
public class InventoryServiceImpl implements InventoryService {
	
	/** Connection to the repository to work with the DAO + database */
	@Autowired
	private InventoryRepository inventoryRepository;

	/**
	 * Creates the inventory.
	 * @param inventoryDto inventory to create
	 * @return updated inventory after creation
	 */
	@Override
	public InventoryDto createInventory(InventoryDto inventoryDto) {
		Inventory inventory = InventoryMapper.mapToInventory(inventoryDto);
		Inventory savedInventory = inventoryRepository.save(inventory);
		return InventoryMapper.mapToInventoryDto(savedInventory);
	}

	/**
	 * Returns the single inventory.
	 * @return the single inventory
	 */
	@Override
	public InventoryDto getInventory() {
		List<Inventory> inventory = inventoryRepository.findAll();
		if (inventory.size() == 0) {
			InventoryDto newInventoryDto = new InventoryDto();
			newInventoryDto.setCoffee(0);
			newInventoryDto.setMilk(0);
			newInventoryDto.setSugar(0);
			newInventoryDto.setChocolate(0);
			
			InventoryDto savedInventoryDto = createInventory(newInventoryDto);
			return savedInventoryDto;
		}
		return InventoryMapper.mapToInventoryDto(inventory.get(0));
	}

	/**
	 * Updates the contents of the inventory.
	 * @param inventoryDto values to update
	 * @return updated inventory
	 */
	@Override
	public InventoryDto updateInventory(InventoryDto inventoryDto) {
		Inventory inventory = inventoryRepository.findById(1L).orElseThrow(
				() -> new ResourceNotFoundException("Inventory does not exist with id of " + inventoryDto.getId()) 
		);
				
		inventory.setCoffee(inventoryDto.getCoffee());
		inventory.setMilk(inventoryDto.getMilk());
		inventory.setSugar(inventoryDto.getSugar());
		inventory.setChocolate(inventoryDto.getChocolate());
		
		Inventory savedInventory = inventoryRepository.save(inventory);
		
		return InventoryMapper.mapToInventoryDto(savedInventory);
	}


}
