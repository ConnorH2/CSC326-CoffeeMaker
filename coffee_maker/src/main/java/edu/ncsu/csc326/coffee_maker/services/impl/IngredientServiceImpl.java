package edu.ncsu.csc326.coffee_maker.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.mapper.IngredientMapper;
import edu.ncsu.csc326.coffee_maker.repositories.IngredientRepository;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public IngredientDto createIngredient ( final IngredientDto ingredientDto ) {
        final Ingredient ingredient = IngredientMapper.mapToIngredient( ingredientDto );
        final Ingredient savedIngredient = ingredientRepository.save( ingredient );
        return IngredientMapper.mapToIngredientDto( savedIngredient );
    }

    @Override
    public IngredientDto getIngredientById ( final Long ingredientId ) {
        final Ingredient ingredient = ingredientRepository.findById( ingredientId ).orElseThrow(
                () -> new ResourceNotFoundException( "Ingredient does not exist with id " + ingredientId ) );
        return IngredientMapper.mapToIngredientDto( ingredient );
    }

    @Override
    public List<IngredientDto> getAllIngredients () {
        final List<Ingredient> ingredients = ingredientRepository.findAll();
        if ( ingredients.size() == 0 ) {
            // Have to return an empty list instead of null to fix the front end error.
            return new ArrayList<>();
        }
        return ingredients.stream().map( ( ingredient ) -> IngredientMapper.mapToIngredientDto( ingredient ) )
                .collect( Collectors.toList() );
    }

    @Override
    public void deleteIngredient ( final Long ingredientId ) {
        final Ingredient ingredient = ingredientRepository.findById( ingredientId ).orElseThrow(
                () -> new ResourceNotFoundException( "Ingredient does not exist with id " + ingredientId ) );

        ingredientRepository.delete( ingredient );
    }

    @Override
    public void deleteAllIngredients () {
        ingredientRepository.deleteAll();
    }

    @Override
    public IngredientDto getIngredientByName ( final String ingredientName ) {
        final Ingredient ingredient = ingredientRepository.findByName( ingredientName ).orElseThrow(
                () -> new ResourceNotFoundException( "Ingredient does not exist with name " + ingredientName ) );
        return IngredientMapper.mapToIngredientDto( ingredient );
    }

    @Override
    public IngredientDto updateIngredient ( final Long ingredientId, final IngredientDto ingredient ) {
        final Ingredient foundIngredient = ingredientRepository.findById( ingredientId ).orElseThrow(
                () -> new ResourceNotFoundException( "Ingredient does not exist with id " + ingredientId ) );

        foundIngredient.setName( ingredient.getName() );
        foundIngredient.setAmount( ingredient.getAmount() );

        final Ingredient savedIngredient = ingredientRepository.save( foundIngredient );

        return IngredientMapper.mapToIngredientDto( savedIngredient );
    }

    @Override
    public List<IngredientDto> updateAllIngredients ( final List<IngredientDto> ingredientList ) {
        final List<IngredientDto> savedIngredientList = new ArrayList<IngredientDto>();

        // Iterate over all ingredients, changing all ingredients with matching
        // names
        for ( int i = 0; i < ingredientList.size(); i++ ) {
            final Ingredient ingredient = IngredientMapper.mapToIngredient( ingredientList.get( i ) );

            final Ingredient foundIngredient = ingredientRepository.findByName( ingredient.getName() )
                    .orElseThrow( () -> new ResourceNotFoundException(
                            "Ingredient does not exist with name " + ingredient.getName() ) );

            foundIngredient.setAmount( ingredient.getAmount() );

            final Ingredient savedIngredient = ingredientRepository.save( foundIngredient );

            savedIngredientList.add( IngredientMapper.mapToIngredientDto( savedIngredient ) );
        }

        return savedIngredientList;
    }

}