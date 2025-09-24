import { useApi } from "./useApi";

export const useRightsService = () => {
  const api = useApi("/rights");

  const createRight = async (rightData) => {
    const response = await api.post("/addRight", rightData);
    return response.data;
  };

  const getAllRights = async () => {
    const response = await api.get("/getAllRights");
    return response.data;
  };

  const updateRightStatus = async (rightCode, newStatus) => {
    const response = await api.put(`/updateRightStatus/${rightCode}?newStatus=${newStatus}`);
    return response.data;
  };

  return { createRight, getAllRights, updateRightStatus };
};