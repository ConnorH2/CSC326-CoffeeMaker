import axios from "axios"

/** Base URL for the Recipe API - Correspond to methods in Backend's Recipe Controller. */
const REST_API_BASE_URL = "http://localhost:8080/api/edit-recipe"

export const editRecipe = (recipeDto) => {
	console.log(`${REST_API_BASE_URL}/${recipeDto.name}`);
	return axios.put(`${REST_API_BASE_URL}/${recipeDto.name}`, recipeDto, {
		headers: {
			'Content-Type': 'application/json',
		}
	});
};