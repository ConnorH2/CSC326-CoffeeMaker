import { useEffect, useState } from 'react'

const AddIngredientComponent = () => {

    const [ingredientName, setName] = useState("")
    const [ingredientAmount, setAmount] = useState("")

    const [errors, setErrors] = useState({
        general: "",
        ingredientName: "",
        ingredientAmount: "",
    })

    /*useEffect(() => {
        getInventory().then((response) => {
            setName(response.data.ingredientName)
            setAmount(response.data.ingredientAmount)
        }).catch(error => {
            console.error(error)
        })
    }, [])*/

    function modifyInventory(e) {
        e.preventDefault()

        if (validateForm()) {
            const inventory = {coffee, honey, syrup, milk}
            console.log(inventory)

            updateInventory(inventory).then((response) => {
                console.log(response.data)
            }).catch(error => {
                console.error(error)
            })
        }
    }

    function validateForm() {
        let valid = true

        const errorsCopy = {... errors}
		
		const ingredient = {ingredientName, ingredientAmount}
		console.log(ingredient)

		if (ingredientName == "Milk") {
            errorsCopy.ingredientName = "Error: Duplicate ingredient name. Ingredient not added"
			valid = false;
        }

		if (ingredientAmount < 0) {
            errorsCopy.ingredientAmount = "Error: Ingredient needs positive integer value. Ingredient not added"
			valid = false;
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
                <div className="card col-md-6 offset-md-3">
                    <h2 className="text-center">New Ingredient</h2>

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
                                    className={`form-control ${errors.ingredientName ? "is-invalid":""}`}
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
                                    className={`form-control ${errors.ingredientAmount ? "is-invalid":""}`}
                                >
                                </input>
								{errors.ingredientAmount && <div className="invalid-feedback">{errors.ingredientAmount}</div>}
                            </div>

                            <button className="btn btn-success" onClick={(e) => modifyInventory(e)}>Add Ingredient</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default AddIngredientComponent