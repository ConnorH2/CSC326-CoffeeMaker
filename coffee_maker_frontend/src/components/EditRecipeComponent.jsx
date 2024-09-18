import { useEffect, useState } from 'react'

const EditRecipeComponent = () => {

    const [name, setName] = useState("")
    const [price, setPrice] = useState("")
	const [coffee, setCoffee] = useState("")
    const [honey, setHoney] = useState("")
    const [syrup, setSyrup] = useState("")
    const [milk, setMilk] = useState("")

    const [errors, setErrors] = useState({
    })

    /*useEffect(() => {
        getInventory().then((response) => {
            setName(response.data.ingredientName)
            setAmount(response.data.ingredientAmount)
        }).catch(error => {
            console.error(error)
        })
    }, [])*/

    function modifyRecipe(e) {
        e.preventDefault()

        if (validateForm()) {
            const recipe = {name, price, coffee, honey, syrup, milk}
            console.log(recipe)
        }
    }

    function validateForm() {
        let valid = true

        const errorsCopy = {... errors}
		
		console.log(name)
		

		if (name == "Beekeeper special") {
            errorsCopy.general = "Recipe does not contain any ingredients"
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
                    <h2 className="text-center">Edit Recipe</h2>

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
                                className="form-control"
                            >
                            </input>
                        </div>

                        <div className="form-group mb-2">
                            <label className="form-label">Amount Coffee</label>
                            <input 
                                type="text"
                                name="recipeName"
                                placeholder="Enter Amount Coffee"
                                value={coffee}
                                onChange={(e) => setCoffee(e.target.value)}
                                className="form-control"
                            >
                            </input>
                        </div>
						
						<div className="form-group mb-2">
                            <label className="form-label">Amount Honey</label>
                            <input 
                                type="text"
                                name="recipeName"
                                placeholder="Enter Amount Honey"
                                value={honey}
                                onChange={(e) => setHoney(e.target.value)}
                                className="form-control"
                            >
                            </input>
                        </div>

                        <div className="form-group mb-2">
                            <label className="form-label">Amount Syrup</label>
                            <input 
                                type="text"
                                name="recipeName"
                                placeholder="Enter Amount Syrup"
                                value={syrup}
                                onChange={(e) => setSyrup(e.target.value)}
                                className="form-control"
                            >
                            </input>
                        </div>

                        <div className="form-group mb-2">
                            <label className="form-label">Amount Milk</label>
                            <input 
                                type="text"
                                name="recipeName"
                                placeholder="Enter Amount Milk"
                                value={milk}
                                onChange={(e) => setMilk(e.target.value)}
                                className="form-control"
                            >
                            </input>
                        </div>

                            <button className="btn btn-success" onClick={(e) => modifyRecipe(e)}>Submit Edit</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default EditRecipeComponent