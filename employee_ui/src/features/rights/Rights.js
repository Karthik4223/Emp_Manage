import React from "react";
import { useState,useEffect } from "react";

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';



function Rights() {
  const [rights, setRights] = useState([]);
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('success');

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
    fetchRights();
  }, []);

  useEffect(() => {
  if (message) {
    const timeout = setTimeout(() => {
      setMessage('');
    }, 3000);
    return () => clearTimeout(timeout);
  }
  }, [message]);

   const fetchRights = async () => {
      try {
        const response = await fetch("rights/getAllRights");
        const data = await response.json();
        setRights(data);
      } catch (error) {
        setMessage('Error fetching rights');
        setMessageType('error');
      }
    };

  const handleStatusChange = (rightCode, currentStatus) => {
    const newStatus = currentStatus === "ACTIVE" ? "INACTIVE" : "ACTIVE";

    const updateStatus = async () => {
      try {
        const response = await fetch(`rights/updateRightStatus/${rightCode}?newStatus=${newStatus}&updatedBy=XYZ`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
        });

        if (response.ok){
        setMessage(`Right: ${rightCode} ${currentStatus.toLowerCase()}d successfully.`);
        setMessageType('success');
        fetchRights();
      }else{
        const data = await response.text();
        setMessage(data);
        setMessageType('error');
      }

      } catch (error) {
        setMessage('Error updating right status');
        setMessageType('error');
      }
    };

    updateStatus();
  }
  
  if (!rights) {
    return <div className="content">Loading rights...</div>;
  }

  return (
    <div className="content">
      <h2>Rights</h2>
      <table className="employee-table">
        <thead className="employee-table-header">
          <tr className="employee-table-row header-row">
            <th>Right Code</th>
            <th>Right Name</th>
            <th>Status</th>
            <th>Created Date </th>
            <th>Updated Date</th>
            <th>Created By</th>
            <th>Updated By</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody className="employee-table-body">
          {rights.map((right) => (
            <tr key={right.rightCode} className="employee-table-row data-row">
              <td>{right.rightCode}</td>
              <td>{right.rightName}</td>
              <td>{right.rightStatus}</td>
              <td>{right.rightCreatedDateTime}</td>
              <td>{right.rightUpdatedDateTime}</td>
              <td>{right.createdBy}</td>
              <td>{right.updatedBy}</td>
              <td className="action-buttons">
                <button className="action-button-status-button" onClick={() => handleStatusChange(right.rightCode, right.rightStatus)}>{right.rightStatus === 'ACTIVE' ? 'Inactivate' : 'Activate'}</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <ToastContainer position="top-right"/>

    </div>
  );
}

export default Rights;