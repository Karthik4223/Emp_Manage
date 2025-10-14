import { useApi } from "./useApi";

export const useSessionService = () => {
  const api = useApi("/auth");

  const getSessionInfo = async () => {
    const response = await api.get("/sessionInfo");
    return response.data;
  };

  return { getSessionInfo };
};
