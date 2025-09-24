import React, { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

function NavBar({ onNavigate, activePage }) {
  const { rightsNames } = useContext(AuthContext) || [];
  console.log("Rights in NavBar:", rightsNames);

  return (
    <nav className="custom-navbar">
      <ul className="custom-navbar-list">
        {/* Home is always visible */}
        <li>
          <button
            className={`custom-navbar-button ${activePage === 'home' ? 'active' : ''}`}
            onClick={() => onNavigate('home')}
          >
            Home
          </button>
        </li>

        {rightsNames?.includes("RIGHT_EMPLOYEE_CREATE_RIGHTS") && (
          <li>
            <button
              className={`custom-navbar-button ${activePage === 'rights' ? 'active' : ''}`}
              onClick={() => onNavigate('rights')}
            >
              Rights
            </button>
          </li>
        )}

        {rightsNames?.includes("RIGHT_EMPLOYEE_VIEW") && (
          <li>
            <button
              className={`custom-navbar-button ${activePage === 'employees' ? 'active' : ''}`}
              onClick={() => onNavigate('employees')}
            >
              Employees
            </button>
          </li>
        )}

        {rightsNames?.includes("RIGHT_EMPLOYEE_CREATE_RIGHTS") && (
          <li>
            <button
              className={`custom-navbar-button ${activePage === 'create-right' ? 'active' : ''}`}
              onClick={() => onNavigate('create-right')}
            >
              Create Right
            </button>
          </li>
        )}

        {rightsNames?.includes("RIGHT_EMPLOYEE_CREATE") && (
          <li>
            <button
              className={`custom-navbar-button ${activePage === 'create-employee' ? 'active' : ''}`}
              onClick={() => onNavigate('create-employee')}
            >
              Create Employee
            </button>
          </li>
        )}

        {rightsNames?.includes("RIGHT_EMPLOYEE_APPROVE_REQUEST") && (
          <li>
            <button
              className={`custom-navbar-button ${activePage === 'employee-request' ? 'active' : ''}`}
              onClick={() => onNavigate('employee-request')}
            >
              Employee Request
            </button>
          </li>
        )}
      </ul>
    </nav>
  );
}

export default NavBar;
