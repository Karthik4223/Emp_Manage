import React from "react";
import { useState,useEffect } from "react";

function Employee({onNavigate}) {
const [employeeDetails, setEmployeeDetails] = useState([]);
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('success');

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



  useEffect(() => {
    fetchEmployeeDetails();
  }, []);

  useEffect(() => {
  if (message) {
    const timeout = setTimeout(() => {
      setMessage('');
    }, 3000);
    return () => clearTimeout(timeout);
  }
  }, [message]);

  const fetchEmployeeDetails = async () => {
    try {
      const response = await fetch("employee/getAllEmployees");
      if (!response.ok) throw new Error(response.statusText);
      const data = await response.json();
      setEmployeeDetails(data);
    } catch (error) {
      setMessage('Error fetching employee details.');
      setMessageType('error');
    }
  };

  const handleEdit = (empCode) => {
  }

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
        fetchEmployeeDetails();
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
      setMessage("Filter applied successfully.");
      setMessageType("success");
    } else {
      const errorText = await response.text();
      setMessage(errorText);
      setMessageType("error");
    }
  } catch (error) {
    console.error("Error fetching filtered employees:", error);
    setMessage("Failed to apply filter.");
    setMessageType("error");
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

          <div className="form-group">
            <label>Name:</label>
            <input
              name="name"
              value={filterData.name}
              onChange={(e) => setFilterData({ ...filterData, name: e.target.value })}
            />
          </div>

          <div className="form-group">
            <label>Email:</label>
            <input
              name="email"
              value={filterData.email}
              onChange={(e) => setFilterData({ ...filterData, email: e.target.value })}
            />
          </div>

          <div className="form-group">
            <label>Phone Number:</label>
            <input
              name="phoneNumber"
              value={filterData.phoneNumber}
              onChange={(e) => setFilterData({ ...filterData, phoneNumber: e.target.value })}
            />
          </div>

          <div className="form-group">
            <label>Emp Code:</label>
            <input
              name="employeeCode"
              value={filterData.employeeCode}
              onChange={(e) => setFilterData({ ...filterData, employeeCode: e.target.value })}
            />
          </div>


          <div className="form-group">
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

          <div className="form-group">
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

          <div className="form-group">
            <label>Search Key:</label>
            <input
              name="searchKey"
              value={filterData.searchKey}
              onChange={(e) => setFilterData({ ...filterData, searchKey: e.target.value })}
            />
          </div>


          <div className="form-group" style={{ marginTop: '10px' }}>
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

      {message && (
        <div className="error" style={ messageType === 'error' ? { color: 'red', marginBottom: '10px', marginTop: '10px' } : { color: 'green', marginBottom: '10px' , marginTop: '10px' }}>
          {message}
        </div>
      )}
    </div>
  );
}

export default Employee;
