package edu.ncsu.csc326.coffee_maker.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

/**
 * An empty interface that tells Spring that we are interested in storing Ingredient
 * objects in the corresponding table, each uniquely identified by a Long.
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    /**
     * Finds an Ingredient object with the provided name. Spring will generate code
     * to make this happen.  Optional let's us call .orElseThrow() when a client
     * works with the method and the value isn't found in the database.
     *
     * @param name
     *            Name of the recipe
     * @return Found recipe, null if none.
     */
    Optional<Ingredient> findByName(String name);
}
