import { useApi } from "./useApi";

export const useEmployeeService = () => {
  const api = useApi("/employee");

  const getAllEmployees = async () => {
    const response = await api.get("/getAllEmployees");
    return response.data;
  };

  const updateEmployeeStatus = async (empCode, newStatus, updatedBy) => {
    const response = await api.put(
      `/updateEmpStatus/${empCode}?newStatus=${newStatus}&updatedBy=${updatedBy}`
    );
    return response.data;
  };

  const searchEmployees = async (payload,token) => {
    const response = await api.post("/search", payload);
    console.log(response);
    return response;
  };

  const updateEmployee = async (employeeData) => {
    const response = await api.put("/updateEmployee", employeeData);
    return response.data;
  };

  return { getAllEmployees, updateEmployeeStatus, searchEmployees, updateEmployee };
};
