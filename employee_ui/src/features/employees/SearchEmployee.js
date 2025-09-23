import React, { useState } from "react";
import { useEffect } from "react";

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function SearchEmployee({ onNavigate }) {
  const [employeeSearchData, setEmployeeSearchData] = useState({
    empCode: '',
    email: '',
    name: '',
    gender: '',
    empDepartment: '',
    phoneNumber: '',
    country: '',
    state: '',
    city: '',
  });

  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("success");
  const [searchResults, setSearchResults] = useState([]);

  useEffect(() => {
    if (message) {
      toast(message, { type: messageType === 'error' ? 'error' : 'success' });
  
      const timeout = setTimeout(() => {
        setMessage('');
      }, 3000);
  
      return () => clearTimeout(timeout);
    }
  }, [message, messageType]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEmployeeSearchData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const payload = {
      employeeNames: employeeSearchData.name ? [employeeSearchData.name] : [],
      employeeEmail: employeeSearchData.email ? [employeeSearchData.email] : [],
      employeePhoneNumber: employeeSearchData.phoneNumber ? [employeeSearchData.phoneNumber] : [],
      employeeDepartment: employeeSearchData.empDepartment ? [employeeSearchData.empDepartment] : [],
      searchKey: employeeSearchData.searchKey || null,
      employeeStatus: employeeSearchData.employeeStatus || null,
    };

    try {
      const response = await fetch('/employee/search', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        const data = await response.json();
        setSearchResults(data);
        setMessage("Search successful");
        setMessageType("success");
      } else {
        const data = await response.text();
        setMessage(data);
        setMessageType("error");
      }
    } catch (error) {
      setMessage("Search failed. Please try again.");
      setMessageType("error");
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
        setMessage(`Employee: ${empCode} ${currentStatus.toLowerCase()}d successfully.`);
        setMessageType('success');
      }else{
        const data = await response.text();
        setMessage(data);
        setMessageType('error');
      }

      } catch (error) {
        console.error("Error updating employee status:", error);
      }
    };

    updateStatus();
  }

  const handleRightsMapping = (empCode) => {
    onNavigate(`rights-mapping/${empCode}`);
  }
  const handleEdit = (empCode) => {
  }


  return (
    <div className="content">
      <div className="create-employee">
        <h3 className="form-title">Search Employee</h3>
        <form onSubmit={handleSubmit} className="employee-form">
          <div className="custom-form-group">
            <label>Email:</label>
            <input
              type="text"
              name="email"
              value={employeeSearchData.email}
              onChange={handleChange}
              className="form-control"
              placeholder="Enter email"
              maxLength={100}
            />
          </div>

          <div className="custom-form-group">
            <label>Name:</label>
            <input
              type="text"
              name="name"
              value={employeeSearchData.name}
              onChange={handleChange}
              className="form-control"
              placeholder="Enter name"
              maxLength={20}
            />
          </div>

          <div className="custom-form-group">
            <label>Phone Number:</label>
            <input
              type="text"
              name="phoneNumber"
              value={employeeSearchData.phoneNumber}
              onChange={handleChange}
              className="form-control"
              placeholder="Enter phone number"
              maxLength={10}
            />
          </div>

          <div className="custom-form-group">
            <label>Gender:</label>
            <select
              className="form-control"
              name="gender"
              onChange={handleChange}
              value={employeeSearchData.gender}
            >
              <option value="">Select gender</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
            </select>
          </div>

          <div className="custom-form-group">
            <label>Department:</label>
            <select
              className="form-control"
              name="empDepartment"
              onChange={handleChange}
              value={employeeSearchData.empDepartment}
            >
              <option value="">Select department</option>
              <option value="hr">HR</option>
              <option value="development">Development</option>
              <option value="testing">Testing</option>
              <option value="management">Management</option>
              <option value="sales">Sales</option>
              <option value="marketing">Marketing</option>
            </select>
          </div>

          <div className="custom-form-group">
            <label>Search Key:</label>
            <input
              type="text"
              name="searchKey"
              value={employeeSearchData.searchKey}
              onChange={handleChange}
              className="form-control"
              placeholder="Enter search key"
              maxLength={50}
            />
          </div>

          <div className="custom-form-group">
            <label>Employee Status:</label>
            <select
              className="form-control"
              name="employeeStatus"
              onChange={handleChange}
              value={employeeSearchData.employeeStatus}
            >
              <option value="">Select status</option>
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
            </select>
          </div>

          <div className="custom-form-group-button">
            <button type="submit">Search</button>
          </div>
        </form>

        <ToastContainer position="top-right" />
      </div>

      {searchResults.length > 0 && (
        <div className="search-results">
       <h4>Results:</h4>
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
          {searchResults.map((employee) => (
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
          </div>
        )}
    </div>
  );
}

export default SearchEmployee;
