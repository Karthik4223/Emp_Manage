import { useApi } from "./useApi";

export const useEmployeeRequestService = () => {
  const api = useApi("/employeeRequest");

  const createEmployee = (data) => api.post("/addEmployeeRequest", data);
  const getAllEmployeeRequests = () => api.get("/getAllEmployeeRequests");
  const updateEmployeeRequestStatus = (empRequestId, action) =>
    api.put(
      `/updateEmployeeRequestStatus/${empRequestId}?newStatus=${action}&updatedBy=XYZ`
    );

  return { createEmployee, getAllEmployeeRequests, updateEmployeeRequestStatus };
};
