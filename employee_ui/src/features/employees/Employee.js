import React, { useContext } from "react";
import { useState,useEffect,useCallback } from "react";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import CreateEmployee from "./CreateEmployee";

import { AsyncTypeahead } from 'react-bootstrap-typeahead';
import 'react-bootstrap-typeahead/css/Typeahead.css';
import { AuthContext } from "../../context/AuthContext";
import { useEmployeeService } from "../../services/employeeService";


function Employee({onNavigate}) {
  const { getAllEmployees, updateEmployeeStatus, searchEmployees } = useEmployeeService();
  const { rightsNames, token } = useContext(AuthContext) || [];
  const [employeeDetails, setEmployeeDetails] = useState([]);
  const [editPopupOpen, setEditPopupOpen] = useState(false);
  const [employeeToEdit, setEmployeeToEdit] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const { username } = useContext(AuthContext);

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
  const [searchKeyOptions, setSearchKeyOptions] = useState([]);
  const [departmentOptions, setDepartmentOptions] = useState([]);
  const [statusOptions, setStatusOptions] = useState([]);


  
  const fetchEmployeeDetails = useCallback(async () => {
  try {
    const  data  = await getAllEmployees();
    setEmployeeDetails(data);
  } catch (error) {
    toast.error("Error fetching employee details.");
  } 
  }, [getAllEmployees]);


    useEffect(() => {
      fetchEmployeeDetails();
    }, []);

  const handleEdit = (empCode) => {
    const emp = employeeDetails.find((e) => e.empCode === empCode);
    if (emp) {
      setEmployeeToEdit(emp);
      setEditPopupOpen(true);
    }
  };

  const handleStatusChange = async (empCode, currentStatus) => {
    const newStatus = currentStatus === "ACTIVE" ? "INACTIVE" : "ACTIVE";
    try {
      await updateEmployeeStatus(empCode, newStatus, username);

      toast.success(`Employee: ${empCode} ${currentStatus.toLowerCase()}d successfully.`);
      filterPopupOpen || Object.values(filterData).some((v) => v) ? fetchFilteredEmployees() : fetchEmployeeDetails();
    } catch (error) {
      toast.error(error || "Error updating employee status.");
    }
  };

  const handleRightsMapping = (empCode) => {
    onNavigate(`rights-mapping/${empCode}`);
  }

    const fetchFilteredEmployees = async () => {
      const payload = {};

      if (filterData.name) payload.employeeNames = [filterData.name];
      if (filterData.email) payload.employeeEmail = [filterData.email];
      if (filterData.phoneNumber) payload.employeePhoneNumber = [filterData.phoneNumber];
      if (filterData.employeeCode) payload.employeeCode = [filterData.employeeCode];
      if (filterData.empDepartment) payload.employeeDepartment = [filterData.empDepartment];
      if (filterData.searchKey) payload.searchKey = filterData.searchKey;
      if (filterData.employeeStatus) payload.employeeStatus = filterData.employeeStatus;

      try {
        console.log("Applying filter with data:", payload);
        const data = await searchEmployees(payload, token);
        console.log("Filtered employees:", data);
        setEmployeeDetails(data.data);
        toast.success("Filter applied successfully.");
      } catch (error) {
        console.error(error);
        toast.error(error?.response?.data?.message || "Failed to apply filter.");
      }
    };


  const clearedSearch = {
    name: '',
    email: '',
    phoneNumber: '',
    employeeCode: '',
    empDepartment: '',
    employeeStatus: '',
    searchKey: '',
  };
 

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
              toast.success("Employee updated successfully.");
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
                isLoading={isLoading}
                minLength={2}
                labelKey="name"
                onSearch={async (query) => {
                  if (!query) {
                    // fetchEmployeeDetails();
                    return;
                  }
                  try {
                    setIsLoading(true);
                    const payload = { employeeNames: [query] };
                    const { data } = await searchEmployees(payload, token);
                    // setEmployeeDetails(data);
                    setNameOptions(data.map((emp) => emp.name));
                    
                  } catch (error) {
                    console.error(error);
                  }
                  finally {
                    setIsLoading(false);
                  }
                }}
                onChange={(selected) =>
                    setFilterData((prev) => ({ ...prev, name: selected[0] || '' }))
                  }
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
                  if (!query) {
                    // fetchEmployeeDetails();
                    return;
                  }
                  try {
                    const payload = { employeeEmail: [query] };
                    const { data } = await searchEmployees(payload,token);
                    // setEmployeeDetails(data);
                    setEmailOptions(data.map((emp) => emp.email));
                    
                  } catch (error) {
                    console.error(error);
                  }
                }}
                onChange={(selected) => setFilterData((prev) => ({ ...prev, email: selected[0] || '' }))}
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
                  if (!query) {
                    // fetchEmployeeDetails();
                    return;
                  }
                  try {
                    const payload = { employeePhoneNumber: [query] };
                    const { data } = await searchEmployees(payload,token);
                    // setEmployeeDetails(data);
                    setPhoneOptions(data.map((emp) => emp.phoneNumber));
                    
                  } catch (error) {
                    console.error(error);
                  }
                }}
                onChange={(selected) => setFilterData((prev) => ({ ...prev, phoneNumber: selected[0] || '' }))}
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
                  if (!query) {
                    // fetchEmployeeDetails();
                    return;
                  }
                  try {
                    const payload = { employeeCode: [query] };
                    const { data } = await searchEmployees(payload,token);
                    // setEmployeeDetails(data);
                    setEmpCodeOptions(data.map((emp) => emp.empCode));
                    
                  } catch (error) {
                    console.error(error);
                  }
                }}
                onChange={(selected) => setFilterData((prev) => ({ ...prev, employeeCode: selected[0] || '' }))}
                options={empCodeOptions || []}
                placeholder="Type to search..."
              />
            </div>

           {/* Department */}
            <div className="custom-form-group">
              <label>Department:</label>
              <AsyncTypeahead
                id="department-search"
                isLoading={false}
                minLength={1}
                labelKey="empDepartment"
                onSearch={async (query) => {
                  if (!query) {
                    // fetchEmployeeDetails();
                    return;
                  }
                  try {
                    const payload = { employeeDepartment: [query] };
                    const { data } = await searchEmployees(payload, token);
                    // setEmployeeDetails(data);
                    setDepartmentOptions([...new Set(data.map((emp) => emp.empDepartment))]);
                  } catch (error) {
                    console.error(error);
                  }
                }}

                onChange={(selected) =>
                  setFilterData((prev) => ({ ...prev, empDepartment: selected[0] || '' }))
                }
                options={departmentOptions || []}
                placeholder="Search department..."
              />
            </div>

            {/* Status */}
            <div className="custom-form-group">
              <label>Status:</label>
              <AsyncTypeahead
                id="status-search"
                isLoading={false}
                minLength={1}
                labelKey="empStatus"
                onSearch={ async(query) => {
                  if (!query) {
                    // fetchEmployeeDetails();
                    return;
                  }

                  try {
                    const payload = { employeeStatus: query };
                    const { data } = await searchEmployees(payload, token);
                    // setEmployeeDetails(data);
                    setStatusOptions([...new Set(data.map((emp) => emp.employeeStatus))]);
                  } catch (error) {
                    console.error(error);
                  }
                  
                }}

                onChange={(selected) =>
                  setFilterData((prev) => ({ ...prev, employeeStatus: selected[0] || '' }))
                }
                options={statusOptions || []}
                placeholder="Search status..."
              />
            </div>


            {/* Search Key */}
            <div className="custom-form-group">
              <label>Search Key:</label>
              <AsyncTypeahead
                id="searchkey-search"
                isLoading={isLoading}
                minLength={2}
                labelKey={(option) =>
                `${option.name || ""} | ${option.empCode || ""} | ${option.email || ""}`
              }
              onSearch={async (query) => {
                if (!query) {
                    // fetchEmployeeDetails();
                    return;
                  }
                try {
                  setIsLoading(true);
                  
                  const payload = { searchKey: query };
                  const { data } = await searchEmployees(payload,token);
                  // setEmployeeDetails(data);
                  setSearchKeyOptions(data);
                  
                } catch (error) {
                  console.error(error);
                } finally {
                  setIsLoading(false);
                }
              }}

              onChange={(selected) =>
                setFilterData((prev) => ({
                  ...prev,
                  searchKey: selected[0] ? (selected[0].name || selected[0].empCode || selected[0].email) : ""
                }))
              }
              options={searchKeyOptions || []}
              placeholder="Search by name, emp code, or email..."
            />
            </div>

            <div className="custom-form-group-button" style={{ marginTop: '10px' }}>
              <button
                onClick={() => {
                  console.log('Applying filter with data:', filterData);
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
        {employeeDetails && employeeDetails.length > 0 ? (
          employeeDetails.map((employee) => (
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
              {rightsNames?.includes("RIGHT_EMPLOYEE_EDIT") && (
                <button
                  className="action-button-edit-button"
                  onClick={() => handleEdit(employee.empCode)}
                  disabled={employee.employeeStatus !== "ACTIVE"}
                >
                  Edit
                </button>
              )}

              {rightsNames?.includes("RIGHT_EMPLOYEE_CHANGE_STATUS") && (
                <button
                  className="action-button-status-button"
                  onClick={() => handleStatusChange(employee.empCode, employee.employeeStatus)}
                >
                  {employee.employeeStatus === "ACTIVE" ? "Inactivate" : "Activate"}
                </button>
              )}

              {rightsNames?.includes("RIGHT_EMPLOYEE_CREATE_RIGHTS") && (
                <button
                  className="action-button-edit-button"
                  onClick={() => handleRightsMapping(employee.empCode)}
                  disabled={employee.employeeStatus !== "ACTIVE"}
                >
                  Manage Rights
                </button>
              )}
            </td>


            </tr>
          ))
        ) : (
          <tr>
            <td colSpan="15" style={{ textAlign: 'center' }}>No employees found.</td>
          </tr>
        )}
        </tbody>  
      </table>
    </div>
    
      <ToastContainer position="top-right"/>
    </div>
  );
}

export default Employee;
