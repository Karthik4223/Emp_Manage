import React, { useState, useEffect, useMemo } from "react";
import { useParams } from "react-router-dom";

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useRightsService } from "../../services/rightsService";
import { useEmployeeRightsService } from "../../services/employeeRightsService";

function RightsMapping() {
  const { getAllRights } = useRightsService();
  const { getEmployeeRights, assignEmployeeRights } = useEmployeeRightsService();
  const { empCode } = useParams();

  const [allRights, setAllRights] = useState([]);
  const [selectedRight, setSelectedRight] = useState([]);

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
  

   useEffect(() => {
    const fetchRightsData = async () => {
      try {
        const rights = await getAllRights();
        setAllRights(rights);

        const empRights = await getEmployeeRights(empCode);
        setSelectedRight(empRights.rightCode || []);
      } catch (error) {
        setMessage(typeof error === 'string' ? error : "Error fetching rights");
        setMessageType('error');
      }
    };

    fetchRightsData();
  }, [empCode]);

   
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
    try {
      await assignEmployeeRights(empCode, selectedRight);
      setMessage('Rights assigned successfully.');
      setMessageType('success');
    } catch (error) {
      setMessage(typeof error === 'string' ? error : 'Error assigning rights.');
      setMessageType('error');
    }
  };


  return (
    <div className="content">
      <h2>Rights Mapping- {empCode}</h2>

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
      
      <ToastContainer position="top-right"/>

      <div className="custom-form-group-button">
        <button onClick={handleSubmit}>Assign Rights</button>
      </div>
    </div>
  );
}

export default RightsMapping;
