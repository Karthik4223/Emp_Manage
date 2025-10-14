import { useApi } from "./useApi";

export const useEmployeeRequestService = () => {
  const api = useApi("/employeeRequest");

  const createEmployee = (data) => api.post("/addEmployeeRequest", data);
  const getAllEmployeeRequests = () => api.get("/getAllEmployeeRequests");
  const updateEmployeeRequestStatus = (empRequestId, action) =>
    api.put(
      `/updateEmployeeRequestStatus/${empRequestId}?newStatus=${action}`
    );

    const uploadEmployeeExcel = async (formData) => {
    return await api.post(`/upload-excel`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  };

  return { createEmployee, getAllEmployeeRequests, updateEmployeeRequestStatus, uploadEmployeeExcel };
};