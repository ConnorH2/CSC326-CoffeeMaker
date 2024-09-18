import { useEffect, useState } from 'react'
import { getInventory, updateInventory } from '../services/InventoryService'

/** Creates the page for viewing and updating the inventory. */
const InventoryComponent = () => {

    const [coffee, setCoffee] = useState("")
    const [honey, setHoney] = useState("")
    const [syrup, setSyrup] = useState("")
    const [milk, setMilk] = useState("")

    const [errors, setErrors] = useState({
        general: "",
        coffee: "",
        honey: "",
        syrup: "",
        milk: "",
    })

    useEffect(() => {
        getInventory().then((response) => {
            setCoffee(response.data.coffee)
            setHoney(response.data.honey)
            setSyrup(response.data.syrup)
            setMilk(response.data.milk)
        }).catch(error => {
            console.error(error)
        })
    }, [])

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
		
		const inventory = {coffee, honey, syrup, milk}
		console.log(inventory)

		if (coffee < 0) {
            errorsCopy.coffee = "Coffee amount must be a positive integer"
			valid = false;
        }

		if (honey < 0) {
            errorsCopy.honey = "Milk amount must be a positive integer"
			valid = false;
        }
		
		if (syrup < 0) {
            errorsCopy.syrup = "Sugar amount must be a positive integer"
			valid = false;
        }

		if (milk < 0) {
            errorsCopy.milk = "Chocolate amount must be a positive integer"
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
                                <label className="form-label">Amount Honey</label>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="Enter Amount Honey"
                                    value={honey}
                                    onChange={(e) => setHoney(e.target.value)}
                                    className={`form-control ${errors.honey ? "is-invalid":""}`}
                                >
                                </input>
								{errors.honey && <div className="invalid-feedback">{errors.honey}</div>}
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Amount Syrup</label>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="Enter Amount Syrup"
                                    value={syrup}
                                    onChange={(e) => setSyrup(e.target.value)}
                                    className={`form-control ${errors.syrup ? "is-invalid":""}`}
                                >
                                </input>
								{errors.syrup && <div className="invalid-feedback">{errors.syrup}</div>}
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

                            <button className="btn btn-success" onClick={(e) => modifyInventory(e)}>Update Inventory</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default InventoryComponent