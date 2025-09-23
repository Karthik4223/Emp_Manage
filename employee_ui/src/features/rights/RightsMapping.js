import React, { useState, useEffect, useMemo } from "react";
import { useParams } from "react-router-dom";

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function RightsMapping() {
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
    const fetchRights = async () => {
      try {
        const response = await fetch('/rights/getAllRights');
        if (response.ok){
            const data = await response.json();
            setAllRights(data);
        } else {
            const data = await response.text();
            setMessage(data);
            setMessageType('error');
        }
      } catch (error) {
        setMessage("Error fetching rights");
        setMessageType("error");
      }
    };

    fetchRights();
  }, []);

  useEffect(() => {
    const fetchEmployeeRights = async () => {
      try {
        const response = await fetch(`/employeeRights/getEmployeeRights/${empCode}`);
        if (response.ok) {
          const data = await response.json();
          setSelectedRight(data.rightCode || []);
        } else {
          const data = await response.text();
          setMessage(data);
          setMessageType('error');
        }
      } catch (error) {
        setMessage("Error fetching rights");
        setMessageType("error");
      }
    };

    fetchEmployeeRights();
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
    const payload = {
      empCode,
      rightCode: selectedRight,
    };

    try {
      const response = await fetch('/employeeRights/addEmployeeRights', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        setMessage('Rights assigned successfully.');
        setMessageType('success');
      } else {
        const data = await response.text();
        setMessage(data);
        setMessageType('error');
      }
    } catch (error) {
      setMessage('Error assigning rights.');
      setMessageType('error');
    }
  };

  return (
    <div className="content">
      <h2>Rights Mapping</h2>

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
