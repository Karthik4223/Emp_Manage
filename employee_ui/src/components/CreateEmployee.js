import React from "react";
import { useState,useEffect } from "react";

function CreateEmployee() {
   const [employeeRequestData, setEmployeeRequestData] = useState({
        email: '',
        name: '',
        gender: '',
        empDepartment: '',
        phoneNumber: '',
        country: '',
        state: '',
        city: '',
    });

    const [message, setMessage] = useState('');
    const [messageType, setMessageType] = useState('success');

    const [countries, setCountries] = useState([]);
    const [states, setStates] = useState([]);
    const [cities, setCities] = useState([]);

    const API_KEY = 'ME95TTJOZERmVG9nb3hKNFNtcEJIWHRNMmNqNzdqVFRNclFnTHhRNw==';
	
    useEffect(() => {
        fetchCountries();
    }, []);

    useEffect(() => {
        if (employeeRequestData.country) {
            fetchStates(employeeRequestData.country);
        } else {
            setStates([]);
            setCities([]);
        }
    }, [employeeRequestData.country]);

    useEffect(() => {
        if (employeeRequestData.country && employeeRequestData.state) {
            fetchCities(employeeRequestData.country, employeeRequestData.state);
        } else {
            setCities([]);
        }
    }, [employeeRequestData.state, employeeRequestData.country]);

    const fetchCountries = async () => {
        try {
            const response = await fetch("https://api.countrystatecity.in/v1/countries", {
                headers: {
                    "X-CSCAPI-KEY": API_KEY
                }
            });
            if (!response.ok) throw new Error(response.statusText);
            const data = await response.json();

            const india = data.find(country => country.iso2 === 'IN');

            if (india) {
                setCountries([india]);
            } else {
                setCountries([]);
            }

            // setCountries(data);
        } catch (error) {
            console.error("Error fetching countries:", error);
            setMessage('Error loading countries.');
            setMessageType('error');
        }
    };

    const fetchStates = async (countryIso2) => {
        try {
            const response = await fetch(`https://api.countrystatecity.in/v1/countries/${countryIso2}/states`, {
                headers: {
                    "X-CSCAPI-KEY": API_KEY
                }
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
                headers: {
                    "X-CSCAPI-KEY": API_KEY
                }
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
        setMessage('');
        setMessageType('');

        try {
            const response = await fetch('/employeeRequest/addEmployeeRequest', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(employeeRequestData),
            });

            if (response.ok) {
                setMessage('Employee created successfully!');
                setMessageType('success');
                setEmployeeRequestData({
                  email: '',
                  name: '',
                  phoneNumber: '',
                  gender: '',
                  empDepartment: '',
                  country: '',
                  state: '',
                  city: '',
               });
            } else {
                const data = await response.text();
                setMessage(data);
                setMessageType('error');
            }
        } catch (error) {
            console.error("Error creating employee:", error);
            setMessage('Error creating employee.');
            setMessageType('error');
        }
    };

  return (
    <div className="content">
      <div className="create-employee">
        <h2 className="form-title">Create Employee</h2>
        <form onSubmit={handleSubmit} className="employee-form">
          <div className="form-group">
            <label>Email:</label>
            <input type="text" name="email" value={employeeRequestData.email} onChange={handleChange} className="form-control" placeholder="Enter email" required maxLength={100}/>
          </div>

          <div className="form-group">
            <label>Name:</label>
            <input type="text" name="name" value={employeeRequestData.name} onChange={handleChange} className="form-control" placeholder="Enter name" required maxLength={20} />
          </div>

          <div className="form-group">
            <label>Phone Number:</label>
            <input type="text" name="phoneNumber" value={employeeRequestData.phoneNumber} onChange={handleChange} className="form-control" placeholder="Enter phone number" required maxLength={10} />
          </div>

          <div className="form-group">
            <label>Gender:</label>
            <select className="form-control" name="gender" onChange={handleChange} value={employeeRequestData.gender} required>
              <option value="">Select gender</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
            </select>
          </div>

           <div className="form-group">
            <label>Department:</label>
            <select className="form-control" name="empDepartment" onChange={handleChange} value={employeeRequestData.empDepartment} required>
              <option value="">Select department</option>
              <option value="hr">HR</option>
              <option value="development">Development</option>
              <option value="testing">Testing</option>
              <option value="management">Management</option>
              <option value="sales">Sales</option>
              <option value="marketing">Marketing</option>
            </select>
          </div>


          <div className="form-group">
            <label>Country:</label>
            <select className="form-control" name="country" onChange={handleChange} value={employeeRequestData.country} required>
              <option value="">Select country</option>
              {countries.map((country) => (
                 <option key={country.iso2} value={country.iso2}>{country.name}</option>
              ))} 
            </select>
          </div>

          <div className="form-group">
            <label>State:</label>
            <select className="form-control" name="state" onChange={handleChange} value={employeeRequestData.state} required>
              <option value="">Select state</option>
              {states.map((state) => (
                <option key={state.iso2} value={state.iso2}>{state.name}</option>
              ))}
            </select>
          </div>
          
          <div className="form-group">
            <label>City:</label>
            <select className="form-control" name="city" onChange={handleChange} value={employeeRequestData.city} required>
              <option value="">Select city</option>
              {cities.map((city) => (
                <option key={city.iso2} value={city.iso2}>{city.name}</option>
              ))}
            </select>
          </div>

           {message && (
            <div className="error" style={ messageType === 'error' ? { color: 'red', marginBottom: '10px', marginTop: '10px' } : { color: 'green', marginBottom: '10px', marginTop: '10px' }}>
              {message}
            </div>
          )}

          <div className="form-group-button">
            <button type="submit">Create</button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default CreateEmployee;
