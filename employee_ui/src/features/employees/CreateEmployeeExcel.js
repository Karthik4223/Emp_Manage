
import { AuthContext } from "../../context/AuthContext";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import React, { useState,useContext } from "react";
import { useEmployeeRequestService } from "../../services/employeeRequestService";

function CreateEmployeeExcel() {
  const { empCode } = useContext(AuthContext);
  const [file, setFile] = useState(null);
  const { uploadEmployeeExcel } = useEmployeeRequestService();

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    try {
      await uploadEmployeeExcel(formData, empCode);
      toast.success("File uploaded successfully!");
      setFile(null);
    } catch (err) {
      toast.error(err.message || "Failed to upload file.");
    }
  };

  return (
    <div className="content">
      <h2>Upload Employee Excel</h2>
      <input type="file" accept=".xlsx,.xls" onChange={handleFileChange} />

      <div className="custom-form-group-button">
        <button className="submit-button" onClick={handleSubmit} disabled={!file}>
          Submit
        </button>
      </div>

      <ToastContainer position="top-right" />
      <div className="instructions-container">
          <div className="instructions">
          <h4>Instructions</h4>
          <p>Please upload an Excel file containing employee data.</p>
          <p>The file should have the following columns:</p>
          <p><strong>Email, Name, Phone Number, Gender, Department, Country, State, City</strong></p>
          <p>Ensure that the data is accurate and complete before uploading.</p>
          <p>Only <strong>.xlsx</strong> and <strong>.xls</strong> file formats are supported.</p>
        </div>
      </div>

    </div>
  );
}

export default CreateEmployeeExcel;
