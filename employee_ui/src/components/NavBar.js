import React from 'react';

function NavBar({ onNavigate , activePage }) {
	return (
		<nav className="navbar">
			<ul className="navbar-list">
				<li><button className={`navbar-button ${activePage === 'home' ? 'active' : ''}`} 
                    onClick={() => onNavigate('home')}>Home</button></li>

				<li><button className={`navbar-button ${activePage === 'rights' ? 'active' : ''}`} 
                    onClick={() => onNavigate('rights')}>Rights</button></li>

				<li><button className={`navbar-button ${activePage === 'employees' ? 'active' : ''}`} 
                    onClick={() => onNavigate('employees')}>Employees</button></li>

				<li><button className={`navbar-button ${activePage === 'create-right' ? 'active' : ''}`} 
                    onClick={() => onNavigate('create-right')}>Create Right</button></li>
			
                <li><button className={`navbar-button ${activePage === 'create-employee' ? 'active' : ''}`} 
                    onClick={() => onNavigate('create-employee')}>Create Employee</button></li>
                    
				<li><button className={`navbar-button ${activePage === 'employee-request' ? 'active' : ''}`} 
                    onClick={() => onNavigate('employee-request')}>Employee Request</button></li>

				<li><button className={`navbar-button ${activePage === 'search-employee' ? 'active' : ''}`} 
                    onClick={() => onNavigate('search-employee')}>Search Employee</button></li>

			</ul>
		</nav>
	);
}

export default NavBar;