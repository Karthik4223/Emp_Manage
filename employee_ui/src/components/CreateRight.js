import React from "react";
import { useState } from "react";

function CreateRight() {
    const [Right, setRight] = useState({
        rightName: ''
    });

    const [message, setMessage] = useState('');
    const [messageType, setMessageType] = useState('success');

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
            <div className="form-group">
                <label htmlFor="rightName">Right Name:</label>
                <input type="text" id="rightName" name="rightName" required value={Right.rightName} onChange={handleChange} />
            </div>
           
            <div className="form-group-button">
                <button type="submit">Create Right</button>
            </div>
        </form>

        {message && (
            <div className="error" style={ messageType === 'error' ? { color: 'red', marginBottom: '10px', marginTop: '10px' } : { color: 'green', marginBottom: '10px', marginTop: '10px' }}>
              {message}
            </div>
          )}

    </div>
  );
}

export default CreateRight;
