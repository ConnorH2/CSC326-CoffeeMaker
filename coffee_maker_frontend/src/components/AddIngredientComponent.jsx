import { useEffect, useState } from 'react'
import { createIngredient, getAllIngredients } from '../services/AddIngredientService'

const AddIngredientComponent = ({ onIngredientAdded }) => {

    const [ingredientName, setName] = useState("")
    const [ingredientAmount, setAmount] = useState("")
	const [existingIngredients, setExistingIngredients] = useState([]);
	const [showModal, setShowModal] = useState(false);

    const [errors, setErrors] = useState({
        general: "",
        ingredientName: "",
        ingredientAmount: "",
	})
	
	// We are going to get all the ingredients right off the back.
	useEffect(() => {
		getAllIngredients().then((response) => {
			// Set the existing ingredients to the response, it will either be an empty list or some set of ingredients
			setExistingIngredients(response.data || []);
		}).catch((error) => {
			console.error('Error adding ingredient:', error);
			setErrors((prev) => ({ ...prev, general: "Failed to add ingredient." }));
		});
	}, []);

	function modifyInventory(e) {
		e.preventDefault();

		if (validateForm()) {
			const newIngredient = {
				name: ingredientName,
				amount: parseInt(ingredientAmount)
			};

			createIngredient(newIngredient).then((response) => {
				
				// Show the modal
				setShowModal(true);
				
				// Clear the fields
				setName("");
				setAmount("");
				
				// We will let the toast exist for a few seconds.
				setTimeout(() => { setShowModal(false) }, 4000)
						
			}).catch(error => {
				console.error('Error adding ingredient:', error);
				setErrors({ ...errors, general: "Failed to add ingredient." });
			});
		}
	}

	function validateForm() {
		let valid = true;
		const errorsCopy = { ...errors };

		// Check for duplicate ingredient names
		if (existingIngredients.some(ingredient => ingredient.name.toLowerCase() === ingredientName.toLowerCase())) {
			errorsCopy.ingredientName = "Error: Duplicate ingredient name. Ingredient not added.";
			valid = false;
		} else {
			errorsCopy.ingredientName = "";
		}
		
		// Check that the amount is positive and an integer. Solves [invalid unit] & [duplicate]
		const amountValue = parseInt(ingredientAmount);
		if (isNaN(amountValue) || amountValue < 1) {
			errorsCopy.ingredientAmount = "Error: Ingredient needs a positive integer value. Ingredient not added.";
			valid = false;
		} else {
			errorsCopy.ingredientAmount = "";
		}

		setErrors(errorsCopy);
		return valid;
	}

    function getGeneralErrors() {
        if (errors.general) {
            return <div className="p-3 mb-2 bg-danger text-white">{errors.general}</div>
        }
    }
	
	// Function to hide the modal
	const handleCloseModal = () => setShowModal(false);

    return (
        <div className="container">
            <br /><br />
            <div className="row">
                <div className="custom-card col-md-6 offset-md-3">
                    <h2 className="custom-title-text">New Ingredient</h2>

                    <div className="card-body">
                        { getGeneralErrors() }
                        <form>
                            <div className="form-group mb-2">
                                <label className="form-label">Name</label>
                                <input 
                                    type="text"
                                    name="ingredientName"
                                    placeholder="Enter name"
                                    value={ingredientName}
                                    onChange={(e) => setName(e.target.value)}
                                    className={`custom-form-control ${errors.ingredientName ? "is-invalid":""}`}
                                >
                                </input>
								{errors.ingredientName && <div className="invalid-feedback">{errors.ingredientName}</div>}
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Amount</label>
                                <input 
                                    type="text"
                                    name="ingredientAmount"
                                    placeholder="Enter amount"
                                    value={ingredientAmount}
                                    onChange={(e) => setAmount(e.target.value)}
                                    className={`custom-form-control ${errors.ingredientAmount ? "is-invalid":""}`}
                                >
                                </input>
								{errors.ingredientAmount && <div className="invalid-feedback">{errors.ingredientAmount}</div>}
                            </div>

                            <button className="custom-green-button" onClick={(e) => modifyInventory(e)}>Add Ingredient</button>
                        </form>
                    </div>
                </div>
            </div>
			
			{/* Modal to inform the user that there an ingredient was successfully added */}
			<div className={`modal fade ${showModal ? "show d-block" : ""} custom-modal`}>
	               <div className={"modal-dialog"}>
	                   <div className={"modal-content"}>
	                       <div className="modal-header">
	                           <h5 className="modal-title">Ingredient Added Successful</h5>
	                       </div>
	                       <div className="modal-body">
	                           The ingredient {ingredientName} was successfully added. 
	                       </div>
	                       <div className="modal-footer">
	                           <button type="button" className="btn btn-secondary" onClick={handleCloseModal}>Close</button>
	                       </div>
	                   </div>
	               </div>
	           </div>
        </div>
    )
}

export default AddIngredientComponent