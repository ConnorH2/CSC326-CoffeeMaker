import { NavLink } from 'react-router-dom'

/** Code for the Header of the webpage. */
const HeaderComponent = () => {
  return (
    <div className="px-5">
        <header>
            <nav className="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
                <a className="custom-navbar-brand" href="http://localhost:3000">Coffee Maker</a>
                <div className="collapse navbar-collapse" id="navbarNav">
                  <ul className="navbar-nav">
					<li className="custom-nav-item">
	                  <NavLink className="nav-link" to="/recipes">Recipes</NavLink>
	                </li>
					<li className="custom-nav-item">
					  <NavLink className="nav-link" to="/inventory">Inventory</NavLink>
					</li>
					<li className="custom-nav-item">
					  <NavLink className="nav-link" to="/make-recipe">Order Beverage</NavLink>
					</li>
					<li className="custom-nav-item">
					  <NavLink className="nav-link" to="/add-ingredient">Add Ingredient</NavLink>
					</li>
                  </ul>
                </div>
            </nav>
        </header>
    </div>
  )
}

export default HeaderComponent