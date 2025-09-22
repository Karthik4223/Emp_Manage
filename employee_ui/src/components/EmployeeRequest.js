import React from "react";
import { useState,useEffect } from "react";

function EmployeeRequest() {
  const [employeeRequests, setEmployeeRequests] = useState([]);
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('success');
  const [statusFilter, setStatusFilter] = useState("ALL");


  useEffect(() => {
    fetchEmployeeRequests();
  }, []);

  useEffect(() => {
  if (message) {
    const timeout = setTimeout(() => {
      setMessage('');
    }, 3000);
    return () => clearTimeout(timeout);
  }
  }, [message]);


  const fetchEmployeeRequests = async () => {
    try {
      const response = await fetch("/employeeRequest/getAllEmployeeRequests");
      if (!response.ok) throw new Error(response.statusText);

      const data = await response.json();
      setEmployeeRequests(data);
    } catch (error) {
      setMessage('Error fetching employee requests.');
      setMessageType('error');
    }
  };

  const handleRequest = async (empRequestId, action) => {
    try {
      const response = await fetch(`/employeeRequest/updateEmployeeRequestStatus/${empRequestId}?newStatus=${action}&updatedBy=XYZ`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (response.ok){
        setMessage(`Request ${action.toLowerCase()} successfully.`);
        setMessageType('success');
        fetchEmployeeRequests();
      }else{
        const data = await response.text();
        setMessage(data);
        setMessageType('error');
      }

    } catch (error) {
      setMessage('Error updating employee request.');
      setMessageType('error');
    }
  };

  return (
    <div className="content">
      <h2>Employee Requests</h2>

      <div className="filter-container" style={{ marginBottom: '20px' }}>
        <label htmlFor="statusFilter" style={{ marginRight: '10px' }}>Filter by Status:</label>
        <select
          id="statusFilter"
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
        >
          <option value="ALL">All</option>
          <option value="CREATED">Created</option>
          <option value="APPROVED">Approved</option>
          <option value="REJECTED">Rejected</option>
        </select>
      </div>


      <table className="employee-table">
        <thead className="employee-table-header">
          <tr className="employee-table-row header-row">
            <th>Request ID</th>
            <th>Email</th>
            <th>Name</th>
            <th>Phone Number</th>
            <th>Gender</th>
            <th>Status</th>
            <th>Department</th>
            <th>Country</th>
            <th>State</th>
            <th>City</th>
            <th>Date Created</th>
            <th>Date Updated</th>
            <th>Created By</th>
            <th>Updated By</th>
            <th >Actions</th>
          </tr>
        </thead>
        <tbody>
          {employeeRequests
            .filter((request) =>
              statusFilter === "ALL" ? true : request.empRequestStatus === statusFilter
            )
            .map((request) => (
            <tr key={request.empRequestId} className="employee-table-row data-row">
              <td>{request.empRequestId}</td>
              <td>{request.email}</td>
              <td>{request.name}</td>
              <td>{request.phoneNumber}</td>
              <td>{request.gender.charAt(0).toUpperCase() + request.gender.slice(1).toLowerCase()}</td>
              <td>{request.empRequestStatus.charAt(0).toUpperCase() + request.empRequestStatus.slice(1).toLowerCase()}</td>
              <td>{request.empDepartment.charAt(0).toUpperCase() + request.empDepartment.slice(1).toLowerCase()}</td>
              <td>{request.country}</td>
              <td>{request.state}</td>
              <td>{request.city}</td>
              <td>{request.empCreatedDateTime}</td>
              <td>{request.empUpdatedDateTime}</td>
              <td>{request.createdBy.charAt(0).toUpperCase() + request.createdBy.slice(1).toLowerCase()}</td>
              <td>{request.updatedBy && request.updatedBy.charAt(0).toUpperCase() + request.updatedBy.slice(1).toLowerCase()}</td>
              {request.empRequestStatus === "CREATED" ? (
                <td className="action-column-cell">
                  <button className="action-button-approve" onClick={() => handleRequest(request.empRequestId, "APPROVED")}>Approve</button>
                  <button className="action-button-reject" onClick={() => handleRequest(request.empRequestId, "REJECTED")}>Reject</button>
                </td>
              ) : <td className="action-column-cell">{ "N/A" }</td>}

            </tr>
          ))}
        </tbody>  
      </table>


     {message && (
        <div className="error" style={ messageType === 'error' ? { color: 'red', marginBottom: '10px', marginTop: '10px' } : { color: 'green', marginBottom: '10px', marginTop: '10px' }}>
          {message}
        </div>
      )}

    </div>
  );
}

export default EmployeeRequest;
