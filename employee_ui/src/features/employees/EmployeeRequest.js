import React from "react";
import { useState,useEffect,useCallback } from "react";

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import { useEmployeeRequestService } from "../../services/employeeRequestService";

function EmployeeRequest() {
  const { getAllEmployeeRequests, updateEmployeeRequestStatus } = useEmployeeRequestService();
  const { rightsNames } = useContext(AuthContext) || [];
  const { username } = useContext(AuthContext);
  const [employeeRequests, setEmployeeRequests] = useState([]);
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('success');
  const [statusFilter, setStatusFilter] = useState("CREATED");

   useEffect(() => {
      if (message) {
        toast(message, { type: messageType === 'error' ? 'error' : 'success' });
    
        const timeout = setTimeout(() => {
          setMessage('');
        }, 3000);
    
        return () => clearTimeout(timeout);
      }
    }, [message, messageType]);
    

  useEffect(() => {
  if (message) {
    const timeout = setTimeout(() => {
      setMessage('');
    }, 3000);
    return () => clearTimeout(timeout);
  }
  }, [message]);

  const fetchEmployeeRequests = useCallback(async () => {
    try {
      const { data } = await getAllEmployeeRequests();
      setEmployeeRequests(data);
      } catch (error) {
        setMessage("Error fetching employee requests.");
        setMessageType("error");
      }
    }, [getAllEmployeeRequests]);

    useEffect(() => {
      fetchEmployeeRequests();
    }, []);



  const handleRequest = async (empRequestId, action) => {
    try {
      await updateEmployeeRequestStatus(empRequestId, action, username);
      setMessage(`Request ${action.toLowerCase()} successfully.`);
      setMessageType("success");
      fetchEmployeeRequests();
    } catch (error) {
      setMessage(error.response?.data || "Error updating employee request.");
      setMessageType("error");
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
                      {rightsNames?.includes("RIGHT_EMPLOYEE_APPROVE_REQUEST") && (
                        <>
                          <button
                            className="action-button-approve"
                            onClick={() => handleRequest(request.empRequestId, "APPROVED")}
                          >
                            Approve
                          </button>
                          <button
                            className="action-button-reject"
                            onClick={() => handleRequest(request.empRequestId, "REJECTED")}
                          >
                            Reject
                          </button>
                        </>
                      )}
                    </td>
              ) : <td className="action-column-cell">{ "N/A" }</td>}

            </tr>
          ))}
        </tbody>  
      </table>

      <ToastContainer position="top-right"/>

    </div>
  );
}

export default EmployeeRequest;
