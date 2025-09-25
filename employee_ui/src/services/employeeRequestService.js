import { useApi } from "./useApi";

export const useEmployeeRequestService = () => {
  const api = useApi("/employeeRequest");

  const createEmployee = (data) => api.post("/addEmployeeRequest", data);
  const getAllEmployeeRequests = () => api.get("/getAllEmployeeRequests");
  const updateEmployeeRequestStatus = (empRequestId, action, updatedBy) =>
    api.put(
      `/updateEmployeeRequestStatus/${empRequestId}?newStatus=${action}&updatedBy=${updatedBy}`
    );

    const uploadEmployeeExcel = async (formData, username) => {
    return await api.post(`/upload-excel?createdBy=${username}`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  };

  return { createEmployee, getAllEmployeeRequests, updateEmployeeRequestStatus, uploadEmployeeExcel };
};