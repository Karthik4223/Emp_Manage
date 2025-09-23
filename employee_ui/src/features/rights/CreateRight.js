import React from "react";
import { useState,useEffect } from "react";


import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


function CreateRight() {
    const [Right, setRight] = useState({
        rightName: ''
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
        

    const handleFormSubmit = (e) => {
        e.preventDefault();

        const submitRight = async () => {
            try {
                const response = await fetch('rights/addRight', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(Right),
                });

                if (response.ok) {
                    setMessage('Right created successfully.');
                    setMessageType('success');
                    setRight({ rightName: '' });
                } else {
                    const data = await response.text();
                    setMessage(data);
                    setMessageType('error');
                }
            } catch (error) {
                setMessage('Error creating right.');
                setMessageType('error');
            }
        };

        submitRight();
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
           
            <div className="custom-form-group-button">
                <button type="submit">Create Right</button>
            </div>
        </form>

        <ToastContainer position="top-right"/>
       

    </div>
  );
}

export default CreateRight;
