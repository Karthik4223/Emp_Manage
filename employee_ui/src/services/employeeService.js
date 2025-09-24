import axios from "axios";

// const API_BASE_URL = "/employee";

// export const getAllEmployees = (token) => {
//   return axios.get(`${API_BASE_URL}/getAllEmployees`, {
//     headers: { Authorization: `Bearer ${token}` },
//   });
// };

// export const updateEmployeeStatus = (empCode, newStatus, token) => {
//   return axios.put(
//     `${API_BASE_URL}/updateEmpStatus/${empCode}?newStatus=${newStatus}&updatedBy=XYZ`,
//     {},
//     { headers: { Authorization: `Bearer ${token}` } }
//   );
// };

// export const searchEmployees = (payload, token) => {
//   return axios.post(`${API_BASE_URL}/search`, payload, {
//     headers: { Authorization: `Bearer ${token}` },
//   });
// };

// export const updateEmployee = async (employeeData, token) => {
//   try {
//     const response = await axios.put('/employee/updateEmployee', employeeData, {
//       headers: {
//         'Content-Type': 'application/json',
//         Authorization: `Bearer ${token}`
//       }
//     });
//     return response.data;
//   } catch (error) {
//     throw error.response?.data || error.message;
//   }
// };


import { useApi } from "./useApi";

export const useEmployeeService = () => {
  const api = useApi("/employee");

  const getAllEmployees = async () => {
    const response = await api.get("/getAllEmployees");
    return response.data;
  };

  const updateEmployeeStatus = async (empCode, newStatus) => {
    const response = await api.put(
      `/updateEmpStatus/${empCode}?newStatus=${newStatus}&updatedBy=XYZ`
    );
    return response.data;
  };

  const searchEmployees = async (payload,token) => {
    return axios.post(`/employee/search`, payload, {
        headers: { Authorization: `Bearer ${token}` },
    });
    };

  const updateEmployee = async (employeeData) => {
    const response = await api.put("/updateEmployee", employeeData);
    return response.data;
  };

  return { getAllEmployees, updateEmployeeStatus, searchEmployees, updateEmployee };
};
