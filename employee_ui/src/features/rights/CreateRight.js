import React from "react";
import { useState,useEffect } from "react";

import { useRightsService } from "../../services/rightsService";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";

function CreateRight() {
    const { createRight } = useRightsService();
    const { username } = useContext(AuthContext);

    const [Right, setRight] = useState({
        rightName: '',
        createdBy: username,
        group: ''
    });

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
        


      const handleFormSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        setMessageType("");

        try {
            await createRight(Right);
            setMessage("Right created successfully.");
            setMessageType("success");
            setRight({ rightName: "", group: "" });
        } catch (err) {
            setMessage(err || "Error creating right.");
            setMessageType("error");
        }
    };

   const handleChange = (e) => {
        const { name, value } = e.target;
        setRight(prev => ({ ...prev, [name]: value }));
    };

  return (
    <div className="content">
        <h2>Create Right</h2>
        <form className="create-right-form" onSubmit={handleFormSubmit}>
            <div className="custom-form-group">
                <label htmlFor="rightName">Right Name:</label>
                <input type="text" id="rightName" name="rightName" required value={Right.rightName} onChange={handleChange} />
            </div>

            <div className="custom-form-group">
            <label>Group:</label>
            <select className="form-control" name="group" onChange={handleChange} value={Right.group} required>
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
           
            <div className="custom-form-group-button">
                <button type="submit">Create Right</button>
            </div>
        </form>

        <ToastContainer position="top-right"/>
       

    </div>
  );
}

export default CreateRight;
