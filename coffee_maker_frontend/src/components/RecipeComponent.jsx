import { useState, useEffect } from 'react'
import { createRecipe, listRecipes } from '../services/RecipesService'
import { getAllIngredients } from '../services/IngredientService'
import { useNavigate } from 'react-router-dom'

/** Form to create a new recipe. */
const RecipeComponent = () => {
	const [ingredients, setIngredients] = useState([])
	const [selectedIngredients, setSelectedIngredients] = useState([])
    const [name, setName] = useState("")
    const [price, setPrice] = useState("")
	const [recipes, setRecipes] = useState([])

    const navigator = useNavigate()
    const [errors, setErrors] = useState({
        general: "",
        name: "",
        price: "",
		ingredients: "",
		recipes: ""
	})

	// We are going to get all the ingredients and all the recipes
	useEffect(() => {
		getAllIngredients()
			.then(response => {
				setIngredients(response.data);
			})
			.catch(error => {
				console.error("Error getting ingredients", error);
			});
		listRecipes()
			.then(response => {
				setRecipes(response.data);
			})
			.catch(error => {
				console.error("Error getting recipes", error);
			});
	}, []);

	// Create a function that 
	function toggleIngredient(ingredientId, amount) {
		const updatedSelectedIngredients = { ...selectedIngredients };

		if (ingredientId in updatedSelectedIngredients) {
			delete updatedSelectedIngredients[ingredientId];
		} else {
			updatedSelectedIngredients[ingredientId] = amount;
		}

		setSelectedIngredients(updatedSelectedIngredients);
	}

	// A function that will update the amount of an ingredient
	function updateIngredientAmount(ingredientId, ingredientName, amount) {
		setSelectedIngredients(prevState  => ({
			...prevState,
			[ingredientId]: {
				name: ingredientName,
				amount: parseInt(amount)
			}
		}));
	}

	function saveRecipe(e) {
        e.preventDefault();
	
		// We make a helper to validate the form
		if (validateForm()) {
			const recipe = {
				name,
				price,
				ingredients: Object.values(selectedIngredients).map(ingredient => ({
					name: ingredient.name,
					amount: ingredient.amount
				}))
			};

            createRecipe(recipe).then((response) => {
                console.log(response.data)
                navigator("/recipes")
            }).catch(error => {
                console.error(error)
                const errorsCopy = {... errors}
                if (error.response.status == 507) {
                    errorsCopy.general = "Recipe list is at capacity."
                } 
                if (error.response.status == 409) {
                    errorsCopy.general = "Duplicate recipe name."
                }

                setErrors(errorsCopy)
            })
        }
    }

	// This is where our error handling comes into play.
    function validateForm() {
        let valid = true
        
        const errorsCopy = {...errors}

		// Every recipe must have a name
		if (name.trim()) {
			errorsCopy.name = ""
		} else {
			errorsCopy.name = "Error: Name is required."
			valid = false
		}
		
		// Checks if there is a duplicate recipe name
		if (recipes.some(recipe => recipe.name.toLowerCase() === name.toLowerCase())) {
			errorsCopy.name = "Error: Duplicate recipe name. Recipe not added.";
			valid = false;
		} else {
			errorsCopy.name = "";
		}
		
		// The price needs to be a number that is positive
		if (isNaN(price) || price < 1) {
			errorsCopy.price = "Error: Price needs a positive integer value. Recipe not added.";
			valid = false;
		} else {
			errorsCopy.price = "";
		}

		// The number of ingredients must be more than 0
		if (Object.keys(selectedIngredients).length === 0) {
			errorsCopy.ingredients = "Error: At least one ingredient must be selected.";
			valid = false;
		} else {
			errorsCopy.ingredients = "";
		}

		// All ingredients need a value of at least one.
		Object.values(selectedIngredients).forEach(ingredient => {
			if (ingredient.amount < 1) {
				errorsCopy.ingredients = "Error: Each selected ingredient must have an amount of at least 1.";
				valid = false;
			}
		});

		// Set any errors that may have happened
		setErrors(errorsCopy)

		// Return valid, if true, then we will add the recipe
		return valid
    }

	// Highlight the general errors
    function getGeneralErrors() {
        if (errors.general) {
            return <div className="p-3 mb-2 bg-danger text-white">{errors.general}</div>
        }
    }		
	
    return (
        <div className="container">
            <br /><br />
            <div className="row">
                <div className={"custom-card col-md-6 offset-md-3"}>
                    <h2 className="text-center">Add Recipe</h2>

                    <div className="card-body">
                        { getGeneralErrors() }
                        <form>
                            <div className="form-group mb-2">
                                <label className="form-label">Recipe Name</label>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="Enter Recipe Name"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    className={`form-control ${errors.name ? "is-invalid":""}`}
                                >
                                </input>
                                {errors.name && <div className="invalid-feedback">{errors.name}</div>}
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Recipe Price</label>
                                <input 
                                    type="text"
                                    name="recipePrice"
                                    placeholder="Enter Recipe Price (as an integer)"
                                    value={price}
                                    onChange={(e) => setPrice(e.target.value)}
                                    className={`form-control ${errors.price ? "is-invalid":""}`}
								>
								</input>
								{errors.price && <div className="invalid-feedback">{errors.price}</div>}
							</div>

							{
								ingredients.map(ingredient =>
									<div key={ingredient.id} className="ingredient-container">
									<label className="form-label-ingAmount">Amount {ingredient.name}: </label>
										{!(ingredient.id in selectedIngredients) ? (
											<button
												type="button"
												className="custom-update-button"
												onClick={() => toggleIngredient(ingredient.id, 1)}
											>
												Set {ingredient.name} Amount
											</button>
										) : (
											<div className="d-flex align-items-center">
												<input
													type="number"
													name={`amount ${ingredient.id}`}
													placeholder={`Enter amount for ${ingredient.name}`}
													value={selectedIngredients[ingredient.id]?.amount || ""}
													onChange={(e) => updateIngredientAmount(ingredient.id, ingredient.name, e.target.value)}
													className="form-control me-2"
												/>
												<button
													type="button"
													className="custom-remove-button"
													onClick={() => toggleIngredient(ingredient.id, 0)}
												>
													Remove
												</button>
											</div>
										)}
									</div>
								)
							}
							{errors.ingredients && <div className="invalid-feedback d-block">{errors.ingredients}</div>}

                            <button className="custom-green-button" onClick={(e) => saveRecipe(e)}>Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )

}

export default RecipeComponent