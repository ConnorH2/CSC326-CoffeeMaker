import React, { useEffect, useState } from 'react'
import { listRecipes, deleteRecipe } from '../services/RecipesService'
import { useNavigate } from 'react-router-dom'

/** Lists all the recipes and provide the option to create a new recipe
 * and delete an existing recipe.
 */
const ListRecipesComponent = () => {

    const [recipes, setRecipes] = useState([])

    const navigator = useNavigate();

    useEffect(() => {
        getAllRecipes()
    }, [])

    function getAllRecipes() {
        listRecipes().then((response) => {
            setRecipes(response.data)
        }).catch(error => {
            console.error(error)
        })
    }

    function addNewRecipe() {
        navigator('/add-recipe')
    }

    function removeRecipe(id) {
        console.log(id)

        deleteRecipe(id).then((response) => {
            getAllRecipes()
        }).catch(error => {
            console.error(error)
        })
    }

    return (
        <div className="container">
            <h2 className="text-center">List of Recipes</h2>
            <button className="btn btn-primary mb-2" onClick={ addNewRecipe }>Add Recipe</button>
            <table className="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>Recipe Name</th>
                        <th>Recipe Price</th>
                        <th>Amount Coffee</th>
                        <th>Amount Honey</th>
                        <th>Amount Syrup</th>
                        <th>Amount Milk</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        recipes.map(recipe => 
                        <tr key={recipe.id}>
                            <td>{recipe.name}</td>
                            <td>{recipe.price}</td>
                            <td>{recipe.coffee}</td>
                            <td>{recipe.milk}</td>
                            <td>{recipe.sugar}</td>
                            <td>{recipe.chocolate}</td>
                            <td>
                                <button className="btn btn-danger" onClick={() => removeRecipe(recipe.id)}
                                    style={{marginLeft: '10px'}}
                                >Delete</button>
                            </td>
                        </tr>)
                    }
                </tbody>
            </table>
        </div>
    )

}

export default ListRecipesComponent