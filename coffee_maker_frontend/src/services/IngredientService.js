import axios from "axios"

/** Base URL for the Inventory API - Correspond to methods in Backend's InventoryController. */
const REST_API_BASE_URL = "http://localhost:8080/api/ingredients"

/** PUT Inventory - gets all ingredients */
export const getAllIngredients = () => axios.get("http://localhost:8080/api/add-ingredient")

/** GET Inventory - updates all ingredients */
export const updateAllIngredients = (ingredientList) => axios.put(REST_API_BASE_URL, ingredientList)