import React from "react";
import { useState,useEffect } from "react";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import CreateEmployee from "./CreateEmployee";

import { AsyncTypeahead } from 'react-bootstrap-typeahead';
import 'react-bootstrap-typeahead/css/Typeahead.css';


function Employee({onNavigate}) {
  const [employeeDetails, setEmployeeDetails] = useState([]);
  const [editPopupOpen, setEditPopupOpen] = useState(false);
  const [employeeToEdit, setEmployeeToEdit] = useState(null);


  const [filterPopupOpen, setFilterPopupOpen] = useState(false);
  const [filterData, setFilterData] = useState({
    name: '',
    email: '',
    phoneNumber: '',
    employeeCode: '',
    empDepartment: '',
    employeeStatus: '',
    searchKey: '',
  });

  const [nameOptions, setNameOptions] = useState([]);
  const [emailOptions, setEmailOptions] = useState([]);
  const [phoneOptions, setPhoneOptions] = useState([]);
  const [empCodeOptions, setEmpCodeOptions] = useState([]);
  // const [searchKeyOptions, setSearchKeyOptions] = useState([]);



  useEffect(() => {
    fetchEmployeeDetails();
  }, []);


  const fetchEmployeeDetails = async () => {
    try {
      const response = await fetch("employee/getAllEmployees");
      if (!response.ok) throw new Error(response.statusText);
      const data = await response.json();
      setEmployeeDetails(data);
    } catch (error) {
      toast.error('Error fetching employee details.');
    }
  };

      const handleEdit = (empCode) => {
      const emp = employeeDetails.find((e) => e.empCode === empCode);
      if (emp) {
        setEmployeeToEdit(emp);
        setEditPopupOpen(true);
      }
    };



  const handleStatusChange = (empCode, currentStatus) => {
    const newStatus = currentStatus === "ACTIVE" ? "INACTIVE" : "ACTIVE";

    const updateStatus = async () => {
      try {
        const response = await fetch(`/employee/updateEmpStatus/${empCode}?newStatus=${newStatus}&updatedBy=XYZ`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
        });

        if (response.ok){

            toast.success(`Employee: ${empCode} ${currentStatus.toLowerCase()}d successfully.`);

           if (filterPopupOpen || Object.values(filterData).some(v => v)) {
              fetchFilteredEmployees();
            } else {
              fetchEmployeeDetails();
            }
      }else{
        const data = await response.text();
        toast.error(data);
      }

      } catch (error) {
        toast.error('Error updating employee status.');
      }
    };

    updateStatus();
  }

  const handleRightsMapping = (empCode) => {
    onNavigate(`rights-mapping/${empCode}`);
  }


  const fetchFilteredEmployees = async () => {
  const payload = {
    employeeNames: filterData.name ? [filterData.name] : [],
    employeeEmail: filterData.email ? [filterData.email] : [],
    employeePhoneNumber: filterData.phoneNumber ? [filterData.phoneNumber] : [],
    employeeDepartment: filterData.empDepartment ? [filterData.empDepartment] : [],
    searchKey: filterData.searchKey || null,
    employeeStatus: filterData.employeeStatus || null,
    employeeCode: filterData.employeeCode ? [filterData.employeeCode] :[],
  };

  try {
    const response = await fetch('/employee/search', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });

    if (response.ok) {
        const data = await response.json();
        setEmployeeDetails(data);
        toast.success("Filter applied successfully.");
      } else {
        const errorText = await response.text();
        toast.error(errorText);
      }

  } catch (error) {
    console.error("Error fetching filtered employees:", error);
    toast.error("Failed to apply filter.");
  }
};

 
  if (!employeeDetails) {
    return <div className="content">Loading employee details...</div>;
  }

  return (
    <div className="content">


    <button className="filter-button" onClick={() => setFilterPopupOpen(true)} style={{ marginBottom: '10px' }}>
      Filter Employees
    </button>

      {editPopupOpen && (
      <div className="popup-overlay" style={{
        position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
        backgroundColor: 'rgba(0, 0, 0, 0.5)', display: 'flex',
        justifyContent: 'center', alignItems: 'center', zIndex: 1000
      }}>
        <div className="popup-content" style={{
          backgroundColor: 'white', padding: '20px', borderRadius: '8px',
          width: '60%', maxHeight: '90vh', overflowY: 'auto'
        }}>
          <CreateEmployee
            prefillData={employeeToEdit}
            onClose={() => {
              setEditPopupOpen(false);
              setEmployeeToEdit(null);
              fetchEmployeeDetails();
            }}
          />
          <div style={{ textAlign: 'center', marginTop: '10px' }}>
            <button
              onClick={() => {
                setEditPopupOpen(false);
                setEmployeeToEdit(null);
              }}
              style={{ backgroundColor: '#6c757d', color: 'white', padding: '8px 12px', borderRadius: '4px' }}
            >
              Close
            </button>
          </div>
        </div>
      </div>
    )}

      {filterPopupOpen && (
        <div className="filter-popup" style={{
          position: 'fixed',
          top: 0, left: 0, right: 0, bottom: 0,
          backgroundColor: 'rgba(0,0,0,0.5)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          zIndex: 1000
        }}>
          <div className="filter-form" style={{
            backgroundColor: 'white',
            padding: '20px',
            borderRadius: '5px',
            minWidth: '400px'
          }}>
            <h3>Filter Employees</h3>

            {/* Name */}
            <div className="custom-form-group">
              <label>Name:</label>
              <AsyncTypeahead
                id="name-search"
                isLoading={false}
                minLength={2}
                labelKey="name"
                onSearch={async (query) => {
                  try {
                    const payload = { employeeNames: [query] };
                    const response = await fetch('/employee/search', {
                      method: 'POST',
                      headers: { 'Content-Type': 'application/json' },
                      body: JSON.stringify(payload),
                    });
                    if (response.ok) {
                      const data = await response.json();
                      setNameOptions(data.map(emp => emp.name));
                    }
                  } catch (error) {
                    console.error(error);
                  }
                }}
                onChange={(selected) => setFilterData({ ...filterData, name: selected[0] || '' })}
                options={nameOptions || []}
                placeholder="Type to search..."
              />
            </div>

            {/* Email */}
            <div className="custom-form-group">
              <label>Email:</label>
              <AsyncTypeahead
                id="email-search"
                isLoading={false}
                minLength={2}
                labelKey="email"
                onSearch={async (query) => {
                  try {
                    const payload = { employeeEmail: [query] };
                    const response = await fetch('/employee/search', {
                      method: 'POST',
                      headers: { 'Content-Type': 'application/json' },
                      body: JSON.stringify(payload),
                    });
                    if (response.ok) {
                      const data = await response.json();
                      setEmailOptions(data.map(emp => emp.email));
                    }
                  } catch (error) {
                    console.error(error);
                  }
                }}
                onChange={(selected) => setFilterData({ ...filterData, email: selected[0] || '' })}
                options={emailOptions || []}
                placeholder="Type to search..."
              />
            </div>

            {/* Phone Number */}
            <div className="custom-form-group">
              <label>Phone Number:</label>
              <AsyncTypeahead
                id="phone-search"
                isLoading={false}
                minLength={2}
                labelKey="phoneNumber"
                onSearch={async (query) => {
                  try {
                    const payload = { employeePhoneNumber: [query] };
                    const response = await fetch('/employee/search', {
                      method: 'POST',
                      headers: { 'Content-Type': 'application/json' },
                      body: JSON.stringify(payload),
                    });
                    if (response.ok) {
                      const data = await response.json();
                      setPhoneOptions(data.map(emp => emp.phoneNumber));
                    }
                  } catch (error) {
                    console.error(error);
                  }
                }}
                onChange={(selected) => setFilterData({ ...filterData, phoneNumber: selected[0] || '' })}
                options={phoneOptions || []}
                placeholder="Type to search..."
              />
            </div>

            {/* Employee Code */}
            <div className="custom-form-group">
              <label>Emp Code:</label>
              <AsyncTypeahead
                id="empcode-search"
                isLoading={false}
                minLength={1}
                labelKey="empCode"
                onSearch={async (query) => {
                  try {
                    const payload = { employeeCode: [query] };
                    const response = await fetch('/employee/search', {
                      method: 'POST',
                      headers: { 'Content-Type': 'application/json' },
                      body: JSON.stringify(payload),
                    });
                    if (response.ok) {
                      const data = await response.json();
                      setEmpCodeOptions(data.map(emp => emp.empCode));
                    }
                  } catch (error) {
                    console.error(error);
                  }
                }}
                onChange={(selected) => setFilterData({ ...filterData, employeeCode: selected[0] || '' })}
                options={empCodeOptions || []}
                placeholder="Type to search..."
              />
            </div>

            {/* Department Dropdown */}
            <div className="custom-form-group">
              <label>Department:</label>
              <select
                name="empDepartment"
                value={filterData.empDepartment}
                onChange={(e) => setFilterData({ ...filterData, empDepartment: e.target.value })}
              >
                <option value="">All</option>
                <option value="hr">HR</option>
                <option value="development">Development</option>
                <option value="testing">Testing</option>
                <option value="management">Management</option>
                <option value="sales">Sales</option>
                <option value="marketing">Marketing</option>
              </select>
            </div>

            <div className="custom-form-group">
              <label>Status:</label>
              <select
                name="employeeStatus"
                value={filterData.employeeStatus}
                onChange={(e) => setFilterData({ ...filterData, employeeStatus: e.target.value })}
              >
                <option value="">All</option>
                <option value="ACTIVE">Active</option>
                <option value="INACTIVE">Inactive</option>
              </select>
            </div>

            <div className="custom-form-group">
              <label>Search Key:</label>
              <input
                name="searchKey"
                value={filterData.searchKey}
                onChange={(e) => setFilterData({ ...filterData, searchKey: e.target.value })}
              />
            </div>

            <div className="custom-form-group-button" style={{ marginTop: '10px' }}>
              <button
                onClick={() => {
                  setFilterPopupOpen(false);
                  fetchFilteredEmployees();
                }}
                style={{ marginRight: '10px' }}
              >
                Apply
              </button>

              <button
                onClick={() => {
                  const cleared = {
                    name: '',
                    email: '',
                    phoneNumber: '',
                    employeeCode: '',
                    empDepartment: '',
                    employeeStatus: '',
                    searchKey: '',
                  };
                  setFilterData(cleared);
                  setFilterPopupOpen(false);
                  fetchEmployeeDetails();
                }}
              >
                Clear
              </button>
            </div>
          </div>
        </div>
      )}




      <h2>Employee Details</h2>
      <div className="table-container"> 
      <table className="employee-table">
        <thead className="employee-table-header">
          <tr className="employee-table-row header-row">
            <th>Employee Code</th>
            <th>Email</th>
            <th>Name</th>
            <th>Phone Number</th>
            <th>Department</th>
            <th>Gender</th>
            <th>Status</th>
            <th>Country</th>
            <th>State</th>
            <th>City</th>
            <th>Date Created</th>
            <th>Date Updated</th>
            <th>Created By</th>
            <th>Updated By</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
        {employeeDetails.map((employee) => (
            <tr key={employee.empCode} className="employee-table-row data-row">
              <td>{employee.empCode}</td>
              <td>{employee.email}</td>
              <td>{employee.name}</td>
              <td>{employee.phoneNumber}</td>
              <td>{employee.empDepartment.charAt(0).toUpperCase() + employee.empDepartment.slice(1).toLowerCase()}</td>
              <td>{employee.gender.charAt(0).toUpperCase() + employee.gender.slice(1).toLowerCase()}</td>
              <td>{employee.employeeStatus.charAt(0).toUpperCase() + employee.employeeStatus.slice(1).toLowerCase()}</td>
              <td>{employee.country}</td>
              <td>{employee.state}</td>
              <td>{employee.city}</td>
              <td>{employee.empCreatedDateTime}</td>
              <td>{employee.empUpdatedDateTime}</td>
              <td>{employee.createdBy.charAt(0).toUpperCase() + employee.createdBy.slice(1).toLowerCase()}</td>
              <td>{employee.updatedBy && employee.updatedBy.charAt(0).toUpperCase() + employee.updatedBy.slice(1).toLowerCase()}</td>
              <td className="action-column-cell">
                <button className="action-button-edit-button" onClick={() => handleEdit(employee.empCode)}>Edit</button>
                <button className="action-button-status-button" onClick={() => handleStatusChange(employee.empCode, employee.employeeStatus)}>{employee.employeeStatus === "ACTIVE" ? "Inactivate" : "Activate"}</button>
                <button className="action-button-rights-button" onClick={() => handleRightsMapping(employee.empCode)}>Manage Rights</button>
              </td>

            </tr>
          ))}
        </tbody>  
      </table>
    </div>
    
      <ToastContainer position="top-right"/>
    </div>
  );
}

export default Employee;
