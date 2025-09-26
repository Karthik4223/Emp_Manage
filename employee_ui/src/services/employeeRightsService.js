import { useApi } from "./useApi";

export const useEmployeeRightsService = () => {
  const api = useApi("/employeeRights");

  const getEmployeeRights = async (empCode) => {
    const response = await api.get(`/getEmployeeRights/${empCode}`);
    return response.data;
  };

  const assignEmployeeRights = async (empCode, rightCodes,group,username) => {
    const payload = { empCode, rightCode: rightCodes };
    const response = await api.post(`/addEmployeeRights?group=${group}&createdBy=${username}`, payload);
    return response.data;
  };

  return { getEmployeeRights, assignEmployeeRights };
};

