import React, { useEffect, useState } from 'react';
import { listRecipes, deleteRecipe } from '../services/RecipesService';
import { useNavigate } from 'react-router-dom';
import { getAllIngredients } from '../services/IngredientService';
import 'bootstrap/dist/css/bootstrap.min.css';

/**
 * This component will be responsible for listing all the recipes in the system. 
 * It will contain the button for adding a new recipe, and actions for editing/removing
 * recipes. This screen will be the initial screen that the user sees when they open
 * coffeeMaker.
 */
const ListRecipesComponent = () => {
    const [recipes, setRecipes] = useState([]);
    const [ingredients, setIngredients] = useState([]);
    const [showModalNoIngredients, setShowModalNoIngredients] = useState(false);
	const [showModalTooManyRecipes, setShowModalTooManyRecipes] = useState(false);
    const navigator = useNavigate();

	// Get all the recipes and all the ingredients right off the bat.
    useEffect(() => {
        getAllRecipes();
        getIngredients();
    }, []);

	// Create a function to get all the recipes
    function getAllRecipes() {
		// List recipes exist in our RecipeService. It uses the backend to get all recipes.
        listRecipes()
            .then((response) => {
				// Set the recipes in our system if there was not an error.
                setRecipes(response.data);
            })
            .catch((error) => {
				// If there was an error then we can display it
                console.error(error);
            });
    }

	// Same as fetch recipes, but now with ingredients
    function getIngredients() {
        getAllIngredients()  
            .then((response) => {
                setIngredients(response.data);
            })
            .catch((error) => {
                console.error(error);
            });
    }

    // Function to handle "Add Recipe" button click
    const addNewRecipe = () => {
		// When we want to add a recipe, there needs to be ingredients
		// in our inventory first. So we will check that there is some. 
        if (ingredients.length === 0) {
			// If there is no ingredients, we will show a modal telling the user that.
            setShowModalNoIngredients(true);
        } else if (recipes.length >= 3) {
			// If the recipes is greater than 3 then we should let the user know.
			setShowModalTooManyRecipes(true);
		}
		
		 else {
            // Navigate to the add recipe page if ingredients exist
            navigator('/add-recipe');
        }
    };

    // Function to hide the modals
    const handleCloseModal = () => setShowModalNoIngredients(false);
	const handleCloseErrorModal = () => setShowModalTooManyRecipes(false);

	// This function will handle the "Remove Recipe" button click
    function removeRecipe(id) {
		// Delete the recipe with a matching id.
        deleteRecipe(id)
            .then(() => {
				// Call get all recipes again so that the list properly updates
                getAllRecipes();
            })
            .catch((error) => {
                console.error(error);
            });
    }

	// A function to handle "Edit Recipe" button clicks
    function editRecipe(name) {
        navigator(`/edit-recipe/${name}`);
    }

    return (
        <div className="container py-3">
		
			{/*The title and the add recipe button*/}
            <h2 className="custom-title-text py-3">List of Recipes</h2>
            <button className="custom-update-button mb-3" onClick={addNewRecipe}>
                Add Recipe
            </button>

            {/* Modal to inform the user that there are no ingredients */}
			<div className={`modal fade ${showModalNoIngredients ? "show d-block" : ""} custom-modal`}>
                <div className={"modal-dialog"}>
                    <div className={"modal-content"}>
                        <div className="modal-header">
                            <h5 className="modal-title">No Ingredients Available</h5>
                            <button type="button" className="btn-close" onClick={handleCloseModal} aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            No ingredients found. Please add ingredients first.
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" onClick={handleCloseModal}>Close</button>
                            <button type="button" className="btn btn-primary" onClick={() => navigator("/add-ingredient")}>Add Ingredients</button>
                        </div>
                    </div>
                </div>
            </div>
			
			{/* Modal to inform the user that there are too many recipes */}
			<div className={`modal fade ${showModalTooManyRecipes ? "show d-block" : ""} custom-modal`}>
                <div className={"modal-dialog"}>
                    <div className={"modal-content"}>
                        <div className="modal-header">
                            <h5 className="modal-title">Max Recipes Met</h5>
                            <button type="button" className="btn-close" onClick={handleCloseErrorModal} aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            There are already 3 recipes in the system. Please remove one before adding another.
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" onClick={handleCloseErrorModal}>Close</button>
                        </div>
                    </div>
                </div>
            </div>

			{/* A table that will alternate colors and is bordered for the recipes in the system */}
            <table className="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>Recipe Name</th>
                        <th>Recipe Price</th>
                        {ingredients.length > 0 && ingredients.map((ingredient) => (
                            <th key={ingredient.name}>Amount {ingredient.name}</th>
                        ))}
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {recipes.map((recipe) => {
                        const recipeIngredients = recipe.ingredients.reduce((acc, ing) => {
                            acc[ing.name] = ing.amount;  
                            return acc;
                        }, {});

                        return (
                            <tr key={recipe.id}>
                                <td>{recipe.name}</td>
                                <td>{recipe.price}</td>
                                {ingredients.length > 0 && ingredients.map((ingredient) => (
                                    <td key={ingredient.name}>{recipeIngredients[ingredient.name] || 0}</td>
                                ))}
                                <td>
                                    <button className="custom-update-button" onClick={() => editRecipe(recipe.name)}>
                                        Edit
                                    </button>
                                    <button className="custom-remove-button" onClick={() => removeRecipe(recipe.id)} style={{ marginLeft: '10px' }}>
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </div>
    );
};

export default ListRecipesComponent;
