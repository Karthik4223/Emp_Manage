import React, { useState, useEffect, useContext } from "react";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useEmployeeService } from "../../services/employeeService";
import { useEmployeeRequestService } from "../../services/employeeRequestService";
import AuthContext from "../../context/AuthContext";


function CreateEmployee({ prefillData, onClose }) {
  const { updateEmployee } = useEmployeeService();
  const { createEmployee } = useEmployeeRequestService();
  const { username }= useContext(AuthContext);

  const [employeeRequestData, setEmployeeRequestData] = useState({
    empCode: '',
    employeeStatus: '',
    email: '',
    name: '',
    gender: '',
    empDepartment: '',
    phoneNumber: '',
    country: '',
    state: '',
    city: '',
    createdBy: username,
    updatedBy: ''
  });

  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('success');

  const [countries, setCountries] = useState([]);
  const [states, setStates] = useState([]);
  const [cities, setCities] = useState([]);

  const isEditMode = !!prefillData;
  const [isDisabled, setIsDisabled] = useState(isEditMode);

  const API_KEY = 'ME95TTJOZERmVG9nb3hKNFNtcEJIWHRNMmNqNzdqVFRNclFnTHhRNw==';

  useEffect(() => {
    fetchCountries();
  }, []);

  useEffect(() => {
    if (!prefillData) return;

    setEmployeeRequestData({
      empCode: prefillData.empCode || '',
      employeeStatus: prefillData.employeeStatus || '',
      email: prefillData.email || '',
      name: prefillData.name || '',
      phoneNumber: prefillData.phoneNumber || '',
      gender: prefillData.gender || '',
      empDepartment: prefillData.empDepartment || '',
      country: prefillData.country || '',
      state: prefillData.state || '',
      city: prefillData.city || '',
      createdBy: prefillData.createdBy || '',
      updatedBy: username
    });
  }, [prefillData, username]);

    useEffect(() => {
      if (employeeRequestData.country) {
        fetchStates(employeeRequestData.country);
      }
    }, [employeeRequestData.country]);

    useEffect(() => {
      if (employeeRequestData.country && employeeRequestData.state) {
        fetchCities(employeeRequestData.country, employeeRequestData.state);
      }
    }, [employeeRequestData.state, employeeRequestData.country]);


  useEffect(() => {
    if (message) {
      toast(message, { type: messageType === 'error' ? 'error' : 'success' });
      const timeout = setTimeout(() => setMessage(''), 3000);
      return () => clearTimeout(timeout);
    }
  }, [message, messageType]);

  const fetchCountries = async () => {
    try {
      const response = await fetch("https://api.countrystatecity.in/v1/countries", {
        headers: { "X-CSCAPI-KEY": API_KEY }
      });
      if (!response.ok) throw new Error(response.statusText);
      const data = await response.json();

      const india = data.find(country => country.iso2 === 'IN');
      if (india) {
        setCountries([india]);
      } else {
        setCountries([]);
      }
    } catch (error) {
      console.error("Error fetching countries:", error);
      setMessage('Error loading countries.');
      setMessageType('error');
    }
  };

  const fetchStates = async (countryIso2) => {
    try {
      const response = await fetch(`https://api.countrystatecity.in/v1/countries/${countryIso2}/states`, {
        headers: { "X-CSCAPI-KEY": API_KEY }
      });
      if (!response.ok) throw new Error(response.statusText);
      const data = await response.json();
      setStates(data);
    } catch (error) {
      console.error("Error fetching states:", error);
      setMessage('Error loading states.');
      setMessageType('error');
    }
  };

  const fetchCities = async (countryIso2, stateIso2) => {
    try {
      const response = await fetch(`https://api.countrystatecity.in/v1/countries/${countryIso2}/states/${stateIso2}/cities`, {
        headers: { "X-CSCAPI-KEY": API_KEY }
      });
      if (!response.ok) throw new Error(response.statusText);
      const data = await response.json();
      setCities(data);
    } catch (error) {
      console.error("Error fetching cities:", error);
      setMessage('Error loading cities.');
      setMessageType('error');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEmployeeRequestData(prev => ({ ...prev, [name]: value }));
  };


   const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage(""); setMessageType("");

    try {
      if (isEditMode) {
        await updateEmployee(employeeRequestData);
        toast.success("Employee updated successfully!");
      } else {
        let payload = { ...employeeRequestData };
        delete payload.empCode;
        delete payload.employeeStatus;
        await createEmployee(payload);
        setMessage("Employee created successfully!");
      }
      setMessageType("success");
      onClose?.();
      setEmployeeRequestData({
        empCode: '',
        employeeStatus: '',
        email: '',
        name: '',
        gender: '',
        empDepartment: '',
        phoneNumber: '',
        country: '',
        state: '',
        city: '',
      });
    } catch (err) {
      setMessage(err || "Error submitting form.");
      setMessageType("error");
    }
  };

 
  return (
    <div className="form-content">
      <div className="create-employee">
        <h2 className="form-title">{isEditMode ? 'Update Employee' : 'Create Employee'}</h2>

        {isEditMode && isDisabled && (
          <button className="btn btn-primary" type="button" onClick={() => setIsDisabled(false)} style={{ marginBottom: '15px' }}>
            Enable Editing
          </button>
        )}

        <form onSubmit={handleSubmit} className="employee-form">
          {isEditMode && (
            <div className="custom-form-group">
              <label>Employee Code:</label>
              <input
                type="text"
                name="empCode"
                value={employeeRequestData.empCode}
                className="form-control"
                disabled
              />
            </div>
          )}

            {isEditMode && (
            <div className="custom-form-group">
              <label>Status:</label>
              <input
                type="text"
                name="status"
                value={employeeRequestData.employeeStatus}
                className="form-control"
                disabled
              />
            </div>
          )}

          <div className="custom-form-group">
            <label>Email:</label>
            <input type="text" name="email" value={employeeRequestData.email} onChange={handleChange} className="form-control" placeholder="Enter email" required maxLength={100} disabled={isDisabled} />
          </div>

          <div className="custom-form-group">
            <label>Name:</label>
            <input type="text" name="name" value={employeeRequestData.name} onChange={handleChange} className="form-control" placeholder="Enter name" required maxLength={20} disabled={isDisabled} />
          </div>

          <div className="custom-form-group">
            <label>Phone Number:</label>
            <input type="text" name="phoneNumber" value={employeeRequestData.phoneNumber} onChange={handleChange} className="form-control" placeholder="Enter phone number" required maxLength={10} disabled={isDisabled} />
          </div>

          <div className="custom-form-group">
            <label>Gender:</label>
            <select className="form-control" name="gender" onChange={handleChange} value={employeeRequestData.gender} required disabled={isDisabled}>
              <option value="">Select gender</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
            </select>
          </div>

          <div className="custom-form-group">
            <label>Department:</label>
            <select className="form-control" name="empDepartment" onChange={handleChange} value={employeeRequestData.empDepartment} required disabled={isDisabled}>
              <option value="">Select department</option>
              <option value="hr">HR</option>
              <option value="development">Development</option>
              <option value="testing">Testing</option>
              <option value="management">Management</option>
              <option value="sales">Sales</option>
              <option value="marketing">Marketing</option>
            </select>
          </div>

          <div className="custom-form-group">
            <label>Country:</label>
            <select className="form-control" name="country" onChange={handleChange} value={employeeRequestData.country} required disabled={isDisabled}>
              <option value="">Select country</option>
              {countries.map((country) => (
                <option key={country.iso2} value={country.iso2}>{country.name}</option>
              ))}
            </select>
          </div>

          <div className="custom-form-group">
            <label>State:</label>
            <select className="form-control" name="state" onChange={handleChange} value={employeeRequestData.state} required disabled={isDisabled}>
              <option value="">Select state</option>
              {states.map((state) => (
                <option key={state.iso2} value={state.iso2}>{state.name}</option>
              ))}
            </select>
          </div>

          <div className="custom-form-group">
            <label>City:</label>
            <select className="form-control" name="city" onChange={handleChange} value={employeeRequestData.city} required disabled={isDisabled}>
              <option value="">Select city</option>
              {cities.map((city) => (
                <option key={city.name} value={city.name}>{city.name}</option>
              ))}
            </select>
          </div>

          <div className="custom-form-group-button" style={{ textAlign: "center" }}>
            <button type="submit" className="submit-button" disabled={isDisabled}>
              {isEditMode ? "Update" : "Create"}
            </button>
          </div>
        </form>

        <ToastContainer position="top-right" />
      </div>
    </div>
  );
}

export default CreateEmployee;
