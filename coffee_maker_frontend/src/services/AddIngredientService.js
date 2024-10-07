import axios from "axios"

/** Base URL for the Inventory API - Correspond to methods in Backend's InventoryController. */
const REST_API_BASE_URL = "http://localhost:8080/api/add-ingredient"

/** PUT Inventory - gets all ingredients */
export const getAllIngredients = () => axios.get(REST_API_BASE_URL)

/** GET Inventory - creates an ingredient */
export const createIngredient = (ingredientDto) => axios.post(REST_API_BASE_URL, ingredientDto)