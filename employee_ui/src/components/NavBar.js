import React from 'react';
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

function NavBar({ onNavigate , activePage }) {
  const { rights } = useContext(AuthContext) || [];
	console.log("Rights in NavBar:", rights);

	return (
		<nav className="custom-navbar">
			<ul className="custom-navbar-list">
				<li><button className={`custom-navbar-button ${activePage === 'home' ? 'active' : ''}`} 
                    onClick={() => onNavigate('home')}>Home</button></li>

				<li><button className={`custom-navbar-button ${activePage === 'rights' ? 'active' : ''}`} 
                    onClick={() => onNavigate('rights')}>Rights</button></li>

				<li><button className={`custom-navbar-button ${activePage === 'employees' ? 'active' : ''}`} 
                    onClick={() => onNavigate('employees')}>Employees</button></li>

                {rights && rights.includes("RIG0001") && (
					<li><button className={`custom-navbar-button ${activePage === 'create-right' ? 'active' : ''}`} 
                    onClick={() => onNavigate('create-right')}>Create Right</button></li>
					)}
				{console.log("Rights:", rights)}
                <li><button className={`custom-navbar-button ${activePage === 'create-employee' ? 'active' : ''}`} 
                    onClick={() => onNavigate('create-employee')}>Create Employee</button></li>
                    
				<li><button className={`custom-navbar-button ${activePage === 'employee-request' ? 'active' : ''}`} 
                    onClick={() => onNavigate('employee-request')}>Employee Request</button></li>

				<li><button className={`custom-navbar-button ${activePage === 'search-employee' ? 'active' : ''}`} 
                    onClick={() => onNavigate('search-employee')}>Search Employee</button></li>
			</ul>
		</nav>
	);
}

export default NavBar;