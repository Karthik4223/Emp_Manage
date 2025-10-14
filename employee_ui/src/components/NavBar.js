import React from 'react';
import { useSelector } from 'react-redux';

function NavBar({ onNavigate, activePage }) {
  const rightsNames = useSelector((state) => state.auth.rights);
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


        {rightsNames?.includes("RIGHT_RIGHTS_VIEW") && (
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

        {rightsNames?.includes("RIGHT_RIGHTS_CREATE") && (
          <li>
            <button
              className={`custom-navbar-button ${activePage === 'create-right' ? 'active' : ''}`}
              onClick={() => onNavigate('create-right')}
            >
              Create Right
            </button>
          </li>
        )}

        {rightsNames?.includes("RIGHT_EMPLOYEE_REQUEST_CREATE") && (
          <li>
            <button
              className={`custom-navbar-button ${activePage === 'create-employee' ? 'active' : ''}`}
              onClick={() => onNavigate('create-employee')}
            >
              Create Employee
            </button>
          </li>
        )}

        {rightsNames?.includes("RIGHT_EMPLOYEE_CREATE_EXCEL") && (
          <li>
            <button
              className={`custom-navbar-button ${activePage === 'create-employee-excel' ? 'active' : ''}`}
              onClick={() => onNavigate('create-employee-excel')}
            >
            Create Employees (Excel)
          </button>
        </li>
        )}

        {rightsNames?.includes("RIGHT_EMPLOYEE_REQUEST_VIEW") && (
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
