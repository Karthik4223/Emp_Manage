import React, { useState, useContext } from "react";
import { ToastContainer, toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import {jwtDecode} from "jwt-decode";

const LoginPage = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!username || !password) {
      toast.error("Please enter both employee code and password");
      return;
    }

    try {
      const response = await fetch("/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ empCode: username, password }),
      });

      if (response.ok) {
        const data = await response.json();
        login(data.token, data.rightsCodes, data.rightsNames);
        toast.success(`Logged in as ${jwtDecode(data.token).empName}`);
        navigate("/home");
        
      } else {
        const errorData = await response.text();
        toast.error(errorData);
      }
    } catch (err) {
      toast.error("Login failed. Please try again.");
    }
  };

  return (
    <div className="content">
      <div className="create-employee">
        <h2 className="form-title">Login</h2>
        <form onSubmit={handleSubmit} className="employee-form">
          <div className="custom-form-group">
            <label>Employee Code:</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter employee code"
            />
          </div>
          <div className="custom-form-group">
            <label>Password:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter password"
            />
          </div>
          <div className="custom-form-group-button">
            <button type="submit">Login</button>
          </div>
        </form>
        <ToastContainer position="top-right" autoClose={3000} />
      </div>
    </div>
  );
};

export default LoginPage;
