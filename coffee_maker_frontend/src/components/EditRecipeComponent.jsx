import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { getRecipe } from '../services/RecipesService'
import { editRecipe } from '../services/EditRecipeService'
import { getAllIngredients } from '../services/IngredientService'
import { useNavigate } from 'react-router-dom'

const EditRecipeComponent = () => {
	const [ingredients, setIngredients] = useState([]);
	const [selectedIngredients, setSelectedIngredients] = useState({});
	const [name, setName] = useState("");
	const [price, setPrice] = useState("");
	const { incomingName } = useParams();

	const [errors, setErrors] = useState({
	    general: "",
	    name: "",
	    price: "",
		ingredients: "",
		selectedIngredients: ""
	});
	
	const navigator = useNavigate()

	useEffect(() => {
		if (incomingName) {
			getRecipe(incomingName)
				.then((response) => {
					setName(response.data.name);
					setPrice(response.data.price);
					
					const ingredientsWithIds = {};
					response.data.ingredients.forEach((ingredient) => {
						ingredientsWithIds[ingredient.id] = {
							id: ingredient.id,
							name: ingredient.name,
							amount: ingredient.amount,
						};
					});
					setSelectedIngredients(ingredientsWithIds);
				})
				.catch(error => {
					console.error(error);
				});
			
			getAllIngredients()
				.then(response => {
					setIngredients(response.data);
				})
				.catch(error => {
					console.error("Error getting ingredients", error);
				});
		}
	}, [incomingName]);

	function toggleIngredient(ingredientId, amount, ingredientName) {
		setSelectedIngredients((prevState) => {
			const updatedSelectedIngredients = { ...prevState };

			if (ingredientId in updatedSelectedIngredients) {
				delete updatedSelectedIngredients[ingredientId];
			} else {
				updatedSelectedIngredients[ingredientId] = { id: ingredientId, name: ingredientName, amount: amount || 1 };
			}

			return updatedSelectedIngredients;
		});
	}

	function updateIngredientAmount(ingredientId, ingredientName, amount) {
		setSelectedIngredients((prevState) => ({
			...prevState,
			[ingredientId]: {
				...prevState[ingredientId],
				name: ingredientName,
				amount: parseInt(amount, 10),
			},
		}));
	}

	function updateRecipe(e) {
		e.preventDefault();

		if (validateForm()) {
			const recipe = {
				name,
				price,
				ingredients: Object.values(selectedIngredients),
			};
			console.log(recipe.name);
			console.log(recipe.price);
			console.log(recipe.ingredients);
			editRecipe(recipe)
				.then((response) => {
					console.log(response.data);
					navigator("/recipes");
				})
				.catch(error => {
					const errorsCopy = { ...errors };
					if (error.response.status === 507) {
						errorsCopy.general = "Recipe list is at capacity.";
					}
					if (error.response.status === 409) {
						errorsCopy.general = "Duplicate recipe name.";
					}
					else {
						errorsCopy.general = error;
					}

					setErrors(errorsCopy);
				});
		}
	}

	function validateForm() {
		let valid = true;
		const errorsCopy = { ...errors };

		if (isNaN(price) || price < 1) {
			errorsCopy.price = "Error: Price needs a positive integer value. Recipe not added.";
			valid = false;
		} else {
			errorsCopy.price = "";
		}

		if (Object.keys(selectedIngredients).length === 0) {
			errorsCopy.selectedIngredients = "Error: At least one ingredient must be selected.";
			valid = false;
		} else {
			errorsCopy.selectedIngredients = "";
		}

		Object.values(selectedIngredients).forEach(ingredient => {
			if (ingredient.amount < 1) {
				errorsCopy.selectedIngredients = "Error: Each selected ingredient must have an amount of at least 1.";
				valid = false;
			}
		});

		setErrors(errorsCopy);

		return valid;
	}

	return (
		<div className='custom-container py-5'>
			<div className='row'>
				<div className='custom-card col-md-6 offset-md-3'>
					<h2 className="custom-title-text">Edit Recipe {incomingName}</h2>
					<div className="card-body">
						<form>
							<div className="form-group mb-2">
							    <label className="form-label">Recipe Name</label>
							    <input
							        type="text"
							        className="custom-form-control"
							        placeholder="Enter Recipe Name"
							        name='name'
							        value={name}
							        disabled
							    />
							</div>
							<div className="form-group mb-2">
								<label className="form-label">Recipe Price</label>
								<input
									type="text"
									name="recipePrice"
									placeholder="Enter Recipe Price (as an integer)"
									value={price}
									onChange={(e) => setPrice(e.target.value)}
									className={`custom-form-control ${errors.price ? "is-invalid" : ""}`}
								>
								</input>
								{errors.price && <div className="invalid-feedback">{errors.price}</div>}
							</div>
							{ingredients.map(ingredient => (
								<div key={ingredient.id} className="ingredient-container">
								<label className="form-label">Amount {ingredient.name}</label>
									{selectedIngredients[ingredient.id] ? (
									    <div className="d-flex align-items-center px-4">
									        <input
									            type="number"
									            name={`amount ${ingredient.id}`}
									            placeholder={`Enter amount for ${ingredient.name}`}
									            value={selectedIngredients[ingredient.id]?.amount || ""}
									            onChange={(e) => updateIngredientAmount(ingredient.id, ingredient.name, e.target.value)}
									            className="custom-form-control px-3"
									        />
									        <button
									            type="button"
									            className="custom-remove-button ms-3"
									            onClick={() => toggleIngredient(ingredient.id, ingredient.name, 0)}
									        >
									            Remove
									        </button>
									    </div>
									) : (
										<button
											type="button"
											className="custom-update-button"
											onClick={() => toggleIngredient(ingredient.id, ingredient.name, 1)}
										>
											Set {ingredient.name} Amount
										</button>
									)}
								</div>
							))}
							{errors.selectedIngredients && <div className="invalid-feedback d-block">{errors.selectedIngredients}</div>}
							<button className="custom-green-button" onClick={(e) => updateRecipe(e)}>Submit</button>
						</form>
					</div>
				</div>
			</div>	
		</div>
	);
}

export default EditRecipeComponent;