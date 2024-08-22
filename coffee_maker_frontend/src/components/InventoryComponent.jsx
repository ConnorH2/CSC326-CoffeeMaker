import { useEffect, useState } from 'react'
import { getInventory, updateInventory } from '../services/InventoryService'

/** Creates the page for viewing and updating the inventory. */
const InventoryComponent = () => {

    const [coffee, setCoffee] = useState("")
    const [milk, setMilk] = useState("")
    const [sugar, setSugar] = useState("")
    const [chocolate, setChocolate] = useState("")

    const [errors, setErrors] = useState({
        general: "",
        coffee: "",
        milk: "",
        sugar: "",
        chocolate: "",
    })

    useEffect(() => {
        getInventory().then((response) => {
            setCoffee(response.data.coffee)
            setMilk(response.data.milk)
            setSugar(response.data.sugar)
            setChocolate(response.data.chocolate)
        }).catch(error => {
            console.error(error)
        })
    }, [])

    function modifyInventory(e) {
        e.preventDefault()

        if (validateForm()) {
            const inventory = {coffee, milk, sugar, chocolate}
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
		
		const inventory = {coffee, milk, sugar, chocolate}
		console.log(inventory)

		if (coffee < 0) {
            errorsCopy.coffee = "Coffee amount must be a positive integer"
			valid = false;
        }

		if (milk < 0) {
            errorsCopy.milk = "Milk amount must be a positive integer"
			valid = false;
        }
		
		if (sugar < 0) {
            errorsCopy.sugar = "Sugar amount must be a positive integer"
			valid = false;
        }

		if (chocolate < 0) {
            errorsCopy.chocolate = "Chocolate amount must be a positive integer"
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
                    <h2 className="text-center">Inventory</h2>

                    <div className="card-body">
                        { getGeneralErrors() }
                        <form>
                            <div className="form-group mb-2">
                                <label className="form-label">Amount Coffee</label>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="Enter Amount Coffee"
                                    value={coffee}
                                    onChange={(e) => setCoffee(e.target.value)}
                                    className={`form-control ${errors.coffee ? "is-invalid":""}`}
                                >
                                </input>
								{errors.coffee && <div className="invalid-feedback">{errors.coffee}</div>}
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Amount Milk</label>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="Enter Amount Milk"
                                    value={milk}
                                    onChange={(e) => setMilk(e.target.value)}
                                    className={`form-control ${errors.milk ? "is-invalid":""}`}
                                >
                                </input>
								{errors.milk && <div className="invalid-feedback">{errors.milk}</div>}
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Amount Sugar</label>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="Enter Amount Sugar"
                                    value={sugar}
                                    onChange={(e) => setSugar(e.target.value)}
                                    className={`form-control ${errors.sugar ? "is-invalid":""}`}
                                >
                                </input>
								{errors.sugar && <div className="invalid-feedback">{errors.sugar}</div>}
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Amount Chocolate</label>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="Enter Amount Chocolate"
                                    value={chocolate}
                                    onChange={(e) => setChocolate(e.target.value)}
                                    className={`form-control ${errors.chocolate ? "is-invalid":""}`}
                                >
                                </input>
								{errors.chocolate && <div className="invalid-feedback">{errors.chocolate}</div>}
                            </div>

                            <button className="btn btn-success" onClick={(e) => modifyInventory(e)}>Update Inventory</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default InventoryComponent