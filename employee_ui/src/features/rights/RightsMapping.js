import React, { useState, useEffect, useMemo } from "react";
import { useParams } from "react-router-dom";

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useRightsService } from "../../services/rightsService";
import { useEmployeeRightsService } from "../../services/employeeRightsService";
import { useSelector } from "react-redux";

function RightsMapping() {
  const { getAllRights } = useRightsService();
  const { getEmployeeRights, assignEmployeeRights } = useEmployeeRightsService();
  const { empCode } = useParams();
  const contextEmpCode = useSelector((state) => state.auth.empCode);

  const [allRights, setAllRights] = useState([]);
  const [selectedRight, setSelectedRight] = useState([]);
  const [group, setGroup] = useState('');
  const [mode, setMode] = useState("");
  const [selectedAvailableRight, setSelectedAvailableRight] = useState(null);
  const [selectedAssignedRight, setSelectedAssignedRight] = useState(null);

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
  


    const fetchRightsData = async () => {
      try {
        const rights = await getAllRights();
        const activeRights = rights.filter(right => right.rightStatus === 'ACTIVE');
        setAllRights(activeRights.sort ((a, b) => a.rightName.localeCompare(b.rightName)));

        const empRights = await getEmployeeRights(empCode);
        setSelectedRight(empRights.rightCode || []);
      } catch (error) {
        setMessage(error.message || "Error fetching rights");
        setMessageType('error');
      }
    };

  useEffect(() => {
    fetchRightsData();
  }, [empCode]);

  console.log("Selected Rights:", selectedRight);
  console.log("All Rights:", allRights);
   
  const availableRights = useMemo(() => {
    return allRights.filter(right => !selectedRight.includes(right.rightCode));
  }, [allRights, selectedRight]);


  const handleAssignRight = (rightCode) => {
    if (!selectedRight.includes(rightCode)) {
      setSelectedRight([...selectedRight, rightCode]);
      setSelectedAvailableRight(null);
      setSelectedAssignedRight(null);
    }
  };

  const handleRemoveRight = (rightCode) => {
    if (selectedRight.includes(rightCode)) {
      setSelectedRight(selectedRight.filter(code => code !== rightCode));
      setSelectedAssignedRight(null);
      setSelectedAvailableRight(null);
    }
  };

 
   const handleSubmit = async () => {
    if (mode === "group") {
      if (!group) return toast.error("Please select a group");

      try {
        await assignEmployeeRights(empCode, [], group, contextEmpCode);
        fetchRightsData();
        toast.success("Group rights assigned successfully");
      } catch (error) {
        toast.error(error.message || "Error assigning group rights");
      }
    } else if (mode === "selected") {
      try {
        await assignEmployeeRights(empCode, selectedRight, "", contextEmpCode);
        toast.success("Rights assigned successfully");
      } catch (error) {
        console.error("Error assigning rights:", error);
        toast.error(error.message || "Error assigning rights");
      }
    } else {
      toast.error("Select a mode to assign rights");
    }
  };


  return (
    <div className="content">
      <h2>Rights Mapping- {empCode}</h2>

      <h5 style={{ fontWeight: 'normal', fontStyle: 'italic', marginTop: '10px', marginBottom: '20px' }}>Assigns Rights Individually</h5>

      <div className="custom-form-group" style={{display: 'flex', justifyContent: 'center'}}>
        <div className="custom-form-group-button" style={{ marginBottom: '20px', gap: '10px', display: 'flex' }}>
        <button onClick={() => setMode("selected")}>Assign Selected Rights</button>
        {/* <button onClick={() => setMode("group")}>Assign by Group</button> */}
      </div>
     </div>

      {mode === "selected" && (
       <div className="rights-mapping-container">
        <div className="available-rights-mapping-container">
          <h3>Available Rights</h3>
          <div className="available-roles-list">
            {availableRights.map((right) => (
              <button 
                key={right.rightCode}
                onClick={() => setSelectedAvailableRight(right.rightCode)}
                style={{ backgroundColor: selectedAvailableRight === right.rightCode ? 'lightblue' : 'white' }}
              >
                {right.rightName}
              </button>
            ))}
          </div>
        </div>

        <div className="mapping-buttons">
          <button onClick={() => selectedAvailableRight && handleAssignRight(selectedAvailableRight)}> &gt; </button>
          <button onClick={() => selectedAssignedRight && handleRemoveRight(selectedAssignedRight)}> &lt; </button>
        </div>

        <div className="assigned-rights-mapping-container">
          <h3>Assigned Rights</h3>
          <div className="assigned-roles-list">
            {selectedRight.map((rightCode) => {
              const right = allRights.find(r => r.rightCode === rightCode);
              return (
                <button 
                  key={rightCode}
                  onClick={() => setSelectedAssignedRight(rightCode)}
                  style={{ backgroundColor: selectedAssignedRight === rightCode ? 'lightblue' : 'white' }}
                >
                  {right?.rightName}
                </button>
              );
            })}
          </div>

        </div>
      </div>
      )}

      {mode === "group" && (
        <div className="rights-mapping-group-container">
          <div className="custom-form-group">
            <label>Group:</label>
            <select className="form-control" name="group" onChange={(e) => setGroup(e.target.value)} value={group} required>
              <option value="">Select group</option>
              <option value="EMPLOYEE">Employee</option>
              <option value="MANAGER">Manager</option>
              <option value="ASSISTANTMANAGER">Assistant Manager</option>
              <option value="HR">HR</option>
              <option value="ADMIN">Admin</option>
              <option value="SUPERADMIN">Super Admin</option>
              <option value="USER">User</option>
            </select>
          </div>
        </div>
      )}

      <ToastContainer position="top-right"/>

      {mode && 
        <div className="custom-form-group-button">
          <button onClick={handleSubmit}>Assign Rights</button>
        </div>
      }
      
    </div>
  );
}

export default RightsMapping;
