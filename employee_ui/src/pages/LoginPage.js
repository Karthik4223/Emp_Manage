import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { setAuthData } from "../features/auth/authSlice";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const LoginPage = () => {
  const [empCode, setEmpCode] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ empCode, password }),
      });

      if (response.ok) {
        const data = await response.json();
        console.log("Login successful:", data);
        dispatch(
          setAuthData({
            empCode: data.empCode,
            empName: data.empName,
            rights: data.rightsNames,
          })
        );
        navigate("/home");
      } else {
        const errorData = await response.text();
        toast.error(errorData);
      }
    } catch (error) {
      toast.error(error.message || "Login failed. Please try again.");
      console.error("Login failed:", error);
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
              value={empCode}
              onChange={(e) => setEmpCode(e.target.value)}
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
