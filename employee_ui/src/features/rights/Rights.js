import React from "react";
import { useState,useEffect,useCallback } from "react";

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useRightsService } from "../../services/rightsService";
import { useSelector,useDispatch } from "react-redux";
import { clearAuthData } from "../../features/auth/authSlice";

function Rights() {
  const { getAllRights, updateRightStatus } = useRightsService();
  const [rights, setRights] = useState([]);
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('success');
  const empCode = useSelector((state) => state.auth.empCode);
  const rightsNames = useSelector((state) => state.auth.rights);
  const dispatch = useDispatch();

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

   const fetchRights = useCallback(async () => {
    try {
      const data = await getAllRights();
      setRights(data);
    } catch (err) {
      if(err.message === "Session invalid or expired" || err.message === "No session found"){
        toast.error("Session expired. Logging out...",{
          onClose: () => dispatch(clearAuthData()),
          autoClose: 1500,
        });

        return;
      }
      setMessage(err.message);
      setMessageType("error");
    }
  }, [getAllRights]);

   useEffect(() => {
    fetchRights();
  }, []);

  const handleStatusChange = async (rightCode, currentStatus) => {
    const newStatus = currentStatus === "ACTIVE" ? "INACTIVE" : "ACTIVE";
    try {
      await updateRightStatus(rightCode, newStatus, empCode);
      setMessage(`Right: ${rightCode} ${currentStatus.toLowerCase()}d successfully.`);
      setMessageType("success");
      fetchRights();
    } catch (err) {
      setMessage(err.message || "Error updating right status");
      setMessageType("error");
    }
  };
  
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
            {/* <th>Group</th> */}
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
              <td>{right.rightStatus[0] + right.rightStatus.slice(1).toLowerCase()}</td>
              {/* <td>{right.group || 'N/A'}</td> */}
              <td>{right.rightCreatedDateTime}</td>
              <td>{right.rightUpdatedDateTime}</td>
              <td>{right.createdBy}</td>
              <td>{right.updatedBy}</td>
            
            {rightsNames?.includes("RIGHT_RIGHTS_CHANGE_STATUS") && (
              <td className="action-buttons">
                <button className="action-button-status-button" onClick={() => handleStatusChange(right.rightCode, right.rightStatus)}>{right.rightStatus === 'ACTIVE' ? 'Inactivate' : 'Activate'}</button>
              </td>
            )}
            </tr>
          ))}
        </tbody>
      </table>

      <ToastContainer position="top-right"/>

    </div>
  );
}

export default Rights;