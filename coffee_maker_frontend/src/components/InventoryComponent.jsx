import { useEffect, useState } from 'react'
import { getAllIngredients, updateAllIngredients } from '../services/IngredientService'

/** Creates the page for viewing and updating the inventory. */
const InventoryComponent = () => {

    const [ingredients, setIngredients] = useState([])

    const [errors, setErrors] = useState({
        general: "",
		ingredients: "",
    })

	useEffect(() => {
		fetchIngredients();
	}, []);

	const fetchIngredients = () => {
		getAllIngredients().then((response) => {
			setIngredients(response.data || []);
		}).catch(error => {
			console.error('Error fetching ingredients:', error);
			setErrors((prev) => ({ ...prev, general: "Failed to fetch ingredients." }));
		});
	};

	const handleInputChange = (e, index) => {
		const updatedIngredients = [...ingredients];
		updatedIngredients[index].amount = e.target.value;
		setIngredients(updatedIngredients);
	};

	function modifyInventory(e) {
		e.preventDefault()

        if (validateForm()) {
			console.log("bruh")
            console.log(ingredients)
			console.log(typeof ingredients)
			console.log(JSON.stringify(ingredients))

            updateAllIngredients(ingredients).then((response) => {
                console.log(response.data)
            }).catch(error => {
                console.error(error)
            })
        }
    }

    function validateForm() {
        let valid = true

        const errorsCopy = {... errors}
		
		const inventory = {... ingredients}
		console.log(inventory)
		console.log(typeof inventory)
		
		if(inventory.length === 0){
			errorsCopy.ingredients = "There must be at least one ingredient.";
			valid = false;
		}
		else {
			ingredients.forEach((ingredient, index) => {
				if (ingredient.amount < 1) {
					errorsCopy[`ingredient_${index}`] = `${ingredient.name} amount must be a positive integer.`;
					valid = false;
				} else {
					errorsCopy[`ingredient_${index}`] = "";
				}
			});
		}

		setErrors(errorsCopy)

        return valid
    }

    function getGeneralErrors() {
        if (errors.general) {
            return <div className="p-3 mb-2 bg-danger text-white">{errors.general}</div>
        }
    }

    return (
        <div className="container">
            <br /><br />
            <div className="row">
                <div className="custom-card col-md-6 offset-md-3">
					<h2 className="custom-title-text">Inventory</h2>

					<div className="card-body">
						{getGeneralErrors()}
						<form>
							{
								ingredients.map((ingredient, index) =>
									<div>
										<div className="form-group mb-2">
											<label className="form-label mb-4">Amount {ingredient.name} </label>
											<input
												type="text"
												name="ingredientAmount"
												placeholder="Enter Amount Placeholder"
												value={ingredient.amount}
												onChange={(e) => handleInputChange(e, index)}
												className={`custom-form-control ${errors[`ingredient_${index}`] ? "is-invalid" : ""}`}
											>
											</input>
											{errors[`ingredient_${index}`] && (
												<div className="invalid-feedback mb-4">
													{errors[`ingredient_${index}`]}
												</div>
											)}
										</div>
									</div>
								)
							}
							{errors.ingredients && <div className="invalid-feedback d-block">{errors.ingredients}</div>}

                            <button className="custom-green-button mb-4" onClick={(e) => modifyInventory(e)}>Update Inventory</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default InventoryComponent